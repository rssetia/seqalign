package seqalign;

public class Sequence {
	char[] aminoChain ; 
	String name; 
	
	public Sequence(char[] aminoChain) { 
		this.aminoChain = aminoChain ; 
	}
	
	public Sequence(char[] aminoChain, String name) { 
		this.aminoChain = aminoChain ; 
		this.name = name;  
	}
	
	
	public char getResidue(int index) { 
		return aminoChain[index] ; 
	}
	
	public int getLength() { 
		return aminoChain.length ; 
	}
}
