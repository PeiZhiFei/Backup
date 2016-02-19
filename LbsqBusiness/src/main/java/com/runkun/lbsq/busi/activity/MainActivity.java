package com.runkun.lbsq.busi.activity;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ImageView;
import android.widget.TextView;

import com.runkun.lbsq.busi.R;
import com.runkun.lbsq.busi.fragment.HomeFragment;
import com.runkun.lbsq.busi.fragment.MyFragment;
import com.runkun.lbsq.busi.util.AnimUtil;
import com.runkun.lbsq.busi.util.MyConstant;
import com.runkun.lbsq.busi.util.Tools;


public class MainActivity extends BaseAcitivity implements
        OnClickListener
{

    private HomeFragment fragment1;
    private MyFragment fragment2;


    private View homeLayout, myLayout, addLayout;
    private ImageView homeImage, myImage;
    private TextView homeText, myText;

    private FragmentManager fragmentManager;
    private int current = -1;
    private int textcolornormal = Color.BLACK;
    private FragmentTransaction transaction;
    private int textcolor = Color.parseColor("#39ac69");

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        initViews();
        tint();
        setSwipeBackEnable(false);
        setTabSelection(0);
    }
    private void initViews()
    {
        homeLayout = findViewById(R.id.home_layout);
        myLayout = findViewById(R.id.my_layout);
        addLayout = findViewById(R.id.add_layout);

        homeImage = (ImageView) findViewById(R.id.home_image);
        myImage = (ImageView) findViewById(R.id.my_image);

        homeText = (TextView) findViewById(R.id.home_text);
        myText = (TextView) findViewById(R.id.my_text);

        homeLayout.setOnClickListener(this);
        myLayout.setOnClickListener(this);
        addLayout.setOnClickListener(this);

        fragmentManager = getSupportFragmentManager();
    }


    @Override
    public void onClick(View v)
    {
        switch (v.getId())
        {
            case R.id.home_layout:
                setTabSelection(0);
                break;
            case R.id.my_layout:
                setTabSelection(1);
                break;
            case R.id.add_layout:
                if (MyConstant.DEBUG){
                    //首页扫一扫功能如果添加新商品无分类信息
                    Intent intent = new Intent(activity, CaptureActivity.class);
                  //  Intent intent = new Intent(activity, ResultActivity.class);
                    //intent.putExtra ("types", "main");
                    startActivity (intent);
                    return;
                }
                startActivity(new Intent(this, CaptureActivity.class));
                AnimUtil.animTo(this);
                break;
        }
    }

    public void setTabSelection(int index)
    {
        switch (index)
        {
            case 0:
                if (current == 0)
                {
                    return;
                }
                current = 0;
                preClick(true);
                homeImage.setImageResource(R.drawable.home_click);
                homeText.setTextColor(textcolor);
                if (fragment1 == null)
                {
                    fragment1 = new HomeFragment();
                    transaction.add(R.id.content, fragment1);
                } else
                {
                    transaction.show(fragment1);
                }
                break;
            case 1:
                if (current == 1)
                {
                    return;
                }
                current = 1;
                preClick(true);
                myImage.setImageResource(R.drawable.my_click);
                myText.setTextColor(textcolor);
                if (fragment2 == null)
                {
                    fragment2 = new MyFragment();
//                    Bundle argBundle = new Bundle();
//                    argBundle.putString("store_id", shop.getStore_id());
//                    fragment2.setArguments(argBundle);
                    transaction.add(R.id.content, fragment2);
                } else
                {
                    transaction.show(fragment2);
                }
                break;
        }
        transaction.commit();
    }

    void preClick(boolean hide)
    {
        clearSelection();
        transaction = fragmentManager.beginTransaction();
//        transaction.setCustomAnimations (R.anim.push_left_in,
//                R.anim.push_right_out);
        transaction.setCustomAnimations(R.anim.zoom_enter,
                R.anim.zoom_exit);
        if (hide)
        {
            hideFragments(transaction);
        }

    }

    private void hideFragments(FragmentTransaction transaction)
    {
        if (fragment1 != null)
        {
            transaction.hide(fragment1);
        }
        if (fragment2 != null)
        {
            transaction.hide(fragment2);
        }
    }

    @SuppressLint("NewApi")
    private void clearSelection()
    {
        homeImage.setImageResource(R.drawable.home_nomal);
        homeText.setTextColor(textcolornormal);
        myImage.setImageResource(R.drawable.my_normal);
        myText.setTextColor(textcolornormal);
        myImage.setImageResource(R.drawable.my_normal);
        myText.setTextColor(textcolornormal);

    }

    private long exitTime = 0;

    @Override
    public void onBackPressed()
    {
        if ((System.currentTimeMillis() - exitTime) > 2000)
        {
            Tools.toast(this, "再按一次退出程序");
            exitTime = System.currentTimeMillis();
        } else
        {
            super.onBackPressed();
        }
    }


}
