package com.runkun.lbsq.busi.activity;

import android.os.Bundle;

import com.runkun.lbsq.busi.R;
import com.runkun.lbsq.busi.fragment.JudgeFragment;

public class JudgeActivity extends ListBaseActivity
{

    JudgeFragment firstFragment;
    JudgeFragment secondFragment;
    JudgeFragment thirdFragment;
    JudgeFragment[] fragments;

    @Override
    protected void onCreate(Bundle paramBundle)
    {
        super.onCreate(paramBundle);
        radioFirst.setText("近一周");
        radioSecond.setText("近一月");
        radioThird.setText("近三月");
        setTitles("口碑");
        fragments = new JudgeFragment[]{firstFragment, secondFragment, thirdFragment};
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
            fragments[k] = JudgeFragment.newInstance(i);
            transaction.add(R.id.container, fragments[k]);
        } else
        {
            transaction.show(fragments[k]);
        }
        transaction.commit();
    }


}
