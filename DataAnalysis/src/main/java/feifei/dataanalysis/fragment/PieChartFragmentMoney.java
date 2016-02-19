
package feifei.dataanalysis.fragment;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.github.mikephil.charting.charts.PieChart;
import com.github.mikephil.charting.components.Legend;
import com.github.mikephil.charting.components.Legend.LegendPosition;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.data.PieData;
import com.github.mikephil.charting.data.PieDataSet;
import com.github.mikephil.charting.utils.ColorTemplate;
import com.github.mikephil.charting.utils.PercentFormatter;
import com.github.mikephil.charting.utils.ValueFormatter;

import java.util.ArrayList;

import feifei.dataanalysis.R;


public class PieChartFragmentMoney extends MainBaseFragment
{
    public static PieChartFragmentMoney newInstance()
    {
        PieChartFragmentMoney cf = new PieChartFragmentMoney();
        return cf;
    }

    private PieChart mChart;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState)
    {
        super.onCreateView(inflater, container, savedInstanceState);

        View view = inflater.inflate(R.layout.fragment_piechart, container, false);
        TextView button = (TextView) view.findViewById(R.id.button);
        button.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {

                mChart.setUsePercentValues(!mChart.isUsePercentValuesEnabled());
                if (mChart.isUsePercentValuesEnabled())
                {
                    data.setValueFormatter(new PercentFormatter());
                } else
                {
                    data.setValueFormatter(new ValueFormatter()
                    {
                        @Override
                        public String getFormattedValue(float v)
                        {
                            return String.valueOf((int) v);
                        }
                    });
                }
                mChart.invalidate();
            }
        });

        float sum = 0;
        for (int i = 0; i < orderMoney7.length; i++)
        {
            sum += orderMoney7[i];
        }
        button.setText("7日营业总额    " + sum);
        mChart = (PieChart) view.findViewById(R.id.chart1);
        mChart.setUsePercentValues(true);
        mChart.setDescription("");
        mChart.setNoDataText("暂无数据");
        mChart.setDragDecelerationFrictionCoef(0.95f);

        mChart.setDrawHoleEnabled(true);
//        mChart.setHoleColorTransparent(true);
        mChart.setCenterTextColor(getResources().getColor(R.color.red));
//        mChart.setTransparentCircleColor(Color.WHITE);
//        mChart.setTransparentCircleAlpha(110);

        mChart.setHoleRadius(40f);
        mChart.setTransparentCircleRadius(45f);
        mChart.setDrawCenterText(true);
        mChart.setRotationAngle(0);
        // enable rotation of the chart by touch
        mChart.setRotationEnabled(false);
        //切换百分比
        mChart.setUsePercentValues(false);
        mChart.setExtraLeftOffset(10);

        // mChart.setUnit(" €");
        // mChart.setDrawUnitsInChart(true);

        mChart.setCenterTextColor(getResources ().getColor (R.color.main_green));
        mChart.setCenterTextSize(15f);

        setData();

//        mChart.animateY (1500, Easing.EasingOption.EaseInOutQuad);
        mChart.animateXY(1800, 1800);
        // mChart.spin(2000, 0, 360);

        Legend l = mChart.getLegend();
        l.setPosition(LegendPosition.BELOW_CHART_CENTER);
        l.setXEntrySpace(7f);
        l.setYEntrySpace(0f);
        l.setYOffset(5f);
        l.setXOffset(5f);

        return view;
    }


//            case R.id.actionToggleValues:
//            {
//                for (DataSet<?> set : mChart.getData ().getDataSets ())
//                    set.setDrawValues (!set.isDrawValuesEnabled ());
//                mChart.invalidate ();
//                break;
//            }
//            case R.id.actionToggleHole:
//            {
//                if ( mChart.isDrawHoleEnabled () )
//                    mChart.setDrawHoleEnabled (false);
//                else
//                    mChart.setDrawHoleEnabled (true);
//                mChart.invalidate ();
//                break;
//            }
//            case R.id.actionDrawCenter:
//            {
//                if ( mChart.isDrawCenterTextEnabled () )
//                    mChart.setDrawCenterText (false);
//                else
//                    mChart.setDrawCenterText (true);
//                mChart.invalidate ();
//                break;
//            }
//            case R.id.actionToggleXVals:
//            {
//                mChart.setDrawSliceText (!mChart.isDrawSliceTextEnabled ());
//                mChart.invalidate ();
//                break;
//            }
//            case R.id.actionSave:
//            {
//                // mChart.saveToGallery("title"+System.currentTimeMillis());
//                mChart.saveToPath ("title" + System.currentTimeMillis (), "");
//                break;
//            }
//            case R.id.actionTogglePercent:
//                mChart.setUsePercentValues (!mChart.isUsePercentValuesEnabled ());
//                mChart.invalidate ();
//                break;

    private void setData()
    {
        ArrayList<Entry> yVals1 = new ArrayList<>();
        ArrayList<String> xVals = new ArrayList<>();
//        boolean have = false;
        for (int i = 0; i < orderMoney7.length; i++)
        {
            // TODO: 15-9-29  
//            if (orderMoney7[i] > 0)
//            {
//                have = true;
                yVals1.add(new Entry(orderMoney7[i], i));
                xVals.add(orderTime7[i]);
//            }
//            if (have)
//            {
//                mChart.setCenterText("总营业额");
//            }
        }

        PieDataSet dataSet = new PieDataSet(yVals1, "");
        dataSet.setSliceSpace(2f);
        dataSet.setSelectionShift(2f);

        ArrayList<Integer> colors = new ArrayList<>();

        colors.add(getResources().getColor(R.color.s1));
        colors.add(ColorTemplate.getHoloBlue());
        colors.add(getResources().getColor(R.color.s2));
        colors.add(getResources().getColor(R.color.s3));
        colors.add(getResources().getColor(R.color.s4));
        colors.add(getResources().getColor(R.color.s5));
        colors.add(getResources().getColor(R.color.s6));


        dataSet.setColors(colors);

        data = new PieData(xVals, dataSet);
        if (mChart.isUsePercentValuesEnabled())
        {
            data.setValueFormatter(new PercentFormatter());
        }
        //        data.setValueFormatter (new );
        data.setValueTextSize(12f);
        data.setValueTextColor(getResources ().getColor (R.color.white));
        mChart.setData(data);
        mChart.highlightValues(null);
        mChart.invalidate();
    }

    PieData data;


    // private void removeLastEntry() {
    //
    // PieData data = mChart.getDataOriginal();
    //
    // if (data != null) {
    //
    // PieDataSet set = data.getDataSet();
    //
    // if (set != null) {
    //
    // Entry e = set.getEntryForXIndex(set.getEntryCount() - 1);
    //
    // data.removeEntry(e, 0);
    // // or remove by index
    // // mData.removeEntry(xIndex, dataSetIndex);
    //
    // mChart.notifyDataSetChanged();
    // mChart.invalidate();
    // }
    // }
    // }
    //
    // private void addEntry() {
    //
    // PieData data = mChart.getDataOriginal();
    //
    // if (data != null) {
    //
    // PieDataSet set = data.getDataSet();
    // // set.addEntry(...);
    //
    // data.addEntry(new Entry((float) (Math.random() * 25) + 20f,
    // set.getEntryCount()), 0);
    //
    // // let the chart know it's data has changed
    // mChart.notifyDataSetChanged();
    //
    // // redraw the chart
    // mChart.invalidate();
    // }
    // }
}
