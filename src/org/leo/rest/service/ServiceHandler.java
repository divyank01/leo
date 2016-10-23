package org.leo.rest.service;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.leo.exception.LeoSerializationException;
import org.leo.serializer.JSONWriter;
import org.leo.serializer.LeoSerializable;

public class ServiceHandler {

	private static ServiceHandler handler;
	private JSONWriter writer=new JSONWriter();
	protected ServiceHandler(){}

	public static ServiceHandler getHandler(){
		if(handler==null)
			handler=new ServiceHandler();
		return ServiceHandler.handler;
	}

	public void handle(HttpServletRequest req,HttpServletResponse resp) throws IOException{
		try{
			resp.reset();
			Object output=ServiceExecutor.getExecuter().execute(getServiceDetails(req));

			if(output!=null && !(output instanceof File)){
				if(isValid(output)){
					String json=writer.getJson(output); 
					resp.setBufferSize(json.length());
					resp.getOutputStream().print(json);
				}else{
					throw new LeoSerializationException("Can not serialize "
							+output.getClass().getCanonicalName()+" leoSeriazable not implemented");
				}
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
			}else{
				resp.sendError(404);
			}
		}catch(Exception e){
			resp.sendError(500);
			e.printStackTrace();
		}
	}

	private boolean isValid(Object output) {		
		return output instanceof LeoSerializable || output instanceof List || output instanceof Map || output.getClass().isArray();
	}

	private String[] getServiceDetails(HttpServletRequest req){
		//String uri=req.getPathInfo(); 
		String releventPath=req.getPathInfo();//uri.replaceFirst(req.getContextPath()+req.getServletPath(), "");
		//System.out.println(req.getPathInfo()+"\n"+req.getPathTranslated());
		String temp=releventPath.replaceFirst("/", "");
		String serviceName=temp.substring(0, temp.indexOf("/"));
		String[] service={serviceName,releventPath};
		return service;
	}
}
