package com.runkun.lbsq.busi.fragment;

import android.os.Bundle;

import com.runkun.lbsq.busi.R;
import com.runkun.lbsq.busi.adapter.JudgeAdapter;
import com.runkun.lbsq.busi.bean.Judge;
import com.runkun.lbsq.busi.util.MyConstant;

import feifei.project.util.ConfigUtil;

public class JudgeFragment extends ListBaseFragment<Judge>
{
    private int type;

    @Override
    public void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        Bundle bundle = getArguments();
        if (bundle != null)
        {
            type = bundle.getInt("type");
        }
    }

    public static JudgeFragment newInstance(int type)
    {
        JudgeFragment myBuyBasefragment = new JudgeFragment();
        Bundle args = new Bundle();
        args.putInt("type", type);
        myBuyBasefragment.setArguments(args);
        return myBuyBasefragment;
    }

    @Override
    protected void itemClick(int position)
    {

    }

    protected void initType()
    {
        adapter = new JudgeAdapter(fragment, data, R.layout.item_judge, type);
        String state = "";
        url = MyConstant.CGETCOMMENT;
        cls = Judge.class;
        switch (type)
        {
            case 1:
                state = "7";
                break;
            case 2:
                state = "1";
                break;
            case 3:
                state = "3";
                break;
        }
        rp.addQueryStringParameter("store_id", ConfigUtil.readString (fragment, MyConstant.KEY_STOREID, ""));
        rp.addQueryStringParameter("state", state);
    }


}
