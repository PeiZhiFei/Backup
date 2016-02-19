package com.runkun.lbsq.busi.test;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.graphics.drawable.BitmapDrawable;
import android.os.Build;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.PopupWindow;
import android.widget.TextView;

import com.hitoosoft.hrssapp.R;

import java.io.File;
import java.math.BigDecimal;
import java.math.RoundingMode;

import library.util.AnimUtil;
import library.widget.snackbar.TS;
import library.widget.swipeback.SwipeBackActivity2;

@SuppressLint("SdCardPath")
public class BaseActivity extends SwipeBackActivity2 {

    protected PopupWindow popupWindow;
    protected ImageView actionbarSettingView;
    protected String diskCachePath, webviewCache;
    protected TextView clearcache;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // 4.0版本之下不支持滑动
        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.ICE_CREAM_SANDWICH) {
            setSwipeBackEnable(false);
        }
        diskCachePath = getApplicationContext().getCacheDir().getAbsolutePath();
        webviewCache = "/data/data/" + getApplicationContext().getPackageName()
                + "/app_webview";
        initPopup();
    }


    public void setting(View view) {
        DisplayMetrics metric = new DisplayMetrics();
        getWindowManager().getDefaultDisplay().getMetrics(metric);
        popupWindow.showAsDropDown(view, (int) (metric.widthPixels * (-0.35)),
                10);
        actionbarSettingView = (ImageView) view;
        double totalSize = getDirSize(new File(diskCachePath));
        double webSize = getDirSize(new File(webviewCache));
        double size = totalSize + webSize;
        BigDecimal number = new BigDecimal(size);
        BigDecimal result = number.setScale(1, RoundingMode.DOWN);
        clearcache.setText("缓存(" + result + "M)");
    }

    @SuppressWarnings("deprecation")
    private void initPopup() {
        View view = LayoutInflater.from(this).inflate(R.layout.popup_setting,
                null);
        popupWindow = new PopupWindow(view,
                ViewGroup.LayoutParams.WRAP_CONTENT,
                ViewGroup.LayoutParams.WRAP_CONTENT);
        popupWindow.setFocusable(true); // 使其聚焦
        popupWindow.setOutsideTouchable(true); // 设置允许在外点击消失
        popupWindow.update(); // 刷新状态
        popupWindow.setAnimationStyle(R.style.popup_animation);
        // 点back键和其他地方使其消失,设置了这个才能触发OnDismisslistener ，设置其他控件变化等操作
        popupWindow.setBackgroundDrawable(new BitmapDrawable());

        TextView subscribeMsgView = (TextView) view
                .findViewById(R.id.tv_subscribemsg);
        TextView aboutView = (TextView) view.findViewById(R.id.tv_about);

        TextView checkupdateView = (TextView) view
                .findViewById(R.id.tv_checkupdate);
        TextView guide = (TextView) view.findViewById(R.id.tv_guide);
        TextView fav = (TextView) view.findViewById(R.id.tv_fav);
        clearcache = (TextView) view.findViewById(R.id.tv_clearcache);

        SettingOnClickListener listener = new SettingOnClickListener();
        subscribeMsgView.setOnClickListener(listener);
        aboutView.setOnClickListener(listener);
        checkupdateView.setOnClickListener(listener);
        clearcache.setOnClickListener(listener);
        guide.setOnClickListener(listener);
        fav.setOnClickListener(listener);

    }

    private class SettingOnClickListener implements OnClickListener {
        @Override
        public void onClick(View v) {
            switch (v.getId()) {
                case R.id.tv_clearcache:
                    tooglePopup();
                    deleteCache(diskCachePath);
                    deleteCache(webviewCache);
                    // 删除阅读状态
                    deleteFiles("/data/data/"
                            + getApplicationContext().getPackageName()
                            + "/databases/readed.db");
                    TS.t(BaseActivity.this, "清除缓存成功");
                    clearcache.setText("清缓存(0.0M)");
                    break;
                case R.id.tv_fav:
                    tooglePopup();
                    Intent favintent = new Intent(BaseActivity.this,
                            FavouriteActivity.class);
                    favintent.putExtra("welcome", false);
                    startActivity(favintent);
                    AnimUtil.animToSlide(BaseActivity.this);
                    break;
            }
        }

        private void deleteFiles(String string) {
            File f = new File(string).getAbsoluteFile();
            if (f.exists()) {
                f.delete();
            } else {
            }

        }

        private void deleteCache(String path) {
            File cachedFileDir = new File(path);
            if (cachedFileDir.exists() && cachedFileDir.isDirectory()) {
                File[] cachedFiles = cachedFileDir.listFiles();
                for (File f : cachedFiles) {
                    if (f.exists()) {
                        if (f.isFile()) {
                            f.delete();
                        } else {
                            deleteCache(f.getAbsolutePath());
                        }
                    }
                }
            }
        }
    }

    public static double getDirSize(File file) {
        // 判断文件是否存在
        if (file.exists()) {
            // 如果是目录则递归计算其内容的总大小
            if (file.isDirectory()) {
                File[] children = file.listFiles();
                double size = 0;
                for (File f : children) {
                    size += getDirSize(f);
                }
                return size;
            } else {// 如果是文件则直接返回其大小,以“兆”为单位
                double size = (double) file.length() / 1024 / 1024;
                return size;
            }
        } else {
            return 0.0;
        }
    }

}