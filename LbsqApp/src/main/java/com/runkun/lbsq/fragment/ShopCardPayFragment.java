package com.runkun.lbsq.fragment;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.view.ViewGroup.LayoutParams;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListAdapter;
import android.widget.TextView;

import com.lidroid.xutils.ViewUtils;
import com.lidroid.xutils.exception.HttpException;
import com.lidroid.xutils.http.RequestParams;
import com.lidroid.xutils.http.ResponseInfo;
import com.lidroid.xutils.http.callback.RequestCallBack;
import com.lidroid.xutils.view.annotation.ViewInject;
import com.lidroid.xutils.view.annotation.event.OnClick;
import com.runkun.lbsq.R;
import com.runkun.lbsq.activity.AlipayActivity;
import com.runkun.lbsq.activity.MyAddressActivity;
import com.runkun.lbsq.activity.ShopCardActivity;
import com.runkun.lbsq.activity.ShowOrderActivity;
import com.runkun.lbsq.bean.Shop;
import com.runkun.lbsq.utils.AnimUtil;
import com.runkun.lbsq.utils.HttpHelper;
import com.runkun.lbsq.utils.MyConstant;
import com.runkun.lbsq.utils.Tools;
import com.runkun.lbsq.utils.XUtilsImageLoader;
import com.runkun.lbsq.view.PromptDialog;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.math.BigDecimal;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;

import feifei.project.util.ToastUtil;
import feifei.project.view.ActionSheet;
import feifei.project.view.swipmenu.SwipeMenuListView;


public class ShopCardPayFragment extends BaseFragment implements OnClickListener
{

    @ViewInject(R.id.shocardpage)
    private LinearLayout viewContent;

    @ViewInject(R.id.buy_adr)
    private TextView address;

    @ViewInject(R.id.buy_person_name)
    private TextView reUserName;

    @ViewInject(R.id.buy_tel_no)
    private EditText phone;

    @ViewInject(R.id.buy_commits)
    private EditText addressDetail;

    // 选择的商户的storeId
    public String selectStoreId;
    public String selectStoreName;
    private String memberId;
    public JSONArray data;

    private LayoutInflater mLayoutInflater;

    private ActionSheet menuView;
    private float totalFee;

    private ClearingBottomHolder clearingHolder;
    private Map<String, ClearingBottomHolder> shops4Clearing;

