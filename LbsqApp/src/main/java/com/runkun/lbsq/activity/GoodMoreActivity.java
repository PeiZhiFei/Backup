package com.runkun.lbsq.activity;

import android.annotation.TargetApi;
import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.os.Build;
import android.os.Bundle;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.AbsListView;
import android.widget.AbsListView.OnScrollListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ArrayAdapter;
import android.widget.BaseAdapter;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.ProgressBar;
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
import com.runkun.lbsq.adapter.GoodAdapter;
import com.runkun.lbsq.bean.GoodMore;
import com.runkun.lbsq.bean.Shop;
import com.runkun.lbsq.utils.AnimUtil;
import com.runkun.lbsq.utils.HttpHelper;
import com.runkun.lbsq.utils.MyConstant;
import com.runkun.lbsq.utils.Tools;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import feifei.project.util.ConfigUtil;
import feifei.project.util.L;
import feifei.project.view.BottomView;

@TargetApi(Build.VERSION_CODES.ICE_CREAM_SANDWICH)
public class GoodMoreActivity extends BaseAcitivity
{

    @ViewInject(R.id.listtest)
    ListView listView;
    @ViewInject(R.id.footer_tv)
    TextView textView;
    @ViewInject(R.id.shopcart_btn)
    public ImageButton shopcard;

    GoodAdapter adapter;
    List<GoodMore> list = new ArrayList<GoodMore> ();
    String store_id;
    String class_id;
    String category_id;
    String class_name;
    View footer;
    boolean more = true;

    private int pageNumber = 1;
    private TextView footerTV;
    private ProgressBar footerPB;
    ArrayList<String> classname = new ArrayList<String> ();
    ArrayList<String> classid = new ArrayList<String> ();
    ArrayAdapter<String> adapter2;
    Drawable drawable;
    boolean isTejia = false;

    @Override
    protected void onCreate (Bundle savedInstanceState)
    {
        super.onCreate (savedInstanceState);
        setContentView (R.layout.activity_goodsmore);
        ViewUtils.inject (this);
        tint ();
        initActionbar ();
        dialogInit();
        Intent intent = getIntent ();
        if ( intent != null )
        {
            store_id = intent.getStringExtra ("store_id");
            class_id = intent.getStringExtra ("class_id");
            category_id = intent.getStringExtra ("category_id");
            class_name = intent.getStringExtra ("class_name");
        }
        setTitles (class_name);
        shopcard.setOnClickListener (new OnClickListener ()
        {

            @Override
            public void onClick (View v)
            {
                Intent intent = new Intent (activity, MainActivity.class);
                intent.putExtra ("types", "shopcard");
                startActivity (intent);
                AnimUtil.animBackSlideFinish (activity);
            }
        });
        isTejia = class_name.equals (MyConstant.CLASSNAMETEJIA) ? true : false;
        footer = LayoutInflater.from (this).inflate (R.layout.listview_footer,
                null);
        footerTV = (TextView) footer.findViewById (R.id.footer_tv);
        footerPB = (ProgressBar) footer.findViewById (R.id.footer_pb);
        adapter = new GoodAdapter (this, list, R.layout.item_good, true, false,
                isTejia);
        listView.addFooterView (footer);

        listView.setAdapter (adapter);
        listView.setOnItemClickListener (new OnItemClickListener ()
        {
            @Override
            public void onItemClick (AdapterView<?> parent, View view,
                                     int position, long id)
            {
                if ( view != footer )
                {
                    Intent intent = new Intent (activity,
                            GoodDetailActivity.class);
                    intent.putExtra ("goods_id", list.get (position).getGoodsId ());
                    startActivity(intent);
                    AnimUtil.animToSlide(activity);
                }
            }
        });

        listView.setOnScrollListener (new OnScrollListener ()
        {

            @Override
            public void onScrollStateChanged (AbsListView view, int scrollState)
            {
                if ( scrollState == OnScrollListener.SCROLL_STATE_IDLE )
                {
                    if ( view.getLastVisiblePosition () == view.getCount () - 1 )
                    {
                        if ( more )
                        {
                            queryGoodsByClass ();
                        }
                    }
                }
            }

            @Override
            public void onScroll (AbsListView view, int firstVisibleItem,
                                  int visibleItemCount, int totalItemCount)
            {
            }
        });

        adapter2 = new ArrayAdapter<String> (this,
                android.R.layout.simple_spinner_item, classname);
        queryGoodsByClass ();
        queryClass ();
    }

    @Override
    public void onResume ()
    {
        super.onResume ();
        String countStr = HttpHelper.getPrefParams (this, "shopcount");
        if ( !"".equals (countStr) )
        {
            int count = Integer.valueOf (countStr);
            Tools.refreshShopcartBadge (this, shopcard, count);
        }
    }

