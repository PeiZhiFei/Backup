package com.runkun.lbsq.fragment;

import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.ImageView;

import com.runkun.lbsq.R;
import com.runkun.lbsq.activity.BuyNowActivity;
import com.runkun.lbsq.activity.LoginActivity;
import com.runkun.lbsq.utils.AnimUtil;
import com.runkun.lbsq.utils.MyConstant;
import com.runkun.lbsq.utils.Tools;
import com.runkun.lbsq.utils.XUtilsImageLoader;
import com.runkun.lbsq.view.wheel.AbstractWheelTextAdapter;
import com.runkun.lbsq.view.wheel.ArrayWheelAdapter;
import com.runkun.lbsq.view.wheel.OnWheelChangedListener;
import com.runkun.lbsq.view.wheel.OnWheelScrollListener;
import com.runkun.lbsq.view.wheel.WheelView;

public class ProductsFragment extends BaseFragment implements View.OnClickListener
{
    protected Button buyNow;
    protected ImageView duckTop;
    protected SharedPreferences sp;
    //    protected String con_address;
    protected String memberId;
    protected Intent intent;

    //1是鸭脖 2是龙虾
    private int type = 1;
    protected String[][] cities;
    protected View view;
    protected boolean scrolling = false;

    protected String goodId;
    protected String goodPrice;
    protected String goodsTempName;
    protected String goodsTempName2;
    protected String by;

    public static ProductsFragment newInstance(int type)
    {
        ProductsFragment productsFragment = new ProductsFragment();
        Bundle args = new Bundle();
        args.putInt("type", type);
        productsFragment.setArguments(args);
        return productsFragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        type = getArguments().getInt("type");
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState)
    {
        super.onCreateView(inflater, container, savedInstanceState);

        sp = getActivity().getSharedPreferences(MyConstant.FILE_NAME,
                Context.MODE_PRIVATE);

        dialogInit();

        view = inflater.inflate(type == 1 ? R.layout.fragment_product1 : R.layout.fragment_product2, container, false);
        buyNow = (Button) view.findViewById(R.id.buy_now);
        duckTop = (ImageView) view.findViewById(R.id.duck_top);
        buyNow.setOnClickListener(this);

        intent = new Intent(getActivity(), BuyNowActivity.class);
        intent.putExtra("storeId", "56");
        intent.putExtra("storeName", "特色专卖店");
        intent.putExtra("unit", "克");
        intent.putExtra("step", "1");
        intent.putExtra("min", "1");
        intent.putExtra("max", "99");
        intent.putExtra("quantity", "1");


        if (getActivity() != null)
        {
            //这里计算了屏幕宽度
            XUtilsImageLoader xui = new XUtilsImageLoader(getActivity());
            WindowManager wm = getActivity().getWindowManager();
            int width = wm.getDefaultDisplay().getWidth();
            xui.display(duckTop, sp.getString(type == 1 ? "duckSubUrl" : "lobsterSubUrl", ""), width);
        }

        return view;
    }

    @Override
    public void onResume()
    {
        super.onResume();
        memberId = sp.getString(MyConstant.KEY_MEMBERID, null);
    }

    @Override
    public void onClick(View v)
    {
        switch (v.getId())
        {
            case R.id.buy_now:
                if (Tools.isEmpty(memberId))
                {
                    startActivity(new Intent(getActivity(), LoginActivity.class));
                    AnimUtil.animToSlide(getActivity());
                } else
                {
                    initDaloag();
                }
                break;
            case R.id.finish:
                wheelDialog.dismiss();
                intent.putExtra("goodId", goodId);
                intent.putExtra("by", by);
                intent.putExtra("goodName", (type == 1 ? "鸭脖" : "小龙虾") + goodsTempName + goodsTempName2);
                intent.putExtra("goodPrice", goodPrice);
                startActivity(intent);
                break;
        }
    }

    Dialog wheelDialog;

