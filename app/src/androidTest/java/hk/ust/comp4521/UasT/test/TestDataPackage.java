package hk.ust.comp4521.UasT.test;

import android.test.AndroidTestCase;

import org.json.JSONException;
import org.json.JSONObject;

import hk.ust.comp4521.UasT.data.Course;
import hk.ust.comp4521.UasT.data.CourseComment;
import hk.ust.comp4521.UasT.data.Group;

public class TestDataPackage extends AndroidTestCase
{

	public void testCourse() throws JSONException
	{
		Course c = new Course();
		Course d = new Course();

		JSONObject obj = new JSONObject();
		obj.put("name", "NAME");
		obj.put("code", "CODE");
		obj.put("desc", "DESC");
		obj.put("credit", "1");
		obj.put("rating", "1");
		obj.put("ratingTotal", "1");
		c.load(obj);
		d.load(obj);
		assertEquals("DESC", c.getDesc());
		assertEquals(1, c.getRating());
		assertFalse(c.getSub().equals("1234"));
		assertFalse(c.getTitle().equals("1234"));
		assertFalse(c.filter("hello"));
		assertNotNull(c.getFragment());
        assertNotNull(d.getFragment());
	}

	public void testCourseComment() throws JSONException
	{
		CourseComment c = new CourseComment();
		CourseComment d = new CourseComment();

		JSONObject obj = new JSONObject();
		obj.put("title", "TITLE");
		obj.put("author", "AUTHOR");
		obj.put("authorId", "ID");
		obj.put("post", "POST");
		obj.put("time", "5");
		c.load(obj);
		d.load(obj);

		assertFalse(c.compareTo(d) == 5);

	}

	public void testGroup() throws JSONException
	{
		Group g = new Group();
		g.setAdmin("ADMIN");
		g.setGroupMembers("MEMBER");
		g.setTitle("TITLE");
		assertEquals("ADMIN", g.getAdmin());
		assertEquals("MEMBER", g.getGroupMembers());
		assertEquals("TITLE", g.getTitle());

	}

	//public void testDatabase() throws JSONException {}
}
