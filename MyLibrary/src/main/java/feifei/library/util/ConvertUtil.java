package feifei.library.util;

import android.graphics.Bitmap;
import android.graphics.Bitmap.CompressFormat;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.util.Base64;

import java.io.BufferedReader;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.InputStream;
import java.io.InputStreamReader;

/**
 * @author 裴智飞
 * @date 2014-7-13
 * @date 下午3:43:59
 * @file ConvertUtil.java
 * @content 各种类型之间的转换
 */
public class ConvertUtil
{

    /**
     * @param string
     * @return
     * @notice Base64String转为byte[]
     */
    public static byte[] stringToByte(String string)
    {
        byte[] byteArray = Base64.decode(string, Base64.DEFAULT);
        return byteArray;
    }

    /**
     * @param byteArray
     * @return
     * @notice byte[]转为Base64String
     */
    public static String byteToString(byte[] byteArray)
    {
        String string = Base64.encodeToString(byteArray, Base64.DEFAULT);
        return string;
    }

    /**
     * @param byteArray
     * @return
     * @notice byte[]转Bitmap
     */
    public static Bitmap byte2Bitmap(byte[] byteArray)
    {
        if (byteArray.length != 0)
        {
            return BitmapFactory
                    .decodeByteArray(byteArray, 0, byteArray.length);
        }
        return null;
    }

    /**
     * @param byteArray
     * @return
     * @notice 将byte[]转InputStream
     */
    public static InputStream byte2Stream(byte[] byteArray)
    {
        ByteArrayInputStream bais = new ByteArrayInputStream(byteArray);
        return bais;
    }

    /**
     * @param in
     * @return
     * @notice InputStream转String，默认为UTF-8
     */
    public static String stream2String(InputStream in)
    {
        try
        {
            BufferedReader br = new BufferedReader(new InputStreamReader(in,
                    "utf-8"));
            StringBuffer strBuffer = new StringBuffer();
            String line;
            while ((line = br.readLine()) != null)
            {
                strBuffer.append(line + "\n");
            }
            br.close();
            return strBuffer.toString();
        } catch (Exception e)
        {
            e.printStackTrace();
        }
        return null;
    }

    /**
     * @param in
     * @return
     * @notice 将InputStream转换成byte[]
     */
    public static byte[] stream2Byte(InputStream in)
    {
        String str = "";
        byte[] readByte = new byte[1024];
        try
        {
            while ((in.read(readByte, 0, 1024)) != -1)
            {
                str += new String(readByte).trim();
            }
            return str.getBytes();
        } catch (Exception e)
        {
            e.printStackTrace();
        }
        return null;
    }

    /**
     * @param in
     * @return
     * @notice 将InputStream转换成Bitmap
     */
    public static Bitmap stream2Bitmap(InputStream in)
    {
        return BitmapFactory.decodeStream(in);
    }

    /**
     * @param bitmap
     * @return
     * @notice 将Bitmap转换成InputStream
     */
    public static InputStream bitmap2Stream(Bitmap bitmap)
    {
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        bitmap.compress(CompressFormat.JPEG, 100, baos);
        InputStream is = new ByteArrayInputStream(baos.toByteArray());
        return is;
    }

    /**
     * @param bitmap
     * @return
     * @notice 将Bitmap转换成String
     */
    public static String bitmap2String(Bitmap bitmap)
    {
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        bitmap.compress(CompressFormat.PNG, 100, baos);
        byte[] appicon = baos.toByteArray();
        return Base64.encodeToString(appicon, Base64.DEFAULT);
    }

    /**
     * @param bm
     * @return
     * @notice Bitmap转换成byte[]
     */
    public static byte[] bitmap2Byte(Bitmap bm)
    {
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        bm.compress(CompressFormat.PNG, 100, baos);
        return baos.toByteArray();
    }

    /**
     * @param string
     * @return
     * @notice String转成bitmap
     */
    public static Bitmap stringToBitmap(String string)
    {
        Bitmap bitmap = null;
        try
        {
            byte[] bitmapArray;
            bitmapArray = Base64.decode(string, Base64.DEFAULT);
            bitmap = BitmapFactory.decodeByteArray(bitmapArray, 0,
                    bitmapArray.length);
            return bitmap;
        } catch (Exception e)
        {
            return null;
        }
    }

    /**
     * @param string
     * @return
     * @notice String转InputStream
     */
    public static InputStream string2Stream(String string)
    {
        if (string != null && !string.trim().equals(""))
        {
            try
            {
                ByteArrayInputStream tInputStringStream = new ByteArrayInputStream(
                        string.getBytes());
                return tInputStringStream;
            } catch (Exception e)
            {
            }
        }
        return null;
    }

    /**
     * @param byteArray
     * @return
     * @notice byte[]转Drawable
     */
    public static Drawable byte2Drawable(byte[] byteArray)
    {
        Bitmap bitmap = byte2Bitmap(byteArray);
        return bitmap2Drawable(bitmap);
    }

    /**
     * @param in
     * @return
     * @notice InputStream转换成Drawable
     */
    public static Drawable stream2Drawable(InputStream in)
    {
        Bitmap bitmap = stream2Bitmap(in);
        return bitmap2Drawable(bitmap);
    }

    /**
     * @param bitmap
     * @return
     * @notice Bitmap转换成Drawable
     */
    public static Drawable bitmap2Drawable(Bitmap bitmap)
    {
        return bitmap == null ? null : new BitmapDrawable(bitmap);
    }

    /**
     * @param drawable
     * @return
     * @notice Drawable转换成Bitmap
     */
    public static Bitmap drawable2Bitmap(Drawable drawable)
    {
        return drawable == null ? null : ((BitmapDrawable) drawable).getBitmap();
    }

    /**
     * @param drawable
     * @return
     * @notice Drawable转换成byte[]
     */
    public static byte[] drawable2Byte(Drawable drawable)
    {
        return bitmap2Byte(drawable2Bitmap(drawable));
    }

    /**
     * @param drawable
     * @return
     * @notice Drawable转换成InputStream
     */
    public static InputStream drawable2Stream(Drawable drawable)
    {
        Bitmap bitmap = drawable2Bitmap(drawable);
        return bitmap2Stream(bitmap);
    }

}
