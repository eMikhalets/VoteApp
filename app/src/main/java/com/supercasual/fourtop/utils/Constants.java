package com.supercasual.fourtop.utils;

import android.Manifest;

public class Constants {

    // SharedPreferences
    public static final String SHARED_FILE = "sharedFourtop";
    public static final String SHARED_TOKEN = "sharedUserToken";

    // Bundle
    public static final String ARGS_TOKEN = "args_user_token";
    public static final String ARGS_LOGIN = "args_login";
    public static final String ARGS_PASS = "args_password";

    // Permissions
    public static final int READ_EXTERNAL_REQUEST = 1;
    public static final String[] READ_EXTERNAL_PERMISSION =
            {Manifest.permission.READ_EXTERNAL_STORAGE};
}
