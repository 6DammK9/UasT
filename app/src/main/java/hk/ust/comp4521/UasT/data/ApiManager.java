/**
 * COMP4521 Project
 *      Cheung Wai Yip
 *      Lau Tsz Hei
 *      Ho Kam Ming
 */

package hk.ust.comp4521.UasT.data;

import android.os.Environment;
import android.util.Log;

import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.FileAsyncHttpResponseHandler;
import com.loopj.android.http.JsonHttpResponseHandler;
import com.loopj.android.http.RequestParams;

import org.apache.http.entity.StringEntity;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.UnsupportedEncodingException;

import hk.ust.comp4521.UasT.json.ApiResponseAddChat;
import hk.ust.comp4521.UasT.json.ApiResponseBase;
import hk.ust.comp4521.UasT.json.ApiResponseData;
import hk.ust.comp4521.UasT.json.ApiResponseIMG;
import hk.ust.comp4521.UasT.json.ApiResponseLike;
import hk.ust.comp4521.UasT.json.ApiResponseValidate;

public class ApiManager {

    private static final String TAG = "UasT.data";
    public static String API_HOST = "http://127.0.0.1:5001/api/";
    //public static final String RES_HOST = "http://127.0.0.1:5001/res/";

    private static final AsyncHttpClient client = new AsyncHttpClient();
    //static HttpClient clinetSync = new DefaultHttpClient();

