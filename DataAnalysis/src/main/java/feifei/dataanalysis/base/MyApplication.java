package feifei.dataanalysis.base;

import android.app.Application;

import com.duowan.mobile.netroid.Network;
import com.duowan.mobile.netroid.RequestQueue;
import com.duowan.mobile.netroid.cache.DiskCache;
import com.duowan.mobile.netroid.stack.HurlStack;
import com.duowan.mobile.netroid.toolbox.BasicNetwork;
import com.tendcloud.tenddata.TCAgent;

import org.apache.http.protocol.HTTP;

import java.io.File;

import feifei.project.util.L;

public class MyApplication extends Application {
    //是否需要网络
    public static final boolean NEEDNET = false;

    //发布的时候改为false，同时版本号加1
    public static boolean debug = true;

    //这个文件是控制登录和热词的总开关
    public static final String KEY_LOGIN_FILE = "config_file";

    //改登录要改这里的key，不是文件
    public static final String KEY_LOGIN = "login2";
    //更换热词改这里的key和文件，第一次才初始化热词
    public static final String KEY_FIRST = "first2";
    public static final String KEY_HOTWORD_FILE = "hot_words2";


    public static RequestQueue mRequestQueue;
    public static final int HTTP_DISK_CACHE_SIZE = 50 * 1024 * 1024; // 50MB
    public static final String HTTP_DISK_CACHE_DIR_NAME = "netroid";
    public static final String USER_AGENT = "netroid.cn";

    @Override
    public void onCreate() {
        super.onCreate();
        if (!debug) {
            //不是调试即发布版本就执行统计，关闭log
            TCAgent.init(this);
            TCAgent.setReportUncaughtExceptions(true);
            L.setDEBUG (false);
        }
        initNetroid();
    }

    public void initNetroid() {
        if (mRequestQueue != null) throw new IllegalStateException("initialized");
        // 创建Netroid主类，指定硬盘缓存方案
        Network network = new BasicNetwork(new HurlStack(USER_AGENT, null), HTTP.UTF_8);
        mRequestQueue = new RequestQueue(network, 4, new DiskCache(
                new File(getCacheDir(), HTTP_DISK_CACHE_DIR_NAME), HTTP_DISK_CACHE_SIZE));
        mRequestQueue.start();
    }
}
