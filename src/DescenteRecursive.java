
/** @author Ahmed Khoumsi */

import com.sun.source.tree.TryTree;

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
  this.file_name = file;
  Reader r = new Reader(this.file_name);
  this.lexical = new AnalLex(r.toString());
  //this.lexical = new AnalLex(r.toString().replaceAll("[()]", "")); // Creation de l'analyseur lexical

  //A CHANGER : NE PAS ENLEVER LES PARANTHESES , SINON LES PRIORITES SUR LES PARANTHESES SONT FUCKED

  this.current_terminal = this.lexical.prochainTerminal();
}


/** AnalSynt() effectue l'analyse syntaxique et construit l'AST.
 *    Elle retourne une reference sur la racine de l'AST construit
 */
public ElemAST AnalSynt( ) {

  ElemAST ast = this.S();
  if(this.current_terminal.type != Terminal.Type.eof){
    this.ErreurSynt("A. Invalid char placement (parenthesis error) : " + "'" + this.current_terminal.chaine + "'" , (this.lexical.ptr - 1));
  }

  return ast;
}

// Methode pour chaque symbole non-terminal de la grammaire retenue
// ... 
// ...
  private ElemAST S(){

    ElemAST n1 = T();

    if(this.current_terminal.type == Terminal.Type.op && this.lexical.isValidChar(current_terminal.chaine.charAt(0), "[+\\-]")){

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

    if(this.current_terminal.type == Terminal.Type.op && this.lexical.isValidChar(this.current_terminal.chaine.charAt(0), "[*/]")){
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

    if (this.current_terminal.type == Terminal.Type.op){

      if(this.current_terminal.chaine.charAt(0) == '('){

        this.current_terminal = this.lexical.prochainTerminal();
        n = S();

        if(this.current_terminal.chaine.charAt(0) == ')'){
          this.current_terminal = this.lexical.prochainTerminal();
        }
        else{
          this.ErreurSynt("U1. Invalid char placement (parenthesis error) : " + "'" + this.current_terminal.chaine + "'", (this.lexical.ptr - 1));
        }
      }
      else{
        this.ErreurSynt("U2. Invalid char placement : " + "'" + this.current_terminal.chaine + "'", (this.lexical.ptr - 1));
      }
    }
    else {
      n = new FeuilleAST(this.current_terminal);
      this.current_terminal = this.lexical.prochainTerminal();
    }

    return n;
  }

/** ErreurSynt() envoie un message d'erreur syntaxique
 */
public void ErreurSynt(String s, int pos)
{
  throw new Error("A syntax error has been detected : " + s  + " at position " + pos);
}

  //Methode principale a lancer pour tester l'analyseur syntaxique 
  public static void main(String[] args) {
    String toWriteLect = "";
    String toWriteEval = "";
    String toWritePostfix = "";

    System.out.println("Debut d'analyse syntaxique");
    if (args.length == 0){
      args = new String [2];
      args[0] = "ExpArith.txt";
      args[1] = "ResultatSyntaxique.txt";
    }
    DescenteRecursive dr = new DescenteRecursive(args[0]);
    try {
      ElemAST RacineAST = dr.AnalSynt();
      toWriteLect += "Lecture de l'AST trouve : " + RacineAST.LectAST() + "\n";
      System.out.println(toWriteLect);
      toWriteEval += "Evaluation de l'AST trouve : " + RacineAST.EvalAST() + "\n";
      System.out.println(toWriteEval);
      toWritePostfix += "Postfix de l'AST trouve : " + RacineAST.Postfix() + "\n";
      System.out.println(toWritePostfix);
      Writer w = new Writer(args[1],toWriteLect+toWriteEval); // Ecriture de toWrite 
                                                              // dans fichier args[1]
    } catch (Error e) {
      System.out.println(e);
      e.printStackTrace();
      System.exit(51);
    }
    System.out.println("Analyse syntaxique terminee");
  }

}

