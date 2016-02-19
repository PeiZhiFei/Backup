package com.runkun.lbsq.busi.activity;

import android.content.Intent;

import com.runkun.lbsq.busi.adapter.GoodListAdapterTest;
import com.runkun.lbsq.busi.bean.GoodMore;
import com.runkun.lbsq.busi.util.AnimUtil;
import com.runkun.lbsq.busi.util.MyConstant;

public class SearchGoodActivity extends SearchBaseActivity<GoodMore>
{

    @Override
    protected void initKey()
    {
        key="goods_name";
        hint="请输入商品名";
    }


    @Override
    protected void itemClick(int position)
    {
        Intent intent = new Intent(activity, ResultActivity.class);
        intent.putExtra ("good_id", data.get (position).getGoods_id ());
        //L.l ("good_id===" + data.get (position).getGoods_id ());
        intent.putExtra ("type", "no_have");
        startActivity(intent);
        AnimUtil.animTo(activity);
    }

    @Override
    protected void initType()
    {
        adapter = new GoodListAdapterTest(activity, data);
        url = MyConstant.CSEARCHGOOD;
        cls = GoodMore.class;
    }
}
