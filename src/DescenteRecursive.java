
/** @author Ahmed Khoumsi */

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

  return ast;
}


// Methode pour chaque symbole non-terminal de la grammaire retenue
// ... 
// ...
  private ElemAST S(){

    ElemAST n1 = T();

    if(this.current_terminal.type == Terminal.Type.op && this.lexical.isValidChar(current_terminal.chaine.charAt(0), "[+\\-]")){

      Terminal op_copy = this.current_terminal;
      if(this.lexical.resteTerminal()){
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
      if(this.lexical.resteTerminal()){
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

        if (this.lexical.resteTerminal()) {
          this.current_terminal = this.lexical.prochainTerminal();
        }
        n = S();

        if(this.current_terminal.chaine.charAt(0) != ')'){
          this.ErreurSynt("Found error at position " + (this.lexical.ptr - 1) + " with unexpected char : " + this.current_terminal.chaine);
        }

        if (this.lexical.resteTerminal()) {
          this.current_terminal = this.lexical.prochainTerminal();
        }
      }
      else{
        this.ErreurSynt("Found error at position " + (this.lexical.ptr - 1) + " with unexpected char : " + this.current_terminal.chaine);
      }
    }
    else {

      n = new FeuilleAST(this.current_terminal);
      if (this.lexical.resteTerminal()) {
        this.current_terminal = this.lexical.prochainTerminal();
      }
    }

    return n;
  }

//private ElemAST T(){
//  if(this.current_terminal.type != Terminal.Type.op){
//    ElemAST n = new FeuilleAST(this.current_terminal);
//    this.current_terminal = this.lexical.prochainTerminal();
//    return n;
//  }
//  else{
//    ErreurSynt("Found error at position " + this.lexical.ptr + "with unexpected char : " + this.current_terminal.chaine);
//  }
//  return null;
//}
//
//private ElemAST E(){
//
//  ElemAST n1 = T();
//
//  if(this.current_terminal.type == Terminal.Type.op){
//    Terminal copy = this.current_terminal; // pas sur
//    this.current_terminal = this.lexical.prochainTerminal();
//    ElemAST n2 = E();
//    n1 = new NoeudAST(n1, n2, copy);
//  }
//  return n1;
//}

/** ErreurSynt() envoie un message d'erreur syntaxique
 */
public void ErreurSynt(String s)
{
  throw new Error("A syntax error has been detected : " + s);
}

  //Methode principale a lancer pour tester l'analyseur syntaxique 
  public static void main(String[] args) {
    String toWriteLect = "";
    String toWriteEval = "";

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
      Writer w = new Writer(args[1],toWriteLect+toWriteEval); // Ecriture de toWrite 
                                                              // dans fichier args[1]
    } catch (Exception e) {
      System.out.println(e);
      e.printStackTrace();
      System.exit(51);
    }
    System.out.println("Analyse syntaxique terminee");
  }

}

