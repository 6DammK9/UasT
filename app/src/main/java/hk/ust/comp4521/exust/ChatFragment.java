package hk.ust.comp4521.exust;

import android.app.AlertDialog;
import android.content.Intent;
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
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Comparator;
import java.util.Map;

import hk.ust.comp4521.exust.data.ApiHandler;
import hk.ust.comp4521.exust.data.ApiManager;
import hk.ust.comp4521.exust.data.CalEventsEntry;
import hk.ust.comp4521.exust.data.CalendarEvent;
import hk.ust.comp4521.exust.data.Chat;
import hk.ust.comp4521.exust.data.ChatItem;
import hk.ust.comp4521.exust.data.ChatItems;
import hk.ust.comp4521.exust.data.Database;
import hk.ust.comp4521.exust.data.DatabaseLoad;
import hk.ust.comp4521.exust.data.ThreadItem;
import hk.ust.comp4521.exust.json.ApiResponseBase;

// TODO implement Photo transfer and capture
public class ChatFragment extends ThreadListFragment {

	Chat chat;
	ImageButton send;
	ImageButton attachment;
	ImageButton camera;
    ImageButton image;
    LinearLayout attachment_layout;
	EditText message;
	String[] ChatUsers;
	static final String TAG = "exust.ChatFragment";
	static ArrayList<CalendarEvent> groupCalEvents;

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		layout_id = R.layout.fragment_chat;
		View view = super.onCreateView(inflater, container, savedInstanceState);
		if (view==null) { return null; }

		list.setDividerHeight(0);
		message = (EditText) view.findViewById(R.id.message);
		send = (ImageButton) view.findViewById(R.id.send);
		attachment = (ImageButton) view.findViewById(R.id.attachment);
		camera = (ImageButton) view.findViewById(R.id.camera);
        image = (ImageButton) view.findViewById(R.id.image);

		attachment_layout = (LinearLayout) view.findViewById(R.id.attachment_layout);
		attachment_layout.setVisibility(View.GONE);

		send.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View v) {
                AlertDialog.Builder builder = new AlertDialog.Builder(
                        getActivity());
                ProgressBar bar = new ProgressBar(getActivity(), null,
                        android.R.attr.progressBarStyleHorizontal);
                bar.setIndeterminate(true);
                builder.setCancelable(true).setTitle("Sending request to server").setView(bar);
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

		attachment.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View view) {
				if (attachment_layout.getVisibility() != View.VISIBLE)
					attachment_layout.setVisibility(View.VISIBLE);
				else attachment_layout.setVisibility(View.GONE);
			}
		});

		camera.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View view) {
				Intent cam = new Intent(view.getContext(), Multimedia_photo.class);
				if (Multimedia_photo.checkCameraHardware(view.getContext()))
					startActivityForResult(cam, 100);
				attachment_layout.setVisibility(View.GONE);
			}
		});

        image.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent img = new Intent(view.getContext(), Multimedia_image.class);
                startActivityForResult(img, 100);
                attachment_layout.setVisibility(View.GONE);
            }
        });

		return view;
	}

	@Override
	public String getTitle() {
		return chat.getTitle();
	}

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

						ChatUsers = obj.getUsers();
					}
				});
	}

	@Override
	public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
		super.onCreateOptionsMenu(menu, inflater);
		if (!chat.getIsGroup()) {
            inflater.inflate(R.menu.chat_del, menu);
            inflater.inflate(R.menu.chat_add, menu);
        }
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
			builder.setCancelable(true).setTitle("Sending request to server")
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

			/**
			AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
			ProgressBar bar = new ProgressBar(getActivity(), null,
					android.R.attr.progressBarStyleHorizontal);
			bar.setIndeterminate(true);
			builder.setCancelable(true).setTitle("Sending request to server")
					.setView(bar);
			final AlertDialog dialog = builder.show();


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

			@Override public void onSuccess(ApiResponseBase response) {
			dialog.dismiss();
			Toast.makeText(getActivity(),
			response.getMessage(), Toast.LENGTH_LONG)
			.show();
			getThread();
			}

			@Override public void onFailure(String message) {
			dialog.dismiss();
			Toast.makeText(getActivity(), message,
			Toast.LENGTH_LONG).show();
			}

			});
			 **/

			groupCalEvents = new ArrayList<CalendarEvent>();
			groupCalEvents.clear();
			for (String ChatUser : ChatUsers) {
				Database.getData("CalEventsEntries", ChatUser, CalEventsEntry.class,
						new DatabaseLoad<Map<String, CalEventsEntry>>() {
							@Override
							public void load(Map<String, CalEventsEntry> obj) {
								if (obj == null)
									return;
								ArrayList<CalEventsEntry> threads = new ArrayList<CalEventsEntry>();
								threads.addAll(obj.values());

								for (int i = 0; i < threads.size(); i++) {
									for (int j = 0; j < threads.get(i).getCalEventArr().length; j++) {
										groupCalEvents.add(new CalendarEvent(threads.get(i).getCalEventArr()[j]));
									}
								}
							}
						});
			}

			//MatchRecord will load groupCalEvents when user press the Show button;
			//Loading groupCalEvents is async so it's not effective if we load it here.
			//Seems it's not API request, loading it through Database is unknown at my sight (Calendar)
			//It is dangerous to put it static; But seems no choice
			if (groupCalEvents == null) return false;
			MatchRecord slots = new MatchRecord();
			MainActivity main = (MainActivity) getActivity();
			main.gotoFragment(2, slots);
		}
			return true;
        case R.id.add: {
            AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
            ProgressBar bar = new ProgressBar(getActivity(), null,  android.R.attr.progressBarStyleHorizontal);
            bar.setIndeterminate(true);
            builder.setCancelable(true).setTitle("Sending request to server") .setView(bar);
            final AlertDialog dialog = builder.show();

            ApiManager.addFriend(key, new ApiHandler<ApiResponseBase>() {

                @Override
                public void onSuccess(ApiResponseBase response) {
                    dialog.dismiss();
                    Toast.makeText(getActivity(), response.getMessage(),Toast.LENGTH_LONG).show();
                }

                @Override
                public void onFailure(String message) {
                    dialog.dismiss();
                    Toast.makeText(getActivity(), message, Toast.LENGTH_LONG).show();
                }

            });
        }
            return true;
		default:
			return super.onOptionsItemSelected(item);
		}
	}
}
