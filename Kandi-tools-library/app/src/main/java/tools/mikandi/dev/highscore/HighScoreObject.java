/*package tools.mikandi.dev.highscore;

import java.util.Map;

import android.content.Context;

import com.saguarodigital.returnable.IParser;
import com.saguarodigital.returnable.IReturnableCache;
import com.saguarodigital.returnable.annotation.Field;
import com.saguarodigital.returnable.annotation.Type;
import com.saguarodigital.returnable.annotation.Field.Constraint;
import com.saguarodigital.returnable.defaultimpl.AutoParser;

import tools.mikandi.dev.utils.IReturnable;


@Type(version = 1, type = Type.JSONDataType.OBJECT)
public class HighScoreObject implements IReturnable, Comparable {
	// grouping identifiers 
	
	@Field(json_name = "appid" , type = Field.Type.NUMBER, constraint = Field.Constraint.NOT_NULL )
	int mAppId; // each app id can have multiple tables 
	@Field(json_name = "publisherid" , type = Field.Type.TEXT, constraint = Field.Constraint.NOT_NULL )
	int mPublisherId;  // publisher id allows all the dev's tables to be viewed at once
	@Field(json_name = "tableid" , type = Field.Type.NUMBER , constraint = Field.Constraint.NONE)
	int mTableId; 	  // Different table	
	@Field(json_name = "score_title" , type = Field.Type.TEXT)
	String mScoreType;  // this will be the colum header 
	@Field(json_name = "score_value" , type = Field.Type.NUMBER)
	int mScore;         // numerical score value
	
	 
	 *	 			|----Goals----|  (scoretype - column header)
     * 				 1    score
	 *  			 2    score
	 *  			 3    score
	 *  			 4    score
	 *   		     5    score
	 *   			 6    score
	 *   
	 
	
	SortOrder mSort = null;
		
	HighScoreObject(int appid, int publisherid, int tableid, String scoretype , int score)  { 
		this.mAppId = appid;
		this.mPublisherId = publisherid;
		this.mTableId = tableid;
		
		this.mScoreType = scoretype;
		this.mScore = score;
	}
	
	public void setSortType(SortOrder s) { 
		this.mSort = s; 
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
		return this.mTableId;
	}
	
	public void setTableid(int tableid) {
		this.mTableId = tableid;
	}
	
	public String getScoretype() {
		return this.mScoreType;
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

	@Override
	public int compareTo(Object arg0) {
		HighScoreObject h = (HighScoreObject) arg0;
		
		if (h.getScore() == this.mScore) { 
			return 0;
		}
		// TODO: 
		// check the sort order to determine the sorting fo objects
		
		if (this.mScore > h.getScore()) { 
			return 1; 
		
		
		
		} else { 
			return -1;
		}
	}

	@Override
	public IParser<HighScoreObject> getParser() {
		return new AutoParser<HighScoreObject>();
	}

	@Override
	public String getUri(Map<String, String> args) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public IReturnableCache<? extends IReturnable> getCache(Context context) {
		// TODO Auto-generated method stub
		return null;
	}
		
}
*/