package hk.ust.comp4521.exust.data;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public class Match2 extends ThreadPostItem {
	
    String[] CalStart;
    String[] CalEnd;

    public String[] getCalStart() {return CalStart;}
    public String[] getCalEnd() {return CalEnd;}

	@Override
	public void load(JSONObject obj) throws JSONException {
		JSONArray _CalStart = obj.getJSONArray("calStart");
        JSONArray _CalEnd = obj.getJSONArray("calEnd");
		CalStart = new String[_CalStart.length()];
        CalEnd = new String[_CalEnd.length()];
		for(int i = 0; i < CalStart.length; i++) {
            CalStart[i] = _CalStart.getString(i);
            CalEnd[i] = _CalEnd.getString(i);
        }
		title = obj.getString("user");
		authorId = obj.getString("name");
	}

}
