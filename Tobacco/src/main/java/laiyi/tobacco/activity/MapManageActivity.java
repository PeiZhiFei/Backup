package laiyi.tobacco.activity;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;

import com.afollestad.materialdialogs.GravityEnum;
import com.afollestad.materialdialogs.MaterialDialog;
import com.afollestad.materialdialogs.Theme;
import com.baidu.location.BDLocation;
import com.baidu.location.BDLocationListener;
import com.baidu.location.LocationClient;
import com.baidu.location.LocationClientOption;
import com.baidu.mapapi.map.offline.MKOLSearchRecord;
import com.baidu.mapapi.map.offline.MKOLUpdateElement;
import com.baidu.mapapi.map.offline.MKOfflineMap;

import java.util.ArrayList;

import butterknife.ButterKnife;
import butterknife.InjectView;
import butterknife.OnClick;
import feifei.library.util.Tools;
import laiyi.tobacco.R;

//这个和登陆页是继承同一个在线定位和下载
public class MapManageActivity extends LocationActivity
{

    @InjectView(R.id.list)
    ListView list;
    private LocalMapAdapter lAdapter = null;

    // 定位相关
    LocationClient mLocClient;
    public MyLocationListenner myListener = new MyLocationListenner ();
    MaterialDialog dialog;
    MaterialDialog.Builder builder;

    private TextView emptyTextView;


    @Override
    protected void onCreate (Bundle savedInstanceState)
    {
        super.onCreate (savedInstanceState);
        setContentView (R.layout.activity_map_manage);
        ButterKnife.inject (this);
        tint ();
        initActionbar ();
        setLeftBack ();
        setTitles("设置管理");

        ListView localMapListView = (ListView) findViewById (R.id.list);
        Tools tools = new Tools();
        View view2 = tools.getEmptyView(this, 0);
        ((ViewGroup) list.getParent()).addView (view2);
        emptyTextView = tools.getEmptyText();
        list.setEmptyView (view2);

        lAdapter = new LocalMapAdapter ();
        localMapListView.setAdapter (lAdapter);


        // 定位初始化
        mLocClient = new LocationClient (this);
        mLocClient.registerLocationListener (myListener);
        LocationClientOption option = new LocationClientOption ();
        option.setOpenGps (true);// 打开gps
        option.setCoorType ("bd09ll"); // 设置坐标类型
        option.setScanSpan (1000);
        option.setIsNeedAddress (true);
        mLocClient.setLocOption (option);

        builder = new MaterialDialog.Builder (activity)
                //                .title ("离线地图下载")
                .titleGravity (GravityEnum.CENTER)
                .titleColorRes (android.R.color.holo_purple)
                .content ("离线地图下载中")
                .contentGravity (GravityEnum.CENTER)
                .contentColorRes (android.R.color.black)
                .theme (Theme.LIGHT)
                .cancelable (false)
                .progress (false, 100, true);
    }


    /**
     * 定位SDK监听函数
     */
    public class MyLocationListenner implements BDLocationListener
    {

        @Override
        public void onReceiveLocation (BDLocation location)
        {
            city = location.getCity ();
            if ( Tools.isEmpty (city) )
            {
                Tools.toast (activity, "定位失败");
                mLocClient.requestLocation ();
            } else
            {
                //                Tools.toast (activity, "定位" + city + "");
                mOffline = new MKOfflineMap ();
                mOffline.init (MapManageActivity.this);
                ArrayList<MKOLSearchRecord> records = mOffline.searchCity (city);
                if ( records != null && records.size () != 0 )
                {
                    int cityid = Integer.parseInt (String.valueOf (records.get (0).cityID));
                    mOffline.start (cityid);
                } else
                {
                    Tools.toast (activity, "暂无该城市的离线地图");
                }
            }
            la = location.getLatitude ();
            lo = location.getLongitude ();
            //            Tools.toast (activity, la + "", lo + "", location.getCity ());
        }
    }


    protected double lo;
    protected double la;
    String city;

    private void loaction ()
    {
        mLocClient.start ();
    }

    @OnClick(R.id.btn_down)
    public void down ()
    {
        if ( !Tools.isNetworkAvailable (activity) )
        {
            Tools.toast (activity, "请打开网络");
        } else
        {
            loaction ();
        }
    }

    @Override
    public void onGetOfflineMapState (int type, int state)
    {
        if ( dialog == null )
        {
            dialog = builder.show ();
        }
        switch (type)
        {
            case MKOfflineMap.TYPE_DOWNLOAD_UPDATE:
            {
                MKOLUpdateElement update = mOffline.getUpdateInfo (state);
                // 处理下载进度更新提示
                if ( update != null )
                {
                    dialog.setProgress (update.ratio);
                    dialog.setContent (city + "离线地图下载中");
                    if ( update.ratio == 100 )
                    {
                        dialog.setContent (city + "离线地图下载完成");
                        Tools.toast (activity, "离线地图下载完成");
                        dialog.dismiss ();
                        updateView();
                    }
                }
            }
            break;
            case MKOfflineMap.TYPE_NEW_OFFLINE:
                // 有新离线地图安装
                Log.d ("OfflineDemo", String.format ("add offlinemap num:%d", state));
                break;
            case MKOfflineMap.TYPE_VER_UPDATE:
                // 版本更新提示
                // MKOLUpdateElement e = mOffline.getUpdateInfo(state);
                break;
        }
    }


    /**
     * 离线地图管理列表适配器
     */
    public class LocalMapAdapter extends BaseAdapter
    {

        @Override
        public int getCount ()
        {
            return localMapList.size ();
        }

        @Override
        public Object getItem (int index)
        {
            return localMapList.get (index);
        }

        @Override
        public long getItemId (int index)
        {
            return index;
        }

        @Override
        public View getView (int index, View view, ViewGroup arg2)
        {
            MKOLUpdateElement e = (MKOLUpdateElement) getItem (index);
            view = View.inflate (activity,
                    R.layout.offline_localmap_list, null);
            initViewItem (view, e);
            return view;
        }

        void initViewItem (View view, final MKOLUpdateElement e)
        {
            Button remove = (Button) view.findViewById (R.id.remove);
            TextView title = (TextView) view.findViewById (R.id.title);
            //            TextView update = (TextView) view.findViewById (R.id.update);
            //            TextView ratio = (TextView) view.findViewById (R.id.ratio);
            //            ratio.setText (e.ratio + "%");
            title.setText (e.cityName);
            //            if ( e.update )
            //            {
            //                update.setText ("可更新");
            //            } else
            //            {
            //                update.setText ("最新");
            //            }
            remove.setOnClickListener (new View.OnClickListener ()
            {
                @Override
                public void onClick (View arg0)
                {
                    mOffline.remove (e.cityID);
                    updateView ();
                }
            });
            //            display.setOnClickListener (new View.OnClickListener ()
            //            {
            //                @Override
            //                public void onClick (View v)
            //                {
            //                    Intent intent = new Intent ();
            //                    intent.putExtra ("x", e.geoPt.longitude);
            //                    intent.putExtra ("y", e.geoPt.latitude);
            //                    intent.setClass (OfflineDemo.this, BaseMapDemo.class);
            //                    startActivity (intent);
            //                }
            //            });
        }

    }

    /**
     * 更新状态显示
     */
    public void updateView ()
    {
        localMapList = mOffline.getAllUpdateInfo ();
        if ( localMapList == null )
        {
            localMapList = new ArrayList<MKOLUpdateElement> ();
        }
        lAdapter.notifyDataSetChanged ();
    }


}
