package com.runkun.lbsq.utils;

import android.animation.Keyframe;
import android.animation.ObjectAnimator;
import android.animation.PropertyValuesHolder;
import android.annotation.SuppressLint;
import android.annotation.TargetApi;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Build;
import android.provider.Settings;
import android.telephony.TelephonyManager;
import android.text.TextUtils;
import android.util.DisplayMetrics;
import android.util.TypedValue;
import android.view.Gravity;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.view.ViewGroup.LayoutParams;
import android.view.WindowManager;
import android.view.animation.AccelerateInterpolator;
import android.view.animation.Animation;
import android.view.animation.AnimationSet;
import android.view.animation.LinearInterpolator;
import android.view.animation.ScaleAnimation;
import android.view.animation.TranslateAnimation;
import android.view.inputmethod.InputMethodManager;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.lidroid.xutils.HttpUtils;
import com.lidroid.xutils.exception.HttpException;
import com.lidroid.xutils.http.RequestParams;
import com.lidroid.xutils.http.ResponseInfo;
import com.lidroid.xutils.http.callback.RequestCallBack;
import com.lidroid.xutils.http.client.HttpRequest;
import com.runkun.lbsq.R;
import com.runkun.lbsq.activity.BaseAcitivity;
import com.runkun.lbsq.activity.LoginActivity;
import com.runkun.lbsq.activity.MainActivity;
import com.runkun.lbsq.interfaces.onButtonClick;
import com.runkun.lbsq.view.PromptDialog;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.UnsupportedEncodingException;
import java.math.BigDecimal;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;
import java.util.UUID;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import feifei.project.util.ToastUtil;

@TargetApi(Build.VERSION_CODES.HONEYCOMB)
@SuppressLint("NewApi")
public class Tools
{

    public static void toast(Context context, CharSequence string)
    {
        if (context != null)
        {
            ToastUtil.toast (context, string);
        }

    }

    public static void toast(Context context, CharSequence string, int g)
    {
        if (context != null)
        {
            ToastUtil.toast(context, string, g);
        }

    }

    public static void toast(Context context, int string)
    {
        if (context != null)
        {
            ToastUtil.toast(context, string);
        }
    }

    public static boolean isEmpty(String s)
    {
        return TextUtils.isEmpty(s) || s.equals("");
    }

    public static String formatMysqlTimestamp(String timestamp, String pattern)
    {
        Date date = new Date(Long.valueOf(timestamp) * 1000);
        SimpleDateFormat sdf = new SimpleDateFormat(pattern,
                Locale.getDefault());
        return sdf.format(date);
    }

    public static boolean checkPhone(String string)
    {
        return match("^((1[0-9][0-9])|(15[^4,\\D])|(18[0,5-9]))\\d{8}$", string);
    }

    public static String getStr(Context context, int str)
    {
        return context != null ? context.getString(str) : "111";
    }

    public static boolean isNetworkAvailable(Context context)
    {
        boolean result = false;
        ConnectivityManager connectivityManager = (ConnectivityManager) context
                .getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo networkInfo = connectivityManager == null ? null
                : connectivityManager.getActiveNetworkInfo();
        if (networkInfo != null && networkInfo.isAvailable())
        {
            result = true;
            return result;
        }
        return result;
    }

    public static void keyboardShow(TextView view)
    {
        InputMethodManager inputManager = (InputMethodManager) view
                .getContext().getSystemService(Context.INPUT_METHOD_SERVICE);
        inputManager.showSoftInput(view, 0);
    }


    public static void dialog(Context context, String content, boolean cancelable,
                              final onButtonClick onButtonClick)
    {
        final PromptDialog pDialog = PromptDialog.create(context, context
                        .getResources().getString(R.string.DTITLE), content,
                PromptDialog.TYPE_CONFIRM);
        pDialog.setCancelable(cancelable);
        pDialog.setConfirmButton(
                context.getResources().getString(R.string.CONFIM),
                new OnClickListener()
                {

                    @Override
                    public void onClick(View v)
                    {
                        pDialog.dismiss();
                        if (onButtonClick != null)
                        {
                            onButtonClick.buttonClick();
                        }
                    }
                }).show();
    }


    private static boolean match(String rule, String string)
    {
        Pattern pattern = Pattern.compile(rule);
        Matcher matcher = pattern.matcher(string);
        return matcher.matches();
    }

