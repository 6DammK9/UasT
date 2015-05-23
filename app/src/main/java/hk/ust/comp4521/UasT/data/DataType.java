/**
 * COMP4521 Project
 *      Cheung Wai Yip
 *      Lau Tsz Hei
 *      Ho Kam Ming
 */

package hk.ust.comp4521.UasT.data;

import org.json.JSONException;
import org.json.JSONObject;

public abstract class DataType {
	public abstract void load(JSONObject obj) throws JSONException;
}
