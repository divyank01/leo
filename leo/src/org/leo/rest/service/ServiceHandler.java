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
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.lang.annotation.Annotation;
import java.util.List;
import java.util.Map;

import javax.servlet.ServletConfig;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.leo.exception.LeoSerializationException;
import org.leo.exception.ServiceEx;
import org.leo.rest.annotations.Serializable;
import org.leo.rest.auth.AuthHandler;
import org.leo.rest.auth.AuthToken;
import org.leo.rest.auth.Authenticator;
import org.leo.rest.auth.LeoBasicAuthenticator;
import org.leo.rest.servlet.ClassLoader;
import org.leo.serializer.JSONWriter;
import org.leo.serializer.LeoSerializable;
import org.leo.models.ServiceResponse;

import static org.leo.exception.ErrorHandler.handleEx;

public class ServiceHandler {

	private static ServiceHandler handler;
	private String authClass;
	private ServletConfig config;
	private String keyFile;
	private Authenticator authenticator;
	AuthHandler authHandler=AuthHandler.getHandler();
	private JSONWriter writer=new JSONWriter();
	protected ServiceHandler(){}

	public static ServiceHandler getHandler(){
		if(handler==null)
			handler=new ServiceHandler();
		return ServiceHandler.handler;
	}

	public void handle(HttpServletRequest req,HttpServletResponse resp) throws Exception{
		if(req.getMethod().equals("GET")){
			handleGet(req, resp);
		}else if(req.getMethod().equals("POST")){
			handlePost(req, resp);
		}else if(req.getMethod().equals("PUT")){
			
		}else if(req.getMethod().equals("DELETE")){
			
		}
	}
	
	private void handlePost(HttpServletRequest req,HttpServletResponse resp) throws Exception{
		InputStream in=null;
		InputStreamReader isr=null;
		BufferedReader br=null;
		try{
			in=req.getInputStream();
			isr=new InputStreamReader(in);
			br=new BufferedReader(isr);
			String str=null;
			while((str=br.readLine())!=null){
				//System.out.println(str);
			}
		}catch(Exception ex){
			if(in!=null)
				in.close();
			if(isr!=null)
				isr.close();
			if(br!=null)
				br.close();
		}finally{
			if(in!=null)
				in.close();
			if(isr!=null)
				isr.close();
			if(br!=null)
				br.close();
		}
	}
	
	@SuppressWarnings("all")
	private void handleGet(HttpServletRequest req,HttpServletResponse resp) throws Exception{
		try{
			if(validate(req)){
				resp.reset();
				Object output=ServiceExecutor.getExecuter().execute(req);
				if(output!=null && !(output instanceof File)){
					if(isValid(output)){
						String json=writer.getJson(new ServiceResponse(output,200)); 
						resp.setBufferSize(json.length());
						resp.getOutputStream().print(json);
					}else{
						throw new LeoSerializationException("Can not serialize "
								+output.getClass().getCanonicalName()+" leoSeriazable not implemented");
					}
					resp.setStatus(200);
				}else if(output!=null && (output instanceof File || output instanceof InputStream)){
					if(output instanceof File){//from FS
						File file=(File)output;
						FileInputStream fis=new FileInputStream(file);
						int data=-20;
						while((data=fis.read())!=-1)
							resp.getOutputStream().write(data);
						fis.close();
					}
					if(output instanceof InputStream){//getting data from data base blob
						InputStream is=(InputStream)output;
						int data=-20;
						while((data=is.read())!=-1)
							resp.getOutputStream().write(data);
						is.close();
					}
					resp.setStatus(200);
				}else if(output==null){
					resp.getOutputStream().print("{}");
				}else{
					resp.sendError(404);
				}
			}else{
				String json=writer.getJson(new ServiceResponse("Authentecation failed",500)); 
				resp.setBufferSize(json.length());
				resp.getOutputStream().print(json);
			}
		}catch(Exception e){
			handleEx(e,"Internal Error occured", resp,500,true);
			throw e;
		}
	}

	private boolean isValid(Object output) {		
		return output instanceof LeoSerializable ||
				output.getClass().getAnnotation(Serializable.class)!=null||
				output instanceof List || 
				output instanceof Map || 
				output.getClass().isArray() || 
				output instanceof String || 
				output instanceof Number;
	}

	private boolean validate(HttpServletRequest req) throws Exception{
		if(isAuthReq()){
			String authToken=req.getHeader("authToken");
			if(authToken==null || authToken.isEmpty())
				authToken=req.getParameter("authToken");
			if(authToken==null || authToken.isEmpty())
				return false;
			return authHandler.isValidToken(authenticator ,new AuthToken(null,authToken),keyFile);
		}
		return true;
	}

	public final void setConfig(ServletConfig config){
		this.config=config;
	}

	public final void load(){
		this.authClass=this.config.getInitParameter("authenticator");
		this.keyFile=this.config.getInitParameter("keyFile");
		String logging=this.config.getInitParameter("logging");
		if(logging!=null && !logging.isEmpty()){
			String[] logDetails=logging.trim().split(":");
			boolean enabled=Boolean.valueOf(logDetails[0]);
			if(enabled && logDetails.length>1){
				System.setProperty("leoLogAppender", logDetails[1]);
				if(logDetails.length>2)
					System.setProperty("leoLogFolder", logDetails[2]);
			}
		}
	}

	private boolean isAuthReq() throws Exception{
		return getAuthenticator()==null?false:true;
	}

	private Authenticator getAuthenticator() throws Exception{
		if(this.authClass!=null && !this.authClass.isEmpty()){
			Class clazz=Class.forName(this.authClass);
			authenticator=(Authenticator)clazz.newInstance();
			return authenticator;
		}
		return null;
	}
}
