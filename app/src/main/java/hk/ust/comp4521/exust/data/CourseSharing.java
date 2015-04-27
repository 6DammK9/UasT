package hk.ust.comp4521.exust.data;

import org.json.JSONException;
import org.json.JSONObject;

import hk.ust.comp4521.exust.R;

//TODO  #   Add Like/Dislike Button
@ThreadItemInfo(filter = true, sort = true, add = true, typeName = "Sharing", title = "Sharing", type = "sharings", layout = R.layout.view_thread_card)
public class CourseSharing extends ThreadPostItem {
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
}
