package org.leo.logging;

import java.util.logging.ConsoleHandler;

public class LoggingHandler  extends ConsoleHandler {


	{
		super.setFormatter(new LoggingFormatter());
	}

}
