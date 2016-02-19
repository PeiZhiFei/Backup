package com.runkun.lbsq.busi.activity;

import android.annotation.TargetApi;
import android.app.Activity;
import android.app.Dialog;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.Gravity;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.runkun.lbsq.busi.R;
import com.runkun.lbsq.busi.util.AnimUtil;

import library.swipebacklayout.SwipeBackActivity;


public class BaseAcitivity extends SwipeBackActivity
{
    protected Activity activity;

    protected TextView actionbar_title;
    protected View actionBar;
    protected Button actionbar_right2;
    protected Button actionbar_left;


    @Override
    protected void onCreate(@Nullable Bundle arg0)
    {
        super.onCreate(arg0);
        activity = this;
//        MyExceptionDialog.getInstance().setContext(this);
        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.ICE_CREAM_SANDWICH || Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP)
        {
            setSwipeBackEnable(false);
        }
    }


    @Override
    protected void onResume()
    {
        super.onResume();
    }

    @Override
    protected void onStart()
    {
        super.onStart();
    }


    @Override
    protected void onStop()
    {
        super.onStop();
    }


    @Override
    protected void onPause()
    {
        super.onPause();
    }


    @Override
    protected void onDestroy()
    {
        super.onDestroy();
    }

    public void tint()
    {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT)
        {
            setTranslucentStatus(true);
            View v = null;
            try
            {
                v = findViewById(R.id.decorder);
            } catch (Exception e)
            {

            }
            int resId = getResources().getIdentifier("status_bar_height",
                    "dimen", "android");
            int height = 0;
            if (resId > 0)
            {
                height = getResources().getDimensionPixelSize(resId);
            }
            if (height > 0)
            {
                LinearLayout.LayoutParams lp = new LinearLayout.LayoutParams(
                        android.view.ViewGroup.LayoutParams.MATCH_PARENT,
                        height);
                v.setLayoutParams(lp);
            } else
            {
                v.setVisibility(View.GONE);
            }
        }
    }

    @TargetApi(19)
    protected void setTranslucentStatus(boolean on)
    {
        Window win = getWindow();
        WindowManager.LayoutParams winParams = win.getAttributes();
        final int bits = WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS;
        if (on)
        {
            winParams.flags |= bits;
        } else
        {
            winParams.flags &= ~bits;
        }
        win.setAttributes(winParams);
    }


    protected void initActionbar()
    {
        actionBar = findViewById(R.id.actionlayout);
        actionbar_title = (TextView) findViewById(R.id.titlebar);
        actionbar_right2 = (Button) findViewById(R.id.actionbar_right2);
        actionbar_left = (Button) findViewById(R.id.actionbar_left);
        actionbar_right2.setVisibility(View.INVISIBLE);
    }

    public void back(View view)
    {
        finish();
    }

    public void setTitles(String title)
    {
        actionbar_title.setText(title);
    }

    public TextView getTitlebar()
    {
        return actionbar_title;
    }

    public void setTitlebar(TextView titlebar)
    {
        this.actionbar_title = titlebar;
    }

    public Button getActionbar_right()
    {
        return actionbar_right2;
    }

    public void setActionbar_right(Button actionbar_right)
    {
        this.actionbar_right2 = actionbar_right;
    }

    public Button getActionbar_left()
    {
        return actionbar_left;
    }

    public void setActionbar_left(Button actionbar_left)
    {
        this.actionbar_left = actionbar_left;
    }

    @Override
    public void onBackPressed()
    {
        super.onBackPressed();
        AnimUtil.animBack(this);
    }

    protected Dialog dialog;
    private boolean open = false;


    public void dialogInit()
    {
        dialog = new Dialog(this, R.style.dialog_loading_style);
        dialog.getWindow().getAttributes().gravity = Gravity.CENTER;
        dialog.setContentView(R.layout.dialog_lbsq);
        dialog.setCancelable(false);
    }

    public synchronized void dialogDismiss()
    {
        if (open && dialog != null)
        {
            dialog.dismiss();
            open = false;
        }
    }

    public synchronized void dialogProgress(String text)
    {
        if (!open && dialog != null)
        {
            ImageView image = (ImageView) dialog
                    .findViewById(R.id.loadingImageView);
            image.startAnimation(AnimUtil.getDialogRotateAnimation());
            TextView textView = (TextView) dialog
                    .findViewById(R.id.id_tv_loadingmsg);
            if (text != null)
            {
                textView.setText(text);
            }
            dialog.show();
            open = true;
        }
    }
}
