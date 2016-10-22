package org.leo.rest.service;

import java.lang.reflect.Method;

import org.leo.rest.template.Template;
import org.leo.rest.template.TemplateCollector;

public class ServiceExecutor {

	private static ServiceExecutor executor;
	
	private ServiceExecutor(){}
	
	protected static ServiceExecutor getExecuter(){
		if(executor==null)
			executor=new ServiceExecutor();
		return executor;
	}
	
	protected Object execute(String[] serviceDetails)throws Exception{
		Template template=null;
		template=TemplateCollector.getTemplate(serviceDetails[0]);
		return _execute(template,serviceDetails[1]);
	}

	private Object _execute(Template template,String mappingUrl)throws Exception{
		Class service=Class.forName(template.getClassName());
		int depth=0;
		Method method=service.getDeclaredMethod(TemplateCollector.getMethod(mappingUrl,depth),null);
		System.out.println(depth+"---------------------");
		LeoService instance=(LeoService)service.newInstance();//make a service bean factory
		//instance.getParameterMap().put(key, value)
		return method.invoke(instance, null);
	}
}
