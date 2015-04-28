package hk.ust.comp4521.exust;

import android.util.Log;
import android.view.MenuItem;

public class ChatListFragment extends ThreadListFragment {

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		switch (item.getItemId()) {
		case R.id.add: {
            // TODO Goto another listFragment Of searching user/ selecting friends
			Log.i("exust.ChatListFragment", "===> Option AddButton Clicked");



			return true;
		}
		}
		return super.onOptionsItemSelected(item);
	}

}
