import java.util.ArrayList;

public class AnalLexTest {

    ArrayList<String> input_files ;
    String output_file;

    public AnalLexTest(ArrayList<String> input_files, String output_file){
        this.input_files = input_files;
        this.output_file = output_file;
    }

    public void Test(){

        String toWrite = "";
        System.out.println("Debut d'analyse lexicale");

        for(String file : this.input_files){
            Reader r = new Reader(file);
            String expression = r.toString();
            AnalLex lexical = new AnalLex(expression); // Creation de l'analyseur lexical

            toWrite += "\nInput file : " + file + "\n";
            toWrite += "Input expression : " + expression + "\n";

            Terminal t = null;
            Error error_msg = null;
            while(lexical.resteTerminal()){
                try{
                    t = lexical.prochainTerminal();
                    toWrite += t.type + " " + t.chaine + "\n" ;  // toWrite contient le resultat
                }
                catch(Error e){
                    error_msg = e;
                    break;
                }
            }
            String error_msg_string = (error_msg == null) ? "none" : error_msg.toString();
            toWrite += "Error : " + error_msg_string;
            toWrite += "\n";
        }
        System.out.println(toWrite); 	// Ecriture de toWrite sur la console
        Writer w = new Writer(this.output_file,toWrite); // Ecriture de toWrite dans fichier args[1]
        System.out.println("Fin d'analyse lexicale");
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
