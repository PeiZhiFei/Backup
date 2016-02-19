package laiyi.tobacco.util;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Environment;
import android.os.Handler;
import android.os.Message;

import com.afollestad.materialdialogs.GravityEnum;
import com.afollestad.materialdialogs.MaterialDialog;
import com.afollestad.materialdialogs.Theme;

import java.io.File;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;


public class Download
{
    private int progress;
    private Context context;
    private String url;

    private final int DOWNLOAD = 1;
    private final int DOWNLOAD_FINISH = 2;
    private final int DOWNLOAD_ERROR = 3;

//    private ProgressBar progressBar;
//    private TextView progressTextView;
    private MaterialDialog dialog;

    void download(Context context, String url) {
        this.context = context;
        this.url = url;

        dialog = new MaterialDialog.Builder(context)
                .title("软件更新")
                .titleGravity(GravityEnum.CENTER)
                .titleColorRes(android.R.color.holo_purple)
                .content("准备下载")
                .contentGravity(GravityEnum.CENTER)
                .contentColorRes(android.R.color.black)
                .theme(Theme.LIGHT)
                .progress(false, 100, true)
                .show();


//        dialog = new Dialog(context, com.feifei.todo.R.style.dialog_trans_style);
//        LayoutInflater inflater = LayoutInflater.from(context);
//        View v = inflater.inflate(R.layout.update_progress, null);
//        progressTextView = (TextView) dialog.getContentView().findViewById(R.id.update_text);
//        progressBar = (ProgressBar) v.findViewById(R.id.update_progress);
//        dialog.setContentView(v, new android.view.ViewGroup.LayoutParams((int) ((double) Tools.getWidth(context) * 0.9D), Tools.dp2px(context, 50)));
//        dialog.show();
        new DownloadApkThread().start();
    }

    private class DownloadApkThread extends Thread
    {

        @Override
        public void run() {
            try {
                if ( Environment.getExternalStorageState ().equals(
                        Environment.MEDIA_MOUNTED)) {
                    URL url2 = new URL (url);
                    // 创建连接
                    HttpURLConnection conn = (HttpURLConnection) url2
                            .openConnection();
                    conn.connect();
                    int length = conn.getContentLength();// 获取文件大小
                    InputStream is = conn.getInputStream();// 创建输入流
                    File fileDir = new File (savePath);
                    if (!fileDir.exists()) {
                        fileDir.mkdirs();
                    }
                    File apkFile = new File (savePath, "sanguo.apk");
                    if (!apkFile.exists()) {
                        apkFile.createNewFile();
                    }
                    FileOutputStream fos = new FileOutputStream (apkFile);
                    int count = 0;
                    byte buf[] = new byte[1024];
                    do {
                        int numread = is.read(buf);
                        count += numread;
                        progress = (int) (((float) count / length) * 100);// 计算进度条位置
                        handler.sendEmptyMessage(DOWNLOAD);// 更新进度
                        if (numread <= 0) { // 下载完成
                            handler.sendEmptyMessage(DOWNLOAD_FINISH);
                            break;
                        }
                        fos.write(buf, 0, numread);
                    } while (true);
                    fos.close();
                    is.close();
                }
            } catch (Exception e) {
                e.printStackTrace();
                handler.sendEmptyMessage(DOWNLOAD_ERROR);
            }
        }

    }

    private final String savePath = Environment.getExternalStorageDirectory ()
            .getPath() + "/download";

    /**
     * 安装APK文件
     */
    //TODO
    private void installApk() {
        File apkfile = new File (savePath, "sanguo.apk");
        if (!apkfile.exists()) {
            return;
        }
        // 通过Intent安装APK文件
        Intent intent = new Intent (Intent.ACTION_VIEW);
        intent.addCategory("android.intent.category.DEFAULT");
        intent.setDataAndType(Uri.fromFile (apkfile),
                "application/vnd.android.package-archive");
        context.startActivity(intent);
    }

    Handler handler = new Handler (new Handler.Callback() {
        public boolean handleMessage(Message msg) {
            if (msg.what == DOWNLOAD) {
                // 更新下载进度
                dialog.setProgress(progress);
//                dialog.setContent("已下载" + progress + "%");
                dialog.setContent("正在下载，请稍后");
//                progressBar.setProgress(progress);
//                progressTextView.setText("已下载" + progress + "%");
            } else if (msg.what == DOWNLOAD_FINISH) {
                dialog.setProgress(100);
                dialog.setContent("下载完成");
//                progressBar.setProgress(100);
//                progressTextView.setText("下载完成");
                dialog.dismiss();
                installApk();
            } else if (msg.what == DOWNLOAD_ERROR) {
                dialog.setContent("下载失败");
//                progressTextView.setText("下载失败");
            }
            return false;
        }
    }

    );
}
