package it.uniroma1.lcl.babelarity;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

/**
 * Classe per la gestione di documenti del corpus.
 * @author Filippo Mazzara
 *
 */
public class CorpusManager implements Iterable<Document>{
	
	private static CorpusManager instance; //istanza di CorpusManager
	private List<Document> corpus=new ArrayList<>(); //lista di documenti salvati nel corpus
	private List<String> corpusLexical=new ArrayList<>();
	 /**
	  * Costruttore privato di CorpusManager.
	  */
	private CorpusManager() {
		File f=new File("resources/corpus");
		System.out.println("ok");
		int count=0;
		for (String doc:f.list()) {
				try {
					count++;
					System.out.println(count);
					String document= new String ( Files.readAllBytes(Paths.get("resources/corpus/"+doc)));
					corpusLexical.add(pulisciTesto(document));}
				catch (IOException e) {e.printStackTrace();}}
		System.out.println(corpusLexical.size());
	}
	
	public List<String> getCorpusLexical(){return corpusLexical;}
	
	/**
	 * Metodo per la creazione/accesso al CorpusManager.
	 * @return l'istanza di CorpusManager
	 */
    static CorpusManager getInstance() {
    	if(instance==null) {instance=new CorpusManager();}
    	return instance;
    }
    
    /**
     * Metodo per la creazione di documenti.
     * @param path percorso del file da cui otterremo il documento
     * @return il nuovo documento
     */
    public Document parseDocument(Path path) {
    	String line1="";
    	String id = "";
    	String t="";
    	String contenuto="";
    	try (BufferedReader bfr=new BufferedReader(new FileReader(path.toString()))){
    		line1=bfr.readLine(); //legge la prima riga
			String[] l=line1.split("\t");
			t=l[0];
			id=l[1];}
		catch (IOException e) {e.printStackTrace();}
    	try {
    		contenuto= new String ( Files.readAllBytes( path ) ); //legge il resto del file
    		}
	    catch (IOException e) {e.printStackTrace();}
    	
    	return new Document(id,t,contenuto.substring(line1.length()+1));
 
	}

	/**
	 * Metodo per pulire il testo dei documenti.
	 * @param s la stringa con il testo da pulire
	 * @return la stringa pulita
	 */
	public static String pulisciTesto(String s) {
		List<String> punt=List.of(".",",",";",":","?","!","/");
		String finale=s;
		for(String c:punt) {
			finale=finale.replace(c, "");
		}
		return finale;
	}
	
    /**
     * Metodo che ritorna un documento salvato nel CorpusManager.
     * @param id id del documento da cercare nel corpus 
     * @return il documento
     */
    public Document loadDocument(String id) {return corpus.stream().filter(d->d.getId().equals(id)).findFirst().get();}
    
    /**
     * Metodo che salva un documento nel CorpusManager.
     * @param document il documento da salvare
     */
    public void saveDocument(Document document) {
    	if (!corpus.contains(document)) {corpus.add(document);}
	}

	@Override
	public Iterator<Document> iterator() {
		return corpus.iterator();
	}
    
	

}
