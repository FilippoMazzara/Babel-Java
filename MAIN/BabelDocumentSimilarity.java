package it.uniroma1.lcl.babelarity;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

/**
 * Classe che implementa la misura di similarita' tra documenti.
 * @author Filippo Mazzara
 */
public class BabelDocumentSimilarity implements BabelSimilarity {
	
	MiniBabelNet m=MiniBabelNet.getInstance();
	
	@Override
	public double computeSimilarity(LinguisticObject l1, LinguisticObject l2) {
		return computeDocumentSimilarity((Document) l1, (Document) l2);
	}
	
	/**
	 * Metodo per il calcol della similarita' tra documenti.
	 * @param d1 il primo documento
	 * @param d2 il secondo documento
	 * @return il valore di similarita' tra i due documenti
	 */
	private double computeDocumentSimilarity(Document d1,Document d2) {
		
		//crea i bag of words dei documenti
		HashMap<String,Double> bow1=getBagOfWords(d1.getListOfWords());
		HashMap<String,Double>	bow2=getBagOfWords(d2.getListOfWords());
		
		List<String> lWordsTot= new ArrayList<>();
		lWordsTot.addAll(d2.getListOfWords());
		lWordsTot.addAll(d1.getListOfWords());
		
		HashMap<String,Double> bowtot=getBagOfWords(lWordsTot);
		
		
		HashMap<String,Double> idf=getIDF(bow1,bow2,bowtot);
		HashMap<String,Double> bowidf1=new HashMap<>();
		HashMap<String,Double> bowidf2=new HashMap<>();
		//crea le mappe TF*IDF 
		for (String p:bow1.keySet()) {
			bowidf1.put(p, bow1.get(p)*idf.get(p));}
		
		for (String p:bow2.keySet()) {
			bowidf2.put(p, bow2.get(p)*idf.get(p));}
		
		//conteggio comune TF1*TF2 di ogni parola
		double dot=0.0;
		for (String s:bowtot.keySet()) {
			if (bow1.containsKey(s) && bow2.containsKey(s)) {dot+=bow1.get(s)*bow2.get(s);}
		}
		
		//conteggi dei TF*IDF^2
		double sr1=0.0;
		for(String p1:bowidf1.keySet()) {sr1+=bowidf1.get(p1)*bowidf1.get(p1);}
		
		double sr2=0.0;
		for(String p2:bowidf2.keySet()) {sr2+=bowidf2.get(p2)*bowidf2.get(p2);}
		
		return dot/(Math.sqrt(sr1)*Math.sqrt(sr2));
	}
	
	/**
	 * Metodo che ritorna una mappa con associato ad ogni parola dei documenti il corrispetivo valore IDF opportunamente calcolato.
	 * @param b1 il bag of words del primo documento
	 * @param b2 il bag of words del secondo documento
	 * @param btot il bag of words totale
	 * @return la mappa parole/IDF
	 */
	public HashMap<String,Double> getIDF(HashMap<String,Double> b1,HashMap<String,Double> b2,HashMap<String,Double> btot){
		HashMap<String,Double> idf=new HashMap<>();
		for(String p:btot.keySet()) {
			int c=0; //occorrenze comuni
			if (b1.containsKey(p)) {c++;}
			if (b2.containsKey(p)) {c++;}
			idf.put(p, 1.0+Math.log(2.0/(double)c));}
		return idf;
	}
	
	/**
	 * Metodo che ritorna il bag of words di un documento.
	 * @param l la lista di parole del documento
	 * @return la mappa bag of words parole/frequenza di occorrenza 
	 */
	public HashMap<String,Double> getBagOfWords(List<String> l){
		HashMap<String,Double> bagOfWords =new HashMap<>();
		Set<String> sWords=l.stream().collect(Collectors.toSet()); //set delle parole del documento
		for(String s:sWords) {
			bagOfWords.put(s,(double)Collections.frequency(l, s)/(double)sWords.size());} //calcola la frequenza
		return bagOfWords;
	}

}
