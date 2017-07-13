package com.pojos;

import org.leo.rest.annotations.Serializable;

@Serializable
public class StockMapping {

	private int id;
	private int locId;
	public int getId() {
		return id;
	}
	public void setId(int id) {
		this.id = id;
	}
	public int getLocId() {
		return locId;
	}
	public void setLocId(int locId) {
		this.locId = locId;
	}
}
