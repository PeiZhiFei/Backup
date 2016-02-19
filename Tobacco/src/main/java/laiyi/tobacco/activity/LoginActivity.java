package laiyi.tobacco.activity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.widget.Button;
import android.widget.TextView;

import com.afollestad.materialdialogs.GravityEnum;
import com.afollestad.materialdialogs.MaterialDialog;
import com.afollestad.materialdialogs.Theme;
import com.alibaba.fastjson.JSON;
import com.baidu.location.BDLocation;
import com.baidu.location.BDLocationListener;
import com.baidu.location.LocationClient;
import com.baidu.location.LocationClientOption;
import com.baidu.mapapi.map.offline.MKOLSearchRecord;
import com.baidu.mapapi.map.offline.MKOLUpdateElement;
import com.baidu.mapapi.map.offline.MKOfflineMap;
import com.baidu.mapapi.map.offline.MKOfflineMapListener;
import com.j256.ormlite.dao.Dao;

import org.json.JSONException;
import org.json.JSONObject;

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
import laiyi.tobacco.bean.Land;
import laiyi.tobacco.bean.User;
import laiyi.tobacco.util.DBHelper;
import laiyi.tobacco.util.MyConstant;
import laiyi.tobacco.util.UpdateManager;

//检查更新 初始化数据统计 异常信息 检查登录
public class LoginActivity extends NetActivity implements UpdateManager.CancelUpdate, MKOfflineMapListener
{

    public final String p = "901";
    @InjectView(R.id.et_password)
    ClearEditText password;
    @InjectView(R.id.et_username)
    ClearEditText username;
    @InjectView(R.id.version)
    TextView version;

    @InjectView(R.id.button)
    Button button;
    @InjectView(R.id.button2)
    Button button2;
    SharedPreferences.Editor editor;

    DBHelper mDbHelper;
    Dao<User, Integer> mUserDao;
    Dao<Land, Integer> mLandDao;
    public boolean mLandstate = true; //土地下载状态


    // 定位相关
    LocationClient mLocClient;
    public MyLocationListenner myListener = new MyLocationListenner ();

