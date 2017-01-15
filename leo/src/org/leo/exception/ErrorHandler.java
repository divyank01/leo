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
				if(message!=null && !(ex instanceof LeoExceptions))
					resp.getWriter().println(writer.getJson(new ServiceResponse(message,statusCode)));
				else
					resp.getWriter().println(writer.getJson(new ServiceResponse(ex.getMessage(),statusCode)));
			}
			else{
				if(message!=null && !(ex instanceof LeoExceptions))
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
