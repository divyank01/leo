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
import java.lang.reflect.Field;
import java.lang.reflect.Method;

import org.leo.exception.LeoExceptions;
import org.leo.rest.annotations.Get;
import org.leo.rest.annotations.Path;
import org.leo.rest.annotations.Post;
import org.leo.rest.annotations.Service;
import org.leo.rest.template.Template;
import org.leo.rest.template.TemplateCollector;

//import com.thoughtworks.xstream.XStream;

public class ClassLoader {

	private static ClassLoader loader;
	private static final String CLASS=".class";
	private static final String DOT="\\.";
	private static final String GET="GET";
	private static final String POST="POST";
	private static final String _DOT=".";
	private static final String DOT_CLASS="\\.class";
	private static final String SLASH="/";
	private static final String LOAD_ERR="Unable to load services from package ";

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
						.getResource(packageName.replaceAll(DOT, Character.toString(File.separatorChar)).trim())
						.getFile());
				for(File f:file.listFiles()){
					if(!f.isHidden() && f.getName().endsWith(CLASS)){
						Class clzz=Class.forName(packageName+_DOT+(f.getName().replaceAll(DOT_CLASS, "")));
						_loadAnnotations(clzz);
					}
				}
			}
		} catch(LeoExceptions e){
			e.printStackTrace();
		}catch (Exception e) {
			Exception ex=new Exception(LOAD_ERR+packageName);
			ex.setStackTrace(e.getStackTrace());
			ex.printStackTrace();
		}
	}

	private void _loadAnnotations(Class clzz) throws Exception {
		Annotation[] _aClass=clzz.getAnnotations();
		Template template=null;
		String serviceName=SLASH;
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
					String method=null;
					Path p=null;
					for(Annotation a:_mClass){
						if(a instanceof Path){
							template.getMapping().put(((Path)a).urlPath(), m.getName());
							p=(Path)a;
						}if(a instanceof Get){
							method=GET;
						}if(a instanceof Post)
							method=POST;
						if(method==null){
							throw new LeoExceptions("HTTP service method not available for method "+m.getName());
						}
					}
					TemplateCollector.addMapping(SLASH+method+serviceName+p.urlPath(), m.getName());
				}
			}
		}
	}

}
