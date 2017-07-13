package com.pojos;

import java.util.ArrayList;
import java.util.List;

import org.leo.rest.annotations.Serializable;

@Serializable
public class Stock {

	private int id;
	private List<StockMapping> mappings =new ArrayList<>();
	public int getId() {
		return id;
	}
	public void setId(int id) {
		this.id = id;
	}
	public List<StockMapping> getMappings() {
		return mappings;
	}
	public void setMappings(List<StockMapping> mappings) {
		this.mappings = mappings;
	}
}
