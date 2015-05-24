/**
 * 	#COMP 4521
 *      #Cheung Wai Yip	20126604	wycheungae@connect.ust.hk
 *      #Lau Tsz Hei		20113451	thlauac@connect.ust.hk
 *      #Ho Kam Ming	20112316	kmhoab@connect.ust.hk
 */

package hk.ust.comp4521.UasT.data;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import hk.ust.comp4521.UasT.R;

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
            member.setParam(obj.getString("authorId"), obj.getString("post"));
			if (membersIds != null) {
				member.load(_members.getJSONObject(membersIds.getString(i)));
			}
			//member.load(_members.getJSONObject(membersIds.getString(i)));
		}
	}

	private GroupMember[] members;
	
	public GroupMember[] getMembers() {
		return members;
	}
}
