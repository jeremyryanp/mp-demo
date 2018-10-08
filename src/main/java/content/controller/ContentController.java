package content.controller;

import java.util.List;

import javax.enterprise.context.RequestScoped;
import javax.inject.Inject;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.Response.ResponseBuilder;

import org.eclipse.microprofile.metrics.annotation.Counted;

import com.google.gson.Gson;

import content.entities.DirectoryContent;
import content.entities.FileContent;
import content.service.FileService;
import content.util.ResponseUtils;

@Path("/content") // just a basic JAX-RS resource
@Counted // track the number of times this endpoint is invoked
@RequestScoped
public class ContentController {
	@Inject
	private FileService fileService;

	@GET
	@Path("/ping")
	public Response ping() {
		return Response.ok().entity("Content service online").build();
	}

//	 @RolesAllowed("admin")
	@GET
	@Path("/file/all")
	@Produces(MediaType.APPLICATION_JSON)
	// uses common annotations to declare a role required
	public List<FileContent> findAll() {
		return fileService.getAll();
	}

//	 @RolesAllowed("admin")
	@GET
	@Path("/file/limit={limit}&offset={offset}")
	@Produces(MediaType.APPLICATION_JSON)
	// uses common annotations to declare a role required
	public Response findPaginated(@PathParam("limit") int limit, @PathParam("offset") int offset) {
		List<FileContent> result = fileService.getAll();
		int start, end;
		Response response;
		start = offset * limit;
		end = start + limit;

		if (end > result.size()) {
			end = result.size();
		}

		Gson gson = new Gson();

		String filesJson = gson.toJson(result.subList(start, end));

		filesJson = "{\"files\":" + filesJson + ",\"filesCount\":\"" + result.size() + "\"}";

		ResponseBuilder rb = Response.ok().entity(filesJson);

		return ResponseUtils.fixResponse(rb);
	}
	
	@GET
	@Path("/file/guid={guid}")
	@Produces(MediaType.APPLICATION_JSON)
	// uses common annotations to declare a role required
	public Response findFileByGuid(@PathParam("guid") String guid) {
		FileContent result = fileService.getFile(guid);

		Gson gson = new Gson();

		String filesJson = gson.toJson(result);

		ResponseBuilder rb = Response.ok().entity(filesJson);

		return ResponseUtils.fixResponse(rb);
	}
	
	@GET
	@Path("/path={path}")
	@Produces(MediaType.APPLICATION_JSON)
	public DirectoryContent getDirContents(@PathParam("path") String path) {
		
		System.out.println("\n\n\n\nGot path:" + path + "\n\n\n\n");
		
		return fileService.getDirectory(path);
	}
	
}
