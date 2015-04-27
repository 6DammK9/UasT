package hk.ust.comp4521.exust.data;

import org.json.JSONException;
import org.json.JSONObject;

import hk.ust.comp4521.exust.BaseFragment;
import hk.ust.comp4521.exust.EditGroupFragment;
import hk.ust.comp4521.exust.R;


// TODO #       Add Number of groupmates currently
// TODO #       Implement Group leader
@ThreadItemInfo(filter = true, sort = true, add = true, typeName = "Group", title = "Groups", type = "groups", layout = R.layout.view_group_card)
public class CourseGroup extends ThreadPostItem {
	long time;
	
	@Override
	public void load(JSONObject obj) throws JSONException {
		title = obj.getString("title");
		sub = obj.getString("author");
		authorId = obj.getString("authorId");
		key = obj.getString("post");
		time = obj.getLong("time");
	}
	
	@Override
	public int compareTo(ThreadItem another) {
		CourseGroup comment = (CourseGroup)another;
		long lhs = time;
		long rhs = comment.time;
		return -(lhs < rhs ? -1 : (lhs == rhs ? 0 : 1));
	}
	
	public BaseFragment[] getFragment() {
		BaseFragment[] fragments = new BaseFragment[1];
		EditGroupFragment post = new EditGroupFragment();
		fragments[0] = post;
		post.setParam(this);
		return fragments;
	}
}
