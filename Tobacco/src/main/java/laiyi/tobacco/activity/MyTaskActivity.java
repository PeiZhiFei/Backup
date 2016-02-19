package laiyi.tobacco.activity;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.util.Base64;
import android.widget.TextView;

import com.alibaba.fastjson.JSON;
import com.j256.ormlite.dao.Dao;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.params.BasicHttpParams;
import org.apache.http.params.HttpConnectionParams;
import org.apache.http.protocol.HTTP;
import org.apache.http.util.EntityUtils;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.ByteArrayOutputStream;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import butterknife.ButterKnife;
import butterknife.OnClick;
import feifei.library.util.L;
import feifei.library.util.Tools;
import laiyi.tobacco.R;
import laiyi.tobacco.bean.Land;
import laiyi.tobacco.bean.User;
import laiyi.tobacco.util.DBHelper;

public class MyTaskActivity extends NetActivity
{
    DBHelper mDbHelper;
    Dao<Land, Integer> mLandDao;
    Dao<User, Integer> mUserDao;
    TextView sezi;
    public int Size = 0; //需要同步的数量
    private static final int REQUEST_TIMEOUT = 5 * 1000;//设置请求超时10秒钟
    private static final int SO_TIMEOUT = 10 * 1000;  //设置等待数据超时时间10秒钟
    List<Land> muser;


    @Override
    protected void onCreate (Bundle savedInstanceState)
    {
        super.onCreate (savedInstanceState);
        setContentView (R.layout.activity_my_task);
        ButterKnife.inject (this);
        tint ();
        dialogInit ();
        initActionbar ();
        setLeftBack ();
        setTitles ("工作任务");
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
        sezi = (TextView) findViewById (R.id.sezi);
        try
        {
            muser = mLandDao.queryBuilder ().where ().notIn ("State", 0).query ();
            sezi.setText ("您有待上传数据" + muser.size () + "条");
        } catch (SQLException e)
        {
            e.printStackTrace ();
        }


    }

