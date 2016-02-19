package com.runkun.lbsq.fragment;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.os.Parcelable;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v4.view.ViewPager.OnPageChangeListener;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.ImageView;
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
import com.runkun.lbsq.activity.LoginActivity;
import com.runkun.lbsq.activity.MyCouponsActivity;
import com.runkun.lbsq.activity.ProductAcitivity;
import com.runkun.lbsq.activity.ShopListActivity;
import com.runkun.lbsq.bean.Ads;
import com.runkun.lbsq.bean.Shop;
import com.runkun.lbsq.utils.AnimUtil;
import com.runkun.lbsq.utils.HttpHelper;
import com.runkun.lbsq.utils.MyConstant;
import com.runkun.lbsq.utils.Tools;
import com.runkun.lbsq.utils.XUtilsImageLoader;
import com.runkun.lbsq.view.BulletinView;
import com.runkun.lbsq.view.MenuItem;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

import feifei.project.util.ConfigUtil;
import feifei.project.util.L;

public class MainAdsFragment extends MainFirstBaseFragment implements
        OnClickListener
{
    @ViewInject(R.id.subpage1)
    private MenuItem menuitem1;
    @ViewInject(R.id.subpage2)
    private MenuItem menuitem2;
    @ViewInject(R.id.subpage3)
    private MenuItem menuitem3;
    @ViewInject(R.id.subpage4)
    private MenuItem menuitem4;
    @ViewInject(R.id.prices)
    private TextView prices;
    // @ViewInject(R.id.fanwei)
    // private TextView fanwei;
    @ViewInject(R.id.fanwei)
    private BulletinView fanwei;
    @ViewInject(R.id.iv1)
    ImageView iv1;
    @ViewInject(R.id.iv2)
    ImageView iv2;

    //    @ViewInject(R.id.iv1)
    //    SmartImageView iv1;
    //    @ViewInject(R.id.iv2)
    //    SmartImageView iv2;
    private String duckSubUrl;
    private String lobsterSubUrl;

    View view;

    @Override
    public View onCreateView(LayoutInflater paramLayoutInflater,
                             ViewGroup paramViewGroup, Bundle paramBundle)
    {
        view = paramLayoutInflater.inflate(R.layout.fragment_ads,
                paramViewGroup, false);
        ViewUtils.inject(this, view);
        // iv3.setVisibility(View.GONE);
        scheduledExecutorService = Executors.newSingleThreadScheduledExecutor();
        final String[] mainLable = new String[]{
                getActivity().getString(R.string.mainneu1),
                getActivity().getString(R.string.mainneu2),
                getActivity().getString(R.string.mainneu3),
                getActivity().getString(R.string.mainneu4)};
        // final int[] imglabel = new int[] { R.drawable.main_menu1,
        // R.drawable.main_menu2, R.drawable.main_menu3,
        // R.drawable.main_menu4 };

        final int[] imglabel = new int[]{R.drawable.menu_change,
                R.drawable.menu_phone, R.drawable.menu_kefu,
                R.drawable.menu_red};
        final MenuItem[] meniten = new MenuItem[]{menuitem1, menuitem2,
                menuitem3, menuitem4};
        new Handler().postDelayed(new Runnable()
        {

            @Override
            public void run()
            {
                // Shop shop = mainActivity.getShop();
                // String fare = shop.getFare();

                List<String> list = new ArrayList<String>();
                list.add(mainActivity.getShop().getDaliver_description());
                fanwei.setData(list);
                // fanwei.setText(mainActivity.getShop().getDaliver_description());
                AnimUtil.animLeftToCenter(prices);
                AnimUtil.animRightToCenter(fanwei);
                for (int x = 0; x < meniten.length; x++)
                {
                    meniten[x].setTextViewForText(mainLable[x]);
                    meniten[x].setImageButton(imglabel[x]);
                    AnimUtil.animFallDown(meniten[x]);
                }
                menuitem1.setOnClickListener(MainAdsFragment.this);
                menuitem2.setOnClickListener(MainAdsFragment.this);
                menuitem3.setOnClickListener(MainAdsFragment.this);
                menuitem4.setOnClickListener(MainAdsFragment.this);

            }
        }, 1000);
        iv1.setOnClickListener(this);
        iv2.setOnClickListener(this);
        fanwei.setFocusable(false);
        fanwei.startScroll();
        getInfo();
        requestAds();
        return view;
    }

    public void reloadData()
    {
        getInfo();
        requestAds();
    }


    public void getInfo()
    {
        RequestParams rp = new RequestParams();
        rp.addQueryStringParameter("store_id", mainActivity.getShop()
                .getStore_id());
        HttpHelper.postByCommand("findstoreinfo", rp,
                new RequestCallBack<String>()
                {

                    @Override
                    public void onFailure(HttpException paramHttpException,
                                          String paramString)
                    {
                        Tools.toast(getActivity(),
                                Tools.getStr(fragment, R.string.NETWORKERROR));
                    }

                    @Override
                    public void onSuccess(ResponseInfo<String> paramResponseInfo)
                    {
                        String result = paramResponseInfo.result;
                        L.l (result);
                        try
                        {
                            JSONObject jsonResul = new JSONObject(result);
                            // Log.e("log", jsonResul.toString());
                            String isResul = jsonResul.getString("code");
                            if (isResul.equals("200"))
                            {

                                JSONObject jsonReult = new JSONObject(result);
                                JSONArray allData = jsonReult
                                        .getJSONArray("datas");
                                JSONObject ob = allData.getJSONObject(0);
                                String alisa = ob.getString("alisa");
                                String fare = ob.getString("fare");
                                if ("0".equals(fare))
                                {
                                    prices.setText(alisa + "元起送");
                                } else
                                {
                                    prices.setText(alisa
                                            + Tools.getStr(fragment,
                                            R.string.WAYPRICEBASE)
                                            + fare
                                            + Tools.getStr(fragment,
                                            R.string.WAYPRICEPRICE));
                                }
                            }
                        } catch (JSONException e)
                        {
                            Tools.toast(getActivity(),
                                    Tools.getStr(fragment, R.string.JSONERROR));
                            e.printStackTrace();
                        }

                    }
                });
    }

    private void requestAds()
    {
        HttpUtils localHttpUtils = new HttpUtils();
        localHttpUtils.send(HttpMethod.POST, MyConstant.URLADS,
                new RequestCallBack<String>()
                {
                    @Override
                    public void onFailure(HttpException paramHttpException,
                                          String paramString)
                    {
                        Tools.toast(getActivity(),
                                Tools.getStr(fragment, R.string.NETWORKERROR));
                    }

                    @Override
                    public void onSuccess(ResponseInfo<String> paramResponseInfo)
                    {
                        String result = paramResponseInfo.result;
                        // Log.e("log", result);
                        try
                        {
                            JSONObject jsonResul = new JSONObject(result);
                            // Log.e("log", jsonResul.toString());
                            String isResul = jsonResul.getString("code");
                            if (isResul.equals("200"))
                            {

                                JSONObject jsonReult = new JSONObject(result);
                                final JSONArray allData = jsonReult
                                        .getJSONArray("datas");

                                JSONObject jsonRow = null;
                                adList.clear();
                                for (int x = 0; x < allData.length(); x++)
                                {
                                    jsonRow = allData.getJSONObject(x);
                                    String isUse = jsonRow.getString("is_use");
                                    if (null != isUse && !("1".equals(isUse)))
                                    {
                                        allData.remove(x);
                                        continue;
                                    }
                                    if (jsonRow.getString("is_type")
                                            .equals("2"))
                                    {
                                        iv1.setVisibility(View.VISIBLE);
                                        //                                        new XUtilsImageLoader (mainActivity, iv1
                                        //                                                .getMeasuredWidth (), iv1
                                        //                                                .getMeasuredHeight ())
                                        //                                                .display (
                                        //                                                        iv1,
                                        //                                                        allData.getJSONObject (x)
                                        //                                                                .getString (
                                        //                                                                        "default_content"));
                                        if (getActivity() != null)
                                        {
                                            new XUtilsImageLoader(getActivity(), iv1.getMeasuredWidth(),
                                                    iv1.getMeasuredHeight(), false).display(iv1,
                                                    allData.getJSONObject(x).getString("default_content"));

//                                            new XUtilsImageLoader (getActivity (), R.drawable.zhanwei, R.drawable.zhanwei,
//                                                    true, true).display (iv1, allData.getJSONObject (x).getString ("default_content"));
                                        }
                                    } else if (jsonRow.getString("is_type")
                                            .equals("4"))
                                    {
                                        iv2.setVisibility(View.VISIBLE);
                                        //                                        new XUtilsImageLoader (mainActivity, iv2
                                        //                                                .getMeasuredWidth (), iv2
                                        //                                                .getMeasuredHeight ())
                                        //                                                .display (
                                        //                                                        iv2,
                                        //                                                        allData.getJSONObject (x)
                                        //                                                                .getString (
                                        //                                                                        "default_content"));
                                        if (getActivity() != null)
                                        {

                                            new XUtilsImageLoader(getActivity(), iv2.getMeasuredWidth(),
                                                    iv2.getMeasuredHeight(), false).display(iv2,
                                                    allData.getJSONObject(x).getString("default_content"));
//                                            new XUtilsImageLoader (getActivity (), R.drawable.zhanwei, R.drawable.zhanwei,
//                                                    true, true).display (iv2, allData.getJSONObject (x).getString ("default_content"));
                                        }

                                    } else if (jsonRow.getString("is_type")
                                            .equals("3"))
                                    {
                                        duckSubUrl = jsonRow
                                                .getString("default_content");
                                    } else if (jsonRow.getString("is_type")
                                            .equals("5"))
                                    {

                                        lobsterSubUrl = jsonRow
                                                .getString("default_content");
                                    } else if (jsonRow.getString("is_type")
                                            .equals("1"))
                                    {
                                        Ads adDomain = new Ads();
                                        adDomain.setAp_id(jsonRow
                                                .getString("ap_id"));
                                        adDomain.setAp_intro(jsonRow
                                                .getString("ap_intro"));
                                        adDomain.setAp_name(jsonRow
                                                .getString("ap_name"));
                                        adDomain.setDefault_content(jsonRow
                                                .getString("default_content"));
                                        adDomain.setLink(jsonRow
                                                .getString("link"));
                                        adDomain.setIs_use(jsonRow
                                                .getString("is_use"));
                                        adList.add(adDomain);
                                    }
                                }
                                // if (adList.size() > 0) {
                                if (getActivity() != null)
                                {
                                    initAdData();
                                    initDot();
                                    startAd();
                                }
                                // }

                            }
                        } catch (JSONException e)
                        {
                            Tools.toast(getActivity(),
                                    Tools.getStr(fragment, R.string.JSONERROR));
                            e.printStackTrace();
                        }

                    }
                });
    }

    @Override
    public void onClick(View view)
    {
        Intent intent = new Intent();
        SharedPreferences sp = null;
        switch (view.getId())
        {
            case R.id.subpage1:
                ConfigUtil.write (mainActivity, MyConstant.KEY_GUIDE_FIRST, true);
                intent.setClass(mainActivity, ShopListActivity.class);
                // startActivityForResult(intent, 8);
                startActivity(intent);
                AnimUtil.animBackSlideFinish(mainActivity);
                break;
            case R.id.subpage2:
                String storeTel = ConfigUtil.readString(mainActivity,
                        Shop.SHOPPHONE, "");
                if (Tools.isEmpty(storeTel))
                {
                    Tools.toast(mainActivity,
                            Tools.getStr(fragment, R.string.CONCANTWRONG));
                } else
                {
                    intent.setAction(Intent.ACTION_DIAL);
                    intent.setData(Uri.parse("tel:" + storeTel));
                    startActivity(intent);
                    AnimUtil.animToSlide(mainActivity);
                }
                break;
            case R.id.subpage3:
                intent.setAction(Intent.ACTION_DIAL);
                intent.setData(Uri.parse("tel:" + "4000279567"));
                startActivity(intent);
                AnimUtil.animToSlide(mainActivity);
                break;
            case R.id.subpage4:
                if (Tools.isEmpty(ConfigUtil.readString(mainActivity, MyConstant.KEY_MEMBERID, "")))
                {
                    startActivity(new Intent(mainActivity, LoginActivity.class));
                    AnimUtil.animToSlide(mainActivity);
                } else
                {
                    intent.setClass(mainActivity, MyCouponsActivity.class);
                    startActivity(intent);
                    AnimUtil.animToSlide(mainActivity);
                }
                break;
            case R.id.iv1:
                sp = getActivity().getSharedPreferences("lbsq",
                        Context.MODE_PRIVATE);
                Editor edit = sp.edit();
                edit.putString("duckSubUrl", duckSubUrl);
                edit.apply();
                intent.putExtra("type", 1);
                intent.setClass(getActivity(), ProductAcitivity.class);
                getActivity().startActivity(intent);
                AnimUtil.animToSlide(getActivity());
                break;
            case R.id.iv2:
                sp = getActivity().getSharedPreferences("lbsq",
                        Context.MODE_PRIVATE);
                Editor edit2 = sp.edit();
                edit2.putString("lobsterSubUrl", lobsterSubUrl);
                edit2.apply();
                intent.putExtra("type", 2);
                intent.putExtra("lobsterSubUrl", lobsterSubUrl);
                intent.setClass(getActivity(), ProductAcitivity.class);
                getActivity().startActivity(intent);
                AnimUtil.animToSlide(getActivity());
                break;
        }
    }

    private ViewPager adViewPager;
    //    private List<SmartImageView> imageViews;
    private List<ImageView> imageViews;

    private int currentItem = 0;

    ImageView dot, dots[];

    private void initDot()
    {
        LinearLayout viewGroup = (LinearLayout) view
                .findViewById(R.id.viewgroup);
        LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(
                20, 20);
        layoutParams.setMargins(5, 5, 5, 5);
        dots = new ImageView[adList.size()];
        for (int i = 0; i < dots.length; i++)
        {
            dot = new ImageView(mainActivity);
            dot.setLayoutParams(layoutParams);
            dots[i] = dot;
            dots[i].setTag(i);
            if (i == 0)
            {
                dots[i].setBackgroundResource(R.drawable.shape_circle_white);
            } else
            {
                dots[i].setBackgroundResource(R.drawable.shape_circle_white_stroke);
            }
            viewGroup.addView(dots[i]);
        }
    }

    private void setCurDot(int position)
    {
        for (int i = 0; i < dots.length; i++)
        {
            if (position == i)
            {
                dots[i].setBackgroundResource(R.drawable.shape_circle_white);
            } else
            {
                dots[i].setBackgroundResource(R.drawable.shape_circle_white_stroke);
            }
        }
    }

    private ScheduledExecutorService scheduledExecutorService;

    private List<Ads> adList = new ArrayList<>();

    private Handler handler = new Handler()
    {
        @Override
        public void handleMessage(android.os.Message msg)
        {
            adViewPager.setCurrentItem(currentItem);
        }
    };

    // private void initImageLoader() {
    // File cacheDir = com.nostra13.universalimageloader.utils.StorageUtils
    // .getOwnCacheDirectory(getApplicationContext(), IMAGE_CACHE_PATH);
    //
    // DisplayImageOptions defaultOptions = new DisplayImageOptions.Builder()
    // .cacheInMemory(true).cacheOnDisc(true).build();
    //
    // ImageLoaderConfiguration config = new ImageLoaderConfiguration.Builder(
    // this).defaultDisplayImageOptions(defaultOptions)
    // .memoryCache(new LruMemoryCache(12 * 1024 * 1024))
    // .memoryCacheSize(12 * 1024 * 1024)
    // .discCacheSize(32 * 1024 * 1024).discCacheFileCount(100)
    // .discCache(new UnlimitedDiscCache(cacheDir))
    // .threadPriority(Thread.NORM_PRIORITY - 2)
    // .tasksProcessingOrder(QueueProcessingType.LIFO).build();
    //
    // ImageLoader.getInstance().init(config);
    // }

    @SuppressWarnings("deprecation")
    private void initAdData()
    {
        //        imageViews = new ArrayList<SmartImageView> ();
        imageViews = new ArrayList<ImageView>();
        // tv_title = (TextView) view.findViewById(R.id.tv_topic);
        addDynamicView();
        adViewPager = (ViewPager) view.findViewById(R.id.vp);
        adViewPager.setAdapter(new MyAdapter());
        adViewPager.setOnPageChangeListener(new MyPageChangeListener());

    }

    private void addDynamicView()
    {
        for (int i = 0; i < adList.size(); i++)
        {
            if (getActivity() != null)
            {
                ImageView imageView = new ImageView(getActivity());
                //            SmartImageView imageView = new SmartImageView (mainActivity);
                //            imageView.setLayoutParams (new LinearLayout.LayoutParams (-1, -2));
                //            imageView.setImageUrl (adList.get (i).getDefault_content (),
                //                    R.drawable.zhanwei, R.drawable.zhanwei);
                // mImageLoader.displayImage(adList.get(i).getDefault_content(),
                // imageView, options);
                imageView.setScaleType(ImageView.ScaleType.CENTER_CROP);
                //            new XUtilsImageLoader (getActivity (), R.drawable.zhanwei, R.drawable.zhanwei,
                //                    true, true).display (imageView, adList.get(i).getDefault_content());
                new XUtilsImageLoader(getActivity(), imageView.getMeasuredWidth(),
                        imageView.getMeasuredHeight(), false).display(imageView,
                        adList.get(i).getDefault_content());

                imageViews.add(imageView);
            }
            // dots.get(i).setVisibility(View.VISIBLE);
            // dotList.counter_add(dots.get(i));
        }
    }

    private void startAd()
    {
        scheduledExecutorService.scheduleAtFixedRate(new ScrollTask(), 5, 3,
                TimeUnit.SECONDS);
    }

    private class ScrollTask implements Runnable
    {

        @Override
        public void run()
        {
            synchronized (adViewPager)
            {
                currentItem = (currentItem + 1) % imageViews.size();
                handler.obtainMessage().sendToTarget();
            }
        }
    }

    private class MyPageChangeListener implements OnPageChangeListener
    {

        private int oldPosition = 0;

        @Override
        public void onPageScrollStateChanged(int arg0)
        {

        }

        @Override
        public void onPageScrolled(int arg0, float arg1, int arg2)
        {

        }

        @Override
        public void onPageSelected(int position)
        {
            currentItem = position;
            Ads adDomain = adList.get(position);
            // tv_date.setText(adDomain.getAp_intro());
            // tv_title.setText(adDomain.getAp_name());
            setCurDot(position);
            // dots.get(oldPosition).setBackgroundResource(R.drawable.dot_normal);
            // dots.get(position).setBackgroundResource(R.drawable.dot_focused);
            oldPosition = position;
        }
    }

    private class MyAdapter extends PagerAdapter
    {

        @Override
        public int getCount()
        {
            return adList.size();
        }

        @Override
        public Object instantiateItem(ViewGroup container, final int position)
        {
            //            SmartImageView iv = imageViews.get (position);
            ImageView iv = imageViews.get(position);
            container.addView(iv);
//            iv.setOnClickListener(new OnClickListener()
//            {
//
//                @Override
//                public void onClick(View v)
//                {
//                    startActivity(new Intent(mainActivity, MyWebActivity.class));
//                }
//            });
            return iv;
        }

        @Override
        public void destroyItem(View arg0, int arg1, Object arg2)
        {
            ((ViewPager) arg0).removeView((View) arg2);
        }

        @Override
        public boolean isViewFromObject(View arg0, Object arg1)
        {
            return arg0 == arg1;
        }

        @Override
        public void restoreState(Parcelable arg0, ClassLoader arg1)
        {

        }

        @Override
        public Parcelable saveState()
        {
            return null;
        }

        @Override
        public void startUpdate(View arg0)
        {

        }

        @Override
        public void finishUpdate(View arg0)
        {

        }

    }

}
