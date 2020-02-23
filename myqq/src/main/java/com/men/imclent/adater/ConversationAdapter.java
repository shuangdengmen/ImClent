package com.men.imclent.adater;

import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.hyphenate.chat.EMConversation;
import com.hyphenate.chat.EMMessage;
import com.hyphenate.chat.EMTextMessageBody;
import com.hyphenate.util.DateUtils;
import com.men.imclent.R;
import com.men.imclent.model.User;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class ConversationAdapter extends RecyclerView.Adapter<ConversationAdapter.MyViewHolder> {

    private List<EMConversation> conversations = new ArrayList<EMConversation>();

    public ConversationAdapter(List<EMConversation> conversations) {
        this.conversations = conversations;
    }

    public void setConversations(List<EMConversation> conversations) {
        this.conversations = conversations;
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.list_item_conversation, viewGroup, false);
        MyViewHolder myViewHolder = new MyViewHolder(view);

        return myViewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder myViewHolder, int position) {
        EMConversation emConversation = conversations.get(position);
        EMMessage lastMessage = emConversation.getLastMessage();
        String userName = lastMessage.getUserName();
        EMTextMessageBody body = (EMTextMessageBody) lastMessage.getBody();
        String message = body.getMessage();
        int unreadMsgCount = emConversation.getUnreadMsgCount();

        myViewHolder.tvUsername.setText(userName);
        myViewHolder.tvMessage.setText(message);
        Date msgTime = new Date(lastMessage.getMsgTime());
        String msgTimeStr = DateUtils.getTimestampString(msgTime);
        myViewHolder.tvTime.setText(msgTimeStr==null?"":msgTimeStr);


        if (unreadMsgCount == 0) {
            myViewHolder.tvUnread.setVisibility(View.GONE);
        } else if (unreadMsgCount>99) {
            myViewHolder.tvUnread.setText(String.valueOf(99));
            myViewHolder.tvUnread.setVisibility(View.VISIBLE);
        } else {
            myViewHolder.tvUnread.setText(String.valueOf(unreadMsgCount));
            myViewHolder.tvUnread.setVisibility(View.VISIBLE);
        }

        myViewHolder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (onAddItemClickListener != null) {
                    onAddItemClickListener.onClick(v,userName);
                }
            }
        });
    }

    @Override
    public int getItemCount() {
        return conversations == null ? 0 : conversations.size();
    }

    class MyViewHolder extends RecyclerView.ViewHolder {
        TextView tvUsername = null;
        TextView tvMessage = null;
        TextView tvTime=null;
        TextView tvUnread=null;

        public MyViewHolder(@NonNull View itemView) {
            super(itemView);
            tvUsername = (TextView) itemView.findViewById(R.id.tv_username);
            tvMessage = (TextView) itemView.findViewById(R.id.tv_message);
            tvTime = (TextView) itemView.findViewById(R.id.tv_time);
            tvUnread = (TextView) itemView.findViewById(R.id.tv_unread);
        }


    }
    private  OnAddItemClickListener onAddItemClickListener;

    public void setOnAddItemClickListener(OnAddItemClickListener onAddItemClickListener) {
        this.onAddItemClickListener = onAddItemClickListener;
    }

    public interface OnAddItemClickListener{
        void onClick(View v, String username);
    }
}