    public void initDaloag()
    {
        wheelDialog = new Dialog(getActivity(), R.style.dialog_bottom_style);
        wheelDialog.setCanceledOnTouchOutside(true);
        WindowManager wm = getActivity().getWindowManager();
        int width = wm.getDefaultDisplay().getWidth();
        wheelDialog.setContentView(
                LayoutInflater.from(getActivity()).inflate(
                        R.layout.title_wheel, null), new ViewGroup.LayoutParams(width,
                        ViewGroup.LayoutParams.WRAP_CONTENT));
        final WheelView parent = (WheelView) wheelDialog.getWindow().findViewById(
                R.id.parent);
        final WheelView child = (WheelView) wheelDialog.getWindow().findViewById(
                R.id.child);
        Button fin = (Button) wheelDialog.getWindow().findViewById(R.id.finish);
        fin.setOnClickListener(this);

        parent.setVisibleItems(3);
        CountryAdapter parentAdapter = new CountryAdapter(getActivity());
        parentAdapter.setTextSize(14);
        parentAdapter.setTextColor(R.color.main_red_color);
        parent.setViewAdapter(parentAdapter);

        if (type == 1)
        {
            cities = new String[][]{
                    new String[]{"大份(￥29.5/450克)", "小份(￥15.0/230克)"},
                    new String[]{"大份(￥29.5/450克)", "小份(￥15.0/230克)"},
                    new String[]{"大份(￥29.5/450克)", "小份(￥15.0/230克)"}
            };
        } else if (type == 2)
        {
            cities = new String[][]{
                    new String[]{"小份(￥49.0/500克)", "中份(￥72.0/750克)", "大份(￥140.0/1500克)"},
                    new String[]{"小份(￥49.0/500克)", "中份(￥72.0/750克)", "大份(￥140.0/1500克)"},
                    new String[]{"小份(￥49.0/500克)", "中份(￥72.0/750克)", "大份(￥140.0/1500克)"}
            };
        }

        child.setVisibleItems(5);
        //这2个监听器首次都会执行的
        parent.addChangingListener(new OnWheelChangedListener()
        {
            @Override
            public void onChanged(WheelView wheel, int oldValue, int newValue)
            {
                updateCities(child, cities, newValue);
                switch (newValue)
                {
                    case 0:
                        goodId = type == 1 ? "1" : "4";
                        goodsTempName = "/微辣";
                        break;
                    case 1:
                        goodId = type == 1 ? "2" : "5";
                        goodsTempName = "/中辣";
                        break;
                    case 2:
                        goodId = type == 1 ? "3" : "6";
                        goodsTempName = "/麻辣";
                        break;
                }
            }
        });

        parent.addScrollingListener(new OnWheelScrollListener()
        {
            @Override
            public void onScrollingStarted(WheelView wheel)
            {
                scrolling = true;
            }

            @Override
            public void onScrollingFinished(WheelView wheel)
            {
                scrolling = false;
                updateCities(child, cities, parent.getCurrentItem());

            }
        });
        child.addChangingListener(new OnWheelChangedListener()
        {

            @Override
            public void onChanged(WheelView wheel, int oldValue, int newValue)
            {
                switch (newValue)
                {
                    case 0:
                        //                        intent.putExtra ("goodPrice", type == 1 ? "29.5" : "49");
                        goodPrice = type == 1 ? "29.5" : "49";
                        goodsTempName2 = type == 1 ? "/大份" : "/小份";
                        //                        intent.putExtra ("goodName", goodsName + (type == 1 ? "/大份" : "/小份"));
                        //                        intent.putExtra ("by", type == 1 ? "450" : "500");
                        by = type == 1 ? "450" : "500";
                        break;
                    case 1:
                        //                        intent.putExtra ("goodPrice", type == 1 ? "15" : "72");
                        goodPrice = type == 1 ? "15" : "72";
                        goodsTempName2 = type == 1 ? "/小份" : "/中份";
                        //                        intent.putExtra ("goodName", goodsName + (type == 1 ? "/小份" : "/中份"));
                        //                        intent.putExtra ("by", type == 1 ? "230" : "750");
                        by = type == 1 ? "230" : "750";
                        break;
                    case 2:
                        //                        intent.putExtra ("goodPrice", "140");
                        goodPrice = "140";
                        goodsTempName2 = "/大份";
                        by = "1500";
                        //                        intent.putExtra ("goodName", goodsName + "/大份");
                        //                        intent.putExtra ("by", "1500");
                        break;
                }

            }
        });
        parent.setCurrentItem(1);
        Window dw = wheelDialog.getWindow();
        dw.setGravity(Gravity.BOTTOM);
        wheelDialog.show();
    }


    protected void updateCities(WheelView city, String cities[][], int index)
    {
        ArrayWheelAdapter<String> adapter = new ArrayWheelAdapter<String>(
                getActivity(), cities[index]);
        adapter.setTextSize(15);
        adapter.setTextColor(0xa039ac69);
        city.setViewAdapter(adapter);
        city.setCurrentItem(cities[index].length / 2);

    }


    protected class CountryAdapter extends AbstractWheelTextAdapter
    {
        private String countries[] = new String[]{"微辣", "中辣", "麻辣"};
        private int flags[] = new int[]{R.color.white, R.color.white,
                R.color.white};

        protected CountryAdapter(Context context)
        {
            super(context, R.layout.item_wheel, NO_RESOURCE);
            setItemTextResource(R.id.country_name);
        }

        @Override
        public View getItem(int index, View cachedView, ViewGroup parent)
        {
            View view = super.getItem(index, cachedView, parent);
            ImageView img = (ImageView) view.findViewById(R.id.flag);
            img.setImageResource(flags[index]);
            return view;
        }

        @Override
        public int getItemsCount()
        {
            return countries.length;
        }

        @Override
        protected CharSequence getItemText(int index)
        {
            return countries[index];
        }
    }
}
