package tools.mikandi.dev.highscore;

import java.util.Comparator;
import java.util.LinkedList;
import java.util.List;

public class HighScoreTable implements Comparator<HighScoreObject> {

	List<HighScoreObject> hso;

	@Override
	public int compare(HighScoreObject lhs, HighScoreObject rhs) {
		
		return 0;
	// return negative to 
	} 
	
	public void addScore(HighScoreObject h) { 
		if (hso == null) { 
			hso = new LinkedList<HighScoreObject>();
		}
	
		if (!hso.contains(h)) {
			hso.add(h);
		}		
	}

	public void removeHighScoreObject(HighScoreObject h) { 
		if (hso.contains(h)) { 
			hso.remove(h);
		}
	}
	
	
	public class AscendingScore implements Comparator<HighScoreObject> {

		@Override
		public int compare(HighScoreObject lhs, HighScoreObject rhs) {
			
			
			
			
			return 0;
		} 
	
	}
		
	public class DescendingScore implements Comparator<HighScoreObject> {

		@Override
		public int compare(HighScoreObject lhs, HighScoreObject rhs) {
			int l = lhs.getScore();
			int r = rhs.getScore(); 
			return 0;
		} 
		
	}
	
}
