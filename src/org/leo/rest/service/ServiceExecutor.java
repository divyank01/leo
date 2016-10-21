package org.leo.rest.service;

import java.lang.reflect.Method;
import java.util.StringTokenizer;

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
		StringTokenizer tokenizer=new StringTokenizer(serviceDetails[1], "/");	
		template=TemplateCollector.getTemplate(serviceDetails[0]);
		System.out.println(serviceDetails[0]+"   "+template);
		System.out.println(serviceDetails[1]);
		System.out.println("method ka naam "+TemplateCollector.getMethod(serviceDetails[1]));
		System.out.println("service ka name "+template.getServiceName());
		return _execute(template,serviceDetails[1]);
	}

	private Object _execute(Template template,String mappingUrl)throws Exception{
		Class service=Class.forName(template.getClassName());
		Method method=service.getDeclaredMethod(TemplateCollector.getMethod(mappingUrl),null);
		Object instance=service.newInstance();//make a service bean factory
		return method.invoke(instance, null);
	}
}
