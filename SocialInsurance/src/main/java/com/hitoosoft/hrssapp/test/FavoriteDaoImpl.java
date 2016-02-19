package com.hitoosoft.hrssapp.test;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

public class FavoriteDaoImpl implements FavoriteDao {

	private DatabaseHelper helper = null;

	public FavoriteDaoImpl(Context context) {
		helper = new DatabaseHelper(context);
	}

	@Override
	public boolean addFavorite(Object[] params) {
		boolean flag = false;
		SQLiteDatabase database = null;
		try {
			String sql = "insert into favorites (title, publishdate, picurl, url) values (?, ?, ?, ?)";
			database = helper.getWritableDatabase();
			database.execSQL(sql, params);
			flag = true;
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			if (null != database) {
				database.close();
			}
		}
		return flag;
	}

	@Override
	public boolean deleteFavorite(Object[] params) {
		boolean flag = false;
		SQLiteDatabase database = null;
		try {
			String sql = "delete from favorites where id = ?";
			database = helper.getWritableDatabase();
			database.execSQL(sql, params);
			flag = true;
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			if (null != database) {
				database.close();
			}
		}
		return flag;
	}

	@Override
	public Map<String, String> viewFavorite(String[] selectionArgs) {
		Map<String, String> map = new HashMap<String, String>();
		SQLiteDatabase database = null;
		try {
			String sql = "select * from favorites where id = ?";
			database = helper.getReadableDatabase();
			Cursor cursor = database.rawQuery(sql, selectionArgs);
			int col_count = cursor.getColumnCount();
			while (cursor.moveToNext()) {
				for (int i = 0; i < col_count; i++) {
					String col_name = cursor.getColumnName(i);
					String col_value = cursor.getString(cursor
							.getColumnIndex(col_name));
					if (null == col_value) {
						col_value = "";
					}
					map.put(col_name, col_value);
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			if (null != database) {
				database.close();
			}
		}
		return map;
	}

	@Override
	public List<Map<String, String>> listFavorite(String[] selectionArgs,
			int from, int sum) {
		List<Map<String, String>> list = new ArrayList<Map<String, String>>();
		SQLiteDatabase database = null;
		try {
			String sql = "select * from favorites order by id desc limit "
					+ sum + " offset " + from;
			database = helper.getReadableDatabase();
			Cursor cursor = database.rawQuery(sql, selectionArgs);
			int col_count = cursor.getColumnCount();
			while (cursor.moveToNext()) {
				Map<String, String> map = new HashMap<String, String>();
				for (int i = 0; i < col_count; i++) {
					String col_name = cursor.getColumnName(i);
					String col_value = cursor.getString(cursor
							.getColumnIndex(col_name));
					if (null == col_value) {
						col_value = "";
					}
					map.put(col_name, col_value);
				}
				list.add(map);
			}
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			if (null != database) {
				database.close();
			}
		}
		return list;
	}

}