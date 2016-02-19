package com.runkun.lbsq.activity;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.graphics.Color;
import android.os.Bundle;
import android.view.Gravity;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ImageButton;
import android.widget.RelativeLayout;
import android.widget.RelativeLayout.LayoutParams;

import com.lidroid.xutils.exception.HttpException;
import com.lidroid.xutils.http.RequestParams;
import com.lidroid.xutils.http.ResponseInfo;
import com.lidroid.xutils.http.callback.RequestCallBack;
import com.runkun.lbsq.R;
import com.runkun.lbsq.adapter.GoodAdapter;
import com.runkun.lbsq.bean.GoodMore;
import com.runkun.lbsq.utils.AnimUtil;
import com.runkun.lbsq.utils.BadgeView;
import com.runkun.lbsq.utils.HttpHelper;
import com.runkun.lbsq.utils.MyConstant;
import com.runkun.lbsq.utils.Tools;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

import feifei.project.util.L;

//git.oschina.net/lbsq/lbsq.git
//git.oschina.net/lbsq/lbsq.git

public class SearchGoodsActivity extends SearchBaseActivity<GoodMore>
{

    String tempString;
    RequestParams rp = new RequestParams ();
    int pageNumber = 1;

    private View itemlistContainer;
    private View shopcartContainer;
    public ImageButton shopcartBtn;

    @Override
    protected void onCreate (Bundle savedInstanceState)
    {
        super.onCreate (savedInstanceState);
        list = new ArrayList<GoodMore> ();
        adaper = new GoodAdapter (this, list, R.layout.item_good, true, false,
                false);
        listView.setAdapter (adaper);
        // listView.setOnItemClickListener(new OnItemClickListener() {
        // @Override
        // public void onItemClick(AdapterView<?> parent, View view,
        // int position, long id) {
        // Intent intent = new Intent(SearchGoodsActivity.this,
        // GoodDetailActivity.class);
        // intent.putExtra("goods_id", list.get(position).getGoodsId());
        // Log.e("log", list.get(position).getGoodsId());
        // startActivity(intent);
        // AnimUtil.animToSlide(activity);
        // }
        // });
        //
        // search.setOnClickListener(new OnClickListener() {
        // @Override
        // public void onClick(View v) {
        // String s = edit.getText().toString();
        // if (Tools.isEmpty(s)) {
        // Tools.toast(activity, "");
        // } else {
        // if (!s.equals(tempString)) {
        // list.clear();
        // adaper.notifyDataSetChanged();
        // emptyTextView.setText("");
        // pageNumber = 1;
        // tempString = s;
        // query();
        // }
        // }
        //
        // }
        // });

        itemlistContainer = findViewById (R.id.itemlist_container);
        RelativeLayout.LayoutParams lp = (LayoutParams) itemlistContainer
                .getLayoutParams ();
        lp.setMargins (0, 0, 0, Tools.dp2px (this, 40));
        itemlistContainer.setLayoutParams (lp);

        shopcartContainer = findViewById (R.id.inc_shopcart);
        shopcartContainer.setVisibility (View.VISIBLE);
        shopcartBtn = (ImageButton) findViewById (R.id.shopcart_btn);
        shopcartBtn.setOnClickListener (new OnClickListener ()
        {

            @Override
            public void onClick (View v)
            {
                Intent intent = new Intent (SearchGoodsActivity.this,
                        MainActivity.class);
                intent.putExtra ("types", "shopcard");
                startActivity (intent);
                AnimUtil.animBackSlideFinish (SearchGoodsActivity.this);
            }

        });
    }

    @Override
    public void onResume ()
    {
        super.onResume ();
        String countStr = HttpHelper.getPrefParams (this, "shopcount");
        if ( !"".equals (countStr) )
        {
            int count = Integer.valueOf (countStr);
            Tools.refreshShopcartBadge (this, shopcartBtn, count);
        }
    }

