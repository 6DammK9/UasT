package hk.ust.comp4521.exust;

import android.view.MenuItem;

public class ChatListFragment extends ThreadListFragment {

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		switch (item.getItemId()) {
		case R.id.add: {
            MainActivity main = (MainActivity) getActivity();
            UserAddFragment fragment = new UserAddFragment();
            fragment.setCode(info, key);
            main.gotoFragment(2, fragment);

			return true;
		}
		}
		return super.onOptionsItemSelected(item);
	}

}
