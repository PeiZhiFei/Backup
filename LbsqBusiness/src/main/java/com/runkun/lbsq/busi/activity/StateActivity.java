package com.runkun.lbsq.busi.activity;

import android.graphics.Color;
import android.os.Bundle;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.view.View;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.RadioGroup.OnCheckedChangeListener;

import com.runkun.lbsq.busi.R;


public abstract class StateActivity extends BaseAcitivity {

    protected int type = 1;
    protected int red;

    protected FragmentManager fragmentManager;
    protected FragmentTransaction transaction;
    RadioButton radioFirst;
    RadioButton radioSecond;
    RadioButton radioThird;
    RadioButton radioForth;
    RadioGroup myCollectRg;
    View mcIndicator1;
    View mcIndicator2;
    View mcIndicator3;
    View mcIndicator4;

    RadioButton[] radioButtons;
    View[] indicators;

    @Override
    protected void onCreate(Bundle arg0) {
        super.onCreate(arg0);
        setContentView(R.layout.activity_state);

        myCollectRg = (RadioGroup) findViewById(R.id.my_collect_rg);
        mcIndicator1 = findViewById(R.id.mc_indicator_1);
        mcIndicator2 = findViewById(R.id.mc_indicator_2);
        mcIndicator3 = findViewById(R.id.mc_indicator_3);
        mcIndicator4 = findViewById(R.id.mc_indicator_4);
        radioFirst = (RadioButton) findViewById(R.id.radio_first);
        radioSecond = (RadioButton) findViewById(R.id.radio_second);
        radioThird = (RadioButton) findViewById(R.id.radio_third);
        radioForth = (RadioButton) findViewById(R.id.radio_forth);
        radioButtons = new RadioButton[]{radioFirst, radioSecond, radioThird, radioForth};
        indicators = new View[]{mcIndicator1, mcIndicator2, mcIndicator3, mcIndicator4};
        tint();
        initActionbar();
        red = getResources().getColor(R.color.main_green);
        fragmentManager = getSupportFragmentManager();
        myCollectRg.setOnCheckedChangeListener(new OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                switch (checkedId) {
                    case R.id.radio_first:
                        loads1();
                        break;
                    case R.id.radio_second:
                        loads2();
                        break;
                    case R.id.radio_third:
                        loads3();
                        break;
                    case R.id.radio_forth:
                        loads4();
                        break;
                }
            }
        });
    }

    protected void changeState(int i) {
        int k = i - 1;
        for (int j = 0; j < 4; j++) {
            if (k == j) {
                indicators[j].setBackgroundColor(red);
                radioButtons[j].setTextColor(Color.BLACK);
            } else {
                indicators[j].setBackgroundColor(Color.argb(0, 0, 0, 0));
                radioButtons[j].setTextColor(Color.parseColor("#ADADAD"));
            }
        }
        type = i;
    }

    protected abstract void loads1();

    protected abstract void loads2();

    protected abstract void loads3();

    protected abstract void loads4();

}
