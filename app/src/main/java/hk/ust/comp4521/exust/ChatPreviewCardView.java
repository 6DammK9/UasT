package hk.ust.comp4521.exust;

import android.content.Context;
import android.text.Html;
import android.util.AttributeSet;

import hk.ust.comp4521.exust.data.Chat;
import hk.ust.comp4521.exust.data.ThreadItem;

// Used in course group list
public class ChatPreviewCardView extends ThreadCardView {

	public ChatPreviewCardView(Context context) {
		super(context);
	}

	public ChatPreviewCardView(Context context, AttributeSet attrs)
	{
		super(context, attrs);
	}

	public ChatPreviewCardView(Context context, AttributeSet attrs, int defStyle)
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
	}

    @Override
    protected void onFinishInflate() {
        super.onFinishInflate();
        setTag(createViewHolder());
        initViews();
    }

	@Override
	public void setThread(ThreadItem thread) {
        Chat chat = (Chat)thread;
        ViewHolder vh = (ViewHolder) getTag();
        vh.title.setText(thread.getTitle());
        if (chat.getIsGroup()) {
            vh.title.setText(Html.fromHtml("<u><b>GROUP:</b>" + thread.getTitle() + "</u>"));
        } else {
            vh.title.setText(thread.getTitle());
        }
        vh.sub.setText(thread.getSub());
	}

	protected static class ViewHolder extends ThreadCardView.ViewHolder {
	}

}

