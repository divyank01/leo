package org.leo.exception;

public class ServiceEx extends Exception {

	public ServiceEx(String msg){
		super(msg);
	}
	
	public ServiceEx(Throwable t){
		super(t);
	}
	
}
