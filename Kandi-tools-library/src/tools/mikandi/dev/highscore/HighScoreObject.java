package tools.mikandi.dev.highscore;

public class HighScoreObject {
	// grouping identifiers 
	int mAppId; // each app id can have multiple tables 
	int mPublisherId;  // publisher id allows all the dev's tables to be viewed at once
	int mTableId; 	  // Different table
	
	String mScoreType;  // this will be the colum header 
	int mScore;         // numerical score value
	
	/* 
	 *	 			|----Goals----|  (scoretype - column header)
     * 				 1    score
	 *  			 2    score
	 *  			 3    score
	 *  			 4    score
	 *   		     5    score
	 *   			 6    score
	 *   
	 */
	
	HighScoreObject(int appid, int publisherid, int tableid, String scoretype , int score)  { 
		this.mAppId = appid;
		this.mPublisherId = publisherid;
		this.mTableId = tableid;
		
		this.mScoreType = scoretype;
		this.mScore = score;
	}
	
	public int getAppid() {
		return mAppId;
	}
	public void setAppid(int appid) {
		this.mAppId = appid;
	}
	public int getPublisherid() {
		return mPublisherId;
	}
	public void setPublisherid(int publisherid) {
		this.mPublisherId = publisherid;
	}
	public int getTableid() {
		return mTableId;
	}
	public void setTableid(int tableid) {
		this.mTableId = tableid;
	}
	public String getScoretype() {
		return mScoreType;
	}
	public void setScoretype(String scoretype) {
		this.mScoreType = scoretype;
	}
	public int getScore() {
		return mScore;
	}
	public void setScore(int score) {
		this.mScore = score;
	}
	

	
	
	
}
