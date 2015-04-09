package hk.ust.comp4521.exust.data;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public class Match extends ThreadPostItem {
	
	boolean[] avail;
	

	public boolean[] getAvail() {
		return avail;
	}

	@Override
	public void load(JSONObject obj) throws JSONException {
		JSONArray _avail = obj.getJSONArray("avail");
		avail = new boolean[_avail.length()];
		for(int i = 0; i < avail.length; i++)
			avail[i] = _avail.getBoolean(i);
		title = obj.getString("user");
		authorId = obj.getString("name");
		
	}
	
}
