/**
 * 	#COMP 4521
 *      #Cheung Wai Yip	20126604	wycheungae@connect.ust.hk
 *      #Lau Tsz Hei		20113451	thlauac@connect.ust.hk
 *      #Ho Kam Ming	20112316	kmhoab@connect.ust.hk
 */

package hk.ust.comp4521.UasT;

import android.app.AlertDialog;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.widget.ProgressBar;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Comparator;

import hk.ust.comp4521.UasT.data.ApiHandler;
import hk.ust.comp4521.UasT.data.ApiManager;
import hk.ust.comp4521.UasT.data.CourseGroup;
import hk.ust.comp4521.UasT.data.Database;
import hk.ust.comp4521.UasT.data.DatabaseLoad;
import hk.ust.comp4521.UasT.data.GroupMember;
import hk.ust.comp4521.UasT.data.GroupMembers;
import hk.ust.comp4521.UasT.data.ThreadItem;
import hk.ust.comp4521.UasT.json.ApiResponseBase;

public class EditGroupFragment extends ThreadListFragment {
	private CourseGroup group;
	private boolean isJoined = false;
	private boolean loaded = false;

	public void setParam(CourseGroup group) {
		this.group = group;
		super.setParams(group, GroupMember.class);
	}

	@Override
	public void getThread() {
		Database.getDataSingle(info.type(), key, GroupMembers.class,
				new DatabaseLoad<GroupMembers>() {
					@Override
					public void load(GroupMembers obj) {
						if (obj == null)
							return;
						item = obj;
						updateHeader();
						threads = new ArrayList<ThreadItem>();
						threads.addAll(Arrays.asList(obj.getMembers()));
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
						loaded = true;
						isJoined = false;
						for(int i = 0; i < threads.size(); i++)
							if(threads.get(i).getSub().equals(Database.getUser().getITSC()))
								isJoined = true;
						
						((MainActivity)getActivity()).updateMenu();
					}
				});
	}

	@Override
	public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
		super.onCreateOptionsMenu(menu, inflater);
		
		if(loaded) {
			if(isJoined)
				inflater.inflate(R.menu.group_leave, menu);
			inflater.inflate(R.menu.group_join, menu);
		}
	}
	
	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		
		ThreadListFragment f = (ThreadListFragment) ((MainActivity)getActivity()).getParentFragment();
		String key = f.getKey();
		
		switch (item.getItemId()) {
		
		
		case R.id.del: {
			AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
			ProgressBar bar = new ProgressBar(getActivity(), null,
					android.R.attr.progressBarStyleHorizontal);
			bar.setIndeterminate(true);
			builder.setCancelable(false).setTitle("Sending request to server")
					.setView(bar);
			final AlertDialog dialog = builder.show();

			ApiManager.leaveGroup(key, group.getKey(), new ApiHandler<ApiResponseBase>() {

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
			
		case R.id.add: {
            if (!isJoined) {
                AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
                ProgressBar bar = new ProgressBar(getActivity(), null,
                        android.R.attr.progressBarStyleHorizontal);
                bar.setIndeterminate(true);
                builder.setCancelable(false).setTitle("Sending request to server")
                        .setView(bar);
                final AlertDialog dialog = builder.show();

                ApiManager.joinGroup(group.getKey(), new ApiHandler<ApiResponseBase>() {

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
            } else {
                MainActivity main = (MainActivity) getActivity();
                UserAddFragment fragment = new UserAddFragment();
                fragment.setCode(info, group.getKey());
                main.gotoFragment(0, fragment);
            }
		}
			return true;
			
		default:
			return super.onOptionsItemSelected(item);
		}
	}

}
