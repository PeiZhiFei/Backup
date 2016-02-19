package com.runkun.lbsq.fragment;

import android.app.Activity;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.runkun.lbsq.activity.MainActivity;

public class MainFirstBaseFragment extends BaseFragment
{
    protected String storeId;

    @Override
    public void onAttach(Activity activity)
    {
        super.onAttach(activity);
        mainActivity = (MainActivity) activity;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState)
    {
        storeId = mainActivity.getShop().getStore_id();
        return super.onCreateView(inflater, container, savedInstanceState);
    }

    MainActivity mainActivity;


}
