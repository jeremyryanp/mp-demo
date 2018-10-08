package content.util;

import javax.json.Json;
import javax.json.JsonObjectBuilder;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.Response.ResponseBuilder;

public class ResponseUtils {
	public static final String HOST_URL = "http://localhost:3000";
	
	public static Response fixResponse (ResponseBuilder rb) {
		rb.header("Access-Control-Allow-Origin", HOST_URL);
		return rb.build();
	}
	
	public static Response returnError () {
/*{"errors":{"body": ["can't be empty"]}}*/
		  JsonObjectBuilder objectBuilder = Json.createObjectBuilder();
		  objectBuilder.add("body", "[\"can't be empty\"]");
		  JsonObjectBuilder jb = Json.createObjectBuilder().add("errors", objectBuilder.build());
		//JSONObject jsonObj = new JSONObject("{\"errors\":{\"body\": [\"can't be empty\"]}}");
			
		  return fixResponse(Response.ok().entity(jb));
	}
}
