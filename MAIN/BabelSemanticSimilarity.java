package it.uniroma1.lcl.babelarity;

import it.uniroma1.lcl.babelarity.MiniBabelNet.SynsetIterator;

/**
 * Classe che implementa l'algoritmo di similarita' tra i synsets.
 * @author Filippo Mazzara
 */
public class BabelSemanticSimilarity implements BabelSimilarity {

	@Override
	public double computeSimilarity(LinguisticObject l1, LinguisticObject l2) {
		return computeSemanticSimilarity((Synset)l1, (Synset)l2);
	}
	
	/**
	 * Metodo per il calcolo della similarita' tra due synset.
	 * @param s1 il primo synset
	 * @param s2 il secondo synset
	 * @return il loro valore di similarita'
	 */
	public double computeSemanticSimilarity(Synset s1,Synset s2) {
		int d=getDistanza(s1,s2);
		return 1.0/((double)d+1.0);
	}
	
	/**
	 * Metodo per il calcolo della distanza tra due synset nel grafo.
	 * @param s1 il primo synset
	 * @param s2 il secondo synset
	 * @return la distanza tra i due synset
	 */
	private int getDistanza(Synset s1,Synset s2) {
		SynsetIterator si=MiniBabelNet.getInstance().iterator(s1);
		int d=0;
		while (!si.next().getID().equals(s2.getID())) {
			d=si.getDinstance(); //si ferma quando ha trovato il secondo nodo
		}
		return d;
	}
}
