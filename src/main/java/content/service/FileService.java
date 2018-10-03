package content.service;

import java.util.List;

import javax.enterprise.context.ApplicationScoped;
import javax.enterprise.inject.Produces;
import javax.inject.Inject;
import javax.persistence.EntityManager;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.PersistenceContext;

import org.eclipse.microprofile.config.inject.ConfigProperty;

import content.entities.FileContent;

@ApplicationScoped
public class FileService  {
	//@Inject
	// JPA is not provided out of the box, but most providers support it at
	// some level. worst case, create your own producer for the field
	@Produces
    @PersistenceContext(unitName = "ContentModel")
	private EntityManager entityManager;

	@Inject
	// use configuration to control how much data you want to supply at
	// a given time
	@ConfigProperty(name = "max.per.page", defaultValue = "20")
	private int maxResults;

	public List<FileContent> getAll() {

		return entityManager.createNamedQuery("FileContent.findAll", FileContent.class)
				.setMaxResults(maxResults) // use that configuration to do a paginated look up
				.getResultList();
	}
	
	public FileContent getFile(String guid) {
		return entityManager.createNamedQuery("FileContent.findByGuid", FileContent.class)
				.setParameter("guid", guid)
				.getSingleResult();
	}
	
	//see contentfacadebeanimpl for more code
}
