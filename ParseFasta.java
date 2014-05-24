package seqalign;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;

import net.sf.jfasta.* ; 
import net.sf.jfasta.impl.* ; 



public class ParseFasta {

	File fastaFile; 
	FASTAFileReader fr; 
	
	ParseFasta(File fasta) throws IOException {
		this.fastaFile = fasta; 
		fr = new FASTAFileReaderImpl(this.fastaFile) ; 
	}
	
	ArrayList<Sequence> getSeqs() throws IOException { 
		ArrayList<Sequence> al = new ArrayList<Sequence>() ; 
		FASTAElementIterator it = this.fr.getIterator() ; 
		
		while(it.hasNext()) { 
			FASTAElement el = it.next() ; 
			char[] charSeq = el.getSequence().toCharArray() ;
			Sequence seq = new Sequence(charSeq, el.getSequence()) ;
			seq.setName(el.getHeader()) ; 
			al.add(seq); 
		}
		
		return al; 
	}
	
	
	
	
	
}
