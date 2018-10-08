package content.service;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.sql.Array;
import java.sql.CallableStatement;
import java.sql.Connection;
import java.sql.SQLException;
import java.sql.Types;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Base64;
import java.util.List;
import java.util.stream.Collectors;

import javax.annotation.Resource;
import javax.annotation.sql.DataSourceDefinition;
import javax.enterprise.context.ApplicationScoped;
import javax.enterprise.inject.Produces;
import javax.inject.Inject;
import javax.persistence.EntityManager;
import javax.persistence.ParameterMode;
import javax.persistence.PersistenceContext;
import javax.persistence.StoredProcedureQuery;
import javax.sql.DataSource;

import org.eclipse.microprofile.config.inject.ConfigProperty;

import content.entities.DirectoryContent;
import content.entities.FileContent;
import content.util.HushHush;
import oracle.jdbc.OracleTypes;
import oracle.jdbc.driver.OracleConnection;
import oracle.jdbc.pool.OracleDataSource;
import oracle.jdbc.OracleCallableStatement;

@ApplicationScoped
public class FileService {
	// @Inject
	// JPA is not provided out of the box, but most providers support it at
	// some level. worst case, create your own producer for the field
	@Produces
	@PersistenceContext(unitName = "ContentModel")
	private EntityManager entityManager;

	@Resource(lookup = "jdbc/APPUSERDS")
	DataSource dataSource;

	@Inject
	// use configuration to control how much data you want to supply at
	// a given time
	@ConfigProperty(name = "max.per.page", defaultValue = "20")
	private int maxResults;

	public List<FileContent> getAll() {
		return entityManager.createNamedQuery("FileContent.findAll", FileContent.class).setMaxResults(maxResults)
				.getResultList();
	}

	public FileContent getFile(String guid) {
		return entityManager.createNamedQuery("FileContent.findByGuid", FileContent.class).setParameter("guid", guid)
				.getSingleResult();
	}

	public DirectoryContent getDirectory(String base64Encoded) {
		// Decode
		byte[] asBytes = Base64.getDecoder().decode(base64Encoded);
		String folderPath = new String(asBytes, StandardCharsets.UTF_8);
		System.out.println("Base64 decoded text: " + folderPath);

		List<String> files = getDirFiles(folderPath);
		List<String> folders = getDirFolders(folderPath);

		return new DirectoryContent(folderPath, files, folders);
	}

	public List<String> getDirFiles(String dir) {
		List<FileContent> list = entityManager.createNamedQuery("FileContent.findByPath", FileContent.class)
				.setParameter("folder", dir).getResultList();

		return list.stream().map(FileContent::getName).collect(Collectors.toList());
	}

	public List<String> getDirFolders(String folder) {
		List<String> result = new ArrayList<>();

		try (Connection connection = dataSource.getConnection()) {
			CallableStatement statement = connection.prepareCall("call EM_CONTENT.list_folders(?,?)");
			statement.setString(1, folder);
			System.out.println("register");
			statement.registerOutParameter(2, Types.ARRAY, "EM_CONTENT.STRING_LIST_T");

			System.out.println("now executing");
			statement.execute();

			System.out.println("get result");
			Array a = statement.getArray(2);
			System.out.println("array " + a);

			String[] recArray = (String[]) (a.getArray());
			for (String item : recArray) {
				if (item != null && !item.isEmpty()) {
					result.add(folder + "/" + item);
				}
			}
			a.free();
			statement.close();
		} catch (SQLException e) {
			e.printStackTrace();
		}

		return result;

	}
}
