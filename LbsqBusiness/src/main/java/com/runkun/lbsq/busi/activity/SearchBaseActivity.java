package com.runkun.lbsq.busi.activity;

import android.os.Bundle;
import android.os.Handler;

import com.runkun.lbsq.busi.R;
import com.runkun.lbsq.busi.util.AnimUtil;
import com.runkun.lbsq.busi.util.MyConstant;
import com.runkun.lbsq.busi.util.Tools;

import butterknife.ButterKnife;
import butterknife.InjectView;
import butterknife.OnClick;
import feifei.project.util.ConfigUtil;
import feifei.project.view.ClearEditText;

public abstract class SearchBaseActivity<T> extends FooterListActivity<T>
{
    @InjectView(R.id.edit)
    ClearEditText edit;

    protected String key;
    protected String hint;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search);
        ButterKnife.inject(this);
        tint();
        init();
        initKey();
        emptyTextView.setClickable(false);
        edit.setHint(hint);

        //// TODO: 2015/8/31
        rp.addQueryStringParameter("store_id", ConfigUtil.readString (activity, MyConstant.KEY_STOREID, ""));
        // rp.addQueryStringParameter("store_id","83");
        new Handler().postDelayed(new Runnable()
        {

            @Override
            public void run()
            {
                Tools.keyboardShow(edit);
            }
        }, 600);

    }

    String tempString;

    @OnClick(R.id.search)
    public void search()
    {
        String s = edit.getText().toString();
        if (Tools.isEmpty(s))
        {
            Tools.toast(activity, "请输入内容吧");
            AnimUtil.animShakeText(edit);
        } else
        {
            if (!s.equals(tempString))
            {
                data.clear();
                adapter.notifyDataSetChanged();
                emptyTextView.setText("正在加载中……");
                pageNum = 1;
                tempString = s;
                // count++;
                // ConfigUtil.write(activity, "count", count);
                // editor.putString("history" + count, s);
                // editor.apply();


                rp.addQueryStringParameter(key, s);
                loadData();
            }
        }
    }

    //初始化rp，cls，key
    protected abstract void initKey();

}
