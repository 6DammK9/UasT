package hk.ust.comp4521.UasT.data;

import org.json.JSONException;
import org.json.JSONObject;

import hk.ust.comp4521.UasT.R;

@ThreadItemInfo(filter = true, sort = true, add = true, typeName = "Comment", title = "Comments", type = "comments", layout = R.layout.view_comment_card, header = R.layout.view_course_header_card)
public class CourseComment extends ThreadPostItem {
	long time;
	int rating;

	@Override
	public void load(JSONObject obj) throws JSONException {
		title = obj.getString("title");
		sub = obj.getString("author");
		authorId = obj.getString("authorId");
		key = obj.getString("post");
		time = obj.getLong("time");
		if (obj.has("rating"))
			rating = obj.getInt("rating");
	}

	@Override
	public int compareTo(ThreadItem another) {
		CourseComment comment = (CourseComment) another;
		long lhs = time;
		long rhs = comment.time;
		return -(lhs < rhs ? -1 : (lhs == rhs ? 0 : 1));
	}

	public int getRating() {
		return rating;
	}
}
