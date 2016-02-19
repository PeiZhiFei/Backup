package feifei.dataanalysis.base;

import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;


public class BaseFragment extends Fragment
{
    protected Context fragment;

    @Override
    public void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        fragment = getActivity();
    }

}
