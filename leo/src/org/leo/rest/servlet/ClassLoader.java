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
package org.leo.rest.servlet;

import java.io.File;
import java.lang.annotation.Annotation;
import java.lang.reflect.Method;

import org.leo.rest.annotations.Path;
import org.leo.rest.annotations.Service;
import org.leo.rest.template.Template;
import org.leo.rest.template.TemplateCollector;

public class ClassLoader {

	private static ClassLoader loader;

	private ClassLoader(){}

	protected static ClassLoader getInstance(){
		if(loader==null)
			loader=new ClassLoader();
		return ClassLoader.loader;
	}

	public void loadTemplates(String packageName){
		try {
			if(!TemplateCollector.isLoaded()){
				File file=new File(Thread.currentThread()
						.getContextClassLoader()
						.getResource(packageName.replaceAll("\\.", Character.toString(File.separatorChar)).trim())
						.getFile());
				for(File f:file.listFiles()){
					if(!f.isHidden() && f.getName().endsWith(".class")){
						Class clzz=Class.forName(packageName+"."+(f.getName().replaceAll("\\.class", "")));
						_loadAnnotations(clzz);
					}
				}
			}
		} catch (Exception e) {
			Exception ex=new Exception("Unable to load services from package "+packageName);
			ex.setStackTrace(e.getStackTrace());
			ex.printStackTrace();
		}
	}

	private void _loadAnnotations(Class clzz) {
		Annotation[] _aClass=clzz.getAnnotations();
		Template template=null;
		String serviceName="/";
		boolean isService=false;;
		for(Annotation a:_aClass){
			if(a instanceof Service){
				isService=true;
				template=new Template(clzz.getCanonicalName(),((Service)a).name());
				serviceName=serviceName+((Service)a).name();
				TemplateCollector.loadTemplate(template);
			}
		}
		if(isService){
			for(Method m:clzz.getDeclaredMethods()){
				Annotation[] _mClass=m.getAnnotations();
				if(_mClass!=null){
					for(Annotation a:_mClass){
						if(a instanceof Path){
							template.getMapping().put(((Path)a).urlPath(), m.getName());
							TemplateCollector.addMapping(serviceName+"/"+((Path)a).urlPath(), m.getName());
						}
					}
				}
			}
		}
	}

}
