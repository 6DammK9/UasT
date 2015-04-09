package hk.ust.comp4521.exust.test;

import org.json.JSONException;
import org.json.JSONObject;

import android.test.AndroidTestCase;
import hk.ust.comp4521.exust.*;
import hk.ust.comp4521.exust.data.*;
import hk.ust.comp4521.exust.json.ApiResponseBase;
import hk.ust.comp4521.exust.json.ApiResponseData;

public class TestJsonPackage extends AndroidTestCase
{

	public void testBase() throws JSONException
	{
		JSONObject obj = new JSONObject();
		obj.put("code", 3025);
		obj.put("msg", "comp");
		ApiResponseBase ab = new ApiResponseBase();
		assertNotNull(ab);
		ab.load(obj);
		assertEquals(obj.getInt("code"), 3025);
		assertEquals(obj.getString("msg"), "comp");

	}

	public void testData() throws JSONException
	{
		JSONObject obj1 = new JSONObject();
		obj1.put("code", 3025);
		obj1.put("msg", "comp");

		JSONObject obj = new JSONObject();
		obj.put("data", obj1);
		obj.put("hash", "comp");
		obj.put("code", 3025);
		obj.put("msg", "comp");

		ApiResponseData arData = new ApiResponseData();
		assertNotNull(arData);
		arData.load(obj);
		assertEquals(obj.get("hash"), "comp");
		assertEquals(obj.getJSONObject("data"), obj1);

	}

}
