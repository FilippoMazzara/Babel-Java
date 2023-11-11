package it.uniroma1.lcl.babelarity;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.LinkedHashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.Queue;
import java.util.Set;
import java.util.stream.Collectors;

/**
 * MiniBabelNet e' un framework di similarita' tra oggetti linguistici.
 * @author Filippo Mazzara
 * @since 08/08/2018
 * @version 1.2
**/
public class MiniBabelNet{
	private static MiniBabelNet instance; //istanza di MiniBabelNet
	
	private HashMap<Synset,Set<BabelEdge>> semanticNetwork; //rete semantica di synsets/insiemi di archi
	
	private HashMap<String,String> lemmatizations; //mappa di tutte le forme flesse delle parole
	private HashMap<String,List<String>> lemmas; //mappa di tutte le parole associate alle loro forme flesse 
	private LinkedHashMap<String,List<String>> glosses; //mappa dei glossari delle parole
	private HashMap<String,List<BabelEdge>> relations; //mappa di tutti i synsets(id) associati ai corrispettivi insiemi di archi
	private HashMap<String,Synset> synsets; //mappa di tutti i sysnsets associati ai loro id
	
	private BabelSimilarity strategy; //tipo di similarita' usata per il confronto
	
	/**
	 *Costruttore di MiniBabelNet, crea la rete semantica.
	 */
	private MiniBabelNet() {
		//parsing del file con le forme flesse e successiva creazione delle mappe di lemmi
		lemmas=new HashMap<>();
		lemmatizations=new HashMap<>();
		String line="";
		final String PATHL="resources/lemmatization-en.txt";
		try (BufferedReader bfr=new BufferedReader(new FileReader(PATHL))){
			 while ((line = bfr.readLine()) != null) {
				 	String[] l=line.split("\t");
				 	
				 	if (!lemmas.containsKey(l[1])) {lemmas.put(l[1],new ArrayList<String>());}
				 	
	               	lemmas.get(l[1]).add(l[0]);
			 		lemmatizations.put(l[0], l[1]);
	            }
			 }
		catch (IOException e) {e.printStackTrace();}
		
		//parsing del file con le definizioni di ogni synset e creazione della mappa dei glossari
		glosses=new LinkedHashMap<>();
		String line2="";
		final String PATHG="resources/glosses.txt";
		try (BufferedReader bfr=new BufferedReader(new FileReader(PATHG))){
			 while ((line2 = bfr.readLine()) != null) {
				 	String[] l=line2.split("\t");
				 	List<String> ls=List.of(l);
	               	glosses.put(l[0],ls.subList(1, ls.size()));
	               	} 
	            }
		catch (IOException e) {e.printStackTrace();}
		
		//parsing del file con i sinonimi di ogni parola
		synsets=new HashMap<>();
		semanticNetwork=new HashMap<>();
		String line3="";
		final String PATHD="resources/dictionary.txt";
		try (BufferedReader bfr=new BufferedReader(new FileReader(PATHD))){
			 while ((line3 = bfr.readLine()) != null) {
				 	String[] l=line3.split("\t");
				 	LinkedHashSet<String> s=new LinkedHashSet<>();
				 	
				 	for(int x=1;x<l.length;x++) {s.add(l[x]);}
				 	
				 	BabelSynset b=new BabelSynset(l[0],s,glosses.get(l[0])); //creazione dei nodi del grafo(synsets)
				 	synsets.put(l[0], b); //creazione mappa id/synset
				 	
	               	if (!semanticNetwork.containsKey(b)) {semanticNetwork.put(b,new HashSet<BabelEdge>());} //inizializzazione della rete semantica
	               	} 
	            }
		catch (IOException e) {e.printStackTrace();}
		
		//parsing del file contenente le relazioni tra i synset
		final String PATHR="resources/relations2.txt";
		relations=new HashMap<>();
		try (BufferedReader bfr=new BufferedReader(new FileReader(PATHR))){
			while (bfr.ready()) {
				 String[] l=bfr.readLine().split("\t");
				 //aggiunta di archi ai synset nella mappa di relazioni
				 if (relations.containsKey(l[0])) {
					 relations.get(l[0]).add(new BabelEdge(getSynset(l[1]),l[2],l[3]));}
				 else {
					 List<BabelEdge> l2 =new ArrayList<>();
					 l2.add(new BabelEdge(getSynset(l[1]),l[2],l[3]));
					 relations.put(l[0], l2);}
				}
				bfr.close();
			}
		catch (IOException e) {e.printStackTrace();}
		
		//aggiunta di archi ai synset della rete semantica
		for (Synset s:semanticNetwork.keySet()) {
			if (relations.containsKey(s.getID())) {
				semanticNetwork.get(s).addAll(relations.get(s.getID()));
			}
		}
	}
	
	/**
	 * Metodo per la creazione/accesso alla rete semantica.
	 * @return l'istanza di MiniBabelNet
	 */
	public static MiniBabelNet getInstance() {
		if (instance==null) {instance=new MiniBabelNet();}
		return instance;
		}
	
	/**
	 * Metodo che ritorna il synset dall'id specificato.
	 * @param id l'id di un synset da cercare
	 * @return il synset dall'id specificato
	 */
	public Synset getSynset(String id) {return synsets.get(id);}
	
	/**
	 * @param s il synset di cui voglio gli archi
	 * @return l'insieme di archi collegati al synset
	 */
	public Set<BabelEdge> getRelations(Synset s){return semanticNetwork.get(s);}
	
