package Testing;

public class Main {
    public static void main(String[] args){
        Test t = new Test("Test", () -> ~Fill~.Program.main(new String[]{}));
        t.testAll();
    }
}
