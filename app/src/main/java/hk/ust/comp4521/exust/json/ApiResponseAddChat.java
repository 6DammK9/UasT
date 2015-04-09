package hk.ust.comp4521.exust.json;

import hk.ust.comp4521.exust.data.Chat;

import org.json.JSONException;
import org.json.JSONObject;

public class ApiResponseAddChat extends ApiResponseBase {

	Chat chat;
	
	public void load(JSONObject obj) throws JSONException {
		super.load(obj);
		if(obj.has("chat")) {
			chat = new Chat();
			chat.load(obj.getJSONObject("chat"));
		}
	}
	
	public Chat getChat() {
		return chat;
	}
}
