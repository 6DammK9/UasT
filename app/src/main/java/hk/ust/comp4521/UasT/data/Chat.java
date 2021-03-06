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

import hk.ust.comp4521.UasT.BaseFragment;
import hk.ust.comp4521.UasT.ChatFragment;
import hk.ust.comp4521.UasT.R;

@ThreadItemInfo(add = true, filter = true, sort = true, title = "Conversations", type = "chats", typeName = "Chat", layout = R.layout.view_chat_card)
public class Chat extends ThreadItem  {
	private long time;
	private boolean isGroup;
	
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
	public int compareTo(@NonNull ThreadItem another) {
		Chat chat = (Chat) another;
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