    @Override
    protected void query ()
    {
        dialogProgress (activity,
                Tools.getStr (activity, R.string.SEARCHING));
        rp.addQueryStringParameter ("keyword", edit.getText ().toString ());
        rp.addQueryStringParameter ("store_id", store_id);
        rp.addQueryStringParameter ("pagenumber", String.valueOf (pageNumber++));
        updateFooter (true, null);

        HttpHelper.postByCommand ("searchgoods", rp, new RequestCallBack<String> ()
        {

            @Override
            public void onFailure (HttpException arg0, String arg1)
            {
                Tools.toast (activity,
                        Tools.getStr (activity, R.string.NETWORKERROR));
                emptyTextView.setText (Tools.getStr (activity,
                        R.string.NETERRORGOING));
                dialogDismiss ();
            }

            @Override
            public void onSuccess (ResponseInfo<String> resultInfo)
            {
                String result = resultInfo.result;
                L.l (result);
                try
                {
                    JSONObject jo = new JSONObject (result);
                    result = jo.getString ("code");
                    if ( result.equals ("200") )
                    {
                        String haveMore = jo.getString ("haveMore");
                        if ( Integer.valueOf (jo.getString ("count")) > 0 )
                        {
                            if ( "true".equals (haveMore) )
                            {
                                // updateFooter(false,getResources().getString(R.string.CLICKLOADINGMORE);
                                updateFooter (false, "");
                            } else
                            {
                                more = false;
                                updateFooter (
                                        false,
                                        Tools.getStr (activity,
                                                R.string.SEARCHNOMOREGOODS));
                            }
                            listView.setTag (haveMore);
                            JSONArray jArray = jo.getJSONArray ("datas");
                            for (int i = 0; i < jArray.length (); i++)
                            {
                                JSONObject object = (JSONObject) jArray
                                        .get (i);
                                GoodMore goodMoreEntity = new GoodMore ();
                                goodMoreEntity.setGoodsPic (object
                                        .getString ("goods_pic"));
                                goodMoreEntity.setGoodsName (object
                                        .getString ("goods_name"));
                                goodMoreEntity.setGoodsPrice (object
                                        .getString ("goods_price"));
                                goodMoreEntity.setGoodsId (object
                                        .getString ("goods_id"));
                                list.add (goodMoreEntity);
                            }
                            adaper.notifyDataSetChanged ();
                        } else
                        {
                            emptyTextView.setText (Tools.getStr (
                                    activity, R.string.SEARCHNOGOODS));
                        }
                    }

                } catch (JSONException e)
                {
                    e.printStackTrace ();
                    emptyTextView.setText ("数据解析错误");
                    dialogDismiss ();
                }
                dialogDismiss ();
            }
        });
    }

    @Override
    protected void jumpTo (int position)
    {
        Intent intent = new Intent (SearchGoodsActivity.this,
                GoodDetailActivity.class);
        intent.putExtra ("goods_id", list.get (position).getGoodsId ());
        startActivity (intent);
        AnimUtil.animToSlide (activity);
    }

    @Override
    protected void search (String s)
    {
        if ( !s.equals (tempString) )
        {
            list.clear ();
            adaper.notifyDataSetChanged ();
            emptyTextView
                    .setText (Tools.getStr (activity, R.string.EMPTYLOADING));
            pageNumber = 1;
            tempString = s;
            // count++;
            // ConfigUtil.write(activity, "count", count);
            // editor.putString("history" + count, s);
            // editor.apply();
            query ();
        }
        // array.counter_add(s);
        // addText(array.size() - 1);
    }

    private BadgeView shopcartBadge = null;

    private void refreshShopcart (int count)
    {
        View b = shopcartContainer.findViewWithTag ("badge");

        if ( shopcartBadge == null || b == null )
        {
            BadgeView badgeView = new BadgeView (this);
            badgeView.setBadgeCount (Integer.valueOf (count));
            badgeView.setBackground (20, Color.RED);
            badgeView.setTextColor (Color.WHITE);
            badgeView.setBadgeGravity (Gravity.RIGHT | Gravity.TOP);
            badgeView.setBadgeMargin (0, 0, 3, 0);
            badgeView.setTargetView (shopcartBtn);
            badgeView.setTag ("badge");
            shopcartBadge = badgeView;
        } else
        {
            shopcartBadge.setBadgeCount (count);
        }

        SharedPreferences sp = getSharedPreferences (MyConstant.FILE_NAME,
                Context.MODE_PRIVATE);
        Editor editor = sp.edit ();
        editor.putString ("shopcount", String.valueOf (count));
        editor.apply ();

        Tools.refreshShopcartBadge (MainActivity.mainActivity, count);
    }

    // @Override
    // protected void onResume() {
    // super.onResume();
    // // array.clear();
    // // array.counter_add("清除搜索历史");
    // count = ConfigUtil.readInt(activity, "count", 0);
    // }
}
