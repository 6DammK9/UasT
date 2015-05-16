package hk.ust.comp4521.exust.data;

import org.json.JSONException;
import org.json.JSONObject;

public class MatchIMG extends ThreadPostItem {
	
    String img;

    public String getIMG() {return img;}

	@Override
	public void load(JSONObject obj) throws JSONException {
		title = obj.getString("user");
		authorId = obj.getString("name");
        img = obj.getString("img");
	}

}
