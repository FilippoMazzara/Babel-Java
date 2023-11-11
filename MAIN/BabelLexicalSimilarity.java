package it.uniroma1.lcl.babelarity;

import java.io.File;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

/**
 * Classe che implementa la misura di similarita' lessicale tra due parole.
 * @author Filippo Mazzara
 *
 */
public class BabelLexicalSimilarity implements BabelSimilarity {
	
	@Override
	public double computeSimilarity(LinguisticObject l1, LinguisticObject l2) {
		return cosineSimilarity((Word)l1,(Word)l2);
	}
	

	private HashMap<String,HashMap<String,Double>> vector;
	CorpusManager cm=CorpusManager.getInstance();
	MiniBabelNet m=MiniBabelNet.getInstance(); 
	
	private double cosineSimilarity(Word w1,Word w2) {
		if (vector==null) {
			vector=getVector();}
		System.out.println(vector.keySet().size());
		HashMap<String,Double> mp1=vector.get(w1.toString());
		HashMap<String,Double> mp2=vector.get(w2.toString());
		double somma=0.0;

		for (String s:mp1.keySet()) {
			if (mp2.containsKey(s)) {somma+=mp1.get(s)*mp2.get(s);}
		}
		double somma2=0.0;
		for (String s2:mp1.keySet()) {somma2+=mp1.get(s2)*mp1.get(s2);}
		double somma3=0.0;
		for (String s3:mp2.keySet()) {somma3+=mp2.get(s3)*mp2.get(s3);}
		return somma/(Math.sqrt(somma2)*Math.sqrt(somma3));
	}
	private HashMap<String,HashMap<String,Double>> getVector(){
		HashMap<String,HashMap<String,Double>> v=new HashMap<>();
		List<List<String>> lDoc=new ArrayList<>();
		List<String> paroleTot=new ArrayList<>();
		List<HashMap<String,Integer>> lMapsOcc=new ArrayList<>();
		for(String s:cm.getCorpusLexical()){
			List<String> ls= List.of(s.split(" "));
			lDoc.add(ls);
			paroleTot.addAll(ls);
		}
		System.out.println("ok2");
		HashMap<String,Integer> mapTot=getMapCount(paroleTot);
		for (List<String> l:lDoc) {lMapsOcc.add(getMapCount(l));}
		for (String s:mapTot.keySet()) {v.put(s, getVectorMaps(s, lMapsOcc, mapTot));}
		return v;
	}
	private  HashMap<String,Double> getVectorMaps(String s,List<HashMap<String,Integer>> lMapsOcc,HashMap<String,Integer> mapTot){
		HashMap<String,Double> hm=new HashMap<>();
		for (String ss:mapTot.keySet()) {hm.put(ss, pmi(s,ss,lMapsOcc,mapTot));}
		return hm;
		}
	
	private double pmi(String s1,String s2,List<HashMap<String,Integer>> lMapsOcc,HashMap<String,Integer> mapTot) {
		int c=0;
		for (HashMap<String,Integer> hm:lMapsOcc) {if (hm.containsKey(s1) && hm.containsKey(s2)) {c++;}}
		return ((double) c/((double)mapTot.get(s1)*(double)mapTot.get(s2)));
	}
	

	private HashMap<String,Integer> getMapCount(List<String> l){
		HashMap<String,Integer> map=new HashMap<>();
		Set<String> insiemeParole=l.stream().collect(Collectors.toSet());
		for(String s:insiemeParole) {
			map.put(s,Collections.frequency(l, s));
			}
		return map;
	}
	/**
	 * Metodo per misurare la similarita' tra due parole.
	 * @param w1 la prima parola
	 * @param w2 la seconda parola
	 * @return la similarita' tra le due parole
	 */
	/**
	private double computeLexicalSimilarity(Word w1,Word w2) {
		
		String word1=w1.toString();
		String word2=w2.toString();
		
		MiniBabelNet m=MiniBabelNet.getInstance();
		
		//controlla se le parole non sono flesse
		if (m.getLemmatizations().containsKey(word1)) {word1=m.getLemmatizations().get(word1);}
		if (m.getLemmatizations().containsKey(word2)) {word2=m.getLemmatizations().get(word2);}
		
		//liste di synsets contenenti le words
		List<Synset> l1=m.getSynsets(word1);
		List<Synset> l2=m.getSynsets(word2);
		
		StringBuilder sb1=new StringBuilder();
		StringBuilder sb2=new StringBuilder();
		
		//cicli che mettono insieme tutti i gloss
		for(Synset bs:l1) {for (String gloss:bs.getGlosses()) {
			gloss=gloss.toLowerCase();
			sb1.append(gloss+" ");}
		}
		
		for(Synset bn:l2) {for (String gloss:bn.getGlosses()) {
			gloss=gloss.toLowerCase();
			sb2.append(gloss+" ");}
		}
		
		//liste di parole contenute nei due testi
		List<String> parole1=List.of(pulisciTesto(sb1.toString()).split(" "));
		List<String> parole2=List.of(pulisciTesto(sb2.toString()).split(" "));
		
		HashSet<String> paroleComuni=new HashSet<>();
		
		Integer conta1=0;
		Integer conta2=0;
		
		//ciclo per contare le occorrenze di tutte le parole nel primo testo (si appoggia ad un insieme di parole comuni)
		for (String p:parole1) {
			if (!(paroleComuni.contains(p) || paroleComuni.contains(m.getLemmatizations().get(p)))) {
				if(parole2.contains(p)) {
					if (m.getLemmatizations().containsKey(p)) {paroleComuni.add(m.getLemmatizations().get(p));}
					else{paroleComuni.add(p);}
					conta1++;}
				}
			else {conta1++;}
		}
		//per il secondo testo
		for (String p:parole2) {
			if (!(paroleComuni.contains(p) || paroleComuni.contains(m.getLemmatizations().get(p)))) {
				if(parole1.contains(p)) {
					if (m.getLemmatizations().containsKey(p)) {paroleComuni.add(m.getLemmatizations().get(p));}
					else{paroleComuni.add(p);}
					conta2++;}
			}
			else {conta2++;}
		}
		return (((double)conta1+(double)conta2)/((double)parole1.size()+(double)parole2.size()));
		}
	
	/**
	 * Metodo per pulire il testo dei glossari.
	 * @param s la stringa di testo da pulire
	 * @return la stringa pulita
	 */
	private String pulisciTesto(String s) {
		List<String> punt=List.of(".",",",")","\"","(",";",":","_","","'","#","[","]","}","{","*","=","?","!","/");
		String finale=s;
		
		for(String c:punt) {
			finale=finale.replace(c, "");
		}
		
		return finale;}
}
