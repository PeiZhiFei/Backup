package com.hitoosoft.hrssapp.database;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;

import com.hitoosoft.hrssapp.util.ToastUtil;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;

public class FavoriteDbHelper {
	FavoriteDb db = null;
	Context context;

	public FavoriteDbHelper(Context context) {
		this.context = context;
		db = new FavoriteDb(context);

	}

	public void insert(final String newsId, final String newsTitle,
			final String newsUrl, final String newsPic, final String newsTime) {

		SQLiteDatabase database = null;
		try {
			if (!isExit(newsId)) {
				database = db.getWritableDatabase();
				ContentValues values = new ContentValues();
				values.put(FavoriteDb.newsId, newsId);
				values.put(FavoriteDb.newsTitle, newsTitle);
				values.put(FavoriteDb.newsUrl, newsUrl);
				values.put(FavoriteDb.newsPic, newsPic);
				values.put(FavoriteDb.newsTime, newsTime);
				database.insert(FavoriteDb.table, null, values);
			}
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			if (null != database) {
				database.close();
			}
		}
	}

	// 添加一条，这里的判断一下是否有网
	public void insertNet(final String newsId, final String newsTitle,
			final String newsUrl, final String newsPic, final String newsTime) {

		new AsyncTask<Void, Void, String>() {
			@Override
			protected String doInBackground(Void... params) {
				StringBuffer sb = new StringBuffer();
				try {
					URL getUrl = new URL(newsUrl);
					HttpURLConnection connection = (HttpURLConnection) getUrl
							.openConnection();
					connection.connect();

					// 取得输入流，并使用Reader读取
					BufferedReader reader = new BufferedReader(
							new InputStreamReader(connection.getInputStream(),
									"utf-8"));

					String line = "";
					while ((line = reader.readLine()) != null) {
						sb.append(line);
					}
					reader.close();
					// 断开连接
					connection.disconnect();
				} catch (Exception e) {
					e.printStackTrace();
				}
				Log.e("log", sb.toString());
				return sb.toString();
			}

			protected void onPostExecute(String result) {

				SQLiteDatabase database = null;
				try {
					if (!isExit(newsId)) {
						database = db.getWritableDatabase();
						ContentValues values = new ContentValues();
						values.put(FavoriteDb.newsId, newsId);
						values.put(FavoriteDb.newsTitle, newsTitle);
						values.put(FavoriteDb.newsUrl, newsUrl);
						values.put(FavoriteDb.newsPic, newsPic);
						values.put(FavoriteDb.newsTime, newsTime);
						values.put(FavoriteDb.newsByteContent, result);
						database.insert(FavoriteDb.table, null, values);
						ToastUtil
								.toast(context.getApplicationContext(), "收藏成功");
					}
				} catch (Exception e) {
					e.printStackTrace();
				} finally {
					if (null != database) {
						database.close();
					}
				}

			}

		}.execute();

	}

	// 删除某一条，防止sql注入攻击的写法
	public void delete(String newsId) {
		SQLiteDatabase database = null;
		try {
			database = db.getWritableDatabase();
			database.delete(FavoriteDb.table, FavoriteDb.newsId + "=?", new String[] { newsId });
		} catch (Exception e) {
			e.printStackTrace();
			Log.e("log", "没有这一条");
		} finally {
			if (null != database) {
				database.close();
			}
		}
	}

	// 删除某一条，防止sql注入攻击的写法
	public void deleteAll() {
		SQLiteDatabase database = null;
		try {
			database = db.getWritableDatabase();
			database.delete(FavoriteDb.table, null, null);
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			if (null != database) {
				database.close();
			}
		}
	}

	// 返回所有数据
	public ArrayList<Bundle> query() {
		SQLiteDatabase database = null;
		ArrayList<Bundle> arrayList = new ArrayList<Bundle>();
		try {
			database = db.getReadableDatabase();
			Cursor cursor = database.query(FavoriteDb.table, null, null, null, null,
					null, null);

			while (cursor.moveToNext()) {
				Bundle bundle = new Bundle();
				bundle.putString(FavoriteDb.newsId,
						cursor.getString(cursor.getColumnIndex(FavoriteDb.newsId)));
				bundle.putString(FavoriteDb.newsTitle,
						cursor.getString(cursor.getColumnIndex(FavoriteDb.newsTitle)));
				bundle.putString(FavoriteDb.newsUrl,
						cursor.getString(cursor.getColumnIndex(FavoriteDb.newsUrl)));
				bundle.putString(FavoriteDb.newsPic,
						cursor.getString(cursor.getColumnIndex(FavoriteDb.newsPic)));
				bundle.putString(FavoriteDb.newsTime,
						cursor.getString(cursor.getColumnIndex(FavoriteDb.newsTime)));
				bundle.putString(FavoriteDb.newsByteContent, cursor.getString(cursor
						.getColumnIndex(FavoriteDb.newsByteContent)));
				arrayList.add(bundle);
			}
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			if (null != database) {
				database.close();
			}
		}
		return arrayList;
	}

	// 返回所有数据，用的地方需要关闭
	public Cursor query2() {
		SQLiteDatabase database = null;
		Cursor cursor = null;
		try {
			database = db.getReadableDatabase();
			cursor = database.query(FavoriteDb.table, null, null, null, null, null,
					null);
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			if (null != database) {
				// 这里不能关闭
				// database.close();
			}
		}

		return cursor;

	}

	// 是否存在
	public boolean isExit(String newsId) {
		SQLiteDatabase database = null;
		boolean result = false;
		try {
			database = db.getReadableDatabase();
			Cursor cursor = database.query(FavoriteDb.table, null, FavoriteDb.newsId + "=?",
					new String[] { newsId }, null, null, null);
			result = cursor.moveToNext();
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			if (null != database) {
				database.close();
			}
		}
		return result;
	}

	// // 是否阅读过
	// public int isReader(String newsId) {
	// SQLiteDatabase database = null;
	// int result = 0;
	// try {
	// database = db.getReadableDatabase();
	// Cursor cursor = database.query(Db.table, null, Db.newsId + "=?",
	// new String[] { newsId }, null, null, null);
	// while (cursor.moveToNext()) {
	// result = cursor.getInt(cursor
	// .getColumnIndexOrThrow(Db.newsReaded));
	// }
	//
	// } catch (Exception e) {
	// e.printStackTrace();
	// } finally {
	// if (null != database) {
	// database.close();
	// }
	// }
	// return result;
	// }

}
