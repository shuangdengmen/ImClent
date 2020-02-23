package com.men.imclent.adater;

import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.men.imclent.R;
import com.men.imclent.utils.StringUtils;

import java.util.List;

public class ContactAdapter extends RecyclerView.Adapter<ContactAdapter.MyViewHolder> {

    private List<String> contacts;

    public void setContacts(List<String> contacts) {
        this.contacts = contacts;
    }

    public List<String> getContacts() {
        return contacts;
    }

    public ContactAdapter(List<String> contacts) {
        this.contacts = contacts;
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.list_contact_item, viewGroup, false);
        MyViewHolder holder = new MyViewHolder(view);
        return holder;
    }


        @Override
    public void onBindViewHolder( MyViewHolder viewHolder, int position) {
        String contact = contacts.get(position);
        viewHolder.tv_contact.setText(contact);
        viewHolder.tv_selection.setText(StringUtils.getFirstChar(contact));

        if (position == 0) {
            viewHolder.tv_selection.setVisibility(View.VISIBLE);
        } else {
                String current = StringUtils.getFirstChar(contact);
                String last = StringUtils.getFirstChar(contacts.get(position - 1));
                if (current.equals(last)) {
                    viewHolder.tv_selection.setVisibility(View.GONE);
                }else {
                    viewHolder.tv_selection.setVisibility(View.VISIBLE);
                }

            }
        viewHolder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (onItemClickListener != null) {
                    onItemClickListener.onClick(v,contact);
                }
            }
        });

        viewHolder.itemView.setOnLongClickListener(
                new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {
                if (onItemClickListener != null) {
                    onItemClickListener.onLongClick(v,contact);
                    return true;
                }
                return false;
            }
        });
    }

    public interface  OnItemClickListener{
        public void onClick(View v ,String username);

        public boolean onLongClick(View view,String username);

    }

    private  OnItemClickListener onItemClickListener;
    public void setOnItemClickListener(OnItemClickListener onItemClickListener) {
        this.onItemClickListener = onItemClickListener;
    }


    @Override
    public int getItemCount() {
        return contacts == null ? 0 : contacts.size();
    }

    class MyViewHolder extends RecyclerView.ViewHolder {
        TextView tv_selection;
        TextView tv_contact;

        private MyViewHolder(@NonNull View itemView) {
            super(itemView);
            tv_selection = (TextView) itemView.findViewById(R.id.tv_selection);
            tv_contact = (TextView) itemView.findViewById(R.id.tv_contact);
        }
    }
}
