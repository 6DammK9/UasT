package hk.ust.comp4521.UasT.data;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public class Post extends ThreadItem {

	public String getContent() {
		return content;
	}

	public String[] getAttachments() {
		return attachments;
	}

	public String getDetails() {
		return details;
	}

	public String getAuthorId() {
		return authorId;
	}

	public long getTime() {
		return time;
	}

	public int getRating() {
		return rating;
	}

	String content;
	String[] attachments = new String[0];
	String details;
	String authorId;
	long time;
	int rating;

	@Override
	public void load(JSONObject obj) throws JSONException {
		title = obj.getString("title");
		if (obj.has("details"))
			details = obj.getString("details");
		if (obj.has("content"))
			content = obj.getString("content");
		sub = obj.getString("author");
		authorId = obj.getString("authorId");
		key = obj.getString("post");
		time = obj.getLong("time");
		

		if (obj.has("rating"))
			rating = obj.getInt("rating");

		if (obj.has("attachments")) {
			JSONArray attachments = obj.getJSONArray("attachments");
			this.attachments = new String[attachments.length()];
			for (int i = 0; i < attachments.length(); i++)
				this.attachments[i] = attachments.getString(i);
		}
	}

}
