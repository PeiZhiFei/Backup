package laiyi.tobacco.util;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;

import com.j256.ormlite.android.apptools.OrmLiteSqliteOpenHelper;
import com.j256.ormlite.dao.Dao;
import com.j256.ormlite.support.ConnectionSource;
import com.j256.ormlite.table.TableUtils;

import java.sql.SQLException;

import laiyi.tobacco.bean.Land;
import laiyi.tobacco.bean.User;

public class DBHelper extends OrmLiteSqliteOpenHelper {
	private final static int DATABASE_VERSION = 1;
	Dao<User, Integer> mUserDao;
	Dao<Land, Integer> mLandDao;
	private static final String DB_NAME = "laie-sqlite-gps.db";
	private static DBHelper mDbHelper;

	private DBHelper(Context context) {
		super(context, DB_NAME, null, DATABASE_VERSION);
	}


	public static DBHelper getInstance(Context context) {
		if (mDbHelper == null) {
			mDbHelper = new DBHelper(context);
		}
		return mDbHelper;
	}

	@Override
	public void onCreate(SQLiteDatabase arg0, ConnectionSource arg1) {
		try {
			TableUtils.createTableIfNotExists(connectionSource, User.class);
			TableUtils.createTableIfNotExists(connectionSource, Land.class);
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}


	@Override
	public void onUpgrade(SQLiteDatabase arg0, ConnectionSource arg1, int arg2,
						  int arg3) {
	}

	public Dao<User, Integer> getUserDao() throws SQLException {
		if (mUserDao == null) {
			mUserDao = getDao(User.class);
		}
		return mUserDao;
	}

	public Dao<Land, Integer> getLandDao() throws SQLException {
		if (mLandDao == null) {
			mLandDao = getDao(Land.class);
		}
		return mLandDao;
	}


}  