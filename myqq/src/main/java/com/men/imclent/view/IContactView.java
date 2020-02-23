package com.men.imclent.view;

import com.hyphenate.chat.EMConversation;

import java.util.List;

public interface IContactView {
    public void onInitContact(List<String> contact);
    public void onUpdateContact(List<String> contact,boolean isUpdateSucess,String message);
    public void onDeleteContact(boolean isDeleteSuccess, String errorMsg);

}
