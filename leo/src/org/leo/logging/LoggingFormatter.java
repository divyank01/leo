package org.leo.logging;

import java.util.logging.Formatter;
import java.util.logging.Handler;
import java.util.logging.LogRecord;

public class LoggingFormatter extends Formatter {

	@Override
	public String format(LogRecord record) {
		StringBuilder sb = new StringBuilder();
        sb.append(record.getLevel()).append(": LEO: ");
        sb.append(record.getMessage()).append('\n');
        record=null;
        return sb.toString();
	}
	
	@Override
	public String getHead(Handler h) {
		return super.getHead(h);
	}


}
