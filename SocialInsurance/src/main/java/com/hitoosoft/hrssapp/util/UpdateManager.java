package com.hitoosoft.hrssapp.util;

import java.io.File;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.HashMap;
import java.util.Map;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

import android.app.AlertDialog;
import android.app.AlertDialog.Builder;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.DialogInterface.OnClickListener;
import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager.NameNotFoundException;
import android.net.Uri;
import android.os.Environment;
import android.os.Handler;
import android.os.Message;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.hitoosoft.hrssapp.R;

public class UpdateManager {

	private static final int GET_VERSION = 0;// 成功获取服务端的版本配置文件
	private static final int DOWNLOAD = 1; // 下载中
	private static final int DOWNLOAD_FINISH = 2; // 下载结束
	private final String savePath = Environment.getExternalStorageDirectory()
			.getPath() + "/download";

	private ProgressBar progressBar;
	private TextView progressTextView;
	private Dialog downloadDialog;

	private boolean cancelUpdate = false; // 是否取消更新
	private int progress;// 进度条刻度
	private Map<String, String> versionMap;
	private int fromWhere = -1; // 1来自检查更新

	private final Context context;

	public UpdateManager(Context context) {
		this.context = context;
	}

	public UpdateManager(Context context, int fromWhere) {
		this.context = context;
		this.fromWhere = fromWhere;
	}

	private final Handler handler = new Handler() {
		public void handleMessage(Message msg) {
			switch (msg.what) {
			case GET_VERSION:// 成功获取服务端的版本配置文件
				int versionCode = getVersionCode(context);// 获取当前应用版本
				int serverVersionCode = Integer.valueOf(versionMap
						.get("version"));
				if (serverVersionCode > versionCode) {// 有新版本
					showNoticeDialog();
				} else if (serverVersionCode == versionCode && 1 == fromWhere) {
					ToastUtil.toast(context, "已是最新版无需升级");
				}
				break;
			case DOWNLOAD:// 正在下载
				progressBar.setProgress(progress);// 设置进度条位置
				progressTextView.setText("正在下载……" + progress);
				break;
			case DOWNLOAD_FINISH:
				installApk();// 安装文件
				break;
			case -3: // 异常信息，调试用
				ToastUtil.toast(context, String.valueOf(msg.obj));
				break;
			default:
				break;
			}
		};
	};

