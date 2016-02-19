package com.runkun.lbsq.test;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.runkun.lbsq.R;
import com.runkun.lbsq.fragment.ShopListBaseFragment;
import com.runkun.lbsq.utils.Tools;

public class TestShopListFragment extends ShopListBaseFragment
{

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState)
    {
        super.onCreateView(inflater, container, savedInstanceState);
        // la = 36.690913;
        // lo = 117.097955;
        // ios
        la = 36.684538;
        lo = 117.085418;
        reloadPosition(null, false);
        shopListActivity.setPosition(Tools.getStr(shopListActivity,
                R.string.jinanshi));
        return view;
    }

}
