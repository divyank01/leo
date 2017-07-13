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

import javax.servlet.http.HttpServletRequest;

import org.leo.exception.ServiceUnavailableException;

public class SpringBeansHandler {

	private static SpringBeansHandler handler;
	private static final String CLASS="org.springframework.web.context.WebApplicationContext.ROOT";
	private static final String METHOD="getBean";
	private static final String MESSAGE=" named service is not available in spring ctx.";
	private SpringBeansHandler(){}

	public static SpringBeansHandler getHandler(){
		if(handler==null)
			handler=new SpringBeansHandler();
		return SpringBeansHandler.handler;
	}
	/**
	 * Will return WebApplicationContext
	 * @param req
	 * @return
	 */
	protected Object getSpringContext(HttpServletRequest req){
		return req.getServletContext().getAttribute(CLASS);
	}
	/**
	 * This method will get service bean from spring context
	 * @param req
	 * @param beanName
	 * @return
	 * @throws Exception
	 */
	protected LeoService getServiceBean(HttpServletRequest req,String beanName)throws Exception{
		try{
			Object webCtx=getSpringContext(req);
			Method m=webCtx.getClass().getMethod(METHOD, String.class);
			LeoService service=(LeoService)m.invoke(webCtx, beanName);
			if(service==null){
				throw new ServiceUnavailableException(beanName+MESSAGE);
			}
			return service;
		}catch(Exception e){
			throw new ServiceUnavailableException(beanName+MESSAGE);
		}
	}
}
