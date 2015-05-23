/**
 * COMP4521 Project
 *      Cheung Wai Yip
 *      Lau Tsz Hei
 *      Ho Kam Ming
 */

package hk.ust.comp4521.UasT.json;

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
