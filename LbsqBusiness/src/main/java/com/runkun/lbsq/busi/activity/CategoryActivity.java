package com.runkun.lbsq.busi.activity;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseExpandableListAdapter;
import android.widget.ExpandableListView;
import android.widget.ImageView;
import android.widget.TextView;

import com.alibaba.fastjson.JSON;
import com.lidroid.xutils.HttpUtils;
import com.lidroid.xutils.exception.HttpException;
import com.lidroid.xutils.http.RequestParams;
import com.lidroid.xutils.http.ResponseInfo;
import com.lidroid.xutils.http.callback.RequestCallBack;
import com.lidroid.xutils.http.client.HttpRequest;
import com.runkun.lbsq.busi.R;
import com.runkun.lbsq.busi.adapter.GoodListAdapterTest;
import com.runkun.lbsq.busi.bean.CategoryBean;
import com.runkun.lbsq.busi.bean.GoodMore;
import com.runkun.lbsq.busi.util.AnimUtil;
import com.runkun.lbsq.busi.util.MyConstant;
import com.runkun.lbsq.busi.util.Tools;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import feifei.project.util.ConfigUtil;
import feifei.project.util.L;

public class CategoryActivity extends FooterListActivity<GoodMore>
{

    private ExpandableListView expListView;
    private TextView className;
    private TextView addNewgoods;
    private GoodsExpCategoryAdapter goodsExpListAdapter;

    private int x = 0;
    private int lastExpandedGroup = 0;
    private int selectedSubClass = 0;

    //点击事件的位置标注
    private String curClassId;
    private String curGroupClassId;
    private String storeId;

    private List<CategoryBean> listGoods = new ArrayList<>();


    @Override
    public void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_category);
