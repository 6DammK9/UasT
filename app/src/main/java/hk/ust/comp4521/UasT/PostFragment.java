package hk.ust.comp4521.UasT;

import android.app.AlertDialog;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.RatingBar;
import android.widget.Toast;

import hk.ust.comp4521.UasT.data.ApiHandler;
import hk.ust.comp4521.UasT.data.ApiManager;
import hk.ust.comp4521.UasT.data.ThreadItemInfo;
import hk.ust.comp4521.UasT.json.ApiResponseBase;

public class PostFragment extends BaseFragment {

	ThreadItemInfo info;
	String code;
	EditText title, details, content;
	RatingBar rating;

	public PostFragment() {

	}

	public void setCode(ThreadItemInfo info, String code) {
		this.info = info;
		this.code = code;
	}

	@Override
	public void onCreate(Bundle savedInstanceState) {
		setHasOptionsMenu(true);
		super.onCreate(savedInstanceState);
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		View view = inflater.inflate(R.layout.fragment_post, container, false);
		title = (EditText) view.findViewById(R.id.title);
		details = (EditText) view.findViewById(R.id.details);
		content = (EditText) view.findViewById(R.id.content);
		rating = (RatingBar) view.findViewById(R.id.rating);
		
		if(info.type().equals("comments")) {
			rating.setVisibility(View.VISIBLE);
		}
		return view;
	}

	@Override
	public String getTitle() {
		return "New " + info.typeName();
	}

	@Override
	public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
		inflater.inflate(R.menu.post, menu);
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		switch (item.getItemId()) {
		case R.id.confirm: {

			AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
			ProgressBar bar = new ProgressBar(getActivity(), null,
					android.R.attr.progressBarStyleHorizontal);
			bar.setIndeterminate(true);
			builder.setCancelable(true).setTitle("Sending request to server")
					.setView(bar);
			final AlertDialog dialog = builder.show();

			ApiManager.post(info.type(), code, title.getText().toString(),
					details.getText().toString(), content.getText().toString(),
					rating.getProgress(), new ApiHandler<ApiResponseBase>() {

						@Override
						public void onSuccess(ApiResponseBase response) {
							dialog.dismiss();
							Toast.makeText(getActivity(),
									response.getMessage(), Toast.LENGTH_LONG)
									.show();
							MainActivity main = (MainActivity) getActivity();
							main.popFragment();
						}

						@Override
						public void onFailure(String message) {
							dialog.dismiss();
							Toast.makeText(getActivity(), message,
									Toast.LENGTH_LONG).show();
						}

					});
		}
			return true;
		default:
			return super.onOptionsItemSelected(item);
		}
	}
}
