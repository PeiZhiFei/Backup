package com.hitoosoft.hrssapp.database;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class FavoriteDb extends SQLiteOpenHelper {
	public static String name = "table.db";
	public static String table = "favourite";
	public static String id = "_id";
	public static String newsId = "_newsId";
	public static String newsTitle = "_newsTitle";
	public static String newsUrl = "_newsUrl";
	public static String newsPic = "_newsPic";
	public static String newsTime = "_newsTime";
	public static String newsByteContent = "_newsByteContent";

	public static int version = 1;
	// 注意彼此的空格
	String sql = "CREATE TABLE " + table + "(" + id
			+ " INTEGER PRIMARY KEY AUTOINCREMENT," + newsId
			+ " TEXT NOT NULL," + newsTitle + " TEXT NOT NULL," + newsUrl
			+ " TEXT NOT NULL," + newsPic + " TEXT NOT NULL," + newsTime
			+ " TEXT NOT NULL," + newsByteContent + " TEXT" + ")";

	public FavoriteDb(Context context) {
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
