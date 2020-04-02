package com.supercasual.fourtop.utils;

public class Requests {

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
}
