package com.men.imclent.view;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.hyphenate.chat.EMClient;
import com.hyphenate.chat.EMConversation;
import com.hyphenate.chat.EMMessage;
import com.men.imclent.MainActivity;
import com.men.imclent.R;
import com.men.imclent.adater.ContactAdapter;
import com.men.imclent.adater.ConversationAdapter;
import com.men.imclent.presenter.ConversationPresenter;
import com.men.imclent.presenter.impl.ConversationPresenterImpl;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.util.List;

public class ConversationFragment extends BaseFragment implements  IConversationView{

    private ConversationAdapter adapter;
    private ConversationPresenter presenter;
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        return inflater.inflate(R.layout.fragment_conversation,null);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        RecyclerView recyclerView = (RecyclerView) view.findViewById(R.id.rv_recyclerView);
        FloatingActionButton floatingActionButton = (FloatingActionButton) view.findViewById(R.id.fab);
        floatingActionButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                presenter.clearAllUnreadMark();
            }
        });
        adapter = new ConversationAdapter(null);
        adapter.setOnAddItemClickListener(new ConversationAdapter.OnAddItemClickListener() {
            @Override
            public void onClick(View v, String username) {
                Intent intent = new Intent(getContext(), ChatActivity.class);
                intent.putExtra("contact", username);
                startActivity(intent);
            }
        });
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        recyclerView.setAdapter(adapter);
        presenter = new ConversationPresenterImpl(this);
        presenter.getConversations();
        super.onViewCreated(view, savedInstanceState);
    }

    @Override
    public void onGetConversations(List<EMConversation> emConversations) {
        adapter.setConversations(emConversations);
        adapter.notifyDataSetChanged();


    }

    @Override
    public void onClearAllUnreadMark() {
        presenter.getConversations();
        MainActivity activity = (MainActivity) getActivity();
        activity.updateBadgeItem();
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onGetMessageEvent(List<EMMessage> list) {
        presenter.getConversations();
    }

    @Override
    public void onStart() {
        if (!EventBus.getDefault().isRegistered(this)) {
            EventBus.getDefault().register(this);
        }
        super.onStart();
    }

    @Override
    public void onStop() {
        EventBus.getDefault().unregister(toString());
        super.onStop();
    }

    @Override
    public void onResume() {
        super.onResume();
        presenter.getConversations();
    }
}
