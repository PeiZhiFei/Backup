package com.runkun.lbsq.busi.activity;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Point;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.provider.MediaStore;
import android.util.Base64;
import android.view.Display;
import android.view.MotionEvent;
import android.view.View;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.TextView;

import com.alibaba.fastjson.JSON;
import com.desmond.squarecamera.CameraActivity;
import com.desmond.squarecamera.ImageUtility;
import com.lidroid.xutils.http.RequestParams;
import com.runkun.lbsq.busi.R;
import com.runkun.lbsq.busi.bean.GoodMore;
import com.runkun.lbsq.busi.util.MyConstant;
import com.runkun.lbsq.busi.util.Pop;
import com.runkun.lbsq.busi.util.Tools;

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

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.util.ArrayList;
import java.util.List;

import butterknife.ButterKnife;
import butterknife.InjectView;
import butterknife.OnClick;
import feifei.project.util.L;
import feifei.project.view.ActionSheet;
import feifei.project.view.ClearEditText;
import feifei.project.view.smartimage.SmartImageView;

/**
 * 商品详情页面
 */
public class ResultActivity extends NetActivity implements ActionSheet.MenuItemClickListener
{
    @InjectView(R.id.add_image)
    ImageView addImage;
    @InjectView(R.id.goodsbarcode)
    ClearEditText goodsbarcode;
    @InjectView(R.id.goodsname)

    ClearEditText goodsname;
    @InjectView(R.id.goodsprice)
    ClearEditText goodsprice;
    @InjectView(R.id.tjprice)
    ClearEditText tjprice;
    @InjectView(R.id.istejia)
    CheckBox istejia;
    @InjectView(R.id.istuijian)
    CheckBox istuijian;
    @InjectView(R.id.cagetory)
    TextView cagetory;

    //图片地址
    public String picPath = "";
    //获取到的网络图片地址
    public String pic="";
    SmartImageView add_image;
    //选择文件
    public static final int TO_SELECT_PHOTO = 3;

    public static String IS_UPDIMG="";

    private ActionSheet menuView;
    List<GoodMore> datas = new ArrayList<> ();
    public String goods_barcode = "";//商品条码
    private String goods_id = "";//商品id

    String class_id, class_name,type;
    String storeId;//店铺id
    String storeName;//店铺名称

    String barcode,name,price,tj,istj,istuij,fl,childid; //获取控件值
    public String PUBLIC_IS_GOOD="";

    public String str=null; //商品库状态
    public String grounding_state=null;
    private static final int REQUEST_TIMEOUT = 5*1000;//设置请求超时10秒钟
    private static final int SO_TIMEOUT = 10*1000;  //设置等待数据超时时间10秒钟

    String responseMsg="";
    String rkey="";

