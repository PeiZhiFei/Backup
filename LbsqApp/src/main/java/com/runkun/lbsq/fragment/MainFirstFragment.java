package com.runkun.lbsq.fragment;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.LinearLayout;
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
import com.runkun.lbsq.activity.GoodMoreActivity;
import com.runkun.lbsq.view.GoodCombineView;
import com.runkun.lbsq.utils.AnimUtil;
import com.runkun.lbsq.utils.MyConstant;
import com.runkun.lbsq.utils.Tools;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

/*
    今日特价等4个模块共用类
 */
public class MainFirstFragment extends MainFirstBaseFragment implements
        OnClickListener
{

    private int type;
    protected TextView nodataText;

    protected View tablelayout;

    @ViewInject(R.id.main_type_right)
    private TextView dishplayAll;

    @ViewInject(R.id.good1)
    private GoodCombineView good1;

    @ViewInject(R.id.good2)
    private GoodCombineView good2;

    @ViewInject(R.id.good3)
    private GoodCombineView good3;

    @ViewInject(R.id.good4)
    private GoodCombineView good4;

    @ViewInject(R.id.good5)
    private GoodCombineView good5;

    @ViewInject(R.id.good6)
    private GoodCombineView good6;

    @ViewInject(R.id.main_type_left)
    private TextView siol;
    @ViewInject(R.id.nodata)
    private LinearLayout nodata;

    GoodCombineView[] arrayOfGoodCombineView;

    private String[] classname = new String[]{MyConstant.CLASSNAMETEJIA,
            MyConstant.CLASSNAMESHIPIN, MyConstant.CLASSNAMEJIUSHUI,
            MyConstant.CLASSNAMELIANGYOU};
    private String[] classid = new String[]{MyConstant.CLASSIDTEJIA,
            MyConstant.CLASSIDSHIPIN, MyConstant.CLASSIDJIUSHUI,
            MyConstant.CLASSIDLIANGYOU};
    private String[] classidsub = new String[]{MyConstant.CLASSIDTEJIA11,
            MyConstant.CLASSIDSHIPIN11, MyConstant.CLASSIDJIUSHUI11,
            MyConstant.CLASSIDLIANGYOU11};


    @Override
    public void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        type = getArguments().getInt("type");
    }

    public static MainFirstFragment newInstance(int type)
    {
        MainFirstFragment mainFirstBaseFragment2 = new MainFirstFragment();
        Bundle bundle = new Bundle();
        bundle.putInt("type", type);
        mainFirstBaseFragment2.setArguments(bundle);
        return mainFirstBaseFragment2;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState)
    {
        super.onCreateView(inflater, container, savedInstanceState);
        View localView = inflater.inflate(R.layout.fragment_tejia,
                container, false);
        ViewUtils.inject(this, localView);
        tablelayout = localView.findViewById(R.id.goodtype4);
        nodataText = (TextView) localView.findViewById(R.id.nodataText);
        arrayOfGoodCombineView = new GoodCombineView[6];
        arrayOfGoodCombineView[0] = good1;
        arrayOfGoodCombineView[1] = good2;
        arrayOfGoodCombineView[2] = good3;
        arrayOfGoodCombineView[3] = good4;
        arrayOfGoodCombineView[4] = good5;
        arrayOfGoodCombineView[5] = good6;
        siol.setText(classname[type]);
        siol.setOnClickListener(this);
        good1.setOnClickListener(this);
        good2.setOnClickListener(this);
        good3.setOnClickListener(this);
        good4.setOnClickListener(this);
        good5.setOnClickListener(this);
        good6.setOnClickListener(this);
        dishplayAll.setOnClickListener(this);
        reloadData();
        return localView;
    }


    protected void reqMain(final GoodCombineView[] viewAll, final View nodata,
                           RequestParams rp, final TextView dishplayAll,
                           final TextView nodataText, final boolean tejia)
    {
        HttpUtils http = new HttpUtils();
        http.send(HttpMethod.POST, MyConstant.API_BASE_URL + "goodslist", rp,
                new RequestCallBack<String>()
                {

                    @Override
                    public void onSuccess(ResponseInfo<String> responseInfo)
                    {
                        String result = responseInfo.result;
                        try
                        {
                            JSONObject data = new JSONObject(result);
                            JSONArray all = data.getJSONArray("datas");
                            int len1 = all.length();
                            int len2 = viewAll.length;
                            if (len1 == 0)
                            {
                                nodataText.setText(Tools.getStr(mainActivity,
                                        R.string.FIRSTNOGOODS));
                                dishplayAll.setEnabled(false);
                                dishplayAll.setTextColor(Color.LTGRAY);
                                tablelayout.setVisibility(View.GONE);
                            } else
                            {
                                JSONObject row;
                                for (int x = 0; x < len2; x++)
                                {
                                    row = all.getJSONObject(x);
                                    viewAll[x].setTitleForText(tejia,
                                            row.getString("goods_name"),
                                            row.getString("tjprice"),
                                            row.getString("goods_price"),
                                            row.getString("goods_pic"),
                                            row.getString("goods_id"),
                                            row.getString("store_id"));
                                    nodata.setVisibility(View.GONE);
                                    viewAll[x].setVisibility(View.VISIBLE);
                                }
                            }
                        } catch (JSONException e)
                        {
                            e.printStackTrace();
                            nodataText.setText(Tools.getStr(mainActivity,
                                    R.string.JSONERROR));
                        }

                    }

                    @Override
                    public void onFailure(HttpException error, String msg)
                    {
                        nodataText.setText(Tools.getStr(mainActivity,
                                R.string.FIRSTNOGOODS));
                        Tools.toast(mainActivity, Tools.getStr(mainActivity,
                                R.string.NETWORKERROR));
                    }
                });
    }

    @Override
    public void onClick(View v)
    {
        switch (v.getId())
        {
            case R.id.main_type_right:
                Intent intent = new Intent(getActivity(), GoodMoreActivity.class);
                intent.putExtra("store_id", storeId);
                intent.putExtra("class_id", classidsub[type]);
                intent.putExtra("category_id", classid[type]);
                intent.putExtra("class_name", classname[type]);
                getActivity().startActivity(intent);
                AnimUtil.animToSlide(getActivity());
                break;
        }
    }

    public void reloadData()
    {
        if (Tools.isNetworkAvailable(mainActivity))
        {
            RequestParams rp = new RequestParams();
            rp.addQueryStringParameter("store_id", storeId);
            // rp.addQueryStringParameter("class_id",
            // MyConstant.CLASSIDLIANGYOU);
            if (type == 0)
            {
                rp.addQueryStringParameter("is_tejia", "1");
            } else
            {
                rp.addQueryStringParameter("class_id", classid[type]);
            }
            reqMain(arrayOfGoodCombineView, nodata, rp, dishplayAll,
                    nodataText, type == 0 ? true : false);
        } else
        {
            // Tools.toast(mainActivity,
            // Tools.getStr(mainActivity, R.string.NETWORKERROR));
            nodataText.setText(Tools
                    .getStr(mainActivity, R.string.NETWORKERROR));

        }
    }
}
