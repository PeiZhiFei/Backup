package laiyi.tobacco.activity;

import com.duowan.mobile.netroid.DefaultRetryPolicy;
import com.duowan.mobile.netroid.Listener;
import com.duowan.mobile.netroid.NetroidError;
import com.lidroid.xutils.HttpUtils;
import com.lidroid.xutils.exception.HttpException;
import com.lidroid.xutils.http.RequestParams;
import com.lidroid.xutils.http.ResponseInfo;
import com.lidroid.xutils.http.callback.RequestCallBack;
import com.lidroid.xutils.http.client.HttpRequest;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

import feifei.library.util.L;
import feifei.library.util.Tools;
import laiyi.tobacco.util.PostRequest;

public abstract class NetActivity extends BaseActivity
{

    protected RequestParams rp = new RequestParams();
    protected String url = "";
    protected Map<String,String> mParams=new HashMap<>();

    //使用loadData前把rp和url填好
    protected void loadData()
    {
        if (!Tools.isNetworkAvailable(activity))
        {
            Tools.toast(activity, "网络连接失败");
        } else
        {

            PostRequest request = new PostRequest(url, mParams, new Listener<String>() {

                @Override
                public void onPreExecute() {
                    super.onPreExecute();
                        dialogProgress();
                }

                @Override
                public void onFinish() {
                    super.onFinish();
                        dialogDismiss();
                }

                @Override
                public void onCancel() {
                    super.onCancel();
                    // 这里不需要关闭dialog，之前的xutil也可以改一下
                }

                @Override
                public void onProgressChange(long fileSize, long downloadedSize) {
                    super.onProgressChange(fileSize, downloadedSize);
                    L.l (downloadedSize / fileSize);
                }

                @Override
                public void onSuccess(String response) {
                    L.l(response);
                    L.l(url);
                    try {
                        JSONObject jsonResult = new JSONObject(response);

                            getData(jsonResult);


                    } catch (JSONException e) {
                        e.printStackTrace();
                        error("数据解析错误");
                    }
                }

                @Override
                public void onError(NetroidError error) {
                    error.printStackTrace();
                    error("网络开小差了");
                }
            }
            );
            request.setRetryPolicy(new DefaultRetryPolicy(10000, 2, DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
            MyApplication.mRequestQueue.add(request);
        }
//            HttpUtils httpUtils = new HttpUtils(30000);
//            httpUtils.send(HttpRequest.HttpMethod.POST, url, rp, new RequestCallBack<String>() {
//                @Override
//                public void onStart() {
//                    super.onStart();
//                    dialogProgress();
//                }
//
//
//                @Override
//                public void onFailure(HttpException arg0, String arg1) {
//                    Tools.toast(activity, "网络连接失败");
//                    dialogDismiss();
//                    arg0.printStackTrace();
//                    L.l(arg0.getMessage());
//                }
//
//                @Override
//                public void onSuccess(ResponseInfo<String> resp) {
//                    String result = resp.result;
//                    L.l(result);
//                    try {
//                        JSONObject jsonResult = new JSONObject(result);
//                        //返回200
//                        if (Tools.isSuccess(jsonResult)) {
//                            getData(jsonResult);
//                        }
//
//                    } catch (JSONException e) {
//                        e.printStackTrace();
//                    } finally {
//                        dialogDismiss();
//                    }
//                }
//
//            });
//        }
}
    
    private long exitTime = 0;

    protected void error(String s) {
        if ((System.currentTimeMillis() - exitTime) > 2000) {
            exitTime = System.currentTimeMillis();
            if (s != null) {
                Tools.toast(activity, s);
            }
        }
    }
    protected abstract void getData(JSONObject jsonResult) throws JSONException;
}
