package com.runkun.lbsq.activity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;

import com.baidu.android.pushservice.PushConstants;
import com.baidu.android.pushservice.PushManager;
import com.runkun.lbsq.R;
import com.runkun.lbsq.utils.AnimUtil;
import com.runkun.lbsq.utils.MyConstant;
import com.runkun.lbsq.utils.Tools;
import com.runkun.lbsq.utils.UpdateManager;
import com.runkun.lbsq.utils.UpdateManager.CancelUpdate;
import com.tendcloud.tenddata.TCAgent;

public class SplashActivity extends BaseAcitivity implements CancelUpdate
{

    boolean isFirstIn = false;
    private static final int GO_HOME = 1000;
    private static final int GO_GUIDE = 1001;
    private static final long SPLASH_MILLIS = 500;

    private Handler handler = new Handler()
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
        setContentView(R.layout.activity_splash);
        init();
    }

    @Override
    protected void onPause()
    {
        super.onPause();
        TCAgent.onPause(this);
    }

    @Override
    protected void onResume()
    {
        super.onResume();
        TCAgent.onResume(this);
    }

    private void init()
    {
        if (Tools.isNetworkAvailable(this))
        {
            UpdateManager updateManager = new UpdateManager(this);
            updateManager.checkUpdate();
        } else
        {
            cancelUpdate();
        }
        try
        {
            PushManager.startWork(getApplicationContext(),
                    PushConstants.LOGIN_TYPE_API_KEY, MyConstant.APPKEY_BAIDUPUSH);
        }
        catch (Exception e){
            e.printStackTrace();

        }
    }

    private void goHome()
    {
        startActivity(new Intent(SplashActivity.this, MainActivity.class));
        AnimUtil.animToSlideFinish(this);
    }

    @Override
    public void cancelUpdate()
    {
        SharedPreferences sp = getSharedPreferences(MyConstant.FILE_NAME,
                MODE_PRIVATE);
        isFirstIn = sp.getBoolean(MyConstant.KEY_GUIDE_FIRST, true);
        if (!isFirstIn)
        {
            handler.sendEmptyMessageDelayed(GO_HOME, SPLASH_MILLIS);
        } else
        {
            handler.sendEmptyMessageDelayed(GO_GUIDE, SPLASH_MILLIS);
        }
    }

    private void goGuide()
    {
        startActivity(new Intent(SplashActivity.this, ShopListActivity.class));
        AnimUtil.animToSlideFinish(this);
    }

}
