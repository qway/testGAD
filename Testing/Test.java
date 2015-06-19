package testing;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.lang.reflect.Method;
import java.util.HashSet;
import java.util.Scanner;
import java.util.Set;
import java.util.logging.FileHandler;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.stream.Stream;

public class Test
{
	
	private String directory;
	private Method main;
	private static ByteArrayOutputStream generatedOutputStream = new ByteArrayOutputStream();
	private static final Logger LOGGER = Logger.getLogger(Test.class.getName());
	private static final Level failLogLevel = Level.FINE;
	private static final Level succLogLevel = Level.FINER;
	
	static {
		LOGGER.setUseParentHandlers(false);
		LOGGER.setLevel(Level.ALL);
		java.util.logging.StreamHandler shandler= new java.util.logging.StreamHandler(StreamHandler.out, new LoggingFormatter());
		shandler.setLevel(Main.getStreamLogLevel());
		LOGGER.addHandler(shandler);
		try
		{
			FileHandler fhandler = new FileHandler("./results.txt");
			fhandler.setLevel(Main.getFileLogLevel());
			LOGGER.addHandler(fhandler);
		}
		catch (Exception e)
		{
			LOGGER.log(Level.WARNING, "Logging to file failed", e);
		}
	}
	
	public Test(String dir, Method main)
	{
		
		directory = dir;
		this.main = main;
	}

	public void testAll()
	{
		

		long start = System.currentTimeMillis();
		String[] data = new File(directory).list();
		data = Stream.of(data).filter(s -> s.contains(".in"))
				.toArray(size -> new String[size]);
		Set<String> badRuns = new HashSet<String>();
		for (String caseName : data)
		{
			caseName = caseName.replace(".in", "");
			if (!testCase(caseName))
			{
				logCase(failLogLevel, caseName);
				badRuns.add(caseName);
			}
			else {
				logCase(succLogLevel, caseName);
			}
		}

		LOGGER.log(Level.INFO,
				"Tests successful: " + (data.length - badRuns.size()) + "/"
						+ data.length);
		LOGGER.log(Level.INFO, "Failed Tests: " + badRuns.toString());
		long totalTime = System.currentTimeMillis() - start;
		LOGGER.log(
				Level.INFO,
				"Testing took " + totalTime + " ms or "
						+ Math.round((double) totalTime / 1000) + " s");

		LOGGER.exiting(Test.class.getName(), "testAll()");

	}

	public boolean testCase(String name)
	{
		String filenameIn = directory + "/" + name + ".in";
		String filenameOut = directory + "/" + name + ".out";
		StreamHandler.setInputFileStream(filenameIn);
		generatedOutputStream = new ByteArrayOutputStream();
		StreamHandler.setOutputStream(generatedOutputStream);

		try
		{
			String[] args = new String[]{};
			main.invoke(main.getDeclaringClass(), (Object)args);
		}
		catch (Exception e)
		{
			LOGGER.log(Level.SEVERE, "Exception thrown while invoking main method: ", e);
		}

		StreamHandler.resetStreams();
		return checkSolution(filenameOut);

	}

	private boolean checkSolution(String filenameOut)
	{
		// Trim is used to remove any whitespace surrounding the output
		String correctOutput = getCorrectOutput(filenameOut).trim().replaceAll(
				"(\\r|\\n|\\r\\n)+", "");
		String generatedOutput = generatedOutputStream.toString().trim()
				.replaceAll("(\\r|\\n|\\r\\n)+", "");
		boolean check = generatedOutput.equals(correctOutput);

		if (check)
		{
			LOGGER.log(succLogLevel, "Correct Output:   " + correctOutput);
			LOGGER.log(succLogLevel, "Generated Output: " + generatedOutput);
			LOGGER.log(succLogLevel, "Successful");
		}
		else
		{
			LOGGER.log(failLogLevel, "Correct Output:   " + correctOutput);
			LOGGER.log(failLogLevel, "Generated Output: " + generatedOutput);
			LOGGER.log(failLogLevel, "Fail");
		}

		return check;
	}

	private String getCorrectOutput(String filename)
	{
		FileReader r = null;
		try
		{
			r = new FileReader(new File(filename));
		}
		catch (FileNotFoundException e)
		{
			LOGGER.log(Level.SEVERE, "File not found: " + filename + "\t", e);
		}
		Scanner s = new Scanner(r);
		StringBuilder correctOutput = new StringBuilder();
		while (s.hasNext())
		{
			correctOutput.append(s.nextLine());
		}
		s.close();
		return correctOutput.toString();

	}
	
	private void logCase(Level logLevel, String caseName) {
		
		LOGGER.log(logLevel, "------------------------\n" + "Case " + caseName
				+ "\n" + "------------------------");
	}

}

interface TestFunction
{
	public void invoke();
}
