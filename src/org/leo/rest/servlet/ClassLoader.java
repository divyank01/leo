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
		if(!TemplateCollector.isLoaded()){
			File file=new File(Thread.currentThread()
					.getContextClassLoader()
					.getResource(packageName.replaceAll("\\.", Character.toString(File.separatorChar)).trim())
					.getFile());
			for(File f:file.listFiles()){
				if(!f.isHidden() && f.getName().endsWith(".class")){
					try {
						Class clzz=Class.forName(packageName+"."+(f.getName().replaceAll("\\.class", "")));
						_loadAnnotations(clzz);
					} catch (ClassNotFoundException e) {
						e.printStackTrace();
					}
				}
			}
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
