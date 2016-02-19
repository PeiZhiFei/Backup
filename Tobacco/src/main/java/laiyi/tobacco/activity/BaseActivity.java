package laiyi.tobacco.activity;

import android.annotation.TargetApi;
import android.app.Activity;
import android.app.Dialog;
import android.os.Build;
import android.os.Bundle;
import android.view.Gravity;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import feifei.library.util.AnimUtil;
import feifei.library.util.Tools;
import feifei.library.view.NewtonCradleLoading;
import laiyi.tobacco.R;
import library.swipebacklayout.SwipeBackActivity;

public class BaseActivity extends SwipeBackActivity

{
    protected Activity activity;
    protected TextView actionbar_title;
    protected View actionBar;
    protected Button actionbar_right2;
    protected Button actionbar_left;


    @Override
    protected void onCreate (Bundle savedInstanceState)
    {
        super.onCreate (savedInstanceState);
        activity = this;
        if ( Build.VERSION.SDK_INT < Build.VERSION_CODES.ICE_CREAM_SANDWICH )
        {
            setSwipeBackEnable (false);
        }
    }

    public void tint ()
    {
        if ( Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT )
        {
            setTranslucentStatus (true);
            View v = null;
            try
            {
                v = findViewById (R.id.decorder);
            } catch (Exception e)
            {

            }
            int resId = getResources ().getIdentifier ("status_bar_height",
                    "dimen", "android");
            int height = 0;
            if ( resId > 0 )
            {
                height = getResources ().getDimensionPixelSize (resId);
            }
            if ( height > 0 )
            {
                LinearLayout.LayoutParams lp = new LinearLayout.LayoutParams (
                        android.view.ViewGroup.LayoutParams.MATCH_PARENT,
                        height);
                v.setLayoutParams (lp);
            } else
            {
                v.setVisibility (View.GONE);
            }
        }
    }

    @TargetApi(19)
    protected void setTranslucentStatus (boolean on)
    {
        Window win = getWindow ();
        WindowManager.LayoutParams winParams = win.getAttributes ();
        final int bits = WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS;
        if ( on )
        {
            winParams.flags |= bits;
        } else
        {
            winParams.flags &= ~bits;
        }
        win.setAttributes (winParams);
    }


    @Override
    public void onBackPressed ()
    {
        super.onBackPressed ();
        AnimUtil.animBack (this);
    }

    protected void initActionbar ()
    {
        actionBar = findViewById (R.id.actionlayout);
        actionbar_title = (TextView) findViewById (R.id.titlebar);
        actionbar_right2 = (Button) findViewById (R.id.actionbar_right2);
        actionbar_left = (Button) findViewById (R.id.actionbar_left);
        actionbar_right2.setVisibility (View.INVISIBLE);
    }

    public void back (View view)
    {
        finish ();
    }

    public void setTitles (String title)
    {
        actionbar_title.setText (title);
    }

    public void setLeftBack ()
    {
        actionbar_left.setBackgroundResource (R.drawable.item_left);
        actionbar_left.setCompoundDrawables (null, null, null, null);
        actionbar_left.setVisibility (View.VISIBLE);
        RelativeLayout.LayoutParams layoutParams = new RelativeLayout.LayoutParams (Tools.dp2px (activity, 30), Tools.dp2px (activity, 30));
        layoutParams.addRule (RelativeLayout.CENTER_VERTICAL);
        layoutParams.leftMargin = Tools.dp2px (activity, 15);
        actionbar_left.setLayoutParams (layoutParams);
        actionbar_left.setOnClickListener (new View.OnClickListener ()
        {

            @Override
            public void onClick (View v)
            {
                onBackPressed ();
            }
        });
    }

    public TextView getTitlebar ()
    {
        return actionbar_title;
    }

    public void setTitlebar (TextView titlebar)
    {
        this.actionbar_title = titlebar;
    }

    public Button getActionbar_right ()
    {
        return actionbar_right2;
    }

    public void setActionbar_right (Button actionbar_right)
    {
        this.actionbar_right2 = actionbar_right;
    }

    public Button getActionbar_left ()
    {
        return actionbar_left;
    }

    public void setActionbar_left (Button actionbar_left)
    {
        this.actionbar_left = actionbar_left;
    }


    protected Dialog dialogs;
    private boolean open = false;
    private NewtonCradleLoading newtonCradleLoading;

    public void dialogInit ()
    {
        if ( activity != null )
        {
            dialogs = new Dialog (activity, R.style.dialog_loading_style);
            dialogs.getWindow ().getAttributes ().gravity = Gravity.CENTER;
            dialogs.setContentView (R.layout.dialog);
            dialogs.setCancelable (false);
        }
    }

    public synchronized void dialogDismiss ()
    {
        if ( open && dialogs != null )
        {
            try
            {
                newtonCradleLoading.stop ();
            } catch (Exception e)
            {
                e.printStackTrace ();
            }
            dialogs.dismiss ();
            open = false;
        }
    }

    public synchronized void dialogProgress ()
    {
        if ( !open && dialogs != null )
        {
            newtonCradleLoading = (NewtonCradleLoading) dialogs.findViewById (R.id.newton_cradle_loading);
            newtonCradleLoading.start ();
            dialogs.show ();
            open = true;
        }
    }

}