    //初始化HttpClient，并设置超时
    public HttpClient getHttpClient()
    {
        BasicHttpParams httpParams = new BasicHttpParams();
        HttpConnectionParams.setConnectionTimeout (httpParams, REQUEST_TIMEOUT);
        HttpConnectionParams.setSoTimeout(httpParams, SO_TIMEOUT);
        HttpClient client = new DefaultHttpClient (httpParams);
        return client;
    }
    private Point mSize;
    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate (savedInstanceState);
        setContentView (R.layout.activity_result);
        ButterKnife.inject(this);
        tint();
        initActionbar();
        dialogInit();
        setTitles("商品信息");
        Display display = getWindowManager().getDefaultDisplay();
        mSize = new Point();
        display.getSize(mSize);
        SharedPreferences preference = activity.getSharedPreferences (MyConstant.FILE_NAME,
                Context.MODE_PRIVATE);
        storeId = preference.getString (MyConstant.KEY_STOREID, "");
        storeName = preference.getString (MyConstant.KEY_STORENAME, "");
        Intent intent = getIntent ();
        type = intent.getStringExtra ("type");
        rkey = intent.getStringExtra ("rkey");
        //这里接收商品分类进入合扫码获取到的分类信息
        //....
        L.l ("rkey---" + rkey);
        if (type.equals ("have"))
        {
            //根据店铺和商品条码查询
            goods_barcode = intent.getStringExtra ("goods_barcode");
            url = MyConstant.C35;
            rp.addQueryStringParameter ("store_id", storeId);
            //扫码结果
            rp.addQueryStringParameter ("goods_barcode", goods_barcode);
            loadData ();
        } else if(type.equals ("no_have"))
        {
            //根据店铺和商品id查询
            L.l("根据店铺和商品id查询");
            goods_id = intent.getStringExtra ("good_id");
            url = MyConstant.C37;
            rp.addQueryStringParameter ("store_id", storeId);
            rp.addQueryStringParameter ("goods_id", goods_id);
            loadData ();

        }
        add_image = (SmartImageView) findViewById (R.id.add_image);
        add_image.setOnClickListener (new View.OnClickListener ()
        {
            @Override
            public void onClick(View v)
            {
                setTheme (R.style.ActionSheetStyleIOS7);
                menuView = new ActionSheet (activity);
                menuView.setCancelButtonTitle ("取消");
                menuView.addItems ("相机", "相册");
                menuView.setItemClickListener (ResultActivity.this);
                menuView.setCancelableOnTouchMenuOutside (true);
                menuView.showMenu ();
            }
        });

    }

    @Override
    public void onItemClick(int itemPosition)
    {

        switch (itemPosition)
        {
            case 0:
                //takePhoto ();
                takePicture ();
                break;
            case 1:
                openAlbum ();
                break;
        }
    }
    /**
     *选择商品分类获取信息
     */
    @OnClick(R.id.cagetory)
    public void select()
    {
        Pop pop = new Pop (activity, R.style.dialog_loading_style, new Pop.ChildClickListener ()
        {
            @Override
            public void onChildClick(String groupId, String childId, String groupName, String ChildName)
            {
                //                Tools.toast (activity,groupId+""+childId);
                fl=groupName + ">" + ChildName;
                cagetory.setText (fl);
                childid=childId;
            }
        });
        pop.init ();
    }
    /**
     * 使用照相机拍照获取图片
     */
    public static final int SELECT_PIC_BY_TACK_PHOTO = 1;
    /**
     * 使用相册中的图片
     */
    public static final int SELECT_PIC_BY_PICK_PHOTO = 2;

    //打开本地相册
    public void openAlbum(){
        Intent intent = new Intent();
        intent.setType("image/*");
        intent.setAction (Intent.ACTION_GET_CONTENT);
        this.startActivityForResult(intent, SELECT_PIC_BY_PICK_PHOTO);
    }
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult (requestCode, resultCode, data);

        if (resultCode != RESULT_OK) return;

        if (requestCode == REQUEST_CAMERA) {
            Uri photoUri = data.getData();
            String realPath = getRealPathFromURI(photoUri);
            L.l("获取图片成功，path=" + realPath);
            picPath = realPath;
            // Get the bitmap in according to the width of the device
            Bitmap bitmap = ImageUtility.decodeSampledBitmapFromPath(photoUri.getPath(), mSize.x, mSize.x);
            add_image.setImageBitmap(bitmap);
        }else if (requestCode == SELECT_PIC_BY_PICK_PHOTO) {
            if (resultCode == RESULT_OK) {
                Uri uri = data.getData();
                if (uri != null) {
                    String realPath = getRealPathFromURI(uri);
                    L.l("获取图片成功，path=" + realPath);
                    picPath = realPath;
                    Bitmap bm = getimage(picPath);
                    add_image.setImageBitmap(bm);
                } else {
                    Tools.toast(activity, "从相册获取图片失败");
                }
            }
        }
        super.onActivityResult(requestCode, resultCode, data);
      /*  if (requestCode == SELECT_PIC_BY_TACK_PHOTO) {
            if (resultCode == RESULT_OK) {
                L.l ("获取图片成功，path=" + picPath);
                Bitmap bm = getimage (picPath);
                add_image.setImageBitmap (bm);
            } else if (resultCode == RESULT_CANCELED) {
                // 用户取消了图像捕获
            } else {
                // 图像捕获失败，提示用户
                Tools.toast (activity,"拍照失败");
            }
        } else if (requestCode == SELECT_PIC_BY_PICK_PHOTO) {
            if (resultCode == RESULT_OK) {
                Uri uri = data.getData();
                if(uri != null){
                    String realPath = getRealPathFromURI(uri);
                    L.l("获取图片成功，path=" + realPath);
                    picPath=realPath;
                    Bitmap bm = getimage (picPath);
                    add_image.setImageBitmap (bm);
                }else{
                    Tools.toast (activity, "从相册获取图片失败");
                }
            }
        }*/
    }
    public String getRealPathFromURI(Uri contentUri){
        try{
            String[] proj = {MediaStore.Images.Media.DATA};
            // Do not call Cursor.close() on a cursor obtained using this method,
            // because the activity will do that for you at the appropriate time
            Cursor cursor = this.managedQuery(contentUri, proj, null, null, null);
            int column_index = cursor.getColumnIndexOrThrow(MediaStore.Images.Media.DATA);
            cursor.moveToFirst();
            return cursor.getString(column_index);
        }catch (Exception e){
            return contentUri.getPath();
        }
    }

   /* @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data)
    {

        if (resultCode == Activity.RESULT_OK)
        {
            doPhoto (requestCode, data);
        }
        super.onActivityResult (requestCode, resultCode, data);
    }*/

    private Bitmap getimage(String srcPath)
    {
        BitmapFactory.Options newOpts = new BitmapFactory.Options ();
        //开始读入图片，此时把options.inJustDecodeBounds 设回true了
        newOpts.inJustDecodeBounds = true;
        Bitmap bitmap = BitmapFactory.decodeFile (srcPath, newOpts);//此时返回bm为空
        newOpts.inJustDecodeBounds = false;
        int w = newOpts.outWidth;
        int h = newOpts.outHeight;
        //现在主流手机比较多是800*480分辨率，所以高和宽我们设置为
        float hh = 800f;//这里设置高度为800f
        float ww = 480f;//这里设置宽度为480f
        //缩放比。由于是固定比例缩放，只用高或者宽其中一个数据进行计算即可
        int be = 1;//be=1表示不缩放
        if (w > h && w > ww)
        {//如果宽度大的话根据宽度固定大小缩放
            be = (int) (newOpts.outWidth / ww);
        } else if (w < h && h > hh)
        {//如果高度高的话根据宽度固定大小缩放
            be = (int) (newOpts.outHeight / hh);
        }
        if (be <= 0)
            be = 1;
        newOpts.inSampleSize = be;//设置缩放比例
        //重新读入图片，注意此时已经把options.inJustDecodeBounds 设回false了
        bitmap = BitmapFactory.decodeFile (srcPath, newOpts);
        return compressImage (bitmap);//压缩好比例大小后再进行质量压缩
    }

    private Bitmap compressImage(Bitmap image)
    {

        ByteArrayOutputStream baos = new ByteArrayOutputStream ();
        image.compress (Bitmap.CompressFormat.JPEG, 100, baos);//质量压缩方法，这里100表示不压缩，把压缩后的数据存放到baos中
        int options = 100;
        while (baos.toByteArray ().length / 1024 > 100)
        {  //循环判断如果压缩后图片是否大于100kb,大于继续压缩
            baos.reset ();//重置baos即清空baos
            image.compress (Bitmap.CompressFormat.JPEG, options, baos);//这里压缩options%，把压缩后的数据存放到baos中
            options -= 10;//每次都减少10
        }
        ByteArrayInputStream isBm = new ByteArrayInputStream (baos.toByteArray ());//把压缩后的数据baos存放到ByteArrayInputStream中
        Bitmap bitmap = BitmapFactory.decodeStream (isBm, null, null);//把ByteArrayInputStream数据生成图片
        return bitmap;
    }



    private Uri photoUri;
    private static final int REQUEST_CAMERA = 0;
    //拍照
    public void takePicture(){
        Intent startCustomCameraIntent = new Intent(this, CameraActivity.class);
        startActivityForResult(startCustomCameraIntent, REQUEST_CAMERA);
    }


    @Override
    public boolean onTouchEvent(MotionEvent event)
    {
        finish ();
        return super.onTouchEvent (event);
    }




    GoodMore goodsmore = null;
    /**
     *获取返回结果
     */
    @Override
    protected void getData(JSONObject jsonResult) throws JSONException
    {
            if (type.equals ("have")){//根据扫码查询信息
                if(PUBLIC_IS_GOOD.equals ("")){ //不是复制状态
                    L.l("扫码第一次进入");
                    str = jsonResult.getString ("str");//只有扫码才有str的值
                    //存在商品信息加载数据
                    if (!str.equals ("havent"))
                    {
                        goodsmore = JSON.parseObject (jsonResult.getString ("datas"), GoodMore.class);
                        L.l ("扫码赋值");
                        inn ();
                    }else {
                        goodsbarcode.setText (goods_barcode);//条码
                        goodsname.setEnabled (true);
                       /* if((PUBLIC_IS_GOOD.equals ("add_god"))){
                            L.l("新品添加");
                            String state = jsonResult.getString ("state");//获取修改信息时候的
                            if(state.equals ("is_ok")){
                                Tools.toast (activity, "新增商品成功");
                            }else{
                                Tools.toast (activity, "新增商品失败");
                            }
                        }*/
                    }
                }else{
                    if(PUBLIC_IS_GOOD.equals ("copygoods")){
                        L.l("获取复制数据");
                        goodsmore = JSON.parseObject (jsonResult.getString ("datas"), GoodMore.class);
                        L.l("复制后的id=="+goodsmore.getGoods_id ());
                        goods_id=goodsmore.getGoods_id ();
                        str="have_old";
                        PUBLIC_IS_GOOD="yes_copygoods";
                        Add_Save (goodsmore.getGoods_id ());
                    }/*else{
                        String state = jsonResult.getString ("state");//获取修改信息时候的
                        if(state.equals ("is_ok")){
                            Tools.toast (activity, "修改商品信息成功");
                        }else{
                            Tools.toast (activity, "修改商品信息失败");
                        }
                    }*/
                }
            }else if(type.equals ("no_have")){//根据商品id查询信息
                if(Tools.isEmpty (PUBLIC_IS_GOOD))
                {
                    goodsmore = JSON.parseObject (jsonResult.getString ("datas"), GoodMore.class);

                    inn ();
                }/*else{
                    L.l ("获取修改或保存返回操作");
                    String state = jsonResult.getString ("state");//获取修改信息时候的
                    if(state.equals ("is_ok")){
                        Tools.toast (activity, "修改商品信息成功");
                    }else{
                        Tools.toast (activity, "修改商品信息失败");
                    }
            }*/
            }
    }
    /**
     *获取信息后给控件和变量赋值
     */
    public void inn(){
        if(Tools.isEmpty(goodsmore.getGoods_barcode ())){
            goodsbarcode.setText (goods_barcode);
        }else{
            goodsbarcode.setText (goodsmore.getGoods_barcode ());//条码
        }
        goods_id = goodsmore.getGoods_id ();
        class_id = goodsmore.getClass_id ();
        class_name = goodsmore.getClass_name ();
        goodsname.setText (goodsmore.getGoods_name ());
        goodsprice.setText (goodsmore.getGoods_price ());
        tjprice.setText (goodsmore.getTjprice ());
        cagetory.setText (goodsmore.getClass_name ());
        if (goodsmore.getIs_tejia ().equals ("0"))
        {
            istejia.setChecked (false);
        } else
        {
            istejia.setChecked (true);
        }
        if (goodsmore.getIs_tuijian ().equals ("0"))
        {
            istuijian.setChecked (false);
        } else
        {
            istuijian.setChecked (true);
        }
        pic=goodsmore.getGoods_pic ();
        add_image.setImageUrl (goodsmore.getGoods_pic ());
        childid=goodsmore.getClass_id (); //分类id
        fl=goodsmore.getClass_name ();//分类名称

    }
    /**
     *下架
     */
    @OnClick(R.id.down_goods)
    public void downGoods()
    {
        grounding_state="2";
        if(!type.equals ("no_have"))
        {
            if (str.equals ("have_old"))
            {
                Add_Save (goods_id);
            }else{
                Tools.toast (activity, "您不能下架非商品库中的产品");
            }
        }else{
            Add_Save (goods_id);
        }
    }

    /**
     *保存
     */
    @OnClick(R.id.save)
    public void save()
    {
        grounding_state="0";
        Add_Save (goods_id);
    }
    /**
     *保存并上架
     */
    @OnClick(R.id.add_goods)
    public void addGood()
    {
        grounding_state="1";
        Add_Save (goods_id);
    }

    /**
     *图片转流
     */
    public String bitmaptoString(Bitmap bitmap) {
        // 将Bitmap转换成字符串
        String string = null;
        ByteArrayOutputStream bStream = new ByteArrayOutputStream();
        bitmap.compress (Bitmap.CompressFormat.PNG, 100, bStream);
        byte[] bytes = bStream.toByteArray();
        string = Base64.encodeToString(bytes, Base64.DEFAULT);
        return string;

    }

    /**
     *获取商品信息
     */
    public void Save_Attribute()
    {
        barcode=goodsbarcode.getText ().toString ();//扫码
       // L.l (barcode);
        name=goodsname.getText ().toString ();//商品名称
        // L.l (name);
        price=goodsprice.getText ().toString ();//商品价格
        // L.l (price);
        tj=tjprice.getText ().toString ();//商品特价价格
        //L.l (tj);
       //childid  //商品分类id
        //  Log.e ("log",childid);
        // L.l (fl);
        if(istejia.isChecked ()){
            //特价选中
            istj="1";
            // L.l (istj);
        }else{
            //未选特价
            istj="0";
            // L.l (istj);
        }
        if(istuijian.isChecked()){
            //首页推荐
            istuij="1";
            // L.l (istuij);
        }else{
            //未选首页推荐
            istuij="0";
            // L.l (istuij);
        }
    }
    /**
     *提交商品信息
     *
     *@param  id 商品id，如果不存在传null
     */
    public void Add_Save(String id)
    {
        dialogProgress ("加载中");
        if(!Tools.isEmpty (pic)||!Tools.isEmpty (picPath)){
            Save_Attribute();
            if(/*Tools.isEmpty (barcode)||*/Tools.isEmpty (name)||Tools.isEmpty (price)
                    ||Tools.isEmpty (fl)){
                Tools.toast (activity, "请输入商品信息");
                dialogDismiss ();
            }else{
                if(Tools.isEmpty (id)){
                    //添加新品操作
                    L.l ("添加新的商品");
                    IS_UPDIMG="2";
                   new Thread(driveServer).start ();
                }else{
                    if(!type.equals ("no_have"))
                    {
                        if(str.equals ("is_public")){//如果是is_public状态  公共有 调用复制接口获取id
                            L.l("公共状态下复制");
                            //url="http://jin19880201.xicp.net/lingbushequ/src/lso2o/mobile/apistore_goods.php?commend=copygoods";
                            url = MyConstant.C39;
                            rp = new RequestParams ();
                            rp.addQueryStringParameter ("store_id", storeId);
                            rp.addQueryStringParameter ("public_goods_id", goods_id);
                            PUBLIC_IS_GOOD="copygoods";
                            loadData ();
                        }else{
                            L.l("满足复制后根据id修改条件");
                            IS_UPDIMG="1";
                            new Thread(driveServer).start ();
                        }
                    }else{
                        L.l("满足根据id修改条件");
                        IS_UPDIMG="1";
                        new Thread(driveServer).start ();
                    }
            }
            }
        } else
        {
            dialogDismiss ();
            Tools.toast (activity, "请选择商品图片后在进行操作");
        }
    }
    Runnable driveServer = new Runnable() {
        public void run() {
            // TODO Auto-generated method stub
            try {
                if(IS_UPDIMG.equals ("1")){ //根据id添加商品
                    L.l ("开始查询");
                   // String url = "http://jin19880201.xicp.net/lingbushequ/src/lso2o/mobile/apistore_goods.php?commend=to_edit_goods";
                    insuranceServer (MyConstant.C38, goods_id);
                    //insuranceServer (url, goods_id);
                }else if(IS_UPDIMG.equals ("2")){ //添加新的商品
                    //String url = "http://jin19880201.xicp.net/lingbushequ/src/lso2o/mobile/apistore_goods.php?commend=add_newgoods";
                    insuranceServer(MyConstant.C36,null);
                    //insuranceServer(url,null);
                }
            } catch (Exception e) {
                // TODO: handle exception
                e.printStackTrace();
            }
        }
    };

    private boolean insuranceServer(String url,String id)
    {
        L.l("insuranceServer");
        boolean loginValidate = false;
        //使用apache HTTP客户端实现
      //  String urlStr = "http://jin19880201.xicp.net/lingbushequ/src/lso2o/mobile/apistore_goods.php?commend=add_newgoods";
        HttpPost request = new HttpPost(url);
        L.l ("insuranceServer");
        //如果传递参数多的话，可以丢传递的参数进行封装
        List<NameValuePair> params = new ArrayList<NameValuePair>();
        //添加传参
        params.add(new BasicNameValuePair ("goods_name",name));
        L.l ("name===" + name);
        params.add (new BasicNameValuePair ("goods_price", price));
        L.l ("price===" + price);
        params.add (new BasicNameValuePair ("store_id", storeId));
        L.l ("storeId===" + storeId);
        params.add (new BasicNameValuePair ("store_name", storeName));
        L.l ("storeName===" + storeName);
        params.add (new BasicNameValuePair ("class_id", childid));
        L.l ("childid===" + childid);
        params.add (new BasicNameValuePair ("class_name", fl));
        L.l ("fl===" + fl);
        params.add (new BasicNameValuePair ("tjprice", tj));
        L.l ("tj===" + tj);
        params.add (new BasicNameValuePair ("is_tejia", istj));
        L.l ("istj===" + istj);
        params.add (new BasicNameValuePair ("is_tuijian", istuij));
        L.l ("istuij===" + istuij);
        params.add (new BasicNameValuePair ("grounding", grounding_state));
        L.l ("grounding_state===" + grounding_state);
        if(!IS_UPDIMG.equals ("1")){
            params.add (new BasicNameValuePair ("goods_barcode", barcode));
            L.l ("goods_barcode===" + barcode);
        }
        if(Tools.isEmpty (picPath)){
        }else{
            Bitmap bitmap1 = getimage (picPath);
            params.add (new BasicNameValuePair ("goods_pic", bitmaptoString (bitmap1)));
        }
        if(!Tools.isEmpty (id)){
            params.add (new BasicNameValuePair ("goods_id", id));
            L.l ("goods_id==="+id);
        }
        try
        {
            //设置请求参数项
            request.setEntity((HttpEntity) new UrlEncodedFormEntity (params, HTTP.UTF_8));
            HttpClient client = getHttpClient();
            //执行请求返回相应
            HttpResponse response = client.execute(request);
            //判断是否请求成功
            if(response.getStatusLine().getStatusCode()==200)
            {
                loginValidate = true;
                //获得响应信息
                responseMsg = EntityUtils.toString (response.getEntity ());
                if(!Tools.isEmpty (responseMsg)){
                    ht.sendEmptyMessage(0);
                }else{
                    ht.sendEmptyMessage(1);
                }
            }else{
                ht.sendEmptyMessage (1);
            }
        }catch(Exception e)
        {
            ht.sendEmptyMessage (1);
            e.printStackTrace();
        }
        return loginValidate;
    }
    private Handler ht= new Handler(){
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            switch (msg.what) {
                case 0:
                    try
                    {
                        JSONObject jsonResult = new JSONObject (responseMsg);
                        L.l (""+jsonResult);
                        if(Tools.isSuccess (jsonResult)){
                            String state = jsonResult.getString ("state");
                            if(state.equals ("is_ok")){
                                Tools.toast (activity, "修改商品信息成功");
                                dialogDismiss ();
                                if(!Tools.isEmpty (rkey)){
                                    Intent intent = new Intent(activity, CaptureActivity.class);
                                    ResultActivity.this.setResult (8, intent);
                                    ResultActivity.this.finish();
                                    L.l ("分类进入刷新");
                                }
                                ResultActivity.this.finish();
                            }else{
                                Tools.toast (activity, "修改商品信息失败");
                                dialogDismiss ();
                            }
                        }else{
                            dialogDismiss ();
                        }
                    } catch (JSONException e)
                    {
                        e.printStackTrace ();
                    }
                    break;
                case 1:
                    dialogDismiss ();
                    break;
                default:
                    break;
            }
        };
    };
}
