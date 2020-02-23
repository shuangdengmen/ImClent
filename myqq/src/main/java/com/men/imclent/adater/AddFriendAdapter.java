package com.men.imclent.adater;

import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import com.men.imclent.R;
import com.men.imclent.model.User;

import java.util.ArrayList;
import java.util.List;

public class AddFriendAdapter extends RecyclerView.Adapter<AddFriendAdapter.MyViewHolder> {
    private List<User> users;
    private List<String> contacts = new ArrayList<String>();

    public void setContacts(List<String> contacts) {
        if (contacts != null) {

            this.contacts = contacts;
        }
    }

    public void setUsers(List<User> users) {
        this.users = users;
    }

    public AddFriendAdapter(List<User> users, List<String> contacts) {
        this.users = users;
        if (contacts != null) {

            this.contacts = contacts;
        }
    }

    @NonNull
    @Override

    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.list_item_add_friend, viewGroup, false);
        MyViewHolder myViewHolder = new MyViewHolder(view);

        return myViewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder myViewHolder, int i) {
        String username = users.get(i).getUsername();
        myViewHolder.tvUsername.setText(username);
        myViewHolder.tvTime.setText(users.get(i).getCreatedAt());
        if (contacts.contains(username)) {
            myViewHolder.btn_add.setText("已经是好友");
            myViewHolder.btn_add.setEnabled(false);
        } else {
            myViewHolder.btn_add.setText("添加");
            myViewHolder.btn_add.setEnabled(true);
            myViewHolder.btn_add.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (onAddFriendClickListener != null) {
                        onAddFriendClickListener.onClick(v,username);
                    }
                }
            });
        }
    }

    @Override
    public int getItemCount() {
        return users == null ? 0 : users.size();
    }

    class MyViewHolder extends RecyclerView.ViewHolder {
        TextView tvUsername = null;
        TextView tvTime = null;
        Button btn_add = null;

        public MyViewHolder(@NonNull View itemView) {
            super(itemView);
            tvUsername = (TextView) itemView.findViewById(R.id.tv_username);
            tvTime = (TextView) itemView.findViewById(R.id.tv_regist_time);
            btn_add = (Button) itemView.findViewById(R.id.btn_add);
        }


    }
    private  OnAddFriendClickListener onAddFriendClickListener;

    public void setOnAddFriendClickListener(OnAddFriendClickListener onAddFriendClickListener) {
        this.onAddFriendClickListener = onAddFriendClickListener;
    }

    public interface OnAddFriendClickListener{
        void onClick(View v,String username);
    }
}
