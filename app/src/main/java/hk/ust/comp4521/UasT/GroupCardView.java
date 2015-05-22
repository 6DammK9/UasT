package hk.ust.comp4521.UasT;

import android.content.Context;
import android.util.AttributeSet;
import android.widget.TextView;

import hk.ust.comp4521.UasT.data.CourseGroup;
import hk.ust.comp4521.UasT.data.ThreadItem;

// Used in course group list
public class GroupCardView extends ThreadCardView {

	public GroupCardView(Context context) {
		super(context);
	}

	public GroupCardView(Context context, AttributeSet attrs)
	{
		super(context, attrs);
	}

	public GroupCardView(Context context, AttributeSet attrs, int defStyle)
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
        CourseGroup courseGroup = (CourseGroup)thread;
		ViewHolder vh = (ViewHolder) getTag();
        vh.text1.setText(courseGroup.getStartTime());
		super.setThread(thread);
	}
	
	protected static class ViewHolder extends ThreadCardView.ViewHolder {
        public TextView text1;
	}
	
}

