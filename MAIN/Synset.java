package it.uniroma1.lcl.babelarity;

import java.util.List;
import java.util.Set;

import it.uniroma1.lcl.babelarity.BabelSynset.POS;

/**
 * Interfaccia segnaposto che definisce i synsets.
 * @author Filippo Mazzara
 */
public interface Synset extends LinguisticObject {
	String getID();
	POS getPos();
	Set<String> getLemmas();
	List<String> getGlosses();
}
