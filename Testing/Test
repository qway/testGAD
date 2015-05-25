package Testing;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.util.Arrays;
import java.util.Scanner;
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
        for(String e : data){
            e = e.replace(".in", "");
            System.out.println("------------------------\n"
                             + "Case "+e+"\n"
                             + "------------------------");
            if(!testCase(e)) break;
        }
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
        String correctOutput = "";
        while(s.hasNext()) correctOutput += s.nextLine();
        s.close();
        return correctOutput;
        
    }

}

interface TestFunction {
    public void invoke();
}
