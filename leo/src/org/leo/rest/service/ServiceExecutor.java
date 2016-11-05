/**
  *  Leo is an open source framework for REST APIs.
  *
  *  Copyright (C) 2016  @author Divyank Sharma
  *
  *  This program is free software: you can redistribute it and/or modify
  *  it under the terms of the GNU General Public License as published by
  *  the Free Software Foundation, either version 3 of the License, or
  *  (at your option) any later version.
  *
  *  This program is distributed in the hope that it will be useful,
  *  but WITHOUT ANY WARRANTY; without even the implied warranty of
  *  MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
  *  GNU General Public License for more details.
  *
  *  You should have received a copy of the GNU General Public License
  *  along with this program.  If not, see <http://www.gnu.org/licenses/>.
  *  
  *  For details please read LICENSE file.
  *  
  *  In Addition to it if you find any bugs or encounter any issue you need to notify me.
  *  I appreciate any suggestions to improve it.
  *  @mailto: divyank01@gmail.com
  */
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
