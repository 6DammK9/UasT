/**
 * COMP4521 Project
 *      Cheung Wai Yip
 *      Lau Tsz Hei
 *      Ho Kam Ming
 */

package hk.ust.comp4521.UasT;

import android.content.Context;
import android.util.AttributeSet;
import android.view.View;
import android.widget.ImageButton;

import hk.ust.comp4521.UasT.data.Course;
import hk.ust.comp4521.UasT.data.ThreadItem;

public class CourseHeaderCardView  extends ThreadCardView {
	public CourseHeaderCardView(Context context) {
		super(context);
	}
	
	public CourseHeaderCardView(Context context, AttributeSet attrs)
	{
		super(context, attrs);
	}

	public CourseHeaderCardView(Context context, AttributeSet attrs, int defStyle)
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
		vh.favorite = (ImageButton) findViewById(R.id.favorite);
		vh.favorite.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				current.setIsFavorite(!current.getIsFavorite());
				setThread(current);
			}
			
		});
	}
	
	Course current;

	@Override
	public void setThread(ThreadItem thread) {
		ViewHolder vh = (ViewHolder) getTag();
		Course course = (Course)thread;
		current = course;
		vh.title.setText(course.getFullTitle());
		vh.sub.setText(course.getDesc());
		boolean isFav = course.getIsFavorite();
		vh.favorite.setImageDrawable(this.getResources().getDrawable(isFav ? R.drawable.favorite_on : R.drawable.favorite));
		
	}
	
	protected static class ViewHolder extends ThreadCardView.ViewHolder {
		public ImageButton favorite;
	}
}


