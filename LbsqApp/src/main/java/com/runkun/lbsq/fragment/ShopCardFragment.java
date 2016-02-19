package com.runkun.lbsq.fragment;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.BaseExpandableListAdapter;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.ExpandableListView;
import android.widget.ExpandableListView.OnGroupClickListener;
import android.widget.ImageView;
import android.widget.TextView;

import com.lidroid.xutils.HttpUtils;
import com.lidroid.xutils.ViewUtils;
import com.lidroid.xutils.exception.HttpException;
import com.lidroid.xutils.http.RequestParams;
import com.lidroid.xutils.http.ResponseInfo;
import com.lidroid.xutils.http.callback.RequestCallBack;
import com.lidroid.xutils.http.client.HttpRequest;
import com.lidroid.xutils.view.annotation.ViewInject;
import com.lidroid.xutils.view.annotation.event.OnClick;
import com.lidroid.xutils.view.annotation.event.OnCompoundButtonCheckedChange;
import com.runkun.lbsq.R;
import com.runkun.lbsq.activity.BaseAcitivity;
import com.runkun.lbsq.activity.GoodDetailActivity;
import com.runkun.lbsq.activity.MainActivity;
import com.runkun.lbsq.activity.ShopCardActivity;
import com.runkun.lbsq.bean.ShopCart;
import com.runkun.lbsq.bean.ShopCart.Good;
import com.runkun.lbsq.bean.ShopCart.Shop;
import com.runkun.lbsq.interfaces.RefreshData;
import com.runkun.lbsq.utils.AnimUtil;
import com.runkun.lbsq.utils.HttpHelper;
import com.runkun.lbsq.utils.MyConstant;
import com.runkun.lbsq.utils.Tools;
import com.runkun.lbsq.utils.XUtilsImageLoader;
import com.runkun.lbsq.view.CounterHolder;
import com.runkun.lbsq.view.CounterHolder.CounterCallback;
import com.runkun.lbsq.view.PromptDialog;
import com.runkun.lbsq.view.swipeex.SwipableExpandListView;
import com.runkun.lbsq.view.swipeex.SwipableExpandListView.OnMenuItemClickListener;
import com.runkun.lbsq.view.swipeex.SwipeMenu;
import com.runkun.lbsq.view.swipeex.SwipeMenuCreator;
import com.runkun.lbsq.view.swipeex.SwipeMenuItem;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.Iterator;
import java.util.List;

import feifei.project.util.L;

@SuppressLint("InflateParams")
public class ShopCardFragment extends BaseFragment implements RefreshData
{

    @ViewInject(R.id.shopcart)
    private SwipableExpandListView shopcartList;

    @ViewInject(R.id.selectall)
    private CheckBox selectAllBtn;

