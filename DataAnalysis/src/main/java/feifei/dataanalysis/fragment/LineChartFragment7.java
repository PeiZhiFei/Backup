
package feifei.dataanalysis.fragment;

import android.graphics.Color;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.github.mikephil.charting.charts.LineChart;
import com.github.mikephil.charting.components.Legend;
import com.github.mikephil.charting.components.Legend.LegendForm;
import com.github.mikephil.charting.components.Legend.LegendPosition;
import com.github.mikephil.charting.components.XAxis;
import com.github.mikephil.charting.components.YAxis;
import com.github.mikephil.charting.components.YAxis.AxisDependency;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.data.LineData;
import com.github.mikephil.charting.data.LineDataSet;
import com.github.mikephil.charting.utils.ColorTemplate;

import java.util.ArrayList;

import feifei.dataanalysis.R;

public class LineChartFragment7 extends MainBaseFragment
{

    public static LineChartFragment7 newInstance ()
    {
        LineChartFragment7 cf = new LineChartFragment7 ();
        return cf;
    }

    private LineChart mChart;

    @Override
    public View onCreateView (LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState)
    {
        super.onCreateView (inflater, container, savedInstanceState);

        View view = inflater.inflate (R.layout.fragment_linechart, container, false);

        mChart = (LineChart) view.findViewById (R.id.chart1);
        mChart.setDescription ("");
        mChart.setNoDataTextDescription ("暂无数据");

        mChart.setHighlightEnabled (true);
        mChart.setTouchEnabled (false);
        mChart.setDragDecelerationFrictionCoef (0.9f);
        mChart.setDragEnabled (false);
        mChart.setScaleEnabled (true);
        mChart.setDrawGridBackground (false);
        mChart.setHighlightPerDragEnabled (true);
        mChart.setPinchZoom (true);

        //设置偏移量
        mChart.setExtraOffsets (20f, 0, 20f, 0);
        setData ();
        mChart.animateXY (2000, 2000);

        // Typeface tf = Typeface.createFromAsset(getActivity ().getAssets(), "OpenSans-Regular.ttf");

        // 设置数据之后才可用，设置坐标
        Legend l = mChart.getLegend ();

        //设置标注
        l.setForm (LegendForm.CIRCLE);
        l.setTextSize (12f);
        l.setTextColor (Color.DKGRAY);
        l.setPosition (LegendPosition.BELOW_CHART_CENTER);
        l.setYOffset (10f);

        //顶部标注
        XAxis xAxis = mChart.getXAxis ();
        xAxis.setTextSize (12f);
        xAxis.setTextColor (getResources ().getColor (R.color.orange));
        xAxis.setDrawGridLines (false);
        xAxis.setDrawAxisLine (false);
        //每一个都显示
        xAxis.setSpaceBetweenLabels (1);
        xAxis.setXOffset (20);
        xAxis.setYOffset (20);

        YAxis leftAxis = mChart.getAxisLeft ();
        leftAxis.setDrawLabels (false);
        //        leftAxis.setTextColor (ColorTemplate.getHoloBlue ());
        //        leftAxis.setTextSize (8);
        float x = getMax (orderCount7);
        leftAxis.setAxisMaxValue (2 * x);
        //        leftAxis.setAxisMinValue (-200);
        //顶部最大值不显示
        //        leftAxis.setDrawTopYLabelEntry (false);
        leftAxis.setDrawAxisLine (false);
        leftAxis.setDrawGridLines (false);


        YAxis rightAxis = mChart.getAxisRight ();
        rightAxis.setDrawLabels (false);
        //        rightAxis.setTextColor (Color.parseColor ("#ff0000"));
        //        rightAxis.setTextSize (8);
        float y = getMax (orderMoney7);
        rightAxis.setAxisMaxValue (1.2f * y);
        //        rightAxis.setStartAtZero(false);
        //        rightAxis.setAxisMinValue (-200);
        rightAxis.setDrawGridLines (false);
        //        rightAxis.setDrawTopYLabelEntry (false);
        rightAxis.setDrawAxisLine (false);

        return view;
    }

    //            case R.id.actionToggleValues: {
    //                for (DataSet<?> set : mChart.getData().getDataSets())
    //                    set.setDrawValues(!set.isDrawValuesEnabled());
    //                mChart.invalidate();
    //                break;
    //            }
    //            case R.id.actionToggleFilled: {
    //                ArrayList<LineDataSet> sets = (ArrayList<LineDataSet>) mChart.getData()
    //                        .getDataSets();
    //                for (LineDataSet set : sets) {
    //                    if (set.isDrawFilledEnabled())
    //                        set.setDrawFilled(false);
    //                    else
    //                        set.setDrawFilled(true);
    //                }
    //                mChart.invalidate();
    //                break;
    //            }
    //            case R.id.actionToggleFilter: {
    //                // the angle of filtering is 35°
    //                Approximator a = new Approximator (ApproximatorType.DOUGLAS_PEUCKER, 35);
    //                if (!mChart.isFilteringEnabled()) {
    //                    mChart.enableFiltering(a);
    //                } else {
    //                    mChart.disableFiltering();
    //                }
    //                mChart.invalidate();
    //                break;
    //            }


    private void setData ()
    {
        String[] orderTime77 = sortN (orderTime7);
        float[] orderCount77 = sortN (orderCount7);
        float[] orderMoney77 = sortN (orderMoney7);

        ArrayList<String> xVals = new ArrayList<> ();
        ArrayList<Entry> yVals1 = new ArrayList<> ();
        ArrayList<Entry> yVals2 = new ArrayList<> ();

        //        for (int i = orderTime7.length - 1; i >= 0; i--)

        for (int i = 0; i < orderTime77.length; i++)
        {
            xVals.add (orderTime77[i]);
            yVals1.add (new Entry (orderCount77[i], i));
            yVals2.add (new Entry (orderMoney77[i], i));
        }

        LineDataSet set1 = new LineDataSet (yVals1, "订单数");
        set1.setAxisDependency (AxisDependency.LEFT);
        set1.setColor (ColorTemplate.getHoloBlue ());
        set1.setCircleColor (ColorTemplate.getHoloBlue ());

        //        set1.setCircleColor(ColorTemplate.getHoloBlue());
        //        set1.setCircleColor(Color.WHITE);

        set1.setLineWidth (2f);
        set1.setCircleSize (3f);
        set1.setFillAlpha (65);
        set1.setDrawCubic (true);
        set1.setFillColor (ColorTemplate.getHoloBlue ());
        set1.setDrawCircleHole (false);

        //        set1.setDrawHorizontalHighlightIndicator(false);
        //        set1.setVisible(false);
        //        set1.setCircleHoleColor(Color.WHITE);

        LineDataSet set2 = new LineDataSet (yVals2, "订单金额");
        set2.setAxisDependency (AxisDependency.RIGHT);
        set2.setColor (Color.RED);
        set2.setCircleColor (getResources ().getColor (R.color.red));
        set2.setLineWidth (2f);
        set2.setCircleSize (3f);
        set2.setDrawCubic (true);
        set2.setFillAlpha (65);
        set2.setFillColor (Color.RED);
        set2.setDrawCircleHole (false);

        ArrayList<LineDataSet> dataSets = new ArrayList<> ();
        dataSets.add (set2);
        dataSets.add (set1);

        LineData data = new LineData (xVals, dataSets);
        data.setValueTextColor (getResources ().getColor (R.color.main_green));
        data.setValueTextSize (11f);

        mChart.setData (data);
    }

}
