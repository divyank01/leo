package org.leo.client;

import java.io.InputStream;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class LeoResponse {

	private int statusCode;
	private String json;
	private Map<String,List<String>> header=new HashMap<>();
	private InputStream inputStream;
	public int getStatusCode() {
		return statusCode;
	}
	public void setStatusCode(int statusCode) {
		this.statusCode = statusCode;
	}
	public String getJson() {
		return json;
	}
	public void setJson(String json) {
		this.json = json;
	}
	public Map<String, List<String>> getHeader() {
		return header;
	}
	/*public void setHeader(Map<String, List<String>> header) {
		this.header = header;
	}*/
	public InputStream getInputStream() {
		return inputStream;
	}
	public void setInputStream(InputStream inputStream) {
		this.inputStream = inputStream;
	}
	
}
