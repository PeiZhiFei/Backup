package com.runkun.lbsq.fragment;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.lidroid.xutils.HttpUtils;
import com.lidroid.xutils.ViewUtils;
import com.lidroid.xutils.exception.HttpException;
import com.lidroid.xutils.http.RequestParams;
import com.lidroid.xutils.http.ResponseInfo;
import com.lidroid.xutils.http.callback.RequestCallBack;
import com.lidroid.xutils.http.client.HttpRequest.HttpMethod;
import com.lidroid.xutils.view.annotation.ViewInject;
import com.runkun.lbsq.R;
import com.runkun.lbsq.activity.BaseAcitivity;
import com.runkun.lbsq.activity.GoodDetailActivity;
import com.runkun.lbsq.activity.GoodMoreActivity;
import com.runkun.lbsq.activity.LoginActivity;
import com.runkun.lbsq.bean.Shop;
import com.runkun.lbsq.interfaces.onButtonClick;
import com.runkun.lbsq.utils.AnimUtil;
import com.runkun.lbsq.utils.MyConstant;
import com.runkun.lbsq.utils.Tools;
import com.runkun.lbsq.utils.XUtilsImageLoader;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import feifei.project.util.ConfigUtil;
import feifei.project.util.ToastUtil;

public class FruitShopFragment extends MainFirstBaseFragment implements
        OnClickListener
{
    @ViewInject(R.id.listview)
    private ListView dataView;
    @ViewInject(R.id.nodata)
    private LinearLayout noData;
    @ViewInject(R.id.nodataText)
    private TextView noDataText;
    @ViewInject(R.id.main_type_left)
    private TextView lTitle;
    @ViewInject(R.id.main_type_right)
    private TextView sTitle;
    @ViewInject(R.id.bgtitle)

    private RelativeLayout tiltp;
    private Integer pagenumber;
    private RequestParams rp;
    private String recordData;
    private Button more;
    private String shopName;
    private SharedPreferences sp;

    @Override
    public View onCreateView(LayoutInflater paramLayoutInflater,
                             ViewGroup viewGroup, Bundle bundle)
    {
        super.onCreateView(paramLayoutInflater, viewGroup, bundle);
        View view = paramLayoutInflater.inflate(R.layout.fragment_fruitshop,
                viewGroup, false);
        ViewUtils.inject(this, view);
        dialogInit();

        sp = getActivity().getSharedPreferences("lbsq", Context.MODE_PRIVATE);
        shopName = sp.getString(Shop.SHOPNAME, null);
        lTitle.setText(null == shopName || "".equals(shopName) ? "超值水果购"
                : shopName);
        lTitle.setTextColor(Color.RED);
        sTitle.setTextColor(Color.RED);
        tiltp.setBackgroundColor(Color.WHITE);
        sTitle.setOnClickListener(this);
        rp = new RequestParams();
        rp.addQueryStringParameter("store_id", storeId);
        pagenumber = 1;
        rp.addQueryStringParameter("pagenumber", String.valueOf(pagenumber));
        rp.addQueryStringParameter("pagesize", "5");
        reqMain(noData, rp, true);
        return view;
    }


    public void nextPage()
    {
        rp = new RequestParams();
        rp.addQueryStringParameter("store_id", storeId);
        pagenumber++;
        rp.addQueryStringParameter("pagenumber", String.valueOf(pagenumber));
        reqMain(noData, rp, null);
    }

    protected void reqMain(final View nodata, RequestParams rp,
                           final Boolean isFirst)
    {
        HttpUtils http = new HttpUtils();
        http.send(HttpMethod.POST, MyConstant.API_BASE_URL + "fruitlistmore",
                rp, new RequestCallBack<String>()
                {

                    @Override
                    public void onStart()
                    {
                        dialogProgress("请稍候");
                    }

                    @Override
                    public void onSuccess(ResponseInfo<String> responseInfo)
                    {
                        String result = responseInfo.result;

                        try
                        {
                            JSONObject data = new JSONObject(result);
                            JSONArray all = data.getJSONArray("datas");
                            if (all.length() == 0)
                            {
                                if (more != null)
                                {
                                    more.setText("暂无更多数据");
                                    more.setEnabled(false);
                                }
                            } else
                            {
                                String crec = null;
                                if (null != recordData)
                                {
                                    crec = recordData.substring(0,
                                            recordData.length() - 1);
                                    crec += ("," + all.toString().substring(1));
                                    recordData = crec;
                                    crec = null;
                                } else
                                {
                                    recordData = all.toString();
                                }
                                JSONArray useAll = new JSONArray(recordData);
                                MyAdapter myAdapter = new MyAdapter(useAll);
                                dataView.setAdapter(myAdapter);
                            }
                            if (null != isFirst && isFirst)
                            {
                                more = new Button(getActivity());
                                more.setText("加载更多");
                                more.setTextColor(Color.rgb(240, 154, 0));
                                more.setBackgroundResource(0);
                                dataView.addFooterView(more);
                            }
                            more.setOnClickListener(new OnClickListener()
                            {

                                @Override
                                public void onClick(View v)
                                {
                                    nextPage();
                                }
                            });
                            setListViewHeightBasedOnChildren(dataView);
                        } catch (JSONException e)
                        {
                            e.printStackTrace();
                            noData.setVisibility(View.VISIBLE);
                            noDataText.setText("数据请求失败");
                        }
                        dialogDismiss();
                    }

                    @Override
                    public void onFailure(HttpException error, String msg)
                    {
                        noData.setVisibility(View.VISIBLE);
                        noDataText.setText("此商店暂无该栏目的上架商品");
                        // no_data.setVisibility(View.VISIBLE);
                        dialogDismiss();
                        Tools.toast(getActivity(), "网络请求失败");
                    }
                });
    }

    public int AddOrSub(int count, Boolean isSub)
    {
        if (null != isSub && isSub)
        {
            count--;

        } else
        {
            count++;

        }
        return count++;
    }

    public class MyAdapter extends BaseAdapter
    {
        private JSONArray data;


        public MyAdapter(JSONArray data)
        {
            this.data = data;
        }

        @Override
        public int getCount()
        {
            return data.length();
        }

        @Override
        public JSONObject getItem(int position)
        {
            try
            {
                return data.getJSONObject(position);
            } catch (JSONException e)
            {
                e.printStackTrace();
                return null;
            }
        }

        @Override
        public long getItemId(int position)
        {
            return position;
        }

        @Override
        public View getView(final int position, View convertView,
                            ViewGroup parent)
        {
            if (convertView == null)
            {
                convertView = View.inflate(getActivity(),
                        R.layout.item_fruitgood, null);
                new ViewHolder(convertView);
            }
            try
            {
                final ViewHolder holder = (ViewHolder) convertView.getTag();
                holder.goodName.setText(data.getJSONObject(position).getString(
                        "goods_name"));
                holder.price
                        .setText("￥"
                                + data.getJSONObject(position).getString(
                                "goods_price"));

                new XUtilsImageLoader(getActivity(), R.drawable.zhanwei, R.drawable.zhanwei,
                        true, true).display(holder.goodImage, data.getJSONObject(position)
                        .getString("goods_pic"));

                // XUtilsImageLoader xut = new XUtilsImageLoader (getActivity (),holder.goodImage.getMeasuredWidth (),
                //      holder.goodImage.getMeasuredHeight ());
                // xut.display (holder.goodImage, data.getJSONObject (position).getString ("goods_pic"));

                final String goodsId = data.getJSONObject(position).getString(
                        "goods_id");
                String sc = holder.newcount.getText().toString().trim();
                if (sc == null || "".endsWith(sc))
                {
                    holder.newcount.setText("1");
                }
                holder.goodImage.setOnClickListener(new OnClickListener()
                {

                    @Override
                    public void onClick(View v)
                    {
                        Intent intent = new Intent();
                        intent.putExtra("goods_id", goodsId);
                        intent.setClass(getActivity(), GoodDetailActivity.class);
                        startActivity(intent);

                    }
                });

                holder.add.setOnClickListener(new OnClickListener()
                {

                    @Override
                    public void onClick(View v)
                    {
                        String count = holder.newcount.getText().toString()
                                .trim();
                        int result = AddOrSub(
                                null == count || "".endsWith(count) ? 0
                                        : Integer.valueOf(count), null);
                        holder.newcount.setText(String.valueOf(result));

                    }
                });
                holder.sub.setOnClickListener(new OnClickListener()
                {

                    @Override
                    public void onClick(View v)
                    {
                        String count = holder.newcount.getText().toString();
                        if (null != count && "0".equals(count))
                        {
                            ToastUtil.toast (getActivity (), "该商品已经清空");
                        } else
                        {
                            int result = AddOrSub(
                                    null == count || "".endsWith(count) ? 0
                                            : Integer.valueOf(count), true);
                            holder.newcount.setText(String.valueOf(result));

                        }
                    }
                });
                holder.fruitshopcard.setOnClickListener(new OnClickListener()
                {

                    @Override
                    public void onClick(View v)
                    {
                        String memberId = ConfigUtil.readString (getActivity (),
                                MyConstant.KEY_MEMBERID, "");
                        if (Tools.isEmpty(memberId))
                        {
                            Tools.dialog(getActivity(), Tools.getStr(fragment, R.string.REQUESTLOGIN),
                                    true, new onButtonClick()
                                    {

                                        @Override
                                        public void buttonClick()
                                        {
                                            getActivity()
                                                    .startActivity(
                                                            new Intent(
                                                                    getActivity(),
                                                                    LoginActivity.class));
                                        }
                                    });
                        } else
                        {
                            try
                            {
                                String count = holder.newcount.getText()
                                        .toString().trim();
                                RequestParams requestParams = new RequestParams();
                                requestParams.addQueryStringParameter(
                                        "goods_id", data
                                                .getJSONObject(position)
                                                .getString("goods_id"));
                                requestParams.addQueryStringParameter(
                                        "quantity",
                                        null == count || "".equals(count) ? "1"
                                                : count);
                                requestParams.addQueryStringParameter(
                                        "member_id", memberId);
                                ImageView aniImg = new ImageView(getActivity());
                                aniImg.setBackgroundDrawable(holder.goodImage.getDrawable());
                                Tools.addShopCard(
                                        (BaseAcitivity) getActivity(),
                                        "addshopcar", holder.goodImage, mainActivity.getShopcardLayout(),
                                        aniImg, requestParams, true);
                            } catch (JSONException e)
                            {
                                e.printStackTrace();
                            }
                        }

                    }
                });
            } catch (JSONException e)
            {
                e.printStackTrace();
            }
            return convertView;
        }

        class ViewHolder
        {
            ImageView goodImage;
            TextView price;
            TextView goodName;
            TextView newcount;
            ImageButton add;
            ImageButton sub;
            ImageButton fruitshopcard;

            public ViewHolder(View view)
            {
                goodImage = (ImageView) view.findViewById(R.id.good_img);
                goodName = (TextView) view.findViewById(R.id.good_name);
                newcount = (TextView) view.findViewById(R.id.newcount);
                add = (ImageButton) view.findViewById(R.id.add);
                price = (TextView) view.findViewById(R.id.price);
                sub = (ImageButton) view.findViewById(R.id.sub);
                fruitshopcard = (ImageButton) view
                        .findViewById(R.id.fruitshopcard);
                view.setTag(this);
            }
        }

    }

    public void setListViewHeightBasedOnChildren(ListView listView)
    {
        if (listView == null)
        {
            return;
        }
        ListAdapter listAdapter = listView.getAdapter();
        if (listAdapter == null)
        {
            // pre-condition
            return;
        }
        int totalHeight = 0;
        for (int i = 0; i < listAdapter.getCount(); i++)
        {
            View listItem = listAdapter.getView(i, null, listView);
            listItem.measure(0, 0);
            totalHeight += listItem.getMeasuredHeight();
        }
        ViewGroup.LayoutParams params = listView.getLayoutParams();
        params.height = totalHeight
                + (listView.getDividerHeight() * (listAdapter.getCount() - 1));
        listView.setLayoutParams(params);
    }

    @Override
    public void onClick(View v)
    {
        switch (v.getId())
        {
            case R.id.main_type_right:
                Intent intent = new Intent(getActivity(), GoodMoreActivity.class);
                intent.putExtra("store_id", storeId);
                intent.putExtra("class_name",
                        null == shopName || "".equals(shopName) ? "超值水果购"
                                : shopName);
                getActivity().startActivity(intent);
                AnimUtil.animToSlide(getActivity());
                break;
        }

    }

}
