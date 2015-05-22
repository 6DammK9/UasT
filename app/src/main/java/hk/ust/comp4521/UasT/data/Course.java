package hk.ust.comp4521.UasT.data;

import org.json.JSONException;
import org.json.JSONObject;

import hk.ust.comp4521.UasT.BaseFragment;
import hk.ust.comp4521.UasT.R;
import hk.ust.comp4521.UasT.ThreadListFragment;

@ThreadItemInfo(favorite = true, filter = true, sort = true, typeName = "Chat", title = "Courses", type = "courses", hint = "Code or Name?", layout = R.layout.view_course_card)
public class Course extends ThreadItem {
	String desc;
	double credit;
	int rating, ratingTotal;

	@Override
	public void load(JSONObject obj) throws JSONException {
		sub = obj.getString("name");
		key = title = obj.getString("code");
		desc = obj.getString("desc");
		credit = obj.getDouble("credit");
		rating = obj.getInt("rating");
		ratingTotal = obj.getInt("ratingTotal");
	}
	
	public String getDesc() {
		return desc;
	}
	
	public String getFullTitle() {
		return title + " - " + sub + " (" + credit + (credit == 1 ? " credit)" : " credits)");
	}
	
	public boolean getIsFavorite() {
		return Database.getUser().getFavorite().containsKey(getKey());
	}
	
	public void setIsFavorite(boolean isFavo) {
		if(isFavo)
			Database.getUser().getFavorite().put(getKey(), null);
		else
			Database.getUser().getFavorite().remove(getKey());
		Database.commitUser();
	}

	@Override
	public int compareTo(ThreadItem another) {
		Course course = (Course) another;
		
		boolean isFavo = getIsFavorite();
		boolean isFavo2 = course.getIsFavorite();
		
		if(isFavo != isFavo2)
			return isFavo ? -1 : 1;
		return key.compareTo(course.key);
	}

	@Override
	public BaseFragment[] getFragment() {
		ThreadListFragment[] fragments = new ThreadListFragment[4];
		
		fragments[0] = new ThreadListFragment();
		fragments[0].setParams(this, CourseComment.class);
		
		fragments[1] = new ThreadListFragment();
		fragments[1].setParams(this, CourseGroup.class);
		
		fragments[2] = new ThreadListFragment();
		fragments[2].setParams(this, CourseSharing.class);
		
		fragments[3] = new ThreadListFragment();
		fragments[3].setParams(this, CourseTrading.class);
		
		return fragments;
	}

	public int getRating() {
		return (int)((double)rating / ratingTotal);
	}
}
