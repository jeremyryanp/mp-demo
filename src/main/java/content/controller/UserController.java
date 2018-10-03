package content.controller;

import javax.enterprise.context.RequestScoped;
import javax.json.Json;
import javax.json.JsonObject;
import javax.json.JsonObjectBuilder;
import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.Response.ResponseBuilder;

import org.eclipse.microprofile.metrics.annotation.Counted;

import content.Json.LoginPost;
import content.util.ResponseUtils;

@Path("/user") // just a basic JAX-RS resource
@Counted // track the number of times this endpoint is invoked
@RequestScoped
public class UserController {
	@GET
	@Path("/ping")
	public Response ping() {
		return Response.ok().entity("User service online").build();
	}

	@GET
	@Produces("application/json; charset=utf-8")
	public Response getUser() {
		ResponseBuilder rb = Response.ok().entity(getUserAsJsonObject());
		
		return ResponseUtils.fixResponse(rb);
	}

	public JsonObject getUserAsJsonObject(){
		  JsonObjectBuilder objectBuilder = Json.createObjectBuilder();
		  objectBuilder.add("email", "jake@gmail.com");
		  objectBuilder.add("token", "eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9");
		  objectBuilder.add("username", "jakeJake");
		  objectBuilder.add("bio", "my name is. my name is jake.");
		  objectBuilder.add("image", JsonObject.NULL);
		  return Json.createObjectBuilder().add("user", objectBuilder.build()).build();
//		  return objectBuilder.build();
	}
	
	@POST
	@Path("/login")
	@Produces(MediaType.APPLICATION_JSON)
	@Consumes(MediaType.APPLICATION_JSON)
	public Response login(final LoginPost input) {
		
		if (input.getUser().getUin() != null && input.getUser().getPassword() != null) {
			ResponseBuilder rb = Response.ok().entity(getUserAsJsonObject());
			
			return ResponseUtils.fixResponse(rb);
		} else {
			return ResponseUtils.returnError();
		}
	}
}
