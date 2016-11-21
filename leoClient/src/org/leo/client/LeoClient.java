package org.leo.client;

import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.ByteArrayInputStream;
import java.io.DataOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.FileWriter;
import java.io.InputStreamReader;
import java.io.ObjectInputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import javax.net.ssl.HttpsURLConnection;

import com.thoughtworks.xstream.XStream;

public class LeoClient {

	private String hostName;

	public static void main(String[] args){
		LeoClient client=new LeoClient();
		try {
			LeoRequest req=new LeoRequest();
			req.setUrl("http://localhost:8080/LeoTest/rest/ProductProvider/product/prodId/");
			req.setMethod("GET");
			req.getAttributeMap().put("authkey", "some key");
			req.getHeader().put("some in head", "thug life");
			LeoResponse resp=client.sendRecieve(req);
			//System.out.println(new XStream().toXML(resp));
			
		}  catch (Exception e) {
			e.printStackTrace();
		}
	}

	public void send(){

	}

	public LeoClient addAttribute(Object input){
		return this;
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

}
