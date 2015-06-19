package testing;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.InputStream;
import java.io.PrintStream;
import java.io.UnsupportedEncodingException;

public class StreamHandler{
    private static final InputStream sIn = System.in;
    protected static final PrintStream out = System.out;
    
    public static ByteArrayOutputStream setOutputStream(int i){
        
        ByteArrayOutputStream x = new ByteArrayOutputStream();
        System.setOut(new PrintStream(x));
        return x;
    } 
    
    public static void setInputFileStream(String filename){
            try {
                System.setIn(new FileInputStream(new File(filename)));
            } catch (FileNotFoundException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }
    }
    
    public static void setOutputStream(ByteArrayOutputStream baos){
        PrintStream y = null;
        try {
            y = new PrintStream(baos, true, "UTF8");
        } catch (UnsupportedEncodingException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        System.setOut(y);
    }
    
    public static void resetStreams(){
        System.out.flush();
        System.setIn(sIn);
        System.setOut(out);
    }
    
}