	/**
	 * 检查是否有新版本发布，如有新版本提示下载更新
	 */
	public void checkUpdate() {
		// 开启线程获取服务器端的版本配置文件
		new Thread(new Runnable() {
			@Override
			public void run() {
				try {
					InputStream is = new URL(
							HrssConstants.APP_VERSION_FILE_PATH).openStream();
					// 解析xml文件，获取最新版本
					versionMap = getServerVersionMap(is);
					if (null != is) {
						is.close();
					}
					handler.sendEmptyMessage(GET_VERSION);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		}).start();
	}

	/**
	 * 获取当前应用版本
	 */
	private int getVersionCode(Context context) {
		int versionCode = 0;
		try {
			PackageInfo info = context.getPackageManager().getPackageInfo(
					context.getPackageName(), 0);
			versionCode = info.versionCode; // 当前版本的版本号
		} catch (NameNotFoundException e) {
			e.printStackTrace();
		}
		return versionCode;
	}

	/**
	 * 解析服务器端xml文件获取最新版本号
	 * 
	 * @param is
	 * @return
	 * @throws Exception
	 */
	private Map<String, String> getServerVersionMap(InputStream is)
			throws Exception {
		Map<String, String> versionMap = new HashMap<String, String>();
		// 实例化一个文档构建器工厂
		DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
		// 通过文档构建器工厂获取一个文档构建器
		DocumentBuilder builder = factory.newDocumentBuilder();
		// 通过文档构建器构建一个文档实例
		Document document = builder.parse(is);
		// 获取XML文件根节点
		Element root = document.getDocumentElement();
		// 获得所有子节点
		NodeList childNodes = root.getChildNodes();
		for (int j = 0; j < childNodes.getLength(); j++) {
			Node childNode = childNodes.item(j);
			if (childNode.getNodeType() == Node.ELEMENT_NODE) {
				Element childElement = (Element) childNode;
				if ("version".equals(childElement.getNodeName())) {
					versionMap.put("version", childElement.getFirstChild()
							.getNodeValue());
				} else if (("name".equals(childElement.getNodeName()))) {
					versionMap.put("name", childElement.getFirstChild()
							.getNodeValue());
				} else if (("url".equals(childElement.getNodeName()))) {
					versionMap.put("url", childElement.getFirstChild()
							.getNodeValue());
				}
			}
		}
		return versionMap;
	}

	/**
	 * 更新提示
	 */
	private void showNoticeDialog() {
		AlertDialog.Builder builder = new Builder(context);
		builder.setTitle("软件更新");
		builder.setMessage("检测到新版本，立即更新吗？");
		// 更新
		builder.setPositiveButton("更新", new OnClickListener() {
			@Override
			public void onClick(DialogInterface dialog, int which) {
				dialog.dismiss();
				showDownloadDialog();// 显示下载对话框
			}
		});
		// 稍后更新
		builder.setNegativeButton("稍后更新", new OnClickListener() {
			@Override
			public void onClick(DialogInterface dialog, int which) {
				dialog.dismiss();
			}
		});
		Dialog noticeDialog = builder.create();
		noticeDialog.show();
	}

	/**
	 * 显示下载对话框
	 */
	private void showDownloadDialog() {
		AlertDialog.Builder builder = new Builder(context);
		builder.setTitle("正在下载");
		// 给下载对话框增加进度条
		LayoutInflater inflater = LayoutInflater.from(context);
		View v = inflater.inflate(R.layout.update_progress, null);
		progressTextView = (TextView) v.findViewById(R.id.update_text);
		progressBar = (ProgressBar) v.findViewById(R.id.update_progress);
		builder.setView(v);
		// 取消更新
		builder.setNegativeButton("取消", new OnClickListener() {
			@Override
			public void onClick(DialogInterface dialog, int which) {
				dialog.dismiss();
				cancelUpdate = true;// 设置取消状态
			}
		});
		downloadDialog = builder.create();
		downloadDialog.show();
		// 开启线程下载应用
		new DownloadApkThread().start();
	}

	private class DownloadApkThread extends Thread {

		@Override
		public void run() {
			try {
				if (Environment.getExternalStorageState().equals(
						Environment.MEDIA_MOUNTED)) {
					URL url = new URL(versionMap.get("url"));
					// 创建连接
					HttpURLConnection conn = (HttpURLConnection) url
							.openConnection();
					conn.connect();
					int length = conn.getContentLength();// 获取文件大小
					InputStream is = conn.getInputStream();// 创建输入流
					File fileDir = new File(savePath);
					if (!fileDir.exists()) {
						fileDir.mkdirs();
					}
					File apkFile = new File(savePath, versionMap.get("name"));
					if (!apkFile.exists()) {
						apkFile.createNewFile();
					}
					FileOutputStream fos = new FileOutputStream(apkFile);
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
					} while (!cancelUpdate);// 点击取消就停止下载.
					fos.close();
					is.close();
				}
			} catch (Exception e) {
				e.printStackTrace();
				Message msg = new Message();
				msg.what = -3;
				msg.obj = "下载出现异常:" + e.getMessage();
				handler.sendMessage(msg);
			}
			downloadDialog.dismiss();// 取消下载对话框显示
		}

	}

	/**
	 * 安装APK文件
	 */
	private void installApk() {
		File apkfile = new File(savePath, versionMap.get("name"));
		if (!apkfile.exists()) {
			return;
		}
		// 通过Intent安装APK文件
		Intent intent = new Intent(Intent.ACTION_VIEW);
		intent.addCategory("android.intent.category.DEFAULT");
		intent.setDataAndType(Uri.fromFile(apkfile),
				"application/vnd.android.package-archive");
		context.startActivity(intent);
	}
}