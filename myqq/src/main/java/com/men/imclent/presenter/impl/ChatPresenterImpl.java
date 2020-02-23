package com.men.imclent.presenter.impl;

import com.hyphenate.chat.EMClient;
import com.hyphenate.chat.EMConversation;
import com.hyphenate.chat.EMMessage;
import com.men.imclent.callback.MyEMCallBack;
import com.men.imclent.presenter.ChatPresenter;
import com.men.imclent.utils.ThreadUtils;
import com.men.imclent.view.IChatView;

import java.util.ArrayList;
import java.util.List;

public class ChatPresenterImpl implements ChatPresenter {
    private IChatView iChatView;
    private List<EMMessage> messages = new ArrayList<EMMessage>();
    public ChatPresenterImpl(IChatView iChatView) {
        this.iChatView = iChatView;
    }



    @Override
    public  List<EMMessage> getChatHistoryMessage(String username) {

         EMConversation conversation = EMClient.getInstance().chatManager().getConversation(username);
        if (conversation!=null) {

             conversation.markAllMessagesAsRead();

            EMMessage lastMessage = conversation.getLastMessage();
            List<EMMessage>  msgFromDB= conversation.loadMoreMsgFromDB(lastMessage.getMsgId(), 20);
            messages.addAll(msgFromDB);
            messages.add(lastMessage);
            iChatView.loadHistoryMessages(messages);
        }

        return null;
    }

    @Override
    public void sendMessage(String contact, String sendMsg) {

        EMMessage message = EMMessage.createTxtSendMessage(sendMsg, contact);
        messages.add(message);
        message.setMessageStatusCallback(new MyEMCallBack() {
            @Override
            public void success() {
                iChatView.updateList();
            }

            @Override
            public void error(int i, String s) {
                iChatView.updateList();
            }
        });
        iChatView.loadHistoryMessages(messages);
        ThreadUtils.runOnNOUI(new Runnable() {
            @Override
            public void run() {
                EMClient.getInstance().chatManager().sendMessage(message);
            }
        });
    }
}