    @OnClick(R.id.uptask)
    public void uplad ()
    {
        if ( muser.size () <= 0 )
        {
            Tools.toast (activity, "没有需要同步的数据");
            return;
        }
        try
        {
            //获取更改状态的数据
            List<Land> muser = mLandDao.queryBuilder ().where ().notIn ("State", 0).query ();
            for (int i = 0; i < muser.size (); i++)
            {
                L.l ("id==" + muser.get (i).getId ());
                //查询所有数据
                Land lan = mLandDao.queryForId (muser.get (i).getId ());
                if ( !Tools.isEmpty (lan.getPic1 ()) )
                {
                    lan.setPic1 (bitmapToString (lan.getPic1 ()));
                } else if ( !Tools.isEmpty (lan.getPic2 ()) )
                {
                    lan.setPic2 (bitmapToString (lan.getPic2 ()));
                } else if ( !Tools.isEmpty (lan.getPic3 ()) )
                {
                    lan.setPic3 (bitmapToString (lan.getPic3 ()));
                }
                muser.set (i, lan);
            }

            url = "http://www.yanyeapp.com/app/@IData/Farm@App.ashx?command=up_landpatch";
            //// TODO: 2015/9/30 测试上传
            mParams.put ("key", "2EG24AFF88Y66DD0B5CB3DS37AGFD9J380D45B1B");
            mParams.put ("machinecode", Tools.getUDID (activity));
            mParams.put ("landlistjson", JSON.toJSONString (muser));
            L.l (JSON.toJSONString (muser));
            loadData ();
        } catch (SQLException e)
        {
            e.printStackTrace ();
        }

        //        new Thread(driveServer).start();
      /*  new AsyncTask<Void, Void, Void>() {
            @Override
            protected Void doInBackground(Void... voids) {
                String pic ="/storage/sdcard0/Pictures/JPEG_20151007_185737_-289853487.jpg";
                L.l(MyConstant.UPDIMAGE);
                //bitmapToString(pic);
//                insuranceServer(MyConstant.UPDIMAGE, "3", bitmapToString(pic));

                L.l("insuranceServer");
                boolean loginValidate = false;
                //使用apache HTTP客户端实现
                HttpPost request = new HttpPost(MyConstant.UPDIMAGE);
                L.l ("insuranceServer");
                //如果传递参数多的话，可以丢传递的参数进行封装
                List<NameValuePair> params = new ArrayList<NameValuePair>();
                //添加传参
                params.add (new BasicNameValuePair("key", "2EG24AFF88Y66DD0B5CB3DS37AGFD9J380D45B1B"));
                params.add (new BasicNameValuePair ("landsid", "1223333"));
                params.add(new BasicNameValuePair("picindex",  "3"));
                params.add(new BasicNameValuePair("overwrite", "true"));
                params.add(new BasicNameValuePair("machinecode", Tools.getUDID(activity)));
                params.add(new BasicNameValuePair("image",bitmapToString(pic)));
                try
                {
                    //设置请求参数项
                    request.setEntity((HttpEntity) new UrlEncodedFormEntity(params, HTTP.UTF_8));
                    HttpClient client = getHttpClient();
                    //执行请求返回相应
                    HttpResponse response = client.execute(request);
                    //判断是否请求成功
                    if(response.getStatusLine().getStatusCode()==200)
                    {
                        loginValidate = true;
                        //获得响应信息
                        String responseMsg = EntityUtils.toString(response.getEntity());
                        L.l("resMsg==="+responseMsg);
                        JSONObject jsonResult = new JSONObject(responseMsg);
                        //返回100
                        if (Tools.isSuccess(jsonResult)) {
                            L.l("成功");
                        } else {
                        }
                    }
                }catch(Exception e)
                {
                    e.printStackTrace();
                }
                return null;
            }

            @Override
            protected void onPostExecute(Void aVoid) {
                super.onPostExecute(aVoid);
            }
        }.execute();
*/
       /* try {

            //查询所有数据
            Land lan=mLandDao.queryForId(24);
            L.l("获取到的name==" + lan.getLandName());
            lan.setLandName("周建国");
            lan.setState(2); //2修改
            mLandDao.createOrUpdate(lan);

            //查询更改了state的所有数据
             List<Land> muser=mLandDao.queryBuilder().where().notIn("State", 0).query();
            for(int i =0;i<muser.size();i++){
                L.l("id=="+muser.get(i).getId());
                L.l(""+muser.get(i).getLandName());
                L.l("state=="+muser.get(i).getState());
            }
*//*
            //查询所有地块数据
            List<Land> muser=mLandDao.queryBuilder().query();
            for(int i =0;i<muser.size();i++){
                L.l("id=="+muser.get(i).getId());
                L.l(""+muser.get(i).getLandName());
            }
            //封装json
            L.l("muser===" + JSON.toJSONString(muser));*//*
            url = MyConstant.CUPLANDPATH;
            //// TODO: 2015/9/30 测试上传
            mParams.put("key", "2EG24AFF88Y66DD0B5CB3DS37AGFD9J380D45B1B");
            mParams.put("machinecode", Tools.getUDID(activity));
            mParams.put("landlistjson",JSON.toJSONString(muser));
            loadData();

        }catch (SQLException e){
            e.printStackTrace();
        }*/

    }


    @Override
    protected void getData (JSONObject jsonResult) throws JSONException
    {
        //返回100
        if ( Tools.isSuccess (jsonResult) )
        {
            Tools.toast (activity, "同步数据成功");
            sezi.setText ("您有待上传数据0条");
            try
            {
                //查询不是删除的成功后修改状态为0
                muser = mLandDao.queryBuilder ().where ().notIn ("State", 3).query ();
                L.l (muser.size ());

                for (int i = 0; i < muser.size (); i++)
                {
                    Land lan = mLandDao.queryForId (muser.get (i).getId ());
                    lan.setState (0);
                    mLandDao.createOrUpdate (lan);
                }
                //删除状态为3的数据
                List<Land> upd = mLandDao.queryBuilder ().where ().eq ("State", "3").query ();
                L.l (upd.size ());
                for (int i = 0; i < upd.size (); i++)
                {
                    mLandDao.deleteById (upd.get (i).getId ());
                }

            } catch (SQLException e)
            {
                e.printStackTrace ();
            }
        }

    }

    /**
     * 图片转流
     */
    public String bitmaptoString (Bitmap bitmap)
    {
        // 将Bitmap转换成字符串
        String string = null;
        ByteArrayOutputStream bStream = new ByteArrayOutputStream ();
        bitmap.compress (Bitmap.CompressFormat.PNG, 100, bStream);
        byte[] bytes = bStream.toByteArray ();
        string = Base64.encodeToString (bytes, Base64.DEFAULT);
        return string;
    }

