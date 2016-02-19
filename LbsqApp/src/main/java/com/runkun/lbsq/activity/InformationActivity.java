package com.runkun.lbsq.activity;

import android.os.Bundle;

import com.runkun.lbsq.R;

public class InformationActivity extends BaseAcitivity
{

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_information);
        tint();
        initActionbar();
        setTitles("优惠券规则");
    }
}
