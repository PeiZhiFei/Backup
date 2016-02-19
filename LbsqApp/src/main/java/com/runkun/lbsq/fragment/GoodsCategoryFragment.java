package com.runkun.lbsq.fragment;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.util.SparseArray;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.BaseExpandableListAdapter;
import android.widget.ExpandableListView;
import android.widget.ExpandableListView.OnChildClickListener;
import android.widget.ExpandableListView.OnGroupClickListener;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.lidroid.xutils.ViewUtils;
import com.lidroid.xutils.exception.HttpException;
import com.lidroid.xutils.http.RequestParams;
import com.lidroid.xutils.http.ResponseInfo;
import com.lidroid.xutils.http.callback.RequestCallBack;
import com.lidroid.xutils.view.annotation.ViewInject;
import com.runkun.lbsq.R;
import com.runkun.lbsq.activity.BaseAcitivity;
import com.runkun.lbsq.activity.LoginActivity;
import com.runkun.lbsq.activity.MainActivity;
import com.runkun.lbsq.adapter.GoodListAdapter;
import com.runkun.lbsq.bean.Good;
import com.runkun.lbsq.interfaces.onButtonClick;
import com.runkun.lbsq.utils.AnimUtil;
import com.runkun.lbsq.utils.HAFGridView;
import com.runkun.lbsq.utils.HttpHelper;
import com.runkun.lbsq.utils.MyConstant;
import com.runkun.lbsq.utils.Tools;
import com.runkun.lbsq.view.GoodsCategoryCombineView;
import com.runkun.lbsq.view.GoodsCategoryCombineView2Test;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import feifei.project.util.ConfigUtil;
import feifei.project.util.L;

public class GoodsCategoryFragment extends MainFirstBaseFragment
{
    private final String pageSize = "20";

    @ViewInject(R.id.goods_category_list)
    private ExpandableListView gcCategoryExpListView;

    @ViewInject(R.id.goods_category_goods_list)
    private HAFGridView goodsGridView;

    @ViewInject(R.id.goods_category_name)
    private TextView goodsClassNameTV;

    private TextView footerTV;
    private ProgressBar footerPB;

    private int pageNumber = 1;

    private int x = 0;

    private GoodListAdapter goodListAdapter;
    private GoodsExpCategoryAdapter goodsExpListAdapter;

    private int lastExpandedGroup = 0;
    private int selectedSubClass = 0;

    private String curClassId;
    private String curGroupClassId;

    @Override
    public View onCreateView (LayoutInflater inflater, ViewGroup container,
                              Bundle savedInstanceState)
    {
        super.onCreateView (inflater, container, savedInstanceState);
        View v = inflater.inflate (R.layout.fragment_goods_category, container,
                false);
        ViewUtils.inject (this, v);
        dialogInit ();
        initView ();
        return v;
    }


