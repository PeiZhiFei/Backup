package com.runkun.lbsq.busi.activity;

import android.os.Bundle;

import com.runkun.lbsq.busi.R;
import com.runkun.lbsq.busi.fragment.OrderFragment;

public class TestActivity extends StateActivity {

    OrderFragment firstFragment;
    OrderFragment secondFragment;
    OrderFragment thirdFragment;
    OrderFragment forthFragment;
    OrderFragment[] fragments;

    @Override
    protected void onCreate(Bundle paramBundle)
    {
        super.onCreate(paramBundle);
        setTitles ("实时订单");
        fragments = new OrderFragment[]{firstFragment, secondFragment, thirdFragment,forthFragment};
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

    @Override
    protected void loads4() {
        changeFragment(4);
        changeState(4);
    }


    private void changeFragment(int i)
    {

        transaction = fragmentManager.beginTransaction();
        transaction.setCustomAnimations(R.anim.activity_in_from_left,
                R.anim.activity_out_to_right);
        int k = i - 1;

        for (int j = 0; j < 4; j++)
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

