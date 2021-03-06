/**
 * 	#COMP 4521
 *      #Cheung Wai Yip	20126604	wycheungae@connect.ust.hk
 *      #Lau Tsz Hei		20113451	thlauac@connect.ust.hk
 *      #Ho Kam Ming	20112316	kmhoab@connect.ust.hk
 */

package hk.ust.comp4521.UasT;

import android.content.Context;
import android.util.AttributeSet;
import android.widget.LinearLayout;
import android.widget.TextView;

import hk.ust.comp4521.UasT.data.ThreadItem;

public class ThreadCardView extends LinearLayout {

	public ThreadCardView(Context context) {
		super(context);
	}

	public ThreadCardView(Context context, AttributeSet attrs) {
		super(context, attrs);
	}

	public ThreadCardView(Context context, AttributeSet attrs, int defStyle) {
		super(context, attrs, defStyle);
	}
	
	protected ViewHolder createViewHolder() {
		return new ViewHolder();
	}
	
	protected void initViews() {
		ViewHolder vh = (ViewHolder) getTag();
		vh.title = (TextView) findViewById(R.id.title);
		vh.sub = (TextView) findViewById(R.id.sub);
	}

	@Override
	protected void onFinishInflate() {
		super.onFinishInflate();
		setTag(createViewHolder());
		initViews();
	}

	public void setThread(ThreadItem thread) {
		ViewHolder vh = (ViewHolder) getTag();
		vh.title.setText(thread.getTitle());
        vh.sub.setText(thread.getSub());
	}

	protected static class ViewHolder {
		public TextView title;
		public TextView sub;
	}
}
