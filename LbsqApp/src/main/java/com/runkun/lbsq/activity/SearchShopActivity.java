package com.runkun.lbsq.activity;

import android.content.Intent;
import android.os.Bundle;

import com.alibaba.fastjson.JSON;
import com.lidroid.xutils.exception.HttpException;
import com.lidroid.xutils.http.RequestParams;
import com.lidroid.xutils.http.ResponseInfo;
import com.lidroid.xutils.http.callback.RequestCallBack;
import com.runkun.lbsq.R;
import com.runkun.lbsq.adapter.ShopAdapter;
import com.runkun.lbsq.bean.Shop;
import com.runkun.lbsq.utils.AnimUtil;
import com.runkun.lbsq.utils.HttpHelper;
import com.runkun.lbsq.utils.MyConstant;
import com.runkun.lbsq.utils.Tools;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

import feifei.project.util.ConfigUtil;

public class SearchShopActivity extends SearchBaseActivity<Shop>
{

    int count = 0;
    int pageNumber = 1;
    String tempString;
    RequestParams rp = new RequestParams ();

    @Override
    protected void onCreate (Bundle savedInstanceState)
    {
        super.onCreate (savedInstanceState);
        edit.setHint (Tools.getStr (activity, R.string.SEARCHSHOPHINT));
        list = new ArrayList<Shop> ();
        adaper = new ShopAdapter (this, list, R.layout.item_shop, true, true);
        listView.setAdapter (adaper);
        // listView.setOnItemClickListener(new OnItemClickListener() {
        // @Override
        // public void onItemClick(AdapterView<?> parent, View view,
        // int position, long id) {
        // if (view != footer) {
        //
        // Intent intent = new Intent(SearchShopActivity.this,
        // MainActivity.class);
        // intent.putExtra("shop",
        // PG.convertParcelable(list.get(position)));
        // startActivity(intent);
        // AnimUtil.animToSlideFinish(activity);
        // }
        // }
        // });
        // search.setOnClickListener(new OnClickListener() {
        //
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
        // }
        // });
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

        HttpHelper.postByCommand ("storelist", rp, new RequestCallBack<String> ()
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
                        try
                        {
                            JSONObject jo = new JSONObject (result);
                            result = jo.getString ("code");
                            if ( result.equals ("200") )
                            {
                                if ( Integer.valueOf (jo.getString ("count")) > 0 )
                                {
                                    String haveMore = jo.getString ("haveMore");
                                    if ( "true".equals (haveMore) )
                                    {
                                        // updateFooter(false,
                                        // MyString.CLICKLOADINGMORE);
                                        updateFooter (false, "");
                                    } else
                                    {
                                        more = false;
                                        updateFooter (false, Tools.getStr (
                                                activity,
                                                R.string.SEARCHNOMORESHOP));
                                    }
                                    listView.setTag (haveMore);

                                    list.addAll (JSON.parseArray (jo.getString ("datas"), Shop.class));

                                    adaper.notifyDataSetChanged ();
                                } else
                                {
                                    emptyTextView.setText (Tools.getStr (
                                            activity, R.string.SEARCHNOSHOP));
                                }
                            }
                        } catch (JSONException e)
                        {
                            e.printStackTrace ();
                           dialogDismiss ();
                        }
                       dialogDismiss ();
                    }
                }

        );
    }

    @Override
    protected void jumpTo (int position)
    {
        Intent intent = new Intent (SearchShopActivity.this, MainActivity.class);
        intent.putExtra ("shop", list.get (position));
        ConfigUtil.write (this, MyConstant.KEY_GUIDE_FIRST, false);

        if ( ShopListActivity.instance != null )
        {
            ShopListActivity.instance.finish ();
        }

        startActivity (intent);
        AnimUtil.animToSlideFinish (activity);
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
            query ();
        }
    }

}
