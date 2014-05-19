package seqalign;

public class ScoreMatrix {
	int[][] score ; 
	
	public ScoreMatrix(int n, int m) { 
		score = new int[n + 1][m + 1] ; 
	}

	public void setVal(int i, int j, int value) { 
		this.score[i][j] = value; 
	}
	
	public int getVal(int i, int j) { 
		return this.score[i][j]  ;
	}
}
