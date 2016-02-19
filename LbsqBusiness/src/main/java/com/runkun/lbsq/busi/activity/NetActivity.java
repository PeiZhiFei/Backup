package com.runkun.lbsq.busi.activity;

import com.lidroid.xutils.HttpUtils;
import com.lidroid.xutils.exception.HttpException;
import com.lidroid.xutils.http.RequestParams;
import com.lidroid.xutils.http.ResponseInfo;
import com.lidroid.xutils.http.callback.RequestCallBack;
import com.lidroid.xutils.http.client.HttpRequest;
import com.runkun.lbsq.busi.util.Tools;

import org.json.JSONException;
import org.json.JSONObject;

import feifei.project.util.L;

public abstract class NetActivity extends BaseAcitivity
{

    protected RequestParams rp = new RequestParams();
    protected String url = "";

    //使用loadData前把rp和url填好
    protected void loadData()
    {
        if (!Tools.isNetworkAvailable(activity))
        {
            Tools.toast(activity, "网络连接失败");
        } else
        {
            HttpUtils httpUtils = new HttpUtils(30000);
            httpUtils.send(HttpRequest.HttpMethod.POST, url, rp, new RequestCallBack<String>()
            {
                @Override
                public void onStart()
                {
                    super.onStart();
                    dialogProgress("加载中");
                }


                @Override
                public void onFailure(HttpException arg0, String arg1)
                {
                    Tools.toast(activity, "网络连接失败");
                    dialogDismiss();
                    arg0.printStackTrace ();
                    L.l (arg0.getMessage ());
                }

                @Override
                public void onSuccess(ResponseInfo<String> resp)
                {
                    String result = resp.result;
                    L.l (result);
                    try
                    {
                        JSONObject jsonResult = new JSONObject(result);
                        //返回200
                        if (Tools.isSuccess(jsonResult))
                        {
                            getData(jsonResult);
                        }

                    } catch (JSONException e)
                    {
                        e.printStackTrace();
                    } finally
                    {
                        dialogDismiss();
                    }
                }

            });
        }
    }

    protected abstract void getData(JSONObject jsonResult) throws JSONException;
}
