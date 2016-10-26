package org.leo.rest.service;

import javax.servlet.http.HttpServletRequest;

import org.leo.exception.ServiceUnavailableException;
import org.springframework.web.context.WebApplicationContext;

public class SpringBeansHandler {

	private static SpringBeansHandler handler;
	private SpringBeansHandler(){}

	public static SpringBeansHandler getHandler(){
		if(handler==null)
			handler=new SpringBeansHandler();
		return SpringBeansHandler.handler;
	}

	protected WebApplicationContext getSpringContext(HttpServletRequest req){
		Object obj=req.getServletContext().getAttribute("org.springframework.web.context.WebApplicationContext.ROOT");
		if(obj!=null)
			return (WebApplicationContext)obj;
		return null;
	}

	protected LeoService getServiceBean(HttpServletRequest req,String beanName)throws Exception{
		try{
			LeoService service=(LeoService)getSpringContext(req).getBean(beanName);
			if(service==null){
				throw new ServiceUnavailableException(beanName+" named service is not available in spring ctx.");
			}
			return service;
		}catch(Exception e){
			throw new ServiceUnavailableException(beanName+" named service is not available in spring ctx.");
		}
	}
}
