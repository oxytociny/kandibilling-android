/*package tools.mikandi.dev.highscore;

import java.util.Collections;
import java.util.Comparator;
import java.util.LinkedList;
import java.util.List;

import com.saguarodigital.returnable.IReturnable;
import com.saguarodigital.returnable.defaultimpl.JSONAsyncTask;
import com.saguarodigital.returnable.defaultimpl.JSONResponse;

import android.content.Context;
import tools.mikandi.dev.highscore.HighScoreUtils.SortOrder;
import tools.mikandi.dev.inapp.AccountBalancePoint;
import tools.mikandi.dev.passreset.OnJSONResponseLoadedListener;
import tools.mikandi.dev.utils.UserInfoObject;

public class HighScoreTable implements OnJSONResponseLoadedListener<HighScoreObject> {

	List<HighScoreObject> hso;
	
	public SortOrder mSort = null;
	private String mPublicId = null; 
	private String mAppId = null;
	
	public HighScoreTable(SortOrder s, Context c) { 
		this.mSort = s;
		
		mAppId = UserInfoObject.getInstance(c).getAppId();
		mPublicId = UserInfoObject.getInstance(c).getPublisherId();
		
		
		
	}

	public void addScore(HighScoreObject h) { 
		if (hso == null) { 
			hso = new LinkedList<HighScoreObject>();
		}
	
		if (!hso.contains(h)) {
			h.setSortType(mSort);
			hso.add(h);
		}
	}

	@SuppressWarnings("unchecked")
	public void sort() { 
		Collections.sort(hso);
	}
	
	public void setSort(SortOrder s) { 
		for (HighScoreObject h : hso) { 
			h.setSortType(s);
		}
		sort();
	}
	
	public List<HighScoreObject> getTableList() { 
		return this.hso;
	}
	
	public void removeHighScoreObject(HighScoreObject h) { 
		if (hso.contains(h)) { 
			hso.remove(h);
		}
	}
	
	public HighScoreTable retreiveTable(int appid, int publisherid, int tableid) { 
		
		String url = new StringBuilder(HighScoreUtils.URL_GET_TABLE).append('?')
				.append("appid").append("=").append(appid)
				.append("publisherid").append("=").append(publisherid)
				.append("tableid").append("=").append(tableid).toString();
		
		return this; 	
	}

	@Override
	public void onJSONLoaded(JSONResponse<HighScoreObject> jsonResponse) {
		
		
	}

	
}

*/