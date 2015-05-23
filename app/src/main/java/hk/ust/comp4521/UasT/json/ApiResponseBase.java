/**
 * COMP4521 Project
 *      Cheung Wai Yip
 *      Lau Tsz Hei
 *      Ho Kam Ming
 */

package hk.ust.comp4521.UasT.json;

import org.json.JSONException;
import org.json.JSONObject;

public class ApiResponseBase {
	protected JSONObject obj;

	public void load(JSONObject obj) throws JSONException {
		this.obj = obj;
		code = obj.getInt("code");
		if (obj.has("msg"))
			msg = obj.getString("msg");
	}

	int code;
	String msg;

	public int getCode() {
		return code;
	}

	public String getMessage() {
		return msg;
	}
}
