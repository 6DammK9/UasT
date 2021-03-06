/**
 * 	#COMP 4521
 *      #Cheung Wai Yip	20126604	wycheungae@connect.ust.hk
 *      #Lau Tsz Hei		20113451	thlauac@connect.ust.hk
 *      #Ho Kam Ming	20112316	kmhoab@connect.ust.hk
 */

package hk.ust.comp4521.UasT.json;

import org.json.JSONException;
import org.json.JSONObject;

public class ApiResponseIMG extends ApiResponseBase {

    private String name;

    public void load(JSONObject obj) throws JSONException {
        super.load(obj);
        if(obj.has("name")) {
            name = obj.getString("name");
        }
    }

    public String getIMG() {
        return name;
    }
}
