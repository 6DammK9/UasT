package hk.ust.comp4521.exust.json;

import org.json.JSONException;
import org.json.JSONObject;


public class ApiResponseValidate extends ApiResponseBase {
	String auth;
	
	public void load(JSONObject obj) throws JSONException {
		super.load(obj);
		if(obj.has("auth"))
			auth = obj.getString("auth");
	}
	
	public String getAuth() {
		return auth;
	}
}
