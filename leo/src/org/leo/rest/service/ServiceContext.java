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

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.HashMap;
import java.util.Map;

import org.leo.serializer.JSONReader;


public class ServiceContext {

	private HashMap<? extends Number, String> serviceParm=new HashMap<>();
	private HashMap<String,String[]> appendedParm=new HashMap<>();
	private InputStream inputStream;
	private String contentType;
	private String jStr;
	private static final JSONReader reader=new JSONReader();
	
	protected String getJson(){
		return this.jStr;
	}
	
	protected void setJson(String jStr){
		this.jStr=jStr;
	}
	
	protected Map<? extends Number, String> getCloneParameters(){
		return (Map<? extends Number, String>) this.serviceParm.clone();
	}
	
	protected HashMap<String,String[]> getCloneAppendedParams(){
		return (HashMap<String,String[]>) this.appendedParm.clone();
	}
	
	protected Map<? extends Number, String> getParameters(){
		return this.serviceParm;
	}
	
	protected void setParams(Map m){
		serviceParm=(HashMap<? extends Number, String>) m;
	}

	protected void setAppendedParams(Map<String, String[]> parameterMap) {
		this.appendedParm=(HashMap<String, String[]>) parameterMap;
	}
	
	protected ServiceContext(Map<? extends Number, String> serviceParm, Map<String,String[]> appendedParm,InputStream in,String contentType){
		this.serviceParm=(HashMap<? extends Number, String>)serviceParm;
		this.appendedParm=(HashMap<String, String[]>) appendedParm;
		this.inputStream=in;
		this.contentType=contentType;
	}
	
	protected InputStream getStream(){
		return this.inputStream;
	}
	
	protected Object getObject(Class clazz){
		InputStream in=getStream();
		InputStreamReader isr=new InputStreamReader(in);
		BufferedReader br=new BufferedReader(isr);
		String json=null;
		StringBuffer sb=null;
		try {
			while((json=br.readLine())!=null){
				sb=new StringBuffer(json);
			}
			return reader.getObject(clazz, reader.data(sb), null);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null; 
	}
	
	
}