    @SuppressLint("RtlHardcoded")
    private void initView ()
    {
        View footerView = ((LayoutInflater) getActivity ().getSystemService (
                Context.LAYOUT_INFLATER_SERVICE)).inflate (
                R.layout.listview_footer, goodsGridView, false);
        footerTV = (TextView) footerView.findViewById (R.id.footer_tv);
        footerPB = (ProgressBar) footerView.findViewById (R.id.footer_pb);
        goodsGridView.addFooterView (footerView);
        footerTV.setOnClickListener (new OnClickListener ()
        {

            @Override
            public void onClick (View v)
            {
                if ( "true".equals (goodsGridView.getTag ()) )
                {
                    queryGoodsByClass (false);
                }
            }

        });

        goodListAdapter = new GoodListAdapter (getActivity (), this);
        goodsGridView.setAdapter (goodListAdapter);

        goodsExpListAdapter = new GoodsExpCategoryAdapter ();
        gcCategoryExpListView.setAdapter (goodsExpListAdapter);

        gcCategoryExpListView
                .setOnGroupClickListener (new OnGroupClickListener ()
                {

                    @Override
                    public boolean onGroupClick (ExpandableListView parent,
                                                 View group, int pos, long id)
                    {
                        if ( !parent.isGroupExpanded (pos) )
                        {
                            CategoryItem item = (CategoryItem) goodsExpListAdapter
                                    .getGroup (pos);
                            if ( item.hasChild )
                            {
                                if ( pos != lastExpandedGroup )
                                {
                                    int[] loc = new int[2];
                                    group.getLocationInWindow (loc);
                                    int distance = loc[1] - x;

                                    gcCategoryExpListView.smoothScrollBy (
                                            distance, 1000);

                                    lastExpandedGroup = pos;
                                }
                                gcCategoryExpListView.expandGroup (pos);
                            }
                        } else
                        {
                            gcCategoryExpListView.collapseGroup (pos);
                        }
                        return true;
                    }

                });

        gcCategoryExpListView
                .setOnChildClickListener (new OnChildClickListener ()
                {

                    @Override
                    public boolean onChildClick (ExpandableListView parent,
                                                 View child, int groupPosition, int childPosition,
                                                 long id)
                    {
                        selectedSubClass = childPosition;
                        lastExpandedGroup = groupPosition;
                        goodsExpListAdapter.notifyDataSetInvalidated ();
                        CategoryItem item = (CategoryItem) goodsExpListAdapter
                                .getChild (groupPosition, childPosition);
                        curClassId = item.classId;
                        curGroupClassId = item.parentClassId;
                        goodsClassNameTV.setText (item.className);

                        pageNumber = 1;

                        queryGoodsByClass (true);

                        return false;
                    }

                });

        dialogProgress (Tools.getStr (fragment, R.string.LOADING));
        RequestParams rp = new RequestParams ();
        rp.addQueryStringParameter ("store_id", storeId);

        HttpHelper.postByCommand ("maincategory", rp,
                new RequestCallBack<String> ()
                {

                    @Override
                    public void onFailure (HttpException arg0, String arg1)
                    {
                        Tools.toast (mainActivity,
                                Tools.getStr (fragment, R.string.NETWORKERROR));
                        dialogDismiss ();
                    }

                    @Override
                    public void onSuccess (ResponseInfo<String> responseInfo)
                    {
                        String result = responseInfo.result;
                        try
                        {
                            JSONObject json = new JSONObject (result);
                            result = json.getString ("code");
                            if ( result.equals ("200") )
                            {
                                JSONArray ja = json.getJSONArray ("datas");
                                for (int i = 0; i < ja.length (); i++)
                                {
                                    JSONObject jo = ja.getJSONObject (i);
                                    CategoryItem bean = json2Bean (jo);
                                    if ( bean.hasChild )
                                    {
                                        goodsExpListAdapter.appendGroup (bean);
                                    }
                                    if ( bean.hasChild )
                                    {
                                        JSONArray subJa = jo
                                                .getJSONArray ("subclass");
                                        // if (subJa.length() > 0) {
                                        for (int m = 0; m < subJa.length (); m++)
                                        {
                                            JSONObject subJo = subJa
                                                    .getJSONObject (m);
                                            CategoryItem subBean = json2Bean (subJo);
                                            bean.appendSubclass (subBean);
                                        }
                                        // }

                                    }
                                }

                                if ( ja.length () > 0 )
                                {
                                    curGroupClassId = ((CategoryItem) goodsExpListAdapter
                                            .getGroup (0)).classId;
                                }

                                goodsExpListAdapter.notifyDataSetChanged ();
                                if ( ja.length () > 0 )
                                {
                                    gcCategoryExpListView.expandGroup (0);
                                    CategoryItem bean = (CategoryItem) goodsExpListAdapter
                                            .getChild (0, 0);
                                    curClassId = bean.classId;
                                    goodsClassNameTV.setText (bean.className);
                                    queryGoodsByClass (true);
                                }
                            }

                        } catch (JSONException e)
                        {
                            e.printStackTrace ();
                        }
                        dialogDismiss ();

                    }

                    private CategoryItem json2Bean (JSONObject jo)
                    {
                        CategoryItem bean = new CategoryItem ();
                        try
                        {
                            bean.classId = jo.getString ("class_id");
                            bean.classImage = jo.optString ("class_image");
                            bean.className = jo.getString ("class_name");
                            bean.hasChild = jo.optString ("hasChind")
                                    .equals ("1");
                            bean.parentClassId = jo.optString (
                                    "parent_class_id", "");
                            bean.storeId = jo.optString ("store_id");
                        } catch (JSONException e)
                        {
                            e.printStackTrace ();
                        }
                        return bean;
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

    private void queryGoodsByClass (final boolean isNewClass)
    {
        dialogProgress ("请稍候");
        RequestParams rp = new RequestParams ();
        rp.addQueryStringParameter ("store_id", storeId);
        rp.addQueryStringParameter ("class_id", curClassId);
        rp.addQueryStringParameter ("pagesize", pageSize);
        rp.addQueryStringParameter ("pagenumber", String.valueOf (pageNumber++));
        updateFooter (true, null);
        HttpHelper.postByCommand ("goodslistmore", rp,
                new RequestCallBack<String> ()
                {

                    @Override
                    public void onFailure (HttpException arg0, String arg1)
                    {
                        dialogDismiss ();
                        Tools.toast (getActivity (),
                                Tools.getStr (fragment, R.string.NETWORKERROR));
                    }

                    @Override
                    public void onSuccess (ResponseInfo<String> resultInfo)
                    {
                        if ( isNewClass )
                        {
                            goodListAdapter.clear ();
                        }
                        String result = resultInfo.result;
                        L.l (result);
                        try
                        {
                            JSONObject jo = new JSONObject (result);
                            result = jo.getString ("code");
                            if ( result.equals ("200") )
                            {
                                String haveMore = jo.getString ("haveMore");
                                if ( "true".equals (haveMore) )
                                {
                                    updateFooter (false, Tools.getStr (fragment,
                                            R.string.CLICKLOADINGMORE));
                                } else
                                {
                                    updateFooter (false, Tools.getStr (fragment,
                                            R.string.SEARCHNOMOREGOODS));
                                }
                                goodsGridView.setTag (haveMore);
                                JSONArray ja = jo.getJSONArray ("datas");
                                for (int i = 0; i < ja.length (); i++)
                                {
                                    jo = ja.getJSONObject (i);
                                    Good e = json2Entity (jo);
                                    goodListAdapter.addGood (e);
                                }
                                goodListAdapter.notifyDataSetChanged ();
                            }
                        } catch (JSONException e)
                        {
                            e.printStackTrace ();
                        }
                        dialogDismiss ();

                        // TODO: 15-9-16
                        //                        gcCategoryExpListView.setSelection(5);
                    }

                    private Good json2Entity (JSONObject jo)
                    {
                        Good entity = new Good ();
                        try
                        {
                            entity.setGoodsId (jo.getString ("goods_id"));
                            entity.setGoodsName (jo.getString ("goods_name"));
                            entity.setGoodsPic (jo.getString ("goods_pic"));
                            entity.setGoodsPrice (Tools.getStr (fragment,
                                    R.string.ADAPTERPRICE)
                                    + jo.getString ("goods_price"));
                            entity.setStoreId (jo.getString ("store_id"));
                        } catch (JSONException e)
                        {
                            e.printStackTrace ();
                        }
                        return entity;
                    }

                });
    }

    @SuppressWarnings("unused")
    private class CategoryItem
    {
        public String parentClassId;
        public String classId;
        public String className;
        public String storeId;
        public String classImage;
        public boolean hasChild;
        List<CategoryItem> subclasses;

        public CategoryItem ()
        {
            subclasses = new LinkedList<CategoryItem> ();
        }

        public void appendSubclass (CategoryItem subclass)
        {
            subclasses.add (subclass);
        }

        public CategoryItem getSubclass (int idx)
        {
            return subclasses.get (idx);
        }
    }

    class GoodsExpCategoryAdapter extends BaseExpandableListAdapter
    {
        private Map<String, CategoryItem> data;
        private SparseArray<String> classIdIndexer;
        private int groupSize = 0;
        private LayoutInflater inflater;

        public GoodsExpCategoryAdapter ()
        {
            data = new HashMap<String, CategoryItem> ();
            classIdIndexer = new SparseArray<String> ();
            inflater = getActivity ().getLayoutInflater ();
        }

        public void setData (Map<String, CategoryItem> data)
        {
            classIdIndexer.clear ();

            Iterator<String> ite = data.keySet ().iterator ();
            int idx = 0;
            while (ite.hasNext ())
            {
                classIdIndexer.append (idx++, ite.next ());
            }
            this.data = data;
            groupSize = classIdIndexer.size ();
        }

        public void appendGroup (CategoryItem group)
        {
            classIdIndexer.append (groupSize++, group.classId);
            data.put (group.classId, group);
        }

        public void appendChild (String parentClassId, CategoryItem child)
        {
            data.get (parentClassId).appendSubclass (child);
        }

        @Override
        public Object getChild (int groupPosition, int chinldPosition)
        {
            CategoryItem group = getGroupByPosition (groupPosition);
            return group.getSubclass (chinldPosition);
        }

        @Override
        public long getChildId (int groupPosition, int chinldPosition)
        {
            CategoryItem child = (CategoryItem) getChild (groupPosition,
                    chinldPosition);
            return Long.valueOf (child.classId);
        }

        @Override
        public View getChildView (int groupPosition, int childPosition,
                                  boolean isLastChild, View convert, ViewGroup parent)
        {
            if ( convert == null )
            {
                convert = inflater.inflate (R.layout.item_category_child,
                        parent, false);
            }
            TextView tv = (TextView) convert
                    .findViewById (R.id.gc_sub_categoryname);
            CategoryItem item = getChildByPosition (groupPosition, childPosition);
            tv.setText (item.className);
            ImageView indicator = (ImageView) convert
                    .findViewById (R.id.indicator);

            // tv.setTextColor(Color.rgb(153, 153, 153));
            if ( item.parentClassId.equals (curGroupClassId)
                    && selectedSubClass == childPosition )
            {
                // tv.setBackgroundColor(Color.parseColor("#DBDBDB"));
                indicator.setVisibility (View.VISIBLE);
                tv.setTextColor (Color.rgb (57, 172, 105));
            } else
            {
                // tv.setBackgroundColor(getResources().getColor(R.color.white));
                indicator.setVisibility (View.GONE);
                tv.setTextColor (Color.rgb (102, 102, 102));
            }
            return convert;
        }

        @Override
        public int getChildrenCount (int groupPosition)
        {
            CategoryItem group = getGroupByPosition (groupPosition);
            return group.subclasses.size ();
        }

        @Override
        public Object getGroup (int groupPosition)
        {
            return getGroupByPosition (groupPosition);
        }

        @Override
        public int getGroupCount ()
        {
            return classIdIndexer.size ();
        }

        @Override
        public long getGroupId (int groupPosition)
        {
            return groupPosition;
        }

        @Override
        public View getGroupView (int groupPosition, boolean isExpanded,
                                  View convertView, ViewGroup parent)
        {
            if ( x == 0 )
            {
                int[] loc = new int[2];
                gcCategoryExpListView.getLocationInWindow (loc);
                x = loc[1];
            }
            if ( convertView == null )
            {
                convertView = inflater.inflate (R.layout.item_category_group,
                        parent, false);
                GoodsClassBean goodsClassBean = new GoodsClassBean ();
                goodsClassBean.tv = (TextView) convertView
                        .findViewById (R.id.gc_exp_group_name);
                convertView.setTag (goodsClassBean);
            }

            CategoryItem group = getGroupByPosition (groupPosition);
            GoodsClassBean gcb = (GoodsClassBean) convertView.getTag ();
            // if (!group.classId.equals(gcb.classId)) {
            // gcb.classId = group.classId;
            // new XUtilsImageLoader(getActivity(),
            // gcb.imgView.getMeasuredWidth(),
            // gcb.imgView.getMeasuredHeight()).display(gcb.imgView,
            // group.classImage);
            // }
            //
            // if (!group.hasChild) {
            // convertView.setBackgroundColor(Color.YELLOW);
            // }
            if ( isExpanded )
            {
                convertView.setBackgroundColor (Color.WHITE);
                gcb.tv.setTextColor (Color.rgb (102, 102, 102));
                gcb.tv.setSelected (true);
            } else if ( curGroupClassId.equals (group.classId) )
            {
                convertView.setBackgroundColor (Color.WHITE);
                gcb.tv.setTextColor (Color.rgb (102, 102, 102));
            } else
            {
                convertView.setBackgroundColor (Color.rgb (242, 242, 242));
                gcb.tv.setTextColor (Color.rgb (102, 102, 102));
                gcb.tv.setSelected (false);
            }

            gcb.tv.setText (group.className);

            return convertView;
        }

        @Override
        public boolean hasStableIds ()
        {
            return false;
        }

        @Override
        public boolean isChildSelectable (int groupPosition, int chinldPosition)
        {
            return true;
        }

        private CategoryItem getGroupByPosition (int groupPosition)
        {
            String classId = classIdIndexer.get (groupPosition);
            CategoryItem group = data.get (classId);
            return group;
        }

        private CategoryItem getChildByPosition (int groupPosition,
                                                 int childPosition)
        {
            String classId = classIdIndexer.get (groupPosition);
            CategoryItem group = data.get (classId);
            return group.getSubclass (childPosition);
        }

    }

    class GoodsClassBean
    {
        // ImageView imgView;
        TextView tv;
        String classId;
    }

    @Override
    public void onAttach(Activity activity)
    {
        super.onAttach(activity);
        mainActivity = (MainActivity) activity;
    }

    public void add2Shopcart (View triggerView, GoodsCategoryCombineView gccv)
    {
        String memberId = ConfigUtil.readString (getActivity (),
                MyConstant.KEY_MEMBERID, "");
        if ( Tools.isEmpty (memberId) )
        {
            Tools.dialog (getActivity (),
                    Tools.getStr (fragment, R.string.REQUESTLOGIN), true,
                    new onButtonClick ()
                    {

                        @Override
                        public void buttonClick ()
                        {
                            getActivity ().startActivity (new Intent (getActivity (), LoginActivity.class));
                            AnimUtil.animToSlide (getActivity ());
                        }
                    });
        } else
        {
            RequestParams requestParams = new RequestParams ();
            requestParams.addQueryStringParameter ("goods_id", gccv.getGoodId ());
            requestParams.addQueryStringParameter ("quantity", gccv
                    .getOrderCountTV ().getText ().toString ().trim ());
            requestParams.addQueryStringParameter ("member_id", memberId);
            ImageView aniImg = new ImageView (getActivity ());
            aniImg.setBackgroundDrawable (gccv.getGoodImgIV ().getDrawable ());
            Tools.addShopCard ((BaseAcitivity) getActivity (),
                    "addshopcar", triggerView, mainActivity.getShopcardLayout (), aniImg, requestParams, true);
        }

    }

    public void add2Shopcart (View triggerView, GoodsCategoryCombineView2Test gccv)
    {
        String memberId = ConfigUtil.readString (getActivity (),
                MyConstant.KEY_MEMBERID, "");
        if ( Tools.isEmpty (memberId) )
        {
            Tools.dialog (getActivity (),
                    Tools.getStr (fragment, R.string.REQUESTLOGIN), true,
                    new onButtonClick ()
                    {

                        @Override
                        public void buttonClick ()
                        {
                            getActivity ().startActivity (new Intent (getActivity (), LoginActivity.class));
                            AnimUtil.animToSlide (getActivity ());
                        }
                    });
        } else
        {
            RequestParams requestParams = new RequestParams ();
            requestParams.addQueryStringParameter ("goods_id", gccv.getGoodId ());
            requestParams.addQueryStringParameter ("quantity", gccv
                    .getOrderCountTV ().getText ().toString ().trim ());
            requestParams.addQueryStringParameter ("member_id", memberId);
            ImageView aniImg = new ImageView (getActivity ());
            aniImg.setBackgroundDrawable (gccv.getGoodImgIV ().getDrawable ());
            Tools.addShopCard ((BaseAcitivity) getActivity (),
                    "addshopcar", triggerView, mainActivity.getShopcardLayout (), aniImg, requestParams, true);
        }

    }

}
