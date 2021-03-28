/** @author Ahmed Khoumsi */

/** Classe representant une feuille d'AST
 */
public class FeuilleAST extends ElemAST {

  // Attribut(s)
   public Terminal symboleTerminal;

/**Constructeur pour l'initialisation d'attribut(s)
 */
  public FeuilleAST(Terminal t) {  // avec arguments
    //
      this.symboleTerminal  = t;
  }

  /** Evaluation de feuille d'AST
   */
  public String EvalAST( ) {
    //
//      if(this.symboleTerminal.type == Terminal.Type.nb){
//          return Integer.valueOf(this.symboleTerminal.chaine);
//      }
//      else{
//          throw new Error("Cannot evaluate this AST because symbol " + "'" + this.symboleTerminal.chaine + "'" + "is not a number");
//      }
      return this.LectAST();
  }

 /** Lecture de chaine de caracteres correspondant a la feuille d'AST
  */
  public String LectAST( ) {
    //
      return this.symboleTerminal.chaine;
  }

}
