package hk.ust.comp4521.exust;

import hk.ust.comp4521.exust.CourseHeaderCardView.ViewHolder;
import hk.ust.comp4521.exust.data.Course;
import hk.ust.comp4521.exust.data.ThreadItem;
import android.content.Context;
import android.util.AttributeSet;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ImageButton;
import android.widget.RatingBar;

public class CourseCardView extends ThreadCardView {

	public CourseCardView(Context context) {
		super(context);
	}
	
	public CourseCardView(Context context, AttributeSet attrs)
	{
		super(context, attrs);
	}

	public CourseCardView(Context context, AttributeSet attrs, int defStyle)
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
		Course course = (Course)thread;
		if(course.getIsFavorite())
			this.setBackground(getResources().getDrawable(R.drawable.course_favo_bg));
		else
			this.setBackground(null);
		ViewHolder vh = (ViewHolder) getTag();
		vh.rating.setProgress(course.getRating());
		super.setThread(thread);
	}
	
	protected static class ViewHolder extends ThreadCardView.ViewHolder {
		public RatingBar rating;
	}
	
}

