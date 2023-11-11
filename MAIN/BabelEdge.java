package it.uniroma1.lcl.babelarity;

/**
 * Classe che rappresenta gli archi del grafo.
 * @author Filippo Mazzara
 */
public class BabelEdge {
	private Synset synsetDest;
	private String relazioneShort;
	private String relazioneLong;
	
	/**
	 * Costruttore degli archi.
	 * @param s il synset destinazione a cui punta l'arco
	 * @param rs la relazione tra i due nodi breve
	 * @param rl la relazione tra i due nodi lunga
	 */
	public BabelEdge(Synset s,String rs,String rl) {
		synsetDest=s;
		relazioneShort=rs;
		relazioneLong=rl;}
	
	@Override
	public int hashCode(){
		final int prime=31;
		int result=10;
		return prime*result+synsetDest.hashCode()+relazioneShort.hashCode();
	}
	
	@Override
	public String toString() {return synsetDest.getID()+"_"+relazioneShort;}
	
	/**
	 * Metodo che ritorna il synset a cui punta l'arco.
	 * @return il synset destinazione
	 */
	public Synset getSynsetDest() {return synsetDest;}
}
