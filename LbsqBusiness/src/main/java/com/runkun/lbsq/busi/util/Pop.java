package com.runkun.lbsq.busi.util;

import android.app.Dialog;
import android.content.Context;
import android.graphics.Color;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseExpandableListAdapter;
import android.widget.ExpandableListView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.TypeReference;
import com.lidroid.xutils.HttpUtils;
import com.lidroid.xutils.exception.HttpException;
import com.lidroid.xutils.http.RequestParams;
import com.lidroid.xutils.http.ResponseInfo;
import com.lidroid.xutils.http.callback.RequestCallBack;
import com.lidroid.xutils.http.client.HttpRequest;
import com.runkun.lbsq.busi.R;
import com.runkun.lbsq.busi.bean.CategoryBean;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import feifei.project.util.ConfigUtil;

/**
 * 作者    你的名字
 * 时间    2015/8/29 14:06
 * 文件    lbsq_busi
 * 描述
 */
public class Pop extends Dialog
{

    Context context;
    ExpandableListView expandableListView;
    GoodsExpCategoryAdapter goodsExpListAdapter;
    ChildClickListener onChildClickListener;

    public Pop(Context context, ChildClickListener onChildClickListener)
    {
        super (context);
        this.context = context;
        this.onChildClickListener = onChildClickListener;
    }

    public Pop(Context context, int theme, ChildClickListener onChildClickListener)
    {
        super (context, theme);
        this.context = context;
        this.onChildClickListener = onChildClickListener;
    }

    protected Pop(Context context, boolean cancelable, OnCancelListener cancelListener, ChildClickListener onChildClickListener)
    {
        super (context, cancelable, cancelListener);
        this.context = context;
        this.onChildClickListener = onChildClickListener;
    }

    protected Dialog dialog;
    private boolean open = false;


    public void dialogInit()
    {
        dialog = new Dialog (context, R.style.dialog_loading_style);
        dialog.getWindow ().getAttributes ().gravity = Gravity.CENTER;
        dialog.setContentView (R.layout.dialog_lbsq);
        dialog.setCancelable (false);
    }

    public synchronized void dialogDismiss()
    {
        if (open && dialog != null)
        {
            dialog.dismiss ();
            open = false;
        }
    }

    public synchronized void dialogProgress(String text)
    {
        if (!open && dialog != null)
        {
            ImageView image = (ImageView) dialog
                    .findViewById (R.id.loadingImageView);
            image.startAnimation (AnimUtil.getDialogRotateAnimation ());
            TextView textView = (TextView) dialog
                    .findViewById (R.id.id_tv_loadingmsg);
            if (text != null)
            {
                textView.setText (text);
            }
            dialog.show ();
            open = true;
        }
    }

    public void init()
    {
        dialogInit ();
        View view = LayoutInflater.from (context).inflate (R.layout.pop, null);
        expandableListView = (ExpandableListView) view.findViewById (R.id.goods_category_list);
        querGoods ();
        test2 ();
        setContentView (view, new LinearLayout.LayoutParams ((int) (Tools.getWidth (context) / 1.2), (int) (Tools.getHeight (context) / 1.3), Gravity.CENTER));
    }

    private void querGoods()
    {
        RequestParams rp = new RequestParams ();
        rp.addQueryStringParameter ("store_id", ConfigUtil.readString (context, MyConstant.KEY_STOREID, ""));
        HttpUtils httpUtils = new HttpUtils ();
        httpUtils.configCurrentHttpCacheExpiry(30000);//设置超时时间
        httpUtils.send (HttpRequest.HttpMethod.POST, MyConstant.C31, rp,
                new RequestCallBack<String> ()
                {

                    @Override
                    public void onStart()
                    {
                        super.onStart ();
                        dialogProgress ("加载中");
                    }

                    @Override
                    public void onFailure(HttpException arg0, String arg1)
                    {
                        Tools.toast (context, "网络连接错误");
                        dialogDismiss ();
                        arg0.printStackTrace ();
                    }

                    @Override
                    public void onSuccess(ResponseInfo<String> responseInfo)
                    {
                        String result = responseInfo.result;
                        try
                        {
                            JSONObject json = new JSONObject (result);
                            result = json.getString ("code");
                            if (result.equals ("200"))
                            {
                                listGoods.clear ();
                                listGoods.addAll (JSON.parseObject (json.getString ("datas"),
                                        new TypeReference<List<CategoryBean>> ()
                                        {
                                        }));
                                goodsExpListAdapter.notifyDataSetChanged ();

                                //                                //设置首类第一个商品展示
                                //                                if (goodsExpListAdapter.getGroupCount() > 0)
                                //                                {
                                //                                    curGroupClassId = goodsExpListAdapter.getGroup(0).getClass_id();
                                //                                    expandableListView.expandGroup(0);
                                //                                    CategoryBean.SubclassEntity bean = goodsExpListAdapter.getChild(0, 0);
                                //                                    curClassId = bean.getClass_id();
                                //                                }
                                //                                for (int i=0;i<listGoods.size();i++){
                                //                                    expandableListView.expandGroup(i);
                                //                                }
                                show ();

                            }

                        } catch (JSONException e)
                        {
                            e.printStackTrace ();
                        }
                        dialogDismiss ();
                    }

                });
    }


