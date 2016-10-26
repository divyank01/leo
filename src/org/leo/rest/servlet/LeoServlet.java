package org.leo.rest.servlet;

import java.io.File;
import java.io.IOException;
import java.io.ObjectOutputStream;
import java.util.Enumeration;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.leo.rest.service.ServiceHandler;

import com.pojos.Product;

public class LeoServlet extends HttpServlet{

	private ServiceHandler serviceHandler;
	
	@Override
	public void init() throws ServletException {
		super.init();
		serviceHandler=ServiceHandler.getHandler();
		String packageName=getServletConfig().getInitParameter("packageName");
		ClassLoader.getInstance().loadTemplates(packageName);
	}
	
	@Override
	protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
			serviceHandler.handle(req,resp);
	}
	
	@Override
	protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
		super.doPost(req, resp);
	}
	
	@Override
	protected void doDelete(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
		// TODO Auto-generated method stub
		super.doDelete(req, resp);
	}
	
	@Override
	protected void doPut(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
		// TODO Auto-generated method stub
		super.doPut(req, resp);
	}

}
