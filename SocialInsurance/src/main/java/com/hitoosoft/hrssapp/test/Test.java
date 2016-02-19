package com.hitoosoft.hrssapp.test;

import java.util.List;
import java.util.Map;

import android.test.AndroidTestCase;
import android.util.Log;


public class Test extends AndroidTestCase {

	public void createDb() {
		DatabaseHelper databaseHelper = new DatabaseHelper(getContext());
		databaseHelper.getWritableDatabase();
	}

	public void insertDb() {
		FavoriteDao dao = new FavoriteDaoImpl(getContext());
		Object[] params = { "标题", "2015年4月30日 12:16:40", "**", "**" };
		boolean result = dao.addFavorite(params);
		Log.i("Test", "-----------------" + result);
	}

	public void deleteDb() {
		FavoriteDao dao = new FavoriteDaoImpl(getContext());
		Object[] params = { 1, 2, 3, 4, 5, 6 };
		boolean result = dao.deleteFavorite(params);
		Log.i("Test", "-----------------" + result);
	}

	public void viewDb() {
		FavoriteDao dao = new FavoriteDaoImpl(getContext());
		String[] params = { "3" };
		Map<String, String> map = dao.viewFavorite(params);
		Log.i("Test", "-----------------" + map.toString());
	}

	public void listDb() {
		FavoriteDao dao = new FavoriteDaoImpl(getContext());
		List<Map<String, String>> list = dao.listFavorite(null, 0, 10);
		Log.i("Test", "-----------------" + list.toString());
	}
}