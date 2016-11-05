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
package org.leo.rest.template;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public class TemplateCollector {

	private static Map<String,Template> template=new ConcurrentHashMap<String,Template>();
	private static TTree mapping=new TTree("/");
	public static void loadTemplate(Template template){
		TemplateCollector.template.put(template.getServiceName(), template);
	}
	
	public static void addMapping(String url,String methodName){
		TemplateCollector.mapping.add(url, methodName);
	}
	
	public static String getMethod(String url){
		return TemplateCollector.mapping.getMethod(url);
	}
	
	public static boolean isLoaded(){
		return template.size()>0;
	}
	
	public static Template getTemplate(String serviceName){
		return template.get(serviceName);
	}
	
}
