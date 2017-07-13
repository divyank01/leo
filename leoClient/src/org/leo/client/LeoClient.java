package org.leo.client;

import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import javax.net.ssl.HttpsURLConnection;

import org.leo.serializer.JSONReader;

import com.pojos.Product;


public class LeoClient {

	private String hostName;

	private String url="";
	public static void main(String[] args){
		LeoClient client=new LeoClient();
		try {
			LeoRequest req=new LeoRequest();
			req.setUrl("http://localhost:8080/LeoClient/rest/PS/OP?productid=505706&");
			req.setMethod("GET");
			req.getAttributeMap().put("authToken", "XQy/tg5mN6R0qTRJOf43mA==");
			req.getHeader().put("authToken", "XQy/tg5mN6R0qTRJOf43mA==");
			LeoResponse resp=client.sendRecieve(req);
			JSONReader jr=new JSONReader();
			System.out.println(resp.getJson());
			Product p=(Product)jr.getObject(Product.class,jr.data(new StringBuffer(resp.getJson())),null);
			System.out.println(resp.getJson());
			System.out.println(p.getName());
		}  catch (Exception e) {
			e.printStackTrace();
		}
	}

	private LeoResponse sendRecieve(LeoRequest req) throws Exception {
		LeoResponse resp=new LeoResponse();
		URL obj = new URL(req.getUrl());
		HttpURLConnection con = (HttpURLConnection) obj.openConnection();
		con.setRequestMethod(req.getMethod());
		Iterator<String> itr=req.getAttributeMap().keySet().iterator();
		while(itr.hasNext()){
			String key=itr.next();
			con.setRequestProperty(key, req.getAttributeMap().get(key));
		}
		BufferedReader in = new BufferedReader(
				new InputStreamReader(con.getInputStream()));
		String inputLine;
		StringBuffer response = new StringBuffer();

		while ((inputLine = in.readLine()) != null) {
			response.append(inputLine);
		}
		in.close();
		Map<String,List<String>> map = con.getHeaderFields();
		for (String key : map.keySet()) {
			resp.getHeader().put(key, map.get(key));
		}
		resp.setJson(response.toString());
		resp.setInputStream(con.getInputStream());
		resp.setStatusCode(con.getResponseCode());
		return resp;
	}

	// HTTP POST request
	private void sendPost() throws Exception {

		String url = "https://selfsolve.apple.com/wcResults.do";
		URL obj = new URL(url);
		HttpsURLConnection con = (HttpsURLConnection) obj.openConnection();

		//add reuqest header
		con.setRequestMethod("POST");
		con.setRequestProperty("User-Agent", "");
		con.setRequestProperty("Accept-Language", "en-US,en;q=0.5");

		String urlParameters = "sn=C02G8416DRJM&cn=&locale=&caller=&num=12345";

		// Send post request
		con.setDoOutput(true);
		DataOutputStream wr = new DataOutputStream(con.getOutputStream());
		wr.writeBytes(urlParameters);
		wr.flush();
		wr.close();

		int responseCode = con.getResponseCode();
		System.out.println("\nSending 'POST' request to URL : " + url);
		System.out.println("Post parameters : " + urlParameters);
		System.out.println("Response Code : " + responseCode);

		BufferedReader in = new BufferedReader(
				new InputStreamReader(con.getInputStream()));
		String inputLine;
		StringBuffer response = new StringBuffer();

		while ((inputLine = in.readLine()) != null) {
			response.append(inputLine);
		}
		in.close();

		//print result
		System.out.println(response.toString());

	}

	private LeoClient(){}

	public static LeoClient createClient(String url) throws Exception{
		LeoClient client=new LeoClient();
		client.url=url;
		return client;
	}

	public void send(){

	}

	public LeoClient addAttribute(Object input){
		return this;
	}


}
