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

import java.util.logging.Handler;
import java.util.logging.Level;

public class Logger {
	private Logger(){}
	
	private static boolean doLog=true;
	
	private static java.util.logging.Logger LOGGER=java.util.logging.Logger.getLogger("");
	
	private static LoggingHandler handler= new LoggingHandler();
	
	static{
		try {
			LOGGER=java.util.logging.Logger.getLogger("");
			for(Handler h:LOGGER.getHandlers()){
				LOGGER.removeHandler(h);
			}
			doLog=Boolean.valueOf(System.getProperty("leoLogAppender")!=null);
			LOGGER.setUseParentHandlers(false);
			LOGGER.addHandler(handler);
			handler.setFormatter(new LoggingFormatter());
			handler.close();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	public static void log(String value){
		if(doLog)
			LOGGER.log(Level.INFO, value);
	}
	
}
