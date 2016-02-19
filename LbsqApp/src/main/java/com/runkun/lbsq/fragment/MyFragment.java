package com.runkun.lbsq.fragment;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.baidu.android.pushservice.PushManager;
import com.lidroid.xutils.HttpUtils;
import com.lidroid.xutils.ViewUtils;
import com.lidroid.xutils.exception.HttpException;
import com.lidroid.xutils.http.RequestParams;
import com.lidroid.xutils.http.ResponseInfo;
import com.lidroid.xutils.http.callback.RequestCallBack;
import com.lidroid.xutils.http.client.HttpRequest.HttpMethod;
import com.runkun.lbsq.R;
import com.runkun.lbsq.activity.LoginActivity;
import com.runkun.lbsq.activity.MyAddressActivity;
import com.runkun.lbsq.activity.MyCollectionActivity;
import com.runkun.lbsq.activity.MyCouponsActivity;
import com.runkun.lbsq.activity.MyJudgeListActivity;
import com.runkun.lbsq.activity.MyOrderActivity;
import com.runkun.lbsq.interfaces.onButtonClick;
import com.runkun.lbsq.utils.AnimUtil;
import com.runkun.lbsq.utils.MyConstant;
import com.runkun.lbsq.utils.Tools;
import com.runkun.lbsq.view.pull.PullToZoomScrollViewEx;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.math.BigDecimal;
import java.math.RoundingMode;

