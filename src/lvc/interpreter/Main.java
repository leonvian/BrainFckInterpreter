package lvc.interpreter;

public class Main {

    public static void main(String[] args) {

        Interpreter interpreter = new Interpreter();

        interpreter.execute("+++++[>+++++++++++++<-]>.>+++++[>+++++++++++++<-]>+.");
    }


    private static String createPlus(int size) {
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < size; i ++) {
            sb.append("+");
        }
        return sb.toString();
    }

}
