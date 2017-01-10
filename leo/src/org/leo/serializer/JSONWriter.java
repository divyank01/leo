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

import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.Arrays;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.leo.exception.LeoSerializationException;
import org.leo.models.ServiceResponse;

public class JSONWriter {

	private StringBuilder buf=null;
	private Set objects=null;

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
			}else if(obj instanceof LeoSerializable){
				builder.append("{");
				int counter=0;
				if(!objects.contains(obj)){
					objects.add(obj);
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
				}
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
			if(objects.contains(obj) && itr.hasNext())
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
	 * Json Mapiing to object
	 */

	public Object getObject(Class type,String json){
		StringBuffer sb=new StringBuffer(json);
		getKeyVal(sb);

		return null;
	}

	private String getKeyVal(StringBuffer sb){
		boolean isObj=false;
		boolean isArr=false;
		if(sb.length()>0){
			for(int i=0;i<sb.length();i++){
				int cIndex=sb.indexOf(":");
				if(i==0 && sb.charAt(i)=='['){
					sb=sb.deleteCharAt(i);
				}
				if(sb.charAt(i)=='{'){
					sb=sb.deleteCharAt(i);
					isObj=true;
				}
				if(isObj){
					System.out.println(sb.substring(i, cIndex-1));
					sb.delete(i, cIndex);
					while(Character.isWhitespace(sb.charAt(i)))
						i++;
					if(sb.charAt(i)=='"'){
						i++;
						int start=i;
						while((i+1<sb.length() && sb.charAt(i)!='"')
								||(sb.charAt(i)=='"' && i+1<sb.length() &&
								(sb.charAt(i+1)!=',' && sb.charAt(i+1)!='\n' && sb.charAt(i+1)!=' '))){
							i++;
						}
						System.out.println(sb.substring(start, i));
						sb.delete(start-1, i+1);
					}
				}
				isObj=false;
				//break;
			}
			System.out.println(sb);
			return getKeyVal(sb);
		}else
			return sb.toString();
	}

	public static void main(String[] args){
		JSONWriter j=new JSONWriter();
		try {
			System.out.println(j.getJson(new ServiceResponse("lol", 500)));
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		//j.getObject(null, "{\"firstName\": \"ha\"rry\",\"lastName\":\"tester\",\"toEmail\":\"testtest@test.com\"}");
	}

}
