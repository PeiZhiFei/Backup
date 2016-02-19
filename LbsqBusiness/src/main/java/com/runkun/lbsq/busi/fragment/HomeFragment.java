package com.runkun.lbsq.busi.fragment;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.runkun.lbsq.busi.R;
import com.runkun.lbsq.busi.activity.CategoryActivity;
import com.runkun.lbsq.busi.activity.JudgeActivity;
import com.runkun.lbsq.busi.activity.OrderActivity;
import com.runkun.lbsq.busi.activity.TestActivity;
import com.runkun.lbsq.busi.util.AnimUtil;
import com.runkun.lbsq.busi.util.MyConstant;

import org.json.JSONException;
import org.json.JSONObject;

import butterknife.ButterKnife;
import butterknife.InjectView;
import butterknife.OnClick;
import feifei.project.util.ConfigUtil;
import feifei.project.view.widget.WaveSwipeRefreshLayout;

public class HomeFragment extends NetFragment
{

    @InjectView(R.id.shop_name)
    TextView shopName;
    @InjectView(R.id.order_count)
    TextView orderCount;
    @InjectView(R.id.submit_money)
    TextView submitMoney;
    @InjectView(R.id.main_swipe)
    WaveSwipeRefreshLayout mainSwipe;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState)
    {
        super.onCreateView(inflater, container, savedInstanceState);
        View view = inflater.inflate(R.layout.fragment_home, container, false);
        ButterKnife.inject(this, view);
        url = MyConstant.CSTORESUBMIT;
        rp.addQueryStringParameter("store_id", ConfigUtil.readString (fragment, MyConstant.KEY_STOREID, ""));
        shopName.setText(ConfigUtil.readString(fragment, MyConstant.KEY_STORENAME, ""));
        mainSwipe.setColorSchemeColors(Color.WHITE);
        mainSwipe.setOnRefreshListener(new WaveSwipeRefreshLayout.OnRefreshListener()
        {
            @Override
            public void onRefresh()
            {
                loadData();
            }
        });
        mainSwipe.setWaveColor(Color.parseColor("#39ac69"));
        loadData();
        return view;
    }

    @Override
    public void onResume()
    {
        super.onResume();
    }

    @OnClick({R.id.order_submit, R.id.goods_manage, R.id.judegs,R.id.state})
    public void click(TextView button)
    {
        switch (button.getId())
        {
            case R.id.order_submit:
                startActivity(new Intent(fragment, OrderActivity.class));
                AnimUtil.animTo((Activity) fragment);
                break;
            case R.id.goods_manage:
                startActivity(new Intent(fragment, CategoryActivity.class));
                AnimUtil.animTo((Activity) fragment);
                break;
            case R.id.judegs:
                startActivity(new Intent(fragment, JudgeActivity.class));
                AnimUtil.animTo((Activity) fragment);
                break;
            case R.id.state:
                startActivity(new Intent(fragment, TestActivity.class));
                AnimUtil.animTo((Activity) fragment);
                break;
        }

    }

    @Override
    public void onDestroyView()
    {
        super.onDestroyView();
        ButterKnife.reset(this);
    }

    @Override
    protected void getData(JSONObject jsonResult) throws JSONException
    {
        JSONObject jsonObject = jsonResult.getJSONObject("datas");
        String countorder = jsonObject.getString("countorder");
        String totalorder = jsonObject.getString("totalorder");
        orderCount.setText(countorder);
        submitMoney.setText(totalorder);
        AnimUtil.animShow(orderCount);
        AnimUtil.animShow(submitMoney);
        mainSwipe.setRefreshing(false);
    }

    protected void error(String s)
    {
        super.error(s);
        mainSwipe.setRefreshing(false);
    }
}
