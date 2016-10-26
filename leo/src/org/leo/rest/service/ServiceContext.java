package org.leo.rest.service;

import java.util.HashMap;
import java.util.Map;

public class ServiceContext {

	private HashMap<? extends Number, String> serviceParm=new HashMap<>();
	private HashMap<String,String[]> appendedParm=new HashMap<>();
	
	protected Map<? extends Number, String> getCloneParameters(){
		return (Map<? extends Number, String>) this.serviceParm.clone();
	}
	
	protected HashMap<String,String[]> getCloneAppendedParams(){
		return (HashMap<String,String[]>) this.appendedParm.clone();
	}
	
	protected Map<? extends Number, String> getParameters(){
		return this.serviceParm;
	}
	
	protected void setParams(Map m){
		serviceParm=(HashMap<? extends Number, String>) m;
	}

	protected void setAppendedParams(Map<String, String[]> parameterMap) {
		this.appendedParm=(HashMap<String, String[]>) parameterMap;
	}
}
