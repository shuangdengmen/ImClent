package com.men.imclent.presenter.impl;

import com.hyphenate.chat.EMClient;
import com.hyphenate.exceptions.HyphenateException;
import com.men.imclent.db.DBUtils;
import com.men.imclent.presenter.ContactPresenter;
import com.men.imclent.utils.ThreadUtils;
import com.men.imclent.view.IContactView;

import java.util.Collection;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

public class ContactPresenterImpl implements ContactPresenter {
    private IContactView contactView;

    public ContactPresenterImpl(IContactView contactView) {
        this.contactView = contactView;
    }

    @Override
    public void initContact() {
        String username = EMClient.getInstance().getCurrentUser();
        List<String> contacts = DBUtils.initContact(username);
        contactView.onInitContact(contacts);
        getContactsFromServer();
    }

    @Override
    public void updateContact() {
        getContactsFromServer();
    }

    @Override
    public void deleteContact(String username) {
        ThreadUtils.runOnNOUI(new Runnable() {
            @Override
            public void run() {
                try {
                    EMClient.getInstance().contactManager().deleteContact(username);
                    ThreadUtils.runOnMainUI(new Runnable() {
                        @Override
                        public void run() {
                            contactView.onDeleteContact(true,null);
                        }
                    });
                } catch (HyphenateException e) {
                    e.printStackTrace();
                    ThreadUtils.runOnMainUI(new Runnable() {
                        @Override
                        public void run() {
                            contactView.onDeleteContact(false,e.getMessage());
                        }
                    });
                }
            }
        });

    }

    private void getContactsFromServer() {
        ThreadUtils.runOnNOUI(new Runnable() {
            @Override
            public void run() {
                try {
                    List<String> contacts = EMClient.getInstance().contactManager().getAllContactsFromServer();

                    Collections.sort(contacts, new Comparator<String>() {
                        @Override
                        public int compare(String o1, String o2) {
                            return o1.compareTo(o2);
                        }
                    });
                    DBUtils.updateContactFromEmServer(EMClient.getInstance().getCurrentUser(),contacts);
                    ThreadUtils.runOnMainUI(new Runnable() {
                        @Override
                        public void run() {
                            contactView.onUpdateContact(contacts,true,null);
                        }
                    });
                } catch (HyphenateException e) {
                    ThreadUtils.runOnMainUI(new Runnable() {
                        @Override
                        public void run() {
                            contactView.onUpdateContact(null,false,null);
                        }
                    });
                }
            }
        });

    }
}
