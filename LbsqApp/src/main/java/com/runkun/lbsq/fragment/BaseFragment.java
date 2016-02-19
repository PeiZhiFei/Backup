package com.runkun.lbsq.fragment;

import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.runkun.lbsq.R;
import com.runkun.lbsq.utils.AnimUtil;
import com.tendcloud.tenddata.TCAgent;

public class BaseFragment extends Fragment
{
    Context fragment;

    @Override
    public void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        // must written in oncreate
        fragment = getActivity();
    }

    @Override
    public View onCreateView (LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState)
    {
        return super.onCreateView (inflater, container, savedInstanceState);
    }

    @Override
    public void onPause()
    {
        TCAgent.onPageEnd(getActivity(), getClass().getName());
        super.onPause();
    }

    @Override
    public void onStart()
    {
        TCAgent.onPageStart(getActivity(), getClass().getName());
        super.onStart();
    }

    protected Dialog dialog;
    private boolean open = false;


    public void dialogInit()
    {
        if (getActivity() != null)
        {
            dialog = new Dialog(getActivity(), R.style.dialog_loading_style);
            dialog.getWindow().getAttributes().gravity = Gravity.CENTER;
            dialog.setContentView(R.layout.dialog_lbsq);
            dialog.setCancelable(true);
        }
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
