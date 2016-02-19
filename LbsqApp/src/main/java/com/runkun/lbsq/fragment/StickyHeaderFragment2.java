/*
 * Copyright 2014 Eduardo Barrenechea
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.runkun.lbsq.fragment;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AbsListView;
import android.widget.AdapterView;
import android.widget.BaseExpandableListAdapter;
import android.widget.ExpandableListView;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import com.alibaba.fastjson.JSON;
import com.lidroid.xutils.ViewUtils;
import com.lidroid.xutils.http.RequestParams;
import com.runkun.lbsq.R;
import com.runkun.lbsq.activity.BaseAcitivity;
import com.runkun.lbsq.activity.LoginActivity;
import com.runkun.lbsq.activity.MainActivity;
import com.runkun.lbsq.adapter.CategoryLeftAdapter;
import com.runkun.lbsq.bean.Category;
import com.runkun.lbsq.bean.GoodDataEntity;
import com.runkun.lbsq.bean.Test;
import com.runkun.lbsq.interfaces.onButtonClick;
import com.runkun.lbsq.utils.AnimUtil;
import com.runkun.lbsq.utils.MyConstant;
import com.runkun.lbsq.utils.Tools;
import com.runkun.lbsq.view.GoodsCategoryCombineView2Test;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import feifei.project.util.ConfigUtil;
import feifei.project.util.L;


public class StickyHeaderFragment2 extends NetFragment implements AdapterView.OnItemClickListener
{
    MainActivity mainActivity;
    private ExpandableListView gcCategoryExpListView;
    private ListView listView;

    private List<Category> datas = new ArrayList<>();
    private List<GoodDataEntity> child = new ArrayList<>();
    private List<Test> left = new ArrayList<>();
    CategoryLeftAdapter adapter;
    private GoodsExpCategoryAdapter goodsExpListAdapter;
    private int lastExpandedGroup = 0;
    private int x = 0;

    boolean st = false;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState)
    {
        super.onCreateView(inflater, container, savedInstanceState);
        final View view = inflater.inflate(R.layout.activity_test, container, false);
        gcCategoryExpListView = (ExpandableListView) view.findViewById(R.id.list);
        listView = (ListView) view.findViewById(R.id.list_left);
        adapter = new CategoryLeftAdapter(getActivity(), left, 0);
        listView.setOnItemClickListener(this);
        //        listView.setAdapter (new ArrayAdapter<> (getActivity (), android.R.layout.simple_list_item_1, Arrays.asList ("4", "2", "4", "5", "7", "2", "4", "5", "7", "2", "4", "5", "7", "2", "4", "5", "7")));
        ViewUtils.inject(this, view);
        dialogInit();

        goodsExpListAdapter = new GoodsExpCategoryAdapter();
        gcCategoryExpListView.setAdapter(goodsExpListAdapter);


//        for (int i = 0; i < left.size(); i++)
//        {
//            gcCategoryExpListView.expandGroup(i);
//        }
//        gcCategoryExpListView.setOnChildClickListener(new ExpandableListView.OnChildClickListener()
//        {
//            @Override
//            public boolean onChildClick(ExpandableListView parent, View v, int groupPosition, int childPosition, long id)
//            {
//                Intent intent = new Intent(getActivity(), GoodDetailActivity.class);
//                intent.putExtra("goods_id", datas.get(groupPosition).getGood_data().get(childPosition).getGoods_id());
//                return false;
//            }
//        });
        gcCategoryExpListView.setOnScrollListener(new AbsListView.OnScrollListener()
        {
            @Override
            public void onScrollStateChanged(AbsListView view, int scrollState)
            {
                L.l ("@@@@@@@@@@@@@@@@@@@     " + scrollState);
//                if (scrollState==1||scrollState==0){
//                    state=3;
//                }

                st = true;

            }

            public void onScroll(AbsListView view, int firstVisibleItem,
                                 int visibleItemCount, int totalItemCount)
            {

//                if (st)
//                {
////                final ExpandableListView ex = (ExpandableListView) view;
//                    L.l(firstVisibleItem + "       $$$$$$$$$$$$$$$$$44");
//                    for (int i=0;i<left.size();i++){
//                        L.l(child.get(firstVisibleItem).getParent_class_name());
//
//                        if (left.get(i).getS().equals( child.get(firstVisibleItem).getParent_class_name()))
//                        {
//                            left.get(i).setSelect(true);
////                            left.get(left.indexOf(child.get(firstVisibleItem).getParent_class_name())).setSelect(true);
//                            adapter.notifyDataSetChanged();
////                            gcCategoryExpListView.setSelectedGroup(left.indexOf(child.get(firstVisibleItem).getParent_class_name()));
//                        }
//                    }
//
//                }

//                if (goodsExpListAdapter.get)
//                if ( ((Category.GoodDataEntity)ex.getItemAtPosition (0) ).getParent_class_name ().equals ("调味品"))
//                    listView.setSelection (2);
//                    adapter.notifyDataSetChanged ();
//                if (state==3){
//                listView.setSelection(3);
//                listView.setItemChecked(3, true);
//                adapter.notifyDataSetChanged();

//                adapter.setSelectedPosition(3);
//                adapter.notifyDataSetInvalidated();
//                }
            }

        });
        gcCategoryExpListView.setOnGroupClickListener(new ExpandableListView.OnGroupClickListener()
        {
            @Override
            public boolean onGroupClick(ExpandableListView parent, View v, int groupPosition, long id)
            {
                return true;
            }
        });


//        JSONObject jsonObject = null;
//        try
//        {
//            jsonObject = new JSONObject(json);
//            datas.addAll(JSON.parseArray(jsonObject.getString("datas"), Category.class));
//            for (int i = 0; i < datas.size(); i++)
//            {
//                left.add(datas.get(i).getClass_name());
//            }
//            listView.setAdapter(adapter);
//        } catch (JSONException e)
//        {
//            e.printStackTrace();
//        }
//


        url = MyConstant.URLCATEGORY;
        rp.addQueryStringParameter("store_id", "29");
        loadData();
        return view;
    }


    @Override
    protected void getData(JSONObject jsonResult) throws JSONException
    {
        try
        {
            datas.addAll(JSON.parseArray(jsonResult.getString("datas"), Category.class));
            for (int i = 0; i < datas.size(); i++)
            {
                Test test = new Test();
                test.setS(datas.get(i).getClass_name());
                test.setSelect(false);
                left.add(test);

                GoodDataEntity goodDataEntity=new GoodDataEntity();
                goodDataEntity.setParent_class_name(datas.get(i).getClass_name());
                child.add(goodDataEntity);
//                L.l(datas.get(i).getGood_data().get(i).getParent_class_name());

            }
            listView.setAdapter(adapter);
            goodsExpListAdapter.expand();
//            listView.setSelection(4);
//            listView.setItemChecked(4, true);
//            listView.setSelected(true);
//            listView.setItemsCanFocus(true);

//            view.setSelected(true);
//            left.get(4).setSelect(true);
//            adapter.notifyDataSetChanged();
////            gcCategoryExpListView.expandGroup(4);
//            gcCategoryExpListView.setSelectedGroup(4);

//            listView.setItemChecked(3, true);
//            adapter.notifyDataSetChanged();
        } catch (JSONException e)
        {
            e.printStackTrace();
        }
    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id)
    {
        //设置样式，并且按下，右边也要改变
//        view.setSelected(true);
        left.get(position).setSelect(true);
        // todo
        for (int i = 0; i < left.size(); i++)
        {
            if (i != position)
            {
                left.get(i).setSelect(false);
            }
        }
        adapter.notifyDataSetChanged();
        gcCategoryExpListView.expandGroup(position);
        gcCategoryExpListView.setSelectedGroup(position);

        //                if (position != lastExpandedGroup)
        //                {
        //                    int[] loc = new int[2];
        //                    view.getLocationInWindow(loc);
        //                    int distance = loc[1] - x;
        //
        //                    gcCategoryExpListView.smoothScrollBy(
        //                            distance, 1000);
        //
        //                    lastExpandedGroup = position;
        //                }

    }

    class GoodsExpCategoryAdapter extends BaseExpandableListAdapter
    {

        public GoodsExpCategoryAdapter()
        {
        }

        void expand()
        {
            for (int i = 0; i < datas.size(); i++)
            {
                gcCategoryExpListView.expandGroup(i);
            }
        }

        @Override
        public GoodDataEntity getChild(int groupPosition, int chinldPosition)
        {
            return datas.get(groupPosition).getGood_data().get(chinldPosition);
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
            if (convert == null)
            {
                convert = new GoodsCategoryCombineView2Test(getActivity());
            }
            ((GoodsCategoryCombineView2Test) convert).setTitleForText(getChild(groupPosition, childPosition), StickyHeaderFragment2.this);
            return convert;
        }

        @Override
        public int getChildrenCount(int groupPosition)
        {
            return datas.get(groupPosition).getGood_data().size();
        }

        @Override
        public Category getGroup(int groupPosition)
        {
            return datas.get(groupPosition);
        }

        @Override
        public int getGroupCount()
        {
            return datas.size();
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
            if (x == 0)
            {
                int[] loc = new int[2];
                gcCategoryExpListView.getLocationInWindow(loc);
                x = loc[1];
            }
            if (convertView == null)
            {
                convertView = LayoutInflater.from(getActivity()).inflate(R.layout.item_category_group,
                        parent, false);
                GoodsClassBean goodsClassBean = new GoodsClassBean();
                goodsClassBean.tv = (TextView) convertView
                        .findViewById(R.id.gc_exp_group_name);
                convertView.setTag(goodsClassBean);
            }

            GoodsClassBean gcb = (GoodsClassBean) convertView.getTag();
            gcb.tv.setText(getGroup(groupPosition).getClass_name());
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

        class GoodsClassBean
        {
            TextView tv;
        }

    }

    public void add2Shopcart(View triggerView, GoodsCategoryCombineView2Test gccv)
    {
        String memberId = ConfigUtil.readString (getActivity (),
                MyConstant.KEY_MEMBERID, "");
        if (Tools.isEmpty(memberId))
        {
            Tools.dialog(getActivity(),
                    Tools.getStr(getActivity(), R.string.REQUESTLOGIN), true,
                    new onButtonClick()
                    {

                        @Override
                        public void buttonClick()
                        {
                            getActivity().startActivity(new Intent(getActivity(), LoginActivity.class));
                            AnimUtil.animToSlide(getActivity());
                        }
                    });
        } else
        {
            RequestParams requestParams = new RequestParams();
            requestParams.addQueryStringParameter("goods_id", gccv.getGoodId());
            requestParams.addQueryStringParameter("quantity", gccv
                    .getOrderCountTV().getText().toString().trim());
            requestParams.addQueryStringParameter("member_id", memberId);
            ImageView aniImg = new ImageView(getActivity());
            aniImg.setBackgroundDrawable(gccv.getGoodImgIV().getDrawable());
            Tools.addShopCard((BaseAcitivity) getActivity(),
                    "addshopcar", triggerView, mainActivity.getShopcardLayout(), aniImg, requestParams, true);
        }

    }

    @Override
    public void onAttach(Activity activity)
    {
        super.onAttach(activity);
        mainActivity = (MainActivity) activity;
    }

/*
    String json = "{\n" +
            "    \"code\": \"200\",\n" +
            "    \"datas\": [\n" +
            "        {\n" +
            "            \"class_name\": \"调味品\",\n" +
            "            \"good_data\": [\n" +
            "                {\n" +
            "                    \"goods_id\": \"45557\",\n" +
            "                    \"goods_name\": \"明泉宝三年老陈醋\",\n" +
            "                    \"goods_price\": \"10.50\",\n" +
            "                    \"goods_pic\": \"http://applingbushequ.nat123.net/lingbushequ/src/lso2o//data/upload/shop/goods/c9b814529722de7656b86f42718986ee.jpg\"\n" +
            "                },\n" +
            "                {\n" +
            "                    \"goods_id\": \"45570\",\n" +
            "                    \"goods_name\": \"海天上等蚝油700g\",\n" +
            "                    \"goods_price\": \"6.50\",\n" +
            "                    \"goods_pic\": \"http://applingbushequ.nat123.net/lingbushequ/src/lso2o//data/upload/shop/goods/b6944c694a49b3b79a6534ea4a8f6cb2.jpg\"\n" +
            "                },\n" +
            "                {\n" +
            "                    \"goods_id\": \"45577\",\n" +
            "                    \"goods_name\": \"德馨斋原汁酱油300ml\",\n" +
            "                    \"goods_price\": \"2.00\",\n" +
            "                    \"goods_pic\": \"http://applingbushequ.nat123.net/lingbushequ/src/lso2o//data/upload/shop/goods/2762bdbb5695208e62b6f27815a7092b.jpg\"\n" +
            "                }\n" +
            "            ]\n" +
            "        },\n" +
            "        {\n" +
            "            \"class_name\": \"牛羊肉\",\n" +
            "            \"good_data\": [\n" +
            "                {\n" +
            "                    \"goods_id\": \"45665\",\n" +
            "                    \"goods_name\": \"羔羊肉片\",\n" +
            "                    \"goods_price\": \"38.00\",\n" +
            "                    \"goods_pic\": \"http://applingbushequ.nat123.net/lingbushequ/src/lso2o//data/upload/shop/goods/8735d8f2cf826066c5af1894544f7211.jpg\"\n" +
            "                },\n" +
            "                {\n" +
            "                    \"goods_id\": \"45668\",\n" +
            "                    \"goods_name\": \"羊肉片\",\n" +
            "                    \"goods_price\": \"42.00\",\n" +
            "                    \"goods_pic\": \"http://applingbushequ.nat123.net/lingbushequ/src/lso2o//data/upload/shop/goods/b804a3724bf44f25c5849426d847ba37.jpg\"\n" +
            "                },\n" +
            "                {\n" +
            "                    \"goods_id\": \"45669\",\n" +
            "                    \"goods_name\": \"肥牛片\",\n" +
            "                    \"goods_price\": \"32.00\",\n" +
            "                    \"goods_pic\": \"http://applingbushequ.nat123.net/lingbushequ/src/lso2o//data/upload/shop/goods/654356b54e3e113b1cbb7e2dad9ccc4c.jpg\"\n" +
            "                }\n" +
            "            ]\n" +
            "        },\n" +
            "        {\n" +
            "            \"class_name\": \"坚果炒货\",\n" +
            "            \"good_data\": [\n" +
            "                {\n" +
            "                    \"goods_id\": \"45471\",\n" +
            "                    \"goods_name\": \"沙土喝茶瓜子260g\",\n" +
            "                    \"goods_price\": \"9.00\",\n" +
            "                    \"goods_pic\": \"http://applingbushequ.nat123.net/lingbushequ/src/lso2o//data/upload/shop/goods/27a73d680dc1d7cf6ad59893bacfd96a.jpg\"\n" +
            "                },\n" +
            "                {\n" +
            "                    \"goods_id\": \"45472\",\n" +
            "                    \"goods_name\": \"沙土吊炉花生\",\n" +
            "                    \"goods_price\": \"6.50\",\n" +
            "                    \"goods_pic\": \"http://applingbushequ.nat123.net/lingbushequ/src/lso2o//data/upload/shop/goods/3d1c3880d8e322ef11d7a6cc7a761618.jpg\"\n" +
            "                },\n" +
            "                {\n" +
            "                    \"goods_id\": \"45473\",\n" +
            "                    \"goods_name\": \"沙土白瓜子200g\",\n" +
            "                    \"goods_price\": \"8.00\",\n" +
            "                    \"goods_pic\": \"http://applingbushequ.nat123.net/lingbushequ/src/lso2o//data/upload/shop/goods/cd832868358f5da3c79f61ac48fe3152.jpg\"\n" +
            "                },\n" +
            "                {\n" +
            "                    \"goods_id\": \"44266\",\n" +
            "                    \"goods_name\": \"余味轩核桃仁80g\",\n" +
            "                    \"goods_price\": \"11.50\",\n" +
            "                    \"goods_pic\": \"http://applingbushequ.nat123.net/lingbushequ/src/lso2o//data/upload/shop/goods/d2f982b89ba37d75e6805a5460cc2461.jpg\"\n" +
            "                }\n" +
            "            ]\n" +
            "        },\n" +
            "        {\n" +
            "            \"class_name\": \"铁观音\",\n" +
            "            \"good_data\": [\n" +
            "                {\n" +
            "                    \"goods_id\": \"45485\",\n" +
            "                    \"goods_name\": \"名士多铁观音\",\n" +
            "                    \"goods_price\": \"22.00\",\n" +
            "                    \"goods_pic\": \"http://applingbushequ.nat123.net/lingbushequ/src/lso2o//data/upload/shop/goods/f719035a25a5fa5e17d6a5d36c40df91.jpg\"\n" +
            "                }\n" +
            "            ]\n" +
            "        },\n" +
            "        {\n" +
            "            \"class_name\": \"衣物清洁\",\n" +
            "            \"good_data\": [\n" +
            "                {\n" +
            "                    \"goods_id\": \"44785\",\n" +
            "                    \"goods_name\": \"1.6kg立白健康柔护皂粉\",\n" +
            "                    \"goods_price\": \"24.00\",\n" +
            "                    \"goods_pic\": \"http://applingbushequ.nat123.net/lingbushequ/src/lso2o//data/upload/shop/goods/ff56436db621fa9f9ccf8b9ca4fae688.jpg\"\n" +
            "                },\n" +
            "                {\n" +
            "                    \"goods_id\": \"44673\",\n" +
            "                    \"goods_name\": \"碧浪洗衣粉300g\",\n" +
            "                    \"goods_price\": \"3.50\",\n" +
            "                    \"goods_pic\": \"http://applingbushequ.nat123.net/lingbushequ/src/lso2o//data/upload/shop/goods/011b2aa79e6944978d06b83a0b36a19b.jpg\"\n" +
            "                },\n" +
            "                {\n" +
            "                    \"goods_id\": \"44605\",\n" +
            "                    \"goods_name\": \"中华健齿白牙膏90g\",\n" +
            "                    \"goods_price\": \"4.00\",\n" +
            "                    \"goods_pic\": \"http://applingbushequ.nat123.net/lingbushequ/src/lso2o//data/upload/shop/goods/afe7eb0f43645ab72dbac25c393bb2f5.jpg\"\n" +
            "                }\n" +
            "            ]\n" +
            "        },\n" +
            "        {\n" +
            "            \"class_name\": \"饮料\",\n" +
            "            \"good_data\": [\n" +
            "                {\n" +
            "                    \"goods_id\": \"1591\",\n" +
            "                    \"goods_name\": \"醒目苹果330ml\",\n" +
            "                    \"goods_price\": \"13.00\",\n" +
            "                    \"goods_pic\": \"http://applingbushequ.nat123.net/lingbushequ/src/lso2o//data/upload/shop/goods/ea2ced1d6dc897975d48c4cbc22b46bc.jpg\"\n" +
            "                },\n" +
            "                {\n" +
            "                    \"goods_id\": \"1575\",\n" +
            "                    \"goods_name\": \"汇源100%葡萄汁1L\",\n" +
            "                    \"goods_price\": \"14.00\",\n" +
            "                    \"goods_pic\": \"http://applingbushequ.nat123.net/lingbushequ/src/lso2o//data/upload/shop/goods/ac49e8b58ebc8da82dd023d4ce130a71.jpg\"\n" +
            "                },\n" +
            "                {\n" +
            "                    \"goods_id\": \"45312\",\n" +
            "                    \"goods_name\": \"露露果仁核桃露\",\n" +
            "                    \"goods_price\": \"3.50\",\n" +
            "                    \"goods_pic\": \"http://applingbushequ.nat123.net/lingbushequ/src/lso2o//data/upload/shop/goods/29b79d082eda7d424d36fcb23e615614.jpg\"\n" +
            "                }\n" +
            "            ]\n" +
            "        },\n" +
            "        {\n" +
            "            \"class_name\": \"方便食品\",\n" +
            "            \"good_data\": [\n" +
            "                {\n" +
            "                    \"goods_id\": \"45341\",\n" +
            "                    \"goods_name\": \"康师傅珍品袋面（红烧牛肉）\",\n" +
            "                    \"goods_price\": \"2.50\",\n" +
            "                    \"goods_pic\": \"http://applingbushequ.nat123.net/lingbushequ/src/lso2o//data/upload/shop/goods/409a6f467e112e52d337247a54844ed7.jpg\"\n" +
            "                },\n" +
            "                {\n" +
            "                    \"goods_id\": \"45396\",\n" +
            "                    \"goods_name\": \"康师傅桶面香菇炖鸡\",\n" +
            "                    \"goods_price\": \"4.00\",\n" +
            "                    \"goods_pic\": \"http://applingbushequ.nat123.net/lingbushequ/src/lso2o//data/upload/shop/goods/49ebde314aa06a365475249a0e5e0180.jpg\"\n" +
            "                },\n" +
            "                {\n" +
            "                    \"goods_id\": \"45397\",\n" +
            "                    \"goods_name\": \"康师傅老坛泡椒\",\n" +
            "                    \"goods_price\": \"4.00\",\n" +
            "                    \"goods_pic\": \"http://applingbushequ.nat123.net/lingbushequ/src/lso2o//data/upload/shop/goods/2b7dda9eea23fa2162996f617e326f84.jpg\"\n" +
            "                }\n" +
            "            ]\n" +
            "        }\n" +
            //            "        },\n" +
            //            "        {\n" +
            //            "            \"class_name\": \"龙井\",\n" +
            //            "            \"good_data\": []\n" +
            //            "        }\n" +
            "    ]\n" +
            "}";
            */
}
