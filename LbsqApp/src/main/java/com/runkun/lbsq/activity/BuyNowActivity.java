package com.runkun.lbsq.activity;

import android.content.Intent;
import android.os.Bundle;

import com.runkun.lbsq.R;
import com.runkun.lbsq.fragment.BuyNowFragment;
import com.runkun.lbsq.utils.MyConstant;
import com.runkun.lbsq.utils.Tools;

public class BuyNowActivity extends ContainerActivity
{

	public static final int REQUEST_ALI = 0;

    private BuyNowFragment buyNowFragment;

    @Override
    protected void onCreate(Bundle paramBundle)
    {
        super.onCreate(paramBundle);
        buyNowFragment = new BuyNowFragment();
        Bundle argBundle = new Bundle();
        Intent intent = getIntent();
        argBundle.putAll(intent.getExtras());
        buyNowFragment.setArguments(argBundle);
        ft.replace(R.id.container, buyNowFragment).commit();
        setTitles(Tools.getStr(this, R.string.TBUYNOW));
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data)
    {
        if (requestCode == REQUEST_ALI && resultCode == RESULT_OK)
        {
            String result = data.getStringExtra("result");
            buyNowFragment.callback(Boolean.valueOf(result));
        }
        else if (requestCode == MyConstant.REQUEST_COUPON && resultCode == RESULT_OK)
        {
            buyNowFragment.setCoupon(data.getStringExtra("id"), data.getStringExtra("amount"));
        }
        else if (requestCode == MyConstant.REQUEST_COUPON)
        {
            return;
        }
        else
        {
            finish();
        }
    }
}