    private Float sapy;
    private Editor edit;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState)
    {
        View view = inflater.inflate(R.layout.include_address_info, container,
                false);

        ViewUtils.inject(this, view);
        dialogInit();
        mLayoutInflater = inflater;

        SharedPreferences sp = getActivity().getSharedPreferences(MyConstant.FILE_NAME,
                Context.MODE_PRIVATE);
        edit = sp.edit();

        try
        {
            String sdata = getActivity().getIntent().getStringExtra("data");
            data = new JSONArray(sdata);
            String conAddress = sp.getString("con_address", null);
//            if (null == conAddress)
//            {
//
//                Tools.dialog(getActivity(), "您还未设定送货地址,请单击确定设置收货地址", true, new onButtonClick()
//                {
//                    @Override
//                    public void buttonClick()
//                    {
//                        Intent intent = new Intent();
//                        intent.setClass(getActivity(), MyAddressActivity.class);
//                        startActivity(intent);
//                    }
//                });
//            } else
            {
                address.setText(sp.getString("con_address", ""));
                reUserName.setText(sp.getString("consigner", ""));
                phone.setText(sp.getString("conmobile", ""));
                addressDetail.setText(sp.getString("addressRmark", ""));

            }
        } catch (JSONException e2)
        {
            e2.printStackTrace();
        }

        memberId = sp.getString(MyConstant.KEY_MEMBERID, "");
        netRequest(data);

        return view;

    }

    public void netRequest(JSONArray data)
    {
        shops4Clearing = new HashMap<String, ClearingBottomHolder>();
        for (int x = 0; x < data.length(); x++)
        {
            try
            {
                JSONObject shopJSON = data.getJSONObject(x);
                LinearLayout li = new LinearLayout(getActivity());
                li.setLayoutParams(new LayoutParams(LayoutParams.MATCH_PARENT,
                        LayoutParams.WRAP_CONTENT));
                int px = Tools.dp2px(getActivity(), 5);
                li.setPadding(px, px, px, px);
                li.setBackgroundColor(Color.rgb(239, 239, 244));
                li.setOrientation(LinearLayout.HORIZONTAL);

                TextView shopName = new TextView(getActivity());
                shopName.setLayoutParams(new LayoutParams(
                        LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT));
                shopName.setPadding(20, 10, 10, 10);
                shopName.setTextColor(Color.BLACK);
                shopName.setText(shopJSON.getString("store_name"));
                li.addView(shopName);

                viewContent.addView(li);
                SwipeMenuListView sml = new SwipeMenuListView(getActivity());

                viewContent.addView(sml);
                JSONArray adData = shopJSON.getJSONArray("goods");
                AppAdapter adapter = new AppAdapter(adData);
                sml.setAdapter(adapter);
                setListViewHeightBasedOnChildren(sml);

                View view = mLayoutInflater.inflate(R.layout.clearing_bottom,
                        null);
                ClearingBottomHolder clearingHolder = new ClearingBottomHolder(
                        shopJSON);
                ViewUtils.inject(clearingHolder, view);
                clearingHolder.init();
                clearingHolder.addRefrence(li, sml, view);
                shops4Clearing.put(shopJSON.getString("store_id"),
                        clearingHolder);
                viewContent.addView(view);

            } catch (NumberFormatException e)
            {
                e.printStackTrace();
            } catch (JSONException e)
            {
                e.printStackTrace();
            }
        }
    }


    class AppAdapter extends BaseAdapter
    {
        private HashMap<Integer, Boolean> isSelected;
        private JSONArray data;

        public AppAdapter(JSONArray data)
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
                        R.layout.item_single_shopcard, null);

                new ViewHolder(convertView);
            }

            try
            {
                final ViewHolder holder = (ViewHolder) convertView.getTag();

                holder.singleGoodSelect.setVisibility(View.GONE);
                holder.count.setText(data.getJSONObject(position).getString(
                        "number"));
                holder.goodName.setText(data.getJSONObject(position).getString(
                        "item_name"));
                holder.price.setText(data.getJSONObject(position).getString(
                        "price"));

                //				new XUtilsImageLoader (getActivity (), R.drawable.zhanwei, R.drawable.zhanwei,
                //						false, true).display (holder.goodImage, data.getJSONObject (position)
                //						.getString ("goods_pic"));

                XUtilsImageLoader xut = new XUtilsImageLoader(getActivity(),
                        holder.goodImage.getMeasuredWidth(),
                        holder.goodImage.getMeasuredHeight());
                xut.display(holder.goodImage, data.getJSONObject(position)
                        .getString("goods_pic"));
            } catch (JSONException e)
            {
                e.printStackTrace();
            }
            return convertView;
        }

        class ViewHolder
        {
            CheckBox singleGoodSelect;
            ImageView goodImage;
            TextView count;
            TextView price;
            TextView goodName;
            View counter;

            public ViewHolder(View view)
            {
                singleGoodSelect = (CheckBox) view
                        .findViewById(R.id.single_good_select);
                goodImage = (ImageView) view.findViewById(R.id.good_img);
                goodName = (TextView) view.findViewById(R.id.good_name);
                count = (TextView) view.findViewById(R.id.count);
                price = (TextView) view.findViewById(R.id.price);
                counter = view.findViewById(R.id.counter);
                counter.setVisibility(View.GONE);
                view.setTag(this);
            }
        }

        public HashMap<Integer, Boolean> getIsSelected()
        {
            return isSelected;
        }

        public void setIsSelected(HashMap<Integer, Boolean> isSelected)
        {
            this.isSelected = isSelected;
        }
    }


    @Override
    @OnClick({R.id.select_addr})
    public void onClick(View v)
    {
        switch (v.getId())
        {
            case R.id.select_addr:
                Intent intent = new Intent();
                intent.setClass(getActivity(), MyAddressActivity.class);
                getActivity().startActivity(intent);
                break;
        }

    }

    public void reqShopFare(final String storeId, final String isPage)
    {
        final RequestParams rp = new RequestParams();
        rp.addQueryStringParameter ("store_id", storeId);
        dialogProgress( "请稍候");
             HttpHelper.postByCommand ("findstoreinfo", rp,
                         new RequestCallBack<String> ()
                         {
                             @Override
                             public void onFailure(HttpException paramHttpException,
                                                   String paramString)
                             {
                                 dialogDismiss ();
                                 ToastUtil.toast (getActivity (), "网络连接错误");
                             }

                             @SuppressLint("DefaultLocale")
                             @Override
                             public void onSuccess(ResponseInfo<String> paramResponseInfo)
                             {
                                 dialogDismiss ();
                                 String localObject = paramResponseInfo.result;
                                 try
                                 {
                                     JSONObject jsonStore = new JSONObject (localObject);
                                     if (HttpHelper.isSuccess (jsonStore))
                                     {
                                         JSONObject jo = jsonStore.getJSONArray ("datas")
                                                 .getJSONObject (0);
                                         int alisa = jo.getInt ("alisa");
                                         final int fare = jo.getInt ("fare");
                                         if (alisa > totalFee)
                                         {

                                             if (fare == 0)
                                             {
                                                 final PromptDialog pDialog = PromptDialog
                                                         .create (getActivity (),
                                                                 "提示",
                                                                 "非常抱歉，该商家不对未满起送额度的订单提供派送服务，去选择更多您喜欢的商品吧～",
                                                                 PromptDialog.TYPE_CONFIRM);
                                                 pDialog.setConfirmButton ("确定",
                                                         new OnClickListener ()
                                                         {

                                                             @Override
                                                             public void onClick(View v)
                                                             {
                                                                 pDialog.dismiss ();
                                                                 getActivity ().finish ();
                                                             }

                                                         }).show ();
                                                 return;
                                             }

                                             String msg = String.format (
                                                     "您消费金额少于起送金额%d元，需要支付%d元外送费！",
                                                     alisa, fare);
                                             final PromptDialog pDialog = PromptDialog
                                                     .create (getActivity (),
                                                             "提示",
                                                             msg,
                                                             PromptDialog.TYPE_CONFIRM_CANCEL);
                                             pDialog.setConfirmButton ("确定",
                                                     new OnClickListener ()
                                                     {

                                                         @Override
                                                         public void onClick(View v)
                                                         {
                                                             pDialog.dismiss ();
                                                             clearingHolder.fare = fare;

                                                             try
                                                             {
                                                                 gotoPay (isPage);
                                                             } catch (JSONException ex)
                                                             {
                                                                 ex.printStackTrace ();
                                                             }
                                                         }

                                                     })
                                                     .setCancelButton ("取消",
                                                             new OnClickListener ()
                                                             {

                                                                 @Override
                                                                 public void onClick(
                                                                         View v)
                                                                 {
                                                                     getActivity ()
                                                                             .finish ();
                                                                 }

                                                             }).show ();
                                         } else
                                         {
                                             gotoPay (isPage);
                                         }
                                     } else
                                     {
                                         ToastUtil.toast (getActivity (), "超市起送价请求失败！！");
                                     }
                                 } catch (JSONException e)
                                 {
                                     Tools.toast (getActivity (), "数据解析错误");
                                     e.printStackTrace ();
                                 }
                             }
                         });
    }

    public void setListViewHeightBasedOnChildren(SwipeMenuListView listView)
    {
        if (listView == null)
        {
            return;
        }
        ListAdapter listAdapter = listView.getAdapter();
        if (listAdapter == null)
        {
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
    public void onResume()
    {
        super.onResume();
        loadDefaultAddress();
    }

    private void loadDefaultAddress()
    {
        String addr = HttpHelper.getPrefParams(getActivity(), "con_address");
        if (addr != null && addr.length() > 0)
        {
            address.setText(addr);
            reUserName.setText(HttpHelper.getPrefParams(getActivity(),
                    "consigner"));
            phone.setText(HttpHelper.getPrefParams(getActivity(), "conmobile"));
        }
    }

    class ClearingBottomHolder implements ActionSheet.MenuItemClickListener,
            OnClickListener
    {
        @ViewInject(R.id.count)
        TextView countTV;

        @ViewInject(R.id.totalFee)
        TextView totalFeeTV;

        @ViewInject(R.id.pay)
        Button payBtn;

        private JSONObject shopJSON;
        private JSONArray goods;
        private float totalShopFee = 0;
        private String goodNames;
        private String storeId;

        View shopNameView;
        View goodsListView;
        View selfView;

        String isPage;

        int fare;

        public ClearingBottomHolder(JSONObject shop) throws JSONException
        {
            shopJSON = shop;
            this.goods = shopJSON.getJSONArray("goods");
        }

        public void init() throws JSONException
        {
            countTV.setText(String.valueOf(goods.length()));
            for (int i = 0; i < goods.length(); i++)
            {
                JSONObject good = goods.getJSONObject(i);
                String price = good.getString("price");
                String count = good.getString("number");
                totalShopFee += Float.valueOf(price) * Integer.valueOf(count);
            }

            BigDecimal bd = new BigDecimal(totalShopFee);
            bd = bd.setScale(2, BigDecimal.ROUND_HALF_UP);
            totalShopFee = bd.floatValue();

            totalFeeTV.setText(String.valueOf(totalShopFee));

            StringBuilder sb = new StringBuilder();
            for (int i = 0; i < goods.length() && i < 5; i++)
            {
                JSONObject good = goods.getJSONObject(i);
                sb.append(good.get("item_name")).append('、');
            }
            goodNames = sb.substring(0, sb.length() - 1);
            if (goods.length() > 5)
            {
                goodNames += "...";
            }
            payBtn.setOnClickListener(this);
        }

        public String getShopcarIds() throws JSONException
        {
            StringBuilder sb = new StringBuilder();
            for (int i = 0; i < goods.length(); i++)
            {
                JSONObject good = goods.getJSONObject(i);
                sb.append(good.get("shopcar_id")).append(',');
            }
            return sb.substring(0, sb.length() - 1);
        }

        public String getGoodsSums() throws JSONException
        {
            StringBuilder sb = new StringBuilder();
            for (int i = 0; i < goods.length(); i++)
            {
                JSONObject good = goods.getJSONObject(i);
                sb.append(good.get("number")).append(',');
            }

            return sb.substring(0, sb.length() - 1);
        }

        public void addRefrence(View sv, View gv, View self)
        {
            shopNameView = sv;
            goodsListView = gv;
            selfView = self;
        }

        @Override
        public void onClick(View v)
        {
            try
            {
                storeId = shopJSON.getString("store_id");
                selectStoreId = shopJSON.getString("store_id");
                selectStoreName = shopJSON.getString("store_name");
                edit.putString("selectStoreName",
                        shopJSON.getString("store_name"));
                edit.putString("buyedGood", goodNames);
                edit.apply();

            } catch (JSONException e)
            {
                e.printStackTrace();
            }

            totalFee = totalShopFee;
            clearingHolder = this;
            if (checkInput())
            {
                ifOnlineNext(storeId);
            }
        }

        @Override
        public void onItemClick(int itemPosition)
        {
            switch (itemPosition)
            {
                case 0:
                    try
                    {
                        edit.putString("shopcarids", getShopcarIds());
                    } catch (JSONException e)
                    {
                        e.printStackTrace();
                    }
                    edit.apply();
                    isPage = "ExternalFragment";
                    reqShopFare(storeId, isPage);

                    break;
                case 1:
                    isPage = "WeChatFragment";
                    reqShopFare(storeId, isPage);
                    break;
            }
        }
    }

    public void removePaidShop()
    {
        String storeId = clearingHolder.storeId;
        shops4Clearing.remove(storeId);
        if (shops4Clearing.isEmpty())
        {
            getActivity().finish();
        } else
        {
            viewContent.removeView(clearingHolder.shopNameView);
            viewContent.removeView(clearingHolder.goodsListView);
            viewContent.removeView(clearingHolder.selfView);
        }
        showOrder(clearingHolder.isPage);
    }

    private void offlinePrompt()
    {
        final PromptDialog pDialog = PromptDialog.create(getActivity(), "提示",
                "店家已打烊,若有急需请电联", PromptDialog.TYPE_CONFIRM_CANCEL);
        pDialog.setConfirmButton("打电话", new OnClickListener()
        {

            @Override
            public void onClick(View v)
            {
                String storeTel = HttpHelper.getPrefParams(getActivity(),
                        Shop.SHOPPHONE);
                if ("".equals(storeTel))
                {
                    Tools.toast(getActivity(), "联系方式有误");
                } else
                {
                    Intent intent = new Intent();
                    intent.setAction(Intent.ACTION_DIAL);
                    intent.setData(Uri.parse("tel:" + storeTel));
                    startActivity(intent);
                    pDialog.dismiss();
                }
            }

        }).setCancelButton("取消", new OnClickListener()
        {

            @Override
            public void onClick(View v)
            {
                pDialog.dismiss();
            }

        }).show();
    }

    /**
     * 查询商家是否在线
     */
    private void ifOnlineNext(String storeId)
    {
        RequestParams rp = new RequestParams();
        rp.addQueryStringParameter("store_id", storeId);

        HttpHelper.postByCommand("isopenstore", rp,
                new RequestCallBack<String>()
                {

                    @Override
                    public void onFailure(HttpException arg0, String arg1)
                    {
                        dialogDismiss();
                        Tools.toast(getActivity(), "请检查您的网络或稍候重试");
                    }

                    @Override
                    public void onSuccess(ResponseInfo<String> resp)
                    {
                        String result = resp.result;
                        try
                        {
                            JSONObject jsonResult = new JSONObject(result);
                            String isopen = jsonResult.optString("isopen", "0");
                            if ("1".equals(isopen))
                            {
                                getActivity().setTheme(
                                        R.style.ActionSheetStyleIOS7);
                                menuView = new ActionSheet(getActivity());
                                menuView.setCancelButtonTitle("取消");
                                menuView.addItems("支付宝", "微信");
                                menuView.setItemClickListener(clearingHolder);
//                                menuView.setCancelableOnTouchMenuOutside(false);
                                dialogDismiss();
                                menuView.showMenu();
                            } else
                            {
                                offlinePrompt();
                            }
                        } catch (JSONException e)
                        {
                            e.printStackTrace();
                            dialogDismiss();
                        }
                        dialogDismiss();
                    }

                });
    }

    private void showOrder(String isPage)
    {
        Intent intent = new Intent();
        intent.setClass(getActivity(), ShowOrderActivity.class);
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm",
                Locale.getDefault());

        intent.putExtra("consigner", reUserName.getText().toString());
        intent.putExtra("mobile", phone.getText().toString());
        intent.putExtra("address", address.getText().toString());
        intent.putExtra("time", sdf.format(new Date()));
        intent.putExtra("orderFee", String.valueOf(sapy));
        intent.putExtra("fare", String.valueOf(clearingHolder.fare));
        intent.putExtra("payMethod", isPage.equals("WeChatFragment") ? "微信支付"
                : "支付宝支付");

        startActivityForResult(intent, ShopCardActivity.REQ_CODE_PAY);
        AnimUtil.animBackSlide(getActivity());
    }

    private boolean checkInput()
    {
//        String addr = address.getText().toString();
//        if (addr == null || "".equals(addr))
//        {
//            AnimUtil.animShakeText(address);
//            return false;
//        }

//        String telNo = phone.getText().toString();
//        if (!Tools.checkPhone(telNo))
//        {
//            AnimUtil.animShakeText(phone);
//            return false;
//        }

        String addr = address.getText().toString();
        if (Tools.isEmpty(addr))
        {
            Tools.toast(getActivity(), "请输入地址");
            AnimUtil.animShakeText(address);
            return false;
        }

        String person = reUserName.getText().toString();
        if (Tools.isEmpty(person))
        {
            Tools.toast(getActivity(), "请输入联系人");
            AnimUtil.animShakeText(reUserName);
            return false;
        }

        String telNo = phone.getText().toString();
        if (Tools.isEmpty(telNo))
        {
            Tools.toast(getActivity(), "请输入手机号");
            AnimUtil.animShakeText(phone);
            return false;
        }
        if (!Tools.checkPhone(telNo))
        {
            Tools.toast(getActivity(), "请输入正确的手机号");
            AnimUtil.animShakeText(phone);
            return false;
        }


        return true;
    }

    public void gotoPay(String isPage) throws JSONException
    {
        Intent intent = new Intent();

        intent.putExtra("member_id", memberId);
        intent.putExtra("con_address", address.getText().toString());
        intent.putExtra("consigner", reUserName.getText().toString());
        intent.putExtra("conmobile", phone.getText().toString());
        intent.putExtra("conremark", addressDetail.getText().toString());
        intent.putExtra("store_ids", selectStoreId);
        intent.putExtra("shopcarids", clearingHolder.getShopcarIds());
        intent.putExtra("goodsnums", clearingHolder.getGoodsSums());
        sapy = Tools.scale(totalFee);
        intent.putExtra("total_fee", String.valueOf(sapy));
        intent.putExtra("isPage", isPage);

        intent.putExtra("fareInt", clearingHolder.fare);
        intent.setClass(getActivity(), AlipayActivity.class);
        getActivity().startActivityForResult(intent, ShopCardActivity.REQ_CODE_PAY);
        AnimUtil.animBackSlide(getActivity());
    }
}
