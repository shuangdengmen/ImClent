package com.men.imclent.presenter.impl;

import com.hyphenate.chat.EMClient;
import com.hyphenate.exceptions.HyphenateException;
import com.men.imclent.db.DBUtils;
import com.men.imclent.model.User;
import com.men.imclent.presenter.AddFriendPresenter;
import com.men.imclent.utils.ThreadUtils;
import com.men.imclent.view.IAddFriendView;

import java.util.List;

import cn.bmob.v3.BmobQuery;
import cn.bmob.v3.exception.BmobException;
import cn.bmob.v3.listener.FindListener;

public class AddFriendPresenterImpl implements AddFriendPresenter {
    IAddFriendView iAddFriendView;

    public AddFriendPresenterImpl(IAddFriendView iAddFriendView) {
        this.iAddFriendView = iAddFriendView;
    }

    @Override
    public void searchFriend(String keyWord) {
        String currentUser = EMClient.getInstance().getCurrentUser();
        BmobQuery<User> bmobQuery = new BmobQuery<User>();
        bmobQuery.addWhereStartsWith("username",keyWord).addWhereNotEqualTo("username",currentUser).findObjects(new FindListener<User>() {
            @Override
            public void done(List<User> list, BmobException e) {
                if (e == null && list != null && list.size() > 0) {
                    List<String> users = DBUtils.initContact(currentUser);
                    iAddFriendView.onQuerySuccess(list,users,true,null);
                } else {
                    if (e == null) {
                        iAddFriendView.onQuerySuccess(null,null,false,"没有数据");
                    }else {
                        iAddFriendView.onQuerySuccess(null, null, false, e.getMessage());
                    }
                }
            }
        });

    }

    @Override
    public void addFriend(String username) {
        ThreadUtils.runOnNOUI(new Runnable() {
            @Override
            public void run() {
                try {
                    EMClient.getInstance().contactManager().addContact(username,"申请添加");

                    ThreadUtils.runOnMainUI(new Runnable() {
                        @Override
                        public void run() {
                            iAddFriendView.onGetAddFriendResult(true,null);
                        }
                    });
                } catch (HyphenateException e) {
                    e.printStackTrace();
                    ThreadUtils.runOnMainUI(new Runnable() {
                        @Override
                        public void run() {
                            iAddFriendView.onGetAddFriendResult(true,e.getMessage());
                        }
                    });
                }

            }
        });

    }
}
