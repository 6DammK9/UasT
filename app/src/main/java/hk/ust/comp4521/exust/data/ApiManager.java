package hk.ust.comp4521.exust.data;

import android.util.Log;

import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.JsonHttpResponseHandler;

import org.apache.http.client.HttpClient;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.DefaultHttpClient;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.UnsupportedEncodingException;

import hk.ust.comp4521.exust.json.ApiResponseAddChat;
import hk.ust.comp4521.exust.json.ApiResponseBase;
import hk.ust.comp4521.exust.json.ApiResponseData;
import hk.ust.comp4521.exust.json.ApiResponseValidate;

public class ApiManager {

	public static final String TAG = "exust.data";
	public static final String API_HOST = "http://192.168.1.23:5001/api";
    //public static final String API_HOST = "http://143.89.225.94:5001/api";
	public static final String RES_HOST = "http://127.0.0.1:5001/res/";

	static AsyncHttpClient client = new AsyncHttpClient();
	static HttpClient clinetSync = new DefaultHttpClient();

	public static <T extends ApiResponseBase> void Api(
			final JSONObject request, final ApiHandler<T> handler,
			final Class<T> responseClass, final int retryCount) {
		StringEntity entity;
		try {
			entity = new StringEntity(request.toString(), "utf-8");
		} catch (UnsupportedEncodingException e) {
			throw new RuntimeException(e);
		}

		Log.i(TAG, "Sending api request: " + request.toString());

		client.setTimeout(5000);
		client.post(null, API_HOST, entity, "application/json",
				new JsonHttpResponseHandler() {
                        @Override
                        public void onSuccess(JSONObject obj) {
                            Log.i(TAG, "Api request done");
                            try {
                                if (handler != null) {
                                    ApiResponseBase base = new ApiResponseBase();
                                    base.load(obj);
                                    if (base.getCode() < 0) {
                                        handler.onFailure(base.getMessage());
                                    } else {
                                        T response = responseClass.newInstance();
                                        response.load(obj);
                                        handler.onSuccess(response);
                                    }
                                }
                            } catch (JSONException exc) {
                                if (handler != null)
                                    handler.onFailure("Invalid response: "
                                            + exc.toString());
                            } catch (InstantiationException e) {
                                throw new RuntimeException("Cannot create "
                                        + responseClass.getName());
                            } catch (IllegalAccessException e) {
                                throw new RuntimeException("Cannot create "
                                        + responseClass.getName());
                            }
                        }

                        @Override
                        public void onFailure(Throwable exc, JSONObject obj) {
						Log.i(TAG, "Api failed, retrying...");
						if (retryCount > 0)
							Api(request, handler, responseClass, retryCount - 1);
						else {
							if (handler != null)
								handler.onFailure(exc.toString());
						}
					}
				});
	}

	public static void datas(String type, String key, String hash,
			ApiHandler<ApiResponseData> handler) {
		JSONObject obj = new JSONObject();
		try {
			obj.put("cmd", type);
			if (key != null)
				obj.put("key", key);
			if (hash != null)
				obj.put("hash", hash);
		} catch (JSONException e) {
			throw new RuntimeException(e);
		}

		Api(obj, handler, ApiResponseData.class, 1);
	}

	public static void login(String email, ApiHandler<ApiResponseBase> handler) {
		JSONObject obj = new JSONObject();
		try {
			obj.put("cmd", "login");
			obj.put("email", email);
		} catch (JSONException e) {
			throw new RuntimeException(e);
		}

		Api(obj, handler, ApiResponseBase.class, 1);
	}

	public static void validate(String email, String code, String gcm,
			ApiHandler<ApiResponseValidate> handler) {
		JSONObject obj = new JSONObject();
		try {
			obj.put("cmd", "validate");
			obj.put("gcm", gcm);
			obj.put("email", email);
			obj.put("code", code);
		} catch (JSONException e) {
			throw new RuntimeException(e);
		}

		Api(obj, handler, ApiResponseValidate.class, 1);
	}

	public static void post(String type, String key, String title,
			String details, String content, int rating,
			ApiHandler<ApiResponseBase> handler) {
		JSONObject obj = new JSONObject();
		try {
			obj.put("cmd", "addPost");
			obj.put("user", Database.getUser().getITSC());
			obj.put("name", Database.getUser().getName());
			obj.put("type", type);
			obj.put("key", key);
			obj.put("title", title);
			obj.put("details", details);
			obj.put("rating", rating);
			obj.put("content", content);
		} catch (JSONException e) {
			throw new RuntimeException(e);
		}

		Api(obj, handler, ApiResponseBase.class, 1);
	}

	public static void send(String key, String message,
			ApiHandler<ApiResponseBase> handler) {

		JSONObject obj = new JSONObject();
		try {
			obj.put("cmd", "sendChat");
			obj.put("user", Database.getUser().getITSC());
			obj.put("name", Database.getUser().getName());
			obj.put("key", key);
			obj.put("content", message);
		} catch (JSONException e) {
			throw new RuntimeException(e);
		}

		Api(obj, handler, ApiResponseBase.class, 1);
	}

	public static void updateId(String gcm, ApiHandler<ApiResponseBase> handler) {
		JSONObject obj = new JSONObject();
		try {
			obj.put("cmd", "updateId");
			obj.put("user", Database.getUser().getITSC());
			obj.put("name", Database.getUser().getName());
			obj.put("gcm", gcm);
		} catch (JSONException e) {
			throw new RuntimeException(e);
		}

		Api(obj, handler, ApiResponseBase.class, 1);
	}

