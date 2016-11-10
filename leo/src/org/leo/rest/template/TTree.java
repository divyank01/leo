/**
  *  Leo is an open source framework for REST APIs.
  *
  *  Copyright (C) 2016  @author Divyank Sharma
  *
  *  This program is free software: you can redistribute it and/or modify
  *  it under the terms of the GNU General Public License as published by
  *  the Free Software Foundation, either version 3 of the License, or
  *  (at your option) any later version.
  *
  *  This program is distributed in the hope that it will be useful,
  *  but WITHOUT ANY WARRANTY; without even the implied warranty of
  *  MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
  *  GNU General Public License for more details.
  *
  *  You should have received a copy of the GNU General Public License
  *  along with this program.  If not, see <http://www.gnu.org/licenses/>.
  *  
  *  For details please read LICENSE file.
  *  
  *  In Addition to it if you find any bugs or encounter any issue you need to notify me.
  *  I appreciate any suggestions to improve it.
  *  @mailto: divyank01@gmail.com
  */
package org.leo.rest.template;

import java.util.HashMap;
import java.util.Map;
import java.util.StringTokenizer;

/**
 * Its a basic implementation of tries.
 * @author divyank
 *
 */
public class TTree {

	private Node root;
	private static final String ROOT="/";
	private static final String COLON=":";
	protected TTree(String key){
		this.root=new Node(key);
	}
	
	public void add(String url,String methodName){
		StringTokenizer tokenizer=new StringTokenizer(url, ROOT);
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
	
	public String getMethod(String url){
		int count=0;
		StringTokenizer tokenizer=new StringTokenizer(url, ROOT);
		Node currentNode=root;
		Node prevNode=null;
		String bestMatch=null;
		int depth=0;
		while (tokenizer.hasMoreElements()) {
			String token = (String) tokenizer.nextElement();
			currentNode=currentNode.getChildren().get(token);
			if(currentNode!=null && currentNode.getValue()!=null && count!=0){
				bestMatch=currentNode.getValue();
				depth=currentNode.getDepth();
			}
			if(currentNode==null && count!=0){
				if(prevNode!=null){
					depth=prevNode.getDepth();
					return prevNode.getValue()+COLON+depth;
				}else
					break;
			}
			prevNode=currentNode;
			count++;
		}
		return bestMatch+COLON+depth;
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
