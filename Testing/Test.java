package Testing;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Scanner;
import java.util.Set;
import java.util.stream.Stream;


public class Test {
    private int testAmount;
    private String directory;
    private TestFunction testFunc;
    private static ByteArrayOutputStream generatedOutputStream = new ByteArrayOutputStream();
    
    public Test(String dir, TestFunction t) {
        testAmount = new File(dir).list().length/2;
        directory = dir;
        testFunc = t;
    }

    
    public void testAll(){
        String[] data = new File(directory).list();
        data = Stream.of(data).filter(s -> s.contains(".in")).toArray(size -> new String[size]);
        Set<String> badRuns = new HashSet<String>();
        for(String e : data){
            e = e.replace(".in", "");
            System.out.println("------------------------\n"
                             + "Case "+e+"\n"
                             + "------------------------");
            if(!testCase(e)) {
            	badRuns.add(e);
            }
        }
     
        System.out.println("\nTests successful: " + (data.length - badRuns.size()) + "/" + data.length);
        System.out.println("Failed Tests: " + badRuns.toString());
        
    }
    
    public boolean testCase(String name){
        String filenameIn  = directory + "/" + name + ".in";
        String filenameOut = directory + "/" + name + ".out";
        StreamHandler.setInputFileStream(filenameIn);
        generatedOutputStream = new ByteArrayOutputStream();
        StreamHandler.setOutputStream(generatedOutputStream);
        
        testFunc.invoke();
        
        StreamHandler.resetStreams();
        return checkSolution(filenameOut);
        
    }
    
    private boolean checkSolution(String filenameOut){
        //Trim is used to remove any whitespace surrounding the output
        String correctOutput = getCorrectOutput(filenameOut).trim().replaceAll("(\\r|\\n|\\r\\n)+", "");
        String generatedOutput = generatedOutputStream.toString().trim().replaceAll("(\\r|\\n|\\r\\n)+", "");
        boolean check = generatedOutput.equals(correctOutput);
        
        StreamHandler.out.println("Correct Output:   "+correctOutput);
        StreamHandler.out.println("Generated Output: "+generatedOutput);
        StreamHandler.out.println(check);

        return check;
    }
    
    private String getCorrectOutput(String filename){
        FileReader r = null;
        try {
            r = new FileReader(new File(filename));
        } catch (FileNotFoundException e) {
            System.out.println("File not found: "+filename);
        }
        Scanner s = new Scanner(r);
        StringBuilder correctOutput = new StringBuilder();
        while(s.hasNext()) { correctOutput.append(s.nextLine());}
        s.close();
        return correctOutput.toString();
        
    }

}

interface TestFunction {
    public void invoke();
}
