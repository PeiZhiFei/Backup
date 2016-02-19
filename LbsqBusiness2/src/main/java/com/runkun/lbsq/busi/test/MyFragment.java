package com.runkun.lbsq.busi.test;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.runkun.lbsq.utils.MyConstant;

import java.io.File;
import java.math.BigDecimal;
import java.math.RoundingMode;

public class MyFragment extends MainFirstBaseFragment
{
    private Button clearCache;
    private TextView login;
    String memberId;
    SharedPreferences sp;

    protected String basePath;
    protected String diskCachePath;// cache文件加
    protected String fileCache;// xml文件


    @Override
    public View onCreateView (LayoutInflater inflater, ViewGroup container,
                              Bundle savedInstanceState)
    {

        DisplayMetrics localDisplayMetrics = new DisplayMetrics ();
        getActivity ().getWindowManager ().getDefaultDisplay ()
                .getMetrics (localDisplayMetrics);
        int mScreenHeight = localDisplayMetrics.heightPixels;
        int mScreenWidth = localDisplayMetrics.widthPixels;
        LinearLayout.LayoutParams params = new LinearLayout.LayoutParams (
                mScreenWidth, (int) (mScreenHeight / 5.0F));
        params.gravity = Gravity.CENTER_VERTICAL;


        clearCache.setOnClickListener (this);
        sp = getActivity ().getSharedPreferences (MyConstant.FILE_NAME,
                Context.MODE_PRIVATE);

        basePath = "/data/data/" + getActivity ().getPackageName () + "/";
        diskCachePath = getActivity ().getCacheDir ().getAbsolutePath ();
        fileCache = getActivity ().getFilesDir ().getAbsolutePath ();
        double totalSize = getDirSize (new File (diskCachePath));
        double fileSize = getDirSize (new File (fileCache));
        double size = totalSize + fileSize;
        BigDecimal number = new BigDecimal (size);
        BigDecimal result = number.setScale (1, RoundingMode.DOWN);
        clearCache.setText ("清除缓存(" + result + "M)");
        return view;
    }


    private void deleteCache (String path)
    {
        File cachedFileDir = new File (path);
        if ( cachedFileDir.exists () && cachedFileDir.isDirectory () )
        {
            File[] cachedFiles = cachedFileDir.listFiles ();
            for (File f : cachedFiles)
            {
                if ( f.exists () )
                {
                    if ( f.isFile () )
                    {
                        f.delete ();
                    } else
                    {
                        deleteCache (f.getAbsolutePath ());
                    }
                }
            }
        }
    }

    public static double getDirSize (File file)
    {
        // 判断文件是否存在
        if ( file.exists () )
        {
            // 如果是目录则递归计算其内容
            if ( file.isDirectory () )
            {
                File[] children = file.listFiles ();
                double size = 0;
                for (File f : children)
                {
                    size += getDirSize (f);
                }
                return size;
            } else
            {
                // 如果是文件则直接返回以兆为单位
                double size = (double) file.length () / 1024 / 1024;
                return size;
            }
        } else
        {
            return 0.0;
        }
    }



}
