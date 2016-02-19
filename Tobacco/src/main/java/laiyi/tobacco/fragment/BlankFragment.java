package laiyi.tobacco.fragment;


import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import butterknife.ButterKnife;
import butterknife.InjectView;
import butterknife.OnClick;
import feifei.library.util.AnimUtil;
import feifei.library.view.percent.PercentLinearLayout;
import laiyi.tobacco.R;
import laiyi.tobacco.activity.ListViewActivity;
import laiyi.tobacco.activity.MainActivity;
import laiyi.tobacco.activity.MapManageActivity;
import laiyi.tobacco.activity.MyTaskActivity;

public class BlankFragment extends BaseFragment
{
    private static final String ARG_PARAM1 = "param1";
    @InjectView(R.id.layout1)
    PercentLinearLayout layout1;
    @InjectView(R.id.layout2)
    PercentLinearLayout layout2;
    @InjectView(R.id.layout3)
    PercentLinearLayout layout3;
    @InjectView(R.id.layout4)
    PercentLinearLayout layout4;
    @InjectView(R.id.layout5)
    PercentLinearLayout layout5;
    @InjectView(R.id.layout6)
    PercentLinearLayout layout6;
    @InjectView(R.id.layout7)
    PercentLinearLayout layout7;
    @InjectView(R.id.layout8)
    public PercentLinearLayout layout8;
    private String mParam1;

    MainActivity mainActivity;

    @Override
    public void onAttach (Activity activity)
    {
        super.onAttach (activity);
        mainActivity = (MainActivity) activity;
    }

    public static BlankFragment newInstance (String param1)
    {
        BlankFragment fragment = new BlankFragment ();
        Bundle args = new Bundle ();
        args.putString (ARG_PARAM1, param1);
        fragment.setArguments (args);
        return fragment;
    }

    public BlankFragment ()
    {
    }

    @Override
    public void onCreate (Bundle savedInstanceState)
    {
        super.onCreate (savedInstanceState);
        if ( getArguments () != null )
        {
            mParam1 = getArguments ().getString (ARG_PARAM1);
        }
    }

    @Override
    public View onCreateView (LayoutInflater inflater, ViewGroup container,
                              Bundle savedInstanceState)
    {
        View view = inflater.inflate (R.layout.fragment_blank, container, false);
        ButterKnife.inject (this, view);
        return view;
    }


    @Override
    public void onDestroyView ()
    {
        super.onDestroyView ();
        ButterKnife.reset (this);
    }

    @OnClick({R.id.layout1, R.id.layout2})
    public void click ()
    {
        startActivity (new Intent (fragment, ListViewActivity.class));
        AnimUtil.animTo ((Activity) fragment);
    }

    @OnClick(R.id.layout8)
    public void MyTask ()
    {
        startActivity (new Intent (fragment, MyTaskActivity.class));
        AnimUtil.animTo ((Activity) fragment);
    }
    @OnClick(R.id.layout7)
    public void mapManage ()
    {
        startActivity (new Intent (fragment, MapManageActivity.class));
        AnimUtil.animTo ((Activity) fragment);
    }
}
