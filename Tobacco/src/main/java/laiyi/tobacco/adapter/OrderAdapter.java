package laiyi.tobacco.adapter;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.widget.TextView;

import java.util.List;

import feifei.library.util.MyBaseAdapter;
import feifei.library.util.MyViewHolder;
import feifei.library.util.Tools;
import feifei.library.view.smartimage.SmartImageView;
import laiyi.tobacco.R;
import laiyi.tobacco.bean.Land;

public class OrderAdapter extends MyBaseAdapter<Land>
{

    public OrderAdapter (Context context, List<Land> datas)
    {
        super (context, datas, R.layout.item_land, false);
    }

    @Override
    protected void convert (final MyViewHolder viewHolder, final Land bean)
    {
//        L.l ("进来了");
        final SmartImageView land_img = viewHolder.getView (R.id.land_img);
        final TextView land_name = viewHolder.getView (R.id.land_name);
        final TextView land_type = viewHolder.getView (R.id.land_type);
        final TextView land_feature = viewHolder.getView (R.id.land_feature);
        final TextView soil_type = viewHolder.getView (R.id.soil_type);
        final TextView tobacco_breed = viewHolder.getView (R.id.tobacco_breed);
        final TextView land_year = viewHolder.getView (R.id.land_year);
        final TextView land_mu = viewHolder.getView (R.id.land_mu);

        if ( !Tools.isEmpty (bean.getPic1 ()) )
        {
            Bitmap bm = getimage (bean.getPic1 ());
            land_img.setImageBitmap (bm);
        }
        else{
            land_img.setImageResource (R.drawable.asd);
        }

        land_name.setText (bean.getLandName ());
        land_type.setText (bean.getLandType ());
        land_feature.setText (bean.getLandFeature ());
        soil_type.setText (bean.getSoilType ());
        tobacco_breed.setText (bean.getTobaccoBreed ());
        land_year.setText ("启用年份" + bean.getSYear ());
        land_mu.setText(bean.getLandArea()+"亩");

    }

    private Bitmap getimage (String srcPath)
    {
        BitmapFactory.Options newOpts = new BitmapFactory.Options ();
        //开始读入图片，此时把options.inJustDecodeBounds 设回true了
        newOpts.inJustDecodeBounds = true;
        Bitmap bitmap = BitmapFactory.decodeFile (srcPath, newOpts);//此时返回bm为空
        newOpts.inJustDecodeBounds = false;
        int w = newOpts.outWidth;
        int h = newOpts.outHeight;
        //现在主流手机比较多是800*480分辨率，所以高和宽我们设置为
        float hh = 800f;//这里设置高度为800f
        float ww = 480f;//这里设置宽度为480f
        //缩放比。由于是固定比例缩放，只用高或者宽其中一个数据进行计算即可
        int be = 1;//be=1表示不缩放
        if ( w > h && w > ww )
        {//如果宽度大的话根据宽度固定大小缩放
            be = (int) (newOpts.outWidth / ww);
        } else if ( w < h && h > hh )
        {//如果高度高的话根据宽度固定大小缩放
            be = (int) (newOpts.outHeight / hh);
        }
        if ( be <= 0 )
            be = 1;
        newOpts.inSampleSize = be;//设置缩放比例
        //重新读入图片，注意此时已经把options.inJustDecodeBounds 设回false了
        bitmap = BitmapFactory.decodeFile (srcPath, newOpts);
        return bitmap;//压缩好比例大小后再进行质量压缩
    }

//    private Bitmap compressImage (Bitmap image)
//    {
//        ByteArrayOutputStream baos = new ByteArrayOutputStream ();
//        image.compress (Bitmap.CompressFormat.JPEG, 10, baos);//质量压缩方法，这里100表示不压缩，把压缩后的数据存放到baos中
//        int options = 100;
//        while (baos.toByteArray ().length / 1024 > 100)
//        {  //循环判断如果压缩后图片是否大于100kb,大于继续压缩
//            baos.reset ();//重置baos即清空baos
//            image.compress (Bitmap.CompressFormat.JPEG, options, baos);//这里压缩options%，把压缩后的数据存放到baos中
//            options -= 10;//每次都减少10
//        }
//        ByteArrayInputStream isBm = new ByteArrayInputStream (baos.toByteArray ());//把压缩后的数据baos存放到ByteArrayInputStream中
//        Bitmap bitmap = BitmapFactory.decodeStream (isBm, null, null);//把ByteArrayInputStream数据生成图片
//        return bitmap;
//    }
}
