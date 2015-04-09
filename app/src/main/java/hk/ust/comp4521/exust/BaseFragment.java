package hk.ust.comp4521.exust;

import android.app.ActionBar;
import android.app.Activity;
import android.app.Fragment;
import android.app.ActionBar.OnNavigationListener;
import android.widget.ArrayAdapter;

public abstract class BaseFragment extends Fragment {
	public abstract String getTitle();
	public boolean onBackPressed() {
		return false;
	}
	
	BaseFragment[] fragments;	
	ActionBar bar;
	
	public void setNavigation(BaseFragment[] fragments) {
		this.fragments = fragments;
	}
	
	@Override
	public void onAttach(Activity activity) {
		super.onAttach(activity);
		if(fragments != null && fragments.length > 1) {
			bar = activity.getActionBar();
			bar.setDisplayShowTitleEnabled(false);
			bar.setNavigationMode(ActionBar.NAVIGATION_MODE_LIST);
			bar.setListNavigationCallbacks(new ArrayAdapter<BaseFragment>(activity, R.layout.view_navigation, fragments), new OnNavigationListener() {
				@Override
				public boolean onNavigationItemSelected(int itemPosition,
						long itemId) {
					if(fragments[itemPosition] != BaseFragment.this)
						((MainActivity)getActivity()).gotoFragment(fragments[itemPosition]);
					return true;
				}
			});
			for(int i = 0; i < fragments.length; i++)
				if(fragments[i] == this) {
					bar.setSelectedNavigationItem(i);
					break;
				}
		}
	}

	@Override
	public void onDetach() {
		if(bar != null) {
			bar.setDisplayShowTitleEnabled(true);
			bar.setNavigationMode(ActionBar.NAVIGATION_MODE_STANDARD);
			((MainActivity)getActivity()).updateTitle();
			bar = null;
		}
		super.onDetach();
	}
	@Override
	public String toString() {
		return getTitle();
	}
	
	
}
