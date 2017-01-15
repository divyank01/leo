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
package org.leo.serializer;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.Arrays;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.leo.exception.LeoSerializationException;
import org.leo.rest.annotations.Serializable;

import com.pojos.Prodcty;

public class JSONWriter {

	private StringBuilder buf=null;
	private Set objects=null;

	private static final String TERMINATE="*****";
	
	private String getString(){
		return buf.toString();
	}

	public String getJson(Object obj) throws Exception{
		buf=new StringBuilder();
		objects=new HashSet<>();
		return _json(obj);
	}

	private String _json(Object obj) throws Exception{
		makeJson(buf, obj);
		return getString();
	}

	private void makeJson(StringBuilder builder,Object obj) throws Exception{
		String fieldName=null;
		try{
			if(obj instanceof Map){
				makeJsonForMap(obj, builder);
			}else if(obj instanceof List){
				builder.append("[");
				List l=(List)obj;
				int i=0;
				for(Object o:l){
					int counter=0;
					builder.append("{");
					Field[] list=o.getClass().getDeclaredFields();
					for(Field field:list){
						builder.append("\"");
						builder.append(field.getName());
						builder.append("\":");
						fieldName=field.getName();
						getValueForObject(o.getClass().getMethod(getterName(field.getName()), null),o,builder);
						counter++;
						if(counter!=list.length)
							builder.append(",");
					}
					builder.append("}");
					i++;
					if(i!=l.size())
						builder.append(",");	
				}
				builder.append("]");
			}else if(obj instanceof Number || obj instanceof String){
				builder.append("{value:\""+obj+"\"}");
			}else if(obj instanceof LeoSerializable || obj.getClass().getAnnotation(Serializable.class)!=null){
				builder.append("{");
				int counter=0;
				/*if(!objects.contains(obj)){
					objects.add(obj);*/
					Field[] list=obj.getClass().getDeclaredFields();
					for(Field field:list){
						builder.append("\"");
						builder.append(field.getName());
						builder.append("\":");
						fieldName=field.getName();
						getValueForObject(obj.getClass().getMethod(getterName(field.getName()), null),obj,builder);
						counter++;
						if(counter!=list.length)
							builder.append(",");
					}
				//}
				builder.append("}");
			}
		}catch(NoSuchMethodException ex){
			throw new LeoSerializationException("Getter not available for field "
					+fieldName+" or "+obj.getClass().getCanonicalName()+" is not a pojo");
		}
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
				makeJsonForMap(obj, builder);
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
			Object obj=itr.next();
			makeJson(builder, obj);
			if(/*objects.contains(obj) && */itr.hasNext())
				builder.append(",");
		}
	}

	private void makeJsonForMap(Object obj,StringBuilder builder) throws Exception{
		Map map=(Map)obj;
		Iterator itr=map.keySet().iterator();
		builder.append("{");
		while(itr.hasNext()){
			Object key=itr.next();
			Object o=map.get(key);
			if(o instanceof Number){
				builder.append("\"");
				builder.append(key);
				builder.append("\":");
				builder.append(o);
			}
			else{
				builder.append("\"");
				builder.append(key);
				builder.append("\":");
				builder.append("\"");
				makeJson(buf,o);	
				builder.append("\"");
				//makeJson(builder, o);
			}
			if(itr.hasNext())
				builder.append(",");
		}
		builder.append("}");
	}


	private String getterName(String f){
		return "get"+f.substring(0, 1).toUpperCase()+f.substring(1);
	}

	/*
	 * Json Mapping to object
	 * Under construction
	 */

	public Object getObject(Class type,String json) throws Exception{
		StringBuffer sb=new StringBuffer(json);
		Object obj=type.newInstance();
		while(sb.length()>2 || sb.indexOf(",")>=0){
			System.out.println(key(sb));
			System.out.println(val(sb));
		}
		return null;
	}

	
	private String key(StringBuffer sb){
		int fStart=sb.indexOf("\"");
		if(fStart<0){
			sb.delete(0, sb.length());
			return TERMINATE;
		}
		while(sb.charAt(fStart-1)=='\\')
			fStart=sb.indexOf("\"",fStart+1);
		int fEnd=sb.indexOf("\"",fStart+1);
		while(sb.charAt(fEnd-1)=='\\')
			fEnd=sb.indexOf("\"",fEnd+1);
		String retVal=sb.substring(fStart+1, fEnd);
		sb.delete(0, fEnd+1);
		return retVal;
	}
	
	private Object val(StringBuffer sb){
		int separator = sb.indexOf(":")+1;
		boolean isObject=false;
		boolean isArray=false;
		boolean isStr=false;
		boolean isInt=false;
		boolean isBool=false;
	
		while(sb.charAt(separator)==' ')
			separator++;
		char curr=sb.charAt(separator);
		if(curr=='{'){
			isObject=true;
		}	
		else if(curr=='['){
			isArray=true;
		}
		else if(curr=='"'){
			isStr=true;
		}else if(!isStr && Character.isAlphabetic(curr)){
			if(curr=='f')
				return "false";
			else if(curr=='t')
				return "true";
			else
				return "null";
		}
		else{
			isInt=true;
		}
		if(isStr){
			return key(sb);
		}else if(isObject){
			//return getObject(null,sb).toString();
		}else if(isInt){
			int tmpPointer=separator;
			while(Character.isDigit(sb.charAt(separator))){
				separator++;
			}
			String ret=sb.substring(tmpPointer, separator);
			sb.delete(tmpPointer, separator);
			return ret;
		}else if(isArray){
			int fStart=sb.indexOf("[")+1;
			int fEnd=sb.indexOf("\"",fStart+1);		
			return sb.substring(fStart+1, fEnd);
		}
		int fStart=sb.indexOf("")+1;
		int fEnd=sb.indexOf("\"",fStart+1);		
		return sb.substring(fStart+1, fEnd);
	}
	

	public static void main(String[] args){
		JSONWriter j=new JSONWriter();
		File f=new File("/home/divyank/Desktop/test.txt");
		
		try {
			BufferedReader br=new BufferedReader(new FileReader(f));
			j.getObject(Prodcty.class, br.readLine());
		} catch (Exception e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
		try {
			//System.out.println(j.getJson(new ServiceResponse("lol", 500)));
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		//j.getObject(null, "{\"firstName\": \"harry\",\"lastName\":\"tester\",\"toEmail\":\"testtest@test.com\"}");
	}

}
