package com.runkun.lbsq.activity;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ImageView;
import android.widget.TextView;

import com.lidroid.xutils.HttpUtils;
import com.lidroid.xutils.exception.HttpException;
import com.lidroid.xutils.http.RequestParams;
import com.lidroid.xutils.http.ResponseInfo;
import com.lidroid.xutils.http.callback.RequestCallBack;
import com.lidroid.xutils.http.client.HttpRequest.HttpMethod;
import com.runkun.lbsq.R;
import com.runkun.lbsq.bean.Shop;
import com.runkun.lbsq.fragment.FruitListFragment;
import com.runkun.lbsq.fragment.MainFirstStaticFragment;
import com.runkun.lbsq.fragment.MainFruiFragment;
import com.runkun.lbsq.fragment.MyFragment;
import com.runkun.lbsq.fragment.ShopCardFragment;
import com.runkun.lbsq.fragment.StickyHeaderFragment2;
import com.runkun.lbsq.utils.AnimUtil;
import com.runkun.lbsq.utils.MyConstant;
import com.runkun.lbsq.utils.Tools;
import com.runkun.lbsq.view.CouponView;

import org.json.JSONException;
import org.json.JSONObject;

import feifei.project.util.ConfigUtil;
import feifei.project.util.L;
import feifei.project.util.ToastUtil;

