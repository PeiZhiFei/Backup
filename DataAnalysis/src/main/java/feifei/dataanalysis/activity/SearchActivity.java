package feifei.dataanalysis.activity;

import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Handler;
import android.view.Gravity;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.ScrollView;
import android.widget.TextView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import feifei.dataanalysis.R;
import feifei.dataanalysis.base.MyApplication;
import feifei.dataanalysis.base.NetActivity;
import feifei.dataanalysis.bean.HotWords;
import feifei.dataanalysis.fragment.HorizontalFragmentCount;
import feifei.dataanalysis.fragment.HorizontalFragmentMoney;
import feifei.dataanalysis.fragment.LineChartFragment30;
import feifei.dataanalysis.fragment.LineChartFragment7;
import feifei.dataanalysis.fragment.PieChartFragmentCount;
import feifei.dataanalysis.fragment.PieChartFragmentMoney;
import feifei.project.util.L;
import feifei.project.util.Tools;
import feifei.project.view.ClearEditText;
import feifei.project.view.FlowLayout;

public class SearchActivity extends NetActivity implements View.OnClickListener {
    ClearEditText edit;
    FlowLayout flowLayout;
    LinearLayout hotLayout;
    FlowLayout resultFlowLayout;
    LinearLayout resultLayout;
    ScrollView scrollView;
    String temp = null;

    public String[] orderTime7 = new String[7];
    public float[] orderCount7 = new float[7];
    public float[] orderMoney7 = new float[7];

    public float[] orderCount30 = new float[30];
    public float[] orderMoney30 = new float[30];
    public String[] orderTime30 = new String[30];

    List<HotWords> result = new ArrayList<>();
    List<HotWords> hotwords = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search);
        tint();
        dialogInit();
        edit = (ClearEditText) findViewById(R.id.edit);
        edit.setListener(new ClearEditText.onRightClick() {
            @Override
            public void rightClick() {
                scrollView.setVisibility(View.GONE);
                resultLayout.setVisibility(View.GONE);
                if (hotwords.size() > 0) {
                    hotLayout.setVisibility(View.VISIBLE);
                }
            }
        });

