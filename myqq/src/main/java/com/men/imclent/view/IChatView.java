package com.men.imclent.view;

import com.hyphenate.chat.EMMessage;

import java.util.List;

public interface IChatView {

    void loadHistoryMessages(List<EMMessage> msgFromDB);

    void updateList();
}