	/**
	 * Metodo che permette di trovare tutti i synsets che hanno tra i propri lemmi una determinata parola.
	 * @param word parola contenuta nei lemmi del synset
	 * @return lista dei synsets che hanno tra i loro lemmi word
	 */
	public List<Synset> getSynsets(String word){return semanticNetwork.keySet().stream().filter(s->s.getLemmas().contains(word)).collect(Collectors.toList());}
	
	/**
	 * Metodo che permette di trovare tutte le forme flesse di una parola.
	 * @param word parola non flessa
	 * @return tutti i lemmi flessi associati a word
	 */
	public List<String> getLemmas(String word){return lemmas.get(word);}
	
	/**
	 * Metodo che ritorna la rappresentazione sotto forma di stringa dei dati contenuti in un synset.
	 * @param s il synset di cui voglio il summary
	 * @return il summary del synset
	 */
	public String getSynsetSummary(Synset s) {return s.toString();}
	
	/**
	 * Metodo che imposta come metodo di similarita' quella lessicale
	 */
	private void setLexicalSimilarityStrategy() {strategy=new BabelLexicalSimilarity();}
	
	/**
	 * Metodo che imposta come metodo di similarita' quella semantica
	 */
	private void setSemanticSimilarityStrategy() {strategy=new BabelSemanticSimilarity();}
	
	/**
	 * Metodo che imposta come metodo di similarita' quella tra documenti
	 */
	private void setDocumentSimilarityStrategy() {strategy=new BabelDocumentSimilarity();}
	
	/**
	 * Metodo per il calcolo della similarita' tra due oggetti linguistici. 
	 * @param l1 il primo oggetto linguistico
	 * @param l2 il secondo oggetto linguistico
	 * @return un numero compreso tra 0 e 1 che indica quanto due oggetti linguisti siano simili
	 */
	public double computeSimilarity(LinguisticObject l1,LinguisticObject l2) {
		if (l1.getClass().equals(Word.class)) {setLexicalSimilarityStrategy();}
		else if(l1.getClass().equals(BabelSynset.class)){setSemanticSimilarityStrategy();}
		else if(l1.getClass().equals(Document.class)) {setDocumentSimilarityStrategy();}
		return strategy.computeSimilarity(l1, l2);
	}
	
	/**
	 * @return la mappa con le forme flesse associate al proprio lemma
	 */
	public HashMap<String, String> getLemmatizations() {return lemmatizations;}
	
	/**
	 * Metodo che rappresenta sotto forma di stringa le relazioni associate ad un synset.
	 * @param s BabelSynset di cui voglio le relazioni
	 * @return le relazioni del BabelSynset come stringa
	 */
	public String stringaTotale(BabelSynset s) {
		StringBuilder sb=new StringBuilder();
		
		for(BabelEdge b:semanticNetwork.get(s)) {sb.append(b.toString()+";");}
		
		if (sb.length()>0) {sb.deleteCharAt(sb.length()-1);}
		
		return sb.toString();
	}
	
	/**
	 * Metodo che ritorna la rete semantica.
	 * @return la rete semantica contenuta in MinibabelNet.
	 */
	public HashMap<Synset, Set<BabelEdge>> getSemanticNetwork() {return semanticNetwork;}
	
	/**
	 * Metodo che ritorna l'iterator sui synset.
	 * @param s il Synset di partenza per la visita del grafo
	 * @return l'iterator per la visita del grafo
	 */
	public SynsetIterator iterator(Synset s) {return new SynsetIterator(s);}
	
	/**
	 * Iterator per scorrere sui synset del grafo.
	 * @author Filippo Mazzara
	 */
	class SynsetIterator implements Iterator<Synset> {
	    private HashSet<Synset> visited = new HashSet<>(); //insieme dei nodi visitati
	    private Queue<Synset> queue = new LinkedList<>(); //coda dei nodi da visitare
	    private HashMap<Synset,Set<BabelEdge>>  graph; //rete semantica
	    private int distance; //profondita' del grafo
	    private int nFigli; //nodi rimanenti a una certa profondita' nel grafo
	    
	    /**
	     * Costruttore dell'iterator.
	     * @param startingVertex il synset iniziale
	     */
	    public SynsetIterator(Synset startingVertex) {
	        if(MiniBabelNet.getInstance().getSemanticNetwork().containsKey(startingVertex)) {
	        	this.graph=MiniBabelNet.getInstance().getSemanticNetwork();
	            this.queue.add(startingVertex);
	            this.visited.add(startingVertex);
	            this.distance=0;
	            this.nFigli=0;}
	        else{throw new IllegalArgumentException();} //deve contenere il sysnet iniziale
	    }
	    
	    /**
	     * @return la profondita' del grafo
	     */
	    public int getDinstance() {return this.distance;}

	    @Override
	    public boolean hasNext() {
	        return !this.queue.isEmpty();
	    }

	    @Override
	    public Synset next() {
	        if(!hasNext()) {throw new NoSuchElementException();} //si ferma se la coda e' vuota
	        
	        Synset next = queue.remove(); //prende il primo nodo della coda
	        HashSet<Synset> figli=new HashSet<>();
	        
	        for (BabelEdge be:graph.get(next)){figli.add(be.getSynsetDest());} //crea l'insieme dei figli del nodo
	        
	        for (Synset vicino : figli) { //aggiunge i figli non visitati alla coda
	            if (!this.visited.contains(vicino)) {
	                this.queue.add(vicino);
	                this.visited.add(vicino);
	            }
	        }
	        
	        //per il calcolo della profondita'
	        if (nFigli==0) {
	        	nFigli=queue.size();
	        	distance++;}
	        else {nFigli--;}
	        return next;
	    }
	}
}
