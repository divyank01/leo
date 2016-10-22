package org.leo.rest.template;

import java.util.HashMap;
import java.util.Map;
import java.util.StringTokenizer;

public class TTree {

	private Node root;
	protected TTree(String key){
		this.root=new Node(key);
	}
	
	public void add(String url,String methodName){
		StringTokenizer tokenizer=new StringTokenizer(url, "/");
		int count=0;
		Node currentChild=null;
		while (tokenizer.hasMoreElements()) {
			count++;
			String token = (String) tokenizer.nextElement();
			if(count==1){
				currentChild=root;
				if(currentChild.getChildren().get(token)==null){
					Node n=new Node(token);
					n.setDepth(count);
					currentChild.getChildren().put(token,n);
					currentChild=n;
				}else
					currentChild=currentChild.getChildren().get(token);
				if(!tokenizer.hasMoreElements()){
					currentChild.setValue(methodName);
				}
			}else{
				Node tempNode=currentChild.getChildren().get(token);
				if(tempNode==null){
					tempNode=new Node(token);
					tempNode.setDepth(count);
					currentChild.getChildren().put(token, tempNode);	
				}
				currentChild=tempNode;
				if(!tokenizer.hasMoreElements()){
					tempNode.setValue(methodName);
				}
			}
			
		}
	}
	
	public String getMethod(String url,int depth){
		StringTokenizer tokenizer=new StringTokenizer(url, "/");
		Node currentNode=root;
		Node prevNode=null;
		String bestMatch=null;
		depth=0;
		while (tokenizer.hasMoreElements()) {
			String token = (String) tokenizer.nextElement();
			currentNode=currentNode.getChildren().get(token);
			if(currentNode!=null && currentNode.getValue()!=null){
				bestMatch=currentNode.getValue();
				depth=currentNode.getDepth();
			}
			if(currentNode==null){
				if(prevNode!=null){
					depth=prevNode.getDepth();
					return prevNode.getValue();
				}else
					break;
			}
			prevNode=currentNode;
		}
		return bestMatch;
	}
	
	private class Node{
		private Map<String, Node> children=new HashMap<>();
		private String key;
		private String value;
		private int depth;
		private Node(String key){
			this.key=key;
		}
		public Map<String, Node> getChildren() {
			return children;
		}
		public void setChildren(Map<String, Node> children) {
			this.children = children;
		}
		public String getKey() {
			return key;
		}
		public void setKey(String key) {
			this.key = key;
		}
		public String getValue() {
			return value;
		}
		public void setValue(String value) {
			this.value = value;
		}
		public int getDepth() {
			return depth;
		}
		public void setDepth(int depth) {
			this.depth = depth;
		}
	}
}
