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
