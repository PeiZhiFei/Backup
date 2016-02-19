package com.hitoosoft.hrssapp.test;

import android.content.ContentProvider;
import android.content.ContentUris;
import android.content.ContentValues;
import android.content.Context;
import android.content.UriMatcher;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteDatabase.CursorFactory;
import android.database.sqlite.SQLiteOpenHelper;
import android.database.sqlite.SQLiteQueryBuilder;
import android.net.Uri;
import android.text.TextUtils;

public class FavouriteContentProvider extends ContentProvider {

	public static final Uri CONTENT_URI = Uri.parse("content://com.hitoosoft.hrssapp/favourite");

	public static final String DATABASR_NAME = "favouriteDatabase.db";
	public static final String DATABASR_TABLE = "favouriteTable";
	public static final int DATABASR_VERSION = 1;

	public static final String KEY_ID = "id";
	public static final String KEY_TITLE = "task";
	public static final String KEY_TIME = "date";
	public static final String KEY_IMAGE = "complete";
	public static final String KEY_PAGE = "complete";

	public static final int ALLOWS = 1;
	public static final int SINGLEROW = 2;

	SQLiteDatabase db;
	String groupBy = null;
	String having = null;
	private static final UriMatcher uriMatcher;
	static {
		uriMatcher = new UriMatcher(UriMatcher.NO_MATCH);
		uriMatcher.addURI("com.example.app", "tidoitems", ALLOWS);
		uriMatcher.addURI("com.example.app", "tidoitems/#", SINGLEROW);
	}

	// 创建新数据库的sql语句
	private static final String DATABASR_CREATE = "create table " + DATABASR_TABLE + " (" + KEY_ID
			+ " integer primary key autoincrement, " + KEY_TITLE + " text not null, " + KEY_TIME
			+ " text not null, " + KEY_IMAGE + " text not null, " + KEY_PAGE + " text not null);";
	private Helper sqlhelper;

	private static class Helper extends SQLiteOpenHelper {

		public Helper(Context context, String name, CursorFactory factory, int version) {
			super(context, name, factory, version);
		}

		@Override
		public void onCreate(SQLiteDatabase db) {
			db.execSQL(DATABASR_CREATE);
		}

		@Override
		public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
			db.execSQL("DROP TABLE IF EXISTS " + DATABASR_CREATE);
			onCreate(db);
		}
	}

	@Override
	public int delete(Uri uri, String selection, String[] selectionArgs) {
		switch (uriMatcher.match(uri)) {
		case SINGLEROW:
			String rowIDString = uri.getPathSegments().get(1);
			selection = KEY_ID + "=" + rowIDString
					+ (!TextUtils.isEmpty(rowIDString) ? " AND (" + selection + ')' : "");
			break;
		default:
			break;
		}

		if (selection == null) {
			selection = "1";
		}
		int count = db.delete(DATABASR_TABLE, selection, selectionArgs);
		getContext().getContentResolver().notifyChange(uri, null);
		return count;
	}

	@Override
	public String getType(Uri uri) {
		switch (uriMatcher.match(uri)) {
		case ALLOWS:
			return "vnd.android.cursor.dir/vnd.favourite";
		case SINGLEROW:
			return "vnd.android.cursor.item/vnd.favourite";
		default:
			throw new IllegalArgumentException("不支持的uri" + "uri");
		}
	}

	@Override
	public Uri insert(Uri uri, ContentValues values) {
		String nullColumnHack = null;
		long id = db.insert(DATABASR_TABLE, nullColumnHack, values);
		if (id > -1) {
			Uri insertedidUri = ContentUris.withAppendedId(CONTENT_URI, id);
			getContext().getContentResolver().notifyChange(insertedidUri, null);
			return insertedidUri;
		} else {
			return null;
		}

	}

	@Override
	public boolean onCreate() {
		sqlhelper = new Helper(getContext(), DATABASR_NAME, null, DATABASR_VERSION);
		try {
			db = sqlhelper.getWritableDatabase();
		} catch (Exception e) {
			db = sqlhelper.getReadableDatabase();
		}
		return true;
	}

	@Override
	public Cursor query(Uri uri, String[] projection, String selection, String[] selectionArgs,
			String sortOrder) {

		SQLiteQueryBuilder queryBuilder = new SQLiteQueryBuilder();
		queryBuilder.setTables(DATABASR_TABLE);

		switch (uriMatcher.match(uri)) {
		case SINGLEROW:
			String rowIDString = uri.getPathSegments().get(1);
			queryBuilder.appendWhere((KEY_ID) + "=" + rowIDString);
			break;
		default:
			break;
		}
		Cursor cursor = queryBuilder.query(db, projection, selection, selectionArgs, groupBy,
				having, sortOrder);
		return cursor;
	}

	@Override
	public int update(Uri uri, ContentValues values, String selection, String[] selectionArgs) {
		switch (uriMatcher.match(uri)) {
		case SINGLEROW:
			String rowIDString = uri.getPathSegments().get(1);
			selection = KEY_ID + "=" + rowIDString
					+ (!TextUtils.isEmpty(rowIDString) ? " AND (" + selection + ')' : "");
			break;
		default:
			break;
		}

		int count = db.update(DATABASR_TABLE, values, selection, selectionArgs);
		getContext().getContentResolver().notifyChange(uri, null);
		return count;
	}

}
