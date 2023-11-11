package it.uniroma1.lcl.babelarity;

/**
 * Interfaccia segnaposto per i vari tipi di similarita'.
 * @author Filippo Mazzara
 *
 */
public interface BabelSimilarity {
	
	/**
	 * Metodo per il calcolo della similarita' tra oggetti linguistici.
	 * @param l1 il primo oggetto linguistico
	 * @param l2 il secondo oggetto linguistico
	 * @return il valore di similarita' compreso tra 0 e 1
	 */
	public double computeSimilarity(LinguisticObject l1,LinguisticObject l2);

}
