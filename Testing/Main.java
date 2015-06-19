package testing;

import java.io.File;
import java.lang.reflect.Method;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLClassLoader;
import java.util.logging.Level;
import java.util.logging.Logger;

public class Main
{

	private final static Logger LOGGER = Logger.getLogger(Test.class.getName());
	private static Level streamLogLevel = Level.FINE;
	private static Level fileLogLevel = Level.ALL;

	static
	{
		LOGGER.setUseParentHandlers(false);
		LOGGER.setLevel(Level.INFO);
		java.util.logging.StreamHandler shandler = new java.util.logging.StreamHandler(
				StreamHandler.out, new LoggingFormatter());
		LOGGER.addHandler(shandler);
	}

	public static void main(String[] args)
	{

		if (args.length != 3)
		{
			LOGGER.log(
					Level.SEVERE,
					"Invalid Parameters. Syntax: testGAD.jar testFolder programPath packageName.className\n"
							+ "The testFolder and programPath needs to be a directory. className must be the .class file which"
							+ "contains the main method of the program to test.\n"
							+ "Example: java -jar testGAD.jar ./Test ./dhashing dhashing.Program");
		}
		else
		{
			try
			{
				String testFolder = args[0];
				String programPath = args[1];
				String className = args[2].replace(".class", "");

				File file = new File(programPath);

				URL url = file.toURI().toURL();
				URL[] urls = new URL[] { url };
				ClassLoader cl = new URLClassLoader(urls);

				Class<?> mainClass = Class.forName(className, true, cl);
				Method main = mainClass.getMethod("main", String[].class);
				Test test = new Test(testFolder, main);
				test.testAll();

			}
			catch (ClassNotFoundException e)
			{
				LOGGER.log(Level.SEVERE, "Class not found", e);
			}
			catch (MalformedURLException e)
			{
				LOGGER.log(Level.SEVERE,
						"Invalid path and/or name for program", e);
			}
			catch (NoSuchMethodException e)
			{
				LOGGER.log(Level.SEVERE, "main method not found", e);
			}
			catch (SecurityException e)
			{
				LOGGER.log(Level.SEVERE, "access to main method denied", e);
			}
			finally
			{
				LOGGER.log(Level.INFO, "testGad terminated");
			}
		}

	}

	public static Level getStreamLogLevel()
	{
		return streamLogLevel;
	}

	public static Level getFileLogLevel()
	{
		return fileLogLevel;
	}
	
}
