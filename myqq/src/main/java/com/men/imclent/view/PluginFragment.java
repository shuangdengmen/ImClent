package com.men.imclent.view;

import android.app.ProgressDialog;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import com.hyphenate.chat.EMClient;
import com.men.imclent.MainActivity;
import com.men.imclent.R;
import com.men.imclent.presenter.LogoutPresenter;
import com.men.imclent.presenter.impl.LogoutPresenterImpl;

public class PluginFragment extends BaseFragment implements IPluginView {
    private ProgressDialog progressDialog =null;
    private LogoutPresenter presenter;
    private Button button;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_plugin, null);
        progressDialog= new ProgressDialog(getActivity());
        progressDialog.setCancelable(false);
        presenter = new LogoutPresenterImpl(this);
        return view;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {

        super.onViewCreated(view, savedInstanceState);
        button = (Button)view.findViewById(R.id.btn_logout);
        button.setText("退"+ EMClient.getInstance().getCurrentUser() +"出");
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                presenter.logout();
                progressDialog.setMessage("正在退出");
            }
        });
    }


    @Override
    public void logout(boolean isLogout, String msg) {
        MainActivity activity = (MainActivity) getActivity();
        if (isLogout){
            activity.startActivity(LoginActivity.class,true);
        }else {
           activity.showToast("退出异常");
        }
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        if (progressDialog!=null){
            progressDialog.dismiss();
        }
    }
}
