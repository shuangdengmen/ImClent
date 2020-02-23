package com.men.imclent.view;

import com.hyphenate.chat.EMConversation;

import java.util.List;

public interface IConversationView {
    void onGetConversations(List<EMConversation> emConversations);

    void onClearAllUnreadMark();
}
