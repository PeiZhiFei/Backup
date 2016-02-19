package com.runkun.lbsq.activity;

import android.os.Bundle;
import android.support.v4.app.FragmentManager;

import com.runkun.lbsq.R;
import com.runkun.lbsq.fragment.ProductsFragment;

public class ProductAcitivity extends BaseAcitivity
{
    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        //全屏效果的另一中实现
        setContentView(R.layout.activity_product);
        tint();
        FragmentManager fm = getSupportFragmentManager();
        int type = getIntent().getIntExtra("type", 0);
        Bundle bundle = new Bundle();
        bundle.putInt("type", type);
        switch (type)
        {
            case 1:
                ProductsFragment duckNeckFragment = ProductsFragment.newInstance(1);
                duckNeckFragment.setArguments(bundle);
                fm.beginTransaction().replace(R.id.container, duckNeckFragment).commit();
                break;
            case 2:
                ProductsFragment lobsterFragment = ProductsFragment.newInstance(2);
                lobsterFragment.setArguments(bundle);
                fm.beginTransaction().replace(R.id.container, lobsterFragment).commit();
                break;
        }
    }
}
