package com.runkun.lbsq.busi.activity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;

import com.runkun.lbsq.busi.R;
import com.runkun.lbsq.busi.util.AnimUtil;
import com.runkun.lbsq.busi.util.MyConstant;
import com.runkun.lbsq.busi.util.Tools;
import com.runkun.lbsq.busi.util.UpdateManager;

import feifei.project.util.ConfigUtil;

/**
 * 作者    zhoujianguo
 * 时间    2015/8/19
 * 文件    lbsq_busi
 * 描述    启动画面
 * (1)判断是否是首次加载应用--采取读取SharedPreferences的方法
 * (2)是，则进入GuideActivity；否，则进入MainActivity
 * (3)3s后执行
 */
public class SplashActivity extends BaseAcitivity implements UpdateManager.CancelUpdate

{
    boolean isFirstIn = false;
    private static final int GO_HOME = 1000;
    private static final int GO_GUIDE = 1001;
    // 延迟3秒
    private static final long SPLASH_DELAY_MILLIS = 500;
    private static final String SHAREDPREFERENCES_NAME = "first_pref";
    /**
     * Handler:跳转到不同界面
     */
    private Handler mHandler = new Handler()
    {
        @Override
        public void handleMessage(Message msg)
        {
            switch (msg.what)
            {
                case GO_HOME:
                    goHome();
                    break;
                case GO_GUIDE:
                    goGuide();
                    break;
            }
            super.handleMessage(msg);
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView (R.layout.activity_splash);
        setSwipeBackEnable (false);
        init ();
        dialogInit ();
    }

    private void init()
    {
        if (MyConstant.DEBUG)
        {
            cancelUpdate();
            return;
        }
        //todo  检查更新
        //检查更新
        if (Tools.isNetworkAvailable(this))
        {
            UpdateManager updateManager = new UpdateManager(this);
            updateManager.checkUpdate();
        } else
        {
            cancelUpdate();
        }
    }

    private void goHome()
    {
        Class<?> c;
        String storeId = ConfigUtil.readString (activity, MyConstant.KEY_STOREID, "");
        if (!Tools.isEmpty(storeId))
        {
            c = MainActivity.class;
        } else
        {
            c = LoginActivity.class;
        }
        Intent intent = new Intent(this, c);
        startActivity(intent);
        AnimUtil.animToFinish(this);

    }

    private void goGuide()
    {
        Intent intent = new Intent(this, GuideActivity.class);
        startActivity(intent);
        AnimUtil.animToFinish(this);
    }

    @Override
    public void cancelUpdate()
    {
        // 使用SharedPreferences来记录程序的使用次数
        SharedPreferences preferences = getSharedPreferences(
                SHAREDPREFERENCES_NAME, MODE_PRIVATE);
        // 取得相应的值，如果没有该值，说明还未写入，用true作为默认值
        isFirstIn = preferences.getBoolean("isFirstIn", true);

        // 判断程序与第几次运行，如果是第一次运行则跳转到引导界面，否则跳转到主界面
        if (!isFirstIn)
        {
            // 使用Handler的postDelayed方法，3秒后执行跳转到MainActivity
            mHandler.sendEmptyMessageDelayed(GO_HOME, SPLASH_DELAY_MILLIS);
        } else
        {
            mHandler.sendEmptyMessageDelayed(GO_HOME, SPLASH_DELAY_MILLIS);
        }

    }
}
