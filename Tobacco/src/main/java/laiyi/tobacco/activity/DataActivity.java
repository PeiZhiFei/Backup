package laiyi.tobacco.activity;

import android.os.Bundle;

import laiyi.tobacco.util.DBHelper;


public class DataActivity extends BaseActivity
{
    DBHelper mDbHelper;

    @Override
    protected void onCreate (Bundle savedInstanceState)
    {
        super.onCreate (savedInstanceState);
        mDbHelper = DBHelper.getInstance (this);
    }
}
