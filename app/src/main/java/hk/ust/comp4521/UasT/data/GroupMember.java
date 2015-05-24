/**
 * 	#COMP 4521
 *      #Cheung Wai Yip	20126604	wycheungae@connect.ust.hk
 *      #Lau Tsz Hei		20113451	thlauac@connect.ust.hk
 *      #Ho Kam Ming	20112316	kmhoab@connect.ust.hk
 */

package hk.ust.comp4521.UasT.data;

import android.support.annotation.NonNull;

import org.json.JSONException;
import org.json.JSONObject;

import hk.ust.comp4521.UasT.BaseFragment;
import hk.ust.comp4521.UasT.R;

// List of Group members
@ThreadItemInfo(sort = true, title = "Group Members", type = "groupinfos", typeName = "Group", layout = R.layout.view_group_members_card, header = R.layout.view_group_header_card)
public class GroupMember extends ThreadItem {

	long time;
    String leaderId, classId, groupId;

    public void setParam(String LeaderId, String GroupId) {
        this.leaderId = LeaderId;
        this.groupId = GroupId;
    }

	@Override
	public void load(JSONObject obj) throws JSONException {
		if (obj.has("title"))
			title = obj.getString("title");
		sub = obj.getString("id");
		time = obj.getLong("time");
	}

	@Override
	public int compareTo(@NonNull ThreadItem another) {
		GroupMember chat = (GroupMember) another;
		long lhs = time;
		long rhs = chat.time;
		return -(lhs > rhs ? -1 : (lhs == rhs ? 0 : 1));
	}

    public BaseFragment[] getFragment() {
        return null;
    }

    public boolean isLeader() { return (this.sub.equals(this.leaderId)); }

    public String getLeaderId() {
        return this.leaderId;
    }
    public String getGroupId() {
        return this.groupId;
    }
}