    @Override
    protected void onCreate (Bundle savedInstanceState)
    {
        super.onCreate (savedInstanceState);
        setContentView (R.layout.activity_login);
        //数据库
        mDbHelper = DBHelper.getInstance (this);
        try
        {
            mUserDao = mDbHelper.getUserDao ();
            mLandDao = mDbHelper.getLandDao ();
        } catch (SQLException e)
        {
            L.l ("constructor exception==" + e);
        }
        ButterKnife.inject (this);
        tint ();
        setSwipeBackEnable (false);
        SharedPreferences sp = getSharedPreferences (MyApplication.KEY_LOGIN_FILE, MODE_PRIVATE);
        editor = sp.edit ();
       /* boolean login = sp.getBoolean (MyApplication.KEY_LOGIN, false);
        if ( login )
        {
            startActivity (new Intent (activity, MainActivity.class));
            AnimUtil.animToFinish (activity);
            return;
        }*/
        username.setText ("901");
        password.setText ("901");
        initOther ();
        dialogInit ();
        version.setText ("当前版本：V" + UpdateManager.getVersionCode (activity));

//        if ( Tools.isNetworkAvailable (this) )
//        {
//            UpdateManager updateManager = new UpdateManager (this);
//            updateManager.checkUpdate ();
//        }
        //        else
        //        {
        //            cancelUpdate();
        //        }

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

    MaterialDialog dialog;
    MaterialDialog.Builder builder;

    private MKOfflineMap mOffline = null;


    @Override
    protected void onDestroy ()
    {
        mLocClient.stop ();
        super.onDestroy ();
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
                mOffline.init (LoginActivity.this);
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

    //    class MyLocationListener implements BDLocationListener
    //    {
    //        @Override
    //        public void onReceiveLocation (BDLocation bdLocation)
    //        {
    //            Tools.toast (activity, la + "", lo + "", city);
    //            location = bdLocation;
    //            la = location.getLatitude ();
    //            lo = location.getLongitude ();
    //
    //            city = location.getCity ();
    //            if ( Tools.isEmpty (city) )
    //            {
    //                Tools.toast (activity, "定位失败");
    //                client.requestLocation ();
    //            } else
    //            {
    //                //                Tools.toast (activity, "定位" + city + "");
    //                mOffline = new MKOfflineMap ();
    //                mOffline.init (LoginActivity.this);
    //                ArrayList<MKOLSearchRecord> records = mOffline.searchCity (city);
    //                if ( records != null && records.size () != 0 )
    //                {
    //                    int cityid = Integer.parseInt (String.valueOf (records.get (0).cityID));
    //                    mOffline.start (cityid);
    //                } else
    //                {
    //                    Tools.toast (activity, "暂无该城市的离线地图");
    //                }
    //            }
    //        }
    //
    //    }


    @OnClick(R.id.button2)
    public void button(){
//        if (!Tools.isNetworkAvailable(activity)) {
//            Tools.toast(activity, "请打开网络");
//            return;
//        }
        new MaterialDialog.Builder (activity)
                .titleGravity(GravityEnum.CENTER)
                .titleColorRes(android.R.color.holo_purple)
                .content("下载离线数据需要联网，确定要下载数据吗？")
                .title("提示")
                .contentGravity(GravityEnum.CENTER)
                .contentColorRes(android.R.color.black)
                .positiveText("确定")
                .negativeText("取消")
                .callback(new MaterialDialog.ButtonCallback() {
                              @Override
                              public void onPositive(MaterialDialog dialog) {
                                  super.onPositive(dialog);

                                  String user = username.getText().toString().trim();
                                  String pass = password.getText().toString().trim();
                                  if (!Tools.isEmpty(user)&& !Tools.isEmpty(pass)) {
                                      Download();
                                  } else {
                                      Tools.toast(activity, "请输入用户名或密码");
                                      AnimUtil.animShakeText(username);
                                      AnimUtil.animShakeText(password);
                                  }


                              }

                              @Override
                              public void onNegative(MaterialDialog dialog) {
                                  super.onNegative(dialog);
                                  dialog.dismiss();
                              }
                          }

                ) .theme(Theme.LIGHT).cancelable(false).show();
                }

    public void Download ()
    {
        List<Land> muser = new ArrayList<>();
            try
            {
                muser = mLandDao.queryBuilder().where().notIn("State", 0).query();
                if ( muser.size () == 0 )
                {
                    url = MyConstant.CLOGIN;
                    mParams.put ("username", username.getText().toString());
                    mParams.put ("userpass",  password.getText().toString());
                    loadData ();
                } else
                {
                    // TODO: 2015/9/30
                   // mLandDao.queryRaw ("delete from land");//使用sql方式清空表
                    //mLandDao.queryRaw ("delete from user");//使用sql方式清空表
                    Tools.toast (activity, "您还有"+muser.size()+"条数据未同步请先同步数据");
                }
            } catch (SQLException e)
            {
                e.printStackTrace ();
            }

    }

    private void loaction ()
    {
        mLocClient.start ();
    }

    @OnClick(R.id.button)
    public void veryLogin ()
    {
        String user = username.getText ().toString ().trim ();
        String pass = password.getText ().toString ().trim ();
        dialogProgress();
        if ( Tools.isEmpty (user) )
        {
            AnimUtil.animShakeText(username);
            Tools.toast (activity, "请输入用户名");
            //                            Snackbar.make (username, "请输入用户名", Snackbar.LENGTH_LONG)
            //                                    .setAction ("Action", null).show ();
        } else if ( Tools.isEmpty (pass) )
        {
            AnimUtil.animShakeText (password);
            Tools.toast (activity, "请输入密码");
        } else
        {
            try
            {
               /* L.l("添加");
                User u=new User();
                u.setUserName("901");
                mUserDao.create(u);
                L.l("添加成功");*/
                // mUserDao.deleteById(1);  //根据id删除
                L.l ("查询");
                List<User> muser = mUserDao.queryBuilder ().where ().eq ("UserNo", user).query ();
               /* for(int i =0;i<muser.size();i++){
                    L.l(""+muser.get(i).getUserName());
                    L.l(""+muser.get(i).getUserId());

                }*/
                if ( muser.size () != 0 )
                {
                    editor.putBoolean (MyApplication.KEY_LOGIN, true).apply ();
                    Intent intent = new Intent (activity, MainActivity.class);
                    intent.putExtra ("city", city);
                    startActivity (intent);
                    AnimUtil.animToFinish (activity);
                } else
                {
                    Tools.toast (activity, "离线登录失败");
                }
            } catch (SQLException e)
            {
                e.printStackTrace ();
            }
            dialogDismiss ();
        }
        /*if ( user.equals (p) && pass.equals (p) )
        {

            new Handler ().postDelayed (new Runnable ()
            {
                @Override
                public void run ()
                {
                    dialogDismiss ();
                    editor.putBoolean (MyApplication.KEY_LOGIN, true).apply ();
                    startActivity (new Intent (activity, MainActivity.class));
                    AnimUtil.animToFinish (activity);
                }
            }, 1500);

        } else
        {
            Tools.toast (activity, "用户名和密码不匹配");
            AnimUtil.animShakeText (username);
            AnimUtil.animShakeText (password);
        }*/
    }

    //这里只是和登录界面相关的一些东西
    private void initOther ()
    {
        //        new Handler ().postDelayed (new Runnable ()
        //        {
        //            @Override
        //            public void run ()
        //            {
        //                Tools.keyboardShow (username);
        //            }
        //        }, 600);

        password.addTextChangedListener (new TextWatcher ()
        {
            @Override
            public void beforeTextChanged (CharSequence s, int start, int count, int after)
            {

            }

            @Override
            public void onTextChanged (CharSequence s, int start, int before, int count)
            {

            }

            @Override
            public void afterTextChanged (Editable s)
            {
                notifys ();
            }
        });

        username.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                notifys();
            }
        });

