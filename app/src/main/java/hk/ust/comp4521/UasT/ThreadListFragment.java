/**
 * 	#COMP 4521
 *      #Cheung Wai Yip	20126604	wycheungae@connect.ust.hk
 *      #Lau Tsz Hei		20113451	thlauac@connect.ust.hk
 *      #Ho Kam Ming	20112316	kmhoab@connect.ust.hk
 */

package hk.ust.comp4521.UasT;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnFocusChangeListener;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.BaseAdapter;
import android.widget.ListView;
import android.widget.SearchView;
import android.widget.SearchView.OnQueryTextListener;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.Locale;
import java.util.Map;

import hk.ust.comp4521.UasT.data.Database;
import hk.ust.comp4521.UasT.data.DatabaseLoad;
import hk.ust.comp4521.UasT.data.ThreadItem;
import hk.ust.comp4521.UasT.data.ThreadItemInfo;

public class ThreadListFragment extends BaseFragment {

	protected ListView list;
	protected ArrayList<ThreadItem> threads, filteredThreads;
	protected String key;
	protected Class<? extends ThreadItem> mClass;
	protected ThreadItemInfo info;
	protected ThreadItemInfo pinfo;
	protected ThreadItem item;

	ThreadCardView header;
	SearchView searchView;
	MenuItem searchMenu;
	boolean isSearchExpanded = false;
	String filter;
	
	public String getKey() {
		return key;
	}
	
	public String getType() {
		return info.type();
	}

	public ThreadListFragment() {
	}

	public void setParams(ThreadItem item, Class<? extends ThreadItem> mClass) {
		this.item = item;
		if (item != null) {
			this.key = item.getKey();
			this.pinfo = item.getClass().getAnnotation(ThreadItemInfo.class);
		}
		this.mClass = mClass;
		info = mClass.getAnnotation(ThreadItemInfo.class);
	}
	
	public void setParams2(String key, Class<? extends ThreadItem> mClass) {
		this.key = key;
		this.mClass = mClass;
		info = mClass.getAnnotation(ThreadItemInfo.class);
	}

	@Override
	public void onCreate(Bundle savedInstanceState) {
		setHasOptionsMenu(true);
		super.onCreate(savedInstanceState);
	}

	protected int layout_id = R.layout.fragment_thread_list;
	
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		View view = inflater.inflate(layout_id, null);
		list = (ListView) view.findViewById(R.id.list);
		list.setOnItemClickListener(new OnItemClickListener() {
			@Override
			public void onItemClick(AdapterView<?> parent, View view,
					int position, long id) {
				if (id == -1)
					return;
				isSearchExpanded = false;
				if(searchMenu != null)
					searchMenu.collapseActionView();
				BaseFragment[] fragment = filteredThreads.get((int) id)
						.getFragment();
				if (fragment != null && fragment.length > 0) {
					for(BaseFragment f : fragment)
							f.setNavigation(fragment);
					((MainActivity) getActivity()).gotoFragment(-1, fragment[0]);
				}
			}
		});
		if (item != null && info.header() != -1) {
			header = (ThreadCardView) inflater.inflate(
					info.header(), null);
			updateHeader();
			list.addHeaderView(header);
		}
		getThread();
		return view;
	}

	protected void updateHeader() {
		header.setThread(item);
	}

	@Override
	public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
		inflater.inflate(R.menu.thread_list, menu);
		if (info.filter()) {
			inflater.inflate(R.menu.thread_list_filter, menu);
			searchMenu = menu.findItem(R.id.search);
			searchView = (SearchView) searchMenu.getActionView();
			if (info.hint() != null)
				searchView.setQueryHint(info.hint());
			searchView.setOnQueryTextListener(new OnQueryTextListener() {
				@Override
				public boolean onQueryTextSubmit(String query) {
					isSearchExpanded = false;
					searchMenu.collapseActionView();
					return false;
				}

				@Override
				public boolean onQueryTextChange(String newText) {
					if(!isSearchExpanded)
						return false;
					if (newText.trim().length() == 0)
						filter = null;
					else
						filter = newText.toLowerCase(Locale.US);
					update();
					return true;
				}		
			});

			searchView
					.setOnQueryTextFocusChangeListener(new OnFocusChangeListener() {
						@Override
						public void onFocusChange(View view,
								boolean queryTextFocused) {
							isSearchExpanded = queryTextFocused;
							if (queryTextFocused) {
								if(filter != null)
									searchView.setQuery(filter, false);
							} else {
								searchMenu.collapseActionView();
							}
						}
					});
		}
		if (info.add() && Database.getUser().isLogon())
			inflater.inflate(R.menu.thread_list_add, menu);
	}

	@Override
	public void onDestroyOptionsMenu() {
		if (searchView != null) {
			searchView.setOnClickListener(null);
			searchView.setOnQueryTextFocusChangeListener(null);
			searchView = null;
		}
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		switch (item.getItemId()) {
		case R.id.add: {
			MainActivity main = (MainActivity) getActivity();
			PostFragment fragment = new PostFragment();
			fragment.setCode(info, key);
			main.gotoFragment(0, fragment);
		}
			return true;
		default:
			return super.onOptionsItemSelected(item);
		}
	}

	protected void update() {
		if (threads == null)
			return;

		if (!info.filter() || filter == null) {
			filteredThreads = threads;
		} else {
			filteredThreads = new ArrayList<ThreadItem>();
			for (ThreadItem c : threads) {
				if (c.filter(filter))
					filteredThreads.add(c);
			}
		}

		list.setAdapter(new BaseAdapter() {
			@Override
			public int getCount() {
				return filteredThreads.size();
			}

			@Override
			public ThreadItem getItem(int position) {
				return filteredThreads.get(position);
			}

			@Override
			public long getItemId(int position) {
				return position;
			}

			@Override
			public View getView(int position, View convertView, ViewGroup parent) {
				ThreadCardView v = (ThreadCardView) convertView;
				if (v == null) {
					v = (ThreadCardView) LayoutInflater.from(getActivity())
							.inflate(info.layout(), parent, false);
				}

				ThreadItem f = getItem(position);
				v.setThread(f);
				return v;
			}
		});
	}

	public void getThread() {
		Database.getData(info.type(), key, mClass,
				new DatabaseLoad<Map<String, ThreadItem>>() {
					@Override
					public void load(Map<String, ThreadItem> obj) {
						if (obj == null)
							return;
						threads = new ArrayList<ThreadItem>();
						threads.addAll(obj.values());
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
					}
				});
	}

	@Override
	public boolean onBackPressed() {
		if(filter != null) {
			filter = null;
			update();
			return true;
		}
		return super.onBackPressed();
	}

	@Override
	public String getTitle() {
		return info.title();
	}
}
