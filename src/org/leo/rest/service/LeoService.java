package org.leo.rest.service;

import java.util.Map;

public class LeoService {

	private ServiceContext ctx;
	
	{
		ctx=new ServiceContext();
	}
	
	/**
	 * After decoding uri for service methods remaining uri converted to hashmap based on index
	 * @return map containing parameters
	 */
	public Map<? extends Number, String> getParameterMap(){
		return ctx.getCloneParameters();
	}
	
	
	/**
	 * This returns parameter appended in the urls
	 * @return Map containing parameters
	 */
	public Map<String,String[]> getParams(){
		return ctx.getCloneAppendedParams();
	}
	
	protected void updateServiceContext(ServiceExecutor exec){
		this.ctx.setParams(exec.getServiceParams());
		this.ctx.setAppendedParams(exec.getParameterMap());
	}
	
}
