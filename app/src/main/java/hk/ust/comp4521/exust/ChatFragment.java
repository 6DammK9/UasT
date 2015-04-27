package hk.ust.comp4521.exust;

import android.app.AlertDialog;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ProgressBar;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Comparator;

import hk.ust.comp4521.exust.data.ApiHandler;
import hk.ust.comp4521.exust.data.ApiManager;
import hk.ust.comp4521.exust.data.CalendarEvent;
import hk.ust.comp4521.exust.data.Chat;
import hk.ust.comp4521.exust.data.ChatItem;
import hk.ust.comp4521.exust.data.ChatItems;
import hk.ust.comp4521.exust.data.Database;
import hk.ust.comp4521.exust.data.DatabaseLoad;
import hk.ust.comp4521.exust.data.ThreadItem;
import hk.ust.comp4521.exust.json.ApiResponseBase;

public class ChatFragment extends ThreadListFragment {

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		layout_id = R.layout.fragment_chat;
		View view = super.onCreateView(inflater, container, savedInstanceState);

		list.setDividerHeight(0);
		message = (EditText) view.findViewById(R.id.message);
		send = (ImageButton) view.findViewById(R.id.send);

		send.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				AlertDialog.Builder builder = new AlertDialog.Builder(
						getActivity());
				ProgressBar bar = new ProgressBar(getActivity(), null,
						android.R.attr.progressBarStyleHorizontal);
				bar.setIndeterminate(true);
				builder.setCancelable(false)
						.setTitle("Sending request to server").setView(bar);
				final AlertDialog dialog = builder.show();

				ApiManager.send(chat.getKey(), message.getText().toString(),
						new ApiHandler<ApiResponseBase>() {

							@Override
							public void onSuccess(ApiResponseBase response) {
								dialog.dismiss();
								Toast.makeText(getActivity(),
										response.getMessage(),
										Toast.LENGTH_LONG).show();
								message.setText("");
								getThread();
							}

							@Override
							public void onFailure(String message) {
								dialog.dismiss();
								Toast.makeText(getActivity(), message,
										Toast.LENGTH_LONG).show();
							}

						});
			}

		});

		return view;
	}

	@Override
	public String getTitle() {
		return chat.getTitle();
	}

	Chat chat;
	ImageButton send;
	EditText message;

	public void setParam(Chat chat) {
		this.chat = chat;
		super.setParams2(chat.getKey(), ChatItem.class);
	}

	@Override
	public void getThread() {
		Database.getDataSingle(info.type(), key, ChatItems.class,
				new DatabaseLoad<ChatItems>() {
					@Override
					public void load(ChatItems obj) {
						if (obj == null)
							return;
						threads = new ArrayList<ThreadItem>();
						threads.addAll(Arrays.asList(obj.getChats()));
						if (info.sort()) {
							Collections.sort(threads,
									new Comparator<ThreadItem>() {
										@Override
										public int compare(ThreadItem a,
												ThreadItem b) {
											return a.compareTo(b);
										}
									});
						}
						update();
						list.setSelection(threads.size() - 1);
					}
				});
	}

	@Override
	public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
		super.onCreateOptionsMenu(menu, inflater);
		if (!chat.getIsGroup())
			inflater.inflate(R.menu.chat_del, menu);
		inflater.inflate(R.menu.chat_match, menu);
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		switch (item.getItemId()) {

		case R.id.del: {
			AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
			ProgressBar bar = new ProgressBar(getActivity(), null,
					android.R.attr.progressBarStyleHorizontal);
			bar.setIndeterminate(true);
			builder.setCancelable(false).setTitle("Sending request to server")
					.setView(bar);
			final AlertDialog dialog = builder.show();

			ApiManager.delChat(key, new ApiHandler<ApiResponseBase>() {

				@Override
				public void onSuccess(ApiResponseBase response) {
					dialog.dismiss();
					Toast.makeText(getActivity(), response.getMessage(),
							Toast.LENGTH_LONG).show();
					MainActivity main = (MainActivity) getActivity();
					main.popFragment();
				}

				@Override
				public void onFailure(String message) {
					dialog.dismiss();
					Toast.makeText(getActivity(), message, Toast.LENGTH_LONG)
							.show();
				}

			});
		}
			return true;

		case R.id.match: {
			AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
			ProgressBar bar = new ProgressBar(getActivity(), null,
					android.R.attr.progressBarStyleHorizontal);
			bar.setIndeterminate(true);
			builder.setCancelable(true).setTitle("Sending request to server")
					.setView(bar);
			final AlertDialog dialog = builder.show();

            /**
			String[] calendar = Database.getUser().getCalendar();
			boolean[] avail = new boolean[calendar.length];

            for(int i = 0; i < calendar.length; i++)
                avail[i] = calendar[i] == null || calendar[i].isEmpty();
**/
            ArrayList<CalendarEvent> calendar2 = Database.getUser().getCalendar2();

            //Make 2 array: Start, End
            String CalStart[] = new String[calendar2.size()];
            String CalEnd[] = new String[calendar2.size()];
            String CalFreq[] = new String[calendar2.size()];
            for (int i=0; i < calendar2.size(); i++) {
                CalStart[i] = calendar2.get(i).getFrom().toString();
                CalEnd[i] = calendar2.get(i).getTo().toString();
                CalFreq[i] = calendar2.get(i).getFreq();
            }


			//ApiManager.match(key, avail,
            ApiManager.match2(key, CalStart, CalEnd, CalFreq,
                    new ApiHandler<ApiResponseBase>() {

                        @Override
                        public void onSuccess(ApiResponseBase response) {
                            dialog.dismiss();
                            Toast.makeText(getActivity(),
                                    response.getMessage(), Toast.LENGTH_LONG)
                                    .show();
                            getThread();
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
