package hk.ust.comp4521.exust;

import hk.ust.comp4521.exust.CourseCardView.ViewHolder;
import hk.ust.comp4521.exust.data.Course;
import hk.ust.comp4521.exust.data.ThreadItem;
import android.content.Context;
import android.util.AttributeSet;
import android.widget.RatingBar;

public class GroupHeaderCardView extends ThreadCardView {
	public GroupHeaderCardView(Context context) {
		super(context);
	}
	
	public GroupHeaderCardView(Context context, AttributeSet attrs)
	{
		super(context, attrs);
	}

	public GroupHeaderCardView(Context context, AttributeSet attrs, int defStyle)
	{
		super(context, attrs, defStyle);
	}
}
