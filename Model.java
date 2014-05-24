package seqalign;

import java.util.Stack;

public class Model {
	int o ; 
	int e ; 
	ScoreMatrix M ; 
	ScoreMatrix S ; 
	ScoreMatrix T ; 
	Sequence seqS ; 
	Sequence seqT; 
	Blosum bl; 
	
	public Model(int o, int e, Sequence s, Sequence t) { 
		bl = new Blosum() ; 
		this.o = o; 
		this.e = e; 
		this.seqS = s; 
		this.seqT = t; 
		int lengthS = s.getLength() ;
		int lengthT = t.getLength() ; 
		// [S][T] 
		M = new ScoreMatrix(lengthS, lengthT) ; 
		S = new ScoreMatrix(lengthS, lengthT) ; 
		T = new ScoreMatrix(lengthS, lengthT) ;
		initM() ; 
		initS() ; 
		initT() ; 
		fill() ; 
		backtrack() ;  
		System.out.println("DONE") ; 
	}

	public int getHighestScore() { 
		int i = seqS.getLength(); 
		int j = seqT.getLength() ; 
		int Mscore = M.getVal(i, j); 
		int Sscore = S.getVal(i, j);
		int Tscore = T.getVal(i, j);
		
		return Math.max( Math.max(Mscore, Sscore), Tscore) ; 
	}
	
	
	private void initM() { 
		M.setVal(0, 0, 0); 
		for(int i = 1 ; i <= seqS.getLength(); i++) { 
			M.setVal(i, 0, -10000) ; 
		}
		for(int j = 1 ; j <= seqT.getLength(); j++) { 
			M.setVal(0, j, -10000) ; 
		}
	}
	
	private void initS() { 
		S.setVal(0, 0, o); 
		for(int i = 1; i <= seqS.getLength(); i ++) { 
			S.setVal(i, 0, o + e * i) ; 
		}
		for(int j = 1; j <= seqT.getLength(); j ++) { 
			S.setVal(0, j, -10000) ; 
		}
	}
	
	private void initT() { 
		T.setVal(0, 0, o); 
		for(int j = 1; j <= seqT.getLength(); j ++) { 
			T.setVal(0, j, o + e * j) ; 
		}
		for(int i = 1; i <= seqS.getLength(); i ++) { 
			T.setVal(i, 0, -10000) ; 
		}
	}
	
	// i and j are indices for the matrices, NOT the sequences itself. 
	// the sequence index to check are i-1 and j-1, respectively 
	// we will start at i = 1 and j = 1 since we already filled out the zeroth row 
	private void calcM(int i, int j) {
		char sChar = this.seqS.getResidue(i - 1) ; 
		char tChar = this.seqT.getResidue(j - 1) ;
		
		int matchScore = bl.getScore(sChar, tChar) ; 
		
		int Mval = this.M.getVal(i - 1, j - 1) ; 
		int Sval = this.S.getVal(i - 1, j - 1) ; 
		int Tval = this.T.getVal(i - 1, j - 1) ;
		
		this.M.setVal(i, j, maxThree(Mval, Sval, Tval) + matchScore ) ; 
	}
	
	private void calcS(int i, int j) { 
		char sChar = this.seqS.getResidue(i - 1) ; 
		char tChar = this.seqT.getResidue(j - 1) ;
		
		int matchScore = bl.getScore(sChar, tChar) ; 
		
		int Mval = this.M.getVal(i - 1, j) + o ; 
		int Sval = this.S.getVal(i - 1, j) + e; 
		int Tval = this.T.getVal(i - 1, j) + o ;

		//this.S.setVal(i, j, maxThree(Mval, Sval, Tval) + matchScore ) ; 
		this.S.setVal(i, j, maxThree(Mval, Sval, Tval)) ; 
	}
	
	private void calcT(int i, int j) { 
		char sChar = this.seqS.getResidue(i - 1) ; 
		char tChar = this.seqT.getResidue(j - 1) ;
		
		int matchScore = bl.getScore(sChar, tChar) ; 
		
		int Mval = this.M.getVal(i, j - 1) + o ; 
		int Sval = this.S.getVal(i, j - 1) + o; 
		int Tval = this.T.getVal(i, j - 1) + e;
		
		//this.T.setVal(i, j, maxThree(Mval, Sval, Tval) + matchScore ) ;
		this.T.setVal(i, j, maxThree(Mval, Sval, Tval)) ;
	}
	
	private int maxThree(int a, int b, int c) { 
		return Math.max( Math.max(a,b), c) ;  
	}
	
	public void fill() { 
		for (int i = 1; i <= seqS.getLength() ; i++) { 
			for (int j = 1; j <= seqT.getLength() ; j++) {
				calcM(i,j); 
				calcS(i,j); 
				calcT(i,j); 
			}
		} 
	}
	
	public void backtrack() { 
		int i = seqS.getLength() ; 
		int j = seqT.getLength() ; 
		Stack<Alignment> st = new Stack<Alignment>(); 
			
		while (i != 0 || j != 0) { 
			int Mscore = M.getVal(i, j); 
			int Sscore = S.getVal(i, j);
			int Tscore = T.getVal(i, j); 
		//	System.out.println("lel:" + i + " " + j) ; 
			if (Mscore >= Sscore && Mscore >= Tscore) { 
			//	System.out.println("Using M") ; 
				Alignment a = new Alignment(seqS.getResidue(i - 1), seqT.getResidue(j - 1)); 
				st.push(a) ;
				i -= 1; 
				j -= 1; 
			// S[i] is aligned to a gap, so we decrement i only 
			} else if (Sscore > Mscore && Sscore >= Tscore) { 
			//	System.out.println("Using S") ; 
				Alignment a = new Alignment(seqS.getResidue(i - 1), '-'); 
				st.push(a) ;
				i -= 1 ;
			} else if (Tscore > Mscore && Tscore > Sscore) { 
			//	System.out.println("Using T") ; 
				Alignment a = new Alignment('-', seqT.getResidue(j - 1)); 
				st.push(a) ;
				j -= 1 ;
			} else { 
				System.out.println("oh shit") ; 
			}
		} 
		
		printAlignment(st) ; 
	}
	
	public void printAlignment(Stack<Alignment> st) { 
		int total = 0 ; 
		while (!st.isEmpty()) { 
			Alignment a = st.pop() ; 
			char s = a.s ; 
			char t = a.t ; 
			int score = 0; 
			if (s == '-' || t == '-') { 
				score = 0; 
			} else { 
				score = bl.getScore(s, t) ; 
				total += score; 
			} 
			System.out.println(s + " " + t  + " " + score) ;

		}
		System.out.println("SCORE: " + total) ; 
	}
	
}
