package com.men.imclent.utils;

import com.men.imclent.view.BaseFragment;
import com.men.imclent.view.ContactFragment;
import com.men.imclent.view.ConversationFragment;
import com.men.imclent.view.PluginFragment;

public class FragmentFactory {
    private static ConversationFragment conversationFragment=null;
    private static ContactFragment contactFragment=null;
    private static PluginFragment pluginFragment=null;

    public static BaseFragment getFragment(int postion){
        BaseFragment baseFragment=null;
        switch(postion){
            case 0 :
                if (conversationFragment==null){
                    conversationFragment=new ConversationFragment();
                }
                baseFragment=conversationFragment;
                break;
            case 1 :
                if (contactFragment==null){
                    contactFragment=new ContactFragment();
                }
                baseFragment=contactFragment;
                break;
            case 2:
                if(pluginFragment==null){
                    pluginFragment= new PluginFragment();
                }
                baseFragment=pluginFragment;
                break;
        }
        return baseFragment;
    }

}
