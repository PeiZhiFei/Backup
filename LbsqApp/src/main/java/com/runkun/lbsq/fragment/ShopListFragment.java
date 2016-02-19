package com.runkun.lbsq.fragment;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.baidu.location.BDLocation;
import com.baidu.location.BDLocationListener;
import com.baidu.location.LocationClient;
import com.baidu.location.LocationClientOption;
import com.runkun.lbsq.R;
import com.runkun.lbsq.utils.Tools;

import feifei.project.util.ToastUtil;

public class ShopListFragment extends ShopListBaseFragment
{
    //    LocationManager locationManager;
    BDLocationListener locationListener = new MyLocationListener();
    BDLocation location;
    LocationClient client;
    boolean one;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState)
    {
        super.onCreateView(inflater, container, savedInstanceState);
        client = new LocationClient(getActivity().getApplicationContext());
        client.registerLocationListener(locationListener);
        LocationClientOption option2 = new LocationClientOption();
        option2.setLocationMode(LocationClientOption.LocationMode.Hight_Accuracy);
        option2.setCoorType("bd09ll");
        option2.setIsNeedAddress(true);
        // option2.setScanSpan(100000);
        client.setLocOption(option2);
        client.start();
        one = false;
        return view;
    }

    class MyLocationListener implements BDLocationListener
    {
        @Override
        public void onReceiveLocation(BDLocation bdLocation)
        {
            if (!one)
            {
                location = bdLocation;
                // locData = new MyLocationData.Builder()
                // .accuracy(location.getRadius())
                // .direction(location.getDirection())
                // .latitude(location.getLatitude())
                // .longitude(location.getLongitude()).build();
                la = location.getLatitude();
                lo = location.getLongitude();
                if (la != 0.0 && 0.0 != lo)
                {
                    reloadPosition(null, false);
                } else
                {
                    ToastUtil.toast (getActivity (),
                            Tools.getStr (fragment, R.string.LOCATIONFAIL));
                    emptyTextView.setText(Tools.getStr(fragment,
                            R.string.LOCATIONFAIL));
                    // client.requestLocation();
                }
                String city = location.getCity();
                if (Tools.isEmpty(city))
                {
                    shopListActivity.setPosition("定位失败");
                } else
                {
                    shopListActivity.setPosition(city + "");
                }

                one = true;
            }
        }
    }

}
