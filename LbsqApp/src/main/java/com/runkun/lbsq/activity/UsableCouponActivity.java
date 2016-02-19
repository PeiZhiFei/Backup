package com.runkun.lbsq.activity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;

import com.lidroid.xutils.ViewUtils;
import com.lidroid.xutils.view.annotation.ViewInject;
import com.runkun.lbsq.R;
import com.runkun.lbsq.adapter.CouponsAdapter;
import com.runkun.lbsq.bean.Coupons;
import com.runkun.lbsq.utils.Tools;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

/**
 * Created by yutong on 2015/8/5.
 * <p>展示可用优惠券供选择使用
 */
public class UsableCouponActivity extends BaseAcitivity
{
    @ViewInject(R.id.list)
    private ListView listView;

    @ViewInject(R.id.titlebar)
    private TextView title;

    @ViewInject(R.id.actionbar_right2)
    private Button searchBtn;

    private String storeId;
    private float totalFee;

    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_usable_coupon);
        ViewUtils.inject(this);

        initView();
    }

    private void initView()
    {
        tint();
        title.setText("优惠券");
        searchBtn.setVisibility(View.INVISIBLE);

        Intent intent = getIntent();

        storeId = intent.getStringExtra("storeId");
        totalFee = Float.valueOf(intent.getStringExtra("totalFee"));

        String couponsStr = intent.getStringExtra("coupons");
        try
        {
            JSONObject jo = new JSONObject(couponsStr);
            JSONArray ja = jo.getJSONArray("datas");

            final List<Coupons> orders = new ArrayList<>();
            CouponsAdapter adapter = new CouponsAdapter(this, orders, R.layout.item_coupons);
            listView.setAdapter(adapter);

            for (int i = 0; i < ja.length(); i++)
            {
                JSONObject couponJSON = ja.getJSONObject(i);
                Coupons coupons = new Coupons();
                //{"id":"47109","coupon_id":"25","member_id":"887","is_use":"0","type":"4","amount":"3","xianzhi":"0","end_time":"1438831860","store_id":null,"store_name":null}
                coupons.setEnd_time(couponJSON.getString("end_time"));
                coupons.setCoupon_id(couponJSON.getString("coupon_id"));
                coupons.setIs_guoqi("0");
                coupons.setIs_use("0");
                coupons.setMember_id(couponJSON.getString("member_id"));
                coupons.setStore_name(couponJSON.getString("store_name"));
                coupons.setAmount(couponJSON.getString("amount"));
                coupons.setId(couponJSON.getString("store_id"));
                coupons.setType(couponJSON.getString("type"));
                coupons.setXianzhi(couponJSON.getString("xianzhi"));
                orders.add(coupons);
            }
            adapter.notifyDataSetChanged();

            listView.setOnItemClickListener(new AdapterView.OnItemClickListener()
            {
                @Override
                public void onItemClick(AdapterView<?> parent, View view, int position, long id)
                {
                    Coupons coupon = orders.get(position);

                    String type = coupon.getType();
                    String couponStoreId = coupon.getId();
                    int limit = Integer.valueOf(coupon.getXianzhi());
                    int amount = Integer.valueOf(coupon.getAmount());

                    if (!Tools.isEmpty(type) && type.equals("2") && !couponStoreId.equals(storeId))
                    {
                        Tools.toast(UsableCouponActivity.this, "非本商家优惠券，无法使用");
                        return;
                    }
//                    if (totalFee - limit <= 0)
//                    {
//                        Tools.toast(activity, String.format(Locale.getDefault(), "您的消费金额未超过%d元，快去选购更多心仪商品吧～", limit));
//                        return;
//                    }
//                    if (totalFee - amount <= 0)
//                    {
//                        Tools.toast(activity, String.format(Locale.getDefault(), "您的消费金额未超过%d元，快去选购更多心仪商品吧～", amount));
//                        return;
//                    }

                    //todo
                    if (totalFee >= limit && totalFee > amount)
                    {
                        Intent intent = new Intent();
                        intent.putExtra("id", coupon.getCoupon_id());
                        intent.putExtra("amount", coupon.getAmount());
                        setResult(Activity.RESULT_OK, intent);
                        finish();
                    } else
                    {
                        Tools.toast(activity, String.format(Locale.getDefault(), "您的消费金额未超过%d元，快去选购更多心仪商品吧～", limit));
                    }


                }
            });
        } catch (JSONException ex)
        {
            ex.printStackTrace();
        }
    }
}
