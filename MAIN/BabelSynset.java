package it.uniroma1.lcl.babelarity;

import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

/**
 * Classe che definisce un BabelSynset, un nodo del grafo.
 * @author Filippo Mazzara
 */
public class BabelSynset implements Synset {
	private String id;
	private POS pos;
	private Set<String> lemmas;
	private List<String> glosses;
	
	/**
	 * Enumerazione contenente le possibili parti del discorso per un synset.
	 * @author Filippo Mazzara
	 *
	 */
	public enum POS{NOUN,ADV,ADJ,VERB}
	
	/**
	 * Costruttore di BabelSynset.
	 * @param ip l'id del synset
	 * @param l l'insieme di lemmi associati al synset
	 * @param g la lista con i glossari associati al synset
	 */
	public BabelSynset(String ip,Set<String> l,List<String> g) {
		String id=ip;
		char p=ip.charAt(ip.length()-1);
		lemmas=l;
		glosses=g;
		if (p=='n') {pos=POS.NOUN;}
		else if (p=='v') {pos=POS.VERB;}
		else if(p=='a') {pos=POS.ADJ;}
		else if(p=='r') {pos=POS.ADV;}
		this.id=id;
	}
	
	@Override
	public int hashCode(){
		final int prime=31;
		int result=10;
		return prime*result+id.hashCode();
	}
	
	@Override
	public String toString() {
		return id+"\t"+pos.toString()+"\t"+lemmas.stream().collect(Collectors.joining(";"))+"\t"+glosses.stream().collect(Collectors.joining(";"))+"\t"+MiniBabelNet.getInstance().stringaTotale(this);
	}
	
	/**
	 * Getter dell'id del documento.
	 */
	public String getID(){return id;}
	
	/**
	 * Getter della parte del discorso del documento.
	 */
	public POS getPos() {return pos;}
	
	/**
	 * Getter dei lemmi contenuti nel synset.
	 */
	public Set<String> getLemmas(){return lemmas;}
	
	/**
	 * Getter dei glossari del synset.
	 */
	public List<String> getGlosses(){return glosses;}
	
	

}