    public static int getWidth(Context context)
    {
        DisplayMetrics mDisplayMetrics = new DisplayMetrics();
//        ((Activity) context).getWindowManager ().getDefaultDisplay ()
//                .getMetrics(mDisplayMetrics);
        WindowManager wm = (WindowManager) context.getSystemService(Context.WINDOW_SERVICE);
        wm.getDefaultDisplay().getMetrics(mDisplayMetrics);
        return mDisplayMetrics.widthPixels;
    }

    public static int getHeight(Context context)
    {
        DisplayMetrics mDisplayMetrics = new DisplayMetrics();
        ((Activity) context).getWindowManager().getDefaultDisplay()
                .getMetrics(mDisplayMetrics);
        return mDisplayMetrics.heightPixels;
    }

    protected TextView emptyText;

    public TextView getEmptyText()
    {
        return emptyText;
    }

    public View getEmptyView(Context context, int dr)
    {
        LinearLayout layout = new LinearLayout(context);
        LinearLayout.LayoutParams layoutParamsss = new LinearLayout.LayoutParams(
                LayoutParams.MATCH_PARENT, LayoutParams.MATCH_PARENT,
                Gravity.CENTER);
        layout.setOrientation(LinearLayout.VERTICAL);
        layout.setLayoutParams(layoutParamsss);
        emptyText = new TextView(context);
        LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(
                LayoutParams.MATCH_PARENT, LayoutParams.WRAP_CONTENT,
                Gravity.CENTER);
        // layoutParams.gravity = Gravity.CENTER;
        layoutParams.weight = 1;
        layoutParams.setMargins(5, dp2px(context, 120), 5, 5);
        emptyText.setLayoutParams(layoutParams);
        emptyText.setText(context.getResources().getString(
                R.string.EMPTYLOADING));
        emptyText.setTextSize(20);
        emptyText.setTextColor(context.getResources().getColor(
                R.color.main_red_color));
        emptyText.setGravity(Gravity.CENTER_HORIZONTAL);
        Drawable drawable;
        if (dr != 0)
        {
            drawable = context.getResources().getDrawable(dr);
        } else
        {
            drawable = context.getResources().getDrawable(R.drawable.no_data);
        }
        drawable.setBounds(0, 0, drawable.getMinimumWidth(),
                drawable.getMinimumHeight());
        emptyText.setCompoundDrawables(null, drawable, null, null);
        emptyText.setCompoundDrawablePadding(30);
        layout.setVisibility(View.GONE);
        layout.addView(emptyText, layoutParams);
        View zhanwei = new View(context);
        LinearLayout.LayoutParams p = new LinearLayout.LayoutParams(-1, 200);
        p.weight = 1;
        layout.addView(zhanwei, p);
        return layout;
    }

    public static int dp2px(Context context, float dp)
    {
        final float scale = context.getResources().getDisplayMetrics().density;
        return (int) (dp * scale + 0.5f);
    }

    public static int dp2px(Context context, int dp)
    {
        return (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, dp,
                context.getResources().getDisplayMetrics());
    }

    public static void refreshShopcartBadge(MainActivity context, int count)
    {

        if (context == null)
        {
            return;
        }
        if (context.findViewById(R.id.buttom_menu) == null)
        {
            return;
        }
        View b = context.findViewById(R.id.buttom_menu)
                .findViewWithTag("badge");
        if (MyConstant.shopcartBadge == null || b == null)
        {
            LinearLayout endView = (LinearLayout) context
                    .findViewById(R.id.shop_card);
            BadgeView badgeView = new BadgeView(context);
            badgeView.setBadgeCount(Integer.valueOf(count));
            badgeView.setBackground(20, Color.RED);
            badgeView.setTextColor(Color.WHITE);
            badgeView.setGravity(Gravity.RIGHT | Gravity.TOP);
            badgeView.setBadgeMargin(0, 0, 15, 0);
            badgeView.setTargetView(endView);
            badgeView.setTag("badge");

            MyConstant.shopcartBadge = badgeView;
        } else
        {

            MyConstant.shopcartBadge.setBadgeCount(count);
        }
        SharedPreferences sp = context.getSharedPreferences(
                MyConstant.FILE_NAME, Context.MODE_PRIVATE);
        Editor editor = sp.edit();
        editor.putString("shopcount", String.valueOf(count));
        editor.apply();
    }

