package com.runkun.lbsq.busi.util;

public class MyConstant
{

    public static final boolean DEBUG = false;

    private static final String URLPRE = DEBUG ? "http://applingbushequ.nat123.net/lingbushequ/src/lso2o/mobile/apistore_" : "http://app.lingbushequ.com/mobile/apistore_";
    private static final String URLMIDDLEGOODS = "goods";
    private static final String URLMIDDLElOGIN = "login";
    private static final String URLMIDDLECOMMENT = "comment";
    private static final String URLMIDDLEORDER = "order";
    private static final String URLLAST = ".php?commend=";

    public static final String FILE_NAME = "sp_file";
    public static final String KEY_STOREID = "storeId";
    public static final String KEY_STORENAME = "storeName";
    public static final String KEY_STOREACCOUNT = "storeAccount";

    public static final String CLOGIN = URLPRE + URLMIDDLElOGIN + URLLAST + "storeLogin";
    public static final String CFORGET = URLPRE + URLMIDDLElOGIN + URLLAST + "forgetPwd";
    public static final String CSENDMESSAGE = URLPRE + URLMIDDLElOGIN + URLLAST + "forgetSendMessage";
    public static final String CSTORESUBMIT = URLPRE + URLMIDDLElOGIN + URLLAST + "storeOrder";

    public static final String C31 = URLPRE + URLMIDDLEGOODS + URLLAST + "store_goodsclass";
    public static final String C32 = URLPRE + URLMIDDLEGOODS + URLLAST + "get_class_goods";
    public static final String CSEARCHGOOD = URLPRE + URLMIDDLEGOODS + URLLAST + "get_name_goods";
    public static final String C34 = URLPRE + URLMIDDLEGOODS + URLLAST + "get_barcode_goods";
    public static final String C35 = URLPRE + URLMIDDLEGOODS + URLLAST + "barcode_addgoods";
    public static final String C36 = URLPRE + URLMIDDLEGOODS + URLLAST + "add_newgoods";
    public static final String C37 = URLPRE + URLMIDDLEGOODS + URLLAST + "edit_goods";
    public static final String C38 = URLPRE + URLMIDDLEGOODS + URLLAST + "to_edit_goods";
    public static final String C39 = URLPRE + URLMIDDLEGOODS + URLLAST + "copygoods";

    public static final String CFEEDBACK = URLPRE + URLMIDDLECOMMENT + URLLAST + "add_feedback";
    public static final String CSETSTATE = URLPRE + URLMIDDLECOMMENT + URLLAST + "set_storestate";
    public static final String CGETSTATE = URLPRE + URLMIDDLECOMMENT + URLLAST + "get_storestate";
    public static final String CGETCOMMENT = URLPRE + URLMIDDLECOMMENT + URLLAST + "get_comment";

    public static final String CSEARCHORDER = URLPRE + URLMIDDLEORDER + URLLAST + "get_orderinfo";

    public static final String CORDERDAY = URLPRE + URLMIDDLEORDER + URLLAST + "storeDayOrder";
    public static final String CORDERWEEK = URLPRE + URLMIDDLEORDER + URLLAST + "storeWeekinfo";
    public static final String CORDERMONTH = URLPRE + URLMIDDLEORDER + URLLAST + "storeYearinfo";
    public static final String CORDERDAYDETAIL = URLPRE + URLMIDDLEORDER + URLLAST + "storeOrderinfo";


}
