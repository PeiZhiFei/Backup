package com.runkun.lbsq.test;

import android.os.Bundle;

import com.runkun.lbsq.R;
import com.runkun.lbsq.activity.ContainerActivity;
import com.runkun.lbsq.fragment.StickyHeaderFragment2;

public class TestActivity extends ContainerActivity
{

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        ft.replace(R.id.container, new StickyHeaderFragment2 ()).commit();
        setTitles("商品分类");
    }

}
