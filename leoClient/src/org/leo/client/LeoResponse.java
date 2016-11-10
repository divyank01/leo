package org.leo.client;

import java.io.InputStream;
import java.util.HashMap;
import java.util.Map;

public class LeoResponse {

	private int statusCode;
	private String json;
	private Map<String,String> header=new HashMap<>();
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
	public Map<String, String> getHeader() {
		return header;
	}
	public void setHeader(Map<String, String> header) {
		this.header = header;
	}
	public InputStream getInputStream() {
		return inputStream;
	}
	public void setInputStream(InputStream inputStream) {
		this.inputStream = inputStream;
	}
	
}
