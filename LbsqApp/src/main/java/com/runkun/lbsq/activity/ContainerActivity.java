package com.runkun.lbsq.activity;

import android.os.Bundle;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;

import com.runkun.lbsq.R;

public class ContainerActivity extends BaseAcitivity
{
    protected FragmentTransaction ft;

    @Override
    protected void onCreate (Bundle savedInstanceState)
    {
        super.onCreate (savedInstanceState);
        setContentView (R.layout.activity_container);
        initActionbar ();
        FragmentManager fm = getSupportFragmentManager ();
        ft = fm.beginTransaction ();
    }

    @Override
    protected void onStart ()
    {
        super.onStart ();
        tint ();
    }
}
