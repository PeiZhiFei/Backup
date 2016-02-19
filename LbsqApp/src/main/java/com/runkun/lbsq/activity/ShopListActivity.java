package com.runkun.lbsq.activity;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.view.View;
import android.view.View.OnClickListener;

import com.runkun.lbsq.R;
import com.runkun.lbsq.fragment.ShopListBaseFragment;
import com.runkun.lbsq.bean.Shop;
import com.runkun.lbsq.test.LocationFactory;
import com.runkun.lbsq.utils.AnimUtil;
import com.runkun.lbsq.utils.MyConstant;
import com.runkun.lbsq.utils.Tools;

public class ShopListActivity extends BaseAcitivity
{
    ShopListBaseFragment shopListFragment;
    public Shop shop;
    public static ShopListActivity instance;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        instance = this;
        setContentView(R.layout.activity_container);
        shopListFragment = LocationFactory.getShoplistFragment();
        FragmentManager fragmentManager = getSupportFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager
                .beginTransaction();
        fragmentTransaction.replace(R.id.container, shopListFragment).commit();

        initActionbar();
        setTitles(Tools.getStr(activity, R.string.TSHOPLIST));
        tint();
        actionbar_left.setText(Tools.getStr(activity, R.string.LOCATING));
        actionbar_left.setTextSize(14);
        actionbar_left.setTextColor(Color.parseColor("#ffffff"));
        actionbar_left.setBackgroundColor(Color.parseColor("#00000000"));
        actionbar_left.setVisibility(View.VISIBLE);
        actionbar_left.setOnClickListener(new OnClickListener()
        {

            @Override
            public void onClick(View v)
            {
                query();
            }
        });
        actionbar_right2.setVisibility(View.VISIBLE);
        actionbar_right2.setBackgroundResource(R.drawable.search_selector);
        actionbar_right2.setOnClickListener(new OnClickListener()
        {

            @Override
            public void onClick(View v)
            {
                Intent intent = new Intent(ShopListActivity.this,
                        SearchShopActivity.class);
                startActivity(intent);
                AnimUtil.animToSlide(activity);
            }
        });
    }

    public void query()
    {
        Intent intent = new Intent(ShopListActivity.this,
                SelectorCity.class);
        startActivityForResult(intent, MyConstant.REQUEST_SHOPLIST);
        AnimUtil.animToSlide(activity);
    }

    public void setTitle(String title)
    {
        setTitles(title);
    }

    public void setPosition(String position)
    {
        actionbar_left.setText(position);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data)
    {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == MyConstant.REQUEST_SHOPLIST
                && resultCode == MyConstant.RESULT_SHOPLIST)
        {
            String s = data.getStringExtra("tests");
            String mail = data.getStringExtra("mail");
            if (s != null)
            {
                actionbar_left.setText(mail);
                shopListFragment.reloads(s);
            }

        }
    }

    @Override
    public void onDestroy()
    {
        super.onDestroy();
        instance = null;
    }
}
