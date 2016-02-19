
package feifei.dataanalysis.fragment;

import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;
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
import com.github.mikephil.charting.highlight.Highlight;
import com.github.mikephil.charting.listener.OnChartValueSelectedListener;
import com.github.mikephil.charting.utils.ColorTemplate;

import java.util.ArrayList;

import feifei.dataanalysis.R;

public class LineChartFragment30 extends MainBaseFragment implements
        OnChartValueSelectedListener
{

    public static LineChartFragment30 newInstance()
    {
        LineChartFragment30 cf = new LineChartFragment30();
        return cf;
    }

    private LineChart mChart;
//    protected float[] orderCount7 = new float[]{50, 60, 78, 87, 34, 54, 26};

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState)
    {
        super.onCreateView(inflater, container, savedInstanceState);

        View view = inflater.inflate(R.layout.fragment_linechart, container, false);

        mChart = (LineChart) view.findViewById(R.id.chart1);
        mChart.setOnChartValueSelectedListener(this);

        // no description text
        mChart.setDescription("");
        mChart.setNoDataTextDescription("请提供数据");

        // enable value highlighting
        mChart.setHighlightEnabled(true);

        // enable touch gestures
        mChart.setTouchEnabled(false);

        mChart.setDragDecelerationFrictionCoef(0.9f);

        // enable scaling and dragging
        mChart.setDragEnabled(true);
        mChart.setScaleEnabled(true);
        mChart.setDrawGridBackground(false);
        mChart.setHighlightPerDragEnabled(true);


        // if disabled, scaling can be done on x- and y-axis separately
        mChart.setPinchZoom(true);

        // set an alternative background color
//        mChart.setBackgroundColor(Color.parseColor ("#ffebee"));

        setData();
        //设置偏移量
//        mChart.setExtraOffsets (20f, 0, 20f, 0);
        mChart.animateXY(2000, 2000);

        Legend l = mChart.getLegend();


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
//        xAxis.setSpaceBetweenLabels (1);
        xAxis.setXOffset (20);
        xAxis.setYOffset (20);


        YAxis leftAxis = mChart.getAxisLeft ();
        leftAxis.setTextColor (ColorTemplate.getHoloBlue ());
        float x = getMax(orderCount30);
        leftAxis.setAxisMaxValue (2 * x);
        leftAxis.setDrawGridLines (false);
        //最顶部的值是否要
        leftAxis.setDrawTopYLabelEntry (false);
        leftAxis.setDrawAxisLine (false);
        leftAxis.setStartAtZero (true);
        leftAxis.setAxisMinValue (-200);

        YAxis rightAxis = mChart.getAxisRight();
        rightAxis.setTextColor(Color.parseColor ("#ff0000"));
        float y = getMax(orderMoney30);
        rightAxis.setAxisMaxValue(1.2f * y);
        rightAxis.setDrawGridLines (false);
        rightAxis.setDrawTopYLabelEntry (false);
        rightAxis.setDrawAxisLine (false);
        leftAxis.setStartAtZero (true);
        leftAxis.setAxisMinValue (-1000);
        return view;
    }

