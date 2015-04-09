package hk.ust.comp4521.exust;

import hk.ust.comp4521.exust.data.CourseComment;
import hk.ust.comp4521.exust.data.ThreadItem;
import android.content.Context;
import android.util.AttributeSet;
import android.widget.RatingBar;

public class CommentCardView extends ThreadCardView {
	public CommentCardView(Context context) {
		super(context);
	}
	
	public CommentCardView(Context context, AttributeSet attrs)
	{
		super(context, attrs);
	}

	public CommentCardView(Context context, AttributeSet attrs, int defStyle)
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
		vh.rating = (RatingBar) findViewById(R.id.rating);
	}

	@Override
	public void setThread(ThreadItem thread) {
		CourseComment course = (CourseComment)thread;
		ViewHolder vh = (ViewHolder) getTag();
		vh.rating.setProgress(course.getRating());
		super.setThread(thread);
	}
	
	protected static class ViewHolder extends ThreadCardView.ViewHolder {
		public RatingBar rating;
	}
}
