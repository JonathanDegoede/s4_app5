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

            to_write += "\n--------------------------------------------------------------------------\n";
            to_write+= "Input file : " + file + "\n";

            Error error_msg = null;
            String evaluation_error = null;
            String fatal_error = null;

            try {
                DescenteRecursive dr = new DescenteRecursive(file);
                to_write+= "Input expression : " + dr.lexical.expression + "\n";
                ElemAST RacineAST = dr.AnalSynt();

                to_write += "Reading AST : " + RacineAST.LectAST() + "\n";

                String evaluation = RacineAST.EvalAST();
                to_write += "Evaluating AST : " + evaluation + "\n";
                evaluation_error = this.isNumeric(evaluation) ? "none" : "Could not fully evaluate this expression because it contains non-numerical symbols";

                to_write += "Postfix AST : " + RacineAST.Postfix() + "\n";

            } catch (Error e) {
                error_msg = e;
                evaluation_error = "Evaluation not executed";
            }
            fatal_error = (error_msg == null) ? "none" : error_msg.toString();
            to_write += "----------------------------------------------------------------------------------------------\n";
            to_write += "Fatal Error : " + fatal_error + "\n";
            to_write += "----------------------------------------------------------------------------------------------\n";
            to_write += "Evaluation Error : " + evaluation_error + "\n";
            to_write += "----------------------------------------------------------------------------------------------\n";
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
            int i = Integer.parseInt(strNum);
        } catch (NumberFormatException nfe) {
            return false;
        }
        return true;
    }

    public static void main(String[] args) {
        ArrayList<String> input_files = new ArrayList<>();
        String output_file = "tests/ResultatSyntaxique.txt";

        input_files.add("tests/input_synt_1.txt");
        input_files.add("tests/input_synt_2.txt");
        input_files.add("tests/input_synt_3.txt");
        input_files.add("tests/input_synt_4.txt");
        input_files.add("tests/input_synt_5.txt");
        input_files.add("tests/input_synt_6.txt");
        input_files.add("tests/input_synt_7.txt");
        input_files.add("tests/input_synt_8.txt");
        input_files.add("tests/input_synt_9.txt");
        input_files.add("tests/input_ast_1.txt");
        input_files.add("tests/input_ast_2.txt");
        input_files.add("tests/input_ast_3.txt");
        input_files.add("tests/input_ast_4.txt");
        input_files.add("tests/input_ast_5.txt");

        DescenteRecursiveTest tester = new DescenteRecursiveTest(input_files, output_file);

        //Tests rapport
        tester.Test();

        //Test autres
    }
}
