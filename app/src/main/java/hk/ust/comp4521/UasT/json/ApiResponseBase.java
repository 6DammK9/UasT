/**
 * 	#COMP 4521
 *      #Cheung Wai Yip	20126604	wycheungae@connect.ust.hk
 *      #Lau Tsz Hei		20113451	thlauac@connect.ust.hk
 *      #Ho Kam Ming	20112316	kmhoab@connect.ust.hk
 */

package hk.ust.comp4521.UasT.json;

import org.json.JSONException;
import org.json.JSONObject;

public class ApiResponseBase {
	private JSONObject obj;

	public void load(JSONObject obj) throws JSONException {
		this.obj = obj;
		code = obj.getInt("code");
		if (obj.has("msg"))
			msg = obj.getString("msg");
	}

	private int code;
	private String msg;

	public int getCode() {
		return code;
	}

	public String getMessage() {
		return msg;
	}
}
