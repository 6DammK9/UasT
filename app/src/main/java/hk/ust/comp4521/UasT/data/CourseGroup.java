/**
 * 	#COMP 4521
 *      #Cheung Wai Yip	20126604	wycheungae@connect.ust.hk
 *      #Lau Tsz Hei		20113451	thlauac@connect.ust.hk
 *      #Ho Kam Ming	20112316	kmhoab@connect.ust.hk
 */

package hk.ust.comp4521.UasT.data;

import android.support.annotation.NonNull;

import org.json.JSONException;
import org.json.JSONObject;

import java.sql.Timestamp;

import hk.ust.comp4521.UasT.BaseFragment;
import hk.ust.comp4521.UasT.EditGroupFragment;
import hk.ust.comp4521.UasT.R;

@ThreadItemInfo(filter = true, sort = true, add = true, typeName = "Group", title = "Groups", type = "groups", layout = R.layout.view_group_card)
public class CourseGroup extends ThreadPostItem {
	private long time;
	
	@Override
	public void load(JSONObject obj) throws JSONException {
		title = obj.getString("title");
		sub = obj.getString("author");
		authorId = obj.getString("authorId");
		key = obj.getString("post");
		time = obj.getLong("time");
	}
	
	@Override
	public int compareTo(@NonNull ThreadItem another) {
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

    public String getStartTime() {
        Timestamp stamp = new Timestamp(time);
        stamp.setTime(time);
        return stamp.toString();
    }
}
