package org.leo.rest.service;

import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.Map;
import java.util.StringTokenizer;

import javax.naming.ServiceUnavailableException;

import org.leo.rest.template.Template;
import org.leo.rest.template.TemplateCollector;


public class ServiceExecutor {

	private static ServiceExecutor executor;

	private Map serviceParams=null;

	protected Map getServiceParams() {
		return serviceParams;
	}

	private ServiceExecutor(){}

	protected static ServiceExecutor getExecuter(){
		if(executor==null)
			executor=new ServiceExecutor();
		return executor;
	}

	protected Object execute(String[] serviceDetails)throws Exception{
		Template template=null;
		template=TemplateCollector.getTemplate(serviceDetails[0]);
		if(template!=null)
			return _execute(template,serviceDetails[1]);
		throw new ServiceUnavailableException("template not found for service "+serviceDetails[0]);
	}

	private Object _execute(Template template,String mappingUrl)throws Exception{
		serviceParams=new HashMap<>();
		Class service=Class.forName(template.getClassName());
		String[] data=TemplateCollector.getMethod(mappingUrl).split(":");
		LeoService instance=(LeoService)service.newInstance();//make a service bean factory
		if(data[0]!=null){
			Method method=service.getDeclaredMethod(data[0],null);
			StringTokenizer tokenizer=new StringTokenizer(mappingUrl, "/");
			int count=0;
			int i=0;
			while(tokenizer.hasMoreTokens()){
				count++;
				String token=tokenizer.nextToken();
				if(count>new Integer(data[1])){
					serviceParams.put(i, token);
					i++;
				}
			}
			instance.updateServiceContext(this);
			return method.invoke(instance, null);
		}
		throw new ServiceUnavailableException("service is not available for url "+mappingUrl);
	}
}