//            case R.id.actionToggleValues: {
//                for (DataSet<?> set : mChart.getData().getDataSets())
//                    set.setDrawValues(!set.isDrawValuesEnabled());
//
//                mChart.invalidate();
//                break;
//            }
//            case R.id.actionToggleHighlight: {
//                if (mChart.isHighlightEnabled())
//                    mChart.setHighlightEnabled(false);
//                else
//                    mChart.setHighlightEnabled(true);
//                mChart.invalidate();
//                break;
//            }
//            case R.id.actionToggleFilled: {
//
//                ArrayList<LineDataSet> sets = (ArrayList<LineDataSet>) mChart.getData()
//                        .getDataSets();
//
//                for (LineDataSet set : sets) {
//                    if (set.isDrawFilledEnabled())
//                        set.setDrawFilled(false);
//                    else
//                        set.setDrawFilled(true);
//                }
//                mChart.invalidate();
//                break;
//            }
//            case R.id.actionToggleCircles: {
//                ArrayList<LineDataSet> sets = (ArrayList<LineDataSet>) mChart.getData()
//                        .getDataSets();
//
//                for (LineDataSet set : sets) {
//                    if (set.isDrawCirclesEnabled())
//                        set.setDrawCircles(false);
//                    else
//                        set.setDrawCircles(true);
//                }
//                mChart.invalidate();
//                break;
//            }
//            case R.id.actionToggleCubic: {
//                ArrayList<LineDataSet> sets = (ArrayList<LineDataSet>) mChart.getData()
//                        .getDataSets();
//
//                for (LineDataSet set : sets) {
//                    if (set.isDrawCubicEnabled())
//                        set.setDrawCubic(false);
//                    else
//                        set.setDrawCubic(true);
//                }
//                mChart.invalidate();
//                break;
//            }
//            case R.id.actionToggleStartzero: {
//                mChart.getAxisLeft().setStartAtZero(!mChart.getAxisLeft().isStartAtZeroEnabled());
//                mChart.getAxisRight().setStartAtZero(!mChart.getAxisRight().isStartAtZeroEnabled());
//                mChart.invalidate();
//                break;
//            }
//            case R.id.actionTogglePinch: {
//                if (mChart.isPinchZoomEnabled())
//                    mChart.setPinchZoom(false);
//                else
//                    mChart.setPinchZoom(true);
//
//                mChart.invalidate();
//                break;
//            }
//            case R.id.actionToggleAutoScaleMinMax: {
//                mChart.setAutoScaleMinMaxEnabled(!mChart.isAutoScaleMinMaxEnabled());
//                mChart.notifyDataSetChanged();
//                break;
//            }
//            case R.id.actionToggleFilter: {
//                // the angle of filtering is 35°
//                Approximator a = new Approximator (ApproximatorType.DOUGLAS_PEUCKER, 35);
//
//                if (!mChart.isFilteringEnabled()) {
//                    mChart.enableFiltering(a);
//                } else {
//                    mChart.disableFiltering();
//                }
//                mChart.invalidate();
//                break;
//            }
//            case R.id.actionSave: {
//                if (mChart.saveToPath("title" + System.currentTimeMillis(), "")) {
//                    Toast.makeText(getApplicationContext(), "Saving SUCCESSFUL!",
//                            Toast.LENGTH_SHORT).show();
//                } else
//                    Toast.makeText(getApplicationContext(), "Saving FAILED!", Toast.LENGTH_SHORT)
//                            .show();
//
//                // mChart.saveToGallery("title"+System.currentTimeMillis())
//                break;
//            }


    private void setData()
    {
        String[] orderTime303 = sortN(orderTime30);
        float[] orderCount303 = sortN(orderCount30);
        float[] orderMoney303 = sortN(orderMoney30);

        ArrayList<String> xVals = new ArrayList<>();
        ArrayList<Entry> yVals1 = new ArrayList<>();
        ArrayList<Entry> yVals2 = new ArrayList<>();

        for (int i = 0; i < orderTime303.length; i++)
        {
            xVals.add(orderTime303[i]);
            yVals1.add(new Entry(orderCount303[i], i));
            yVals2.add(new Entry(orderMoney303[i], i));
        }

        LineDataSet set1 = new LineDataSet(yVals1, "订单数");
        set1.setAxisDependency(AxisDependency.LEFT);
        set1.setColor(ColorTemplate.getHoloBlue());
        set1.setCircleColor(getResources().getColor(R.color.pink));
//        set1.setCircleColor(Color.WHITE);
        set1.setLineWidth(2f);
        set1.setCircleSize(3f);
        set1.setValueTextSize(20);
        set1.setFillAlpha(65);
        set1.setFillColor(ColorTemplate.getHoloBlue());
        set1.setHighLightColor(Color.rgb(244, 117, 117));
        set1.setDrawCircleHole(false);

        // TODO: 15-9-6
        set1.setDrawValues(false);
        set1.setDrawCubic(true);
        set1.setDrawCircles(false);
//        set1.setDrawHorizontalHighlightIndicator(false);
//        set1.setVisible(false);
//        set1.setCircleHoleColor(Color.WHITE);

        LineDataSet set2 = new LineDataSet(yVals2, "订单金额");
        set2.setAxisDependency(AxisDependency.RIGHT);
        set2.setColor(Color.RED);
        set2.setCircleColor(getResources().getColor(R.color.pink));
//        set2.setCircleColor(Color.WHITE);
        set2.setLineWidth(2f);
        set2.setCircleSize(3f);
        set2.setFillAlpha(65);
        set2.setValueTextSize(15);
        set2.setFillColor(Color.RED);
        set2.setDrawCircleHole(false);
        set2.setHighLightColor(Color.rgb(244, 117, 117));
        // TODO: 15-9-6
        set2.setDrawValues(false);
        set2.setDrawCubic(true);
        set2.setDrawCircles(false);

        ArrayList<LineDataSet> dataSets = new ArrayList<>();
        dataSets.add(set1);
        dataSets.add(set2); // add the datasets

        // create a data object with the datasets
        LineData data = new LineData(xVals, dataSets);
        data.setValueTextColor(Color.DKGRAY);
        data.setValueTextSize(9f);

        // set data
        mChart.setData(data);
    }

    @Override
    public void onValueSelected(Entry e, int dataSetIndex, Highlight h)
    {
        Log.i("Entry selected", e.toString());
    }

    @Override
    public void onNothingSelected()
    {
        Log.i("Nothing selected", "Nothing selected.");
    }


}
