package feifei.dataanalysis.base;

import android.annotation.TargetApi;
import android.app.Activity;
import android.content.Context;
import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.LinearLayout;

import com.tendcloud.tenddata.TCAgent;

import feifei.dataanalysis.R;
import feifei.project.util.AnimUtil;
import library.swipebacklayout.SwipeBackActivity;

public class BaseActivity extends SwipeBackActivity

{
    protected Activity activity;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        activity = this;
        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.ICE_CREAM_SANDWICH) {
            setSwipeBackEnable(false);
        }
    }

    public void onResume() {
        super.onResume();
        if (!MyApplication.debug) {
            TCAgent.onResume(this);
            TCAgent.onPageStart(activity, getClass().getSimpleName());
        }
    }

    public void onPause() {
        super.onPause();
        if (!MyApplication.debug) {
            TCAgent.onPause(this);
            TCAgent.onPageEnd(activity, getClass().getSimpleName());
        }
    }

    public static void data(Context context, String s) {
        if (!MyApplication.debug) {
            TCAgent.onEvent(context, s);
        }
    }

    public void tint() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            setTranslucentStatus(true);
            View v = null;
            try {
                v = findViewById(R.id.decorder);
            } catch (Exception e) {

            }
            int resId = getResources().getIdentifier("status_bar_height",
                    "dimen", "android");
            int height = 0;
            if (resId > 0) {
                height = getResources().getDimensionPixelSize(resId);
            }
            if (height > 0) {
                LinearLayout.LayoutParams lp = new LinearLayout.LayoutParams(
                        android.view.ViewGroup.LayoutParams.MATCH_PARENT,
                        height);
                v.setLayoutParams(lp);
            } else {
                v.setVisibility(View.GONE);
            }
        }
    }

    @TargetApi(19)
    protected void setTranslucentStatus(boolean on) {
        Window win = getWindow();
        WindowManager.LayoutParams winParams = win.getAttributes();
        final int bits = WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS;
        if (on) {
            winParams.flags |= bits;
        } else {
            winParams.flags &= ~bits;
        }
        win.setAttributes(winParams);
    }


    @Override
    public void onBackPressed() {
        super.onBackPressed();
        AnimUtil.animBack (this);
    }

}
