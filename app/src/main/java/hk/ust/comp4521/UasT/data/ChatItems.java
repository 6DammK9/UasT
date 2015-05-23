/**
 * COMP4521 Project
 *      Cheung Wai Yip
 *      Lau Tsz Hei
 *      Ho Kam Ming
 */

package hk.ust.comp4521.UasT.data;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public class ChatItems extends DataType {

	@Override
	public void load(JSONObject obj) throws JSONException {
		JSONObject _chats = obj.getJSONObject("chats");
		JSONArray chatIds = _chats.names();
		JSONArray userIds = obj.getJSONObject("users").names();
		
		chats = new ChatItem[chatIds == null ? 0 : chatIds.length()];
		users = new String[userIds == null ? 0 : userIds.length()];
		
		for(int i = 0; i < chats.length; i++) {
			ChatItem chat = chats[i] = new ChatItem();
			if (chatIds != null) {
				chat.load(_chats.getJSONObject(chatIds.getString(i)));
			}
		}
		
		for(int i = 0; i < users.length; i++) {
			if (userIds != null) {
				users[i] = userIds.getString(i);
			}
		}
	}

	public ChatItem[] getChats() {
		return chats;
	}
	public String[] getUsers() {
		return users;
	}

	ChatItem[] chats;
	String[] users;
}
