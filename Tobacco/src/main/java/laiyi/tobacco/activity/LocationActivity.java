package laiyi.tobacco.activity;

import android.os.Bundle;

import com.baidu.mapapi.map.offline.MKOLUpdateElement;
import com.baidu.mapapi.map.offline.MKOfflineMap;
import com.baidu.mapapi.map.offline.MKOfflineMapListener;

import java.util.ArrayList;

public class LocationActivity extends BaseActivity implements MKOfflineMapListener
{

    protected ArrayList<MKOLUpdateElement> localMapList = null;
    protected MKOfflineMap mOffline = null;

    @Override
    protected void onCreate (Bundle savedInstanceState)
    {
        super.onCreate (savedInstanceState);
        mOffline = new MKOfflineMap ();
        mOffline.init (this);

        // 获取已下过的离线地图信息
        localMapList = mOffline.getAllUpdateInfo ();
        if ( localMapList == null )
        {
            localMapList = new ArrayList<> ();
        }

    }

    @Override
    public void onGetOfflineMapState (int i, int i1)
    {

    }
}
