package com.men.imclent.presenter;

import com.hyphenate.chat.EMMessage;

import java.util.List;

public interface ChatPresenter {
    List<EMMessage> getChatHistoryMessage(String username);

    void sendMessage(String contact, String sendMsg);
}
