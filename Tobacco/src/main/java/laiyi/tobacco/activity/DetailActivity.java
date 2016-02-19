package laiyi.tobacco.activity;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v7.widget.OrientationHelper;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.StaggeredGridLayoutManager;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;

import com.j256.ormlite.dao.Dao;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import butterknife.ButterKnife;
import butterknife.InjectView;
import butterknife.OnClick;
import feifei.library.util.AnimUtil;
import feifei.library.util.L;
import feifei.library.util.Tools;
import feifei.library.view.ClearEditText;
import laiyi.tobacco.R;
import laiyi.tobacco.adapter.PhotoAdapter;
import laiyi.tobacco.bean.Land;
import me.iwf.photopicker.PhotoPickerActivity;
import me.iwf.photopicker.utils.PhotoPickerIntent;

public class DetailActivity extends DataActivity
{

    @InjectView(R.id.et_name)
    EditText etName;
    @InjectView(R.id.et_address)
    EditText etAddress;
    @InjectView(R.id.et_area)
    EditText etArea;
    @InjectView(R.id.et_haiba)
    ClearEditText etHaiba;

    @InjectView(R.id.sp_soiltype)
    Spinner spSoilType;
    @InjectView(R.id.sp_landfeature)
    Spinner spLandFeature;
    @InjectView(R.id.sp_landtype)
    Spinner spLandType;
    @InjectView(R.id.sp_soilfertility)
    Spinner spSoilfertility;
    @InjectView(R.id.sp_tobaccotype)
    Spinner spTobaccotype;
    @InjectView(R.id.sp_tobaccobreed)
    Spinner spTobaccobreed;

    @InjectView(R.id.cb_waterexist)
    CheckBox cbWaterexist;
    @InjectView(R.id.cb_waterstate)
    CheckBox cbWaterstate;
    @InjectView(R.id.tv_gps)
    TextView tvGPS;

    @InjectView(R.id.recycler_view)
    RecyclerView recyclerView;

    @InjectView(R.id.button)
    Button button;

    ArrayList<String> arrayFeature = new ArrayList<> ();
    ArrayList<String> arrayType = new ArrayList<> ();
    ArrayList<String> arraySoilType = new ArrayList<> ();
    ArrayList<String> arraySoilFertility = new ArrayList<> ();
    ArrayList<String> arrayTobaccoType = new ArrayList<> ();
    ArrayList<String> arrayTobaccoBreed = new ArrayList<> ();

    Land land;
    Dao<Land, Integer> mLandDao;
    String type = "";

    PhotoAdapter photoAdapter;
    ArrayList<String> selectedPhotos = new ArrayList<> ();
    public final static int REQUEST_CODE = 1;
    public final static int REQUEST_GPS = 2;

