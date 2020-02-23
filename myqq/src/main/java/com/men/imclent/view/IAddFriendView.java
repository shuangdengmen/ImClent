package com.men.imclent.view;

import com.men.imclent.model.User;

import java.util.List;

public interface IAddFriendView {
    void onQuerySuccess(List<User> list, List<String> users, boolean b, String errorMsg);

    void onGetAddFriendResult(boolean b, String message);
}
