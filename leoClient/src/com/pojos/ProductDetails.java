package com.pojos;

import org.leo.rest.annotations.Serializable;

@Serializable
public class ProductDetails {

	private String size;
	private String details;
	private int pdId;
	public String getSize() {
		return size;
	}
	public void setSize(String size) {
		this.size = size;
	}
	public String getDetails() {
		return details;
	}
	public void setDetails(String details) {
		this.details = details;
	}
	public int getPdId() {
		return pdId;
	}
	public void setPdId(int pdId) {
		this.pdId = pdId;
	}

	
}
