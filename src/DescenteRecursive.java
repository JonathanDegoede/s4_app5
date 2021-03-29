
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
  throw new Error("A syntax error has been detected : " + s  + " at position " + pos);
}

  //Methode principale a lancer pour tester l'analyseur syntaxique 
  public static void main(String[] args) {

    long start_time = System.currentTimeMillis();
    String toWriteLect = "";
    String toWriteEval = "";
    String toWritePostfix = "";

    System.out.println("Debut d'analyse syntaxique");
    if (args.length == 0){
      args = new String [2];
      args[0] = "tests/ExpArith.txt";
      args[1] = "tests/ResultatSyntaxique.txt";
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
    }
    System.out.println("Analyse syntaxique terminee");

    System.out.println(System.currentTimeMillis() - start_time);
  }

}

