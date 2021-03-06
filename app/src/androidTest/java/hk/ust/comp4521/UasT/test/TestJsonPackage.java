/**
 * 	#COMP 4521
 *      #Cheung Wai Yip	20126604	wycheungae@connect.ust.hk
 *      #Lau Tsz Hei		20113451	thlauac@connect.ust.hk
 *      #Ho Kam Ming	20112316	kmhoab@connect.ust.hk
 */

package hk.ust.comp4521.UasT.test;

import android.test.AndroidTestCase;

import org.json.JSONException;
import org.json.JSONObject;

import hk.ust.comp4521.UasT.json.ApiResponseBase;
import hk.ust.comp4521.UasT.json.ApiResponseData;

public class TestJsonPackage extends AndroidTestCase
{

	public void testBase() throws JSONException
	{
		JSONObject obj = new JSONObject();
		obj.put("code", 200);
		obj.put("msg", "OK");
		ApiResponseBase ab = new ApiResponseBase();
		assertNotNull(ab);
		ab.load(obj);
		assertEquals(obj.getInt("code"), 200);
		assertEquals(obj.getString("msg"), "OK");

	}

	public void testData() throws JSONException
	{
		JSONObject obj1 = new JSONObject();
		obj1.put("code", 200);
		obj1.put("msg", "OK");

		JSONObject obj = new JSONObject();
		obj.put("data", obj1);
		obj.put("hash", "md5");
		obj.put("code", 404);
		obj.put("msg", "NOT_FOUND");

		ApiResponseData arData = new ApiResponseData();
		assertNotNull(arData);
		arData.load(obj);
		assertEquals(obj.getJSONObject("data"), obj1);
		assertEquals(obj.get("hash"), "md5");
		assertEquals(obj.getInt("code"), 404);
		assertEquals(obj.getString("msg"), "NOT_FOUND");
	}

}
