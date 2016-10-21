package org.leo.rest.service;

import java.util.Map;

public class LeoService {

	private ServiceContext ctx;
	
	{
		ctx=new ServiceContext();
	}
	
	public Map getParameterMap(){
		return ctx.getCloneParameters();
	}
	
}