	public static void delPost(String type, String pkey, String key,
			ApiHandler<ApiResponseBase> handler) {
		
		JSONObject obj = new JSONObject();
		try {
			obj.put("cmd", "delPost");
			obj.put("type", type);
			obj.put("key", key);
			obj.put("pkey", pkey);
			
		} catch (JSONException e) {
			throw new RuntimeException(e);
		}

		Api(obj, handler, ApiResponseBase.class, 1);
	}

	public static void leaveGroup(String pkey, String key,
			ApiHandler<ApiResponseBase> handler) {
		JSONObject obj = new JSONObject();
		try {
			obj.put("cmd", "leaveGroup");
			obj.put("key", key);
			obj.put("pkey", pkey);
			obj.put("user", Database.getUser().getITSC());
			obj.put("name", Database.getUser().getName());
		} catch (JSONException e) {
			throw new RuntimeException(e);
		}

		Api(obj, handler, ApiResponseBase.class, 1);
	}

	public static void joinGroup(String key,
			ApiHandler<ApiResponseBase> handler) {
		JSONObject obj = new JSONObject();
		try {
			obj.put("cmd", "joinGroup");
			obj.put("post", key);
			obj.put("user", Database.getUser().getITSC());
			obj.put("name", Database.getUser().getName());
		} catch (JSONException e) {
			throw new RuntimeException(e);
		}

		Api(obj, handler, ApiResponseBase.class, 1);
	}

	public static void addChat(String authorId,
			ApiHandler<ApiResponseAddChat> handler) {
		JSONObject obj = new JSONObject();
		try {
			obj.put("cmd", "addChat");
			obj.put("dest", authorId);
			obj.put("user", Database.getUser().getITSC());
			obj.put("name", Database.getUser().getName());
		} catch (JSONException e) {
			throw new RuntimeException(e);
		}

		Api(obj, handler, ApiResponseAddChat.class, 1);
	}

	public static void delChat(String key,
			ApiHandler<ApiResponseBase> handler) {
		JSONObject obj = new JSONObject();
		try {
			obj.put("cmd", "delChat");
			obj.put("key", key);
			obj.put("user", Database.getUser().getITSC());
			obj.put("name", Database.getUser().getName());
		} catch (JSONException e) {
			throw new RuntimeException(e);
		}

		Api(obj, handler, ApiResponseBase.class, 1);
	}

	public static void match0(String key, boolean[] avail,
			ApiHandler<ApiResponseBase> handler) {
		JSONObject obj = new JSONObject();
		try {
			obj.put("cmd", "match");
			obj.put("key", key);
			obj.put("user", Database.getUser().getITSC());
			obj.put("name", Database.getUser().getName());
			
			JSONArray _avail = new JSONArray();
			for(int i = 0; i < avail.length; i++)
				_avail.put(avail[i]);
			
			obj.put("avail", _avail);
		} catch (JSONException e) {
			throw new RuntimeException(e);
		}

		Api(obj, handler, ApiResponseBase.class, 1);
	}

	public static void joinMatch0(String matchId, boolean[] avail,
			ApiHandler<ApiResponseBase> handler) {
		JSONObject obj = new JSONObject();
		try {
			obj.put("cmd", "joinMatch");
			obj.put("matchId", matchId);
			obj.put("user", Database.getUser().getITSC());
			obj.put("name", Database.getUser().getName());
			
			JSONArray _avail = new JSONArray();
			for(int i = 0; i < avail.length; i++)
				_avail.put(avail[i]);
			
			obj.put("avail", _avail);
		} catch (JSONException e) {
			throw new RuntimeException(e);
		}

		Api(obj, handler, ApiResponseBase.class, 1);
		
	}

    public static void match2(String key, String[] CalStart, String[] CalEnd,
                             ApiHandler<ApiResponseBase> handler) {
        JSONObject obj = new JSONObject();
        try {
            obj.put("cmd", "match2");
            obj.put("key", key);
            obj.put("user", Database.getUser().getITSC());
            obj.put("name", Database.getUser().getName());

            JSONArray _CalStart = new JSONArray();
            JSONArray _CalEnd = new JSONArray();
            for(int i = 0; i < CalStart.length; i++) {
                _CalStart.put(CalStart[i]);
                _CalEnd.put(CalEnd[i]);
            }

            obj.put("calStart", _CalStart);
            obj.put("calEnd", _CalEnd);
        } catch (JSONException e) {
            throw new RuntimeException(e);
        }

        Api(obj, handler, ApiResponseBase.class, 1);
    }

    public static void joinMatch2(String matchId, String[] CalStart, String[] CalEnd,
                                 ApiHandler<ApiResponseBase> handler) {
        JSONObject obj = new JSONObject();
        try {
            obj.put("cmd", "joinMatch2");
            obj.put("matchId", matchId);
            obj.put("user", Database.getUser().getITSC());
            obj.put("name", Database.getUser().getName());

            JSONArray _CalStart = new JSONArray();
            JSONArray _CalEnd = new JSONArray();
            for(int i = 0; i < CalStart.length; i++) {
                _CalStart.put(CalStart[i]);
                _CalEnd.put(CalEnd[i]);
            }

            obj.put("calStart", _CalStart);
            obj.put("calEnd", _CalEnd);
        } catch (JSONException e) {
            throw new RuntimeException(e);
        }

        Api(obj, handler, ApiResponseBase.class, 1);

    }
}
