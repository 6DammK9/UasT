/**
 * COMP4521 Project
 *      Cheung Wai Yip
 *      Lau Tsz Hei
 *      Ho Kam Ming
 */

package hk.ust.comp4521.UasT.json;

import org.json.JSONException;
import org.json.JSONObject;

public class ApiResponseLike extends ApiResponseBase {
    int num;

    public void load(JSONObject obj) throws JSONException {
        super.load(obj);
        if(obj.has("num"))
            num = obj.getInt("num");
    }

    public int getNum() {
        return num;
    }
}
