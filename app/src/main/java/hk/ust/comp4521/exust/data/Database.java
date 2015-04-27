package hk.ust.comp4521.exust.data;

import android.content.Context;
import android.content.SharedPreferences;
import android.util.Log;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.HashMap;
import java.util.Map;

import hk.ust.comp4521.exust.json.ApiResponseData;

public class Database {
    public static final String TAG = "exust.Database";
	static Context context;
	static SharedPreferences pref;
	static SharedPreferences config;

	public static void init(Context context) {
		Database.context = context;
		pref = context.getSharedPreferences("db", Context.MODE_PRIVATE);
		config = context.getSharedPreferences("config", Context.MODE_PRIVATE);
	}

	public static <T extends DataType> void getData(final String type,
			final String key, final Class<? extends T> mClass,
			final DatabaseLoad<Map<String, T>> handler) {
		getData(type, key, new DatabaseLoad<JSONObject>() {

			@Override
			public void load(JSONObject obj) {
				Map<String, T> datas = new HashMap<String, T>();
				if (obj != null) {
					JSONArray names = obj.names();
					if (names != null) {
						for (int i = 0; i < names.length(); i++) {
							try {
								String key = names.get(i).toString();
								T item = mClass.newInstance();
								item.load(obj.getJSONObject(key));
								datas.put(key, item);
							} catch (JSONException e) {
								throw new RuntimeException(e);
							} catch (InstantiationException e) {
								e.printStackTrace();
							} catch (IllegalAccessException e) {
								e.printStackTrace();
							}
						}
					}
				}
				if (handler != null)
					handler.load(datas);
			}

		});
	}

	public static <T extends DataType> void getDataSingle(final String type,
			final String key, final Class<? extends T> mClass,
			final DatabaseLoad<T> handler) {
		getData(type, key, new DatabaseLoad<JSONObject>() {

			@Override
			public void load(JSONObject obj) {
				T item = null;
				if (obj != null) {
					try {
						item = mClass.newInstance();
						item.load(obj);
					} catch (JSONException e) {
						throw new RuntimeException(e);
					} catch (InstantiationException e) {
						e.printStackTrace();
					} catch (IllegalAccessException e) {
						e.printStackTrace();
					}
				}
				if (handler != null)
					handler.load(item);
			}

		});
	}

	public static void getData(final String type, final String key,
			final DatabaseLoad<JSONObject> handler) {
		final String fullName = type + (key == null ? "" : "_" + key);
		String hash = pref.getString(fullName, null);
		final File file = context.getFileStreamPath(fullName + ".json");
		if (!file.exists() || file.length() == 0)
			hash = null;
        Log.i(TAG, "Database.getData: " + type + " /" + key + " /"+ hash);
		ApiManager.datas(type, key, hash, new ApiHandler<ApiResponseData>() {
            @Override
            public void onSuccess(ApiResponseData mdatas) {
                if (mdatas == null) {
                    Toast.makeText(
                            context,
                            "Cannot get " + type + " data"
                                    + (key == null ? "" : " of " + key),
                            Toast.LENGTH_LONG).show();
                }
                JSONObject obj = null;
                if (mdatas == null && file.exists() || mdatas != null
                        && mdatas.getData() == null) {
                    obj = loadJSON(file);
                } else if (mdatas != null) {
                    obj = mdatas.getData();
                    if (saveJSON(file, obj)) {
                        pref.edit().putString(fullName, mdatas.getHash()).apply();
                    }
                }
                if (handler != null) {
                    handler.load(obj);
                }
            }

            @Override
            public void onFailure(String message) {
                onSuccess(null);
            }
        });
	}

	public static JSONObject loadJSON(File file) {
		try {
			InputStream is = context.openFileInput(file.getName());
			int size = is.available();
			byte[] buffer = new byte[size];
			is.read(buffer);
			is.close();
			return new JSONObject(new String(buffer, "UTF-8"));
		} catch (IOException ex) {
			ex.printStackTrace();
			return null;
		} catch (JSONException e) {
			e.printStackTrace();
			return null;
		}
	}

	public static boolean saveJSON(File file, JSONObject obj) {
		try {
			FileOutputStream is = context.openFileOutput(file.getName(),
					Context.MODE_PRIVATE);
			byte[] buffer = obj.toString().getBytes("UTF-8");
			is.write(buffer);
			is.close();
			return true;
		} catch (IOException ex) {
			ex.printStackTrace();
			return false;
		}
	}

	static UserData user;

	public static UserData getUser() {
		if (user == null) {
			String userData = config.getString("user", null);
			user = new UserData();
			user.load(userData);
		}
		return user;
	}

	public static void commitUser() {
		config.edit().putString("user", getUser().save()).apply();
	}

	public static boolean getFirstRun() {
		return config.getBoolean("firstRun", true);
}

	public static void setFirstRun(boolean firstRun) {
		config.edit().putBoolean("firstRun", firstRun).apply();
	}
}