    public static void refreshShopcartBadge(Context context, View endView,
                                            int count)
    {

        if (context == null)
        {
            return;
        }
        View container = (View) endView.getParent();
        View b = container.findViewWithTag("badge");
        if (MyConstant.bottomShopcartBadge == null || b == null)
        {
            BadgeView badgeView = new BadgeView(context);
            badgeView.setBadgeCount(Integer.valueOf(count));
            badgeView.setBackground(20, Color.RED);
            badgeView.setTextColor(Color.WHITE);
            badgeView.setGravity(Gravity.RIGHT | Gravity.TOP);
            badgeView.setBadgeMargin(0, 0, 3, 0);
            badgeView.setTargetView(endView);
            badgeView.setTag("badge");
            MyConstant.bottomShopcartBadge = badgeView;
        } else
        {

            MyConstant.bottomShopcartBadge.setBadgeCount(count);
        }

        refreshShopcartBadge(MainActivity.mainActivity, count);
    }

    public static void shake(View view)
    {
        //		if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB) {
        //			shakeOne(view);
        //		} else {
        AnimUtil.animShakeText(view);
        //		}
    }

    public static void shakeOne(View view)
    {
        tada(view, 1f).start();
    }

    public static void shakeThree(View view)
    {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB)
        {
            ObjectAnimator object = tada(view, 1f);
            object.setRepeatCount(2);
            object.start();
        }
    }

    public static ObjectAnimator tada(View view, float shakeFactor)
    {

        PropertyValuesHolder pvhScaleX = PropertyValuesHolder.ofKeyframe(
                View.SCALE_X, Keyframe.ofFloat(0f, 1f),
                Keyframe.ofFloat(.1f, .9f), Keyframe.ofFloat(.2f, .9f),
                Keyframe.ofFloat(.3f, 1.1f), Keyframe.ofFloat(.4f, 1.1f),
                Keyframe.ofFloat(.5f, 1.1f), Keyframe.ofFloat(.6f, 1.1f),
                Keyframe.ofFloat(.7f, 1.1f), Keyframe.ofFloat(.8f, 1.1f),
                Keyframe.ofFloat(.9f, 1.1f), Keyframe.ofFloat(1f, 1f));

        PropertyValuesHolder pvhScaleY = PropertyValuesHolder.ofKeyframe(
                View.SCALE_Y, Keyframe.ofFloat(0f, 1f),
                Keyframe.ofFloat(.1f, .9f), Keyframe.ofFloat(.2f, .9f),
                Keyframe.ofFloat(.3f, 1.1f), Keyframe.ofFloat(.4f, 1.1f),
                Keyframe.ofFloat(.5f, 1.1f), Keyframe.ofFloat(.6f, 1.1f),
                Keyframe.ofFloat(.7f, 1.1f), Keyframe.ofFloat(.8f, 1.1f),
                Keyframe.ofFloat(.9f, 1.1f), Keyframe.ofFloat(1f, 1f));

        PropertyValuesHolder pvhRotate = PropertyValuesHolder.ofKeyframe(
                View.ROTATION, Keyframe.ofFloat(0f, 0f),
                Keyframe.ofFloat(.1f, -3f * shakeFactor),
                Keyframe.ofFloat(.2f, -3f * shakeFactor),
                Keyframe.ofFloat(.3f, 3f * shakeFactor),
                Keyframe.ofFloat(.4f, -3f * shakeFactor),
                Keyframe.ofFloat(.5f, 3f * shakeFactor),
                Keyframe.ofFloat(.6f, -3f * shakeFactor),
                Keyframe.ofFloat(.7f, 3f * shakeFactor),
                Keyframe.ofFloat(.8f, -3f * shakeFactor),
                Keyframe.ofFloat(.9f, 3f * shakeFactor),
                Keyframe.ofFloat(1f, 0));

        return ObjectAnimator.ofPropertyValuesHolder(view, pvhScaleX,
                pvhScaleY, pvhRotate).setDuration(1000);
    }

    public static float scale(float fee)
    {
        BigDecimal bd = new BigDecimal(fee);
        return bd.setScale(2, BigDecimal.ROUND_HALF_UP).floatValue();
    }

    public static double scale(double fee)
    {
        BigDecimal bd = new BigDecimal(fee);
        return bd.setScale(2, BigDecimal.ROUND_HALF_UP).doubleValue();
    }

    public static void animateTo(Activity activity, View aniView,
                                 int[] startLoc, int[] endLoc, int durationMills,
                                 Animation.AnimationListener listener)
    {
        ViewGroup rootView = (ViewGroup) activity.getWindow().getDecorView();
        LinearLayout animLayout = new LinearLayout(activity);
        LinearLayout.LayoutParams lp = new LinearLayout.LayoutParams(
                LayoutParams.MATCH_PARENT, LayoutParams.MATCH_PARENT);
        animLayout.setLayoutParams(lp);
        animLayout.setId(Integer.MAX_VALUE);
        animLayout.setBackgroundResource(android.R.color.transparent);
        rootView.addView(animLayout);

        int x = startLoc[0];
        int y = startLoc[1];
        LinearLayout.LayoutParams aniViewLP = new LinearLayout.LayoutParams(
                LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT);
        aniViewLP.leftMargin = x;
        aniViewLP.topMargin = y;
        aniView.setLayoutParams(aniViewLP);
        animLayout.addView(aniView);

        int endX = endLoc[0] - startLoc[0];
        int endY = endLoc[1] - startLoc[1];
        TranslateAnimation translateAnimationX = new TranslateAnimation(0,
                endX, 0, 0);
        translateAnimationX.setInterpolator(new LinearInterpolator());
        translateAnimationX.setRepeatCount(0);
        translateAnimationX.setFillAfter(true);

        TranslateAnimation translateAnimationY = new TranslateAnimation(0, 0,
                0, endY);
        translateAnimationY.setInterpolator(new AccelerateInterpolator());
        translateAnimationY.setRepeatCount(0);
        translateAnimationX.setFillAfter(true);

        Animation mScaleAnimation = new ScaleAnimation(1f, 0.1f, 1f, 0.1f,
                Animation.RELATIVE_TO_PARENT, Animation.RELATIVE_TO_PARENT);
        mScaleAnimation.setDuration(500);
        mScaleAnimation.setFillAfter(true);
        AnimationSet mAnimationSet = new AnimationSet(false);
        mAnimationSet.addAnimation(mScaleAnimation);
        mAnimationSet.setFillAfter(false);
        mAnimationSet.addAnimation(translateAnimationX);
        mAnimationSet.addAnimation(translateAnimationY);
        mAnimationSet.setDuration(500);

        if (listener != null)
        {
            mAnimationSet.setAnimationListener(listener);
        }

        aniView.startAnimation(mAnimationSet);
    }

    public static void addShopCard(final BaseAcitivity pram, String url,
                                   final View trgerView, final View endView, final ImageView aniView, RequestParams requestParams, final boolean main)
    {
        HttpUtils localHttpUtils = new HttpUtils();
        pram.dialogInit();
        pram.dialogProgress(pram, "请稍候");
        localHttpUtils.send(HttpRequest.HttpMethod.POST, MyConstant.API_BASE_URL + url,
                requestParams, new RequestCallBack<String>()
                {
                    @Override
                    public void onFailure(HttpException paramHttpException,
                                          String paramString)
                    {
                        Tools.toast(pram, "网络连接错误");
                        pram.dialogDismiss();
                    }

                    @Override
                    public void onSuccess(ResponseInfo<String> paramResponseInfo)
                    {
                        pram.dialogDismiss();
                        String localObject = paramResponseInfo.result;
                        try
                        {
                            JSONObject resultJson = new JSONObject(localObject);
                            String result = resultJson.getString("code");
                            Boolean isAdd = null;
                            isAdd = null;
                            Long id = null;
                            try
                            {
                                isAdd = resultJson.getBoolean("datas");
                            } catch (Exception e)
                            {
                                id = resultJson.getLong("datas");
                            }
                            int[] startLoc = new int[2];
                            int[] end_location = new int[2];// 这是用来存储动画结束位置的X、Y坐标
                            trgerView.getLocationInWindow(startLoc);
                            endView.getLocationInWindow(end_location);

                            if (("200".equals(result)) && null != isAdd
                                    && isAdd)
                            {
                                Tools.animateTo(pram, aniView,
                                        startLoc, end_location, 300,
                                        new Animation.AnimationListener()
                                        {
                                            @Override
                                            public void onAnimationStart(
                                                    Animation animation)
                                            {
                                                aniView.setVisibility(View.VISIBLE);
                                            }

                                            @Override
                                            public void onAnimationRepeat(
                                                    Animation animation)
                                            {
                                            }

                                            // 动画的结束
                                            @Override
                                            public void onAnimationEnd(
                                                    Animation animation)
                                            {
                                                aniView.setVisibility(View.GONE);
                                            }
                                        });
                                String oldCount = resultJson.getString("count");

                                if (main)
                                {
                                    Tools.refreshShopcartBadge((MainActivity) pram,
                                            Integer.valueOf(oldCount));
                                } else
                                {
                                    Tools.refreshShopcartBadge(pram, endView,
                                            Integer.valueOf(oldCount));
                                }

                            } else if (("200".equals(result)) && null != id)
                            {
                                Tools.animateTo(pram, aniView,
                                        startLoc, end_location, 300,
                                        new Animation.AnimationListener()
                                        {
                                            @Override
                                            public void onAnimationStart(
                                                    Animation animation)
                                            {
                                                aniView.setVisibility(View.VISIBLE);
                                            }

                                            @Override
                                            public void onAnimationRepeat(
                                                    Animation animation)
                                            {
                                            }

                                            // 动画的结束
                                            @Override
                                            public void onAnimationEnd(
                                                    Animation animation)
                                            {
                                                aniView.setVisibility(View.GONE);
                                            }
                                        });
                                String oldCount = resultJson.getString("count");

                                if (main)
                                {
                                    Tools.refreshShopcartBadge((MainActivity) pram,
                                            Integer.valueOf(oldCount));
                                } else
                                {
                                    Tools.refreshShopcartBadge(pram, endView,
                                            Integer.valueOf(oldCount));
                                }

                            } else if(("205".equals(result))){//超过商品限购数量
                                int counts = resultJson.getInt("count");
                                Tools.toast(pram, "超过商品限购数量,商品限购"+counts+"件");
                            }else if(("206".equals(result))){//没有这个商品
                                Tools.toast(pram, "这个商品已经下架了");
                            }else if(("207".equals(result))){//当天超过限购数量
                                int counts = resultJson.getInt("count");
                                Tools.toast(pram, "超过当天限购数量了,商品限购"+counts+"件");
                            }else {
                                Tools.dialog(pram, "您还没有登录,单击确定立即登录添加",
                                        true, new onButtonClick()
                                        {
                                            @Override
                                            public void buttonClick()
                                            {
                                                pram.startActivity(new Intent(
                                                        pram,
                                                        LoginActivity.class));
                                            }
                                        });
                            }
                        } catch (JSONException localJSONException)
                        {
                            Tools.toast(pram, "数据解析错误");
                            pram.dialogDismiss();
                            localJSONException.printStackTrace();
                        }
                        pram.dialogDismiss();
                    }
                });
    }


    protected static final String PREFS_FILE = "get_device_id.xml";
    protected static final String PREFS_DEVICE_ID = "get_device_id";
    protected static String uuid;

    public static String getUDID(Context context)
    {
        if (uuid == null)
        {
            final SharedPreferences prefs = context.getSharedPreferences(
                    PREFS_FILE, 0);
            final String id = prefs.getString(PREFS_DEVICE_ID, null);
            if (id != null)
            {
                uuid = id;
            } else
            {
                final String androidId = Settings.Secure.getString(context.getContentResolver(),
                        Settings.Secure.ANDROID_ID);
                try
                {
                    if (!"9774d56d682e549c".equals(androidId))
                    {
                        uuid = UUID.nameUUIDFromBytes(androidId.getBytes("utf8")).toString();
                    } else
                    {
                        final String deviceId = ((TelephonyManager) context.getSystemService(
                                Context.TELEPHONY_SERVICE)).getDeviceId();
                        uuid = deviceId != null ?
                                UUID.nameUUIDFromBytes(deviceId.getBytes("utf8")).toString() :
                                UUID.randomUUID().toString();
                    }
                } catch (UnsupportedEncodingException e)
                {
                    throw new RuntimeException(e);
                }
                prefs.edit().putString(PREFS_DEVICE_ID, uuid).apply();
            }
        }
        return uuid;
    }

    public static boolean isSuccess(JSONObject result) {
        try {
            return 200 == result.getInt("code");
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return false;
    }


}