package org.leo.rest.template;

import java.util.HashMap;
import java.util.Map;

public class Template {

	private String className;
	private String serviceName;
	private Map<String, String> mapping=new HashMap<String,String>();
	
	public Template(String className, String serviceName) {
		this.className=className;
		this.serviceName=serviceName;
	}
	public Map<String, String> getMapping() {
		return mapping;
	}
	public void setMapping(Map<String, String> mapping) {
		this.mapping = mapping;
	}
	public String getClassName() {
		return className;
	}
	public void setClassName(String className) {
		this.className = className;
	}
	
	public String getServiceName() {
		return serviceName;
	}
	public void setServiceName(String serviceName) {
		this.serviceName = serviceName;
	}
}
