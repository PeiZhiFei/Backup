package com.runkun.lbsq.busi.activity;

import android.os.Bundle;
import android.widget.EditText;

import com.runkun.lbsq.busi.R;
import com.runkun.lbsq.busi.util.AnimUtil;
import com.runkun.lbsq.busi.util.MyConstant;
import com.runkun.lbsq.busi.util.Tools;

import org.json.JSONException;
import org.json.JSONObject;

import butterknife.ButterKnife;
import butterknife.InjectView;
import butterknife.OnClick;
import feifei.project.util.ConfigUtil;
import feifei.project.util.L;

public class FeedBackActivity extends NetActivity
{

    @InjectView(R.id.et_content)
    EditText etContent;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_feed_back);
        ButterKnife.inject(this);
        tint();
        initActionbar();
        setTitles("意见反馈");
        dialogInit();
        url = MyConstant.CFEEDBACK;
        rp.addQueryStringParameter("store_id", ConfigUtil.readString (activity, MyConstant.KEY_STOREID, ""));
        rp.addQueryStringParameter("store_name", ConfigUtil.readString(activity, MyConstant.KEY_STORENAME, ""));
    }


    @OnClick(R.id.submit)
    public void submit()
    {
        String content = etContent.getText().toString();
        if (Tools.isEmpty(content))
        {
            Tools.toast(activity, "请给我们一些宝贵的建议吧");
            AnimUtil.animShakeText(etContent);
        } else
        {
            rp.addQueryStringParameter("content", content);
            L.l (ConfigUtil.readString (activity, MyConstant.KEY_STOREID, ""));
            L.l(ConfigUtil.readString(activity, MyConstant.KEY_STORENAME, ""));
            L.l(content);
            loadData();
        }
    }


    @Override
    protected void getData(JSONObject jsonResult) throws JSONException
    {
        String result = jsonResult.getString("datas");
        if (result.equals("is_ok"))
        {
            Tools.toast(activity, "提交成功");
            AnimUtil.animBackFinish(activity);
        } else
        {
            Tools.toast(activity, "提交失败");
        }

    }
}
