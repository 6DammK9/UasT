/**
 * COMP4521 Project
 *      Cheung Wai Yip
 *      Lau Tsz Hei
 *      Ho Kam Ming
 */

package hk.ust.comp4521.UasT.data;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public class CalEventsEntry extends ThreadPostItem {

    String[] CalEventArr;

    public String[] getCalEventArr() {return CalEventArr;}

    @Override
    public void load(JSONObject obj) throws JSONException {
        JSONArray _CalEventArr = obj.getJSONArray("CalEventArr");
        CalEventArr = new String[_CalEventArr.length()];
        for( int i = 0; i < CalEventArr.length; i++) {
            CalEventArr[i] = _CalEventArr.getString(i);
        }
        title = obj.getString("user");
        authorId = obj.getString("name");
    }

}
