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
