package com.hitoosoft.hrssapp.test;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class DatabaseHelper extends SQLiteOpenHelper {

	private static String name = "hrssmsp.db";
	private static int version = 1;
	
	public DatabaseHelper(Context context) {
		super(context, name, null, version);
	}

	/**
	 * 数据库创建的时候执行一次
	 */
	public void onCreate(SQLiteDatabase db) {
		//创建收藏夹表
		String sql = "create table favorites(id integer primary key autoincrement,title varchar(200),publishdate varchar(20),picurl varchar(100),url varchar(100))";
		db.execSQL(sql);
	}

	@Override
	public void onUpgrade(SQLiteDatabase db, int arg1, int arg2) {
//		String sql = "alter table person add sex varchar(8)";
//		db.execSQL(sql);
	}

}
