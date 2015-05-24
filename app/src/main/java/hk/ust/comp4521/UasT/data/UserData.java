/**
 * 	#COMP 4521
 *      #Cheung Wai Yip	20126604	wycheungae@connect.ust.hk
 *      #Lau Tsz Hei		20113451	thlauac@connect.ust.hk
 *      #Ho Kam Ming	20112316	kmhoab@connect.ust.hk
 */

package hk.ust.comp4521.UasT.data;

import android.util.Log;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.Random;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class UserData {
    String email = "", auth, name = "User " + Math.abs(new Random().nextInt());
    final Map<String, String> favorite = new HashMap<String, String>();
    ArrayList<CalendarEvent> calendar2;

    public ArrayList<CalendarEvent> getCalendar2() {
        if (calendar2 == null) {
            calendar2 = new ArrayList<CalendarEvent>();
        }
        return calendar2;
    }

    public Map<String, String> getFavorite() {
        return favorite;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    //public String getAuth() { return auth; }

    public void setAuth(String auth) {
        this.auth = auth;
    }

    public boolean isLogon() {
        return email != null && !email.isEmpty() && auth != null
                && !auth.isEmpty();
    }

    static final Pattern itscPattern = Pattern.compile(
            "^([A-Z._]+)@[A-Z.]*ust\\.hk$", Pattern.CASE_INSENSITIVE);

    public String getITSC() {
        if (email != null && auth != null) {
            Matcher matcher = itscPattern.matcher(email);
            if (matcher.find())
                return matcher.group(1);
        }
        return null;
    }

    public void load(String data) {
        if (data != null) {
            try {
                JSONObject obj = new JSONObject(data);
                if (obj.has("email"))
                    email = obj.getString("email");
                if (obj.has("auth"))
                    auth = obj.getString("auth");
                if (obj.has("name"))
                    name = obj.getString("name");
                if (obj.has("calendar2")) {
                    JSONArray _calendar2 = obj.getJSONArray("calendar2");
                    calendar2 = new ArrayList<CalendarEvent>();
                    for (int i = 0; i < _calendar2.length(); i++)
                        calendar2.add(new CalendarEvent(_calendar2.getString(i)));
                }
                favorite.clear();
                if (obj.has("favorite")) {
                    JSONArray _favorite = obj.getJSONArray("favorite");
                    for (int i = 0; i < _favorite.length(); i++)
                        favorite.put(_favorite.getString(i), null);
                }
            } catch (JSONException e) {
                Log.i("UasT UserData", e.toString());
            }
        }
    }

    public String save() {
        try {
            JSONObject obj = new JSONObject();
            if (email != null)
                obj.put("email", email);
            if (auth != null)
                obj.put("auth", auth);
            if (name != null)
                obj.put("name", name);
            if (favorite != null) {
                JSONArray _favorite = new JSONArray();
                for (String item : favorite.keySet())
                    _favorite.put(item);
                obj.put("favorite", _favorite);
            }
            if (calendar2 != null) {
                JSONArray _calendar2 = new JSONArray();
                for (int i = 0; i < calendar2.size(); i++)
                    _calendar2.put(calendar2.get(i).toString());
                obj.put("calendar2", _calendar2);
            }
            return obj.toString();
        } catch (JSONException e) {
            return null;
        }
    }

    public void setCalendar2(ArrayList<CalendarEvent> in) {
        this.calendar2 = in;
    }
}
