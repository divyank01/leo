package org.leo.rest.service;

import java.util.HashMap;
import java.util.Map;

public class ServiceContext {

	private HashMap<? extends Number, Object> serviceParm=new HashMap<>();
	
	protected Map<? extends Number, Object> getCloneParameters(){
		return (Map<? extends Number, Object>) this.serviceParm.clone();
	}
	
	protected Map<? extends Number, Object> getParameters(){
		return this.serviceParm;
	}
	
	protected void setParams(Map m){
		serviceParm=(HashMap<? extends Number, Object>) m;
	}
}
