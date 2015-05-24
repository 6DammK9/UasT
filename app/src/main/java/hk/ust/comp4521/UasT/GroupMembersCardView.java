/**
 * 	#COMP 4521
 *      #Cheung Wai Yip	20126604	wycheungae@connect.ust.hk
 *      #Lau Tsz Hei		20113451	thlauac@connect.ust.hk
 *      #Ho Kam Ming	20112316	kmhoab@connect.ust.hk
 */

package hk.ust.comp4521.UasT;

import android.app.AlertDialog;
import android.content.Context;
import android.text.Html;
import android.util.AttributeSet;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import hk.ust.comp4521.UasT.data.ApiHandler;
import hk.ust.comp4521.UasT.data.ApiManager;
import hk.ust.comp4521.UasT.data.Database;
import hk.ust.comp4521.UasT.data.GroupMember;
import hk.ust.comp4521.UasT.data.ThreadItem;
import hk.ust.comp4521.UasT.json.ApiResponseAddChat;
import hk.ust.comp4521.UasT.json.ApiResponseBase;

// Used in course group list
public class GroupMembersCardView extends ThreadCardView {
    GroupMember groupMember;

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
        vh.delButton = (Button)findViewById(R.id.delButton);
        vh.baseLayout = (LinearLayout)findViewById(R.id.baseLayout);
	}

    @Override
    protected void onFinishInflate() {
        super.onFinishInflate();
        setTag(createViewHolder());
        initViews();
    }

	@Override
	public void setThread(ThreadItem thread) {
        groupMember = (GroupMember)thread;
		ViewHolder vh = (ViewHolder) getTag();

        vh.delButton.setVisibility(INVISIBLE);

        if (groupMember.isLeader()) {
            vh.text1.setTextColor(android.graphics.Color.RED);
            vh.text1.setText(Html.fromHtml("<u>Group Leader</u>"));
        } else {
            vh.text1.setTextColor(android.graphics.Color.BLACK);
            vh.text1.setText("Members");

            if (Database.getUser().getITSC().equals(groupMember.getLeaderId())) {
                //Log.i("exust.GroupMemCard", "isLeader=" + groupMember.isLeader() + " this.ITSC=" + (String) groupMember.getSub() + " User.ITSC=" + Database.getUser().getITSC());
                vh.delButton.setVisibility(VISIBLE);
                vh.delButton.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        ViewHolder vh = (ViewHolder) getTag();
                        AlertDialog.Builder builder = new AlertDialog.Builder(GroupMembersCardView.this.getContext());
                        ProgressBar bar = new ProgressBar(GroupMembersCardView.this.getContext(), null,
                                android.R.attr.progressBarStyleHorizontal);
                        bar.setIndeterminate(true);
                        builder.setCancelable(true).setTitle("Sending request to server").setView(bar);
                        final AlertDialog dialog = builder.show();

                        ThreadListFragment f = (ThreadListFragment) ((MainActivity)GroupMembersCardView.this.getContext()).getParentFragment();
                        ApiManager.kickGroup(f.getKey(), groupMember.getGroupId(), (String)vh.sub.getText(), (String)vh.title.getText(),
                                new ApiHandler<ApiResponseBase>() {

                            @Override
                            public void onSuccess(ApiResponseBase response) {
                                dialog.dismiss();
                                Toast.makeText(GroupMembersCardView.this.getContext(),
                                        response.getMessage(), Toast.LENGTH_LONG)
                                        .show();
                                MainActivity main = (MainActivity) GroupMembersCardView.this.getContext();
                                main.popFragment();
                            }

                            @Override
                            public void onFailure(String message) {
                                dialog.dismiss();
                                Toast.makeText(GroupMembersCardView.this.getContext(), message,
                                        Toast.LENGTH_LONG).show();
                            }

                        });
                    }
                });
            }
        }

        vh.baseLayout.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View view) {
                ViewHolder vh2 = (ViewHolder) getTag();
                AlertDialog.Builder builder = new AlertDialog.Builder(GroupMembersCardView.this.getContext());
                ProgressBar bar = new ProgressBar(GroupMembersCardView.this.getContext(), null, android.R.attr.progressBarStyleHorizontal);
                bar.setIndeterminate(true);
                builder.setCancelable(false).setTitle("Sending request to server")
                        .setView(bar);
                final AlertDialog dialog = builder.show();

                ApiManager.addChat((String) vh2.sub.getText(),
                        new ApiHandler<ApiResponseAddChat>() {

                            @Override
                            public void onSuccess(ApiResponseAddChat response) {
                                dialog.dismiss();
                                Toast.makeText(GroupMembersCardView.this.getContext(),
                                        response.getMessage(), Toast.LENGTH_LONG)
                                        .show();
                                MainActivity main = (MainActivity) GroupMembersCardView.this.getContext();
                                ChatFragment post = new ChatFragment();
                                post.setParam(response.getChat());
                                main.popFragment(2, 1);
                                main.gotoFragment(2, post);
                            }

                            @Override
                            public void onFailure(String message) {
                                dialog.dismiss();
                                Toast.makeText(GroupMembersCardView.this.getContext(), message, Toast.LENGTH_LONG).show();
                            }

                });
            }
        });




		super.setThread(thread);
	}
	
	protected static class ViewHolder extends ThreadCardView.ViewHolder {
        public TextView text1;
        public Button delButton;
        public LinearLayout baseLayout;
	}
	
}

