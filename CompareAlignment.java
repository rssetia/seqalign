package seqalign;

import java.io.File;
import java.util.ArrayList;

public class CompareAlignment {
	String filePath = "/home/cs349/cs482/globins.fa" ;
	File fa ; 
	CompareAlignment() { 
		fa = new File(filePath) ; 
	}
	
	public void compare() { 
		ArrayList<Sequence> al = null; 
		try { 
			ParseFasta pf = new ParseFasta(fa) ;
			al = pf.getSeqs() ; 
		} catch (Exception e) { }
		
		for(int i = 0; i < al.size() - 1; i++) { 
			for (int j = i + 1; j < al.size(); j++) { 
				Model model = new Model(-11, -1, al.get(i), al.get(j)) ; 
				int highScore = model.getHighestScore() ;
				System.out.println(highScore) ;
			}
		}
		 
	}
}
