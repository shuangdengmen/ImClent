package com.men.imclent.view;

import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.inputmethod.InputMethod;
import android.view.inputmethod.InputMethodManager;
import android.widget.ImageView;
import android.widget.SearchView;
import android.widget.TextView;

import com.men.imclent.R;
import com.men.imclent.adater.AddFriendAdapter;
import com.men.imclent.model.User;
import com.men.imclent.presenter.AddFriendPresenter;
import com.men.imclent.presenter.impl.AddFriendPresenterImpl;
import com.men.imclent.utils.ToastUtils;

import java.util.List;

import butterknife.ButterKnife;
import butterknife.InjectView;

public class AddFriendActivity extends BaseActivity implements IAddFriendView {

    @InjectView(R.id.tv_title)
    TextView tvTitle;
    @InjectView(R.id.tb_tool)
    Toolbar tbTool;
    @InjectView(R.id.rv_addFriend)
    RecyclerView rvAddFriend;
    @InjectView(R.id.iv_nodata)
    ImageView ivNoData;
    private SearchView searchView;
    private AddFriendPresenter presenter = null;
    private AddFriendAdapter adapter;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_friend);
        ButterKnife.inject(this);
        presenter = new AddFriendPresenterImpl(this);

        initToolBar();
    }

    private void initToolBar() {
        tbTool.setTitle("");
        setSupportActionBar(tbTool);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.addfriendmenu, menu);
        MenuItem menuItem = menu.findItem(R.id.menu_search);
        searchView = (SearchView) menuItem.getActionView();
        searchView.setQueryHint("搜索好友");
        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                if (adapter == null) {
                    adapter = new AddFriendAdapter(null, null);
                    rvAddFriend.setLayoutManager(new LinearLayoutManager(getApplicationContext()));
                    rvAddFriend.setAdapter(adapter);
                    adapter.setOnAddFriendClickListener(new AddFriendAdapter.OnAddFriendClickListener() {
                        @Override
                        public void onClick(View v, String username) {
                            presenter.addFriend(username);
                        }
                    });
                }

                presenter.searchFriend(query);

                InputMethodManager inputMethodManager = getSystemService(InputMethodManager.class);
                inputMethodManager.hideSoftInputFromWindow(rvAddFriend.getWindowToken(), 0);
                return true;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                if (!TextUtils.isEmpty(newText)) {
                    showToast(newText);
                    return true;
                }
                return false;
            }
        });

        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch(item.getItemId()){
            case (android.R.id.home) :
                finish();
                break;
            default:
                break;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onQuerySuccess(List<User> list, List<String> users, boolean isSuccess, String errorMsg) {
        if (isSuccess) {
            adapter.setContacts(users);
            adapter.setUsers(list);
            adapter.notifyDataSetChanged();
            ivNoData.setVisibility(View.GONE);
            rvAddFriend.setVisibility(View.VISIBLE);
        } else {
            showToast("错误："+errorMsg);
            ivNoData.setVisibility(View.VISIBLE);
            rvAddFriend.setVisibility(View.GONE);
        }
    }

    @Override
    public void onGetAddFriendResult(boolean b, String message) {
        if (b) {
            showToast("添加好友成功");
        } else {
            showToast(message);
        }
    }
}
