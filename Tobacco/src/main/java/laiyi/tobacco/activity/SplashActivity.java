package laiyi.tobacco.activity;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;

import feifei.library.util.AnimUtil;
import laiyi.tobacco.R;

public class SplashActivity extends BaseActivity
{

    @Override
    protected void onCreate (Bundle savedInstanceState)
    {
        super.onCreate (savedInstanceState);
        setContentView (R.layout.activity_splash);
        tint ();
        setSwipeBackEnable (false);

        new Handler ().postDelayed (new Runnable ()
        {
            @Override
            public void run ()
            {
                startActivity (new Intent (activity, LoginActivity.class));
                AnimUtil.animToFinish (activity);
            }
        }, 2000);
    }

}
