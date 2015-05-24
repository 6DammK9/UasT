/**
 * 	#COMP 4521
 *      #Cheung Wai Yip	20126604	wycheungae@connect.ust.hk
 *      #Lau Tsz Hei		20113451	thlauac@connect.ust.hk
 *      #Ho Kam Ming	20112316	kmhoab@connect.ust.hk
 */

package hk.ust.comp4521.UasT;

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
