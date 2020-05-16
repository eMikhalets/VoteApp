package com.supercasual.fourtop.uimain.topusers;

import java.util.List;

public class TopUsersRepository {

    // TODO: replace List<String> on users data class
    public interface RequestCallback {
        void success(List<String> users);
        void failure(String result);
    }

    public void topUsersRequest(String token, RequestCallback callback) {
    }
}
