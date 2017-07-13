package com.pojos;

import org.leo.rest.annotations.Serializable;

@Serializable
public class Product {

	private String name;
	private int id;
	private int price;
	private String color;
	private String description;
	private ProductDetails details;
	private Stock stock;
	
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public int getId() {
		return id;
	}
	public void setId(int id) {
		this.id = id;
	}
	public int getPrice() {
		return price;
	}
	public void setPrice(int price) {
		this.price = price;
	}
	public String getColor() {
		return color;
	}
	public void setColor(String color) {
		this.color = color;
	}
	public String getDescription() {
		return description;
	}
	public void setDescription(String description) {
		this.description = description;
	}
	public ProductDetails getDetails() {
		return details;
	}
	public void setDetails(ProductDetails details) {
		this.details = details;
	}
	public Stock getStock() {
		return stock;
	}
	public void setStock(Stock stock) {
		this.stock = stock;
	}
}
