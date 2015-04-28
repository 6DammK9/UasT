package hk.ust.comp4521.exust;

import android.app.AlertDialog;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import hk.ust.comp4521.exust.data.ApiHandler;
import hk.ust.comp4521.exust.data.ApiManager;
import hk.ust.comp4521.exust.data.Database;
import hk.ust.comp4521.exust.data.DatabaseLoad;
import hk.ust.comp4521.exust.data.Post;
import hk.ust.comp4521.exust.data.ThreadItemInfo;
import hk.ust.comp4521.exust.data.ThreadPostItem;
import hk.ust.comp4521.exust.json.ApiResponseAddChat;
import hk.ust.comp4521.exust.json.ApiResponseBase;

public class ViewPostFragment extends BaseFragment {
	public ViewPostFragment() {

	}

	ThreadItemInfo info;
	ThreadPostItem post;

	public void setCode(ThreadPostItem post) {
		this.info = post.getClass().getAnnotation(ThreadItemInfo.class);
		this.post = post;
	}

	@Override
	public String getTitle() {
		return post.getTitle();
	}

	@Override
	public void onCreate(Bundle savedInstanceState) {
		setHasOptionsMenu(true);
		super.onCreate(savedInstanceState);
	}

	TextView content;
	Post postData;

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		View view = inflater.inflate(R.layout.fragment_view_post, null);
		content = (TextView) view.findViewById(R.id.content);

		Database.getDataSingle("posts", post.getKey(), Post.class,
				new DatabaseLoad<Post>() {
					@Override
					public void load(Post data) {
						postData = data;
						updateView();
					}
				});
		return view;
	}

	void updateView() {
		if (postData != null)
			content.setText(postData.getContent());
	}

	@Override
	public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
		inflater.inflate(R.menu.view_post, menu);
		if (post.getAuthorId().equals(Database.getUser().getITSC())) {
			inflater.inflate(R.menu.view_post_del, menu);
		} else {
			inflater.inflate(R.menu.view_post_contact, menu);
		}
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {

		ThreadListFragment f = (ThreadListFragment) ((MainActivity) getActivity())
				.getParentFragment();
		String key = f.getKey();

		switch (item.getItemId()) {
		case R.id.del: {
			AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
			ProgressBar bar = new ProgressBar(getActivity(), null,
					android.R.attr.progressBarStyleHorizontal);
			bar.setIndeterminate(true);
			builder.setCancelable(true).setTitle("Sending request to server")
					.setView(bar);
			final AlertDialog dialog = builder.show();

			ApiManager.delPost(info.type(), key, post.getKey(),
					new ApiHandler<ApiResponseBase>() {

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

		case R.id.contact: {
			AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
			ProgressBar bar = new ProgressBar(getActivity(), null,
					android.R.attr.progressBarStyleHorizontal);
			bar.setIndeterminate(true);
			builder.setCancelable(true).setTitle("Sending request to server")
					.setView(bar);
			final AlertDialog dialog = builder.show();

			ApiManager.addChat(post.getAuthorId(),
					new ApiHandler<ApiResponseAddChat>() {

						@Override
						public void onSuccess(ApiResponseAddChat response) {
							dialog.dismiss();
							Toast.makeText(getActivity(),
									response.getMessage(), Toast.LENGTH_LONG)
									.show();
							MainActivity main = (MainActivity) getActivity();
							ChatFragment post = new ChatFragment();
							post.setParam(response.getChat());
							main.popFragment(2, 1);
							main.gotoFragment(2, post);
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