    private void queryClass ()
    {
    dialogProgress (activity, Tools.getStr (this, R.string.LOADING));
        RequestParams rp = new RequestParams ();
        rp.addQueryStringParameter ("class_id", category_id);
        HttpUtils httpUtils = new HttpUtils ();
        httpUtils.send (HttpMethod.POST, MyConstant.URLSMALLCLASS, rp,
                new RequestCallBack<String> ()
                {

                    @Override
                    public void onFailure (HttpException arg0, String arg1)
                    {
                        Tools.toast (activity,
                                Tools.getStr (activity, R.string.NETWORKERROR));
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
                                JSONArray jArray = jo.getJSONArray ("data");
                                for (int i = 0; i < jArray.length (); i++)
                                {
                                    JSONObject object = (JSONObject) jArray
                                            .get (i);
                                    classname.add (object
                                            .getString ("class_name"));
                                    classid.add (object.getString ("class_id"));
                                }
                                if ( classname.size () > 0 )
                                {
                                    initArrowDown ();
                                }
                                adapter2.notifyDataSetChanged ();
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

    private void initArrowDown ()
    {
        drawable = getResources ().getDrawable (R.drawable.arrow_down);
        drawable.setBounds (0, 0, drawable.getMinimumWidth (),
                drawable.getMinimumHeight ());
        actionbar_title.setCompoundDrawables (null, null, drawable, null);
        actionbar_title.setCompoundDrawablePadding (10);
        actionbar_title.setOnClickListener (new OnClickListener ()
        {

            @Override
            public void onClick (View v)
            {
                if ( classname.size () <= 0 )
                {
                    Tools.toast (activity,
                            Tools.getStr (activity, R.string.GOODNOMORE));
                } else
                {

                    final BottomView bottomView = new BottomView (activity,
                            R.layout.pop_layout);
                    ListView l = (ListView) bottomView.getView ().findViewById (
                            R.id.listView);
                    l.setAdapter (new PopAdapter ());
                    l.setOnItemClickListener (new OnItemClickListener ()
                    {

                        @Override
                        public void onItemClick (AdapterView<?> parent,
                                                 View view, int position, long id)
                        {
                            bottomView.dismissBottomView ();
                            list.clear ();
                            adapter.notifyDataSetChanged ();
                            class_id = classid.get (position);
                            pageNumber = 1;
                            actionbar_title.setText (classname.get (position));
                            queryGoodsByClass ();
                        }
                    });
                    bottomView.showBottomView ();

                }
            }
        });
        Tools.toast (activity, Tools.getStr (activity, R.string.CLICKME),
                Gravity.TOP);
        Tools.shakeThree (actionbar_title);
    }

    private void queryGoodsByClass ()
    {
        String url;
        RequestParams rp2 = new RequestParams ();
        if ( Tools.isEmpty (class_id) )
        {
            rp2 = new RequestParams ();
            rp2.addQueryStringParameter ("store_id", store_id);
            url = "fruitlistmore";
        } else
        {
            rp2.addQueryStringParameter ("store_id", store_id);
            rp2.addQueryStringParameter ("class_id", class_id);
            url = "goodslistmore";

            // rp2.addQueryStringParameter("class_id", class_id);
            // rp2.addQueryStringParameter("class_id", category_id);
            if ( isTejia )
            {
                rp2.addQueryStringParameter ("is_tejia", "1");
            }
        }
        rp2.addQueryStringParameter ("pagenumber", String.valueOf (pageNumber++));
        updateFooter (true, null);
    dialogProgress (activity, Tools.getStr (this, R.string.LOADING));
        HttpHelper.postByCommand (url, rp2,
                new RequestCallBack<String> ()
                {
                    @Override
                    public void onFailure (HttpException arg0, String arg1)
                    {
                        updateFooter (false,
                                Tools.getStr (activity, R.string.NETWORKERROR));
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
                                String haveMore = jo.getString ("haveMore");
                                if ( "true".equals (haveMore) )
                                {
                                    // updateFooter(false,
                                    // MyString.CLICKLOADINGMORE);
                                    updateFooter (false, "");
                                } else
                                {
                                    more = false;
                                    updateFooter (false, Tools.getStr (activity,
                                            R.string.GOODNOMORE));
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
                                    goodMoreEntity.setStoreId (object
                                            .getString ("store_id"));
                                    if ( isTejia )
                                    {
                                        goodMoreEntity.setGoodsOldPrice (object
                                                .getString ("goods_price"));
                                        goodMoreEntity
                                                .setGoodsTejiaPrice (object
                                                        .getString ("tjprice"));
                                    }
                                    goodMoreEntity.setStoreName (ConfigUtil
                                            .readString (activity,
                                                    Shop.SHOPNAME, ""));
                                    list.add (goodMoreEntity);
                                }
                                adapter.notifyDataSetChanged ();
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

    private void updateFooter (boolean going, String info)
    {
        if ( going )
        {
            footerTV.setVisibility (View.GONE);
            footerPB.setVisibility (View.VISIBLE);
        } else
        {
            footerPB.setVisibility (View.GONE);
            footerTV.setVisibility (View.VISIBLE);
            footerTV.setText (info);
        }
    }

    private final class PopAdapter extends BaseAdapter
    {

        @Override
        public int getCount ()
        {
            return classname.size ();
        }

        @Override
        public Object getItem (int position)
        {
            return classname.get (position);
        }

        @Override
        public long getItemId (int position)
        {
            return position;
        }

        @Override
        public View getView (int position, View convertView, ViewGroup parent)
        {
            ViewHolder holder;
            if ( convertView == null )
            {
                convertView = LayoutInflater.from (activity).inflate (
                        R.layout.pop_item, null);
                holder = new ViewHolder ();
                holder.groupItem = (TextView) convertView
                        .findViewById (R.id.menu_text);
                convertView.setTag (holder);

            } else
            {
                holder = (ViewHolder) convertView.getTag ();
            }

            holder.groupItem.setText (classname.get (position));
            return convertView;
        }

        private final class ViewHolder
        {
            TextView groupItem;
        }
    }

}
