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
