package com.runkun.lbsq.activity;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;

import com.lidroid.xutils.ViewUtils;
import com.lidroid.xutils.exception.HttpException;
import com.lidroid.xutils.http.RequestParams;
import com.lidroid.xutils.http.ResponseInfo;
import com.lidroid.xutils.http.callback.RequestCallBack;
import com.lidroid.xutils.view.annotation.ViewInject;
import com.runkun.lbsq.R;
import com.runkun.lbsq.bean.Address;
import com.runkun.lbsq.utils.AnimUtil;
import com.runkun.lbsq.utils.HttpHelper;
import com.runkun.lbsq.utils.MyConstant;
import com.runkun.lbsq.utils.Tools;

import org.json.JSONException;
import org.json.JSONObject;

import feifei.project.util.ConfigUtil;
import feifei.project.view.ClearEditText;

public class AddAddressActivity extends BaseAcitivity implements
        OnClickListener
{
    @ViewInject(R.id.username)
    private ClearEditText userName;

    @ViewInject(R.id.phone)
    private ClearEditText phone;

    @ViewInject(R.id.address)
    private ClearEditText address;

    @ViewInject(R.id.mark)
    private ClearEditText mark;

    @ViewInject(R.id.save)
    private Button save;
    // @ViewInject(R.id.voice)
    // private Button voice;
    // ClearEditText[] ed;

    private String mName;
    private String mPhone;
    private String mAddress;
    private String mMark;

    private String memberId;

    private boolean add;
    private Address myAddress;

    @Override
    protected void onCreate (Bundle arg0)
    {
        super.onCreate (arg0);
        setContentView (R.layout.activity_addaddress);
        ViewUtils.inject (this);
        tint ();
        dialogInit();
        initActionbar ();
        Intent intent = getIntent ();
        myAddress = intent.getParcelableExtra ("address");
        if ( myAddress == null )
        {
            setTitles (Tools.getStr (activity, R.string.TADSADD));
            add = true;
        } else
        {
            setTitles (Tools.getStr (activity, R.string.TADSEDIT));
            add = false;
            userName.setText (myAddress.getConsigner ());
            phone.setText (myAddress.getMobile ());
            address.setText (myAddress.getAddress ());
            mark.setText (myAddress.getRemark ());
        }
        save.setOnClickListener (this);
        new Handler ().postDelayed (new Runnable ()
        {

            @Override
            public void run ()
            {
                Tools.keyboardShow (userName);

            }
        }, 1000);

        // ed = new ClearEditText[] { userName, phone, address, mark };
    }


    // int p = 0;
    // for (int i = 0; i < ed.length; i++) {
    // if (ed[i].hasFocus()) {
    // p = i;
    // }
    // }
    // ed[p].setText(resultBuffer.toString());
    // ed[p].setSelection(ed[p].length());
    //
    // }

    @Override
    protected void onResume ()
    {
        super.onResume ();
        memberId = ConfigUtil.readString (activity, MyConstant.KEY_MEMBERID, "");
    }

    @Override
    public void onClick (View v)
    {
        switch (v.getId ())
        {
            case R.id.save:
                mName = userName.getText ().toString ().trim ();
                mAddress = address.getText ().toString ().trim ();
                mPhone = phone.getText ().toString ().trim ();
                mMark = mark.getText ().toString ().trim ();
                if ( Tools.isEmpty (mName) )
                {
                    Tools.toast (this,
                            Tools.getStr (activity, R.string.PLEASEUSERNAME));
                    AnimUtil.animShakeText (userName);
                } else if ( !Tools.checkPhone (mPhone) )
                {
                    Tools.toast (this, Tools.getStr (activity, R.string.PHONEWRONG));
                    AnimUtil.animShakeText (phone);
                } else if ( Tools.isEmpty (mAddress) )
                {
                    Tools.toast (this,
                            Tools.getStr (activity, R.string.PLEASEADDRESS));
                    AnimUtil.animShakeText (address);
                } else
                {
                    AddOrChangeAddress ();
                }
                break;
        }
    }

    private void AddOrChangeAddress ()
    {
        RequestParams rp = new RequestParams ();
        rp.addQueryStringParameter ("member_id", memberId);
        rp.addQueryStringParameter ("consigner", mName);
        rp.addQueryStringParameter ("mobile", mPhone);
        rp.addQueryStringParameter ("address", mAddress);
        rp.addQueryStringParameter ("remark", mMark);
        addOrUpdateAddress (rp);
    }

    public void addOrUpdateAddress (RequestParams requestParams)
    {
        dialogProgress (activity, "请稍后");
        String s;
        if ( add )
        {
            s = "addaddress";
        } else
        {
            s = "editaddressinfo";
            requestParams.addQueryStringParameter ("id", myAddress.getId ());
        }
        HttpHelper.postByCommand (s, requestParams,
                new RequestCallBack<String> ()
                {
                    @Override
                    public void onFailure (HttpException paramHttpException,
                                           String paramString)
                    {
                        Tools.toast (activity,
                                Tools.getStr (activity, R.string.NETWORKERROR));
                        dialogDismiss ();
                    }

                    @Override
                    public void onSuccess (ResponseInfo<String> paramResponseInfo)
                    {
                        try
                        {
                            String result = paramResponseInfo.result;
                            JSONObject jsonData = new JSONObject (result);
                            String code = jsonData.getString ("code");
                            if ( code.equals ("200") )
                            {
                                if ( add )
                                {
                                    Tools.toast (activity, Tools.getStr (
                                            activity, R.string.ADSADDSUCCESS));
                                    setResult (12);
                                    AnimUtil.animBackSlideFinish (activity);
                                } else
                                {
                                    String da = jsonData.getString ("data");
                                    if ( da.equals ("true") )
                                    {
                                        Tools.toast (activity, Tools.getStr (
                                                activity,
                                                R.string.ADSEDITSUCCESS));
                                        setResult (12);
                                        AnimUtil.animBackSlideFinish (activity);
                                    } else
                                    {
                                        Tools.toast (activity, Tools.getStr (
                                                activity, R.string.ADSEDITFAIL));
                                    }
                                }
                            } else
                            {
                                if ( code.equals ("202") )
                                {
                                    Tools.toast (activity, Tools.getStr (
                                            activity, R.string.ADSEXIST));
                                }
                            }
                        } catch (JSONException ex)
                        {
                            ex.printStackTrace ();
                            dialogDismiss ();
                        }
                        dialogDismiss ();
                    }
                });
    }

}
