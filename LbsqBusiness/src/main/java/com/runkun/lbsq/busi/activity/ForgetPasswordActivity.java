package com.runkun.lbsq.busi.activity;

import android.os.AsyncTask;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.widget.Button;

import com.runkun.lbsq.busi.R;
import com.runkun.lbsq.busi.util.AnimUtil;
import com.runkun.lbsq.busi.util.HttpSessionUtil;
import com.runkun.lbsq.busi.util.MyConstant;
import com.runkun.lbsq.busi.util.Tools;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import butterknife.ButterKnife;
import butterknife.InjectView;
import butterknife.OnClick;
import feifei.project.util.L;
import feifei.project.view.ClearEditText;

public class ForgetPasswordActivity extends BaseAcitivity
{

    @InjectView(R.id.phone)
    ClearEditText phone;
    @InjectView(R.id.pass)
    ClearEditText pass;
    @InjectView(R.id.rpass)
    ClearEditText rpass;
    @InjectView(R.id.verification)
    ClearEditText verification;

    @InjectView(R.id.re_verification)
    Button reVerification;

    private TimeCount time;

    String getMember;
    String getPassword;
    String getRePassword;
    String verify;


    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_forget);
        ButterKnife.inject(this);
        tint();
        initActionbar();
        setTitles("忘记密码");
        dialogInit();
        time = new TimeCount(60000, 1000);
    }


    @OnClick(R.id.re_verification)
    public void sendMessage()
    {
        if (Tools.checkPhone(phone.getText().toString()))
        {
            sendMessageForget();
        } else
        {
            Tools.toast(this, "手机格式错误，请重试");
            AnimUtil.animShakeText(phone);
        }
    }


    private void sendMessageForget()
    {

        new AsyncTask<Void, String, String>()
        {
            @Override
            protected void onPreExecute()
            {
                super.onPreExecute();
                dialogProgress("请求中");
            }

            @Override
            protected String doInBackground(Void... params2)
            {
                HttpSessionUtil reqNetService = new HttpSessionUtil();
                List<NameValuePair> params = new ArrayList<NameValuePair>();
                params.add(new BasicNameValuePair("mobile", phone.getText().toString()));
                String s = null;
                try
                {
                    s = reqNetService.doPost(activity, MyConstant.CSENDMESSAGE, params);
                } catch (JSONException e)
                {
                    e.printStackTrace();
                    publishProgress("网络连接错误");
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
                            publishProgress("该手机号未注册");
                        } else
                        {
                            if (resultx.equals("true"))
                            {
                                if (ob.getString("send").equals("success"))
                                {
                                    publishProgress("验证码发送成功");
                                } else
                                {
                                    publishProgress("验证码发送失败");
                                }
                            }
                        }
                    }
                } catch (JSONException e)
                {
                    publishProgress("数据解析错误");
                    e.printStackTrace();
                }

                return resultx;
            }

            @Override
            protected void onProgressUpdate(String[] values)
            {
                Tools.toast(activity, values[0]);
                dialogDismiss();
                if (values[0].equals("验证码发送成功"))
                {
                    time.start();
                }
            }

        }.execute();

    }


    @OnClick(R.id.register)
    public void forget()
    {

        getMember = phone.getText().toString();
        getPassword = pass.getText().toString();
        getRePassword = rpass.getText().toString();
        verify = verification.getText().toString();
        if (Tools.isEmpty(getMember))
        {
            Tools.toast(activity, "请输入手机号");
            AnimUtil.animShakeText(phone);
        } else if (!Tools.checkPhone(getMember))
        {
            Tools.toast(activity, "手机格式不正确");
            AnimUtil.animShakeText(phone);
        } else if (Tools.isEmpty(getPassword))
        {
            Tools.toast(activity, "请输入密码");
            AnimUtil.animShakeText(pass);
        } else if (getPassword.length() < 6)
        {
            Tools.toast(activity, "密码长度不足6位");
            AnimUtil.animShakeText(pass);
        } else if (Tools.isEmpty(getRePassword))
        {
            Tools.toast(activity, "请输入确认密码");
            AnimUtil.animShakeText(rpass);
        } else if (getRePassword.length() < 6)
        {
            Tools.toast(activity, "密码长度不足6位");
            AnimUtil.animShakeText(rpass);
        } else if (!getPassword.equals(getRePassword))
        {
            Tools.toast(activity, "两次密码不匹配");
            AnimUtil.animShakeText(pass);
            AnimUtil.animShakeText(rpass);
        } else if (Tools.isEmpty(verify))
        {
            Tools.toast(this, "请输入验证码");
            AnimUtil.animShakeText(verification);
        } else
        {
            // TODO 写一个存map的sharepreference和一个的
            if (Tools.isNetworkAvailable(activity))
            {
                regesitUser();
            } else
            {
                Tools.toast(activity, "网络连接失败");
            }
        }
    }

    private void regesitUser()
    {

        new AsyncTask<Void, String, String>()
        {

            @Override
            protected void onPreExecute()
            {
                super.onPreExecute();
                dialogProgress("请求中");
            }

            @Override
            protected String doInBackground(Void... params2)
            {
                HttpSessionUtil reqNetService = new HttpSessionUtil();
                List<NameValuePair> params = new ArrayList<NameValuePair>();
                params.add(new BasicNameValuePair("mobile", getMember));
                params.add(new BasicNameValuePair("password", getPassword));
                params.add(new BasicNameValuePair("fcode", verify));
                String s = null;
                try
                {
                    s = reqNetService.doPost(activity,
                            MyConstant.CFORGET, params);
                } catch (JSONException e)
                {
                    e.printStackTrace();
                    publishProgress("数据解析异常");
                }
                String result = s;
                String resultx = "";
                try
                {
                    L.l(result);
                    JSONObject jsonResul = new JSONObject(result);
                    String isResul = jsonResul.getString("code");
                    if (isResul.equals("200"))
                    {
                        JSONObject datas = jsonResul.getJSONObject("datas");
                        String re = datas.getString("isresult");
                        if (re.equals("1"))
                        {
                            publishProgress("密码修改成功");
                        } else if (re.equals("0"))
                        {
                            publishProgress("密码修改失败，请重试");
                        }
                    } else if (isResul.equals("201"))
                    {
                        publishProgress("没有该会员，请注册");
                    } else if (isResul.equals("202"))
                    {
                        publishProgress("验证码错误");
                    }

                } catch (JSONException e)
                {
                    publishProgress("数据解析错误");
                    e.printStackTrace();
                }
                return resultx;
            }

            @Override
            protected void onProgressUpdate(String[] values)
            {
                Tools.toast(activity, values[0]);
                dialogDismiss();

                if (values[0].equals("验证码错误"))
                {
                    AnimUtil.animShakeText(verification);
                } else if (values[0].equals("密码修改成功"))
                {
                    AnimUtil.animBackFinish(activity);
                } else if (values[0].equals("数据解析错误"))
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
            reVerification
                    .setBackgroundResource(R.drawable.verify_button_border);
            reVerification.setClickable(false);
            reVerification.setText(millisUntilFinished / 1000
                    + "秒后重新发送");
        }

        @Override
        public void onFinish()
        {
            reVerification
                    .setText("获取验证码");
            reVerification.setClickable(true);
            reVerification.setBackgroundResource(R.drawable.button_green);
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
