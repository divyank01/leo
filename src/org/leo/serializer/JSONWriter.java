package org.leo.serializer;

import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.Arrays;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

public class JSONWriter {

	private StringBuilder buf=new StringBuilder();

	private String getString(){
		return buf.toString();
	}

	public String getJson(Object obj) throws Exception{
		makeJson(buf, obj);
		return getString();
	}

	private void makeJson(StringBuilder builder,Object obj) throws Exception{
		builder.append("{");
		int counter=0;
		Field[] list=obj.getClass().getDeclaredFields();
		for(Field field:list){
			builder.append("\"");
			builder.append(field.getName());
			builder.append("\":");
			getValueForObject(obj.getClass().getMethod(getterName(field.getName()), null),obj,builder);
			counter++;
			if(counter!=list.length)
				builder.append(",");
		}
		builder.append("}");
	}

	private void getValueForObject(Method method, Object obj1, StringBuilder builder) throws Exception{
		Object obj=method.invoke(obj1, null);
		if(obj!=null){
			if(obj instanceof Number || obj.getClass().isPrimitive()){
				builder.append(obj);
			}else if(obj.getClass().isArray()){
				builder.append("[");
				makeFor(builder, Arrays.asList(obj).iterator());
				builder.append("]");
			}else if(obj instanceof List){
				builder.append("[");
				makeFor(builder, ((List)obj).iterator());
				builder.append("]");
			}else if(obj instanceof Map){
				Map map=(Map)obj;
				Iterator itr=map.keySet().iterator();
				builder.append("{");
				while(itr.hasNext()){
					Object key=itr.next();
					Object o=map.get(key);
					if(o instanceof Number)
						builder.append(o);
					else{
						builder.append("\"");
						builder.append(key);
						builder.append("\":");
						builder.append("\""+o+"\"");
						//makeJson(builder, o);
					}
					if(itr.hasNext())
						builder.append(",");
				}
				builder.append("}");
			}else if(obj instanceof String){
				builder.append("\"");
				builder.append(obj);
				builder.append("\"");
			}else{
				makeJson(builder, obj);
			}
		}else
			builder.append("null");
	}

	private void makeFor(StringBuilder builder,Iterator itr) throws Exception{
		while(itr.hasNext()){
			makeJson(builder, itr.next());
			if(itr.hasNext())
				builder.append(",");
		}
	}

	private String getterName(String f){
		return "get"+f.substring(0, 1).toUpperCase()+f.substring(1);
	}

}
