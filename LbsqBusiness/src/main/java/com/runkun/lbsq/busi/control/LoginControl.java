package com.runkun.lbsq.busi.control;

import com.runkun.lbsq.busi.util.ILogin;


public class LoginControl
{
    ILogin iLogin;
    String account;
    String storeId;
    String storeName;


    public LoginControl(ILogin iLogin)
    {
        this.iLogin = iLogin;
    }

    public void requestLogin(String userName, String password)
    {
        模拟网络请求(userName, password);
        赋值();
        iLogin.resultLogin(storeId, storeName, account);
    }

    private void 模拟网络请求(String userName, String password)
    {

    }

    private void 赋值()
    {
        account = "1";
        storeId = "2";
        storeName = "3";
    }
}
