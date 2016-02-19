package laiyi.tobacco.activity;

import android.content.Intent;
import android.os.Bundle;

import com.afollestad.materialdialogs.GravityEnum;
import com.afollestad.materialdialogs.MaterialDialog;
import com.afollestad.materialdialogs.Theme;
import com.baidu.location.BDLocation;
import com.baidu.location.BDLocationListener;
import com.baidu.location.LocationClient;
import com.baidu.location.LocationClientOption;
import com.baidu.mapapi.map.BaiduMap;
import com.baidu.mapapi.map.MapStatus;
import com.baidu.mapapi.map.MapStatusUpdate;
import com.baidu.mapapi.map.MapStatusUpdateFactory;
import com.baidu.mapapi.map.MapView;
import com.baidu.mapapi.map.MyLocationData;
import com.baidu.mapapi.model.LatLng;

import butterknife.ButterKnife;
import butterknife.OnClick;
import feifei.library.util.AnimUtil;
import feifei.library.util.Tools;
import laiyi.tobacco.R;

/**
 * 这个是离线定位
 */
public class MapActivity extends LocationActivity
{
    @SuppressWarnings("unused")
    private static final String LTAG = MapActivity.class.getSimpleName ();
    private MapView mMapView;
    private BaiduMap mBaiduMap;

    // 定位相关
    LocationClient mLocClient;
    public MyLocationListenner myListener = new MyLocationListenner ();

    @Override
    public void onCreate (Bundle savedInstanceState)
    {
        super.onCreate (savedInstanceState);
        setSwipeBackEnable (false);
        setContentView (R.layout.activity_map);
        tint ();
        initActionbar ();
        setLeftBack ();
        ButterKnife.inject (this);

        mMapView = (MapView) findViewById (R.id.bmapView);
        mBaiduMap = mMapView.getMap ();

        if ( localMapList.size () <= 0 )
        {
            new MaterialDialog.Builder (activity)
                    //                .title ("离线地图下载")
                    .titleGravity (GravityEnum.CENTER)
                    .titleColorRes (android.R.color.holo_purple)
                    .content ("是否下载离线地图")
                    .title ("提示")
                    .contentGravity (GravityEnum.CENTER)
                    .contentColorRes (android.R.color.black)
                    .positiveText ("去下载")
                    .negativeText ("取消")
                    .callback (new MaterialDialog.ButtonCallback ()
                    {
                        @Override
                        public void onPositive (MaterialDialog dialog)
                        {
                            super.onPositive (dialog);
                            startActivity (new Intent (activity, MapManageActivity.class));
                            AnimUtil.animToFinish (activity);
                        }

                        @Override
                        public void onNegative (MaterialDialog dialog)
                        {
                            super.onNegative (dialog);
                            dialog.dismiss ();
                            AnimUtil.animBackFinish (activity);
                        }
                    })
                    .theme (Theme.LIGHT)
                    .cancelable (false)
                    .show ();

            return;
        }

        mMapView.showScaleControl (false);
        mMapView.showZoomControls (false);
        mBaiduMap.setMyLocationEnabled (true);
        mBaiduMap.setOnMapLongClickListener (new BaiduMap.OnMapLongClickListener ()
        {
            public void onMapLongClick (LatLng point)
            {
                if ( point != null && point.latitude != 0 && point.longitude != 0 )
                {
                    Intent intent = new Intent ();
                    intent.putExtra ("la", point.latitude);
                    intent.putExtra ("lo", point.longitude);
                    setResult (DetailActivity.RESULT_OK, intent);
                    AnimUtil.animBackFinish (activity);
                } else
                {
                    Tools.toast (activity, "请重新长按");
                }
            }
        });


        // 定位初始化
        mLocClient = new LocationClient (this);
        mLocClient.registerLocationListener (myListener);
        LocationClientOption option = new LocationClientOption ();
        option.setOpenGps (true);// 打开gps
        option.setCoorType ("bd09ll"); // 设置坐标类型
        option.setScanSpan (1000);
        //这句和离线地图 option.setIsNeedAddress (true);
        mLocClient.setLocOption (option);
        mLocClient.start ();
    }

    @OnClick(R.id.btn_gps)
    public void getGps ()
    {
        if ( la != 0 && lo != 0 )
        {
            Intent intent = new Intent ();
            intent.putExtra ("la", la);
            intent.putExtra ("lo", lo);
            setResult (DetailActivity.RESULT_OK, intent);
            AnimUtil.animBackFinish (activity);
        } else
        {
            Tools.toast (activity, "定位失败");
        }

    }

    /**
     * 定位SDK监听函数
     */
    public class MyLocationListenner implements BDLocationListener
    {
        MapStatus mMapStatus;
        MyLocationData locData;

        @Override
        public void onReceiveLocation (BDLocation location)
        {
            //la纬度 lo经度
            la = location.getLatitude ();
            lo = location.getLongitude ();
            if ( la != 0 && lo != 0 )
            {
                // 构造定位数据
                locData = new MyLocationData.Builder ()
                        // 定位经度
                        .accuracy (location.getRadius ())
                                // 此处设置开发者获取到的方向信息，顺时针0-360
                        .direction (location.getDirection ())
                        .latitude (location.getLatitude ())
                        .longitude (location.getLongitude ()).build ();
                // 设置缩放
                mMapStatus = new MapStatus.Builder ()
                        .target (new LatLng (location.getLatitude (), location
                                .getLongitude ())).zoom (16).build ();
                mBaiduMap.setMyLocationData (locData);
                MapStatusUpdate mMapStatusUpdate = MapStatusUpdateFactory
                        .newMapStatus (mMapStatus);
                // 改变地图状态
                mBaiduMap.setMapStatus (mMapStatusUpdate);
                //                Tools.toast (activity, la + "", lo + "", location.getCity ());
            }
        }
    }


    protected double lo;
    protected double la;

    @Override
    protected void onPause ()
    {
        super.onPause ();
        // activity 暂停时同时暂停地图控件
        mMapView.onPause ();
    }

    @Override
    protected void onResume ()
    {
        super.onResume ();
        // activity 恢复时同时恢复地图控件
        mMapView.onResume ();
    }

    @Override
    protected void onDestroy ()
    {
        super.onDestroy ();
        // activity 销毁时同时销毁地图控件
        mMapView.onDestroy ();
    }

}
