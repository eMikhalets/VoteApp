package com.ntech.fourtop.utils;

import android.Manifest;

public class Const {

    public static final String BASE_URL = "http://ntech.team:8082";

    // SharedPreferences
    public static final String SHARED_FILE = "shared_fourtop";
    public static final String SHARED_TOKEN = "shared_user_token";

    // Bundle
    public static final String ARGS_TOKEN = "args_user_token";
    public static final String ARGS_LOGIN = "args_login";
    public static final String ARGS_PASS = "args_password";

    // Permissions
    public static final int READ_EXTERNAL_REQUEST = 1;
    public static final String[] READ_EXTERNAL_PERMISSION =
            {Manifest.permission.READ_EXTERNAL_STORAGE};
}