public class MainActivity extends BaseAcitivity implements
        OnClickListener
{

    private CouponView storeConponView;
    private MainFirstStaticFragment mainFragment;
//    private GoodsCategoryFragment goodsCategoryFragment;
    private StickyHeaderFragment2 goodsCategoryFragment;
    private FruitListFragment fruitListFragment;
    private ShopCardFragment shopCardFragment;
    private MyFragment myFragment;
    private MainFruiFragment mainFruiFragment;

    public static MainActivity mainActivity;

    private View firstLayout, categoryLayout, shopcardLayout, myLayout;
    private ImageView firstImage, categoryImage, shopcardImage, myImage;
    private TextView firstText, categoryText, shopcardText, myText;

    private FragmentManager fragmentManager;
    private int current = -1;
    private String memberId;
    private SharedPreferences sp;
    private SharedPreferences.Editor editor;
    private String storeId;
    private String shopName;
    private Shop shop;
    private int type = -1;
    private String onlineTip = "";
    private int textcolor;
    private int textcolornormal = Color.BLACK;

    private boolean first = true;
    private FragmentTransaction transaction;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        mainActivity = this;
        setContentView(R.layout.activity_main);
        textcolor = getResources().getColor(R.color.main_red_color);
        setSwipeBackEnable(false);
        initActionbar();
        tint();
        actionbar_right2.setVisibility(View.VISIBLE);
        actionbar_right2.setOnClickListener(new OnClickListener()
        {

            @Override
            public void onClick(View v)
            {
                search();
            }
        });
        sp = getSharedPreferences(MyConstant.FILE_NAME, Context.MODE_PRIVATE);
        editor = sp.edit();

        initData();
        initViews();
        setTabSelection(0);
        storeId = ConfigUtil.readString (getApplication (), "storeId", null);
        RequestParams onLine = new RequestParams();
        onLine.addQueryStringParameter("store_id", storeId);
        isOnline("isopenstore", onLine);

        String shopcount = sp.getString("shopcount", "");
        if (!("".equals(shopcount)))
        {
            Tools.refreshShopcartBadge(this, Integer.valueOf(shopcount));
        }
    }

    private void search()
    {
        Intent intent = new Intent(MainActivity.this, SearchGoodsActivity.class);
        intent.putExtra("type", "good");
        startActivity(intent);
        AnimUtil.animToSlide(activity);
    }

    @Override
    protected void onNewIntent(Intent intent)
    {
        super.onNewIntent(intent);
        String s = intent.getStringExtra("types");
        if (!Tools.isEmpty(s) && s.equals("shopcard"))
        {
            setTabSelection(2);
        }

    }

    private void initData()
    {
        Intent intent = getIntent();
        if (intent.getParcelableExtra("shop") != null)
        {
            shop = intent.getParcelableExtra("shop");
        } else
        {
            shop = new Shop();
            shop.setStore_id(sp.getString(Shop.SHOPID, ""));
            shop.setStore_name(sp.getString(Shop.SHOPNAME, ""));
            shop.setAddress(sp.getString(Shop.SHOPADDRESS, ""));
            shop.setAlisa(sp.getString(Shop.SHOPALISA, ""));
            shop.setDaliver_description(sp.getString(Shop.SHOPDALIVER, ""));
            shop.setTelephone(sp.getString(Shop.SHOPPHONE, ""));
            shop.setJfdk(sp.getString(Shop.SHOPJFDK, ""));
            shop.setFare(sp.getString(Shop.SHOPFARE, ""));
            shop.setClass_id(sp.getString(Shop.CLASSID, ""));
        }

        editor.putString(Shop.SHOPID, shop.getStore_id());
        editor.putString(Shop.SHOPNAME, shop.getStore_name());
        editor.putString(Shop.SHOPADDRESS, shop.getAddress());
        editor.putString(Shop.SHOPALISA, shop.getAlisa());
        editor.putString(Shop.SHOPDALIVER, shop.getDaliver_description());
        editor.putString(Shop.SHOPPHONE, shop.getTelephone());
        editor.putString(Shop.SHOPJFDK, shop.getJfdk());
        editor.putString(Shop.SHOPFARE, shop.getFare());
        editor.putString(Shop.CLASSID, shop.getClass_id());
        editor.commit();
        storeId = shop.getStore_id();
        shopName = shop.getStore_name();
    }

    public Shop getShop()
    {
        return shop;
    }

    private void hongbao()
    {
        if (Tools.isEmpty(memberId))
        {
            Tools.toast(activity, "登陆后可以领取优惠券");
        } else
        {
            RequestParams couponsRp = new RequestParams();
            couponsRp.addQueryStringParameter("store_id", storeId);
            couponsRp.addQueryStringParameter("member_id", memberId);
            reqStoreCoupons(this, MyConstant.API_BASE_URL_COUPON + "couponstore", couponsRp);
        }
    }

    public void reqStoreCoupons(final BaseAcitivity pram, String url,
                                RequestParams rp)
    {

        HttpUtils httpUtils = new HttpUtils();
        httpUtils.send(HttpMethod.POST, url, rp,
                new RequestCallBack<String>()
                {

                    @Override
                    public void onSuccess(ResponseInfo<String> responseInfo)
                    {
                        L.l (responseInfo.result);
                        String result = responseInfo.result;
                        try
                        {

                            JSONObject jsonResult = new JSONObject(
                                    result);
                            String code = jsonResult.getString("code");

                            if (code.equals("200"))
                            {
                                String youhuiquan = jsonResult.getString("isyouhuijuan");
                                String islingqu = jsonResult.getString("islingqu");
                                String isxiaofei = jsonResult.getString("isxiaofei");
                                if (youhuiquan.equals("1") && islingqu.equals("0") && isxiaofei.equals("0"))
                                {
                                    String datas = jsonResult.getString("datas");
                                    JSONObject jsonObject = new JSONObject(datas);
                                    String amount = jsonObject.getString("amount");
                                    //view出现
                                    storeConponView.setVisibility(View.VISIBLE);
                                    storeConponView.setTitleForText(memberId, storeId, amount);
                                } else
                                {
                                    storeConponView.setVisibility(View.GONE);
                                }
                            }

                        } catch (JSONException e)
                        {
                            e.printStackTrace();

                        }
                    }

                    @Override
                    public void onFailure(HttpException arg0, String arg1)
                    {
                    }
                });

    }


    @Override
    protected void onResume()
    {
        super.onResume();
        String x = getIntent().getStringExtra("type");
        if (x != null)
        {
            if (x.contains("login"))
            {
                setTabSelection(3);
            } else
            {
                if (x.equals("shopcard"))
                {
                    setTabSelection(2);
                }
            }
        }
        updateUser();
        hongbao();
        initData();

        if (current == 2)
        {
            shopCardFragment.refreshData();
        }
    }

    private void initViews()
    {
        firstLayout = findViewById(R.id.news_layout);
        categoryLayout = findViewById(R.id.guide_layout);
        shopcardLayout = findViewById(R.id.siservice_layout);
        myLayout = findViewById(R.id.job_layout);

        firstImage = (ImageView) findViewById(R.id.news_image);
        categoryImage = (ImageView) findViewById(R.id.guide_image);
        shopcardImage = (ImageView) findViewById(R.id.siservice_image);
        myImage = (ImageView) findViewById(R.id.job_image);

        firstText = (TextView) findViewById(R.id.news_text);
        categoryText = (TextView) findViewById(R.id.guide_text);
        shopcardText = (TextView) findViewById(R.id.siservice_text);
        myText = (TextView) findViewById(R.id.job_text);

        storeConponView = (CouponView) findViewById(R.id.store_coupons);

        firstLayout.setOnClickListener(this);
        categoryLayout.setOnClickListener(this);
        shopcardLayout.setOnClickListener(this);
        myLayout.setOnClickListener(this);

        fragmentManager = getSupportFragmentManager();

    }

    public View getShopcardLayout()
    {
        return shopcardImage;
    }

    @Override
    public void onClick(View v)
    {
        switch (v.getId())
        {
            case R.id.news_layout:
                if (MyConstant.SHOP_ONLINE)
                {
                    actionbar_title.setTextColor(Color.WHITE);
                } else
                {
                    actionbar_title.setTextColor(0x7f0200fd);
                }
                setTabSelection(0);
                break;
            case R.id.guide_layout:
                setTabSelection(1);
                break;
            case R.id.siservice_layout:
                setTabSelection(2);
                break;
            case R.id.job_layout:
                setTabSelection(3);
                break;
        }
    }

    public void setTabSelection(int index)
    {
        switch (index)
        {
            case 0:
                if (current == 0)
                {
                    return;
                }
                current = 0;
                preClick(true);
                firstImage.setImageResource(R.drawable.nav_shop_click);

                firstText.setTextColor(textcolor);
                if (shop.getClass_id().equals("18"))
                {
                    if (mainFragment == null)
                    {
                        mainFragment = new MainFirstStaticFragment();
                        transaction.add(R.id.content, mainFragment);
                    } else
                    {
                        transaction.show(mainFragment);
                    }
                } else
                {
                    if (mainFruiFragment == null)
                    {
                        mainFruiFragment = new MainFruiFragment();
                        transaction.add(R.id.content, mainFruiFragment);
                    } else
                    {
                        transaction.show(mainFruiFragment);
                    }
                }
                actionbar_title.setText(shopName + onlineTip);
                actionbar_right2.setVisibility(View.VISIBLE);
                actionbar_right2.setBackgroundResource(R.drawable.search_selector);
                actionbar_right2.setOnClickListener(new OnClickListener()
                {

                    @Override
                    public void onClick(View v)
                    {
                        search();
                    }
                });
                type = 0;
                break;
            case 1:
                if (current == 1)
                {
                    return;
                }
                current = 1;
                preClick(true);
                categoryImage.setImageResource(R.drawable.nav_type_click);

                categoryText.setTextColor(textcolor);
                if (shop.getClass_id().equals("18"))
                {
                    if (goodsCategoryFragment == null)
                    {
                        goodsCategoryFragment = new StickyHeaderFragment2();
//                        goodsCategoryFragment = new GoodsCategoryFragment();
                        transaction.add(R.id.content, goodsCategoryFragment);
                    } else
                    {
                        transaction.show(goodsCategoryFragment);
                    }
                    setTitles(Tools.getStr(this, R.string.TCAGEROTY));
                } else
                {
                    if (fruitListFragment == null)
                    {
                        fruitListFragment = new FruitListFragment();
                        Bundle argBundle = new Bundle();
                        argBundle.putString("store_id", shop.getStore_id());
                        fruitListFragment.setArguments(argBundle);
                        transaction.add(R.id.content, fruitListFragment);
                    } else
                    {
                        transaction.show(fruitListFragment);
                    }
                    setTitles(shop.getStore_name());
                }

                actionbar_right2.setVisibility(View.VISIBLE);
                actionbar_right2.setBackgroundResource(R.drawable.search_selector);
                actionbar_right2.setOnClickListener(new OnClickListener()
                {

                    @Override
                    public void onClick(View v)
                    {
                        search();
                    }
                });
                type = 1;
                break;
            case 2:
                if (current == 2)
                {
                    return;
                }
                if (memberId == null || memberId.equals(""))
                {
                    Intent intent = new Intent(this, LoginActivity.class);
                    intent.putExtra("login", "shop");
                    startActivityForResult(intent, MyConstant.REQUEST_SHOPCARD);

                    AnimUtil.animToSlide(activity);
                    return;
                }
                current = 2;

                preClick(true);
                shopcardImage.setImageResource(R.drawable.nav_card_click);

                shopcardText.setTextColor(textcolor);

                if (shopCardFragment == null)
                {
                    shopCardFragment = new ShopCardFragment();
                    transaction.add(R.id.content, shopCardFragment);
                } else
                {
                    transaction.show(shopCardFragment);
                    first = false;
                }

                setTitles(Tools.getStr(this, R.string.TSHOPCARD));

                actionbar_right2.setBackgroundResource(R.drawable.ic_delete);
                actionbar_right2.setVisibility(View.VISIBLE);
                actionbar_right2.setOnClickListener(new OnClickListener()
                {

                    @Override
                    public void onClick(View v)
                    {
                        shopCardFragment.deleteAll();
                    }
                });
                if (!first)
                {
                    shopCardFragment.query();
                }
                type = 2;
                break;
            case 3:
                if (current == 3)
                {
                    return;
                }
                if (memberId == null || memberId.equals(""))
                {
                    Intent intent = new Intent(this, LoginActivity.class);
                    intent.putExtra("login", "my");

                    startActivityForResult(intent, MyConstant.REQUEST_MY);
                    AnimUtil.animToSlide(activity);
                    return;
                }
                current = 3;
                preClick(true);
                myImage.setImageResource(R.drawable.nav_my_click);

                myText.setTextColor(textcolor);
                if (myFragment == null)
                {
                    myFragment = new MyFragment();
                    transaction.add(R.id.content, myFragment);
                } else
                {
                    transaction.show(myFragment);
                }

                setTitles(Tools.getStr(this, R.string.TMY));

                actionbar_right2.setVisibility(View.INVISIBLE);
                type = 3;
                break;
        }
        transaction.commit();
    }

    void preClick(boolean hide)
    {
        clearSelection();
        transaction = fragmentManager.beginTransaction();
//        transaction.setCustomAnimations (R.anim.push_left_in,
//                R.anim.push_right_out);
        transaction.setCustomAnimations(R.anim.activity_in_from_right,
                R.anim.activity_out_to_left);
        if (hide)
        {
            hideFragments(transaction);
        }

    }

    private void hideFragments(FragmentTransaction transaction)
    {
        if (mainFragment != null)
        {
            transaction.hide(mainFragment);
        }
        if (goodsCategoryFragment != null)
        {
            transaction.hide(goodsCategoryFragment);
        }
        if (shopCardFragment != null)
        {
            transaction.hide(shopCardFragment);
        }
        if (myFragment != null)
        {
            transaction.hide(myFragment);
        }
        if (mainFruiFragment != null)
        {
            transaction.hide(mainFruiFragment);
        }
        if (fruitListFragment != null)
        {
            transaction.hide(fruitListFragment);
        }
    }

    @SuppressLint("NewApi")
    private void clearSelection()
    {
        firstImage.setImageResource(R.drawable.nav_shop_nomal);
        firstText.setTextColor(textcolornormal);

        categoryImage.setImageResource(R.drawable.nav_type_normal);
        categoryText.setTextColor(textcolornormal);

        shopcardImage.setImageResource(R.drawable.nav_card_normal);
        shopcardText.setTextColor(textcolornormal);

        myImage.setImageResource(R.drawable.nav_my_normal);
        myText.setTextColor(textcolornormal);

    }

    private long exitTime = 0;

    @Override
    public void onBackPressed()
    {
        if ((System.currentTimeMillis() - exitTime) > 2000)
        {
            ToastUtil.toast (this, Tools.getStr (this, R.string.DOUBLEQUIT));
            exitTime = System.currentTimeMillis();
        } else
        {
            super.onBackPressed();
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data)
    {
        super.onActivityResult(requestCode, resultCode, data);
        updateUser();
        if (requestCode == MyConstant.REQUEST_SHOPCARD
                && resultCode == MyConstant.RESULT_MAIN)
        {
            setTabSelection(2);
        } else if (requestCode == MyConstant.REQUEST_MY
                && resultCode == MyConstant.RESULT_MAIN)
        {
            setTabSelection(3);
        }
        hongbao();
    }

    public void setQuit()
    {
        setTabSelection(0);
        updateUser();
    }

    public void updateUser()
    {
        memberId = sp.getString(MyConstant.KEY_MEMBERID, "");
    }

    @Override
    public void setTitles(String str)
    {
        super.setTitles(str);
        actionbar_title.setTextColor(Color.WHITE);
        if (current == 0 && !MyConstant.SHOP_ONLINE)
        {
            actionbar_title.setTextColor(Color.rgb(102, 102, 102));
            String name = actionbar_title.getText().toString();
            actionbar_title.setText(String.format(
                    "%s(" + Tools.getStr(this, R.string.LEAVEUP) + ")", name));
        }
    }

    public void isOnline(String url, RequestParams rp)
    {
        HttpUtils httpUtils = new HttpUtils();
        httpUtils.send(HttpMethod.POST, MyConstant.API_BASE_URL + url, rp,
                new RequestCallBack<String>()
                {

                    @Override
                    public void onSuccess(ResponseInfo<String> responseInfo)
                    {
                        String result = responseInfo.result;
                        String isRedPackage = result;
                        if (null != isRedPackage)
                        {
                            try
                            {
                                JSONObject jsonResult = new JSONObject(
                                        isRedPackage);
                                String isopen = jsonResult.getString("isopen");
                                if (isopen.equals("1"))
                                {
                                    MyConstant.SHOP_ONLINE = true;
                                } else
                                {
                                    MyConstant.SHOP_ONLINE = false;
                                    onlineTip = "(离开)";
                                }
                                setTitles(actionbar_title.getText().toString());
                            } catch (JSONException e)
                            {
                                e.printStackTrace();
                            }
                        }
                    }

                    @Override
                    public void onFailure(HttpException arg0, String arg1)
                    {
                    }
                });

    }
}
