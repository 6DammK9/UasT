package hk.ust.comp4521.UasT;

import android.app.Activity;
import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.content.SharedPreferences;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager.NameNotFoundException;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.IBinder;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.RadioGroup;
import android.widget.Toast;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.GooglePlayServicesUtil;
import com.google.android.gms.gcm.GoogleCloudMessaging;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Stack;

import hk.ust.comp4521.UasT.data.ApiHandler;
import hk.ust.comp4521.UasT.data.ApiManager;
import hk.ust.comp4521.UasT.data.Chat;
import hk.ust.comp4521.UasT.data.Course;
import hk.ust.comp4521.UasT.data.Database;
import hk.ust.comp4521.UasT.json.ApiResponseBase;

public class MainActivity extends Activity {
    static final String TAG = "UasT.MainActivity";
    //static final String SENDER_ID = "479417287507";
    static final String SENDER_ID = "254106385173";
    private final static int PLAY_SERVICES_RESOLUTION_REQUEST = 9000;
    public static final String PROPERTY_REG_ID = "registration_id";
    private static final String PROPERTY_APP_VERSION = "appVersion";

    FragmentManager fragmentManager;
    RadioGroup tab;
    int curTab;
    Context context;
    GoogleCloudMessaging gcm;
    static String regid;
    PlayReceiver ReminderService;
    private GcmIntentService mBoundService;
    boolean mIsBound;
    boolean showError = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Database.init(this);

        initViews();