public class MyFragment extends MainFirstBaseFragment implements
        OnClickListener
{
    private Button clearCache;
    private TextView login;
    String memberId;
    SharedPreferences sp;

    protected String basePath;
    protected String diskCachePath;// cache文件加
    protected String fileCache;// xml文件

    private PullToZoomScrollViewEx scrollView;

    @Override
    public View onCreateView (LayoutInflater inflater, ViewGroup container,
                              Bundle savedInstanceState)
    {
        View view = inflater.inflate (R.layout.fragment_my, container, false);
        ViewUtils.inject (this, view);
        dialogInit();

        scrollView = (PullToZoomScrollViewEx) view
                .findViewById (R.id.scroll_view);
        View headView = LayoutInflater.from (getActivity ()).inflate (
                R.layout.fragment_my_header, container, false);
        View zoomView = LayoutInflater.from (getActivity ()).inflate (
                R.layout.fragment_my_image, container, false);
        View contentView = LayoutInflater.from (getActivity ()).inflate (
                R.layout.fragment_my_content, container, false);
        scrollView.setHeaderView (headView);
        scrollView.setZoomView (zoomView);
        scrollView.setScrollContentView (contentView);

        DisplayMetrics localDisplayMetrics = new DisplayMetrics ();
        getActivity ().getWindowManager ().getDefaultDisplay ()
                .getMetrics (localDisplayMetrics);
        int mScreenHeight = localDisplayMetrics.heightPixels;
        int mScreenWidth = localDisplayMetrics.widthPixels;
        LinearLayout.LayoutParams params = new LinearLayout.LayoutParams (
                mScreenWidth, (int) (mScreenHeight / 5.0F));
        params.gravity = Gravity.CENTER_VERTICAL;
        scrollView.setHeaderLayoutParams (params);
        login = (TextView) scrollView.getHeaderView ().findViewById (R.id.login);
        Button myCollect = (Button) contentView.findViewById (R.id.my_collect);
        Button myScored = (Button) contentView.findViewById (R.id.my_scored);
        clearCache = (Button) contentView.findViewById (R.id.my_hun);
        Button myAddress = (Button) contentView.findViewById (R.id.my_address);
        Button myjudge = (Button) contentView.findViewById (R.id.my_judge);
        Button quit = (Button) contentView.findViewById (R.id.quit);
        Button order = (Button) contentView.findViewById (R.id.my_order);
        myCollect.setOnClickListener (this);
        myScored.setOnClickListener (this);
        myAddress.setOnClickListener (this);
        myjudge.setOnClickListener (this);
        quit.setOnClickListener (this);
        order.setOnClickListener (this);
        clearCache.setOnClickListener (this);
        sp = getActivity ().getSharedPreferences (MyConstant.FILE_NAME,
                Context.MODE_PRIVATE);

        basePath = "/data/data/" + getActivity ().getPackageName () + "/";
        diskCachePath = getActivity ().getCacheDir ().getAbsolutePath ();
        fileCache = getActivity ().getFilesDir ().getAbsolutePath ();
        double totalSize = getDirSize (new File (diskCachePath));
        double fileSize = getDirSize (new File (fileCache));
        double size = totalSize + fileSize;
        BigDecimal number = new BigDecimal (size);
        BigDecimal result = number.setScale (1, RoundingMode.DOWN);
        clearCache.setText ("清除缓存(" + result + "M)");
        return view;
    }

    @Override
    public void onResume ()
    {
        super.onResume ();
        login.setText (sp.getString ("phone", "用户登陆"));
    }

    private void deleteCache (String path)
    {
        File cachedFileDir = new File (path);
        if ( cachedFileDir.exists () && cachedFileDir.isDirectory () )
        {
            File[] cachedFiles = cachedFileDir.listFiles ();
            for (File f : cachedFiles)
            {
                if ( f.exists () )
                {
                    if ( f.isFile () )
                    {
                        f.delete ();
                    } else
                    {
                        deleteCache (f.getAbsolutePath ());
                    }
                }
            }
        }
    }

    public static double getDirSize (File file)
    {
        // 判断文件是否存在
        if ( file.exists () )
        {
            // 如果是目录则递归计算其内容
            if ( file.isDirectory () )
            {
                File[] children = file.listFiles ();
                double size = 0;
                for (File f : children)
                {
                    size += getDirSize (f);
                }
                return size;
            } else
            {
                // 如果是文件则直接返回以兆为单位
                double size = (double) file.length () / 1024 / 1024;
                return size;
            }
        } else
        {
            return 0.0;
        }
    }

    RequestParams rp = new RequestParams ();

    @Override
    public void onClick (View v)
    {
        memberId = sp.getString ("memberId", "");
        switch (v.getId ())
        {
            case R.id.my_collect:
                if ( Tools.isEmpty (memberId) )
                {
                    Tools.dialog (mainActivity,
                            Tools.getStr (fragment, R.string.REQUESTLOGIN),
                            true,new onButtonClick ()
                            {
                                @Override
                                public void buttonClick ()
                                {
                                    gotoActivity (LoginActivity.class);
                                }
                            });
                } else
                {
                    gotoActivity (MyCollectionActivity.class);
                }

                break;
            case R.id.my_scored:
                gotoActivity (MyCouponsActivity.class);
                break;
            case R.id.my_address:
                gotoActivity (MyAddressActivity.class);
                break;
            case R.id.quit:
                quits ();
                break;
            case R.id.my_order:
                gotoActivity (MyOrderActivity.class);
                break;
            case R.id.my_judge:
                gotoActivity (MyJudgeListActivity.class);
                break;
            case R.id.my_hun:
                deleteCache (diskCachePath);
                deleteCache (fileCache);
                Tools.toast (getActivity (), "清除缓存成功");
                clearCache.setText ("清除缓存(0.0M)");
                // mainActivity.updateUser();
                // mainActivity.setTabSelection(0);
                break;
        }

    }

    private void quits ()
    {
       dialogProgress ("正在退出……");
        RequestParams rp = new RequestParams ();
        rp.addBodyParameter ("member_id", memberId);
        HttpUtils localHttpUtils = new HttpUtils ();
        localHttpUtils.send (HttpMethod.POST, MyConstant.URLQUITLOGIN, rp,
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
                        String result = paramResponseInfo.result;
                        try
                        {
                            JSONObject jsonResul = new JSONObject (result);
                            String isResul = jsonResul.getString ("code");
                            if ( isResul.equals ("200") )
                            {
                                String re = jsonResul.getString ("islogin");
                                if ( re.equals ("0") )
                                {
                                    Tools.toast (getActivity (), "退出登陆");
                                    SharedPreferences sharedPreferences = getActivity ()
                                            .getSharedPreferences (MyConstant.FILE_NAME,
                                                    Context.MODE_PRIVATE);
                                    SharedPreferences.Editor editor = sharedPreferences
                                            .edit ();
                                    editor.remove (MyConstant.KEY_MEMBERID);
                                    editor.remove (MyConstant.KEY_ADDCON);
                                    editor.remove (MyConstant.KEY_ADDCONTACT);
                                    editor.remove (MyConstant.KEY_ADDMOBILE);
                                    editor.remove (MyConstant.KEY_ADDREMARK);
                                    editor.apply ();
                                    mainActivity.setQuit ();
                                    PushManager.unbind (mainActivity);
                                    // PushManager.stopWork (mainActivity);
                                } else
                                {
                                    Tools.toast (getActivity (), "注销失败，请重试");
                                }
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

    public void gotoActivity (Class<?> cls)
    {
        startActivity (new Intent (getActivity (), cls));
        AnimUtil.animToSlide (getActivity ());
    }

}