    /* Runnable driveServer = new Runnable() {
         public void run() {
             // TODO Auto-generated method stub
             String pic ="/storage/sdcard0/Pictures/JPEG_20151007_185737_-289853487.jpg";
             L.l(MyConstant.UPDIMAGE);
             //bitmapToString(pic);
             insuranceServer(MyConstant.UPDIMAGE, "3", bitmapToString(pic));
         }
     };*/
    private boolean insuranceServer (String url, String imageindex, String pic)
    {
        L.l ("insuranceServer");
        boolean loginValidate = false;
        //使用apache HTTP客户端实现
        HttpPost request = new HttpPost (url);
        L.l ("insuranceServer");
        //如果传递参数多的话，可以丢传递的参数进行封装
        List<NameValuePair> params = new ArrayList<NameValuePair> ();
        //添加传参
        params.add (new BasicNameValuePair ("key", "2EG24AFF88Y66DD0B5CB3DS37AGFD9J380D45B1B"));
        params.add (new BasicNameValuePair ("landsid", "1223333"));
        params.add (new BasicNameValuePair ("picindex", imageindex));
        params.add (new BasicNameValuePair ("overwrite", "true"));
        params.add (new BasicNameValuePair ("machinecode", Tools.getUDID (activity)));
        params.add (new BasicNameValuePair ("image", pic));
        try
        {
            //设置请求参数项
            request.setEntity ((HttpEntity) new UrlEncodedFormEntity (params, HTTP.UTF_8));
            HttpClient client = getHttpClient ();
            //执行请求返回相应
            HttpResponse response = client.execute (request);
            //判断是否请求成功
            if ( response.getStatusLine ().getStatusCode () == 200 )
            {
                loginValidate = true;
                //获得响应信息
                String responseMsg = EntityUtils.toString (response.getEntity ());
                L.l ("resMsg===" + responseMsg);
                JSONObject jsonResult = new JSONObject (responseMsg);
                //返回100
                if ( Tools.isSuccess (jsonResult) )
                {
                    L.l ("成功");
                } else
                {
                }
            }
        } catch (Exception e)
        {
            e.printStackTrace ();
        }
        return loginValidate;
    }

    //初始化HttpClient，并设置超时
    public HttpClient getHttpClient ()
    {
        BasicHttpParams httpParams = new BasicHttpParams ();
        HttpConnectionParams.setConnectionTimeout (httpParams, REQUEST_TIMEOUT);
        HttpConnectionParams.setSoTimeout (httpParams, SO_TIMEOUT);
        HttpClient client = new DefaultHttpClient (httpParams);
        return client;
    }

    //计算图片的缩放值
    public static int calculateInSampleSize (BitmapFactory.Options options, int reqWidth, int reqHeight)
    {
        final int height = options.outHeight;
        final int width = options.outWidth;
        int inSampleSize = 1;

        if ( height > reqHeight || width > reqWidth )
        {
            final int heightRatio = Math.round ((float) height / (float) reqHeight);
            final int widthRatio = Math.round ((float) width / (float) reqWidth);
            inSampleSize = heightRatio < widthRatio ? heightRatio : widthRatio;
        }
        L.l ("inSampleSize" + inSampleSize);
        return inSampleSize;
    }

    // 根据路径获得图片并压缩，返回bitmap用于显示
    public static Bitmap getSmallBitmap (String filePath)
    {
        final BitmapFactory.Options options = new BitmapFactory.Options ();
        options.inJustDecodeBounds = true;
        BitmapFactory.decodeFile (filePath, options);

        // Calculate inSampleSize
        options.inSampleSize = calculateInSampleSize (options, 720, 960);

        // Decode bitmap with inSampleSize set
        options.inJustDecodeBounds = false;

        return BitmapFactory.decodeFile (filePath, options);
    }

    //把bitmap转换成String
    public static String bitmapToString (String filePath)
    {

        Bitmap bm = getSmallBitmap (filePath);
        ByteArrayOutputStream baos = new ByteArrayOutputStream ();
        bm.compress (Bitmap.CompressFormat.JPEG, 30, baos);
        byte[] b = baos.toByteArray ();
        return Base64.encodeToString (b, Base64.DEFAULT);
    }


}