    @Override
    protected void onCreate (Bundle savedInstanceState)
    {
        super.onCreate (savedInstanceState);
        setContentView (R.layout.activity_detail);
        ButterKnife.inject (this);
        tint ();
        initActionbar ();
        setLeftBack();
        actionbar_right2.setVisibility (View.VISIBLE);
        actionbar_right2.setText ("保存");
        actionbar_right2.setTextColor (Color.WHITE);
        actionbar_right2.setTextSize (12);
        actionbar_right2.setBackgroundResource (R.drawable.transparent);
        actionbar_right2.setOnClickListener (new View.OnClickListener ()
        {
            @Override
            public void onClick (View view)
            {
                // TODO: 15-10-1 验证并存储数据
                saveData ();
            }
        });
        setTitles ("地块详情");
        photoAdapter = new PhotoAdapter (this, selectedPhotos);
        recyclerView.setLayoutManager (new StaggeredGridLayoutManager (4, OrientationHelper.VERTICAL));
        recyclerView.setAdapter (photoAdapter);

        type = getIntent ().getStringExtra ("type");
        arrayFeature.add ("坡地");
        arrayFeature.add ("丘陵");
        arrayFeature.add ("平田");
        arraySoilType.add ("水稻土");
        arraySoilType.add ("石灰土");
        arraySoilType.add ("沙土");
        arraySoilType.add ("红土");
        arraySoilType.add ("黄土");
        arraySoilType.add ("黑土");
        arrayType.add ("旱地");
        arrayType.add ("水田");
        arraySoilFertility.add ("高");
        arraySoilFertility.add ("中");
        arraySoilFertility.add ("低");
        arrayTobaccoType.add ("考烟");
        arrayTobaccoBreed.add ("云烟87");

        spLandFeature.setAdapter (new ArrayAdapter<> (this, android.R.layout.simple_spinner_item, arrayFeature));
        spLandType.setAdapter (new ArrayAdapter<> (this, android.R.layout.simple_spinner_item, arrayType));
        spSoilType.setAdapter (new ArrayAdapter<> (this, android.R.layout.simple_spinner_item, arraySoilType));
        spSoilfertility.setAdapter (new ArrayAdapter<> (this, android.R.layout.simple_spinner_item, arraySoilFertility));
        spTobaccotype.setAdapter (new ArrayAdapter<> (this, android.R.layout.simple_spinner_item, arrayTobaccoType));
        spTobaccobreed.setAdapter (new ArrayAdapter<> (this, android.R.layout.simple_spinner_item, arrayTobaccoBreed));

        if ( type.equals ("edit") )
        {
            land = getIntent ().getParcelableExtra ("data");

            etName.setText (land.getLandName ());
            etArea.setText (land.getLandArea () + "");
            etAddress.setText (land.getLandAddress ());
            etHaiba.setText (land.getSeaLevel () + "");
            cbWaterexist.setChecked (land.getWaterExist () == 1);
            cbWaterstate.setChecked (land.getWaterState () == 1);
            // TODO: 15-10-6  
            if ( land.getGpsX () != null && land.getGpsY () != null && !land.getGpsX ().equals ("0") && !land.getGpsY ().equals ("0") )
            {
                tvGPS.setText ("经度：" + land.getGpsX () + "\n" + "纬度：" + land.getGpsY ());
            }

            spLandFeature.setSelection (arrayFeature.indexOf (land.getLandFeature ()));
            spLandType.setSelection (arrayType.indexOf (land.getLandType ()));
            spSoilType.setSelection (arraySoilType.indexOf (land.getSoilType ()));
            spSoilfertility.setSelection (arraySoilFertility.indexOf (land.getSoilType ()));
            spTobaccotype.setSelection (arrayTobaccoType.indexOf (land.getSoilType ()));
            spTobaccobreed.setSelection (arrayTobaccoBreed.indexOf (land.getSoilType ()));

            add (land.getPic1 ());
            add (land.getPic2 ());
            add (land.getPic3 ());
            if ( selectedPhotos.size () > 0 )
            {
                recyclerView.setVisibility (View.VISIBLE);
                photoAdapter.notifyDataSetChanged ();
            }
        } else if ( type.equals ("add") )
        {
        }

        tvGPS.setOnClickListener (new View.OnClickListener ()
        {
            @Override
            public void onClick (View view)
            {
                //                if ( ConfigUtil.readBoolean (activity,"down",false) )
                //                {
                //                    ArrayList<MKOLUpdateElement> localMapList = mOffline.getAllUpdateInfo ();
                //                    MKOLUpdateElement e = localMapList.get (0);
                Intent intent = new Intent ();
                //                    intent.putExtra ("x", e.geoPt.longitude);
                //                    intent.putExtra ("y", e.geoPt.latitude);
                intent.setClass (activity, MapActivity.class);
                startActivityForResult (intent, REQUEST_GPS);
                AnimUtil.animTo (activity);
                //                } else
                //                {
                //                    Tools.toast (activity, "请先下载离线地图");
                //                }
            }
        });

        //  adapterView.getItemAtPosition (i).toString ()
    }

    void add (String s)
    {
        if ( !Tools.isEmpty (s) )
        {
            selectedPhotos.add (s);
        }

    }

    @OnClick(R.id.button)
    public void select ()
    {
        PhotoPickerIntent intent = new PhotoPickerIntent (activity);
        intent.setPhotoCount (3);
        startActivityForResult (intent, REQUEST_CODE);
    }

    public void previewPhoto (Intent intent)
    {
        startActivityForResult (intent, REQUEST_CODE);
    }

    @Override
    protected void onActivityResult (int requestCode, int resultCode, Intent data)
    {
        super.onActivityResult (requestCode, resultCode, data);

        List<String> photos = new ArrayList<> ();
        if ( resultCode == RESULT_OK )
        {
            if ( requestCode == REQUEST_CODE )
            {
                if ( data != null )
                {
                    photos = data.getStringArrayListExtra (PhotoPickerActivity.KEY_SELECTED_PHOTOS);
                }
                selectedPhotos.clear ();
                if ( photos != null )
                {
                    selectedPhotos.addAll (photos);
                }
                photoAdapter.notifyDataSetChanged ();
                if ( photos.size () > 0 )
                {
                    //                button.setVisibility (View.GONE);
                    recyclerView.setVisibility (View.VISIBLE);
                }
            } else if ( requestCode == REQUEST_GPS )
            {
                if ( data != null )
                {
                    x = data.getDoubleExtra ("la", 0);
                    y = data.getDoubleExtra ("lo", 0);
                    tvGPS.setText ("经度：" + x + "\n" + "纬度：" + y);
                }
            }
        }
    }

    double x = 0;
    double y = 0;