        fragmentManager = getFragmentManager();
        tab = (RadioGroup) findViewById(R.id.tab);
        tab.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                curTab = getTabIdFromId(checkedId);
                updateFragment();
            }
        });

        tab.check(R.id.course);

        context = this.getApplicationContext();
        if (checkPlayServices()) {
            gcm = GoogleCloudMessaging.getInstance(this);
            regid = getRegistrationId();

            if (regid.isEmpty()) {
                registerInBackground();
            } else {
                uploadID();
            }
        } else {
            Log.i(TAG, "No valid Google Play Services APK found.");
        }

        doBindService();

        ApiManager.API_HOST = Database.getAPIaddress();

        if (Database.getFirstRun()) {
            Database.setFirstRun(false);
            showLogin();
        }
    }

    private void showLogin() {
        Intent startActivity = new Intent(this, LoginActivity.class);
        this.startActivityForResult(startActivity, 100);
    }

    private void initViews() {
        stacks.clear();
        stacks.add(new Stack<BaseFragment>());
        stacks.add(new Stack<BaseFragment>());
        stacks.add(new Stack<BaseFragment>());

        ThreadListFragment courseList = new ThreadListFragment();
        courseList.setParams(null, Course.class);

        ChatListFragment chatList = new ChatListFragment();
        chatList.setParams2(Database.getUser().getITSC(), Chat.class);

        CalendarFragment calendar = new CalendarFragment();
        calendar.initialize();
        calendar.setEvents2(Database.getUser().getCalendar2());

        stacks.get(0).push(courseList);
        stacks.get(1).push(calendar);
        stacks.get(2).push(chatList);
    }

    @Override
    protected void onDestroy() {
        this.doUnbindService();
        super.onDestroy();
    }

    @Override
    protected void onNewIntent(Intent intent) {
        Bundle extras = intent.getExtras();
        if ("chat".equals(extras.getString("type"))) {
            if (mBoundService != null)
                mBoundService.clearNotifications();
        }
        super.onNewIntent(intent);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (resultCode == 100) {
            if (data.getBooleanExtra("result", false)) {
                initViews();
                updateFragment();
            }
        }
        super.onActivityResult(requestCode, resultCode, data);
    }

    private int getTabIdFromId(int id) {
        switch (id) {
            case R.id.course:
                return 0;
            case R.id.calendar:
                return 1;
            case R.id.chat:
            default:
                return 2;
        }
    }

    final List<Stack<BaseFragment>> stacks = new ArrayList<Stack<BaseFragment>>();

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.login:
                showLogin();
                return true;
        }
        return super.onOptionsItemSelected(item);
    }

    public void gotoFragment(int i, BaseFragment fragment) {
        if (i == -1)
            i = curTab;
        stacks.get(i).push(fragment);
        if (curTab == i) {
            updateFragment();
        } else {
            switch (i) {
                case 0:
                    tab.check(R.id.course);
                    break;
                case 1:
                    tab.check(R.id.calendar);
                    break;
                case 2:
                    tab.check(R.id.chat);
                    break;
            }
        }
    }

    public void gotoFragment(BaseFragment fragment) {
        stacks.get(curTab).pop();
        gotoFragment(-1, fragment);
    }

    public BaseFragment getParentFragment() {
        BaseFragment f = stacks.get(curTab).pop();
        BaseFragment r = stacks.get(curTab).peek();
        stacks.get(curTab).push(f);
        return r;
    }

    private void updateFragment() {
        InputMethodManager inputManager = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
        View focusView = getCurrentFocus();
        if (focusView != null) {
            inputManager.hideSoftInputFromWindow(focusView.getWindowToken(),
                    InputMethodManager.HIDE_NOT_ALWAYS);
        }

        FragmentTransaction transaction = fragmentManager.beginTransaction();
        BaseFragment fragment = stacks.get(curTab).peek();
        transaction.replace(R.id.content, fragment, fragment.getTitle());
        transaction.setTransition(FragmentTransaction.TRANSIT_NONE);
        transaction.commit();
        setTitle(fragment.getTitle());
    }

    public void updateMenu() {
        fragmentManager.invalidateOptionsMenu();
    }

    @Override
    public void onBackPressed() {
        if (!stacks.get(curTab).peek().onBackPressed()) {
            if (stacks.get(curTab).size() > 1) {
                stacks.get(curTab).pop();
                updateFragment();
            } else
                super.onBackPressed();
        }
    }

    public void popFragment() {
        onBackPressed();
    }

    public void popFragment(int idx, int max) {
        while (stacks.get(idx).size() > max)
            stacks.get(idx).pop();
    }

    public void updateTitle() {
        setTitle(stacks.get(curTab).peek().getTitle());
    }

    private boolean checkPlayServices() {
        int resultCode = GooglePlayServicesUtil
                .isGooglePlayServicesAvailable(this);
        if (resultCode != ConnectionResult.SUCCESS) {
            if (GooglePlayServicesUtil.isUserRecoverableError(resultCode)) {
                if (!showError) {
                    GooglePlayServicesUtil.getErrorDialog(resultCode, this,
                            PLAY_SERVICES_RESOLUTION_REQUEST).show();
                    showError = true;
                }
                return false;
            } else {
                Log.i(TAG, "This device is not supported.");
            }
            return false;
        }
        return true;
    }

    @Override
    protected void onResume() {
        super.onResume();
        checkPlayServices();
        if (mBoundService != null)
            mBoundService.setCallback(this);
        if (ReminderService != null)
            ReminderService.setCallback(this);
    }


    @Override
    protected void onPause() {
        if (mBoundService != null)
            mBoundService.setCallback(null);
        if (ReminderService != null)
            ReminderService.setCallback(this);
        super.onPause();
    }

    private String getRegistrationId() {

        final SharedPreferences prefs = getGCMPreferences();
        String registrationId = prefs.getString(PROPERTY_REG_ID, "");
        if (registrationId != null) {
            if (registrationId.isEmpty()) {
                Log.i(TAG, "Registration not found.");
                return "";
            }
        } else {
            Log.i(TAG, "Registration not found.");
            return "";
        }

        int registeredVersion = prefs.getInt(PROPERTY_APP_VERSION,
                Integer.MIN_VALUE);
        int currentVersion = getAppVersion();
        if (registeredVersion != currentVersion) {
            Log.i(TAG, "App version changed.");
            return "";
        }
        return registrationId;
    }

    private SharedPreferences getGCMPreferences() {
        return getSharedPreferences(MainActivity.class.getSimpleName(),
                Context.MODE_PRIVATE);
    }

    private int getAppVersion() {
        try {
            PackageInfo packageInfo = context.getPackageManager()
                    .getPackageInfo(context.getPackageName(), 0);
            return packageInfo.versionCode;
        } catch (NameNotFoundException e) {
            throw new RuntimeException("Could not get package name: " + e);
        }
    }

    private void registerInBackground() {
        new AsyncTask<Void, Void, Void>() {
            @Override
            protected void onPostExecute(Void msg) {
            }

            @Override
            protected Void doInBackground(Void... params) {
                try {
                    if (gcm == null) {
                        gcm = GoogleCloudMessaging.getInstance(context);
                    }
                    regid = gcm.register(SENDER_ID);
                    Log.i(TAG, "Device registered, registration ID=" + regid);
                    storeRegistrationId(regid);
                    uploadID();
                } catch (IOException ex) {
                    Log.e(TAG, "Error :" + ex.getMessage());

                }
                return null;
            }
        }.execute(null, null, null);
    }

    private void uploadID() {
        if (Database.getUser().isLogon() && regid != null && !regid.isEmpty())
            ApiManager.updateId(regid, new ApiHandler<ApiResponseBase>() {

                @Override
                public void onSuccess(ApiResponseBase response) {
                    Toast.makeText(MainActivity.this, response.getMessage(),
                            Toast.LENGTH_LONG).show();
                }

                @Override
                public void onFailure(String message) {
                    Toast.makeText(MainActivity.this, message,
                            Toast.LENGTH_LONG).show();
                }

            });
    }

    private void storeRegistrationId(String regId) {
        final SharedPreferences prefs = getGCMPreferences();
        int appVersion = getAppVersion();
        Log.i(TAG, "Saving regId on app version " + appVersion);
        SharedPreferences.Editor editor = prefs.edit();
        editor.putString(PROPERTY_REG_ID, regId);
        editor.putInt(PROPERTY_APP_VERSION, appVersion);
        editor.apply();
    }

    private final ServiceConnection mConnection = new ServiceConnection() {
        public void onServiceConnected(ComponentName className, IBinder service) {
            mBoundService = ((GcmIntentService.LocalBinder) service)
                    .getService();
        }

        public void onServiceDisconnected(ComponentName className) {
            mBoundService = null;
        }
    };

    void doBindService() {
        bindService(new Intent(this, GcmIntentService.class), mConnection,
                Context.BIND_AUTO_CREATE);
        mIsBound = true;
    }

    void doUnbindService() {
        if (mIsBound) {
            unbindService(mConnection);
            mIsBound = false;
        }
    }

    public String getGCM() {
        return regid;
    }

    public void refreshChat(String chatId) {
        if (curTab == 2) {
            ThreadListFragment f = (ThreadListFragment) stacks.get(curTab).peek();
            if (f.getType().equals("chats"))
                f.getThread();
            else if (f.getType().equals(chatId))
                f.getThread();
        }
    }

}