    private String memberId;
    private ShopCart shopcart;
    private LayoutInflater inflater;
    private AppAdapter adapter;
    public String goodsid;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState)
    {
        View view = inflater.inflate (R.layout.fragment_shopcart, container, false);
        ViewUtils.inject (this, view);
        this.inflater = inflater;
        dialogInit ();

        SharedPreferences sp = getActivity ().getSharedPreferences ("lbsq",
                Context.MODE_PRIVATE);
        memberId = sp.getString (MyConstant.KEY_MEMBERID, "");

        shopcart = new ShopCart (selectAllBtn);
        adapter = new AppAdapter ();
        shopcartList.setOnMenuItemClickListener (new OnMenuItemClickListener ()
        {

            @Override
            public void onMenuItemClick(int position, SwipeMenu menu, int index)
            {
                int gs = menu.getGroupPos ();
                int cs = menu.getChildPos ();
                delete (gs, cs);
            }

        });

        shopcartList.setOnGroupClickListener (new OnGroupClickListener ()
        {

            @Override
            public boolean onGroupClick(ExpandableListView parent, View v,
                                        int groupPosition, long id)
            {
                return true;
            }

        });

        SwipeMenuCreator creator = new SwipeMenuCreator ()
        {
            @Override
            public void create(SwipeMenu menu)
            {
                SwipeMenuItem deleteItem = new SwipeMenuItem (getActivity ());
                deleteItem.setBackground (new ColorDrawable (Color.rgb (0xF9,
                        0x3F, 0x25)));
                deleteItem.setWidth (Tools.dp2px (getActivity (), 90));
                deleteItem.setIcon (R.drawable.ic_delete);
                menu.addMenuItem (deleteItem);
            }
        };

        shopcartList.setMenuCreator (creator);
        shopcartList.setAdapter (adapter);
        refreshData ();
        return view;

    }


    public void query()
    {
        dialogProgress ( "请稍候");
        RequestParams rp = new RequestParams ();
        rp.addQueryStringParameter ("member_id", HttpHelper.getPrefParams (fragment, MyConstant.KEY_MEMBERID));
        rp.addQueryStringParameter ("pagesize", "51");
        rp.addQueryStringParameter ("pagenumber", "1");

        HttpHelper.postByCommand ("shopcarlist", rp,
                new RequestCallBack<String> ()
                {

                    @Override
                    public void onFailure(HttpException arg0, String arg1)
                    {
                        L.l (arg1);
                        dialogDismiss ();
                    }

                    @Override
                    public void onSuccess(ResponseInfo<String> resp)
                    {
                        String result = resp.result;

                        try
                        {
                            JSONObject jsonResult = new JSONObject (result);
                            if (HttpHelper.isSuccess (jsonResult))
                            {
                                shopcart.clear ();
                                JSONArray ja = jsonResult.getJSONArray ("datas");
                                int goodsCount = 0;
                                for (int i = 0; i < ja.length (); i++)
                                {
                                    JSONObject jo = ja.getJSONObject (i);
                                    shopcart.add (jo);
                                    goodsCount++;
                                }

                                Tools.refreshShopcartBadge (
                                        MainActivity.mainActivity, goodsCount);

                                if (goodsCount > 0)
                                {
                                    selectAllBtn.setChecked (true);
                                }
                                onClick (selectAllBtn);

                                adapter.notifyDataSetChanged ();

                                for (int i = 0; i < shopcart.getShopsCount (); i++)
                                {
                                    shopcartList.expandGroup (i);
                                }
                            }
                        } catch (JSONException e)
                        {
                            e.printStackTrace ();
                            dialogDismiss ();
                        }
                        dialogDismiss ();
                    }

                });
    }

    /**
     * 删除商品
     */
    private void delete(final int groupPosition, final int childPosition)
    {
        dialogProgress ("请稍候...");
        RequestParams rp = new RequestParams ();
        final Shop shop = shopcart.getShop (groupPosition);
        Good good = shop.getGood (childPosition);

        rp.addQueryStringParameter ("shopcar_id", good.getShopcarId ());
        rp.addQueryStringParameter ("member_id", memberId);

        HttpHelper.postByCommand ("delshopcar", rp,
                new RequestCallBack<String> ()
                {

                    @Override
                    public void onFailure(HttpException arg0, String arg1)
                    {
                        L.l (arg1);
                        dialogDismiss ();
                    }

                    @Override
                    public void onSuccess(ResponseInfo<String> resp)
                    {
                        String result = resp.result;

                        try
                        {
                            JSONObject jsonResult = new JSONObject (result);
                            if (HttpHelper.isSuccess (jsonResult))
                            {
                                JSONObject datas = jsonResult
                                        .getJSONObject ("datas");
                                result = datas.getString ("delResult");
                                if ("true".equals (result))
                                {
                                    shop.removeGood (childPosition);
                                    if (shop.getGoodsCount () == 0)
                                    {
                                        shopcart.removeShop (groupPosition);
                                    }

                                    int goodsCount = 0;
                                    List<Shop> shops = shopcart.getShops ();
                                    for (Shop shop : shops)
                                    {
                                        goodsCount += shop.getGoodsCount ();
                                    }

                                    Tools.refreshShopcartBadge (
                                            MainActivity.mainActivity,
                                            goodsCount);

                                    adapter.notifyDataSetInvalidated ();
                                }
                            }
                        } catch (JSONException e)
                        {
                            e.printStackTrace ();
                            dialogDismiss ();
                        }
                        dialogDismiss ();
                    }

                });
    }

    @OnClick({R.id.selectall, R.id.clearing})
    public void onClick(View v)
    {
        int id = v.getId ();
        switch (id)
        {
            case R.id.selectall:
                for (int i = 0; i < shopcart.getShopsCount (); i++)
                {
                    shopcart.getShop (i).setSelected (selectAllBtn.isChecked ());
                }
                for (int i = 0; i < shopcart.getShopsCount (); i++)
                {
                    shopcart.getShop (i).onClick ();
                }
                adapter.notifyDataSetChanged ();
                break;
            case R.id.clearing:
                goClearing ();
                break;
        }
    }

    public void goClearing()
    {
        List<Shop> shops = shopcart.getSelectedShop ();

        if (shops.size () < 1)
        {
            Tools.toast (getActivity (), "尚未选择结算商品");
            return;
        }

        try
        {
            JSONArray ja = new JSONArray ();
            for (Shop shop : shops)
            {
                JSONObject jo = new JSONObject ();
                jo.put ("store_id", shop.getStoreId ());
                jo.put ("store_name", shop.getStoreName ());
                JSONArray gja = new JSONArray ();
                List<Good> goods = shop.getSelectedGoods ();
                for (Good good : goods)
                {
                    JSONObject gjo = new JSONObject ();
                    gjo.put ("shopcar_id", good.getShopcarId ());
                    gjo.put ("item_id", good.getGoodId ());
                    gjo.put ("item_name", good.getGoodName ());
                    gjo.put ("price", good.getPrice ());
                    gjo.put ("number", good.getNewcount ());
                    gjo.put ("goods_pic", good.getGoodPic ());
                    gja.put (gjo);
                }
                jo.put ("goods", gja);
                ja.put (jo);
            }
            Intent intent = new Intent ();
            intent.putExtra ("data", ja.toString ());
            intent.setClass (getActivity (), ShopCardActivity.class);
            startActivity (intent);
            AnimUtil.animToSlide (getActivity ());
        } catch (JSONException ex)
        {
            ex.printStackTrace ();
        }
    }


    class AppAdapter extends BaseExpandableListAdapter
    {

        @Override
        public int getGroupCount()
        {
            return shopcart.getShopsCount ();
        }

        @Override
        public int getChildrenCount(int groupPosition)
        {
            return shopcart.getShop (groupPosition).getGoodsCount ();
        }

        @Override
        public Object getGroup(int groupPosition)
        {
            return shopcart.getShop (groupPosition);
        }

        @Override
        public Object getChild(int groupPosition, int childPosition)
        {

            return shopcart.getShop (groupPosition).getGood (childPosition);
        }

        @Override
        public long getGroupId(int groupPosition)
        {
            if (groupPosition > getGroupCount ())
            {
                return -1;
            }
            return Long.valueOf (shopcart.getShop (groupPosition).getStoreId ());
        }

        @Override
        public long getChildId(int groupPosition, int childPosition)
        {
            return Long.valueOf (shopcart.getShop (groupPosition)
                    .getGood (childPosition).getGoodId ());
        }

        @Override
        public boolean hasStableIds()
        {
            return true;
        }

        @Override
        public View getGroupView(int groupPosition, boolean isExpanded,
                                 View convertView, ViewGroup parent)
        {
            if (convertView == null)
            {
                convertView = inflater.inflate (R.layout.item_group_shopcart_shop, null);
                GroupViewHolder gvh = new GroupViewHolder ();
                ViewUtils.inject (gvh, convertView);
                convertView.setTag (gvh);
            }

            GroupViewHolder gvh = (GroupViewHolder) convertView.getTag ();
            Shop shop = (Shop) getGroup (groupPosition);

            gvh.shop = shop;
            gvh.ck.setTag (groupPosition);
            gvh.ck.setChecked (shop.isSelected ());
            gvh.name.setText (shop.getStoreName ());

            return convertView;
        }

        @Override
        public View getChildView(int groupPosition, int childPosition,
                                 boolean isLastChild, View convertView, ViewGroup parent)
        {
            if (convertView == null)
            {
                convertView = inflater.inflate (R.layout.item_single_shopcard, null);
                ChildViewHolder cvh = new ChildViewHolder ();
                ViewUtils.inject (cvh, convertView);
                ViewUtils.inject (cvh.counterHolder, cvh.counter);
                convertView.setTag (cvh);
            }

            ChildViewHolder cvh = (ChildViewHolder) convertView.getTag ();
            Shop shop = (Shop) getGroup (groupPosition);
            Good good = shop.getGood (childPosition);

            cvh.good = good;
            cvh.count.setTag (groupPosition);
            cvh.img.setTag (childPosition);

            cvh.ck.setChecked (good.isSelected ());
            cvh.name.setText (good.getGoodName ());
            cvh.price.setText (good.getPrice ());
            cvh.count.setText (String.valueOf (good.getNumber ()));
            cvh.counterHolder.setCount (Float.valueOf (good.getNewcount ()));
            new XUtilsImageLoader (getActivity (), cvh.img.getMeasuredWidth (),
                    cvh.img.getMeasuredHeight (), false).display (cvh.img,
                    good.getGoodPic ());

            //			new XUtilsImageLoader (getActivity (), 0, 0,
            //					false, true).display (cvh.img, 	good.getGoodPic ());

            return convertView;
        }

        @Override
        public boolean isChildSelectable(int groupPosition, int childPosition)
        {
            return true;
        }

        class GroupViewHolder
        {
            @ViewInject(R.id.shop_sel)
            CheckBox ck;

            @ViewInject(R.id.shop_name)
            TextView name;

            private Shop shop;

            @OnCompoundButtonCheckedChange({R.id.shop_sel})
            public void onCheckedChanged(CompoundButton buttonView,
                                         boolean isChecked)
            {
                shop.setSelected (isChecked);
            }

            @OnClick({R.id.shop_sel})
            public void onClick(View v)
            {
                shop.onClick ();
                adapter.notifyDataSetChanged ();
            }
        }

        class ChildViewHolder implements CounterCallback, OnClickListener
        {
            @ViewInject(R.id.single_good_select)
            CheckBox ck;

            @ViewInject(R.id.good_img)
            ImageView img;

            @ViewInject(R.id.good_name)
            TextView name;

            @ViewInject(R.id.price)
            TextView price;

            @ViewInject(R.id.count)
            TextView count;

            @ViewInject(R.id.counter)
            View counter;

            Good good;

            CounterHolder counterHolder;

            public ChildViewHolder()
            {
                counterHolder = new CounterHolder (1, 1, 1, 99, this,true);
            }

            @OnCompoundButtonCheckedChange({R.id.single_good_select})
            public void onCheckedChanged(CompoundButton buttonView,
                                         boolean isChecked)
            {
                good.setSelected (isChecked);

            }

            @Override
            @OnClick({R.id.good_img, R.id.single_good_select})
            public void onClick(View v)
            {
                int id = v.getId ();
                switch (id)
                {
                    case R.id.single_good_select:
                        good.onClick (good.isSelected ());
                        adapter.notifyDataSetChanged ();
                        break;
                    case R.id.good_img:
                        Intent intent = new Intent ();
                        intent.putExtra ("goods_id", good.getGoodId ());
                        intent.setClass (getActivity (), GoodDetailActivity.class);
                        startActivity (intent);
                        break;
                }
            }

            @Override
            public void newCount(float newCount)
            {
                setCount (newCount, count);
            }

            @Override
            public void requestShopCard(boolean plus,float newCount)
            {
                //// TODO: 2015/9/12
                RequestParams requestParams = new RequestParams ();
                requestParams.addQueryStringParameter ("goods_id", good.getGoodId ());
                requestParams.addQueryStringParameter ("quantity", plus?"1":"-1");
                requestParams.addQueryStringParameter ("member_id",
                        memberId);
                addShopCard ((BaseAcitivity) fragment, "addshopcar", requestParams, newCount);
            }

            public  void addShopCard(final BaseAcitivity pram, String url, RequestParams requestParams, final float newcount)
            {
                HttpUtils localHttpUtils = new HttpUtils ();
                pram.dialogInit ();
                pram.dialogProgress (pram, "请稍候");
                localHttpUtils.send (HttpRequest.HttpMethod.POST, MyConstant.API_BASE_URL + url,
                        requestParams, new RequestCallBack<String> ()
                        {
                            @Override
                            public void onFailure(HttpException paramHttpException,
                                                  String paramString)
                            {
                                Tools.toast (pram, "网络连接错误");
                                pram.dialogDismiss ();
                            }

                            @Override
                            public void onSuccess(ResponseInfo<String> paramResponseInfo)
                            {
                                pram.dialogDismiss ();
                                String localObject = paramResponseInfo.result;
                                try
                                {
                                    JSONObject resultJson = new JSONObject (localObject);
                                    String result = resultJson.getString ("code");
                                    Log.e ("TAG","res==="+result);
                                    if (("205".equals (result)))
                                    {//超过商品限购数量
                                        int counts = resultJson.getInt("count");
                                        setCount (newcount-1, count);
                                        Tools.toast (pram, "超过商品限购数量,商品限购"+counts+"件");
                                    } else if (("206".equals (result)))
                                    {//没有这个商品
                                        Tools.toast (pram, "这个商品已经下架了");
                                    } else if (("207".equals (result)))
                                    {//当天超过限购数量
                                        int counts = resultJson.getInt("count");
                                        setCount (newcount-1, count);
                                        Tools.toast (pram, "超过当天限购数量了,商品限购"+counts+"件");
                                    } else if ("200".equals (result)){
                                        setCount (newcount, count);
                                    }
                                } catch (JSONException localJSONException)
                                {
                                    Tools.toast (pram, "数据解析错误");
                                    pram.dialogDismiss ();
                                    localJSONException.printStackTrace ();
                                }
                                pram.dialogDismiss ();
                            }
                        });
            }

            private void setCount(float newCount, TextView tv)
            {
                int iCount = (int) newCount;
                if (iCount == newCount)
                {
                    tv.setText (String.valueOf (iCount));
                    good.setNewcount (String.valueOf (iCount));
                    counterHolder.setCount (iCount);
                } else
                {
                    tv.setText (String.valueOf (newCount));
                    good.setNewcount (String.valueOf (newCount));
                    counterHolder.setCount (newCount);
                }

            }
        }
    }

    @Override
    public void refreshData()
    {
        query ();
        selectAllBtn.setChecked (true);
    }

    public void deleteAll()
    {
        int shopsCount = shopcart.getShopsCount ();
        if (shopsCount < 1)
        {
            return;
        }
        final PromptDialog pDialog = PromptDialog.create (
                MainActivity.mainActivity, "提示", "您确定删除所选商品吗?",
                PromptDialog.TYPE_CONFIRM_CANCEL);
        pDialog.setCancelButton ("取消", new OnClickListener ()
        {

            @Override
            public void onClick(View v)
            {
                pDialog.dismiss ();
            }

        }).setConfirmButton ("确定", new OnClickListener ()
        {

            @Override
            public void onClick(View v)
            {
                deleteAllGoods ();
                pDialog.dismiss ();
            }

        }).show ();
    }

    private void deleteAllGoods()
    {
        StringBuilder sb = new StringBuilder ();
        int shopsCount = shopcart.getShopsCount ();
        for (int i = 0; i < shopsCount; i++)
        {
            Shop shop = shopcart.getShop (i);
            int goodsCount = shop.getGoodsCount ();
            for (int m = 0; m < goodsCount; m++)
            {
                Good good = shop.getGood (m);
                if (good.isSelected ())
                {
                    sb.append (good.getShopcarId ()).append (',');
                }
            }
        }

        sb.append ("-1");

        RequestParams rp = new RequestParams ();

        rp.addQueryStringParameter ("shopcar_id", sb.toString ());
        rp.addQueryStringParameter ("member_id", memberId);

        //        final CProgressDialog pDialog = CProgressDialog.createCProgressDialog(
        //                MainActivity.mainActivity, "请稍候", false);
        //        pDialog.show();
        dialogProgress ("请稍候");

        HttpHelper.postByCommand ("delshopcar", rp,
                new RequestCallBack<String> ()
                {

                    @Override
                    public void onFailure(HttpException arg0, String arg1)
                    {
                        //                        System.out.println(arg1);
                        //                        pDialog.dismiss();
                        L.l (arg1);
                        dialogDismiss ();
                    }

                    @Override
                    public void onSuccess(ResponseInfo<String> resp)
                    {
                        String result = resp.result;

                        try
                        {
                            JSONObject jsonResult = new JSONObject (result);
                            if (HttpHelper.isSuccess (jsonResult))
                            {
                                JSONObject datas = jsonResult
                                        .getJSONObject ("datas");
                                result = datas.getString ("delResult");
                                if ("true".equals (result))
                                {
                                    List<Shop> shops = shopcart.getShops ();
                                    Iterator<Shop> ites = shops.iterator ();
                                    while (ites.hasNext ())
                                    {
                                        Shop shop = ites.next ();
                                        List<Good> goods = shop.getGoods ();
                                        Iterator<Good> ite = goods.iterator ();

                                        while (ite.hasNext ())
                                        {
                                            Good good = ite.next ();
                                            if (good.isSelected ())
                                            {
                                                ite.remove ();
                                            }
                                        }

                                        if (shop.getGoodsCount () < 1)
                                        {
                                            ites.remove ();
                                        }
                                    }

                                    int goodsCount = 0;
                                    shops = shopcart.getShops ();
                                    for (Shop shop : shops)
                                    {
                                        goodsCount += shop.getGoodsCount ();
                                    }

                                    Tools.refreshShopcartBadge (
                                            MainActivity.mainActivity,
                                            goodsCount);
                                    shopcart.onShopCheckedChange ();
                                    adapter.notifyDataSetInvalidated ();
                                }
                            }
                        } catch (JSONException e)
                        {
                            e.printStackTrace ();
                            dialogDismiss ();
                        }
                        //                        pDialog.dismiss();
                        dialogDismiss ();
                    }

                });
    }

}
