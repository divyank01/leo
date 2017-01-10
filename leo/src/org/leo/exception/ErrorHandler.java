package org.leo.exception;

import java.io.IOException;

import javax.servlet.http.HttpServletResponse;
import org.leo.models.ServiceResponse;
import org.leo.serializer.JSONWriter;

public class ErrorHandler {

	private static JSONWriter writer=new JSONWriter();

	public static void handleEx(Exception ex,String message,HttpServletResponse resp,int statusCode){
		resp.reset();
		resp.setStatus(statusCode);
		try {
			if(message!=null)
				resp.getWriter().println(message);
			else
				resp.getWriter().println(ex.getMessage());
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	public static void handleEx(Exception ex,String message,HttpServletResponse resp,int statusCode,boolean writeJson){
		resp.reset();
		resp.setStatus(statusCode);
		try {
			if(writeJson){
				resp.getWriter().println(writer.getJson(new ServiceResponse(message,statusCode)));
				System.out.println(writer.getJson(new ServiceResponse(message,statusCode)));
			}
			else{
				if(message!=null)
					resp.getWriter().println(message);
				else
					resp.getWriter().println(ex.getMessage());
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public static void handleEx(Exception ex,String message,HttpServletResponse resp){
		resp.reset();
		resp.setStatus(500);
		try {
			if(message==null || (message!=null && message.isEmpty()))
				resp.getWriter().println(message);
			else
				resp.getWriter().println(ex.getMessage());
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	public static void handleEx(String message,HttpServletResponse resp){
		resp.reset();
		resp.setStatus(500);
		try {
			resp.getWriter().println(message);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
}
