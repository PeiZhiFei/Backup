package com.runkun.lbsq.busi.test;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.SeekBar;
import android.widget.TextView;
import android.widget.Toast;

import com.baidu.location.BDLocation;
import com.baidu.location.BDLocationListener;
import com.baidu.location.LocationClient;
import com.baidu.location.LocationClientOption;
import com.baidu.mapapi.cloud.CloudListener;
import com.baidu.mapapi.cloud.CloudManager;
import com.baidu.mapapi.cloud.CloudPoiInfo;
import com.baidu.mapapi.cloud.CloudSearchResult;
import com.baidu.mapapi.cloud.DetailSearchResult;
import com.baidu.mapapi.cloud.NearbySearchInfo;
import com.baidu.mapapi.map.BaiduMap;
import com.baidu.mapapi.map.BitmapDescriptor;
import com.baidu.mapapi.map.BitmapDescriptorFactory;
import com.baidu.mapapi.map.CircleOptions;
import com.baidu.mapapi.map.InfoWindow;
import com.baidu.mapapi.map.MapStatus;
import com.baidu.mapapi.map.MapStatusUpdate;
import com.baidu.mapapi.map.MapStatusUpdateFactory;
import com.baidu.mapapi.map.MapView;
import com.baidu.mapapi.map.Marker;
import com.baidu.mapapi.map.MarkerOptions;
import com.baidu.mapapi.map.MyLocationConfiguration;
import com.baidu.mapapi.map.MyLocationData;
import com.baidu.mapapi.map.OverlayOptions;
import com.baidu.mapapi.map.Stroke;
import com.baidu.mapapi.model.LatLng;
import com.baidu.mapapi.model.LatLngBounds;
import com.hitoosoft.hrssapp.R;

import java.util.ArrayList;
import java.util.Map;

import library.util.AnimUtil;
import library.widget.snackbar.ToastCustom;

