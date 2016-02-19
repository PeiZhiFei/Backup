/*
 * Copyright (C) 2013 Andreas Stuetz <andreas.stuetz@gmail.com>
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package feifei.dataanalysis.activity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.ColorStateList;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.View;
import android.widget.TextView;

import com.umeng.update.UmengUpdateAgent;

import butterknife.ButterKnife;
import butterknife.InjectView;
import butterknife.OnClick;
import feifei.dataanalysis.R;
import feifei.dataanalysis.base.MyApplication;
import feifei.dataanalysis.base.OrderListActivity;
import feifei.dataanalysis.fragment.HorizontalFragmentCount;
import feifei.dataanalysis.fragment.HorizontalFragmentMoney;
import feifei.dataanalysis.fragment.LineChartFragmentBase;
import feifei.dataanalysis.fragment.PieChartFragmentCount;
import feifei.dataanalysis.fragment.PieChartFragmentMoney;
import feifei.project.util.AnimUtil;
import feifei.project.util.Tools;
import feifei.project.view.PagerSlidingTabStrip;
import feifei.project.view.percent.PercentLinearLayout;
import feifei.project.view.widget.WaveSwipeRefreshLayout;

public class MainActivity extends DataActivity {

    @InjectView(R.id.tabs)
    PagerSlidingTabStrip tabs;
    @InjectView(R.id.pager)
    ViewPager pager;
    @InjectView(R.id.main_swipe)
    WaveSwipeRefreshLayout mainSwipe;

    @InjectView(R.id.order_money)
    TextView orderMoney;
    @InjectView(R.id.order_count)
    TextView orderCount;
    @InjectView(R.id.store_count)
    TextView storeCount;
    @InjectView(R.id.all_money)
    TextView allOrderMoney;
    @InjectView(R.id.all_count)
    TextView allOrderCount;

    @InjectView(R.id.test)
    PercentLinearLayout testLayout;
    private MyPagerAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        if (Build.VERSION.SDK_INT > Build.VERSION_CODES.ICE_CREAM_SANDWICH) {
            setSwipeBackEnable(false);
        }
        ButterKnife.inject(this);
        tint();
        UmengUpdateAgent.update(this);
        getData(true);
        mainSwipe.setColorSchemeColors(Color.WHITE);
        mainSwipe.setOnRefreshListener(new WaveSwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                //                url = Tools.URL + "getTodayData";
                //                loadData(false, "simple");
                getData(false);
            }
        });
//        mainSwipe.setWaveColor(getResources().getColor(R.color.main_green));
        mainSwipe.setWaveColor(Color.parseColor("#84ce08"));

        adapter = new MyPagerAdapter(getSupportFragmentManager());

        tabs.setTextColor(new ColorStateList(new int[][]{
                new int[]{android.R.attr.state_pressed}, //pressed
                new int[]{android.R.attr.state_selected}, // enabled
                new int[]{} //default
        },
                new int[]{
                        getResources().getColor(R.color.red),
                        getResources().getColor(R.color.red),
                        getResources().getColor(R.color.darkgray2)
                }));
//        final int pageMargin = (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 4, getResources()
//                .getDisplayMetrics());
//        pager.setPageMargin(pageMargin);
        pager.setCurrentItem(0);
        tabs.setBackgroundColor(getResources().getColor(R.color.white));


        orderCount.setText(todayCount);
        orderMoney.setText(todayMoney);
        storeCount.setText(allStore);
        allOrderCount.setText("订单总数:  " + allCount);
        allOrderMoney.setText("营业总额:  " + allMoney);

        SharedPreferences sp = getSharedPreferences(MyApplication.KEY_LOGIN_FILE, MODE_PRIVATE);
        SharedPreferences.Editor editor = sp.edit();
        boolean first = sp.getBoolean(MyApplication.KEY_FIRST, true);
        if (first) {
            initHotWords();
            editor.putBoolean(MyApplication.KEY_FIRST, false).apply();
        }

        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                AnimUtil.animScaleAlpha (testLayout);
                testLayout.setVisibility(View.VISIBLE);
                AnimUtil.animScaleAlpha(allOrderMoney);
                allOrderMoney.setVisibility(View.VISIBLE);
                AnimUtil.animScaleAlpha(allOrderCount);
                allOrderCount.setVisibility(View.VISIBLE);
            }
        }, 100);

        dialogInit();
        //        MaterialShowcaseView.resetSingleUse(this, "guide");
        //        MaterialShowcaseView.resetSingleUse(this, "test");
        //        presentShowcaseSequence();
        //        presentShowcaseView(2000);
        //        url = Tools.URL + "getTodayData";
        //        loadData(false, "simple");

        //        url = Tools.URL + "getMonthData";
        //        loadData(true, "30");

//        presentShowcaseView(1000);
    }

    @OnClick(R.id.money_layout)
    public void money(final View view) {
        AnimUtil.shake(view);
        if (allOrderCount.getVisibility() == View.VISIBLE) {
            allOrderCount.setVisibility(View.GONE);
        } else {
            allOrderCount.setVisibility(View.VISIBLE);
        }

        if (allOrderMoney.getVisibility() == View.VISIBLE) {
            allOrderMoney.setVisibility(View.GONE);
        } else {
            allOrderMoney.setVisibility(View.VISIBLE);
        }
        //        startActivity(new Intent(activity, OrderListActivity2.class));
        //        AnimUtil.animTo(activity);
    }

    @OnClick(R.id.search_layout)
    public void search(final View view) {
        data(activity, "搜索");
        startActivity(new Intent(activity, SearchActivity.class));
        AnimUtil.animTo(activity);
    }

    @OnClick(R.id.order_layout)
    public void orderList(final View view) {
        data(activity, "订单");
        startActivity(new Intent(activity, OrderListActivity.class));
        AnimUtil.animTo(activity);
    }


    @Override
    protected void onResult(int type) {
        switch (type) {
            case 1:
                orderCount.setText(todayCount);
                orderMoney.setText(todayMoney);
                storeCount.setText(allStore);
                Double money = Double.valueOf(allMoney);
                Double count = Double.valueOf(allCount);
                allOrderCount.setText("订单总数:  " + Tools.scale (count / 10000) + "万");
                allOrderMoney.setText("营业总额:  " + Tools.scale(money / 10000) + "万");
                AnimUtil.animShow(orderCount);
                AnimUtil.animShow(orderMoney);
                AnimUtil.animShow(storeCount);
                AnimUtil.animShow(allOrderCount);
                AnimUtil.animShow(allOrderMoney);
                break;
            case 2:
                if (first) {
                    pager.setAdapter(adapter);
                    tabs.setViewPager(pager);
                    first = false;
                } else {
                    adapter.notifyDataSetChanged();
                }
                mainSwipe.setRefreshing(false);
                break;
        }
    }

    boolean first = true;

    @Override
    protected void onError() {
        mainSwipe.setRefreshing(false);
    }


    protected void error(String s) {
        super.error(s);
        mainSwipe.setRefreshing(false);
    }


    public class MyPagerAdapter extends FragmentPagerAdapter {
        private final String[] TITLES = {"趋势概览", "7日营业额", "7日订单数", "30天营业额", "30天订单数"};
        private int mChildCount = 0;

        public MyPagerAdapter(FragmentManager fm) {
            super(fm);
        }

        @Override
        public CharSequence getPageTitle(int position) {
            return TITLES[position];
        }

        @Override
        public int getCount() {
            return TITLES.length;
        }

        @Override
        public void notifyDataSetChanged() {
            mChildCount = getCount();
            super.notifyDataSetChanged();
        }

        public int getItemPosition(Object object) {
            if (mChildCount > 0) {
                mChildCount--;
                return POSITION_NONE;
            }
            return super.getItemPosition(object);
        }

        @Override
        public Fragment getItem(int position) {
            switch (position) {
                case 0:
                    return LineChartFragmentBase.newInstance();
                case 1:
                    return PieChartFragmentMoney.newInstance ();
                case 2:
                    return PieChartFragmentCount.newInstance ();
                case 3:
                    return HorizontalFragmentMoney.newInstance();
                case 4:
                    return HorizontalFragmentCount.newInstance();
                default:
                    return HorizontalFragmentCount.newInstance();
            }

        }
    }

    private void initHotWords() {
        SharedPreferences sp = getSharedPreferences(MyApplication.KEY_HOTWORD_FILE, MODE_PRIVATE);
        SharedPreferences.Editor editor = sp.edit();
        editor.putString("32", "益万家超市");
        editor.putString("83", "翡翠清河超市");
        editor.putString("23", "瑞隆超市");
        editor.putString("89", "时尚涮吧");
        editor.putString("92", "瑞鑫超市");
        editor.putString("29", "零步社区");
        editor.putString("100", "快客超市");
        editor.putString("14", "零步社区车库超市");
        editor.apply();
    }

    //    public float[] orderCount7 = new float[7];
    //    public float[] orderMoney7 = new float[7];
    //    public String[] orderTime7 = new String[7];
    //
    //    public float[] orderCount30 = new float[30];
    //    public float[] orderMoney30 = new float[30];
    //    public String[] orderTime30 = new String[30];

    //    @Override
    //    protected void getData(JSONArray jsonArray, String tag) throws JSONException {
    //        switch (tag) {
    //            case "simple":
    //                JSONObject jsonObject = (JSONObject) jsonArray.get(0);
    //                String todayCount = jsonObject.getString("order_count");
    //                String todayMoney = jsonObject.getString("order_money");
    //                String allStore = jsonObject.getString("store_count");
    //                orderCount.setText(todayCount);
    //                orderMoney.setText(todayMoney);
    //                storeCount.setText(allStore);
    //                AnimUtil.animShow(orderCount);
    //                AnimUtil.animShow(orderMoney);
    //                AnimUtil.animShow(storeCount);
    //                mainSwipe.setRefreshing(false);
    //                break;
    //            case "30":
    //                for (int i = 0; i < orderCount7.length; i++) {
    //                    orderCount7[i] = Float.valueOf(jsonArray.getJSONObject(i).getString("order_count"));
    //                    orderMoney7[i] = Float.valueOf(jsonArray.getJSONObject(i).getString("order_money"));
    //                    orderTime7[i] = Tools.formatMysqlTimestamp(jsonArray.getJSONObject(i).getString("order_time"), "MM-dd");
    //                }
    //                for (int i = 0; i < orderCount30.length; i++) {
    //                    orderCount30[i] = Float.valueOf(jsonArray.getJSONObject(i).getString("order_count"));
    //                    orderMoney30[i] = Float.valueOf(jsonArray.getJSONObject(i).getString("order_money"));
    //                    orderTime30[i] = Tools.formatMysqlTimestamp(jsonArray.getJSONObject(i).getString("order_time"), "MM-dd");
    //                }
    //                pager.setAdapter(adapter);
    //                tabs.setViewPager(pager);
    //                break;
    //        }
    //    }

//    private void presentShowcaseView(int withDelay) {
//        new MaterialShowcaseView.Builder(this)
//                .setTarget(orderMoney)
//                .setDismissText("OK")
//                .setContentText("这里显示今天的总交易额")
//                .setDelay(withDelay) // optional but starting animations immediately in onCreate can make them choppy
//                .singleUse("test") // provide a unique ID used to ensure it is only shown once
//                .show();
//    }


}