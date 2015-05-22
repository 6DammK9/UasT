package hk.ust.comp4521.UasT;

import android.app.AlertDialog;
import android.content.Context;
import android.util.AttributeSet;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import hk.ust.comp4521.UasT.data.ApiHandler;
import hk.ust.comp4521.UasT.data.ApiManager;
import hk.ust.comp4521.UasT.data.CourseTrading;
import hk.ust.comp4521.UasT.data.Database;
import hk.ust.comp4521.UasT.data.ThreadItem;
import hk.ust.comp4521.UasT.json.ApiResponseLike;

// Used in course group list
public class CourseTradingCardView extends ThreadCardView {
    CourseTrading courseTrading;

	public CourseTradingCardView(Context context) {
		super(context);
	}

	public CourseTradingCardView(Context context, AttributeSet attrs)
	{
		super(context, attrs);
	}

	public CourseTradingCardView(Context context, AttributeSet attrs, int defStyle)
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
        vh.likeMessage = (TextView)findViewById(R.id.likeMessage);
        vh.likeButton = (TextView)findViewById(R.id.likeButton);
	}

    @Override
    protected void onFinishInflate() {
        super.onFinishInflate();
        setTag(createViewHolder());
        initViews();
    }

	@Override
	public void setThread(ThreadItem thread) {
        courseTrading = (CourseTrading)thread;
		ViewHolder vh = (ViewHolder) getTag();

        vh.likeMessage.setText(courseTrading.getRating() + " Likes");

        vh.likeButton.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View view) {
                ViewHolder vh = (ViewHolder) getTag();
                AlertDialog.Builder builder = new AlertDialog.Builder(CourseTradingCardView.this.getContext());
                ProgressBar bar = new ProgressBar(CourseTradingCardView.this.getContext(), null,
                        android.R.attr.progressBarStyleHorizontal);
                bar.setIndeterminate(true);
                builder.setCancelable(true).setTitle("Sending request to server").setView(bar);
                final AlertDialog dialog = builder.show();

                ApiManager.likeTrading(courseTrading.getCourseId(), courseTrading.getKey(), Database.getUser().getITSC(),
                        new ApiHandler<ApiResponseLike>() {

                            @Override
                            public void onSuccess(ApiResponseLike response) {
                                dialog.dismiss();
                                ViewHolder vh = (ViewHolder) getTag();

                                vh.likeButton.setText(response.getMessage());
                                vh.likeMessage.setText(response.getNum() + " Likes");
                            }

                            @Override
                            public void onFailure(String message) {
                                dialog.dismiss();
                                Toast.makeText(CourseTradingCardView.this.getContext(), message,
                                        Toast.LENGTH_LONG).show();
                            }

                        });
            }
        });

		super.setThread(thread);
	}
	
	protected static class ViewHolder extends ThreadCardView.ViewHolder {
        public TextView likeMessage;
        public TextView likeButton;
	}
	
}

