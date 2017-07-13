package com.leo.service;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.leo.rest.annotations.Get;
import org.leo.rest.annotations.Path;
import org.leo.rest.annotations.Post;
import org.leo.rest.annotations.Service;
import org.leo.rest.service.LeoService;

import com.pojos.Product;
import com.pojos.ProductDetails;
import com.pojos.Stock;
import com.pojos.StockMapping;
import com.thoughtworks.xstream.XStream;

@Service(name="PS")
public class ProductService extends LeoService {

	
	@Get
	@Path(urlPath="/music")
	public File getGana() {
		return new File("/home/divyank/Desktop/abc.mp3");
	}
	
	@Get
	@Path(urlPath="/video")
	public File getVideo() {
		return new File("/home/divyank/Desktop/abc.mp4");
	}
	
	@Get
	@Path(urlPath="/img")
	public File getImg() {
		return new File("/home/divyank/Desktop/pp.jpg");
	}
	
	@Get
	@Path(urlPath="/map")
	public Map getSomeMap() {
		Map m= new HashMap();
		m.put("key", "value");
		return m;
	}
	
	@Get
	@Path(urlPath="/str")
	public String getSomeStr() {
		Map m= new HashMap();
		m.put("key", "value");
		return "this is the string";
	}
	
	@Post
	@Path(urlPath="/send")
	public void method1() {
		List<Product> p=getObject(List.class,Product.class);
		System.out.println(new XStream().toXML(p));
	}
	
	@Post
	@Path(urlPath="/ss")
	public void method2() {
		Product p=getObject(Product.class,null);
		System.out.println(new XStream().toXML(p));
	}
	
	@Get
	@Path(urlPath="/RP")
	public List<Product> getSomeRandomProduct() {
		Product p=new Product();
		ProductDetails pd=new ProductDetails();
		Stock stock=new Stock();
		StockMapping sm=new StockMapping();
		p.setDetails(pd);
		p.setStock(stock);
		List<StockMapping> mappings=new ArrayList<>();
		mappings.add(sm);
		mappings.add(sm);
		mappings.add(sm);
		mappings.add(sm);
		mappings.add(sm);
		mappings.add(sm);
		mappings.add(sm);
		stock.setMappings(mappings);
		List<Product> lp=new ArrayList<>();
		lp.add(p);lp.add(p);lp.add(p);lp.add(p);lp.add(p);lp.add(p);lp.add(p);
		lp.add(p);lp.add(p);lp.add(p);lp.add(p);lp.add(p);lp.add(p);lp.add(p);
		lp.add(p);lp.add(p);lp.add(p);lp.add(p);lp.add(p);lp.add(p);lp.add(p);
		lp.add(p);lp.add(p);lp.add(p);lp.add(p);lp.add(p);lp.add(p);lp.add(p);
		lp.add(p);lp.add(p);lp.add(p);lp.add(p);lp.add(p);lp.add(p);lp.add(p);
		lp.add(p);lp.add(p);lp.add(p);lp.add(p);lp.add(p);lp.add(p);lp.add(p);
		lp.add(p);lp.add(p);lp.add(p);lp.add(p);lp.add(p);lp.add(p);lp.add(p);lp.add(p);
		lp.add(p);lp.add(p);lp.add(p);lp.add(p);lp.add(p);lp.add(p);lp.add(p);lp.add(p);
		return lp;
	}
	
	@Get
	@Path(urlPath="/OP")
	public Product blah() {
		Product p=new Product();
		ProductDetails pd=new ProductDetails();
		Stock stock=new Stock();
		StockMapping sm=new StockMapping();
		p.setDetails(pd);
		p.setStock(stock);
		List<StockMapping> mappings=new ArrayList<>();
		mappings.add(sm);
		mappings.add(sm);mappings.add(sm);mappings.add(sm);mappings.add(sm);mappings.add(sm);mappings.add(sm);
		stock.setMappings(mappings);
		return p;
	}
	
}