    //Api(obj, handler, ApiResponseBase.class, 1);
    private static <T extends ApiResponseBase> void Api(
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

    private static <T extends ApiResponseBase> void ApiUploadFile(
            final File request, final ApiHandler<T> handler,
            final Class<T> responseClass, final int retryCount) {

        try {
            //Log.i(TAG, "Transferring file: " + request.getPath());

            FileInputStream fs = new FileInputStream(request);

            final String URL = API_HOST.substring(0, API_HOST.length() - 4) + "upload/";
            Log.i(TAG, URL);
            RequestParams params = new RequestParams();
            //params.put("IMG", fs, "image/jpeg", "TEST.jpg");
            //Assume request is in proper path and format
            String type = request.getPath().substring(request.getPath().lastIndexOf(".") + 1);
            params.put("IMG", fs, "image/" + type, "." + type);

            //client.post(context, url, param, handler)
            client.setTimeout(5000);
            client.post(null, URL, params, new JsonHttpResponseHandler() {
                @Override
                public void onSuccess(JSONObject obj) {
                    Log.i(TAG, "Api request done");
                    try {
                        if (handler != null) {
                            ApiResponseIMG img = new ApiResponseIMG();
                            img.load(obj);
                            if (img.getCode() < 0) {
                                handler.onFailure(img.getMessage());
                            } else {
                                T response = responseClass.newInstance();
                                response.load(obj);
                                handler.onSuccess(response);
                            }
                        }
                    } catch (JSONException exc) {
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
                        ApiUploadFile(request, handler, responseClass, retryCount - 1);
                    else {
                        if (handler != null)
                            handler.onFailure(exc.toString());
                    }
                }
            });
            Log.i(TAG, "Transferring file: " + request.getPath());
        } catch (FileNotFoundException exc) {
            Log.i(TAG, "ERROR: FileNotFoundException" + request.getPath());
            handler.onFailure(exc.toString());
        }
    }

    private static <T extends ApiResponseBase> void ApiDownloadFile(
            final JSONObject request, final ApiHandler<T> handler,
            final Class<T> responseClass, final int retryCount) {

        StringEntity entity;
        try {
            entity = new StringEntity(request.toString(), "utf-8");
        } catch (UnsupportedEncodingException e) {
            throw new RuntimeException(e);
        }

        Log.i(TAG, "Sending api request: " + request.toString());
        File mediaStorageDir = new File(Environment.getExternalStoragePublicDirectory(
                Environment.DIRECTORY_PICTURES), "USTasUST");

        File tmp_file = new File(mediaStorageDir.getPath() + File.separator + "TEMP.jpg");

        client.setTimeout(5000);
        client.post(null, API_HOST, entity, "application/json",
                new FileAsyncHttpResponseHandler(tmp_file) {
                    @Override
                    public void onSuccess(int statusCode, org.apache.http.Header[] headers, byte[] responseBytes) {
                        Log.i(TAG, "Api request done");
                        try {
                            if (statusCode != 200) {
                                handler.onFailure("Fail with code: " + statusCode);
                            } else {
                                //Create "Success" JSON handle
                                String fileName = "Download success!";
                                for (org.apache.http.Header header : headers) {
                                    if (header.getName().equals("File-name")) {
                                        fileName = header.getValue();
                                    }
                                }
                                JSONObject success = new JSONObject();
                                success.put("code", 0);
                                success.put("msg", fileName);
                                T response = responseClass.newInstance();
                                response.load(success);
                                handler.onSuccess(response);
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
                    public void onFailure(int statusCode, org.apache.http.Header[] headers, byte[] responseBytes, java.lang.Throwable throwable) {
                        Log.i(TAG, "Api failed, retrying...");
                        if (retryCount > 0)
                            ApiDownloadFile(request, handler, responseClass, retryCount - 1);
                        else {
                            if (handler != null)
                                handler.onFailure(throwable.toString());
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
                            String details, String content, int rating, String attachment,
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
            obj.put("img", attachment);
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

    public static void kickGroup(String pkey, String key, String ITSC, String Name, ApiHandler<ApiResponseBase> handler) {
        JSONObject obj = new JSONObject();
        try {
            obj.put("cmd", "leaveGroup");
            obj.put("key", key);
            obj.put("pkey", pkey);
            obj.put("user", ITSC);
            obj.put("name", Name);

        } catch (JSONException e) {
            throw new RuntimeException(e);
        }

        Api(obj, handler, ApiResponseBase.class, 1);
    }

    public static void leaveGroup(String pkey, String key,
                                  ApiHandler<ApiResponseBase> handler) {
        kickGroup(pkey, key, Database.getUser().getITSC(), Database.getUser().getName(), handler);
    }

    //public static void leaveGroup(String user, String name, String pkey, String key, ApiHandler<ApiResponseBase>)

    public static void joinGroupSuper(String key, String user, String name,
                                      ApiHandler<ApiResponseBase> handler) {
        JSONObject obj = new JSONObject();
        try {
            obj.put("cmd", "joinGroup");
            obj.put("post", key);
            obj.put("user", user);
            obj.put("name", name);
        } catch (JSONException e) {
            throw new RuntimeException(e);
        }

        Api(obj, handler, ApiResponseBase.class, 1);
    }

    public static void joinGroup(String key,
                                 ApiHandler<ApiResponseBase> handler) {
        joinGroupSuper(key, Database.getUser().getITSC(), Database.getUser().getName(), handler);
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

    public static void likeSharing(String courseId, String postId, String ITSC, ApiHandler<ApiResponseLike> handler) {
        JSONObject obj = new JSONObject();
        try {
            obj.put("cmd", "likeSharing");
            obj.put("key", postId);
            obj.put("pkey", courseId);
            obj.put("user", ITSC);
        } catch (JSONException e) {
            throw new RuntimeException(e);
        }

        Api(obj, handler, ApiResponseLike.class, 1);
    }

    public static void likeTrading(String courseId, String postId, String ITSC, ApiHandler<ApiResponseLike> handler) {
        JSONObject obj = new JSONObject();
        try {
            obj.put("cmd", "likeTrading");
            obj.put("key", postId);
            obj.put("pkey", courseId);
            obj.put("user", ITSC);
        } catch (JSONException e) {
            throw new RuntimeException(e);
        }

        Api(obj, handler, ApiResponseLike.class, 1);
    }

    public static void addFriend(String key, ApiHandler<ApiResponseBase> handler) {
        JSONObject obj = new JSONObject();
        try {
            obj.put("cmd", "addFriend");
            obj.put("key", key);
            obj.put("user", Database.getUser().getITSC());
        } catch (JSONException e) {
            throw new RuntimeException(e);
        }

        Api(obj, handler, ApiResponseBase.class, 1);
    }

    public static void upCalEvents(String[] CalEventArr, ApiHandler<ApiResponseBase> handler) {
        JSONObject obj = new JSONObject();
        try {
            obj.put("cmd", "upCalEvents");
            obj.put("user", Database.getUser().getITSC());
            obj.put("name", Database.getUser().getName());

            JSONArray _CalEventArr = new JSONArray();
            for (String CalEventStr : CalEventArr) {
                _CalEventArr.put(CalEventStr);
            }

            obj.put("CalEventArr", _CalEventArr);
        } catch (JSONException e) {
            throw new RuntimeException(e);
        }

        Api(obj, handler, ApiResponseBase.class, 1);
    }

    public static void upIMG(File file, ApiHandler<ApiResponseIMG> handler) {
        //Upload imgae file to server
        ApiUploadFile(file, handler, ApiResponseIMG.class, 1);
    }

    public static void sendIMG(String key, String img, String msg, ApiHandler<ApiResponseBase> handler) {
        JSONObject obj = new JSONObject();
        try {
            obj.put("cmd", "sendIMG");
            obj.put("key", key);
            obj.put("user", Database.getUser().getITSC());
            obj.put("name", Database.getUser().getName());
            obj.put("img", img);
            obj.put("msg", msg);
        } catch (JSONException e) {
            throw new RuntimeException(e);
        }

        Api(obj, handler, ApiResponseBase.class, 1);
    }

    public static void downIMG(String matchID, String authorID, ApiHandler<ApiResponseBase> handler) {
        JSONObject obj = new JSONObject();
        try {
            obj.put("cmd", "downIMG");
            obj.put("matchID", matchID);
            obj.put("user", authorID);
        } catch (JSONException e) {
            throw new RuntimeException(e);
        }

        ApiDownloadFile(obj, handler, ApiResponseBase.class, 1);
    }
}
