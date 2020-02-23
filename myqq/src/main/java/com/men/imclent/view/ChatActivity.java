package com.men.imclent.view;

import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.hyphenate.chat.EMMessage;
import com.hyphenate.chat.EMTextMessageBody;
import com.men.imclent.R;
import com.men.imclent.adater.ChatAdapter;
import com.men.imclent.presenter.ChatPresenter;
import com.men.imclent.presenter.impl.ChatPresenterImpl;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.util.List;

import butterknife.ButterKnife;
import butterknife.InjectView;
import butterknife.OnClick;

public class ChatActivity extends BaseActivity implements IChatView {
    @InjectView(R.id.tv_title)
    TextView tvTitle;
    @InjectView(R.id.tb_tool)
    Toolbar tbTool;
    @InjectView(R.id.rv_chat)
    RecyclerView rvChat;
    @InjectView(R.id.et_message)
    EditText etMessage;
    @InjectView(R.id.btn_send)
    Button btnSend;

    private ChatPresenter presenter;
    private ChatAdapter adapter;
    private String contact;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chat);
        ButterKnife.inject(this);
        initToolBar();
        contact = getIntent().getStringExtra("contact");
        tvTitle.setText("与"+ contact +"聊天中");
        adapter = new ChatAdapter(null);
        rvChat.setLayoutManager(new LinearLayoutManager(getApplicationContext()));
        rvChat.setAdapter(adapter);
        presenter = new ChatPresenterImpl(this);
        presenter.getChatHistoryMessage(contact);


        etMessage.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if (s.length() == 0) {
                    btnSend.setEnabled(false);
                } else {
                    btnSend.setEnabled(true);
                }
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });
    }

    private void initToolBar() {
        tbTool.setTitle("");
        setSupportActionBar(tbTool);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch(item.getItemId()){
            case android.R.id.home:
                finish();
                break;
            default:
                break;
        }
        return super.onOptionsItemSelected(item);
    }

    @OnClick(R.id.btn_send)
    public void onClick(View v) {
        String sendMsg = etMessage.getText().toString();
        etMessage.setText("");
        presenter.sendMessage(contact,sendMsg);
    }

    @Subscribe(threadMode= ThreadMode.MAIN )
   public void onGetReceiveMessages(List<EMMessage> list) {
        EMTextMessageBody textMessageBody= (EMTextMessageBody) list.get(0).getBody();

        Log.d("textMessageBody",textMessageBody.getMessage());
        presenter.getChatHistoryMessage(contact);
    }

    @Override
    public void loadHistoryMessages(List<EMMessage> msgFromDB) {
        adapter.setMessages(msgFromDB);
        adapter.notifyDataSetChanged();
        if(adapter.getItemCount()>0){
            rvChat.smoothScrollToPosition(adapter.getItemCount()-1);
        }
    }

    @Override
    public void updateList() {
        adapter.notifyDataSetChanged();
        if(adapter.getItemCount()>0){
            rvChat.smoothScrollToPosition(adapter.getItemCount()-1);
        }
    }



}
