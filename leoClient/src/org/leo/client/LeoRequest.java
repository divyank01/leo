package org.leo.client;

import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;

public class LeoRequest {

	private String method;
	private String url;
	private Map<String,String> attributeMap=new HashMap<>();
	private Map<String,String> header=new HashMap<>();
	private Serializable transferObject;
	
	public String getMethod() {
		return method;
	}
	public void setMethod(String method) {
		this.method = method;
	}
	public String getUrl() {
		return url;
	}
	public void setUrl(String url) {
		this.url = url;
	}
	public Map<String, String> getAttributeMap() {
		return attributeMap;
	}
	public void setAttributeMap(Map<String, String> attributeMap) {
		this.attributeMap = attributeMap;
	}
	public Map<String, String> getHeader() {
		return header;
	}
	public void setHeader(Map<String, String> header) {
		this.header = header;
	}
	public Serializable getTransferObject() {
		return transferObject;
	}
	public void setTransferObject(Serializable transferObject) {
		this.transferObject = transferObject;
	}
	
}
