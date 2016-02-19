package com.runkun.lbsq.activity;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;

import com.lidroid.xutils.ViewUtils;
import com.lidroid.xutils.view.annotation.ViewInject;
import com.runkun.lbsq.R;
import com.runkun.lbsq.utils.AnimUtil;
import com.runkun.lbsq.utils.HttpSessionUtil;
import com.runkun.lbsq.utils.MyConstant;
import com.runkun.lbsq.utils.Tools;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import feifei.project.util.L;
import feifei.project.view.ClearEditText;

public class RegisterActivity extends BaseAcitivity implements
        OnClickListener
{
    @ViewInject(R.id.pass)
    private ClearEditText pass;

    @ViewInject(R.id.phone)
    private ClearEditText phone;

    @ViewInject(R.id.register)
    private Button register;

    @ViewInject(R.id.re_verification)
    private Button reqVerification;

    @ViewInject(R.id.rpass)
    private ClearEditText rpass;

    @ViewInject(R.id.verification)
    private ClearEditText verifaction;

    private String phones;
    private String pass1;
    private String repass1;
    private String verifactions;
    private String type;

    private SharedPreferences sharedPreferences;
    private SharedPreferences.Editor editor;

    private TimeCount time;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);
        ViewUtils.inject(this);
        tint();
        initActionbar();
        dialogInit();
        type = getIntent().getStringExtra("type");
        if (type.equals("forget"))
        {
            setTitles(Tools.getStr(activity, R.string.TFORGET));
            register.setText(Tools.getStr(activity, R.string.CONFIM));
            // verifactionLayout.setVisibility(View.GONE);
        } else
        {
            setTitles(Tools.getStr(activity, R.string.TREGISTER));
            register.setText(Tools.getStr(activity, R.string.TREGISTER));
        }
        register.setOnClickListener(this);
        reqVerification.setOnClickListener(this);
        time = new TimeCount(60000, 1000);
        sharedPreferences = getSharedPreferences("lbsq", Context.MODE_PRIVATE);
        editor = sharedPreferences.edit();

    }


    @Override
    public void onClick(View v)
    {
        switch (v.getId())
        {
            case R.id.re_verification:
                if (Tools.checkPhone(phone.getText().toString()))
                {
                    if (type.equals("forget"))
                    {
                        sendMessageForget();
                    } else
                    {
                        sendMessageRegister();
                    }
                } else
                {
                    Tools.toast(this, Tools.getStr(activity, R.string.PHONEWRONG));
                    AnimUtil.animShakeText(phone);
                }
                break;
            case R.id.register:
                phones = phone.getText().toString().trim();
                pass1 = pass.getText().toString().trim();
                repass1 = rpass.getText().toString().trim();
                verifactions = verifaction.getText().toString().trim();
                if (Tools.checkPhone(phones))
                {
                    if (Tools.isEmpty(pass1) || (pass1.length() < 6)
                            || Tools.isEmpty(repass1) || (repass1.length() < 6))
                    {
                        Tools.toast(this,
                                Tools.getStr(activity, R.string.RPASSSHORT));
                        AnimUtil.animShakeText(pass);
                        AnimUtil.animShakeText(rpass);
                        return;
                    }
                    if (!pass1.equals(repass1))
                    {
                        Tools.toast(this,
                                Tools.getStr(activity, R.string.RPASSNOMATCH));
                        AnimUtil.animShakeText(pass);
                        AnimUtil.animShakeText(rpass);
                        return;
                    }
                    // if (!type.equals("forget")) {
                    if (Tools.isEmpty(verifactions))
                    {
                        Tools.toast(this,
                                Tools.getStr(activity, R.string.RPLEASEVERIFY));
                        AnimUtil.animShakeText(verifaction);
                        return;
                    }
                    // }
                    if (type.equals("forget"))
                    {
                        if (Tools.isNetworkAvailable(activity))
                        {
                            forgetPassword();
                        } else
                        {
                            Tools.toast(activity,
                                    Tools.getStr(activity, R.string.NETWORKERROR));
                        }
                    } else
                    {
                        if (Tools.isNetworkAvailable(activity))
                        {
                            regesitUser();
                        } else
                        {
                            Tools.toast(activity,
                                    Tools.getStr(activity, R.string.NETWORKERROR));
                        }
                    }
                } else
                {
                    Tools.toast(this, Tools.getStr(activity, R.string.PHONEWRONG));
                    AnimUtil.animShakeText(phone);
                }
                break;
        }
    }

    private void sendMessageRegister()
    {
        dialogProgress(activity,
                Tools.getStr(activity, R.string.RGETTING));
        new AsyncTask<Void, String, String>()
        {

            @Override
            protected String doInBackground(Void... params2)
            {
                HttpSessionUtil reqNetService = new HttpSessionUtil();
                List<NameValuePair> params = new ArrayList<NameValuePair>();
                params.add(new BasicNameValuePair("mobile", phone.getText().toString().trim()));
                String s = null;
                try
                {
                    s = reqNetService.doPost(RegisterActivity.this,
                            MyConstant.URLSENDMESSAGE, params);
                } catch (JSONException e)
                {
                    e.printStackTrace();
                    publishProgress(Tools.getStr(activity,
                            R.string.NETWORKERROR));
                }

                String result = s;
                String resultx = null;
                try
                {
                    JSONObject jsonResul = new JSONObject(result);
                    L.l (result);
                    String isResul = jsonResul.getString("code");
                    if (isResul.equals("200"))
                    {
                        JSONObject ob = jsonResul.getJSONObject("datas");
                        resultx = ob.getString("haveUser");
                        if (resultx.equals("false"))
                        {
                            publishProgress(Tools.getStr(activity,
                                    R.string.RERRORALREADYREGSITERLOGIN));
                        } else
                        {
                            if (resultx.equals("true"))
                            {
                                if (ob.getString("send").equals("success"))
                                {
                                    publishProgress(Tools.getStr(activity,
                                            R.string.RSENDSUCCESS));
                                } else
                                {
                                    publishProgress(Tools.getStr(activity,
                                            R.string.RSENDFAIL));
                                }
                            }
                        }
                    }
                } catch (JSONException e)
                {
                    publishProgress(Tools.getStr(activity, R.string.JSONERROR));
                    e.printStackTrace();
                }

                return resultx;
            }

            @Override
            protected void onProgressUpdate(String[] values)
            {
                Tools.toast(RegisterActivity.this, values[0]);
                dialogDismiss();
                if (values[0].equals(Tools.getStr(activity,
                        R.string.RSENDSUCCESS)))
                {
                    time.start();
                }
            }

        }.execute();

    }

    private void sendMessageForget()
    {
        dialogProgress(activity,
                Tools.getStr(activity, R.string.RGETTING));
        new AsyncTask<Void, String, String>()
        {

            @Override
            protected String doInBackground(Void... params2)
            {
                HttpSessionUtil reqNetService = new HttpSessionUtil();
                List<NameValuePair> params = new ArrayList<NameValuePair>();
                params.add(new BasicNameValuePair("mobile", phone.getText()
                        .toString().trim()));
                String s = null;
                try
                {
                    s = reqNetService.doPost(RegisterActivity.this,
                            MyConstant.URLSENDMESSAGEFORGET, params);
                } catch (JSONException e)
                {
                    e.printStackTrace();
                    publishProgress(Tools.getStr(activity,
                            R.string.NETWORKERROR));
                }

                String result = s;
                String resultx = null;
                try
                {
                    JSONObject jsonResul = new JSONObject(result);
                    L.l(result);
                    String isResul = jsonResul.getString("code");
                    if (isResul.equals("200"))
                    {
                        JSONObject ob = jsonResul.getJSONObject("datas");
                        resultx = ob.getString("haveUser");
                        if (resultx.equals("false"))
                        {
                            publishProgress(Tools.getStr(activity,
                                    R.string.RERRORNOREGSITER));
                        } else
                        {
                            if (resultx.equals("true"))
                            {
                                if (ob.getString("send").equals("success"))
                                {
                                    publishProgress(Tools.getStr(activity,
                                            R.string.RSENDSUCCESS));
                                } else
                                {
                                    publishProgress(Tools.getStr(activity,
                                            R.string.RSENDFAIL));
                                }
                            }
                        }
                    }
                } catch (JSONException e)
                {
                    publishProgress(Tools.getStr(activity, R.string.JSONERROR));
                    e.printStackTrace();
                }

                return resultx;
            }

            @Override
            protected void onProgressUpdate(String[] values)
            {
                Tools.toast(RegisterActivity.this, values[0]);
                dialogDismiss();
                if (values[0].equals(Tools.getStr(activity,
                        R.string.RSENDSUCCESS)))
                {
                    time.start();
                }
            }

        }.execute();

    }

    private void forgetPassword()
    {
        dialogProgress(activity,
                Tools.getStr(activity, R.string.RGETTING));
        new AsyncTask<Void, String, String>()
        {

            @Override
            protected String doInBackground(Void... params2)
            {
                HttpSessionUtil reqNetService = new HttpSessionUtil();
                List<NameValuePair> params = new ArrayList<NameValuePair>();
                params.add(new BasicNameValuePair("mobile", phones));
                params.add(new BasicNameValuePair("password", pass1));
                params.add(new BasicNameValuePair("fcode", verifactions));
                String s = null;
                try
                {
                    s = reqNetService.doPost(RegisterActivity.this,
                            MyConstant.URLFORGETPASSWORD, params);
                } catch (JSONException e)
                {
                    e.printStackTrace();
                    publishProgress(Tools.getStr(activity, R.string.JSONERROR));
                    Tools.toast(activity,
                            Tools.getStr(activity, R.string.JSONERROR));
                }
                String result = s;
                String resultx = null;
                try
                {
                    L.l(result);
                    JSONObject jsonResul = new JSONObject(result);
                    String isResul = jsonResul.getString("code");
                    if (isResul.equals("200"))
                    {
                        String dString = jsonResul.getString("datas");
                        if (dString.equals("true"))
                        {
                            publishProgress(Tools.getStr(activity,
                                    R.string.RCHANGESUCCESS));
                        } else
                        {
                            publishProgress(Tools.getStr(activity,
                                    R.string.RCHANGEFAIL));
                        }
                    } else if (isResul.equals("201"))
                    {
                        publishProgress(Tools.getStr(activity,
                                R.string.RERRORNOREGSITER));
                    } else if (isResul.equals("202"))
                    {
                        publishProgress(Tools.getStr(activity,
                                R.string.RERRORVERIFY));
                    }
                } catch (JSONException e)
                {
                    publishProgress(Tools.getStr(activity, R.string.JSONERROR));
                    e.printStackTrace();
                }
                return resultx;
            }

            @Override
            protected void onProgressUpdate(String[] values)
            {
                Tools.toast(RegisterActivity.this, values[0]);
                dialogDismiss();
                if (values[0].equals(Tools.getStr(activity,
                        R.string.RCHANGESUCCESS)))
                {
                    editor.putString(MyConstant.KEY_MEMBERPHONE, phones);
                    editor.apply();
                    setResult(89);
                    AnimUtil.animBackFinish(activity);
                }
            }

        }.execute();
    }

    private void regesitUser()
    {
        dialogProgress(activity,
                Tools.getStr(activity, R.string.RREGSITING));
        new AsyncTask<Void, String, String>()
        {

            @Override
            protected String doInBackground(Void... params2)
            {
                HttpSessionUtil reqNetService = new HttpSessionUtil();
                List<NameValuePair> params = new ArrayList<NameValuePair>();
                params.add(new BasicNameValuePair("member_name", phones));
                params.add(new BasicNameValuePair("password", pass1));
                params.add(new BasicNameValuePair("code", verifactions));
//                L.l("CHANNELID:##########    " Tools.getUDID(activity));
                params.add(new BasicNameValuePair("phone_code", Tools.getUDID(activity)));
                String s = null;
                try
                {
                    s = reqNetService.doPost(RegisterActivity.this,
                            MyConstant.URLREGISTER, params);
                } catch (JSONException e)
                {
                    e.printStackTrace();
                    publishProgress(Tools.getStr(activity,
                            R.string.NETWORKERROR));
                }
                String result = s;
                String resultx = null;
                try
                {
                    L.l(result);
                    JSONObject jsonResul = new JSONObject(result);
                    String isResul = jsonResul.getString("code");
                    if (isResul.equals("200"))
                    {
                        JSONObject jsonArray = jsonResul.getJSONObject("datas");
                        String member = jsonArray.getString("member_id");
                        String haveUser = jsonArray.getString("haveUser");
                        if (haveUser.equals("false"))
                        {
                            publishProgress(Tools.getStr(activity,
                                    R.string.RERRORALREADYREGSITER));
                        } else
                        {
                            if (haveUser.equals("true"))
                            {
                                publishProgress(Tools.getStr(activity,
                                                R.string.RREGISTERSUCCESS), phones,
                                        member);
                                if (jsonArray.getString("iscoupon").equals("1"))
                                {
                                    publishProgress("新用户优惠券已发放到您的账户里，可以到[我的优惠券]里查看");
                                }
                            }
                        }
                    } else
                    {
                        if (isResul.equals("201"))
                        {
                            publishProgress(Tools.getStr(activity,
                                    R.string.RERRORVERIFY));
                        }
                    }

                } catch (JSONException e)
                {
                    publishProgress(Tools.getStr(activity, R.string.JSONERROR));
                    e.printStackTrace();
                }

                return resultx;
            }

            @Override
            protected void onProgressUpdate(String[] values)
            {
                Tools.toast(RegisterActivity.this, values[0]);
                try
                {
                    dialogDismiss();
                } catch (Exception e)
                {
                    e.printStackTrace();
                }

                if (values[0].equals(Tools.getStr(activity,
                        R.string.RERRORALREADYREGSITER)))
                {
                    AnimUtil.animShakeText(phone);
                } else if (values[0].equals(Tools.getStr(activity,
                        R.string.RREGISTERSUCCESS)))
                {
                    editor.putString("phone", values[1]);
                    editor.putString(MyConstant.KEY_MEMBERID, values[2]);
                    editor.apply();
                    setResult(89);
                    AnimUtil.animBackSlideFinish(activity);
                } else if (values[0].equals(Tools.getStr(activity,
                        R.string.RERRORVERIFY)))
                {
                    AnimUtil.animShakeText(verifaction);
                } else if (values[0].equals(Tools.getStr(activity,
                        R.string.JSONERROR)))
                {
                    time.cancel();
                }
            }
        }.execute();

    }

    class TimeCount extends CountDownTimer
    {

        public TimeCount(long millisInFuture, long countDownInterval)
        {
            super(millisInFuture, countDownInterval);
        }

        @Override
        public void onTick(long millisUntilFinished)
        {
            reqVerification
                    .setBackgroundResource(R.drawable.verify_button_border);
            reqVerification.setClickable(false);
            reqVerification.setText(millisUntilFinished / 1000
                    + Tools.getStr(activity, R.string.RSEND));
        }

        @Override
        public void onFinish()
        {
            reqVerification
                    .setText(Tools.getStr(activity, R.string.RGETVERIFY));
            reqVerification.setClickable(true);
            reqVerification.setBackgroundResource(R.drawable.button_green);
        }
    }

    @Override
    public void onPause()
    {
        super.onPause();
        if (time != null)
        {
            time.cancel();
        }
    }

}
