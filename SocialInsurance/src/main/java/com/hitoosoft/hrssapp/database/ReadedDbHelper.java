package com.hitoosoft.hrssapp.database;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

public class ReadedDbHelper {
	ReadedDb db2 = null;
	Context context;

	public ReadedDbHelper(Context context) {
		this.context = context;
		db2 = new ReadedDb(context);
	}

	// 添加一条
	public void insert(String newsId) {
		SQLiteDatabase database = null;
		try {
			if (!isExit(newsId)) {
				database = db2.getWritableDatabase();
				ContentValues values = new ContentValues();
				values.put(ReadedDb.newsId, newsId);
				database.insert(ReadedDb.table, null, values);
			}
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			if (null != database) {
				database.close();
			}
		}
	}

	// 返回所有数据
	public Cursor query2() {
		SQLiteDatabase database = null;
		Cursor cursor = null;
		try {
			database = db2.getReadableDatabase();
			cursor = database.query(ReadedDb.table, null, null, null, null, null,
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
			database = db2.getReadableDatabase();
			Cursor cursor = database.query(ReadedDb.table, null, ReadedDb.newsId + "=?",
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

}
