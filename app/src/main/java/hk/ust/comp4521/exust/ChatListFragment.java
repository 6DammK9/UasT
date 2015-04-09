package hk.ust.comp4521.exust;

import android.view.MenuItem;

public class ChatListFragment extends ThreadListFragment {

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		switch (item.getItemId()) {
		case R.id.add: {
			
			return true;
		}
		}
		return super.onOptionsItemSelected(item);
	}

}
