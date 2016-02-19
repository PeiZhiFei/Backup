package com.runkun.lbsq.busi.fragment;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.runkun.lbsq.busi.R;
import com.runkun.lbsq.busi.activity.FeedBackActivity;
import com.runkun.lbsq.busi.activity.ForgetPasswordActivity;
import com.runkun.lbsq.busi.activity.LoginActivity;
import com.runkun.lbsq.busi.util.AnimUtil;
import com.runkun.lbsq.busi.util.MyConstant;
import com.runkun.lbsq.busi.util.Tools;
import com.runkun.lbsq.busi.view.pull.PullToZoomScrollViewEx;

import org.json.JSONException;
import org.json.JSONObject;

import feifei.project.util.ConfigUtil;

public class MyFragment extends NetFragment implements
        View.OnClickListener
{
    //    RoundSmartImageView photo;
    TextView phone;

    Button contactAdmin;
    Button kefu;
    Button changePassword;
    CheckBox shopState;
    Button feedback;
    Button quit;
    PullToZoomScrollViewEx scrollView;
    boolean state = false;
    int type;//第一个请求

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState)
    {
        super.onCreateView(inflater, container, savedInstanceState);
        View view = inflater.inflate(R.layout.fragment_my, container, false);
        dialogInit();
        type = 0;
        scrollView = (PullToZoomScrollViewEx) view.findViewById(R.id.scroll_view);
        View headView = LayoutInflater.from(getActivity()).inflate(
                R.layout.fragment_my_header, container, false);
        View zoomView = LayoutInflater.from(getActivity()).inflate(
                R.layout.fragment_my_image, container, false);
        View contentView = LayoutInflater.from(getActivity()).inflate(
                R.layout.fragment_my_content, container, false);
        scrollView.setHeaderView(headView);
        scrollView.setZoomView(zoomView);
        scrollView.setScrollContentView(contentView);

        DisplayMetrics localDisplayMetrics = new DisplayMetrics();
        getActivity().getWindowManager().getDefaultDisplay().getMetrics(localDisplayMetrics);
        int mScreenHeight = localDisplayMetrics.heightPixels;
        int mScreenWidth = localDisplayMetrics.widthPixels;
        LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(
                mScreenWidth, (int) (mScreenHeight / 5.0F));
        params.gravity = Gravity.CENTER_VERTICAL;
        scrollView.setHeaderLayoutParams(params);

//        photo = (RoundSmartImageView) headView.findViewById(R.id.riv_image);
        phone = (TextView) headView.findViewById(R.id.tv_phone);
        phone.setText(ConfigUtil.readString (fragment, MyConstant.KEY_STOREACCOUNT, ""));

        contactAdmin = (Button) contentView.findViewById(R.id.contact_admin);
        kefu = (Button) contentView.findViewById(R.id.kefu);
        changePassword = (Button) contentView.findViewById(R.id.change_password);
        shopState = (CheckBox) contentView.findViewById(R.id.shop_state);
        feedback = (Button) contentView.findViewById(R.id.feedback);
        quit = (Button) contentView.findViewById(R.id.quit);

        contactAdmin.setOnClickListener(this);
        kefu.setOnClickListener(this);
        changePassword.setOnClickListener(this);
        feedback.setOnClickListener(this);
        quit.setOnClickListener(this);

        // TODO: 2015/8/22  添加商家状态，需要网络请求
        url = MyConstant.CGETSTATE;
        rp.addQueryStringParameter("store_id", ConfigUtil.readString(fragment, MyConstant.KEY_STOREID, ""));

        loadData();

        url = MyConstant.CSETSTATE;
        shopState.setOnClickListener(new CompoundButton.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                type = 1;
                //点击之后的状态变了
                state = shopState.isChecked();
                rp.addQueryStringParameter("is_open", state ? "1" : "0");
                loadData();
            }
        });

        return view;
    }

    @Override
    public void onClick(View v)
    {
        switch (v.getId())
        {
            case R.id.kefu:
//                IntentUtil.intentToDial(fragment, "4000279567");
                AnimUtil.animTo((Activity) fragment);
                break;
            case R.id.change_password:
                gotoActivity(ForgetPasswordActivity.class);
                break;
            case R.id.feedback:
                gotoActivity(FeedBackActivity.class);
                break;
            case R.id.quit:
                Tools.clearShopInfo(fragment);
                startActivity(new Intent(fragment, LoginActivity.class));
                AnimUtil.animBackFinish((Activity) fragment);
                break;
        }
    }

    public void gotoActivity(Class<?> cls)
    {
        startActivity(new Intent(getActivity(), cls));
        AnimUtil.animTo(getActivity());
    }


    @Override
    protected void getData(JSONObject jsonResult) throws JSONException
    {
        String result = jsonResult.getString("datas");
        boolean success = result.equals("1");
        //查询状态接口
        if (type == 0)
        {
            shopState.setChecked(success);
        }
        //修改状态接口
        else
        {
            Tools.toast(fragment, success ? "修改成功" : "修改失败");
            shopState.setChecked(success ? state : !state);
        }
    }

    @Override
    protected void error(String s)
    {
        super.error(s);
        shopState.setChecked(!state);
    }
}

