package feifei.project.util;

import android.util.Log;

import java.util.Date;
import java.util.List;

public class L
{

	private final static String TAG = "log";

	public static void setDEBUG(boolean DEBUG) {
		L.DEBUG = DEBUG;
	}

	private static boolean DEBUG = true;

	public static void l(int msg) {
		if (DEBUG) {
			Log.e(TAG, "" + msg);
		}
	}

	public static void l(double msg) {
		if (DEBUG) {
			Log.e(TAG, "" + msg);
		}
	}

	public static void l(byte[] msg) {
		if (DEBUG) {
			for (byte element : msg) {
				Log.e(TAG, "" + element);
			}
		}
	}

	public static void l (int[] msg)
	{
		if ( DEBUG )
		{
			for (int element : msg)
			{
				Log.e (TAG, "" + element);
			}
		}
	}

	public static void l(boolean msg) {
		if (DEBUG) {
			Log.e(TAG, "" + msg);
		}

	}

	public static void l(String msg) {
		if (DEBUG) {
			Log.e(TAG, "" + msg);
		}
	}

	public static void l(Date date) {
		if (DEBUG) {
			Log.e(TAG, "" + date.toString());
		}
	}

	public static void l(String[] msg) {
		if (DEBUG) {
			String result = "";
			for (String string : msg) {
				result += string + "\n";
			}
			Log.e(TAG, "" + result);
		}
	}

	public static void l(List<String> msg) {
		if (DEBUG) {
			String result = "";
			for (String string : msg) {
				result += string + "\n";
			}
			Log.e(TAG, "" + result);
		}
	}

	public static void l(String tag, String msg) {
		if (DEBUG) {
			Log.e(tag, "" + msg);
		}
	}

}
