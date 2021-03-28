import java.awt.image.TileObserver;
import java.util.ArrayList;

public class DescenteRecursiveTest {

    ArrayList<String> input_files ;
    String output_file;

    public DescenteRecursiveTest(ArrayList<String> input_files, String output_file){
        this.input_files = input_files;
        this.output_file = output_file;
    }

    public void Test(){

        String to_write = "";

        System.out.println("Syntax analysis - start");

        for(String file : this.input_files){


            to_write+= "\nInput file : " + file + "\n";

            Error error_msg = null;
            String small_error = null;
            String error_msg_string = null;

            try {
                DescenteRecursive dr = new DescenteRecursive(file);
                to_write+= "Input expression : " + dr.lexical.expression + "\n";
                ElemAST RacineAST = dr.AnalSynt();
                to_write += "Reading AST : " + RacineAST.LectAST() + "\n";

                String evaluation = RacineAST.EvalAST();
                to_write += "Evaluating AST : " + evaluation + "\n";
                small_error = this.isNumeric(evaluation) ? "none" : "Could not fully evaluate this expression because it contains non-numerical symbols";

                to_write += "Postfix AST : " + RacineAST.Postfix() + "\n";

            } catch (Error e) {
                error_msg = e;
            }
            error_msg_string = (error_msg == null) ? "none" : error_msg.toString();
            to_write += "Fatal Error : " + error_msg_string + "\n";
            error_msg_string = (small_error == null) ? "none" : small_error;
            to_write += "Evaluation Error : " + small_error + "\n";
        }
            System.out.println(to_write);
            Writer w = new Writer(this.output_file,to_write);
            System.out.println("Syntax analysis - end");
    }

    public static boolean isNumeric(String strNum) {
        if (strNum == null) {
            return false;
        }
        try {
            double d = Double.parseDouble(strNum);
        } catch (NumberFormatException nfe) {
            return false;
        }
        return true;
    }

    public static void main(String[] args) {
        ArrayList<String> input_files = new ArrayList<>();
        String output_file = "ResultatLexical.txt";

        input_files.add("ExpArith1.txt");
        input_files.add("ExpArith2.txt");

        DescenteRecursiveTest tester = new DescenteRecursiveTest(input_files, output_file);

        //Tests rapport
        tester.Test();

        //Test autres
    }
}
