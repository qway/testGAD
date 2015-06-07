package Testing;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.util.HashSet;
import java.util.Scanner;
import java.util.Set;
import java.util.logging.Formatter;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.logging.SimpleFormatter;
import java.util.stream.Stream;

public class Test
{
	private int testAmount;
	private String directory;
	private TestFunction testFunc;
	private static ByteArrayOutputStream generatedOutputStream = new ByteArrayOutputStream();
	private final static Logger LOGGER = Logger.getLogger(Test.class.getName());

	public Test(String dir, TestFunction t)
	{
		testAmount = new File(dir).list().length / 2;
		directory = dir;
		testFunc = t;
	}

	public void testAll()
	{
		
		LOGGER.addHandler(new java.util.logging.StreamHandler(StreamHandler.out, new SimpleFormatter()));
		LOGGER.setUseParentHandlers(false);
		LOGGER.entering(Test.class.getName(), "testAll()");

		long start = System.currentTimeMillis();
		String[] data = new File(directory).list();
		data = Stream.of(data).filter(s -> s.contains(".in"))
				.toArray(size -> new String[size]);
		Set<String> badRuns = new HashSet<String>();
		for (String e : data)
		{
			e = e.replace(".in", "");
			LOGGER.log(Level.FINE, "------------------------\n" + "Case " + e
					+ "\n" + "------------------------");
			if (!testCase(e))
			{
				badRuns.add(e);

			}
		}

		LOGGER.log(Level.INFO,
				"\nTests successful: " + (data.length - badRuns.size()) + "/"
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
			testFunc.invoke();
		}
		catch (Exception e)
		{
			LOGGER.log(Level.SEVERE, "Exception thrown: ", e);
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
			LOGGER.log(Level.FINE, "Correct Output:   " + correctOutput);
			LOGGER.log(Level.FINE, "Generated Output: " + generatedOutput);
			LOGGER.log(Level.FINE, "Successful");
		}
		else
		{
			LOGGER.log(Level.INFO, "Correct Output:   " + correctOutput);
			LOGGER.log(Level.INFO, "Generated Output: " + generatedOutput);
			LOGGER.log(Level.INFO, "Fail");
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

}

interface TestFunction
{
	public void invoke();
}
