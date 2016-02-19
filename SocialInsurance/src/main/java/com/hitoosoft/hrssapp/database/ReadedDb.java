package com.hitoosoft.hrssapp.database;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

//一列的数据库有什么替代的东西,阅读列表，不含阅读历史记录功能
public class ReadedDb extends SQLiteOpenHelper {
	public static String name = "readed.db";
	public static String table = "readed";
	public static String id = "_id";
	public static String newsId = "_newsId";
	public static String newsReaded = "_newsReaded";

	public static int version = 1;
	// 注意彼此的空格
	String sql = "CREATE TABLE " + table + "(" + id
			+ " INTEGER PRIMARY KEY AUTOINCREMENT," + newsId + " TEXT NOT NULL"
			+ ")";

	public ReadedDb(Context context) {
		super(context, name, null, version);
	}

	@Override
	public void onCreate(SQLiteDatabase db) {
		db.execSQL(sql);

	}

	@Override
	public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
		if (newVersion > oldVersion) {
			onCreate(db);
		}

	}

}
