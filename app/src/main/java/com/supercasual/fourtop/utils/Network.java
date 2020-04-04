package com.supercasual.fourtop.utils;

import android.content.Context;
import android.util.Log;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.request.SimpleMultiPartRequest;
import com.android.volley.request.StringRequest;
import com.android.volley.toolbox.Volley;
import com.supercasual.fourtop.model.CurrentUser;
import com.supercasual.fourtop.model.Image;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Network {

    private static final String TAG = "debug_logs";

    public static final int VOTE_FIRST = 0;
    public static final int VOTE_SECOND = 1;

    private static final String BASE_URL = "http://ntech.team:8082";

    private static final String REGISTER_REQUEST = BASE_URL + "/api/register";
    private static final String LOGIN_REQUEST = BASE_URL + "/api/login";
    private static final String LOGOUT_REQUEST = BASE_URL + "/api/logout";
    private static final String GALLERY_ADD_REQUEST = BASE_URL + "/api/gallery/add";
    private static final String GALLERY_REQUEST = BASE_URL + "/api/gallery";
    private static final String GALLERY_REMOVE_REQUEST = BASE_URL + "/api/gallery/remove";
    private static final String VOTE_CREATE_REQUEST = BASE_URL + "/api/vote/create";
    private static final String VOTE_REQUEST = BASE_URL + "/api/vote";
    private static final String TOP_PHOTOS_REQUEST = BASE_URL + "/api/top/photos";

    /**
     * TODO: translate to English
     * Сервер возвращает JSON объект по одному шаблону (пример запроса /api/login):
     * {
     *     "status": 200,
     *     "error_code": 0,
     *     "error_msg": "",
     *     "error_ucode": "",
     *     "data": ""
     * }
     */

    //JSON keys general
    private static final String STATUS = "status";
    private static final String ERROR_CODE = "error_code";
    private static final String ERROR_MSG = "error_msg";
    private static final String ERROR_UCODE = "error_ucode";
    private static final String DATA = "data";

    //JSON keys for send form-data request
    private static final String EMAIL = "email";
    private static final String LOGIN = "login";
    private static final String PASSWORD = "password";
    private static final String TESTER_NAME = "tester_name";
    private static final String FILE = "file";
    private static final String COUNT = "count";
    private static final String OFFSET = "offset";
    private static final String USER_TOKEN = "user_token";

    //JSON keys for get form-data response
    private static final String ID = "id";
    private static final String NAME = "name";
    private static final String LINK = "link";
    private static final String RATE = "rate";
    private static final String PHOTOS = "photos";
    private static final String VOTE_TOKEN = "vote_token";
    private static final String VOTE = "vote";

    private static Network network;
    private RequestQueue requestQueue;

    private Network(Context context) {
        requestQueue = Volley.newRequestQueue(context);
    }

    public static synchronized Network get(Context context) {
        if (network == null) {
            network = new Network(context);
        }
        return network;
    }

    public interface VolleyCallBack {
        void onSuccess();
    }

    private void responseStatusError(JSONObject jsonObject) throws JSONException {
        int errorCode = jsonObject.getInt(ERROR_CODE);
        String errorMsg = jsonObject.getString(ERROR_MSG);
        String errorUCode = jsonObject.getString(ERROR_UCODE);
        Log.e(TAG, "Response status = 500: " +
                "error_code = " + errorCode +
                ", error_msg = " + errorMsg +
                ", error_ucode = " + errorUCode);
    }

    /**
     * Send body:
     * Text email;
     * Text login;
     * Text password;
     * Text tester_name;
     * <p>
     * Return data: ""
     */
    public void registerRequest(String userEmail, String userLogin, String userPass,
                                String testerName, VolleyCallBack callBack) {
        StringRequest stringRequest = new StringRequest(Request.Method.POST, REGISTER_REQUEST,
                response -> {
                    try {
                        JSONObject jsonObject = new JSONObject(response);
                        int status = jsonObject.getInt(STATUS);

                        switch (status) {
                            case 200:
                                Log.i(TAG, "Success /register/ request");
                                break;
                            case 500:
                                responseStatusError(jsonObject);
                                break;
                        }
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }

                    callBack.onSuccess();

                }, Throwable::printStackTrace) {
            protected Map<String, String> getParams() {
                HashMap<String, String> hashMapParams = new HashMap<>();
                hashMapParams.put(EMAIL, userEmail);
                hashMapParams.put(LOGIN, userLogin);
                hashMapParams.put(PASSWORD, userPass);
                hashMapParams.put(TESTER_NAME, testerName);
                return hashMapParams;
            }
        };
        requestQueue.add(stringRequest);
    }

    /**
     * Send body:
     * Text login;
     * Text password;
     * <p>
     * Return data:
     * "user_token": "d571379fbc8588f35e048a18c"
     */
    public void loginRequest(String userLogin, String userPass, VolleyCallBack callBack) {
        StringRequest stringRequest = new StringRequest(Request.Method.POST, LOGIN_REQUEST,
                response -> {
                    try {
                        JSONObject jsonObject = new JSONObject(response);
                        int status = jsonObject.getInt(STATUS);

                        switch (status) {
                            case 200:
                                CurrentUser.get().setToken(
                                        jsonObject.getJSONObject(DATA).getString(USER_TOKEN));
                                Log.i(TAG, "Success /login/ request");
                                break;
                            case 500:
                                responseStatusError(jsonObject);
                                break;
                        }
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }

                    callBack.onSuccess();

                }, Throwable::printStackTrace) {
            protected Map<String, String> getParams() {
                HashMap<String, String> hashMapParams = new HashMap<>();
                hashMapParams.put(LOGIN, userLogin);
                hashMapParams.put(PASSWORD, userPass);
                return hashMapParams;
            }
        };
        requestQueue.add(stringRequest);
    }

    /**
     * Send body:
     * Text user_token;
     * <p>
     * Return data: ""
     */
    public void logoutRequest(VolleyCallBack callBack) {
        StringRequest stringRequest = new StringRequest(Request.Method.POST, LOGOUT_REQUEST,
                response -> {
                    try {
                        JSONObject jsonObject = new JSONObject(response);
                        int status = jsonObject.getInt(STATUS);

                        switch (status) {
                            case 200:
                                Log.i(TAG, "Success /logout/ request");
                                break;
                            case 500:
                                responseStatusError(jsonObject);
                                break;
                        }
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }

                    callBack.onSuccess();

                }, Throwable::printStackTrace) {
            protected Map<String, String> getParams() {
                HashMap<String, String> hashMapParams = new HashMap<>();
                hashMapParams.put(USER_TOKEN, CurrentUser.get().getToken());
                return hashMapParams;
            }
        };
        requestQueue.add(stringRequest);
    }

    /**
     * Send body:
     * Text user_token;
     * File file;
     * <p>
     * Return data: ""
     */
    public void galleryAddRequest(String imagePath, VolleyCallBack callBack) {
        SimpleMultiPartRequest multiPartRequest =
                new SimpleMultiPartRequest(Request.Method.POST, GALLERY_ADD_REQUEST,
                        response -> {
                            try {
                                JSONObject jsonObject = new JSONObject(response);
                                int status = jsonObject.getInt(STATUS);

                                switch (status) {
                                    case 200:
                                        Log.i(TAG, "Success /gallery/add/ request");
                                        break;
                                    case 500:
                                        responseStatusError(jsonObject);
                                        break;
                                }
                            } catch (JSONException e) {
                                e.printStackTrace();
                            }

                            callBack.onSuccess();

                        }, Throwable::printStackTrace);
        multiPartRequest.addStringParam(USER_TOKEN, CurrentUser.get().getToken());
        multiPartRequest.addFile(FILE, imagePath);
        requestQueue.add(multiPartRequest);
    }

    /**
     * Send body:
     *     Text user_token;
     *     Text count;
     *     Text offset;
     *
     * Return data:
     * [
     *     {
     *         "id": 23,
     *         "name": "464608018433113178.jpg",
     *         "link": "http://s1.ntech.team:8092/464608018433113178.jpg",
     *         "rate": 0
     *     }
     * ]
     */
    public List<Image> galleryRequest(int count, int offset, VolleyCallBack callBack) {
        List<Image> images = new ArrayList<>();

        StringRequest stringRequest = new StringRequest(Request.Method.POST, GALLERY_REQUEST,
                response -> {
                    try {
                        JSONObject jsonObject = new JSONObject(response);
                        int status = jsonObject.getInt(STATUS);

                        switch (status) {
                            case 200:
                                JSONArray jsonArray = jsonObject.getJSONArray(DATA);

                                for (int i = 0; i < jsonArray.length(); i++) {
                                    Image image = new Image();
                                    image.setId(jsonArray.getJSONObject(i).getInt(ID));
                                    image.setName(jsonArray.getJSONObject(i).getString(NAME));
                                    image.setImageURL(jsonArray.getJSONObject(i).getString(LINK));
                                    image.setRate(jsonArray.getJSONObject(i).getInt(RATE));
                                    images.add(image);
                                }

                                Log.i(TAG, "Success /gallery/ request");
                                break;
                            case 500:
                                responseStatusError(jsonObject);
                                break;
                        }

                        callBack.onSuccess();

                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }, Throwable::printStackTrace) {
            protected Map<String, String> getParams() {
                HashMap<String, String> hashMapParams = new HashMap<>();
                hashMapParams.put(USER_TOKEN, CurrentUser.get().getToken());
                hashMapParams.put(COUNT, String.valueOf(count));
                hashMapParams.put(OFFSET, String.valueOf(offset));
                return hashMapParams;
            }
        };
        requestQueue.add(stringRequest);
        return images;
    }

    /**
     * Send body:
     * Text user_token;
     * Text id;
     * <p>
     * Return data: ""
     */
    public void galleryRemoveRequest(Image image, VolleyCallBack callBack) {
        StringRequest stringRequest =
                new StringRequest(Request.Method.POST, GALLERY_REMOVE_REQUEST,
                        response -> {
                            try {
                                JSONObject jsonObject = new JSONObject(response);
                                int status = jsonObject.getInt(STATUS);

                                switch (status) {
                                    case 200:
                                        Log.i(TAG, "Success /gallery/remove/ request");
                                        break;
                                    case 500:
                                        responseStatusError(jsonObject);
                                        break;
                                }

                                callBack.onSuccess();

                            } catch (JSONException e) {
                                e.printStackTrace();
                            }
                        }, Throwable::printStackTrace) {
                    protected Map<String, String> getParams() {
                        HashMap<String, String> hashMapParams = new HashMap<>();
                        hashMapParams.put(USER_TOKEN, CurrentUser.get().getToken());
                        hashMapParams.put(ID, String.valueOf(image.getId()));
                        return hashMapParams;
                    }
                };
        requestQueue.add(stringRequest);
    }

    /**
     * Send body:
     * Text user_token;
     * <p>
     * Return data:
     * {
     * "photos": [
     * {
     * "id": 5,
     * "name": "252580415678854431.jpg",
     * "link": "http://s1.ntech.team:8092/252580415678854431.jpg",
     * "rate": 0
     * },
     * {
     * "id": 31,
     * "name": "548105026384646746.jpg",
     * "link": "http://s1.ntech.team:8092/548105026384646746.jpg",
     * "rate": 0
     * }
     * ],
     * "vote_token": "362b081e76d1cc394f15cbf10259c9f37b"
     * }
     */
    public List<Image> voteCreateRequest(VolleyCallBack callBack) {
        List<Image> images = new ArrayList<>();

        StringRequest stringRequest = new StringRequest(Request.Method.POST, VOTE_CREATE_REQUEST,
                response -> {
                    try {
                        JSONObject jsonObject = new JSONObject(response);
                        int status = jsonObject.getInt(STATUS);

                        switch (status) {
                            case 200:
                                JSONObject jsonObjectData = jsonObject.getJSONObject(DATA);
                                JSONArray jsonArray = jsonObjectData.getJSONArray(PHOTOS);

                                for (int i = 0; i < jsonArray.length(); i++) {
                                    Image image = new Image();
                                    image.setId(jsonArray.getJSONObject(i).getInt(ID));
                                    image.setName(jsonArray.getJSONObject(i).getString(NAME));
                                    image.setImageURL(jsonArray.getJSONObject(i).getString(LINK));
                                    image.setRate(jsonArray.getJSONObject(i).getInt(RATE));
                                    images.add(image);
                                }

                                CurrentUser.get().setVoteToken(jsonObjectData.getString(VOTE_TOKEN));

                                Log.i(TAG, "Success /vote/create/ request");
                                break;
                            case 500:
                                responseStatusError(jsonObject);
                                break;
                        }

                        callBack.onSuccess();

                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }, Throwable::printStackTrace) {
            protected Map<String, String> getParams() {
                HashMap<String, String> hashMapParams = new HashMap<>();
                hashMapParams.put(USER_TOKEN, CurrentUser.get().getToken());
                return hashMapParams;
            }
        };
        requestQueue.add(stringRequest);
        return images;
    }

    /**
     * Send body:
     * Text user_token;
     * Text vote_token;
     * Text vote = 0 or 1;
     * <p>
     * Return data: ""
     */
    public void voteRequest(int vote, VolleyCallBack callBack) {
        StringRequest stringRequest = new StringRequest(Request.Method.POST, VOTE_REQUEST,
                response -> {
                    try {
                        JSONObject jsonObject = new JSONObject(response);
                        int status = jsonObject.getInt(STATUS);

                        switch (status) {
                            case 200:
                                Log.i(TAG, "Success /vote/ request");
                                break;
                            case 500:
                                responseStatusError(jsonObject);
                                break;
                        }

                        callBack.onSuccess();

                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }, Throwable::printStackTrace) {
            protected Map<String, String> getParams() {
                HashMap<String, String> hashMapParams = new HashMap<>();
                hashMapParams.put(USER_TOKEN, CurrentUser.get().getToken());
                hashMapParams.put(VOTE_TOKEN, CurrentUser.get().getVoteToken());
                hashMapParams.put(VOTE, String.valueOf(vote));
                return hashMapParams;
            }
        };
        requestQueue.add(stringRequest);
    }

    /**
     * Send body:
     * Text user_token;
     * Text count;
     * Text offset;
     * <p>
     * Return data:
     * [
     * {
     * "id": 23,
     * "name": "464608018433113178.jpg",
     * "link": "http://s1.ntech.team:8092/464608018433113178.jpg",
     * "rate": 0
     * }
     * ]
     */
    public List<Image> topPhotosRequest(int count, int offset, VolleyCallBack callBack) {
        List<Image> images = new ArrayList<>();

        StringRequest stringRequest = new StringRequest(Request.Method.POST, TOP_PHOTOS_REQUEST,
                response -> {
                    try {
                        JSONObject jsonObject = new JSONObject(response);
                        int status = jsonObject.getInt(STATUS);

                        switch (status) {
                            case 200:
                                JSONArray jsonArray = jsonObject.getJSONArray(DATA);

                                for (int i = 0; i < jsonArray.length(); i++) {
                                    Image image = new Image();
                                    image.setId(jsonArray.getJSONObject(i).getInt(ID));
                                    image.setName(jsonArray.getJSONObject(i).getString(NAME));
                                    image.setImageURL(jsonArray.getJSONObject(i).getString(LINK));
                                    image.setRate(jsonArray.getJSONObject(i).getInt(RATE));
                                    images.add(image);
                                }

                                Log.i(TAG, "Success /top/photos/ request");
                                break;
                            case 500:
                                responseStatusError(jsonObject);
                                break;
                        }

                        callBack.onSuccess();

                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }, Throwable::printStackTrace) {
            protected Map<String, String> getParams() {
                HashMap<String, String> hashMapParams = new HashMap<>();
                hashMapParams.put(USER_TOKEN, CurrentUser.get().getToken());
                hashMapParams.put(COUNT, String.valueOf(count));
                hashMapParams.put(OFFSET, String.valueOf(offset));
                return hashMapParams;
            }
        };
        requestQueue.add(stringRequest);
        return images;
    }
}
