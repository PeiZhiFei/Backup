
package feifei.dataanalysis.fragment;

import android.annotation.SuppressLint;
import android.graphics.PointF;
import android.graphics.RectF;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.github.mikephil.charting.charts.HorizontalBarChart;
import com.github.mikephil.charting.components.Legend;
import com.github.mikephil.charting.components.Legend.LegendPosition;
import com.github.mikephil.charting.components.XAxis;
import com.github.mikephil.charting.components.XAxis.XAxisPosition;
import com.github.mikephil.charting.components.YAxis;
import com.github.mikephil.charting.data.BarData;
import com.github.mikephil.charting.data.BarDataSet;
import com.github.mikephil.charting.data.BarEntry;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.highlight.Highlight;
import com.github.mikephil.charting.listener.OnChartValueSelectedListener;

import java.util.ArrayList;

import feifei.dataanalysis.R;

public class HorizontalFragmentMoney extends MainBaseFragment implements OnChartValueSelectedListener {
    public static HorizontalFragmentMoney newInstance() {
        HorizontalFragmentMoney cf = new HorizontalFragmentMoney();
        return cf;
    }

    protected HorizontalBarChart mChart;

//    private Typeface tf;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        super.onCreateView(inflater, container, savedInstanceState);
        View view = inflater.inflate(R.layout.fragment_horizontal, container, false);
        TextView button = (TextView) view.findViewById(R.id.button);
        float sum = 0;
        for (int i = 0; i < orderMoney30.length; i++) {
            sum += orderMoney30[i];
        }
        button.setText("30天营业总额    " + sum);
        mChart = (HorizontalBarChart) view.findViewById(R.id.chart1);
        mChart.setOnChartValueSelectedListener(this);
//         mChart.setHighlightEnabled(false);
        mChart.setDrawBarShadow(false);
        mChart.setDrawValueAboveBar(true);
        mChart.setDescription("");
        mChart.setDrawMarkerViews(false);
        mChart.setExtraOffsets(0, 10, 20, 0);
        // if more than 60 entries are displayed in the chart, no values will be
        // drawn
        mChart.setMaxVisibleValueCount(300);
        // scaling can now only be done on x- and y-axis separately
        mChart.setPinchZoom(false);
        // draw shadows for each bar that show the maximum value
        // mChart.setDrawBarShadow(true);
        // mChart.setDrawXLabels(false);
        mChart.setDrawGridBackground(false);

        // mChart.setDrawYLabels(false);

        XAxis xl = mChart.getXAxis();
        xl.setPosition(XAxisPosition.BOTTOM);
        xl.setDrawAxisLine(true);
        xl.setAxisLineColor(getResources().getColor(R.color.pink));
        xl.setDrawGridLines(false);
        xl.setGridLineWidth(0.3f);
        xl.setTextColor(getResources().getColor(R.color.pink));
        xl.setTextSize(12);

        YAxis yl = mChart.getAxisLeft();
        yl.setDrawAxisLine(true);
        yl.setAxisLineColor(getResources().getColor(R.color.orange));
        yl.setDrawGridLines(false);
        yl.setGridLineWidth(0.3f);
        yl.setTextSize(12);
        yl.setDrawTopYLabelEntry(false);
        yl.setYOffset(10);
        yl.setTextColor(getResources().getColor(R.color.orange));
        //只显示最大和最小
        yl.setShowOnlyMinMax(true);
        yl.setDrawTopYLabelEntry(true);

        YAxis yr = mChart.getAxisRight();
        yr.setDrawAxisLine(true);
        yr.setAxisLineColor(getResources().getColor(R.color.orange));
        yr.setDrawGridLines(false);
        yr.setDrawTopYLabelEntry(false);
        yr.setTextSize(12);
        yr.setTextColor(getResources().getColor(R.color.orange));
        //设置反向 yr.setInverted(true);
        //只显示最大和最小
        yr.setShowOnlyMinMax(true);
        yr.setDrawTopYLabelEntry(true);

        setData();
        mChart.animateY(2500);

        Legend l = mChart.getLegend();
        l.setPosition(LegendPosition.BELOW_CHART_CENTER);
        l.setFormSize(10f);
        l.setXEntrySpace(4f);

        // mChart.setDrawLegend(false);
        return view;
    }

    private void setData() {

        ArrayList<BarEntry> yVals1 = new ArrayList<>();
        ArrayList<String> xVals = new ArrayList<>();

        for (int i = orderTime30.length - 1; i >= 0; i--) {
            xVals.add(orderTime30[i]);
            //todo
            yVals1.add(new BarEntry(orderMoney30[i], Math.abs(orderTime30.length - 1 - i)));
        }

//        for (int i = 0; i < orderTime30.length; i++)
//        {
//            yVals1.add(new BarEntry(orderMoney30[i], i));
//        }

        BarDataSet set1 = new BarDataSet(yVals1, "营业额");

        ArrayList<BarDataSet> dataSets = new ArrayList<>();
        dataSets.add(set1);

        BarData data = new BarData(xVals, dataSets);
        data.setValueTextSize(12f);
        data.setValueTextColor(getResources().getColor(R.color.deep_red));

        mChart.setData(data);
    }

    @SuppressLint("NewApi")
    @Override
    public void onValueSelected(Entry e, int dataSetIndex, Highlight h) {

        if (e == null)
            return;

        RectF bounds = mChart.getBarBounds((BarEntry) e);
        PointF position = mChart.getPosition(e, mChart.getData().getDataSetByIndex(dataSetIndex)
                .getAxisDependency());

        Log.i("bounds", bounds.toString());
        Log.i("position", position.toString());
    }

    public void onNothingSelected() {
    }

    //            case R.id.actionToggleAutoScaleMinMax: {
    //                mChart.setAutoScaleMinMaxEnabled(!mChart.isAutoScaleMinMaxEnabled());
    //                mChart.notifyDataSetChanged();
    //            case R.id.actionToggleFilter: {
    //                Approximator a = new Approximator (ApproximatorType.DOUGLAS_PEUCKER, 25);
    //
    //                if (!mChart.isFilteringEnabled()) {
    //                    mChart.enableFiltering(a);
    //                } else {
    //                    mChart.disableFiltering();
    //                }
    //                mChart.invalidate();
}
