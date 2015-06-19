package testing;

import java.util.logging.LogRecord;
import java.util.logging.SimpleFormatter;

public class LoggingFormatter extends SimpleFormatter
{

	@Override
	public synchronized String format(LogRecord record)
	{
		StringBuilder message = new StringBuilder();

		
		message.append(record.getLevel().toString());
		message.append(": ");
		message.append(record.getMessage());
		message.append("\n");
		Throwable thrown = record.getThrown();
		if (thrown != null)
		{
			message.append(record.getThrown());
		}

		return message.toString();
	}

}