//        edit.setOnKeyListener(new View.OnKeyListener()
//        {
//            @Override
//            public boolean onKey(View v, int keyCode, KeyEvent event)
//            {
//                if (keyCode == KeyEvent.KEYCODE_ENTER)
//                {
//                    preSearch();
//                }
//                return false;
//            }
//        });

        flowLayout = (FlowLayout) findViewById(R.id.flow_layout);
        hotLayout = (LinearLayout) findViewById(R.id.hot_layout);
        resultFlowLayout = (FlowLayout) findViewById(R.id.result_flow_layout);
        resultLayout = (LinearLayout) findViewById(R.id.result_layout);
        scrollView = (ScrollView) findViewById(R.id.scrollview);
        Button button = (Button) findViewById(R.id.search);
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                preSearch();
            }
        });

        //取热词
        sp = getSharedPreferences(MyApplication.KEY_HOTWORD_FILE, MODE_PRIVATE);
        editor = sp.edit();

        HashMap<String, String> map = (HashMap<String, String>) sp.getAll();

        Iterator iter = map.entrySet().iterator();
        while (iter.hasNext()) {
            Map.Entry entry = (Map.Entry) iter.next();
            HotWords hotWords = new HotWords((String) entry.getKey(), (String) entry.getValue());
            hotwords.add(hotWords);
        }

        if (hotwords.size() > 0) {
            //todo 这里也该限制一下
            for (int i = 0; i < hotwords.size(); i++) {
                addText(i);
            }
        } else {
            hotLayout.setVisibility(View.GONE);
        }

        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                Tools.keyboardShow(edit);
            }
        }, 600);

    }

    private void preSearch() {
        String s = edit.getText().toString().trim();
        if (Tools.isEmpty(s)) {
            Tools.toast(activity, "请搜索");
            Tools.shake(edit);
        } else if (!s.equals(temp)) {
            temp = s;
            search(s);
        } else {
            Tools.toast(activity, "请换个词吧");
        }
    }


    SharedPreferences sp;
    SharedPreferences.Editor editor;

    public void search(String s) {
        scrollView.setVisibility(View.GONE);
        hotLayout.setVisibility(View.VISIBLE);
        url = Tools.URL + "searchStore";
        mParams.clear();
        mParams.put("store_name", s);
        data(activity, "点击搜索" + s);
        loadData(true, "searchStore");
    }

    //热词
    protected void addText(int i) {
        TextView textView = new TextView(this);

        LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(
                (Tools.getWidth(activity) - 10) / 5, (Tools.getWidth(activity) - 10) / 5);
        layoutParams.setMargins(30, 30, 0, 0);
        textView.setLayoutParams(layoutParams);
        textView.setGravity(Gravity.CENTER);
        textView.setPadding(20, 20, 20, 20);
        textView.setTextColor(Color.WHITE);
        textView.setClickable(true);
        //TODO 这里的tag是id
        textView.setTag(i);
        textView.setHint("hotword");
        textView.setTextSize(12);
        textView.setBackgroundResource(R.drawable.shape_circle_orange);
        textView.setOnClickListener(this);
        textView.setText(hotwords.get(i).getStoreName());
        flowLayout.addView(textView);
    }

    //搜索结果
    protected void addText2(int i) {
        TextView textView = new TextView(this);
        LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(
                -2, 100);
        layoutParams.setMargins(30, 30, 0, 0);
        textView.setLayoutParams(layoutParams);
        textView.setGravity(Gravity.CENTER);
        textView.setPadding(20, 20, 20, 20);
        textView.setTextColor(getResources().getColor(R.color.main_green));
        textView.setClickable(true);
        //TODO 这里的tag是id
        textView.setTag(i);
        textView.setHighlightColor(Color.RED);
        textView.setHint("result");
        textView.setTextSize(14);
        textView.setBackgroundResource(R.drawable.tv_round_border);
        textView.setOnClickListener(this);
        textView.setText(result.get(i).getStoreName());
        resultFlowLayout.addView(textView);
    }


    @Override
    public void onClick(View v) {
        int tag = (int) v.getTag();
        L.l(tag);
        mParams.clear();
        //从搜索结果来
        if (((TextView) v).getHint().equals("result")) {
            mParams.put("store_id", result.get(tag).getID());
            edit.setText(result.get(tag).getStoreName());
            edit.setSelection(edit.getText().length());
            data(activity, "搜索结果点击" + result.get(tag).getStoreName());
            //存热词
            if (!sp.contains(result.get(tag).getID())) {
                editor.putString("" + tag, result.get(tag).getStoreName()).apply();
                L.l(result.get(tag).getStoreName());
                L.l(result.get(tag).getID());
            }
        } else {
            mParams.put("store_id", hotwords.get(tag).getID());
            edit.setText(hotwords.get(tag).getStoreName());
            edit.setSelection(edit.getText().length());
            data(activity, "热词点击" + hotwords.get(tag).getStoreName());
            //存热词
            if (!sp.contains(hotwords.get(tag).getID())) {
                editor.putString("" + tag, hotwords.get(tag).getStoreName()).apply();
                L.l(hotwords.get(tag).getStoreName());
                L.l(hotwords.get(tag).getID());
            }
        }


        hotLayout.setVisibility(View.GONE);
        resultLayout.setVisibility(View.GONE);
//        Tools.toast(activity, "正在查询数据并生成报表");

        url = Tools.URL + "getStoreInfo";
        loadData(true, "chart");
    }

    @Override
    protected void getData(JSONArray jsonArray, String tag) throws JSONException {
        switch (tag) {
            case "searchStore":
                //todo 这里根据s去做请求，返回列表的结果
                //如果result > 0
                result.clear();
                resultFlowLayout.removeAllViews();

                // TODO: 2015/12/19 测试代码
                if (!MyApplication.NEEDNET) {
                    for (int i = 0; i < 10; i++) {
                        HotWords hotWords = new HotWords();
                        hotWords.setID("store_id" + i);
                        hotWords.setStoreName("超市" + i);
                        result.add(hotWords);
                    }
                } else {
                    for (int i = 0; i < jsonArray.length(); i++) {
                        JSONObject jsonObject = jsonArray.getJSONObject(i);
                        HotWords hotWords = new HotWords();
                        hotWords.setID(jsonObject.getString("store_id"));
                        hotWords.setStoreName(jsonObject.getString("store_name"));
                        result.add(hotWords);
                    }
                }

                if (result.size() > 0) {
                    resultFlowLayout.removeAllViews();
                    //这里原来是12，取个最小值
                    for (int i = 0; i < result.size(); i++) {
                        addText2(i);
                        resultLayout.setVisibility(View.VISIBLE);
                    }
                } else {
                    resultLayout.setVisibility(View.GONE);
                    Tools.toast(activity, "暂无搜索结果");
                }
                break;
            case "chart":
                // TODO: 2015/12/19 测试代码 
                if (!MyApplication.NEEDNET) {
                    for (int i = 0; i < orderCount7.length; i++) {
                        orderCount7[i] = 5 + i * i * i;
                        orderMoney7[i] = 1000 + i * i * i + 3;
                        orderTime7[i] = "090" + i;
                    }
                    for (int i = 0; i < orderCount30.length; i++) {
                        orderCount30[i] = 5 + i * i * i;
                        orderMoney30[i] = 1000 + i * i * i + 3;
                        orderTime30[i] = "090" + i;
                    }
                    scrollView.setVisibility(View.VISIBLE);
                    //回滚到顶部
                    scrollView.scrollTo(0, 30);
                    getSupportFragmentManager().beginTransaction().replace(R.id.fragment1, LineChartFragment7.newInstance()).commit();
                    getSupportFragmentManager().beginTransaction().replace(R.id.fragment2, LineChartFragment30.newInstance()).commit();
                    getSupportFragmentManager().beginTransaction().replace(R.id.fragment3, PieChartFragmentMoney.newInstance()).commit();
                    getSupportFragmentManager().beginTransaction().replace(R.id.fragment4, PieChartFragmentCount.newInstance()).commit();
                    getSupportFragmentManager().beginTransaction().replace(R.id.fragment5, HorizontalFragmentMoney.newInstance()).commit();
                    getSupportFragmentManager().beginTransaction().replace(R.id.fragment6, HorizontalFragmentCount.newInstance()).commit();
                    return;
                }

                if (jsonArray.length() > 0) {
                    for (int i = 0; i < orderCount7.length; i++) {
                        orderCount7[i] = Float.valueOf(jsonArray.getJSONObject(i).getString("order_count"));
                        orderMoney7[i] = Float.valueOf(jsonArray.getJSONObject(i).getString("order_money"));
                        orderTime7[i] = Tools.formatMysqlTimestamp(jsonArray.getJSONObject(i).getString("order_time"), "MM-dd");
                    }
                    for (int i = 0; i < orderCount30.length; i++) {
                        orderCount30[i] = Float.valueOf(jsonArray.getJSONObject(i).getString("order_count"));
                        orderMoney30[i] = Float.valueOf(jsonArray.getJSONObject(i).getString("order_money"));
                        orderTime30[i] = Tools.formatMysqlTimestamp(jsonArray.getJSONObject(i).getString("order_time"), "MM-dd");
                    }

//                    for (int i = 0; i < orderCount30.length; i++)
//                    {
//                        L.l ("时间：" + orderTime30[i]);
//                        L.l ("订单数：" + orderCount30[i]);
//                        L.l ("订单金额：" + orderMoney30[i]);
//                    }

                    scrollView.setVisibility(View.VISIBLE);
                    //回滚到顶部
                    scrollView.scrollTo(0, 30);
                    getSupportFragmentManager().beginTransaction().replace(R.id.fragment1, LineChartFragment7.newInstance()).commit();
                    getSupportFragmentManager().beginTransaction().replace(R.id.fragment2, LineChartFragment30.newInstance()).commit();
                    getSupportFragmentManager().beginTransaction().replace(R.id.fragment3, PieChartFragmentMoney.newInstance()).commit();
                    getSupportFragmentManager().beginTransaction().replace(R.id.fragment4, PieChartFragmentCount.newInstance()).commit();
                    getSupportFragmentManager().beginTransaction().replace(R.id.fragment5, HorizontalFragmentMoney.newInstance()).commit();
                    getSupportFragmentManager().beginTransaction().replace(R.id.fragment6, HorizontalFragmentCount.newInstance()).commit();
                } else {
                    Tools.toast(activity, "");
                }
                break;
        }
    }
}
