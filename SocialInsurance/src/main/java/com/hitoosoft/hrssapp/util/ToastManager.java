package com.hitoosoft.hrssapp.util;

import java.lang.ref.WeakReference;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.Queue;
import java.util.WeakHashMap;

import android.annotation.TargetApi;
import android.app.Activity;
import android.app.Application;
import android.app.Application.ActivityLifecycleCallbacks;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;

import com.hitoosoft.hrssapp.R;

/**
 * @author   裴智飞
 * @date       2014-7-28
 * @date       下午8:49:22
 * @file         ToastManager.java
 * @content  ToastCustom的控制类
 */
@TargetApi(Build.VERSION_CODES.ICE_CREAM_SANDWICH)
public class ToastManager extends Handler {

	private static final int MESSAGE_DISPLAY = 0xc2007;
	private static final int MESSAGE_ADD_VIEW = 0xc20074dd;
	private static final int MESSAGE_REMOVE = 0xc2007de1;

	private static WeakHashMap<Activity, ToastManager> toastManager;
	private static ReleaseCallbacks releaseCallbacks;

	private final Queue<ToastCustom> msgQueue;
	// 进出动画和时间参数
	private Animation inAnimation, outAnimation;

	private ToastManager() {
		msgQueue = new LinkedList<ToastCustom>();
	}

	public static synchronized ToastManager obtain(Activity activity) {
		if (toastManager == null) {
			toastManager = new WeakHashMap<Activity, ToastManager>(1);
		}
		ToastManager manager = toastManager.get(activity);
		if (manager == null) {
			manager = new ToastManager();
			ensureReleaseOnDestroy(activity);
			toastManager.put(activity, manager);
		}

		return manager;
	}

	static void ensureReleaseOnDestroy(Activity activity) {
		if (releaseCallbacks == null) {
			releaseCallbacks = new ReleaseCallbacksIcs();
		}
		releaseCallbacks.register(activity.getApplication());
	}

	public static synchronized void release(Activity activity) {
		if (toastManager != null) {
			final ToastManager manager = toastManager.remove(activity);
			if (manager != null) {
				manager.clearAllMsg();
			}
		}
	}

	public static synchronized void clearAll() {
		if (toastManager != null) {
			final Iterator<ToastManager> iterator = toastManager.values()
					.iterator();
			while (iterator.hasNext()) {
				final ToastManager manager = iterator.next();
				if (manager != null) {
					manager.clearAllMsg();
				}
				iterator.remove();
			}
			toastManager.clear();
		}
	}

	public void add(ToastCustom toastCustom) {
		msgQueue.add(toastCustom);
		if (inAnimation == null) {
			inAnimation = AnimationUtils.loadAnimation(
					toastCustom.getActivity(), R.anim.toast_in);
		}
		if (outAnimation == null) {
			outAnimation = AnimationUtils.loadAnimation(
					toastCustom.getActivity(), R.anim.toast_out);
		}
		displayMsg();
	}

	public void clearMsg(ToastCustom toastCustom) {
		if (msgQueue.contains(toastCustom)) {
			removeMessages(MESSAGE_DISPLAY, toastCustom);
			removeMessages(MESSAGE_ADD_VIEW, toastCustom);
			removeMessages(MESSAGE_REMOVE, toastCustom);
			msgQueue.remove(toastCustom);
			removeMsg(toastCustom);
		}
	}

	void clearAllMsg() {
		if (msgQueue != null) {
			msgQueue.clear();
		}
		removeMessages(MESSAGE_DISPLAY);
		removeMessages(MESSAGE_ADD_VIEW);
		removeMessages(MESSAGE_REMOVE);
	}

	private void displayMsg() {
		if (msgQueue.isEmpty()) {
			return;
		}
		final ToastCustom toastCustom = msgQueue.peek();
		final Message msg;
		if (!toastCustom.isShowing()) {
			msg = obtainMessage(MESSAGE_ADD_VIEW);
			msg.obj = toastCustom;
			sendMessage(msg);
		} else {
			msg = obtainMessage(MESSAGE_DISPLAY);
			// 时间的长度在这里
			sendMessageDelayed(msg,
					ToastCustom.LENGTH + inAnimation.getDuration()
							+ outAnimation.getDuration());
		}
	}

	private void removeMsg(final ToastCustom toastCustom) {
		clearMsg(toastCustom);
		final View view = toastCustom.getView();
		ViewGroup parent = ((ViewGroup) view.getParent());
		if (parent != null) {
			outAnimation.setAnimationListener(new OutAnimationListener(
					toastCustom));
			view.startAnimation(outAnimation);
			if (toastCustom.isFloating()) {
				parent.removeView(view);
			} else {
				toastCustom.getView().setVisibility(View.INVISIBLE);
			}
		}

		Message msg = obtainMessage(MESSAGE_DISPLAY);
		sendMessage(msg);
	}

	private void addMsgToView(ToastCustom toastCustom) {
		View view = toastCustom.getView();
		if (view.getParent() == null) {
			toastCustom.getActivity().addContentView(view,
					toastCustom.getLayoutParams());
		}
		view.startAnimation(inAnimation);
		if (view.getVisibility() != View.VISIBLE) {
			view.setVisibility(View.VISIBLE);
		}
		final Message msg = obtainMessage(MESSAGE_REMOVE);
		msg.obj = toastCustom;
		sendMessageDelayed(msg, ToastCustom.LENGTH);
	}

	@Override
	public void handleMessage(Message msg) {
		final ToastCustom toastCustom;
		switch (msg.what) {
		case MESSAGE_DISPLAY:
			displayMsg();
			break;
		case MESSAGE_ADD_VIEW:
			toastCustom = (ToastCustom) msg.obj;
			addMsgToView(toastCustom);
			break;
		case MESSAGE_REMOVE:
			toastCustom = (ToastCustom) msg.obj;
			removeMsg(toastCustom);
			break;
		default:
			super.handleMessage(msg);
			break;
		}
	}

	private static class OutAnimationListener implements
			Animation.AnimationListener {
		private final ToastCustom toastCustom;

		private OutAnimationListener(ToastCustom toastCustom) {
			this.toastCustom = toastCustom;
		}

		public void onAnimationStart(Animation animation) {

		}

		public void onAnimationEnd(Animation animation) {
			if (!toastCustom.isFloating()) {
				toastCustom.getView().setVisibility(View.GONE);
			}
		}

		public void onAnimationRepeat(Animation animation) {

		}
	}

	interface ReleaseCallbacks {
		void register(Application application);
	}

	@TargetApi(Build.VERSION_CODES.ICE_CREAM_SANDWICH)
	static class ReleaseCallbacksIcs implements ActivityLifecycleCallbacks,
			ReleaseCallbacks {
		private WeakReference<Application> mLastApp;

		public void register(Application app) {
			if (mLastApp != null && mLastApp.get() == app) {
				return;
			} else {
				mLastApp = new WeakReference<Application>(app);
			}
			app.registerActivityLifecycleCallbacks(this);
		}

		public void onActivityDestroyed(Activity activity) {
			release(activity);
		}

		public void onActivityCreated(Activity activity,
				Bundle savedInstanceState) {
		}

		public void onActivityStarted(Activity activity) {
		}

		public void onActivityResumed(Activity activity) {
		}

		public void onActivityPaused(Activity activity) {
		}

		public void onActivityStopped(Activity activity) {
		}

		public void onActivitySaveInstanceState(Activity activity,
				Bundle outState) {
		}
	}
}