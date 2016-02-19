package com.runkun.lbsq.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.FragmentManager;

import com.runkun.lbsq.R;
import com.runkun.lbsq.fragment.ShopCardPayFragment;
import com.runkun.lbsq.utils.Tools;

public class ShopCardActivity extends BaseAcitivity
{
    public static int REQ_CODE_PAY = 0;
    private ShopCardPayFragment payFragment;

    @Override
    protected void onCreate (Bundle arg0)
    {
        super.onCreate (arg0);
        setContentView (R.layout.activity_pay);
        initActionbar ();
        setTitles (Tools.getStr (activity, R.string.TPAY));
        tint ();
        FragmentManager fm = getSupportFragmentManager ();
        payFragment = new ShopCardPayFragment ();
        fm.beginTransaction ().replace (R.id.content, payFragment).commit ();
    }

    @Override
    protected void onActivityResult (int requestCode, int resultCode, Intent data)
    {
        if ( requestCode == REQ_CODE_PAY && resultCode == RESULT_OK )
        {
            String result = data.getStringExtra ("result");
                if ( "true".equals (result) )
                {
                    payFragment.removePaidShop ();
            }
        }
    }

}
