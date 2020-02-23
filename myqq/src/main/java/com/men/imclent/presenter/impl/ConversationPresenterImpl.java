package com.men.imclent.presenter.impl;

import com.hyphenate.chat.EMClient;
import com.hyphenate.chat.EMConversation;
import com.men.imclent.presenter.ContactPresenter;
import com.men.imclent.presenter.ConversationPresenter;
import com.men.imclent.view.IContactView;
import com.men.imclent.view.IConversationView;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.Map;

public class ConversationPresenterImpl implements ConversationPresenter {
    private IConversationView iConversationView;

    public ConversationPresenterImpl(IConversationView iConversationView) {
        this.iConversationView = iConversationView;
    }

    @Override
    public void getConversations() {
        Map<String, EMConversation> conversationMaps = EMClient.getInstance().chatManager().getAllConversations();
        Collection<EMConversation> collection = conversationMaps.values();
        List<EMConversation> emConversations = new ArrayList<EMConversation>(collection);
        Collections.sort(emConversations, new Comparator<EMConversation>() {
            @Override
            public int compare(EMConversation o1, EMConversation o2) {
                return (int) (o2.getLastMessage().getMsgTime()-o1.getLastMessage().getMsgTime());
            }
        });
        iConversationView.onGetConversations(emConversations);
    }

    @Override
    public void clearAllUnreadMark() {
        EMClient.getInstance().chatManager().markAllConversationsAsRead();
        iConversationView.onClearAllUnreadMark();
    }
}
