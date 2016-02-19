package com.runkun.lbsq.utils;

public class MyConstant
{
    // TODO: 2015/8/25 发布时这里改为true，同时关闭log，混淆，提升版本
    public static final boolean DEBUG = false;

    //public static final String TESTURL="http://applingbushequ.nat123.net/lingbushequ/src/lso2o/";
    public static final String TESTURL = "http://jin19880201.xicp.net/lingbushequ/src/lso2o/";
    public static final String TRUEURL = "http://app.lingbushequ.com/";

    public static final String URL_STRING = (MyConstant.DEBUG ? TESTURL : TRUEURL) + "index.php?";
    public static final String API_BASE_URL = (MyConstant.DEBUG ? TESTURL : TRUEURL) + "mobile/api.php?commend=";

    public static final String API_BASE_URL_COUPON = (MyConstant.DEBUG ? TESTURL : TRUEURL) + "mobile/api_coupon.php?commend=";
    public static final String API_BASE_URL_ORDER = (MyConstant.DEBUG ? TESTURL : TRUEURL) + "mobile/api_order.php?commend=";
    public static final String WECHAT_PAY_NOTIFY_URL = (MyConstant.DEBUG ? TESTURL : TRUEURL) + "mobile/notify_url.php";
    public static final String WX_PAY_NOTIFY_URL = (MyConstant.DEBUG ? TESTURL : TRUEURL) + "mobile/alipaynotify_url.php";

    //"http://app.lingbushequ.com/"  TODO: 2015/9/15
//    public static final String URLCATEGORY = "http://applingbushequ.nat123.net/lingbushequ/src/lso2o/"+ "mobile/apigoods_class.php?commend=get_twoclass_goods";
    public static final String URLCATEGORY = "http://app.lingbushequ.com/mobile/apigoods_class.php?commend=" + "get_twoclass_goods";

    public static boolean SHOP_ONLINE = false;

    public static final String CLASSNAMETEJIA = "今日特价";
    public static final String CLASSNAMESHIPIN = "休闲食品";
    public static final String CLASSNAMEJIUSHUI = "酒水/饮料";
    public static final String CLASSNAMELIANGYOU = "粮油调味";

    public static final String CLASSIDTEJIA = String.valueOf(1);
    public static final String CLASSIDSHIPIN = String.valueOf(137);
    public static final String CLASSIDJIUSHUI = String.valueOf(160);
    public static final String CLASSIDLIANGYOU = String.valueOf(153);

    public static final String CLASSIDTEJIA11 = String.valueOf(1);
    public static final String CLASSIDSHIPIN11 = String.valueOf(144);
    public static final String CLASSIDJIUSHUI11 = String.valueOf(161);
    public static final String CLASSIDLIANGYOU11 = String.valueOf(154);

    public static final String FILE_NAME = "sp_file";
    public static final String KEY_GUIDE_FIRST = "isFirstIn";
    public static final String KEY_MEMBERID = "memberId";
    public static final String KEY_MEMBERPHONE = "phone";

    public static final String KEY_ADDCON = "con_address";
    public static final String KEY_ADDCONTACT = "consigner";
    public static final String KEY_ADDMOBILE = "conmobile";
    public static final String KEY_ADDREMARK = "addressRmark";


    public static final int REQUEST_SHOPCARD = 0X000001;
    public static final int REQUEST_MY = 0X000002;
    public static final int REQUEST_SHOPLIST = 0X000003;
    public static final int RESULT_SHOPLIST = 0X000004;
    public static final int RESULT_MAIN = 0X000005;
    public static final int REQUEST_REGISTER = 0X000006;
    public static final int REQUEST_FORGET = 0X000007;
    public static final int REQUEST_COUPON = 0X000010;

    public static final String URLSTORELIST = MyConstant.API_BASE_URL
            + "storelist";
    public static final String URLSELECTCITY = MyConstant.API_BASE_URL + "city";
    public static final String URLLOGIN = MyConstant.API_BASE_URL + "login";
    public static final String URLQUITLOGIN = MyConstant.API_BASE_URL
            + "outlogin";
    public static final String URLADS = MyConstant.API_BASE_URL + "addisplay";
    public static final String URLSMALLCLASS = MyConstant.API_BASE_URL
            + "smallclass";
    public static final String URLJUDGE = MyConstant.API_BASE_URL
            + "addcomment";

    public static final String URLSENDMESSAGE = MyConstant.URL_STRING
            + "act=reg&op=sendmessage";
    public static final String URLSENDMESSAGEFORGET = MyConstant.URL_STRING
            + "act=reg&op=forgetsendmessage";
    public static final String URLFORGETPASSWORD = MyConstant.URL_STRING
            + "act=reg&op=forgetpwd";
    public static final String URLREGISTER = MyConstant.URL_STRING
            + "act=reg&op=register";

    public static final String APP_VERSION_FILE_PATH = "http://app.lingbushequ.com/update/app-version.xml";
    public static final String APPKEY_BAIDUPUSH = "vZRairjPtTnaU9dqIsNyWp5h";

    public static final String FindorderStatus = MyConstant.API_BASE_URL + "findorderstatus";//支付回调接口

    public static BadgeView shopcartBadge = null;
    public static BadgeView bottomShopcartBadge = null;

}
