package hk.ust.comp4521.exust.test;

import org.json.JSONException;
import org.json.JSONObject;

import android.test.AndroidTestCase;
import hk.ust.comp4521.exust.*;
import hk.ust.comp4521.exust.data.*;

public class TestDataPackage extends AndroidTestCase
{

	public void testCourse() throws JSONException
	{
		Course c = new Course();
		Course d = new Course();

		JSONObject obj = new JSONObject();
		obj.put("name", "1");
		obj.put("code", "1");
		obj.put("desc", "1");
		obj.put("credit", "1");
		obj.put("rating", "1");
		obj.put("ratingTotal", "1");
		c.load(obj);
		d.load(obj);
		assertEquals("1", c.getDesc());
		assertEquals(1, c.getRating());
		assertFalse(c.getSub() == "1234");
		assertFalse(c.getTitle() == "1234");
		assertFalse(c.filter("hello"));
		assertNotNull(c.getFragment());

	}

	public void testCourseComment() throws JSONException
	{
		CourseComment c = new CourseComment();
		CourseComment d = new CourseComment();

		JSONObject obj = new JSONObject();
		obj.put("title", "1");
		obj.put("author", "2");
		obj.put("authorId", "3");
		obj.put("post", "4");
		obj.put("time", "5");
		c.load(obj);
		d.load(obj);

		assertFalse(c.compareTo(d) == 5);

	}

	public void testGroup() throws JSONException
	{
		Group g = new Group();
		g.setAdmin("a");
		g.setGroupMembers("b");
		g.setTitle("c");
		assertEquals("a", g.getAdmin());
		assertEquals("b", g.getGroupMembers());
		assertEquals("c", g.getTitle());

	}

	public void testDatabase() throws JSONException
	{
		//

	}
}
