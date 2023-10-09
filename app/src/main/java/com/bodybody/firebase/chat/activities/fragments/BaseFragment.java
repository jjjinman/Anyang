package com.bodybody.firebase.chat.activities.fragments;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.os.Bundle;

import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.RecyclerView;

import com.bodybody.firebase.chat.activities.R;
import com.bodybody.firebase.chat.activities.adapters.UserAdapters;
import com.bodybody.firebase.chat.activities.managers.Screens;
import com.bodybody.firebase.chat.activities.managers.Utils;
import com.bodybody.firebase.chat.activities.models.User;

import java.util.ArrayList;


//Activity와 Fragment에서 Data Binding을 선언하다보면
//중복되는 코드가 생겨나므로 중복을 피하기 위해 BaseFragment를 생성해 상속받도록 했습니다.
//다른 Activity와 Fragment에서 상속해 사용할 수 있도록 했습니다.
public class BaseFragment extends Fragment {

    public RecyclerView mRecyclerView;
    public ArrayList<User> mUsers;
    public UserAdapters userAdapters;
    public Screens screens;
    public Activity mActivity;
    public Context mContext;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mActivity = getActivity();
        screens = new Screens(getActivity());
        mContext = getContext();
    }

    private ProgressDialog pd = null;

    public void showProgress() {
        try {
            if (pd == null) {
                pd = new ProgressDialog(mActivity);
            }
            pd.setMessage(getString(R.string.msg_please_wait));
            pd.show();
        } catch (Exception e) {
            Utils.getErrors(e);
        }
    }

    public void hideProgress() {
        try {
            if (pd != null) {
                pd.dismiss();
                pd = null;
            }
        } catch (Exception e) {
            Utils.getErrors(e);
        }
    }
}
