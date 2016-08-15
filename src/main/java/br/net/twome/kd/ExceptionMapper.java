package br.net.twome.kd;

import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.Response.Status;
import javax.ws.rs.ext.Provider;

import org.mentabean.BeanException;

@Provider
public class ExceptionMapper implements javax.ws.rs.ext.ExceptionMapper<BeanException> {

	@Override
	public Response toResponse(BeanException exception) {
		return Response.status(Status.UNAUTHORIZED)
				.entity(new ResponseEntity(exception.getMessage()))
				.type(MediaType.APPLICATION_JSON)
				.build();
	}
	
	public static class ResponseEntity {
		
		private String error;
		
		public ResponseEntity() {}
		
		public ResponseEntity(String error) {
			this.error = error;
		}

		public String getError() {
			return error;
		}

		public void setError(String error) {
			this.error = error;
		}
		
	}

}
