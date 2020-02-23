package com.men.imclent.event;

public class ContactChangeEvent {

    public String username;
    public boolean isAdded;

    public ContactChangeEvent(String username, boolean isAdded) {
        this.username = username;
        this.isAdded = isAdded;
    }
}
