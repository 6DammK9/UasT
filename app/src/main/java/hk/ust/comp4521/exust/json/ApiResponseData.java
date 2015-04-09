package hk.ust.comp4521.exust.json;

import org.json.JSONException;
import org.json.JSONObject;

public class ApiResponseData extends ApiResponseBase {
	
	JSONObject data;
	String hash;
	
	public void load(JSONObject obj) throws JSONException {
		super.load(obj);
		if(obj.has("data"))
			data = obj.getJSONObject("data");
		if(obj.has("hash"))
			hash = obj.getString("hash");
	}
	
	public JSONObject getData() {
		return data;
	}
	
	public String getHash() {
		return hash;
	}
}
