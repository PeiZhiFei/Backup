package com.runkun.lbsq.fragment;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.text.Html;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import com.lidroid.xutils.HttpUtils;
import com.lidroid.xutils.ViewUtils;
import com.lidroid.xutils.exception.HttpException;
import com.lidroid.xutils.http.RequestParams;
import com.lidroid.xutils.http.ResponseInfo;
import com.lidroid.xutils.http.callback.RequestCallBack;
import com.lidroid.xutils.http.client.HttpRequest.HttpMethod;
import com.lidroid.xutils.view.annotation.ViewInject;
import com.runkun.lbsq.R;
import com.runkun.lbsq.activity.RegisterActivity;
import com.runkun.lbsq.utils.AnimUtil;
import com.runkun.lbsq.utils.MyConstant;
import com.runkun.lbsq.utils.Tools;

import org.json.JSONException;
import org.json.JSONObject;

import feifei.project.view.ClearEditText;

public class LoginFragment extends BaseFragment implements OnClickListener
{

    @ViewInject(R.id.forget_pass)
    private TextView forgetPass;

    @ViewInject(R.id.register)
    private TextView freeRegister;

    @ViewInject(R.id.login)
    private Button login;

    @ViewInject(R.id.pass)
    private ClearEditText passWord;

    @ViewInject(R.id.phone)
    private ClearEditText userName;

    SharedPreferences sharedPreferences;
    SharedPreferences.Editor editor;
    String logins;

    @Override
    public void onCreate (Bundle paramBundle)
    {
        super.onCreate (paramBundle);
        Bundle bundle = getArguments ();
        if ( bundle != null )
        {
            logins = bundle.getString ("login");
        }
    }

    @Override
    public View onCreateView (LayoutInflater paramLayoutInflater,
                              ViewGroup viewGroup, Bundle bundle)
    {
        super.onCreateView (paramLayoutInflater, viewGroup, bundle);
        View view = paramLayoutInflater.inflate (R.layout.fragment_login,
                viewGroup, false);
        ViewUtils.inject (this, view);
        dialogInit ();

        forgetPass.setText (Html.fromHtml ("<u color=red>"
                + Tools.getStr (fragment, R.string.FORGETPASSWORDBUTTON)
                + "</u>"));
        forgetPass.setOnClickListener (this);
        freeRegister.setOnClickListener (this);
        login.setOnClickListener (this);
        sharedPreferences = getActivity ().getSharedPreferences (MyConstant.FILE_NAME,
                Context.MODE_PRIVATE);
        editor = sharedPreferences.edit ();
        return view;
    }


    @Override
    public void onResume ()
    {
        super.onResume ();
        userName.setText (sharedPreferences.getString ("phone", ""));
    }

    @Override
    public void onClick (View v)
    {
        switch (v.getId ())
        {
            case R.id.forget_pass:
                Intent intent = new Intent (getActivity (), RegisterActivity.class);
                intent.putExtra ("type", "forget");
                startActivityForResult (intent, MyConstant.REQUEST_FORGET);
                AnimUtil.animToSlide (getActivity ());
                break;
            case R.id.register:
                Intent intent2 = new Intent (getActivity (), RegisterActivity.class);
                intent2.putExtra ("type", "register");
                startActivityForResult (intent2, MyConstant.REQUEST_REGISTER);
                AnimUtil.animToSlide (getActivity ());
                break;
            case R.id.login:
                login ();
                break;

        }
    }

    @Override
    public void onActivityResult (int requestCode, int resultCode, Intent data)
    {
        super.onActivityResult (requestCode, resultCode, data);
        if ( resultCode == 89 )
        {
            getActivity ().finish ();
        }
    }

    private void login ()
    {
        if ( Tools.checkPhone (userName.getText ().toString ()) )
        {
            if ( (passWord != null) && (!passWord.equals (""))
                    && (passWord.length () >= 6) )
            {
                RequestParams rp = new RequestParams ();
                rp.addQueryStringParameter ("username", userName.getText ()
                        .toString ().trim ());
                rp.addQueryStringParameter ("password", passWord.getText ()
                        .toString ().trim ());
                SharedPreferences sp = getActivity ().getSharedPreferences (
                        MyConstant.FILE_NAME, Context.MODE_PRIVATE);
                String storeId = sp.getString ("storeId", "");
                requestLogin (storeId, rp);
            } else
            {
                Tools.toast (getActivity (),
                        Tools.getStr (fragment, R.string.RPASSSHORT));
                Tools.shake (passWord);
            }
        } else
        {
            Tools.toast (getActivity (),
                    Tools.getStr (fragment, R.string.PHONEWRONG));
            Tools.shake (userName);
        }
    }

    public void requestLogin (final String storeId, RequestParams requestParams)
    {
        dialogProgress (Tools.getStr (fragment, R.string.LOGINING));
        HttpUtils hu = new HttpUtils ();
        hu.send (HttpMethod.POST, MyConstant.URLLOGIN, requestParams,
                new RequestCallBack<String> ()
                {

                    @Override
                    public void onFailure (HttpException paramHttpException,
                                           String paramString)
                    {
                        Tools.toast (getActivity (),
                                Tools.getStr (fragment, R.string.NETWORKERROR));
                        dialogDismiss ();
                    }

                    @Override
                    public void onSuccess (ResponseInfo<String> paramResponseInfo)
                    {
                        String localObject = paramResponseInfo.result;
                        int isLogin = 0;
                        try
                        {
                            JSONObject resultJson = new JSONObject (localObject);
                            String result = resultJson.getString ("code");
                            isLogin = resultJson.getInt ("islogin");
                            if ( ("200".equals (result)) && (isLogin == 1) )
                            {
                                JSONObject jbResul = resultJson
                                        .getJSONObject ("datas");
                                editor.putString (MyConstant.KEY_MEMBERPHONE,
                                        jbResul.getString ("mobile"));
                                editor.putString (MyConstant.KEY_MEMBERID,
                                        jbResul.getString ("member_id"));
                                editor.putString ("shopcount", resultJson
                                        .getString ("membershopcarcount"));
                                editor.putString ("storeId", storeId);
                                editor.commit ();
                                Intent intent = new Intent ();
                                intent.putExtra ("login", logins);
                                getActivity ().setResult (MyConstant.RESULT_MAIN,
                                        intent);
                                AnimUtil.animBackSlideFinish (getActivity ());
                                Tools.toast (getActivity (), Tools.getStr (
                                        fragment, R.string.LOGINSUCCESS));

                            } else if ( ("201".equals (result)) && (isLogin == 0) )
                            {
                                Tools.toast (getActivity (), Tools.getStr (fragment, R.string.LOGINFAIL));
                            }
                        } catch (JSONException e)
                        {
                            Tools.toast (getActivity (),
                                    Tools.getStr (fragment, R.string.JSONERROR));
                            e.printStackTrace ();
                            dialogDismiss ();
                        }
                        dialogDismiss ();
                    }
                });
    }
}
