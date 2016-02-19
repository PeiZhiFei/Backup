package com.runkun.lbsq.activity;

import android.os.Bundle;

import com.runkun.lbsq.R;
import com.runkun.lbsq.fragment.GoodsDetailFragment;
import com.runkun.lbsq.utils.Tools;

public class GoodDetailActivity extends ContainerActivity
{
    @Override
    protected void onCreate (Bundle paramBundle)
    {
        super.onCreate (paramBundle);
        GoodsDetailFragment gd = new GoodsDetailFragment ();
        Bundle argBundle = new Bundle ();
        argBundle.putString ("goods_id", getIntent ().getStringExtra ("goods_id"));
        gd.setArguments (argBundle);
        ft.replace (R.id.container, gd).commit ();
        setTitles (Tools.getStr (this, R.string.TGOODDETAIL));
    }
}
