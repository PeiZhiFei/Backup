package com.hitoosoft.hrssapp.test;

import java.util.List;
import java.util.Map;

public interface FavoriteDao {
	
	/**
	 * 添加收藏
	 */
	public boolean addFavorite(Object[] params);
	
	/**
	 * 删除收藏
	 */
	public boolean deleteFavorite(Object[] params);
	
	/**
	 * 查询明细
	 */
	public Map<String, String> viewFavorite(String[] selectionArgs);
	
	/**
	 * 分页查询列表，from 从第几条开始， sum 返回多少条记录
	 */
	public List<Map<String, String>> listFavorite(String[] selectionArgs, int from, int sum);
}