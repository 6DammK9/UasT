package hk.ust.comp4521.exust.data;

import hk.ust.comp4521.exust.BaseFragment;
import hk.ust.comp4521.exust.R;

import org.json.JSONException;
import org.json.JSONObject;

@ThreadItemInfo(sort = true, title = "Group Members", type = "groupinfos", typeName = "Group", layout = R.layout.view_thread_card, header = R.layout.view_group_header_card)
public class GroupMember extends ThreadItem {

	long time;

	@Override
	public void load(JSONObject obj) throws JSONException {
		if (obj.has("title"))
			title = obj.getString("title");
		sub = obj.getString("id");
		time = obj.getLong("time");
	}

	@Override
	public int compareTo(ThreadItem another) {
		GroupMember chat = (GroupMember) another;
		long lhs = time;
		long rhs = chat.time;
		return -(lhs < rhs ? -1 : (lhs == rhs ? 0 : 1));
	}

	public BaseFragment[] getFragment() {
		return null;
	}
}
