package com.supercasual.fourtop.utils;

import android.content.Context;
import android.util.Log;

import androidx.recyclerview.widget.LinearLayoutManager;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.request.StringRequest;
import com.android.volley.toolbox.Volley;
import com.supercasual.fourtop.adapter.ImageAdapter;
import com.supercasual.fourtop.model.CurrentUser;
import com.supercasual.fourtop.model.Image;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Requests {

    private static final String TAG = "debug_logs";

    private static final String BASE_URL = "http://ntech.team:8082";

    public static final String REGISTER = BASE_URL + "/api/register";
    public static final String LOGIN = BASE_URL + "/api/login";
    public static final String LOGOUT = BASE_URL + "/api/logout";
    public static final String GALLERY_ADD = BASE_URL + "/api/gallery/add";
    public static final String GALLERY = BASE_URL + "/api/gallery";
    public static final String GALLERY_REMOVE = BASE_URL + "/api/gallery/remove";
    public static final String VOTE_CREATE = BASE_URL + "/api/vote/create";
    public static final String VOTE = BASE_URL + "/api/vote";
    public static final String TOP_PHOTOS = BASE_URL + "/api/top/photos";

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

    //JSON KEYS LOGIN
    private static final String USER_TOKEN = "user_token";

    //JSON KEYS GALLERY
    private static final String ID = "id";
    private static final String NAME = "name";
    private static final String LINK = "link";
    private static final String RATE = "rate";

    /**
     * Return JSON:
     * {
     *     "status": 200,
     *     "error_code": 0,
     *     "error_msg": "",
     *     "error_ucode": "",
     *     "data": [
     *         {
     *             "id": 16,
     *             "name": "378362376518825883.jpg",
     *             "link": "http://s1.ntech.team:8092/378362376518825883.jpg",
     *             "rate": 0
     *         }
     *     ]
     * }
     */
    private List<Image> galleryRequest(Context context) {
        List<Image> images = new ArrayList<>();
        StringRequest stringRequest = new StringRequest(Request.Method.POST, GALLERY,
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
                                break;
                            case 500:
                                responseStatusError(jsonObject);
                                break;
                        }
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }, error -> Log.d(TAG, "Incorrect request: " + error)) {
            protected Map<String, String> getParams() {
                HashMap<String, String> hashMapParams = new HashMap<>();
                hashMapParams.put("user_token", CurrentUser.get().getToken());
                hashMapParams.put("count", "10");
                hashMapParams.put("offset", "0");
                return hashMapParams;
            }
        };
        RequestQueue requestQueue = Volley.newRequestQueue(context);
        requestQueue.add(stringRequest);
        return;
    }

    private void responseStatusError(JSONObject jsonObject) throws JSONException {
        int errorCode = jsonObject.getInt(ERROR_CODE);
        String errorMsg = jsonObject.getString(ERROR_MSG);
        String errorUCode = jsonObject.getString(ERROR_UCODE);
        Log.e(TAG, "Responce status = 500: " +
                "error_code = " + errorCode +
                ", error_msg = " + errorMsg +
                ", error_ucode = " + errorUCode);
    }
}
