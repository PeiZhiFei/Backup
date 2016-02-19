package feifei.dataanalysis.base;

import android.app.Dialog;
import android.os.Bundle;
import android.view.Gravity;

import com.duowan.mobile.netroid.DefaultRetryPolicy;
import com.duowan.mobile.netroid.Listener;
import com.duowan.mobile.netroid.NetroidError;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

import feifei.dataanalysis.R;
import feifei.project.util.L;
import feifei.project.util.Tools;
import feifei.project.view.NewtonCradleLoading;

public abstract class NetActivity extends BaseActivity {

    protected String url = Tools.URL;
    protected Map<String, String> mParams = new HashMap<>();


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        dialogInit();
    }

    public void loadData(final boolean dialog, final String tag) {
        if (!Tools.isNetworkAvailable(activity)) {
            error("网络开小差了");
        } else {
            PostRequest request = new PostRequest(url, mParams, new Listener<String>() {

                @Override
                public void onPreExecute() {
                    super.onPreExecute();
                    if (dialog) {
                        dialogProgress();
                    }
                }

                @Override
                public void onFinish() {
                    super.onFinish();
                    if (dialog) {
                        dialogDismiss();
                    }
                    onfinish();
                }

                @Override
                public void onCancel() {
                    super.onCancel();
                    // 这里不需要关闭dialog，之前的xutil也可以改一下
                }

                @Override
                public void onProgressChange(long fileSize, long downloadedSize) {
                    super.onProgressChange(fileSize, downloadedSize);
                    L.l(downloadedSize / fileSize);
                }

                @Override
                public void onSuccess(String response) {
                    L.l(response);
                    L.l(url);
                    try {
                        if (tag.equals("simple")) {
                            getData(null, tag);
                        } else if (tag.equals("30")) {
                            getData(null, tag);
                        } else {
                            // TODO: 15-9-30
                            JSONObject jsonResult = new JSONObject(response);
                            //返回200
                            if (Tools.isSuccess(jsonResult)) {
                                JSONArray jsonArray = jsonResult.getJSONArray("datas");
                                getData(jsonArray, tag);
                            } else {
                                error(null);
                            }
                        }

                    } catch (JSONException e) {
                        e.printStackTrace();
                        error("数据解析错误");
                    }
                }

                @Override
                public void onError(NetroidError error) {
                    error.printStackTrace();
                    // TODO: 2015/12/19 测试代码
                    if (!MyApplication.NEEDNET) {
                        try {
                            getData(null, tag);
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                        return;
                    }

                    error("网络开小差了");

                }
            }
            );
            request.setRetryPolicy(new DefaultRetryPolicy(10000, 2, DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
            MyApplication.mRequestQueue.add(request);
        }
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

    protected void onfinish() {
    }


    protected abstract void getData(JSONArray jsonArray, String tag) throws JSONException;


    protected Dialog dialogs;
    private boolean open = false;
    private NewtonCradleLoading newtonCradleLoading;

    public void dialogInit() {
        if (activity != null) {
            dialogs = new Dialog(activity, R.style.dialog_loading_style);
            dialogs.getWindow().getAttributes().gravity = Gravity.CENTER;
            dialogs.setContentView(R.layout.dialog);
            dialogs.setCancelable(false);
        }
    }

    public synchronized void dialogDismiss() {
        if (open && dialogs != null) {
            try {
                newtonCradleLoading.stop();
            } catch (Exception e) {
                e.printStackTrace();
            }
            dialogs.dismiss();
            open = false;
        }
    }

    public synchronized void dialogProgress() {
        if (!open && dialogs != null) {
            newtonCradleLoading = (NewtonCradleLoading) dialogs.findViewById(R.id.newton_cradle_loading);
            newtonCradleLoading.start();
            dialogs.show();
            open = true;
        }
    }

}
