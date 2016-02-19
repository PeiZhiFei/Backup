package com.runkun.lbsq.busi.activity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import com.runkun.lbsq.busi.R;
import com.runkun.lbsq.busi.fragment.OrderFragment;

public class OrderActivity extends ListBaseActivity
{

    OrderFragment firstFragment;
    OrderFragment secondFragment;
    OrderFragment thirdFragment;
    OrderFragment[] fragments;

    @Override
    protected void onCreate(Bundle paramBundle)
    {
        super.onCreate (paramBundle);
        radioFirst.setText ("每天");
        radioSecond.setText("每周");
        radioThird.setText ("每月");
        setTitles ("订单");
        actionbar_right2.setVisibility (View.VISIBLE);
        actionbar_right2.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                startActivity(new Intent (activity, SearchOrderActivity.class));
            }
        });
        fragments = new OrderFragment[]{firstFragment, secondFragment, thirdFragment};
        loads1();
    }

    @Override
    protected void loads1()
    {
        changeFragment(1);
        changeState(1);
    }


    @Override
    protected void loads2()
    {
        changeFragment(2);
        changeState(2);
    }

    @Override
    protected void loads3()
    {
        changeFragment(3);
        changeState(3);
    }


    private void changeFragment(int i)
    {

        transaction = fragmentManager.beginTransaction();
        transaction.setCustomAnimations(R.anim.activity_in_from_left,
                R.anim.activity_out_to_right);
        int k = i - 1;

        for (int j = 0; j < 3; j++)
        {
            if (j != k && fragments[j] != null)
            {
                transaction.hide(fragments[j]);
            }
        }
        if (fragments[k] == null)
        {
            fragments[k] = OrderFragment.newInstance(i);
            transaction.add(R.id.container, fragments[k]);
        } else
        {
            transaction.show(fragments[k]);
        }
        transaction.commit();
    }


}
