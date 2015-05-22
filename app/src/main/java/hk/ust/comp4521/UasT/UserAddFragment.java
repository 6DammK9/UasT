package hk.ust.comp4521.UasT;

import android.app.AlertDialog;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.Iterator;

import hk.ust.comp4521.UasT.data.ApiHandler;
import hk.ust.comp4521.UasT.data.ApiManager;
import hk.ust.comp4521.UasT.data.Database;
import hk.ust.comp4521.UasT.data.ThreadItemInfo;
import hk.ust.comp4521.UasT.json.ApiResponseAddChat;
import hk.ust.comp4521.UasT.json.ApiResponseBase;
import hk.ust.comp4521.UasT.json.ApiResponseData;

public class UserAddFragment extends BaseFragment {

	ThreadItemInfo info;
	String code;
	EditText destITSC;
    ListView list;
    ArrayList<String> FriendList;

	public UserAddFragment() {

	}

	public void setCode(ThreadItemInfo info, String code) {
		this.info = info;
		this.code = code;
	}

	@Override
	public void onCreate(Bundle savedInstanceState) {
		setHasOptionsMenu(true);
		super.onCreate(savedInstanceState);
	}

    @Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		View view = inflater.inflate(R.layout.fragment_user_add, null);
        destITSC = (EditText) view.findViewById(R.id.title);
        list = (ListView) view.findViewById(R.id.list);
        FriendList = new ArrayList<String>();

        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        ProgressBar bar = new ProgressBar(getActivity(), null, android.R.attr.progressBarStyleHorizontal);
        bar.setIndeterminate(true);
        builder.setCancelable(true).setTitle("Sending request to server").setView(bar);
        final AlertDialog dialog = builder.show();

        ApiManager.datas("usersFriends", Database.getUser().getITSC(), null,
                     new ApiHandler<ApiResponseData>() {

                    @Override
                    public void onSuccess(ApiResponseData response) {
                        dialog.dismiss();

                        Iterator<?> keys = response.getData().keys();

                        while( keys.hasNext() ) {
                            String key = (String)keys.next();
                            FriendList.add(key);
                        }

                        list.setAdapter(new ArrayAdapter<String>(getActivity(), R.layout.list_friend, FriendList));
                        list.setTextFilterEnabled(true);

                        list.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                            public void onItemClick(AdapterView<?> parent, View view,
                                                    int position, long id) {
                                destITSC.setText(((TextView) view).getText());
                            }
                        });
                    }

                    @Override
                    public void onFailure(String message) {
                        dialog.dismiss();
                        Toast.makeText(getActivity(), message,
                                Toast.LENGTH_LONG).show();
                    }

                });
        super.onCreate(savedInstanceState);
		return view;
	}

	@Override
	public String getTitle() {
		return "New " + info.typeName();
	}

	@Override
	public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
		inflater.inflate(R.menu.post, menu);
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		switch (item.getItemId()) {
		case R.id.confirm: {
            if (info.typeName().equals("Chat")) {
                AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
                ProgressBar bar = new ProgressBar(getActivity(), null,
                        android.R.attr.progressBarStyleHorizontal);
                bar.setIndeterminate(true);
                builder.setCancelable(true).setTitle("Sending request to server")
                        .setView(bar);
                final AlertDialog dialog = builder.show();

                ApiManager.addChat(this.destITSC.getText().toString(),
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
            } else if (info.typeName().equals("Group")) {
                AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
                ProgressBar bar = new ProgressBar(getActivity(), null,
                        android.R.attr.progressBarStyleHorizontal);
                bar.setIndeterminate(true);
                builder.setCancelable(true).setTitle("Sending request to server")
                        .setView(bar);
                final AlertDialog dialog = builder.show();

                ApiManager.joinGroupSuper(this.code, this.destITSC.getText().toString(), this.destITSC.getText().toString(),
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
		}
			return true;
		default:
			return super.onOptionsItemSelected(item);
		}
	}
}