        if ( password.getText ().toString ().trim ().length() > 0 && username.getText ().toString ().trim ().length() > 0 )
        {
            button.setEnabled(true);
        } else
        {
            button.setEnabled (false);
        }
    }

    private void notifys ()
    {
        if ( password.length() > 0 && username.length() > 0 )
        {
            button.setEnabled (true);
        } else
        {
            button.setEnabled (false);
        }
    }

    @Override
    protected void getData (final JSONObject jsonResult) throws JSONException
    {
        try
        {
            //返回100
            if (Tools.isSuccess(jsonResult)) {
                if ( mLandstate )
                {
                    new MaterialDialog.Builder(activity)
                            //                .title ("离线地图下载")
                            .titleGravity(GravityEnum.CENTER)
                            .titleColorRes(android.R.color.holo_purple)
                            .content("是否下载离线地图")
                            .title("提示")
                            .contentGravity(GravityEnum.CENTER)
                            .contentColorRes(android.R.color.black)
                            .positiveText("下载")
                            .negativeText("取消")
                            .callback(new MaterialDialog.ButtonCallback() {
                                @Override
                                public void onPositive(MaterialDialog dialog) {
                                    super.onPositive(dialog);
                                    loaction();
                                    login(jsonResult);
                                }

                                @Override
                                public void onNegative(MaterialDialog dialog) {
                                    super.onNegative(dialog);
                                    dialog.dismiss();
                                    login(jsonResult);
                                }
                            })
                            .theme(Theme.LIGHT)
                            .cancelable(false)
                            .show();

                } else
                {
                    mLandDao.queryRaw ("delete from land");//使用sql方式清空表
                    L.l ("Landjson===" + jsonResult);
/*
                mLandDao.queryRaw("delete from land");//使用sql方式清空表
                L.l("清空了数据下面是查询");
                List<Land> muser3=mLandDao.queryBuilder().query();
                for(int i =0;i<muser3.size();i++){
                    L.l("sid=="+muser3.get(i).getSId());
                    L.l("id=="+muser3.get(i).getId());
                    L.l(""+muser3.get(i).getLandName());
                    L.l(""+muser3.get(i).getTobaccoType());
                }
                L.l("查询结束");*/
                    List<Land> muser2 = new ArrayList<> ();
                    //String SId = UUID.randomUUID().toString().replaceAll("-","");  //后期添加SID 是自己生成唯一方法
                    muser2.addAll(JSON.parseArray(jsonResult.getString("Datas"), Land.class));
                    mLandDao.create(muser2);
                    L.l ("添加土地成功");
                    mLandstate = true;
                    Tools.toast(activity, "离线数据下载成功，欢迎使用烟叶生产系统");

                    //测试查询添加的信息
                    List<Land> muser = mLandDao.queryBuilder ().query ();
                    for (int i = 0; i < muser.size (); i++)
                    {
                        L.l ("sid==" + muser.get (i).getSId ());
                        L.l ("id==" + muser.get (i).getId ());
                        L.l ("" + muser.get (i).getLandName ());
                        L.l ("" + muser.get (i).getTobaccoType ());
                        L.l ("sate==" + muser.get (i).getState ());
                    }
                }
            }else{
               String msg= jsonResult.getString ("Msg");
                Tools.toast(activity, msg);
            }


        } catch (SQLException e)
        {
            e.printStackTrace ();
        }
    }

    public void login(JSONObject jsonResult){
        try{
            mLandDao.queryRaw("delete from user");//使用sql方式清空表
            User u = new User ();
            u = JSON.parseObject (jsonResult.getString ("Datas"), User.class);
            mUserDao.create (u);
            L.l ("添加成功");
            //测试查询
            List<User> muser = mUserDao.queryBuilder ().where ().eq ("UserNo", "901").query ();
            for (int i = 0; i < muser.size (); i++)
            {
                L.l ("" + muser.get (i).getUserName ());
                L.l ("" + muser.get (i).getUserId ());
                L.l ("key==" + muser.get (i).getKey ());
                url = MyConstant.CLAND;
                mParams.put ("key", muser.get (i).getKey ());
                mLandstate = false;
                loadData ();
            }
        }catch (SQLException e)
        {
            e.printStackTrace();
        } catch (JSONException e) {
            e.printStackTrace();
        }

    }

    @Override
    public void cancelUpdate ()
    {

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


    //    private void initUpdate() {
    //        UmengUpdateAgent.update(this);
    //        UmengUpdateAgent.setUpdateListener(new UmengUpdateListener() {
    //            @Override
    //            public void onUpdateReturned(int updateStatus, UpdateResponse updateInfo) {
    //                switch (updateStatus) {
    //                    case UpdateStatus.Yes:
    //                        UmengUpdateAgent.showUpdateDialog(LoginActivity.this, updateInfo);
    //                        break;
    //                    //没有更新
    //                    case UpdateStatus.No:
    //                        //没有wifi连接， 只在wifi下更新
    //                    case UpdateStatus.NoneWifi:
    //                        //超时
    //                    case UpdateStatus.Timeout:
    //                        jumpUpdate();
    //                        break;
    //                    default:
    //                        jumpUpdate();
    //                }
    //            }
    //        });
    //
    //        UmengUpdateAgent.setDialogListener(new UmengDialogButtonListener() {
    //
    //            @Override
    //            public void onClick(int status) {
    //                switch (status) {
    //                    case UpdateStatus.Update:
    //                        break;
    //                    case UpdateStatus.Ignore:
    //                        break;
    //                    case UpdateStatus.NotNow:
    //                        jumpUpdate();
    //                        break;
    //                }
    //            }
    //        });
    //    }


    //    public void jumpUpdate() {
    //        startActivity(new Intent(activity, MainActivity.class));
    //        AnimUtil.animToFinish(activity);
    //    }


}
