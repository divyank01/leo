package org.leo.rest.service;

import java.lang.reflect.Method;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.Map;
import java.util.StringTokenizer;

import javax.naming.ServiceUnavailableException;
import javax.servlet.http.HttpServletRequest;

import org.leo.rest.template.Template;
import org.leo.rest.template.TemplateCollector;

public class ServiceExecutor {

	private static ServiceExecutor executor;

	private Map serviceParams=null;
	private Map<String,String[]> aParam=null;
	SpringBeansHandler springHandler=SpringBeansHandler.getHandler();

	protected Map getServiceParams() {
		return serviceParams;
	}

	protected Map<String,String[]> getParameterMap(){
		return this.aParam;
	}
	private ServiceExecutor(){}

	protected static ServiceExecutor getExecuter(){
		if(executor==null)
			executor=new ServiceExecutor();
		return executor;
	}

	protected Object execute(HttpServletRequest req)throws Exception{
		Template template=null;
		String[] serviceDetails=getServiceDetails(req);
		template=TemplateCollector.getTemplate(serviceDetails[0]);
		if(template!=null)
			return _execute(template,serviceDetails[1],req);
		throw new ServiceUnavailableException("template not found for service "+serviceDetails[0]);
	}

	private Object _execute(Template template,String mappingUrl,HttpServletRequest req)throws Exception{
		this.serviceParams=new HashMap<>();
		this.aParam=new HashMap<>();
		
		String[] data=TemplateCollector.getMethod(mappingUrl).split(":");
		if(data[0]!=null && !data[0].equals("null") && !data[0].isEmpty()){
			StringTokenizer tokenizer=new StringTokenizer(mappingUrl, "/");
			int count=0;
			int i=0;
			Class service=null;
			LeoService instance=null;
			if(!isSpringCtxAvailable(req)){
				service=Class.forName(template.getClassName());
				instance=(LeoService)service.newInstance();//make a service bean factory
			}else{
				instance=springHandler.getServiceBean(req, template.getServiceName());
				service=instance.getClass();
			}
			Method method=service.getDeclaredMethod(data[0],null);
			while(tokenizer.hasMoreTokens()){
				count++;
				String token=tokenizer.nextToken();
				if(count>new Integer(data[1])){
					this.serviceParams.put(i, token);
					i++;
				}
			}
			this.aParam=req.getParameterMap();
			instance.updateServiceContext(this);
			return method.invoke(instance, null);
		}
		throw new ServiceUnavailableException("No such service available!! \npath: "+mappingUrl);
	}
	
	private String[] getServiceDetails(HttpServletRequest req){
		String releventPath=req.getPathInfo();
		String temp=releventPath.replaceFirst("/", "");
		String serviceName=temp.substring(0, temp.indexOf("/"));
		String[] service={serviceName,releventPath};
		return service;
	}
	
	private boolean isSpringCtxAvailable(HttpServletRequest req) {
		return req.getServletContext().getAttribute("org.springframework.web.context.WebApplicationContext.ROOT")==null?false:true;
	}
}