//E5:D0:B8:AA:BA:AE:65:73:6C:4F:CA:4A:FF:CE:32:60:F0:7C:52:94;com.hitoosoft.hrssapp
//XKvbIMUfLmMV26lFgTDbKdby
public class MapActivity extends BaseActivity implements View.OnClickListener,
        CloudListener, SeekBar.OnSeekBarChangeListener {

    MapView mMapView;
    BaiduMap mBaiduMap;
    // 标注弹窗
    InfoWindow mInfoWindow;
    // 位置回调接口
    BDLocationListener locationListener = new MyLocationListener();
    // 清单文件中注册服务
    BDLocation location;
    // 位置
    LocationClient client;
    Button button1, button;

    SeekBar seekBar;
    TextView progressText;

    Marker[] markerx;
    int p;
    public static final boolean DEBUG = true;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        CloudManager.getInstance().init(this);
        setContentView(R.layout.activity_map);
        setSwipeBackEnable(false);
        ((TextView) findViewById(R.id.titlebar)).setText("附近药店");
        findViewById(R.id.actionbar_right).setVisibility(View.INVISIBLE);
        mMapView = (MapView) findViewById(R.id.bmapView);
        mMapView.showScaleControl(false);
        mMapView.showZoomControls(false);
        mBaiduMap = mMapView.getMap();

        mBaiduMap.setMyLocationConfigeration(new MyLocationConfiguration(
                MyLocationConfiguration.LocationMode.FOLLOWING, true, null));
        mBaiduMap.setMyLocationEnabled(true);
        client = new LocationClient(this);
        client.registerLocationListener(locationListener);

        LocationClientOption option2 = new LocationClientOption();
        option2.setLocationMode(LocationClientOption.LocationMode.Hight_Accuracy);
        // option2.setOpenGps(true);
        option2.setCoorType("bd09ll");
        option2.setScanSpan(60000);
        // option2.setIsNeedAddress(true);
        // option2.setNeedDeviceDirect(true);
        client.setLocOption(option2);

        button = (Button) findViewById(R.id.button);
        button1 = (Button) findViewById(R.id.button1);
        button.setOnClickListener(this);
        button1.setOnClickListener(this);

        seekBar = (SeekBar) findViewById(R.id.seekBar);
        seekBar.setOnSeekBarChangeListener(this);
        seekBar.setFocusable(false);
        progressText = (TextView) findViewById(R.id.progress);

        // timer = new Timer(true);
        // 点击标注弹出layout，点击layout的事件
        mBaiduMap
                .setOnMarkerClickListener(new BaiduMap.OnMarkerClickListener() {
                    @Override
                    public boolean onMarkerClick(final Marker marker2) {
                        Button button = new Button(MapActivity.this);
                        button.setBackgroundResource(R.drawable.popmap);
                        InfoWindow.OnInfoWindowClickListener listener = null;
                        for (int i = 0; i < markerx.length; i++) {
                            if (marker2 == markerx[i]) {
                                button.setText(enertyArrayList.get(i).getName());
                                p = i;
                                break;
                            }
                        }
                        listener = new InfoWindow.OnInfoWindowClickListener() {
                            public void onInfoWindowClick() {
                                mBaiduMap.hideInfoWindow();
                                Intent intent = new Intent(MapActivity.this,
                                        DetailActivity.class);
                                intent.putExtra("p", enertyArrayList.get(p));
                                if (DEBUG) {
                                    intent.putExtra("lat", 35.425289528306);
                                    intent.putExtra("lon", 116.60377473566);
                                } else {
                                    intent.putExtra("lat",
                                            location.getLatitude());
                                    intent.putExtra("lon",
                                            location.getLongitude());
                                }
                                startActivity(intent);
                                AnimUtil.animToSlide(MapActivity.this);
                            }
                        };
                        LatLng ll = marker2.getPosition();
                        mInfoWindow = new InfoWindow(BitmapDescriptorFactory
                                .fromView(button), ll, -47, listener);
                        mBaiduMap.showInfoWindow(mInfoWindow);
                        return true;
                    }
                });
        client.start();
        // Handler handler = new Handler();
        // handler.postDelayed(new Runnable() {
        //
        // @Override
        // public void run() {
        // client.start();
        // }
        // }, 2000);
    }

    ArrayList<Enerty> enertyArrayList = new ArrayList<Enerty>();
    // private Timer timer;
    // int time = 0;
    NearbySearchInfo info;

    private void search(int radius) {
        info = new NearbySearchInfo();
        // info.ak = "l131TKHStp6eQ8exAtmM3d1V";
        // info.geoTableId = 103204;
        info.ak = "UNaGjIllyzV4q0kYDsUpP47g";
        info.geoTableId = 103722;
        info.radius = radius;
        if (DEBUG) {
            info.location = "" + 116.60377473566 + "," + 35.425289528306;
        } else {
            info.location = "" + location.getLongitude() + ","
                    + location.getLatitude();
        }

        info.pageSize = 50;
        // timer.schedule(new TimerTask() {
        // @Override
        // public void run() {
        // if (time < 6) {
        // info.pageIndex = time;
        CloudManager.getInstance().nearbySearch(info);
        // time++;
        // }
        // }
        // }, 1000, 2000);
    }

    // 获取到搜索结果后添加标记
    public void onGetSearchResult(CloudSearchResult result, int error) {
        try {

            mBaiduMap.clear();
            if (result != null && result.poiList != null
                    && result.poiList.size() > 0) {
                // Log.e("map",
                // "onGetSearchResult, result length: "
                // + result.poiList.size());
                BitmapDescriptor bd1 = BitmapDescriptorFactory
                        .fromResource(R.drawable.icon_marka);
                BitmapDescriptor bd2 = BitmapDescriptorFactory
                        .fromResource(R.drawable.icon_markb);
                LatLng ll;
                LatLngBounds.Builder builder = new LatLngBounds.Builder();
                markerx = new Marker[result.poiList.size()];
                for (int i = 0; i < result.poiList.size(); i++) {
                    CloudPoiInfo cloudPoiInfo = result.poiList.get(i);
                    Enerty enerty = new Enerty(cloudPoiInfo.title,
                            cloudPoiInfo.address, cloudPoiInfo.latitude,
                            cloudPoiInfo.longitude);
                    Map<String, Object> map = cloudPoiInfo.extras;
                    enerty.setPerson((String) map.get("person"));
                    enerty.setPhone((String) map.get("phone"));
                    String typeString = (String) map.get("types");
                    enerty.setType(typeString);
                    enertyArrayList.add(enerty);
                    // for (CloudPoiInfo info : result.poiList) {
                    // ll = new LatLng(info.latitude, info.longitude);
                    ll = new LatLng(cloudPoiInfo.latitude,
                            cloudPoiInfo.longitude);
                    OverlayOptions oo;
                    if (typeString.equals("定点药店")) {
                        oo = new MarkerOptions().icon(bd1).position(ll);
                    } else {
                        oo = new MarkerOptions().icon(bd2).position(ll);
                    }

                    markerx[i] = (Marker) mBaiduMap.addOverlay(oo);
                    builder.include(ll);
                }

                // LatLngBounds bounds = builder.build();
                // MapStatusUpdate u =
                // MapStatusUpdateFactory.newLatLngBounds(bounds);
                // mBaiduMap.animateMapStatus(u);

            }
            // 添加圆
            LatLng llCircle;
            if (DEBUG) {
                llCircle = new LatLng(35.425289528306, 116.60377473566);
            } else {
                llCircle = new LatLng(location.getLatitude(),
                        location.getLongitude());
            }

            OverlayOptions ooCircle = new CircleOptions().fillColor(0x3054FF9F)
                    .center(llCircle).stroke(new Stroke(5, 0x3033cccc))
                    .radius(1200);

            mBaiduMap.addOverlay(ooCircle);
            ToastCustom.toast(MapActivity.this, null, Gravity.BOTTOM, true, false,
                    LayoutInflater.from(MapActivity.this).inflate(
                            R.layout.map_tips, null));
        } catch (Exception e) {
            e.printStackTrace();
            // 这里不能太慢了
            // TS.toast(MapActivity.this, "有异常");
        }
    }

    public void onGetDetailSearchResult(DetailSearchResult result, int error) {
        if (result != null) {
            if (result.poiInfo != null) {
                Toast.makeText(MapActivity.this, result.poiInfo.title,
                        Toast.LENGTH_SHORT).show();
            } else {
                Toast.makeText(MapActivity.this, "status:" + error,
                        Toast.LENGTH_SHORT).show();
            }
        }
    }

    @Override
    public void onProgressChanged(SeekBar seekBar, int i, boolean b) {
        progressText.setText("范围" + i);
    }

    @Override
    public void onStartTrackingTouch(SeekBar seekBar) {

    }

    @Override
    public void onStopTrackingTouch(SeekBar seekBar) {
        // time = 0;
        // mBaiduMap.clear();
        if (seekBar.getProgress() == 0) {
            search(1);
        } else {
            search(seekBar.getProgress());
        }

    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.button1:
                // 点击定位的时候只需重新设置一下参数就行了
                mBaiduMap
                        .setMyLocationConfigeration(new MyLocationConfiguration(
                                MyLocationConfiguration.LocationMode.FOLLOWING,
                                true, null));
                break;
            case R.id.button:
                search(2000);
                break;
        }

    }

    class MyLocationListener implements BDLocationListener {
        MapStatus mMapStatus;
        MyLocationData locData;

        @Override
        public void onReceiveLocation(BDLocation bdLocation) {
            location = bdLocation;
            if (DEBUG) {
                locData = new MyLocationData.Builder()
                        // 定位经度
                        .accuracy(location.getRadius())
                                // 此处设置开发者获取到的方向信息，顺时针0-360
                        .direction(location.getDirection())
                        .latitude(35.425289528306).longitude(116.60377473566)
                        .build();
                mMapStatus = new MapStatus.Builder()
                        .target(new LatLng(35.425289528306, 116.60377473566))
                        .zoom(16).build();
            } else {
                // 构造定位数据
                locData = new MyLocationData.Builder()
                        // 定位经度
                        .accuracy(location.getRadius())
                                // 此处设置开发者获取到的方向信息，顺时针0-360
                        .direction(location.getDirection())
                        .latitude(location.getLatitude())
                        .longitude(location.getLongitude()).build();
                // 设置缩放
                mMapStatus = new MapStatus.Builder()
                        .target(new LatLng(location.getLatitude(), location
                                .getLongitude())).zoom(16).build();
            }

            // 设置定位数据
            // if (mBaiduMap != null && locData != null) {
            mBaiduMap.setMyLocationData(locData);
            MapStatusUpdate mMapStatusUpdate = MapStatusUpdateFactory
                    .newMapStatus(mMapStatus);
            // 改变地图状态
            mBaiduMap.setMapStatus(mMapStatusUpdate);
            // }
            // search(2000);
            Handler handler = new Handler();
            handler.postDelayed(new Runnable() {

                @Override
                public void run() {
                    search(2000);
                }
            }, 2000);

        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        // 在activity执行onDestroy时执行mMapView.onDestroy()，实现地图生命周期管理
        mMapView.onDestroy();
    }

    @Override
    protected void onResume() {
        super.onResume();
        // 在activity执行onResume时执行mMapView. onResume ()，实现地图生命周期管理
        mMapView.onResume();
    }

    @Override
    protected void onPause() {
        super.onPause();
        // 在activity执行onPause时执行mMapView. onPause ()，实现地图生命周期管理
        mMapView.onPause();
        client.stop();
    }
}
