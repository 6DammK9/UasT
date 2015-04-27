package hk.ust.comp4521.exust;

import android.content.Context;
import android.util.AttributeSet;
import android.widget.RadioButton;

import hk.ust.comp4521.exust.data.ThreadItem;

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
        vh.testButton = (RadioButton)findViewById(R.id.radioButton);
	}

	@Override
	public void setThread(ThreadItem thread) {
		ViewHolder vh = (ViewHolder) getTag();
        vh.testButton.setText("on99");
		super.setThread(thread);
	}
	
	protected static class ViewHolder extends ThreadCardView.ViewHolder {
        public RadioButton testButton;
	}
	
}

