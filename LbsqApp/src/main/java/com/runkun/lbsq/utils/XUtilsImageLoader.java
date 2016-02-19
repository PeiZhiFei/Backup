package com.runkun.lbsq.utils;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.TransitionDrawable;
import android.view.ViewGroup.LayoutParams;
import android.widget.ImageView;

import com.lidroid.xutils.BitmapUtils;
import com.lidroid.xutils.bitmap.BitmapDisplayConfig;
import com.lidroid.xutils.bitmap.callback.BitmapLoadFrom;
import com.lidroid.xutils.bitmap.callback.DefaultBitmapLoadCallBack;
import com.runkun.lbsq.R;

public class XUtilsImageLoader
{
    private static final ColorDrawable TRANSPARENT_DRAWABLE = new ColorDrawable (
            17170445);
    private BitmapUtils bitmapUtils;
    private Context mContext;
    private boolean fadeIn = false;
    private boolean crop = true;

    public XUtilsImageLoader (Context paramContext)
    {
        this.mContext = paramContext;
        this.bitmapUtils = new BitmapUtils (this.mContext);
        this.bitmapUtils.configDefaultLoadingImage (R.drawable.zhanwei);
        this.bitmapUtils.configDefaultLoadFailedImage (R.drawable.no_data);
        this.bitmapUtils.configDefaultBitmapConfig (Bitmap.Config.RGB_565);
        this.bitmapUtils.configMemoryCacheEnabled (false);
        this.bitmapUtils.configDiskCacheEnabled (true);
    }

    public XUtilsImageLoader (Context context, int width, int height)
    {
        this.mContext = context;
        this.bitmapUtils = new BitmapUtils (this.mContext);
        // this.bitmapUtils.configDefaultLoadingImage(R.drawable.ads);
        // this.bitmapUtils.configDefaultLoadFailedImage(R.drawable.no_data);
        this.bitmapUtils.configDefaultBitmapConfig (Bitmap.Config.RGB_565);
        this.bitmapUtils.configDefaultBitmapMaxSize (width, height);
        this.bitmapUtils.configMemoryCacheEnabled (false);
        this.bitmapUtils.configDiskCacheEnabled (true);
    }

    public XUtilsImageLoader (Context context, int width, int height,
                              boolean isfadeIn)
    {
        this.mContext = context;
        this.bitmapUtils = new BitmapUtils (this.mContext);
        // this.bitmapUtils.configDefaultLoadingImage(R.drawable.ads);
        //        this.bitmapUtils.configDefaultLoadFailedImage (R.drawable.no_data);
        this.bitmapUtils.configDefaultBitmapConfig (Bitmap.Config.RGB_565);
        this.bitmapUtils.configDefaultBitmapMaxSize (width, height);
        this.bitmapUtils.configMemoryCacheEnabled (true);
        this.bitmapUtils.configDiskCacheEnabled (true);
        fadeIn = isfadeIn;
    }

    public XUtilsImageLoader (Context context, int paramInteger1, int paramInteger2, boolean isfadeIn, boolean crop)
    {

        this.mContext = context;
        this.bitmapUtils = new BitmapUtils (this.mContext);
        // this.bitmapUtils.configDefaultLoadingImage(R.drawable.ads);
        if ( paramInteger1 != 0 )
        {
            this.bitmapUtils.configDefaultLoadingImage (paramInteger1);
        }
        if ( paramInteger2 != 0 )
        {
            this.bitmapUtils.configDefaultLoadFailedImage (paramInteger2);
        }
        this.bitmapUtils.configDefaultBitmapConfig (Bitmap.Config.RGB_565);
        this.bitmapUtils.configMemoryCacheEnabled (false);
        this.bitmapUtils.configDiskCacheEnabled (true);
        fadeIn = isfadeIn;
        this.crop = crop;
    }

    public XUtilsImageLoader (Context paramContext, Integer paramInteger1,
                              Integer paramInteger2)
    {
        this.mContext = paramContext;
        this.bitmapUtils = new BitmapUtils (this.mContext);
        this.bitmapUtils.configDefaultLoadingImage (paramInteger1.intValue ());
        this.bitmapUtils.configDefaultLoadFailedImage (paramInteger2.intValue ());
        this.bitmapUtils.configDefaultBitmapConfig (Bitmap.Config.RGB_565);
    }

