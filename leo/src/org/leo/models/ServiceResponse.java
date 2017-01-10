package org.leo.models;

import org.leo.serializer.LeoSerializable;

public class ServiceResponse implements LeoSerializable{

	private Header header;
	private Response body;
	
	public ServiceResponse(Object message,int status){
		this.header=new Header(status);
		this.body=new Response(message);
	}
	
	public ServiceResponse(int status){
		this.header=new Header(status);
	}
	
	public ServiceResponse(Header header,Response resp){
		this.header=header;
		this.body=resp;
	}
	
	public static class Header implements LeoSerializable{
		private int status;
		
		public Header(int status) {
			super();
			this.status = status;
		}

		public int getStatus() {
			return status;
		}

		public void setStatus(int status) {
			this.status = status;
		}
	}
	
	public static class Response implements LeoSerializable{
		private Object data;
		
		public Response(Object resp) {
			super();
			this.data = resp;
		}

		public Object getData() {
			return data;
		}

		public void setData(Object resp) {
			this.data = resp;
		}
	}

	public Header getHeader() {
		return header;
	}

	public void setHeader(Header header) {
		this.header = header;
	}

	public Response getBody() {
		return body;
	}

	public void setBody(Response response) {
		this.body = response;
	}
}