    private void test2()
    {
        goodsExpListAdapter = new GoodsExpCategoryAdapter (listGoods);
        expandableListView.setAdapter (goodsExpListAdapter);

        expandableListView
                .setOnGroupClickListener (new ExpandableListView.OnGroupClickListener ()
                {

                    @Override
                    public boolean onGroupClick(ExpandableListView parent,
                                                View group, int pos, long id)
                    {
                        if (!parent.isGroupExpanded (pos))
                        {

                            CategoryBean item = goodsExpListAdapter.getGroup (pos);
                            if (item.getHasChildren ())
                            {
                                if (pos != lastExpandedGroup)
                                {
                                    int[] loc = new int[2];
                                    group.getLocationInWindow (loc);
                                    int distance = loc[1] - x;

                                    expandableListView.smoothScrollBy (
                                            distance, 1000);

                                    lastExpandedGroup = pos;
                                }
                                expandableListView.expandGroup (pos);
                            }
                        } else
                        {
                            expandableListView.collapseGroup (pos);
                        }
                        return true;
                    }

                });

        expandableListView
                .setOnChildClickListener (new ExpandableListView.OnChildClickListener ()
                {

                    @Override
                    public boolean onChildClick(ExpandableListView parent,
                                                View child, int groupPosition, int childPosition,
                                                long id)
                    {
                        selectedSubClass = childPosition;
                        lastExpandedGroup = groupPosition;
                        goodsExpListAdapter.notifyDataSetInvalidated ();
                        CategoryBean.SubclassEntity item = goodsExpListAdapter
                                .getChild (groupPosition, childPosition);
                        curClassId = item.getClass_id ();
                        curGroupClassId = item.getParent_class_id ();
                        Log.e ("log",curGroupClassId + "######" + curClassId);
                        String groupName = goodsExpListAdapter.getGroup (groupPosition).getClass_name ();
                        String childName = item.getClass_name ();
                        dismiss ();
                        if (onChildClickListener != null)
                        {
                            onChildClickListener.onChildClick (curGroupClassId, curClassId, groupName, childName);
                        }
                        return false;
                    }

                });

    }

    private int x = 0;
    private int lastExpandedGroup = 0;
    private int selectedSubClass = 0;
    private String curClassId;
    private String curGroupClassId;
    private List<CategoryBean> listGoods = new ArrayList<> ();


    class GoodsExpCategoryAdapter extends BaseExpandableListAdapter
    {
        private List<CategoryBean> categoryBeans;

        public GoodsExpCategoryAdapter(List<CategoryBean> categoryBeans)
        {
            this.categoryBeans = categoryBeans;
        }


        @Override
        public CategoryBean.SubclassEntity getChild(int groupPosition, int chinldPosition)
        {

            return categoryBeans.get (groupPosition).getSubclass ().get (chinldPosition);
        }

        @Override
        public long getChildId(int groupPosition, int chinldPosition)
        {
            return chinldPosition;
        }

        @Override
        public View getChildView(int groupPosition, int childPosition,
                                 boolean isLastChild, View convert, ViewGroup parent)
        {
            ChildHolder childHolder;
            if (convert == null)
            {
                childHolder = new ChildHolder ();
                convert = LayoutInflater.from (context).inflate (R.layout.item_category_child,
                        parent, false);
                childHolder.tv = (TextView) convert
                        .findViewById (R.id.gc_sub_categoryname);
                childHolder.iv = (ImageView) convert
                        .findViewById (R.id.indicator);
                convert.setTag (childHolder);
            } else
            {
                childHolder = (ChildHolder) convert.getTag ();
            }

            CategoryBean.SubclassEntity subclassEntity = getChild (groupPosition, childPosition);
            childHolder.tv.setText (subclassEntity.getClass_name ());
            childHolder.tv.setTextColor (Color.rgb (102, 102, 102));
            childHolder.tv.setTextSize(15);
            childHolder.tv.setBackgroundResource (R.drawable.white_gray_click);
            return convert;
        }

        @Override
        public int getChildrenCount(int groupPosition)
        {
            return categoryBeans.get (groupPosition).getSubclass ().size ();
        }

        @Override
        public CategoryBean getGroup(int groupPosition)
        {
            return categoryBeans.get (groupPosition);
        }

        @Override
        public int getGroupCount()
        {
            return categoryBeans.size ();
        }

        @Override
        public long getGroupId(int groupPosition)
        {
            return groupPosition;
        }

        @Override
        public View getGroupView(int groupPosition, boolean isExpanded,
                                 View convertView, ViewGroup parent)
        {
            //做动画的，group顶到头
            if (x == 0)
            {
                int[] loc = new int[2];
                expandableListView.getLocationInWindow (loc);
                x = loc[1];
            }
            GroupHolder groupHolder;
            if (convertView == null)
            {
                groupHolder = new GroupHolder ();
                convertView = LayoutInflater.from (context).inflate (R.layout.item_category_group,
                        parent, false);
                groupHolder.tv = (TextView) convertView
                        .findViewById (R.id.gc_exp_group_name);
                convertView.setTag (groupHolder);
            } else
            {
                groupHolder = (GroupHolder) convertView.getTag ();
            }
            CategoryBean categoryBean = getGroup (groupPosition);
            groupHolder.tv.setText (categoryBean.getClass_name ());
            groupHolder.tv.setTextColor (Color.parseColor("#A52A2A"));
            groupHolder.tv.setTextSize (18);
            convertView.setBackgroundColor (Color.parseColor("#DBDBDB"));
            return convertView;
        }

        @Override
        public boolean hasStableIds()
        {
            return false;
        }

        @Override
        public boolean isChildSelectable(int groupPosition, int chinldPosition)
        {
            return true;
        }

    }

    class GroupHolder
    {
        TextView tv;
    }

    class ChildHolder
    {
        TextView tv;
        ImageView iv;
    }

    //todo 这里还应该返回父和子的classname
    public interface ChildClickListener
    {
        public void onChildClick(String groupId, String childId, String groupName, String ChildName);
    }
}
