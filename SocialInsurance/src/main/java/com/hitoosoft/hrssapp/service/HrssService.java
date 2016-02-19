package com.hitoosoft.hrssapp.service;

import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

import org.json.JSONException;

import com.hitoosoft.hrssapp.R;
import com.hitoosoft.hrssapp.acitivity.WebActivity;
import com.hitoosoft.hrssapp.entity.OperResult;
import com.hitoosoft.hrssapp.entity.ServiceMessage;
import com.hitoosoft.hrssapp.util.AsyncHttpGetTask;
import com.hitoosoft.hrssapp.util.HrssConstants;
import com.hitoosoft.hrssapp.util.SpFactory;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Intent;
import android.os.Handler;
import android.os.IBinder;
import android.os.Message;
import android.util.Log;

public class HrssService extends Service {
	private static final String TAG = "HRSSSERVICE";
	
	private NotificationManager notificationManager;
	private Timer timer;
	
	@Override
	public IBinder onBind(Intent intent) {
		return null;
	}
	
	private Handler handler = new Handler(){
		@Override
		public void handleMessage(Message msg) {
			if(HrssConstants.SUCCESS == msg.what){
				OperResult operResult = OperResult.fromJsonToObject((String) msg.obj);
				if ("W0001".equals(operResult.getOperCode())) {
					Calendar calendar = Calendar.getInstance();
					try {
						calendar.setTimeInMillis(operResult.getData().getLong("nextDate"));
					} catch (JSONException e) {
						e.printStackTrace();
					}
					SpFactory.saveNextDate(getApplicationContext(), calendar.getTime());
					List<ServiceMessage> list = ServiceMessage.getDataFromJson(operResult.getDataArray());
					int pendingIntentId = 0, notificationId = 0;
					for(ServiceMessage message : list){
						Notification notification = new Notification(R.drawable.ic_launcher, "消息推送", System.currentTimeMillis());
						Intent intent = new Intent(getApplicationContext(), WebActivity.class);
						intent.putExtra("openUrl", message.getUrl());
						intent.putExtra("pushMsg", true);
						PendingIntent pendingIntent = PendingIntent.getActivity(getApplicationContext(), pendingIntentId ++, intent, PendingIntent.FLAG_UPDATE_CURRENT);
						notification.setLatestEventInfo(getApplicationContext(), message.getTitle(), message.getUrl(), pendingIntent); 
						notification.flags = Notification.FLAG_AUTO_CANCEL;//点击后自动消失
	                    notification.defaults = Notification.DEFAULT_SOUND;//声音默认
	                    notificationManager.notify(notificationId ++ , notification);//发通知,id由自己指定，每一个Notification对应的唯一标志
					}
				}
			}
		}
	};

	@Override
	public void onCreate() {
		super.onCreate();
		Log.i(TAG, "手机人社消息推送服务启动了");
		SpFactory.saveNextDate(getApplicationContext(), new Date());
		notificationManager = (NotificationManager) getSystemService(NOTIFICATION_SERVICE);
		timer = new Timer(true);//守护线程
		timer.schedule(new TimerTask() {
			@Override
			public void run() {
				String pushDate = SpFactory.getNextDate(getApplicationContext());
				AsyncHttpGetTask task = new AsyncHttpGetTask(HrssConstants.HRSSMSP_SERVICE_MSG_NEWS + "?pushDate=" + pushDate, handler);
				task.run();
			}
		}, 5 * 1000, 60 * 1000);//开启服务5秒后启动线程，并且每一小时循环一次
	}

}