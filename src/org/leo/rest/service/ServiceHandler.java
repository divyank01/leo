package org.leo.rest.service;

import java.util.StringTokenizer;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.leo.serializer.JSONWriter;

import com.pojos.Product;
import com.sun.org.apache.xerces.internal.impl.dv.dtd.NMTOKENDatatypeValidator;

public class ServiceHandler {

	private static ServiceHandler handler;
	protected ServiceHandler(){}

	public static ServiceHandler getHandler(){
		if(handler==null)
			handler=new ServiceHandler();
		return ServiceHandler.handler;
	}

	public void handle(HttpServletRequest req,HttpServletResponse resp){
		try{
			Object output=ServiceExecutor.getExecuter().execute(getServiceDetails(req));
			JSONWriter writer=new JSONWriter();
			resp.getOutputStream().print(writer.getJson(output));
		}catch(Exception e){
			e.printStackTrace();
		}
	}

	private String[] getServiceDetails(HttpServletRequest req){
		String uri=req.getRequestURI(); 
		String releventPath=uri.replaceFirst(req.getContextPath()+req.getServletPath(), "");
		String temp=releventPath.replaceFirst("/", "");
		String serviceName=temp.substring(0, temp.indexOf("/"));
		String[] service={serviceName,releventPath};
		return service;
	}
}
