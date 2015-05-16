package hk.ust.comp4521.exust.data;

import org.json.JSONException;
import org.json.JSONObject;

import hk.ust.comp4521.exust.BaseFragment;
import hk.ust.comp4521.exust.R;

@ThreadItemInfo(sort = true, title = "Conversations", type = "chatDatas", typeName = "Chat", layout = R.layout.view_chat_item_card)
public class ChatItem extends ThreadPostItem {
	long time;
	String matchId;
	String imgId;
	String imgName;

	public String getMatchId() {
		return matchId;
	}

	public String getIMGId() {
		return imgId;
	}

	public String getIMGName() {
		return imgName;
	}

	public long getTime() {
		return time;
	}

	@Override
	public void load(JSONObject obj) throws JSONException {
		title = obj.getString("content");
		sub = obj.getString("userName");
		authorId = obj.getString("user");
		//if (obj.has("match"))
			//matchId = obj.getString("match");
        if (obj.has("match2"))
            matchId = obj.getString("match2");
		if (obj.has("matchIMG"))
			imgId = obj.getString("matchIMG");
		if (obj.has("img"))
			imgName =  obj.getString("img");
		key = obj.getString("id");
		time = obj.getLong("time");
	}

	@Override
	public int compareTo(ThreadItem another) {
		ChatItem chat = (ChatItem) another;
		long lhs = time;
		long rhs = chat.time;
		return (lhs < rhs ? -1 : (lhs == rhs ? 0 : 1));
	}

	public BaseFragment[] getFragment() {
		return null;
	}
}