    public XUtilsImageLoader (Context paramContext, Integer paramInteger1,
                              Integer paramInteger2, int paramInt1, int paramInt2)
    {
        this.mContext = paramContext;
        this.bitmapUtils = new BitmapUtils (this.mContext);
        this.bitmapUtils.configDefaultAutoRotation (true);
        this.bitmapUtils.configMemoryCacheEnabled (false);
        this.bitmapUtils.configDefaultLoadingImage (paramInteger1.intValue ());
        this.bitmapUtils.configDefaultLoadFailedImage (paramInteger2.intValue ());
        this.bitmapUtils.configDefaultBitmapConfig (Bitmap.Config.RGB_565);
        this.bitmapUtils.configDefaultBitmapMaxSize (paramInt1, paramInt2);
    }

    private void fadeInDisplay (ImageView paramImageView, Bitmap paramBitmap)
    {
        if ( fadeIn )
        {
            Drawable[] local = new Drawable[2];
            local[0] = TRANSPARENT_DRAWABLE;
            local[1] = new BitmapDrawable (paramImageView.getResources (),
                    paramBitmap);
            TransitionDrawable localObject = new TransitionDrawable (local);
            paramImageView.setImageDrawable (null);
            paramImageView.setScaleType (crop ? ImageView.ScaleType.FIT_XY : ImageView.ScaleType.FIT_CENTER);
            paramImageView.setImageDrawable (localObject);
            // paramImageView.setImageDrawable(localObject);
            localObject.startTransition (500);

        } else
        {
            paramImageView.setImageDrawable (new BitmapDrawable (
                    paramImageView.getResources (), paramBitmap));
        }
    }

    private void fadeInDisplay (ImageView paramImageView, Bitmap paramBitmap,
                                int width)
    {
        Drawable[] local = new Drawable[2];
        local[0] = TRANSPARENT_DRAWABLE;
        int orHeight = paramBitmap.getHeight ();
        int orwidht = paramBitmap.getWidth ();
        int imagheight = 0;
        imagheight = (width * orHeight) / orwidht;
        LayoutParams lp = paramImageView.getLayoutParams ();
        lp.height = imagheight;
        lp.width = width;
        paramImageView.setLayoutParams (lp);
        local[1] = new BitmapDrawable (paramImageView.getResources (),
                paramBitmap);
        TransitionDrawable localObject = new TransitionDrawable (local);
        paramImageView.setImageDrawable (null);
        paramImageView.setBackgroundDrawable (localObject);
        localObject.startTransition (500);
    }

    public void display (ImageView paramImageView, String url)
    {
        this.bitmapUtils.display (paramImageView, url,
                new CustomBitmapLoadCallBack ());
    }

    public void display (ImageView paramImageView, String url, int widht)
    {
        this.bitmapUtils.display (paramImageView, url,
                new CustomBitmapLoadCallBack (widht));
    }

    public class CustomBitmapLoadCallBack extends
            DefaultBitmapLoadCallBack<ImageView>
    {
        private int width;

        public CustomBitmapLoadCallBack ()
        {
        }

        public CustomBitmapLoadCallBack (int width)
        {
            this.width = width;
        }

        @Override
        public void onLoadCompleted (ImageView paramImageView,
                                     String paramString, Bitmap paramBitmap,
                                     BitmapDisplayConfig paramBitmapDisplayConfig,
                                     BitmapLoadFrom paramBitmapLoadFrom)
        {
            paramImageView.getMeasuredHeight ();
            paramImageView.getMeasuredWidth ();
            if ( paramBitmapLoadFrom == BitmapLoadFrom.MEMORY_CACHE )
            {
                paramImageView.setImageBitmap (paramBitmap);
            }
            if ( width != 0 )
            {
                XUtilsImageLoader.this.fadeInDisplay (paramImageView,
                        paramBitmap, width);
            } else
            {
                XUtilsImageLoader.this.fadeInDisplay (paramImageView,
                        paramBitmap);
            }

        }

    }
}
