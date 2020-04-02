package com.supercasual.fourtop;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.View;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.supercasual.fourtop.adapter.ImageAdapter;
import com.supercasual.fourtop.model.CurrentUser;
import com.supercasual.fourtop.utils.Requests;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class UserImagesActivity extends AppCompatActivity {

    private static final String TAG = "debug_logs";
    private static final int RESULT_LOAD_IMAGE = 0;

    private RequestQueue requestQueue;
    private StringRequest stringRequest;
    private List<String> imagesList;

    private RecyclerView recyclerView;
    private ImageAdapter imageAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_images);

        requestQueue = Volley.newRequestQueue(this);
        imagesList = new ArrayList<>();

        recyclerView = findViewById(R.id.recycler_user_images);
        galleryRequest();
    }

    @Override
    protected void onResume() {
        super.onResume();
    }

    private void galleryRequest() {
        stringRequest = new StringRequest(Request.Method.POST, Requests.GALLERY,
                response -> {
                    try {
                        JSONObject jsonObject = new JSONObject(response);
                        JSONArray jsonArray = jsonObject.getJSONArray("data");
                        for (int i = 0; i < jsonArray.length(); i++) {
                            imagesList.add(jsonArray.getJSONObject(i).getString("link"));
                        }

                        recyclerView.setLayoutManager(new LinearLayoutManager(this));
                        imageAdapter = new ImageAdapter(this, imagesList);
                        recyclerView.setAdapter(imageAdapter);
                    } catch (JSONException e) {
                        e.printStackTrace();
                        Log.d(TAG, "JSONException: " + e.toString());
                    }
                }, error -> Log.d(TAG, "onErrorResponse: " + error.toString())) {
            protected Map<String, String> getParams() {
                HashMap<String, String> hashMapParams = new HashMap<>();
                hashMapParams.put("user_token", CurrentUser.get().getToken());
                hashMapParams.put("count", "10");
                hashMapParams.put("offset", "0");
                return hashMapParams;
            }
        };
        requestQueue.add(stringRequest);
    }

    private void galleryAddRequest() {
        stringRequest = new StringRequest(Request.Method.POST, Requests.GALLERY_ADD,
                response -> {
                    try {
                        JSONObject jsonObject = new JSONObject(response);
                    } catch (JSONException e) {
                        e.printStackTrace();
                        Log.d(TAG, "JSONException: " + e.toString());
                    }
                }, error -> Log.d(TAG, "onErrorResponse: " + error.toString())) {
            protected Map<String, String> getParams() {
                HashMap<String, String> hashMapParams = new HashMap<>();
                hashMapParams.put("user_token", CurrentUser.get().getToken());
                return hashMapParams;
            }
        };
        requestQueue.add(stringRequest);
    }

    private void galleryRemoveRequest() {
        stringRequest = new StringRequest(Request.Method.POST, Requests.GALLERY_REMOVE,
                response -> {
                    try {
                        JSONObject jsonObject = new JSONObject(response);
                    } catch (JSONException e) {
                        e.printStackTrace();
                        Log.d(TAG, "JSONException: " + e.toString());
                    }
                }, error -> Log.d(TAG, "onErrorResponse: " + error.toString())) {
            protected Map<String, String> getParams() {
                HashMap<String, String> hashMapParams = new HashMap<>();
                hashMapParams.put("user_token", CurrentUser.get().getToken());
                return hashMapParams;
            }
        };
        requestQueue.add(stringRequest);
    }

    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.btn_user_images_upload:
                pickFromGallery();
                break;
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == RESULT_OK) {
            switch (requestCode) {
                case RESULT_LOAD_IMAGE:
                    Uri selectedImage = data.getData();
                    Log.d(TAG, "file: " + selectedImage);
            }
        }
    }

    private void pickFromGallery() {
        Intent intent = new Intent(Intent.ACTION_PICK);
        intent.setType("image/*");
        String[] mimeTypes = {"image/jpeg", "image/png"};
        intent.putExtra(Intent.EXTRA_MIME_TYPES, mimeTypes);
        startActivityForResult(intent, RESULT_LOAD_IMAGE);
    }
}
