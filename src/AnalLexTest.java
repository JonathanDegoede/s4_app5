import java.util.ArrayList;

public class AnalLexTest {

    ArrayList<String> input_files ;
    String output_file;

    public AnalLexTest(ArrayList<String> input_files, String output_file){
        this.input_files = input_files;
        this.output_file = output_file;
    }

    public void Test(){

        String to_write = "";
        System.out.println("Lexical analysis - start");

        for(String file : this.input_files){
            Reader r = new Reader(file);
            String expression = r.toString();
            AnalLex lexical = new AnalLex(expression); // Creation de l'analyseur lexical

            to_write += "\nInput file : " + file + "\n";
            to_write += "Input expression : " + expression + "\n";

            Terminal t = null;
            Error error_msg = null;
            while(lexical.resteTerminal()){
                try{
                    t = lexical.prochainTerminal();
                    to_write += t.type + " " + t.chaine + "\n" ;  // toWrite contient le resultat
                }
                catch(Error e){
                    error_msg = e;
                    break;
                }
            }
            String error_msg_string = (error_msg == null) ? "none" : error_msg.toString();
            to_write += "Error : " + error_msg_string;
            to_write += "\n";
        }
        System.out.println(to_write); 	// Ecriture de toWrite sur la console
        Writer w = new Writer(this.output_file,to_write); // Ecriture de toWrite dans fichier args[1]
        System.out.println("Lexical analysis - end");
    }

    public static void main(String[] args) {
        ArrayList<String> input_files = new ArrayList<>();
        String output_file = "ResultatLexical.txt";

        input_files.add("ExpArith1.txt");
        input_files.add("ExpArith2.txt");

        AnalLexTest tester = new AnalLexTest(input_files, output_file);

        //Tests rapport
        tester.Test();

        //Test autres
    }
}
