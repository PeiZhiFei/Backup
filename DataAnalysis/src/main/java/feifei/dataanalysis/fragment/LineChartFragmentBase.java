package feifei.dataanalysis.fragment;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import feifei.dataanalysis.R;


public class LineChartFragmentBase extends MainBaseFragment
{

    @Override
    public View onCreateView (LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState)
    {
        super.onCreateView (inflater, container, savedInstanceState);
        View view = inflater.inflate (R.layout.fragment_line_static, container, false);

        getFragmentManager ().beginTransaction ().replace (R.id.fragment7,LineChartFragment7.newInstance ()).commit ();
        getFragmentManager ().beginTransaction ().replace (R.id.fragment30,LineChartFragment30.newInstance ()).commit ();

        return view;
    }

    public static LineChartFragmentBase newInstance ()
    {
        LineChartFragmentBase cf = new LineChartFragmentBase ();
        return cf;
    }
}
