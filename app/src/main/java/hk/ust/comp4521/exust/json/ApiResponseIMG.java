package hk.ust.comp4521.exust.json;

import org.json.JSONException;
import org.json.JSONObject;

public class ApiResponseIMG extends ApiResponseBase {

    String name;

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
