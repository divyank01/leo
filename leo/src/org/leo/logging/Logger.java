/**  Leo is an open source framework for REST APIs.
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

package org.leo.logging;

import java.util.logging.ConsoleHandler;
import java.util.logging.FileHandler;
import java.util.logging.Handler;
import java.util.logging.Level;
import java.util.logging.StreamHandler;

public class Logger {
	private Logger(){}
	
	private static boolean doLog=true;
	
	private static final String C="console";
	
	private static final String F="file";
	
	protected static String appender;
	
	protected static String folder;
	
	private static java.util.logging.Logger LOGGER=java.util.logging.Logger.getLogger("");
	
	private static StreamHandler handler= null;
	
	protected static void load(){
		try {
			LOGGER=java.util.logging.Logger.getLogger("");
			for(Handler h:LOGGER.getHandlers()){
				LOGGER.removeHandler(h);
			}			
			doLog=Boolean.valueOf(appender!=null);
			if(doLog){
				LOGGER.setUseParentHandlers(false);
				if(appender.equals(C)){
					handler=new ConsoleHandler();
				}
				if(appender.equals(F)){
					handler=new FileHandler(Logger.folder,1024000,10,true);
				}
				LOGGER.addHandler(handler);
				handler.setFormatter(new LoggingFormatter());
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	public static void info(String value){
		if(doLog)
			LOGGER.log(Level.INFO, value);
	}
	
	public static void fine(String value){
		if(doLog)
			LOGGER.log(Level.FINE, value);
	}
	
	public static void severe(String value){
		if(doLog)
			LOGGER.log(Level.SEVERE, value);
	}
	
	public static void warn(String value){
		if(doLog)
			LOGGER.log(Level.WARNING, value);
	}
	
}
