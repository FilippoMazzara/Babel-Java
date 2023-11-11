package it.uniroma1.lcl.babelarity;

/**
 * Classe che rappresenta le parole.
 * @author Filippo Mazzara
 *
 */
public class Word implements LinguisticObject {
	
	private String w;
	
	/**
	 * Costruttore della Word.
	 * @param s la stringa contenente la parola
	 */
	private Word(String s) {w=s.toLowerCase();}
	
	/**
	 * Metodo che ritorna una Word.
	 * @param s la parola con cui creare la Word
	 * @return la Word
	 */
	public static Word fromString(String s) {return new Word(s);}
	
	@Override
	public int hashCode(){
		final int prime=31;
		int result=10;
		return prime*result+w.hashCode();
	}
	
	@Override
	public String toString() {return w;}
}

