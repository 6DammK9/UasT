package hk.ust.comp4521.exust.data;

import org.json.JSONException;
import org.json.JSONObject;

import hk.ust.comp4521.exust.BaseFragment;
import hk.ust.comp4521.exust.ChatFragment;
import hk.ust.comp4521.exust.R;

@ThreadItemInfo(add = true, filter = true, sort = true, title = "Conversations", type = "chats", typeName = "Chat", layout = R.layout.view_chat_card)
public class Chat extends ThreadItem  {
	long time;
	boolean isGroup;
	
	public long getTime() {
		return time;
	}

	public boolean getIsGroup() {
		return isGroup;
	}

	@Override
	public void load(JSONObject obj) throws JSONException {
		title = obj.getString("title");
		if(obj.has("last"))
			sub = obj.getString("last");
		key = obj.getString("chat");
		time = obj.getLong("time");
		isGroup = obj.getBoolean("isGroup");
	}

	@Override
	public int compareTo(ThreadItem another) {
		Chat chat = (Chat)another;
		long lhs = time;
		long rhs = chat.time;
		return -(lhs < rhs ? -1 : (lhs == rhs ? 0 : 1));
	}
	
	public BaseFragment[] getFragment() {
		BaseFragment[] fragments = new BaseFragment[1];
		ChatFragment post = new ChatFragment();
		fragments[0] = post;
		post.setParam(this);
		return fragments;
	}
}
