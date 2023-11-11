package it.uniroma1.lcl.babelarity;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.nio.file.Paths;

import it.uniroma1.lcl.babelarity.MiniBabelNet.SynsetIterator;

public class Test {
	public static void main(String[] args) throws FileNotFoundException, IOException {
	MiniBabelNet m=MiniBabelNet.getInstance();
	CorpusManager cm=CorpusManager.getInstance();
	//System.out.println(m.getSynset("bn:00081546n").getGlosses().size());
	//System.out.println(m.computeSimilarity(cm.loadDocument(d.getId()), cm.loadDocument(d2.getId())));
	//System.out.println(d.getId());System.out.println(d.getTitle());System.out.println(d.getContent());
	//System.out.println(m.computeSimilarity(m.getSynset("bn:00085614v"), m.getSynset("bn:00085614v")));
	//System.out.println(m.computeSimilarity(m.getSynset("bn:00094686v"), m.getSynset("bn:00085614v")));
	/**Synset s1 = m.getSynset("bn:00034472n");
    Synset s2 = m.getSynset("bn:00015008n");
    Synset s3 = m.getSynset("bn:00081546n");
    Synset s4 = m.getSynset("bn:00070528n");**//**
    Synset s1 = m.getSynset("bn:00024712n");
    Synset s2 = m.getSynset("bn:00029345n");
    Synset s3 = m.getSynset("bn:00035023n");
    Synset s4 = m.getSynset("bn:00010605n");**/
	//SynsetIterator sr=m.iterator(m.getSynset("bn:00094686v"));
	//System.out.println(m.getRelations(m.getSynset("bn:00094686v")));
	//while(sr.hasNext()) {System.out.println(sr.getDinstance());System.out.println(sr.next().getID());}
	//System.out.println(m.computeSimilarity(Word.fromString("emotion"), Word.fromString("emotion")));
	Word w1=(Word.fromString("port"));
	Word w2=(Word.fromString("ship"));
	Word w3=(Word.fromString("fear"));
	Word w4=(Word.fromString("emotion"));/**
	Word w1=(Word.fromString("test"));
	Word w2=(Word.fromString("exam"));
	Word w3=(Word.fromString("pop"));
	Word w4=(Word.fromString("rock"));**/
	double sim1=m.computeSimilarity(w1, w2);
	double sim2=m.computeSimilarity(w3, w4);
	double sim3=m.computeSimilarity(w1, w3);
	double sim4=m.computeSimilarity(w2, w4);
	System.out.println(sim1);System.out.println(sim2);System.out.println(sim3);System.out.println(sim4);
	System.out.println(sim1>sim3 && sim2>sim4);
	
	}

}
