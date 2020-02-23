package com.men.imclent;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.view.menu.MenuBuilder;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.FrameLayout;
import android.widget.TextView;

import com.ashokvarma.bottomnavigation.BottomNavigationBar;
import com.ashokvarma.bottomnavigation.BottomNavigationItem;
import com.ashokvarma.bottomnavigation.ShapeBadgeItem;
import com.ashokvarma.bottomnavigation.TextBadgeItem;
import com.hyphenate.chat.EMClient;
import com.hyphenate.chat.EMMessage;
import com.hyphenate.chat.EMTextMessageBody;
import com.men.imclent.utils.FragmentFactory;
import com.men.imclent.view.AddFriendActivity;
import com.men.imclent.view.BaseActivity;
import com.men.imclent.view.BaseFragment;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.util.List;

import butterknife.ButterKnife;
import butterknife.InjectView;

public class MainActivity extends BaseActivity {

    @InjectView(R.id.tb_tool)
    Toolbar tbTool;
    @InjectView(R.id.tv_title)
    TextView tvTitle;
    @InjectView(R.id.fl_container)
    FrameLayout flContainer;
    String titles[] ={"消息","联系人","动态"};
    private TextBadgeItem numberBadgeItem;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ButterKnife.inject(this);
        initView();
        initFirstFragment();
    }

    private void initFirstFragment() {
        List<Fragment> fragments = getSupportFragmentManager().getFragments();
        FragmentTransaction fragmentTransaction = getSupportFragmentManager().beginTransaction();
        if (fragments!=null&&fragments.size()>0){
            for (int i = 0; i < fragments.size(); i++) {
                fragmentTransaction.remove(fragments.get(i));
                fragmentTransaction.commit();
            }
        }

        BaseFragment firstFragment = FragmentFactory.getFragment(0);
        fragmentTransaction.add(R.id.fl_container,firstFragment,0+"");
        fragmentTransaction.commit();
    }

    private void initView() {
        tbTool.setTitle("");
        setSupportActionBar(tbTool);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        BottomNavigationBar bottomNavigationBar = (BottomNavigationBar) findViewById(R.id.bottom_navigation_bar);
//        TextBadgeItem numberBadgeItem = new TextBadgeItem();
//        ShapeBadgeItem shapeBadgeItem = new ShapeBadgeItem();
    /*    numberBadgeItem.setBorderWidth(4)
                .setBackgroundColorResource(R.color.blue)
                .setText("" + lastSelectedPosition)
                .setHideOnSelect(autoHide.isChecked());

        shapeBadgeItem.setShape(ShapeBadgeItem.SHAPE_STAR_5_VERTICES)
                .setShapeColorResource(R.color.teal)
                .setGravity(Gravity.TOP | Gravity.END)
                .setHideOnSelect(autoHide.isChecked());*/
        BottomNavigationItem conversationItem = new BottomNavigationItem(R.mipmap.conversation_selected_2, "消息");
        numberBadgeItem = new TextBadgeItem();
        updateBadgeItem();
        conversationItem.setBadgeItem(numberBadgeItem);

        bottomNavigationBar.addItem(conversationItem);

        BottomNavigationItem item= new BottomNavigationItem(R.mipmap.conversation_selected_2, "消息");
        item = new BottomNavigationItem(R.mipmap.contact_selected_2, "联系人");
        bottomNavigationBar.addItem(item);
        item = new BottomNavigationItem(R.mipmap.plugin_selected_2, "动态");
        bottomNavigationBar.addItem(item);
        bottomNavigationBar.setFirstSelectedPosition(0);
        bottomNavigationBar.setActiveColor(R.color.btn_normal);
        bottomNavigationBar.setInActiveColor(R.color.inactive);

        bottomNavigationBar.initialise();


        bottomNavigationBar.setTabSelectedListener(new BottomNavigationBar.OnTabSelectedListener() {
            @Override
            public void onTabSelected(int position) {
                FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
                BaseFragment fragment =FragmentFactory.getFragment(position);
                if (fragment.isAdded()){
                    transaction.show(fragment).commit();
                }else {
                    transaction.add(R.id.fl_container,fragment,position+"").commit();
                }
                tvTitle.setText(titles[position]);
            }

            @Override
            public void onTabUnselected(int position) {
                getSupportFragmentManager().beginTransaction().hide(FragmentFactory.getFragment(position)).commit();
            }

            @Override
            public void onTabReselected(int position) {

            }
        });
    }

    public void updateBadgeItem() {
        int unreadMessageCount = EMClient.getInstance().chatManager().getUnreadMessageCount();
        if (unreadMessageCount == 0) {
            numberBadgeItem.hide(true);
        } else if (unreadMessageCount > 99) {
            numberBadgeItem.show(true);
            numberBadgeItem.setText("99");
        } else {
            numberBadgeItem.show(true);
            numberBadgeItem.setText(String.valueOf(unreadMessageCount));
        }
    }

    @SuppressLint("RestrictedApi")
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu, menu);
        MenuBuilder menuBuilder = (MenuBuilder) menu;
        menuBuilder.setOptionalIconsVisible(true);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int itemId = item.getItemId();
        switch (itemId) {
            case (R.id.menu_item_addFriend):
//                showToast("添加好友");
                startActivity(AddFriendActivity.class,false);
                break;
            case (R.id.menu_item_scan):
                showToast("扫一扫");

                break;
            case (R.id.menu_item_about):
                showToast("关于");

                break;
            case (android.R.id.home):
                finish();
                break;
        }
        return super.onOptionsItemSelected(item);
    }

    @Subscribe(threadMode= ThreadMode.MAIN )
    public void onGetReceiveMessages(List<EMMessage> list) {
        updateBadgeItem();
    }

    @Override
    protected void onResume() {
        super.onResume();
        updateBadgeItem();
    }
}