//        dialogInit();
        tint();
        initActionbar();
        actionbar_right2.setVisibility(View.VISIBLE);
        actionbar_right2.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                startActivity(new Intent(activity, SearchGoodActivity.class));
            }
        });
        setTitles("商品维护");
        storeId = ConfigUtil.readString (activity, MyConstant.KEY_STOREID, "");
        initView();
    }

    @Override
    protected void itemClick(int position)
    {
        L.l (ConfigUtil.readString (activity, MyConstant.KEY_STOREID, ""));
        L.l(data.get(position).getGoods_id());
        Intent intent = new Intent(activity, ResultActivity.class);
        intent.putExtra ("good_id", data.get (position).getGoods_id ());
        intent.putExtra ("type", "no_have");
        intent.putExtra ("rkey", "123");
        startActivityForResult (intent, 5);
        AnimUtil.animTo (activity);
        }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data)
    {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 5)
        {
            if (resultCode == 8)
            {
                //success
                try
                {
                    queryGoodsByClass(true);
                } catch (Exception e)
                {
                    e.printStackTrace();
                }
            }
        }
    }

    @Override
    protected void initType()
    {
        adapter = new GoodListAdapterTest(this, data);
        url = MyConstant.C32;
        cls = GoodMore.class;
    }

    private void initView()
    {
        expListView = (ExpandableListView) findViewById(R.id.goods_category_list);
        className = (TextView) findViewById(R.id.goods_category_name);
        addNewgoods = (TextView) findViewById(R.id.add_newgoods);

        goodsExpListAdapter = new GoodsExpCategoryAdapter(listGoods);
        expListView.setAdapter(goodsExpListAdapter);

        expListView
                .setOnGroupClickListener(new ExpandableListView.OnGroupClickListener()
                {

                    @Override
                    public boolean onGroupClick(ExpandableListView parent,
                                                View group, int pos, long id)
                    {
                        if (!parent.isGroupExpanded(pos))
                        {

                            CategoryBean item = goodsExpListAdapter.getGroup(pos);
                            if (item.getHasChildren())
                            {
                                if (pos != lastExpandedGroup)
                                {
                                    int[] loc = new int[2];
                                    group.getLocationInWindow(loc);
                                    int distance = loc[1] - x;

                                    expListView.smoothScrollBy(
                                            distance, 1000);

                                    lastExpandedGroup = pos;
                                }
                                expListView.expandGroup(pos);
                            }
                        } else
                        {
                            expListView.collapseGroup(pos);
                        }
                        return true;
                    }

                });

        expListView
                .setOnChildClickListener(new ExpandableListView.OnChildClickListener()
                {

                    @Override
                    public boolean onChildClick(ExpandableListView parent,
                                                View child, int groupPosition, int childPosition,
                                                long id)
                    {
                        selectedSubClass = childPosition;
                        lastExpandedGroup = groupPosition;
                        goodsExpListAdapter.notifyDataSetInvalidated();
                        CategoryBean.SubclassEntity item = goodsExpListAdapter
                                .getChild(groupPosition, childPosition);
                        curClassId = item.getClass_id();
                        curGroupClassId = item.getParent_class_id();
                        className.setText(item.getClass_name());
                        queryGoodsByClass(true);
                        return false;
                    }

                });
        addNewgoods.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                Intent intent = new Intent(activity, CaptureActivity.class);
                startActivity(intent);
                AnimUtil.animTo(activity);
            }
        });
        init();
        emptyTextView.setClickable(false);
        querGoods();
    }


    private void querGoods()
    {
        RequestParams rp = new RequestParams();
        rp.addQueryStringParameter("store_id", storeId);
        HttpUtils httpUtils = new HttpUtils(3000);
        httpUtils.send(HttpRequest.HttpMethod.POST, MyConstant.C31, rp,
                new RequestCallBack<String>()
                {

                    @Override
                    public void onStart()
                    {
                        super.onStart();
                        dialogProgress("加载中");
                    }

                    @Override
                    public void onFailure(HttpException arg0, String arg1)
                    {
                        Tools.toast(activity, "网络连接错误");
                        dialogDismiss();
                        arg0.printStackTrace();
                    }

                    @Override
                    public void onSuccess(ResponseInfo<String> responseInfo)
                    {
                        String result = responseInfo.result;
                        try
                        {
                            JSONObject json = new JSONObject(result);
                            result = json.getString("code");
                            if (result.equals("200"))
                            {
                                listGoods.clear();
                                listGoods.addAll(JSON.parseArray(json.getString("datas"), CategoryBean.class));
                                goodsExpListAdapter.notifyDataSetChanged();
                                //设置首类第一个商品展示
                                if (goodsExpListAdapter.getGroupCount() > 0)
                                {
                                    curGroupClassId = goodsExpListAdapter.getGroup(0).getClass_id();
                                    expListView.expandGroup(0);
                                    CategoryBean.SubclassEntity bean = goodsExpListAdapter.getChild(0, 0);
                                    curClassId = bean.getClass_id();
                                    className.setText(bean.getClass_name());
                                    queryGoodsByClass(true);
                                }

                            }

                        } catch (JSONException e)
                        {
                            e.printStackTrace();
                        }
                        dialogDismiss();
                    }

                });
    }

    private void queryGoodsByClass(final boolean isNewClass)
    {
        if (isNewClass)
        {
            reset();
        }
        rp.addQueryStringParameter("store_id", storeId);
        rp.addQueryStringParameter("class_id", curClassId);
        loadData();
//        RequestParams rp = new RequestParams();

//        HttpUtils httpUtils = new HttpUtils(3000);
//        httpUtils.send(HttpRequest.HttpMethod.POST, MyConstant.C32, rp,
//                new RequestCallBack<String>()
//                {
//                    @Override
//                    public void onStart()
//                    {
//                        super.onStart();
////                        dialogProgress("请稍候");
//                    }
//
//                    @Override
//                    public void onFailure(HttpException arg0, String arg1)
//                    {
////                        dialogDismiss();
//                        Tools.toast(activity, "网络连接错误");
//                        arg0.printStackTrace();
//                    }
//
//                    @Override
//                    public void onSuccess(ResponseInfo<String> resultInfo)
//                    {
//                        if (isNewClass)
//                        {
//                            goodListAdapter.clear();
//                        }
//                        String result = resultInfo.result;
//                        try
//                        {
//                            JSONObject jo = new JSONObject(result);
//                            result = jo.getString("code");
//                            if (result.equals("200"))
//                            {
//                                goods.clear();
//                                goods.addAll(JSON.parseObject(jo.getString("datas"),
//                                        new TypeReference<List<Goods>>()
//                                        {
//                                        }));
//                                goodListAdapter.notifyDataSetChanged();
//                            }
//                        } catch (JSONException e)
//                        {
//                            e.printStackTrace();
//                        } finally
//                        {
////                            dialogDismiss();
//                        }
//
//                    }
//
//                });
    }


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

            return categoryBeans.get(groupPosition).getSubclass().get(chinldPosition);
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
                childHolder = new ChildHolder();
                convert = LayoutInflater.from(activity).inflate(R.layout.item_category_child,
                        parent, false);
                childHolder.tv = (TextView) convert
                        .findViewById(R.id.gc_sub_categoryname);
                childHolder.iv = (ImageView) convert
                        .findViewById(R.id.indicator);
                convert.setTag(childHolder);
            } else
            {
                childHolder = (ChildHolder) convert.getTag();
            }

            CategoryBean.SubclassEntity subclassEntity = getChild(groupPosition, childPosition);
            childHolder.tv.setText(subclassEntity.getClass_name());

            //todo 这里设置了左边标识
            if (subclassEntity.getParent_class_id().equals(curGroupClassId)
                    && selectedSubClass == childPosition)
            {
                childHolder.iv.setVisibility(View.VISIBLE);
                childHolder.tv.setTextColor(Color.rgb(57, 172, 105));
            } else
            {
                childHolder.iv.setVisibility(View.GONE);
                childHolder.tv.setTextColor(Color.rgb(102, 102, 102));
            }
            return convert;
        }

        @Override
        public int getChildrenCount(int groupPosition)
        {
            return categoryBeans.get(groupPosition).getSubclass().size();
        }

        @Override
        public CategoryBean getGroup(int groupPosition)
        {
            return categoryBeans.get(groupPosition);
        }

        @Override
        public int getGroupCount()
        {
            return categoryBeans.size();
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
                expListView.getLocationInWindow(loc);
                x = loc[1];
            }
            GroupHolder groupHolder;
            if (convertView == null)
            {
                groupHolder = new GroupHolder();
                convertView = LayoutInflater.from(activity).inflate(R.layout.item_category_group,
                        parent, false);
                groupHolder.tv = (TextView) convertView
                        .findViewById(R.id.gc_exp_group_name);
                convertView.setTag(groupHolder);
            } else
            {
                groupHolder = (GroupHolder) convertView.getTag();
            }
            CategoryBean categoryBean = getGroup(groupPosition);
            groupHolder.tv.setText(categoryBean.getClass_name());
            if (isExpanded)
            {
                convertView.setBackgroundColor(Color.WHITE);
                groupHolder.tv.setTextColor(Color.rgb(102, 102, 102));
                groupHolder.tv.setSelected(true);
            } else if (curGroupClassId.equals(categoryBean.getClass_id()))
            {
                convertView.setBackgroundColor(Color.WHITE);
                groupHolder.tv.setTextColor(Color.rgb(102, 102, 102));
            } else
            {
                convertView.setBackgroundColor(Color.rgb(242, 242, 242));
                groupHolder.tv.setTextColor(Color.rgb(102, 102, 102));
                groupHolder.tv.setSelected(false);
            }
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


    public interface AdapterOnClickListener
    {
        public void onGroupClick();

        public void onChildClick();
    }
}
