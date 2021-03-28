/** @author Ahmed Khoumsi */

import javax.swing.plaf.ColorUIResource;

/** Cette classe effectue l'analyse lexicale
 */
public class AnalLex {

// Attributs
//  ...
int ptr;
int state;
String expression;

	
/** Constructeur pour l'initialisation d'attribut(s)
 */
  public AnalLex(String s) {  // arguments possibles
    //
    this.expression = s.replaceAll("\\s+", "");
    this.ptr = 0;
    this.state = 0;
  }

/** resteTerminal() retourne :
      false  si tous les terminaux de l'expression arithmetique ont ete retournes
      true s'il reste encore au moins un terminal qui n'a pas ete retourne 
 */
  public boolean resteTerminal( ) {
    return this.ptr < this.expression.length();
  }
  
  
/** prochainTerminal() retourne le prochain terminal
      Cette methode est une implementation d'un AEF
 */  
  public Terminal prochainTerminal() {

    String chaine = "";
    this.state = 0;

    while(true) {
      char current_char;

      if(this.resteTerminal()){
        current_char = this.expression.charAt(this.ptr);
      }
      else{
        current_char = '\0';
      }
      this.ptr++;

      switch (this.state) {

          case 0:
            if(isValidChar(current_char, "[0-9]")){
              chaine += current_char;
              this.state = 1;
            }
            else if(isValidChar(current_char, "[A-Z]")){
              chaine += current_char;
              this.state = 2;
            }
            else if(isValidChar(current_char, "[+\\-()*/]")){
              chaine += current_char;
              return new Terminal(chaine, Terminal.Type.op);
            }
            else if(current_char == '\0'){
              chaine += current_char;
              return new Terminal(chaine, Terminal.Type.eof);
            }
            else{
              this.ErreurLex("0. Invalid char placement or unknown char encountered : " + "'" + current_char + "'" , this.ptr);
            }
            break;

        case 1:
          if(isValidChar(current_char, "[0-9]")){
            chaine += current_char;
          }
          else {
            this.ptr--;
            return new Terminal(chaine, Terminal.Type.nb);
          }
          break;

        case 2:
          if(isValidChar(current_char, "[A-Za-z]")){
            chaine += current_char;
          }
          else if(current_char == '_'){
            chaine += current_char;
            this.state = 3;
          }
          else {
            this.ptr--;
            return new Terminal(chaine, Terminal.Type.ch);
          }
          break;

        case 3:
          if(isValidChar(current_char, "[A-Z|a-z]")){
            chaine += current_char;
            this.state = 2;
          }
          else{
            this.ErreurLex("3. Invalid char placement or unknown char encountered : " + "'" + current_char + "'" , this.ptr);
          }
          break;

        default:
          throw new Error("D. this state doesnt exist");
      }
    }
  }

/** ErreurLex() envoie un message d'erreur lexicale
 */ 
  public void ErreurLex(String s, int pos) {
     throw new Error("A Lexical error has been detected : " + s + " at position " + pos);//
  }

  public boolean isValidChar(char verified_char, String regex){
    return String.valueOf(verified_char).matches(regex);
  }
  
  //Methode principale a lancer pour tester l'analyseur lexical
  public static void main(String[] args) {
    String toWrite = "";
    System.out.println("Debut d'analyse lexicale");
    if (args.length == 0){
    args = new String [2];
            args[0] = "ExpArith.txt";
            args[1] = "ResultatLexical.txt";
    }
    Reader r = new Reader(args[0]);

    AnalLex lexical = new AnalLex(r.toString()); // Creation de l'analyseur lexical

    // Execution de l'analyseur lexical
    Terminal t = null;
    while(lexical.resteTerminal()){
      t = lexical.prochainTerminal();
      toWrite +=t.chaine + "\n" ;  // toWrite contient le resultat
    }				   //    d'analyse lexicale
    System.out.println(toWrite); 	// Ecriture de toWrite sur la console
    Writer w = new Writer(args[1],toWrite); // Ecriture de toWrite dans fichier args[1]
    System.out.println("Fin d'analyse lexicale");
  }
}
