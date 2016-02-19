package com.runkun.lbsq.busi.activity;

import android.content.Intent;
import android.os.Bundle;

import com.runkun.lbsq.busi.R;
import com.runkun.lbsq.busi.util.AnimUtil;
import com.runkun.lbsq.busi.util.MyConstant;
import com.runkun.lbsq.busi.util.Tools;

import org.json.JSONException;
import org.json.JSONObject;

import butterknife.ButterKnife;
import butterknife.InjectView;
import butterknife.OnClick;
import feifei.project.view.ClearEditText;

/**
 * 作者    你的名字
 * 时间    2015/8/19 15:58
 * 文件    lbsq_busi
 * 描述
 */
public class LoginActivity extends NetActivity
{

    @InjectView(R.id.member)
    ClearEditText member;
    @InjectView(R.id.password)
    ClearEditText password;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        ButterKnife.inject(this);
        tint();
        dialogInit();
    }

    @OnClick(R.id.login_button)
    public void login()
    {

        String getMember = member.getText().toString();
        String getPassword = password.getText().toString();
        if (Tools.isEmpty(getMember))
        {
            Tools.toast(activity, "请输入手机号");
            AnimUtil.animShakeText(member);
//        } else if (!Tools.checkPhone(getMember))
//        {
//            Tools.toast(activity, "手机格式不正确");
//            AnimUtil.animShakeText(member);
        } else if (Tools.isEmpty(getPassword))
        {
            Tools.toast(activity, "请输入密码");
            AnimUtil.animShakeText(password);
        } else if (getPassword.length() < 6)
        {
            Tools.toast(activity, "密码长度不足6位");
            AnimUtil.animShakeText(password);
        } else
        {
            url = MyConstant.CLOGIN;
            rp.addQueryStringParameter("username", getMember);
            rp.addQueryStringParameter("password", getPassword);
            loadData();
        }
    }


    @OnClick(R.id.forget_password)
    public void forget()
    {
        startActivity(new Intent(this, ForgetPasswordActivity.class));
        AnimUtil.animTo(this);
    }

    @Override
    protected void getData(JSONObject jsonResult) throws JSONException
    {
        JSONObject jsonObject = jsonResult.getJSONObject("datas");
        String isLogin = jsonObject.getString("islogin");
        if (isLogin.equals("1"))
        {
            String storeId = jsonObject.getString("store_id");
            String storeName = jsonObject.getString("store_name");
            String account = jsonObject.getString("account");
            Tools.saveShopInfo(activity, storeId, storeName, account);
            startActivity(new Intent(this, MainActivity.class));
            AnimUtil.animToFinish(this);
        } else
        {
            Tools.toast(activity, "用户名或密码错误");
        }
    }

}
