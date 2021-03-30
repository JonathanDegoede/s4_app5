
/** @author Ahmed Khoumsi */

import com.sun.source.tree.TryTree;

import java.util.ArrayList;
import java.util.Stack;

/** Cette classe effectue l'analyse syntaxique
 */
public class DescenteRecursive {

  // Attributs
  String file_name;
  Terminal current_terminal;
  AnalLex lexical;

/** Constructeur de DescenteRecursive :
      - recoit en argument le nom du fichier contenant l'expression a analyser
      - pour l'initalisation d'attribut(s)
 */
public DescenteRecursive(String file) {
    //
  try{
    this.file_name = file;
    Reader r = new Reader(this.file_name);
    this.lexical = new AnalLex(r.toString());
    this.current_terminal = this.lexical.prochainTerminal();
  }
  catch(Error e){
    throw new Error(e);
  }
}


/** AnalSynt() effectue l'analyse syntaxique et construit l'AST.
 *    Elle retourne une reference sur la racine de l'AST construit
 */
public ElemAST AnalSynt( ) {

  ElemAST ast = this.S();
  if(this.current_terminal.type != Terminal.Type.eof){
    this.ErreurSynt("A. Invalid char placement (parenthesis error) : " + "'" + this.current_terminal.chaine + "' expected ' '" , (this.lexical.ptr - 1));
  }

  return ast;
}

// Methode pour chaque symbole non-terminal de la grammaire retenue
// ... 
// ...
  private ElemAST S(){

    ElemAST n1 = T();

    if(this.lexical.isValidChar(current_terminal.chaine, "[+\\-]")){

      Terminal op_copy = this.current_terminal;
      if(lexical.resteTerminal()){
        this.current_terminal = this.lexical.prochainTerminal();
      }
      ElemAST n2 = S();
      n1 = new NoeudAST(n1, n2, op_copy);
    }

    return n1;

  }

  private ElemAST T(){

    ElemAST n1 = U();

    if(this.lexical.isValidChar(this.current_terminal.chaine, "[*/]")){
      Terminal op_copy = this.current_terminal;
      if(lexical.resteTerminal()){
        this.current_terminal = this.lexical.prochainTerminal();
      }
      ElemAST n2 = T();
      n1 = new NoeudAST(n1, n2, op_copy);
    }

    return n1;
  }

  private ElemAST U(){

    ElemAST n = null;

    if(this.current_terminal.chaine.equals("(")){

        this.current_terminal = this.lexical.prochainTerminal();
        n = S();

        if(this.current_terminal.chaine.equals(")")){
          this.current_terminal = this.lexical.prochainTerminal();
        }
        else{
          this.ErreurSynt("U1. Invalid char placement (missing parenthesis error) : " + "'" + this.current_terminal.chaine + "' expected ')'", (this.lexical.ptr - 1));
        }
    }
    else if(this.current_terminal.type == Terminal.Type.id || this.current_terminal.type == Terminal.Type.nb){
      n = new FeuilleAST(this.current_terminal);
      this.current_terminal = this.lexical.prochainTerminal();
    }
    else{
      this.ErreurSynt("U2. Invalid char placement : " + "'" + this.current_terminal.chaine + "' expected [id|nb|(]", (this.lexical.ptr - 1));
    }

    return n;
  }

/** ErreurSynt() envoie un message d'erreur syntaxique
 */
public void ErreurSynt(String s, int pos)
{
  String pointer = " ";
  pointer = pointer.repeat(this.lexical.ptr - 1) + "^";
  throw new Error("A syntax error has been detected : " + s  + " at position " + pos + "\n" +
                  "Expression : " + this.lexical.expression + "\n" +
                  "Position   : " + pointer);
}

  //Methode principale a lancer pour tester l'analyseur syntaxique 
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
    input_files.add("tests/input_ast_6.txt");

    DescenteRecursiveTest tester = new DescenteRecursiveTest(input_files, output_file);

    //Tests rapport
    tester.Test();

    //Test autres
  }
}

