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
package org.leo.rest.service;

import java.io.InputStream;
import java.util.Map;

public class LeoService {

	private final ServiceContext ctx=null;
	
	
	/**
	 * After decoding uri for service methods remaining uri converted to hashmap based on index
	 * @return map containing parameters
	 */
	public final Map<? extends Number, String> getParameterMap(){
		return ctx.getCloneParameters();
	}
	
	
	/**
	 * This returns parameter appended in the urls
	 * @return Map containing parameters
	 */
	public final Map<String,String[]> getParams(){
		return ctx.getCloneAppendedParams();
	}

	public final InputStream getStream(){
		return ctx.getStream();
	}
	
	public final <T,A extends Class> T getObject(A type1,A type2){
		return (T)ctx.getObject(type1,type2); 
	}
}
