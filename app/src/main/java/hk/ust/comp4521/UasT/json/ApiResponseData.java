/**
 * 	#COMP 4521
 *      #Cheung Wai Yip	20126604	wycheungae@connect.ust.hk
 *      #Lau Tsz Hei		20113451	thlauac@connect.ust.hk
 *      #Ho Kam Ming	20112316	kmhoab@connect.ust.hk
 */

package hk.ust.comp4521.UasT.json;

import org.json.JSONException;
import org.json.JSONObject;

public class ApiResponseData extends ApiResponseBase {
	
	private JSONObject data;
	private String hash;
	
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
