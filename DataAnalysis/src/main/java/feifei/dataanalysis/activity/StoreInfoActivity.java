package feifei.dataanalysis.activity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ScrollView;
import android.widget.TextView;

import org.json.JSONArray;
import org.json.JSONException;

import butterknife.ButterKnife;
import butterknife.InjectView;
import feifei.dataanalysis.R;
import feifei.dataanalysis.base.MyApplication;
import feifei.dataanalysis.base.NetActivity;
import feifei.dataanalysis.fragment.HorizontalFragmentCount;
import feifei.dataanalysis.fragment.HorizontalFragmentMoney;
import feifei.dataanalysis.fragment.LineChartFragment30;
import feifei.dataanalysis.fragment.LineChartFragment7;
import feifei.dataanalysis.fragment.PieChartFragmentCount;
import feifei.dataanalysis.fragment.PieChartFragmentMoney;
import feifei.project.util.Tools;

public class StoreInfoActivity extends NetActivity {

    public String[] orderTime7 = new String[7];
    public float[] orderCount7 = new float[7];
    public float[] orderMoney7 = new float[7];

    public float[] orderCount30 = new float[30];
    public float[] orderMoney30 = new float[30];
    public String[] orderTime30 = new String[30];

    @InjectView(R.id.scrollview)
    ScrollView scrollview;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_store_info);
        ButterKnife.inject(this);
        tint();
        dialogInit();
        Intent intent = getIntent();
        String id = intent.getStringExtra("id");
        ((TextView) findViewById(R.id.store_name)).setText(intent.getStringExtra("storeName"));
//        Tools.toast(activity, "正在查询数据并生成报表");
        mParams.put("store_id", id);
        url = Tools.URL + "getStoreInfo";
        loadData(true, "chart");
    }

    @Override
    protected void getData(JSONArray jsonArray, String tag) throws JSONException {
        // TODO: 2015/12/20 测试代码
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
            scrollview.setVisibility(View.VISIBLE);
            getSupportFragmentManager().beginTransaction().replace(R.id.fragment1, LineChartFragment7.newInstance()).commit();
            getSupportFragmentManager().beginTransaction().replace(R.id.fragment2, LineChartFragment30.newInstance()).commit();
            getSupportFragmentManager().beginTransaction().replace(R.id.fragment3, PieChartFragmentMoney.newInstance()).commit();
            getSupportFragmentManager().beginTransaction().replace(R.id.fragment4, PieChartFragmentCount.newInstance()).commit();
            getSupportFragmentManager().beginTransaction().replace(R.id.fragment5, HorizontalFragmentMoney.newInstance()).commit();
            getSupportFragmentManager().beginTransaction().replace(R.id.fragment6, HorizontalFragmentCount.newInstance()).commit();
            return;
        }

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
        scrollview.setVisibility(View.VISIBLE);
        getSupportFragmentManager().beginTransaction().replace(R.id.fragment1, LineChartFragment7.newInstance()).commit();
        getSupportFragmentManager().beginTransaction().replace(R.id.fragment2, LineChartFragment30.newInstance()).commit();
        getSupportFragmentManager().beginTransaction().replace(R.id.fragment3, PieChartFragmentMoney.newInstance()).commit();
        getSupportFragmentManager().beginTransaction().replace(R.id.fragment4, PieChartFragmentCount.newInstance()).commit();
        getSupportFragmentManager().beginTransaction().replace(R.id.fragment5, HorizontalFragmentMoney.newInstance()).commit();
        getSupportFragmentManager().beginTransaction().replace(R.id.fragment6, HorizontalFragmentCount.newInstance()).commit();
    }
}
