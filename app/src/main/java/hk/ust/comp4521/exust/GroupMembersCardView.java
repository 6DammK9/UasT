package hk.ust.comp4521.exust;

import android.content.Context;
import android.text.Html;
import android.text.method.LinkMovementMethod;
import android.util.AttributeSet;
import android.widget.TextView;

import hk.ust.comp4521.exust.data.GroupMember;
import hk.ust.comp4521.exust.data.ThreadItem;

// Used in course group list
public class GroupMembersCardView extends ThreadCardView {

	public GroupMembersCardView(Context context) {
		super(context);
	}

	public GroupMembersCardView(Context context, AttributeSet attrs)
	{
		super(context, attrs);
	}

	public GroupMembersCardView(Context context, AttributeSet attrs, int defStyle)
	{
		super(context, attrs, defStyle);
	}
	
	@Override
	protected ViewHolder createViewHolder() {
		return new ViewHolder();
	}
	
	@Override
	protected void initViews() {
		super.initViews();
		ViewHolder vh = (ViewHolder) getTag();
        vh.text1 = (TextView)findViewById(R.id.textView);
	}

    @Override
    protected void onFinishInflate() {
        super.onFinishInflate();
        setTag(createViewHolder());
        initViews();
    }

	@Override
	public void setThread(ThreadItem thread) {
        GroupMember groupMember = (GroupMember)thread;
		ViewHolder vh = (ViewHolder) getTag();
        if (groupMember.isLeader()) {
            vh.text1.setTextColor(android.graphics.Color.RED);
            vh.text1.setMovementMethod(LinkMovementMethod.getInstance());
            vh.text1.setText(Html.fromHtml("<u>Group Leader</u>"));
        } else {
            vh.text1.setTextColor(android.graphics.Color.BLACK);
            vh.text1.setText("Members");
        }
		super.setThread(thread);
	}
	
	protected static class ViewHolder extends ThreadCardView.ViewHolder {
        public TextView text1;
	}
	
}

