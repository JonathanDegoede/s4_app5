/** @author Ahmed Khoumsi */

/** Classe representant une feuille d'AST
 */
public class NoeudAST extends ElemAST {

  // Attributs
  ElemAST gauche;
  ElemAST droit;
  Terminal operateur;

  /** Constructeur pour l'initialisation d'attributs
   */
  public NoeudAST(ElemAST g, ElemAST d, Terminal o) { // avec arguments
    //
    this.gauche = g;
    this.droit = d;
    this.operateur = o;
  }
 
  /** Evaluation de noeud d'AST
   */
  public String EvalAST( ) {
     //
    String symbol = this.operateur.chaine;
    String result;

    try{
      switch(symbol){
        case "+" :
          result = Integer.toString(Integer.parseInt(this.gauche.EvalAST()) + Integer.parseInt(this.droit.EvalAST()));
          break;
        case "-" :
          result = Integer.toString(Integer.parseInt(this.gauche.EvalAST()) - Integer.parseInt(this.droit.EvalAST()));
          break;
        case "/" :
          result = Integer.toString(Integer.parseInt(this.gauche.EvalAST()) / Integer.parseInt(this.droit.EvalAST()));
          break;
        case "*" :
          result = Integer.toString(Integer.parseInt(this.gauche.EvalAST()) * Integer.parseInt(this.droit.EvalAST()));
          break;
        default :
          throw new Error("Cannot evaluate this AST because symbol " + "'" + symbol + "'" + "is not accepted");
      }
    }
    catch(Exception e) {
      switch (symbol) {
        case "+":
          result = " ( " + this.gauche.EvalAST() + " + " + this.droit.EvalAST() + " ) ";
          break;
        case "-":
          result = " ( " + this.gauche.EvalAST() + " - " + this.droit.EvalAST() + " ) ";
          break;
        case "/":
          result = " ( " + this.gauche.EvalAST() + " / " + this.droit.EvalAST() + " ) ";
          break;
        case "*":
          result = " ( " + this.gauche.EvalAST() + " * " + this.droit.EvalAST() + " ) ";
          break;
        default:
          throw new Error("Cannot evaluate this AST because symbol " + "'" + symbol + "'" + "is not accepted");
      }
    }

    return result;
  }

  /** Lecture de noeud d'AST
   */
  public String LectAST( ) {
    return " ( " + this.gauche.LectAST() + " " + this.operateur.chaine + " " + this.droit.LectAST() + " ) " ;
  }

}


