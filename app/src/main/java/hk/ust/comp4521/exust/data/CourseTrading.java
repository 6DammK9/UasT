package hk.ust.comp4521.exust.data;

import org.json.JSONException;
import org.json.JSONObject;

import hk.ust.comp4521.exust.R;

// TODO photo
@ThreadItemInfo(filter = true, sort = true, add = true, typeName = "Trading", title = "Trading", type = "tradings", layout = R.layout.view_course_trading_card)
public class CourseTrading extends ThreadPostItem {
	long time;
    int rating;
    String courseId;
	
	@Override
	public void load(JSONObject obj) throws JSONException {
		title = obj.getString("title");
		sub = obj.getString("author");
		authorId = obj.getString("authorId");
		key = obj.getString("post");
		time = obj.getLong("time");
        rating = obj.getInt("rating");
        courseId = obj.getString("course");
	}
	
	@Override
	public int compareTo(ThreadItem another) {
        CourseTrading comment = (CourseTrading)another;
		long lhs = time;
		long rhs = comment.time;
		return -(lhs < rhs ? -1 : (lhs == rhs ? 0 : 1));
	}

    public int getRating() {
        return rating;
    }

    public String getCourseId() {
        return this.courseId;
    }
}
