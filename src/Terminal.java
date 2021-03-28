/** @author Ahmed Khoumsi */

/** Cette classe identifie les terminaux reconnus et retournes par
 *  l'analyseur lexical
 */
public class Terminal {

// Constantes et attributs
//  ....
    public String chaine;
    public Type type;
    public enum Type{
        nb,
        ch,
        op,
        eof
    };

/** Un ou deux constructeurs (ou plus, si vous voulez)
  *   pour l'initalisation d'attributs 
 */	
  public Terminal(String chaine, Type type) {   // arguments possibles
     //
    this.chaine = chaine;
    this.type = type;
  }

}
