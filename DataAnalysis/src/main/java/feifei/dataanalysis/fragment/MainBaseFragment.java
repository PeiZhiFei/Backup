package feifei.dataanalysis.fragment;

import android.app.Activity;

import java.util.Arrays;

import feifei.dataanalysis.activity.MainActivity;
import feifei.dataanalysis.activity.SearchActivity;
import feifei.dataanalysis.activity.StoreInfoActivity;
import feifei.dataanalysis.base.BaseFragment;

public class MainBaseFragment extends BaseFragment

{
    protected String[] orderTime7;
    protected String[] orderTime30;
    protected float[] orderCount7;
    protected float[] orderMoney7;
    protected float[] orderCount30;
    protected float[] orderMoney30;

    @Override
    public void onAttach(Activity activity)
    {
        super.onAttach(activity);
        if (activity instanceof MainActivity)
        {
            orderTime7 = ((MainActivity) activity).orderTime7;
            orderTime30 = ((MainActivity) activity).orderTime30;
            orderCount7 = ((MainActivity) activity).orderCount7;
            orderMoney7 = ((MainActivity) activity).orderMoney7;
            orderCount30 = ((MainActivity) activity).orderCount30;
            orderMoney30 = ((MainActivity) activity).orderMoney30;
        } else if (activity instanceof SearchActivity)
        {
            orderTime7 = ((SearchActivity) activity).orderTime7;
            orderTime30 = ((SearchActivity) activity).orderTime30;
            orderCount7 = ((SearchActivity) activity).orderCount7;
            orderMoney7 = ((SearchActivity) activity).orderMoney7;
            orderCount30 = ((SearchActivity) activity).orderCount30;
            orderMoney30 = ((SearchActivity) activity).orderMoney30;
        }
        else if (activity instanceof StoreInfoActivity)
        {
            orderTime7 = ((StoreInfoActivity) activity).orderTime7;
            orderTime30 = ((StoreInfoActivity) activity).orderTime30;
            orderCount7 = ((StoreInfoActivity) activity).orderCount7;
            orderMoney7 = ((StoreInfoActivity) activity).orderMoney7;
            orderCount30 = ((StoreInfoActivity) activity).orderCount30;
            orderMoney30 = ((StoreInfoActivity) activity).orderMoney30;
        }

    }


    public static int getMax(int[] arr)
    {
        int max = arr[0];
        for (int x = 1; x < arr.length; x++)
        {
            if (arr[x] > max)
                max = arr[x];
        }
        return max;
    }

    public static float getMax(float[] arr)
    {
        float max = arr[0];
        for (int x = 1; x < arr.length; x++)
        {
            if (arr[x] > max)
                max = arr[x];
        }
        return max;
    }

    public int[] sortN(int[] a)
    {
        int[] b = new int[a.length];
        Arrays.sort(a);
        for (int i = 0; i < a.length; i++)
        {
            b[i] = a[a.length - i - 1];
        }
        return b;
    }

//    public float[] sortN(float[] a)
//    {
//        float[] b = new float[a.length];
//        Arrays.sort(a);
//        for (int i = 0; i < a.length; i++)
//        {
//            b[i] = a[a.length - i - 1];
//        }
//        return b;
//    }
//
//    public String[] sortN(String[] a)
//    {
//        String[] b = new String[a.length];
//        Arrays.sort(a);
//        for (int i = 0; i < a.length; i++)
//        {
//            b[i] = a[a.length - i - 1];
//        }
//        return b;
//    }

    public float[] sortN(float[] a)
    {
        if (a != null)
        {
            float[] b = new float[a.length];
            for (int i = 0; i < a.length; i++)
            {
                b[i] = a[a.length - i - 1];
            }
            return b;
        }
        return null;
    }

    public String[] sortN(String[] a)
    {
        if (a != null)
        {
            String[] b = new String[a.length];
            for (int i = 0; i < a.length; i++)
            {
                b[i] = a[a.length - i - 1];
            }
            return b;
        }
        return null;
    }


}
