package com.men.imclent.view;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.men.imclent.R;
import com.men.imclent.adater.ContactAdapter;
import com.men.imclent.event.ContactChangeEvent;
import com.men.imclent.presenter.ContactPresenter;
import com.men.imclent.presenter.impl.ContactPresenterImpl;
import com.men.imclent.utils.ToastUtils;
import com.men.imclent.widget.ContactLayout;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.util.List;

public class ContactFragment extends BaseFragment implements  IContactView{

    private ContactLayout contactLayout;
    private ContactPresenter presenter;
    private ContactAdapter contactAdapter;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_contact,null);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        contactLayout = (ContactLayout) view.findViewById(R.id.cl_contactLayout);
        presenter = new ContactPresenterImpl(this);
        presenter.initContact();
        super.onViewCreated(view, savedInstanceState);
    }

    @Override
    public void onInitContact(List<String> contact) {
        contactAdapter = new ContactAdapter(contact);
        contactLayout.setAdapter(contactAdapter);
        contactLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                presenter.updateContact();
            }
        });
        contactAdapter.setOnItemClickListener(new ContactAdapter.OnItemClickListener() {
            @Override
            public void onClick(View v,String username) {
                Intent intent = new Intent(getContext(),ChatActivity.class);
                intent.putExtra("contact", username);
                startActivity(intent);
            }

            @Override
            public boolean onLongClick(View view,String username) {

                Snackbar.make(contactLayout,"真的要删除"+username+"吗？",Snackbar.LENGTH_LONG).setAction("确定", new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        presenter.deleteContact(username);
                    }
                }).show();
                return true;
            }
        });
    }

    @Override
    public void onUpdateContact(List<String> contact, boolean isUpdateSucess, String message) {
        contactLayout.setRefreshing(false);
        if (isUpdateSucess) {
            contactAdapter.setContacts(contact);
            contactAdapter.notifyDataSetChanged();
        } else {
            ToastUtils.showToast(getActivity(),"更新联系人失败");
        }
    }

    @Override
    public void onDeleteContact(boolean isDeleteSuccess, String errorMsg) {
        if (isDeleteSuccess) {
            ToastUtils.showToast(getContext(), "删除成功");
        } else {
            ToastUtils.showToast(getContext(), "删除失败:"+errorMsg);
        }

    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onGetContactChangeEvent(ContactChangeEvent event) {
        presenter.updateContact();
    }
    @Override
    public void onStart() {
        super.onStart();
        EventBus.getDefault().register(this);
    }

    @Override
    public void onStop() {
        super.onStop();
        EventBus.getDefault().unregister(this);
    }
}
