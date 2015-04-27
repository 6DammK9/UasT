package hk.ust.comp4521.exust.data;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import hk.ust.comp4521.exust.R;

// List of Group
@ThreadItemInfo(sort = true, title = "Group Members", type = "groupinfos", typeName = "Group", layout = R.layout.view_thread_card)
public class GroupMembers extends ThreadItem {

	@Override
	public void load(JSONObject obj) throws JSONException {
		JSONObject _members = obj.getJSONObject("users");
		JSONArray membersIds = _members.names();
		title = obj.getString("title");
		sub = obj.getString("content");
		
		members = new GroupMember[membersIds == null ? 0 : membersIds.length()];
		
		for(int i = 0; i < members.length; i++) {
			GroupMember member = members[i] = new GroupMember();
            member.loadWithLeader(_members.getJSONObject(membersIds.getString(i)), obj.getString("authorId"));
			//member.load(_members.getJSONObject(membersIds.getString(i)));
		}
	}

	GroupMember[] members;
	
	public GroupMember[] getMembers() {
		return members;
	}
}