    private void saveData ()
    {
        String name = etName.getText ().toString ();
        String address = etAddress.getText ().toString ();
        String area = etArea.getText ().toString ();
        String haiba = etHaiba.getText ().toString ();
        if ( Tools.isEmpty (name) )
        {
            AnimUtil.animShakeText (etName);
            Tools.toast (activity, "请输入地块名称");
        } else if ( Tools.isEmpty (address) )
        {
            AnimUtil.animShakeText (etAddress);
            Tools.toast (activity, "请输入地块地址");
        } else if ( Tools.isEmpty (area) )
        {
            AnimUtil.animShakeText (etArea);
            Tools.toast (activity, "请输入地块面积");
        } else if ( Tools.isEmpty (haiba) )
        {
            AnimUtil.animShakeText (etHaiba);
            Tools.toast (activity, "请输入海拔");
            // TODO: 15-10-6
        } else if ( Tools.isEmpty (tvGPS.getText ().toString ()) )
        {
            AnimUtil.animShakeText (tvGPS);
            Tools.toast (activity, "请选择经纬度");
        } else
        {
            if ( type.equals ("add") )
            {
                land = new Land ();
            }
            //            else if ( type.equals ("edit") )
            //            {
            //            }

            land.setLandName (name);
            land.setLandAddress (address);
            land.setLandArea (Double.valueOf (area));
            land.setSeaLevel (Double.valueOf (haiba));

            land.setLandFeature (spLandFeature.getSelectedItem ().toString ());
            land.setSoilType (spSoilType.getSelectedItem ().toString ());
            land.setSoilFertility (spSoilfertility.getSelectedItem ().toString ());
            land.setLandType (spLandType.getSelectedItem ().toString ());
            land.setTobaccoType (spTobaccotype.getSelectedItem ().toString ());
            land.setTobaccoBreed (spTobaccobreed.getSelectedItem ().toString ());

            land.setWaterExist (cbWaterexist.isChecked () ? 1 : 0);
            land.setWaterState (cbWaterstate.isChecked () ? 1 : 0);
            land.setState (type.equals ("add") ? 1 : 2);

            // TODO: 15-10-6
            if ( x != 0 && y != 0 )
            {
                land.setGpsX (x + "");
                land.setGpsY (y + "");
            }

            try
            {
                land.setPic1 (selectedPhotos.get (0));
            } catch (Exception e)
            {
                land.setPic1 ("");
            }
            try
            {
                land.setPic2 (selectedPhotos.get (1));
            } catch (Exception e)
            {
                land.setPic2 ("");
            }
            try
            {
                land.setPic3 (selectedPhotos.get (2));
            } catch (Exception e)
            {
                land.setPic3 ("");
            }

            L.l (land.toString ());

            try
            {
                mLandDao = mDbHelper.getLandDao ();
                mLandDao.createOrUpdate (land);
            } catch (SQLException e)
            {
                e.printStackTrace ();
            }
            Tools.toast (activity, type.equals ("add") ? "添加成功" : "编辑成功");
            AnimUtil.animBackFinish (activity);
        }

    }



    //    LocationManager locManager;
    //    Location location;
    //    private void openGPSSettings() {
    //        locManager = (LocationManager) this
    //                .getSystemService(Context.LOCATION_SERVICE);
    //        if (locManager
    //                .isProviderEnabled(android.location.LocationManager.GPS_PROVIDER)) {
    //            Toast.makeText(this, "GPS功能开启成功！", Toast.LENGTH_SHORT).show();
    //            // 启动发送线程
    //            if (!threadRunning) {
    //                startSendThread();// 开启传输线程
    //            }
    //            // 启动监听
    //            LocationListener locLinstener = new LocationListener() {
    //                @Override
    //                public void onLocationChanged(Location arg0) {
    //                    if (location == null) {
    //                        location = arg0;
    //                    } else {
    //                        SimpleDateFormat sdf = new SimpleDateFormat(
    //                                "yyyy-MM-dd HH:mm:ss");
    //                        String currentTime = sdf.format(arg0.getTime());
    //                        if (Math.abs(location.getLatitude()
    //                                - arg0.getLatitude()) >= 0.00007
    //                                || Math.abs(location.getLongitude()
    //                                - arg0.getLongitude()) >= 0.00007) {
    //                            location = arg0;
    //                            processLocation(location);
    //                        }
    //                    }
    //                }
    //
    //                @Override
    //                public void onProviderDisabled(String provider) {
    //                }
    //
    //                @Override
    //                public void onProviderEnabled(String provider) {
    //                }
    //
    //                @Override
    //                public void onStatusChanged(String provider, int status,
    //                                            Bundle extras) {
    //                }
    //            };
    //            locManager.requestLocationUpdates(LocationManager.GPS_PROVIDER,
    //                    10000, 12, locLinstener);
    //            // 获得当前的位置
    //            location = locManager
    //                    .getLastKnownLocation(LocationManager.GPS_PROVIDER);
    //        } else {
    //            Toast.makeText (this, "请开启GPS！", Toast.LENGTH_SHORT).show();
    //            Intent intent = new Intent(Settings.ACTION_SECURITY_SETTINGS);
    //            startActivityForResult(intent, 0); // 此为设置完成后返回到获取界面
    //        }
    //    }
}
