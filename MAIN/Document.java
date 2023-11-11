package it.uniroma1.lcl.babelarity;

import java.util.ArrayList;
import java.util.List;

/**
 * Classe che rappresenta i documenti.
 * @author Filippo Mazzara
 */
public class Document implements LinguisticObject {
	private String id;
	private String title;
	private String content;
	private List<String> lWords; //lista di parole del contenuto
	
	/**
	 * @return l'id del documento
	 */
	public String getId() {return this.id;}
	
	/**
	 * @return il titolo del documento
	 */
	public String getTitle() {return this.title;}
	
	/**
	 * @return il contenuto del documento
	 */
	public String getContent() {return this.content;}
	
	/**
	 * @return la lista di parole contenute nel documento
	 */
	public List<String> getListOfWords() {return this.lWords;}
	
	/**
	 * Costruttore di documenti.
	 * @param id l'id del documento
	 * @param title il titolo del documento
	 * @param content il contenuto del documento
	 */
	public Document(String id,String title,String content) {
		this.id=id;
		this.title=title;
		this.content=content;
		List<String> l= List.of(CorpusManager.pulisciTesto(content.toLowerCase()).split(" "));
		lWords=new ArrayList<>();
		for (String s:l) {lWords.add(getOriginal(s));} //sostituisce le parole flesse con le non flesse
	}
	
	/**
	 * Metodo che prende il lemma non flesso di una parola
	 * @param s la parola che voglio non flessa
	 * @return la parola non flessa
	 */
	public static String getOriginal(String s) {
		if (MiniBabelNet.getInstance().getLemmatizations().containsKey(s)) {
			 return MiniBabelNet.getInstance().getLemmatizations().get(s);}
		else{return s;}
	}

	
	@Override
	public int hashCode(){
		final int prime=31;
		int result=10;
		return prime*result+id.hashCode();
	}
	
	@Override
	public String toString() {return id+" "+title+" "+content;}

}
