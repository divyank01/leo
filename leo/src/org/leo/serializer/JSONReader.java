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
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;


public class JSONReader {
	/*
	 * Json Mapping to object
	 * Under construction
	 */

	/*
	 * do make a wrapper for JSON
	 * 
	 * working fine for objects with out collection
	 * 
	 * read type info from @type and process collections
	 * 
	 */

	private static final String TERMINATE="1234";// as key can not be number

	public Object getObject(Class type,StringBuffer sb,Class genericType) throws Exception{
		if(sb.indexOf("null")==1)
			return null;
		Object root=null;
		if(!type.isInterface())
		root=type.newInstance();
		else{
			root=getInstance(type);
			return fillType(root,genericType, sb);
		}
		String val=null;
		while(sb.length()>2 || sb.indexOf(",")>=0){
			String key=key(sb);
			if(key.equals(TERMINATE))
				return root;
			Field field=root.getClass().getDeclaredField(key);
			field.setAccessible(true);
			if(sb.length()>0)
				val=val(sb);
			if(validType(field.getType())){
				if(val.equals("null")){
					field.set(root, nullCast(field.getType(),val));
				}else{
					field.set(root, cast(field.getType(),val));
				}
			}else{
				if(isCollection(field.getType()))
					field.set(root, getObject(field.getType(), sb,getListType(field)));
				else
					field.set(root, getObject(field.getType(), sb,null));
			}
			field.setAccessible(false);
		}
		return root;
	}

	private Object getInstance(Class type) {
		if(type == List.class)
			return new ArrayList<>();
		if(type == Map.class)
			return new HashMap<>();
		if(type == Set.class)
			return new HashSet<>();
		return null;
	}

	private boolean isCollection(Class type) {
		if(type == List.class||type == Map.class||type == Set.class)
			return true;
		return false;
	}

	private Object fillType(Object root, Class type,StringBuffer sb){
		if(root instanceof List){
			List list=(List)root;
			try {
				int countLB=1;
				int countRB=0;
				boolean flag=true;
				while(countLB>=0 || flag){
					if(sb.indexOf("[")>0){
						if(sb.indexOf("[")<sb.indexOf("]")){
							countLB++;
						}
					}
					list.add(getObject(type, sb, null));
					countLB--;
					flag=false;
					if(countLB==0)
						return list;
				}
			} catch (Exception e) {
				e.printStackTrace();
			}
			return list;
		}
		return null;
	}

	private String key(StringBuffer sb){
		int fStart=sb.indexOf("\"");
		int closed=sb.indexOf("}");
		if(closed>=0 && closed<fStart){
			sb.delete(0, closed+1);
			return TERMINATE;
		}
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

	private String val(StringBuffer sb){
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
			while(sb.length() > separator && Character.isDigit(sb.charAt(separator))){
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

	private static boolean validType(Class clazz){
		if(clazz == boolean.class || clazz == int.class || clazz == float.class||
				clazz == long.class || clazz == double.class || clazz == Boolean.class ||
				clazz == Integer.class || clazz == Float.class || clazz == Long.class ||
				clazz == Double.class || clazz == String.class)
			return true;
		return false;
	}

	private Object cast(Class clazz,String val){
		if(clazz == boolean.class || clazz == Boolean.class){
			return Boolean.valueOf(val);
		}
		if(clazz == int.class ||clazz == Integer.class){
			return Integer.valueOf(val);
		}
		if(clazz == float.class||clazz == Float.class){
			return Float.valueOf(val);
		}
		if(clazz == long.class ||clazz == Long.class){
			return Long.valueOf(val);
		}
		if(clazz == double.class || clazz == Double.class) {
			return Double.valueOf(val);
		}
		if(clazz == String.class)
			return val;
		return null;
	}


	private Object nullCast(Class clazz,String val){
		if(clazz == boolean.class || clazz == Boolean.class){
			return clazz == boolean.class ? false : null;
		}
		if(clazz == int.class ||clazz == Integer.class){
			return clazz == int.class?0:null;
		}
		if(clazz == float.class||clazz == Float.class){
			return clazz == float.class?0.0:null;
		}
		if(clazz == long.class ||clazz == Long.class){
			return clazz == long.class?0L:null;
		}
		if(clazz == double.class || clazz == Double.class) {
			return clazz == double.class?0.0:null;
		}
		if(clazz == String.class)
			return null;
		return null;
	}

	private Class getListType(Field f) throws ClassNotFoundException{
		String s=f.getGenericType().getTypeName();
		return Class.forName(s.substring(s.indexOf("<")+1, s.indexOf(">")));
	}

	public StringBuffer data(StringBuffer s){
		s.delete(0, s.indexOf("data")+6);
		s.delete(s.length()-3, s.length());
		return s;
	}
	

	public static void main(String[] args){
		JSONReader j=new JSONReader();
		File f=new File("/home/divyank/Desktop/test.txt");

		try {
			BufferedReader br=new BufferedReader(new FileReader(f));
			/*Product p=new Product();
			//String s=p.getClass().getDeclaredField("stocks").getGenericType().getTypeName();
			Object o=j.getObject(Product.class, j.data(new StringBuffer(br.readLine())),null);
			System.out.println(o);*/
		} catch (Exception e1) {
			e1.printStackTrace();
		}
		
	}
}
