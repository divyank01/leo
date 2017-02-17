package org.leo.logging;

public class LogUtil {

	public static void setupLogging(String appender,String loc){
		Logger.appender=appender;
		Logger.folder=loc;
		Logger.load();
	}
}
