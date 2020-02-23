package com.men.imclent.adater;

import android.graphics.drawable.AnimationDrawable;
import android.graphics.drawable.Drawable;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.hyphenate.chat.EMMessage;
import com.hyphenate.chat.EMMessageBody;
import com.hyphenate.chat.EMTextMessageBody;
import com.hyphenate.util.DateUtils;
import com.men.imclent.R;

import java.util.Date;
import java.util.List;

public class ChatAdapter extends RecyclerView.Adapter<ChatAdapter.MyViewHolder> {

    private List<EMMessage> messages;
    private View view;

    public ChatAdapter(List<EMMessage> messages) {
        this.messages = messages;
    }

    public void setMessages(List<EMMessage> messages) {
        this.messages = messages;
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int viewType) {

        if (viewType == 0) {
            view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.list_chat_item, viewGroup, false);

        } else {
            view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.list_chat_item_send, viewGroup, false);
        }


        return new MyViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder myViewHolder, int position) {
        EMMessage emMessage = messages.get(position);
        long msgTime = emMessage.getMsgTime();
        if (position == 0) {
            if (DateUtils.isCloseEnough(msgTime, System.currentTimeMillis())) {
                myViewHolder.tv_time.setVisibility(View.GONE);

            } else {
                myViewHolder.tv_time.setVisibility(View.VISIBLE);
                myViewHolder.tv_time.setText(DateUtils.getTimestampString(new Date(emMessage.getMsgTime())));

            }
        } else {
            if (DateUtils.isCloseEnough(msgTime, messages.get(position-1).getMsgTime())) {
                myViewHolder.tv_time.setVisibility(View.GONE);

            } else {
                myViewHolder.tv_time.setVisibility(View.VISIBLE);
                myViewHolder.tv_time.setText(DateUtils.getTimestampString(new Date(emMessage.getMsgTime())));

            }
        }
        //
        EMTextMessageBody body = (EMTextMessageBody)emMessage.getBody();

        String msgContent = body.getMessage();
        myViewHolder.tv_message.setText(msgContent);
        if (emMessage.direct() == EMMessage.Direct.SEND) {
            EMMessage.Status status = emMessage.status();
            switch(status){
                case   SUCCESS:
                    myViewHolder.iv_state.setVisibility(View.GONE);
                    break;
                case  FAIL:
                    myViewHolder.iv_state.setImageResource(R.mipmap.msg_error);
                    break;
                case INPROGRESS:
                    myViewHolder.iv_state.setImageResource(R.drawable.send_animation);
                    AnimationDrawable drawable = (AnimationDrawable)myViewHolder.iv_state.getDrawable();
                    drawable.start();

                    break;
            }
        }
    }



    @Override
    public int getItemCount() {
        return messages ==null?0: messages.size();
    }

    @Override
    public int getItemViewType(int position) {
        EMMessage emMessage = messages.get(position);
//        emMessage.getFrom();
        EMMessage.Direct direct = emMessage.direct();

        return  direct==EMMessage.Direct.RECEIVE?0:1 ;
    }

    class MyViewHolder extends RecyclerView.ViewHolder{
        TextView tv_time;
        TextView tv_message;
        ImageView iv_state;
        public MyViewHolder(@NonNull View itemView) {
            super(itemView);
            tv_time = (TextView) itemView.findViewById(R.id.tv_time);
            tv_message= (TextView) itemView.findViewById(R.id.tv_message);
            iv_state = (ImageView) itemView.findViewById(R.id.iv_state);
        }
    }
}
