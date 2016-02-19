package com.hitoosoft.hrssapp.acitivity;

import android.annotation.TargetApi;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.view.ViewGroup.LayoutParams;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.CursorAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.hitoosoft.hrssapp.R;
import com.hitoosoft.hrssapp.database.FavoriteDb;
import com.hitoosoft.hrssapp.database.FavoriteDbHelper;
import com.hitoosoft.hrssapp.util.AnimUtil;
import com.hitoosoft.hrssapp.util.ToastUtil;
import com.hitoosoft.hrssapp.view.SlideCutListView;
import com.hitoosoft.hrssapp.view.SmartImageView;
import com.hitoosoft.hrssapp.view.SlideCutListView.RemoveDirection;
import com.hitoosoft.hrssapp.view.SlideCutListView.RemoveListener;

@TargetApi(Build.VERSION_CODES.HONEYCOMB)
public class FavouriteActivity extends BaseActivity implements
		OnItemClickListener, RemoveListener {
	FavoriteDbHelper dbHelper;
	MyAdapter adapter;
	SlideCutListView listView;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_favourite);
		((TextView) findViewById(R.id.titlebar)).setText("我的收藏");
		dbHelper = new FavoriteDbHelper(this);
		ImageView imageView = (ImageView) findViewById(R.id.actionbar_right);
		imageView.setImageResource(R.drawable.delete_button);

		listView = (SlideCutListView) findViewById(R.id.fav);
		TextView textView = new TextView(this);
		LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(
				LayoutParams.MATCH_PARENT, LayoutParams.MATCH_PARENT,
				Gravity.CENTER);
		layoutParams.gravity = Gravity.CENTER;
		layoutParams.weight = 1;
		layoutParams.setMargins(5, 0, 5, 5);
		textView.setLayoutParams(layoutParams);
		textView.setText("这里是空的" + "\n\n" + "快去收藏文章吧");
		textView.setTextSize(26);
		textView.setTextColor(Color.parseColor("#33cccc"));
		textView.setVisibility(View.GONE);
		textView.setGravity(Gravity.CENTER);
		((ViewGroup) listView.getParent()).addView(textView, layoutParams);
		textView.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				AnimUtil.animBackSlideFinish(FavouriteActivity.this);
			}
		});
		listView.setEmptyView(textView);

		Cursor cursor = dbHelper.query2();
		adapter = new MyAdapter(this, cursor,
				CursorAdapter.FLAG_REGISTER_CONTENT_OBSERVER);
		listView.setAdapter(adapter);
		// 这里不能关闭，否则没数据
		// cursor.close();
		listView.setOnItemClickListener(this);
		listView.setRemoveListener(this);
		imageView.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				new AlertDialog.Builder(FavouriteActivity.this)
						.setTitle("确定要清空收藏夹吗？")
						.setIcon(R.drawable.icon_error)
						.setNegativeButton("取消", null)
						.setPositiveButton("确定",
								new DialogInterface.OnClickListener() {
									@Override
									public void onClick(DialogInterface dialog,
											int which) {
										// 这是清空的刷新方法
										try {

											dbHelper.deleteAll();
											adapter.getCursor().requery();
											adapter.swapCursor(null);
											adapter.swapCursor(adapter
													.getCursor());
											ToastUtil.toast(
													FavouriteActivity.this,
													"已清空");
										} catch (Exception e) {
											e.printStackTrace();
										}
									}
								}).show();

			}
		});
	}

	class MyAdapter extends CursorAdapter {
		Context context;

		public MyAdapter(Context context, Cursor c, int flags) {
			super(context, c, flags);
			this.context = context;
		}

		// 找到布局和控件，返回给bindView
		@Override
		public View newView(Context context, Cursor cursor, ViewGroup parent) {
			ViewHolder holder = new ViewHolder();
			LayoutInflater inflater = getLayoutInflater();
			View inflate = inflater.inflate(R.layout.list_item, null);
			holder.title = (TextView) inflate.findViewById(R.id.tv_title);
			holder.time = (TextView) inflate.findViewById(R.id.tv_time);
			holder.image = (SmartImageView) inflate.findViewById(R.id.iv_pic);
			inflate.setTag(holder);
			return inflate;// 返回的view传给bindView。
		}

		// 复用布局,把数据设置到界面上
		@Override
		public void bindView(View view, Context context, Cursor cursor) {
			ViewHolder holder = (ViewHolder) view.getTag();
			String title = cursor
					.getString(cursor.getColumnIndex(FavoriteDb.newsTitle));
			String time = cursor.getString(cursor.getColumnIndex(FavoriteDb.newsTime));
			String pic = cursor.getString(cursor.getColumnIndex(FavoriteDb.newsPic));
			holder.title.setText(title);
			holder.time.setText(time);
			holder.image.setImageUrl(pic);
		}
	}

	static class ViewHolder {
		TextView title;
		TextView time;
		SmartImageView image;

	}

	@Override
	protected void onResume() {
		super.onResume();
		adapter.getCursor().requery();
		adapter.notifyDataSetChanged();
	}

	@Override
	public void onItemClick(AdapterView<?> parent, View view, int position,
			long id) {
		if (-1 != id) {// id==-1表示点击的footerView
			Cursor cursor = adapter.getCursor();
			cursor.moveToPosition(position);
			Log.e("log", position + "");
			Intent intent = new Intent(this, WebActivity.class);
			// Intent intent = new Intent(this, WebActivity2.class);
			intent.putExtra("openUrl",
					cursor.getString(cursor.getColumnIndex(FavoriteDb.newsUrl)));
			intent.putExtra(FavoriteDb.newsId,
					cursor.getString(cursor.getColumnIndex(FavoriteDb.newsId)));
			intent.putExtra(FavoriteDb.newsTitle,
					cursor.getString(cursor.getColumnIndex(FavoriteDb.newsTitle)));
			intent.putExtra(FavoriteDb.newsTime,
					cursor.getString(cursor.getColumnIndex(FavoriteDb.newsTime)));
			intent.putExtra(FavoriteDb.newsPic,
					cursor.getString(cursor.getColumnIndex(FavoriteDb.newsPic)));
			// intent.putExtra(Db.newsByteContent,
			// cursor.getString(cursor.getColumnIndex(Db.newsByteContent)));
			// 这里也不能关闭
			// cursor.close();
			intent.putExtra("isFav", true);
			startActivity(intent);
			AnimUtil.animToSlide(this);
		}
	}

	// 滑动删除之后的回调方法
	@Override
	public void removeItem(RemoveDirection direction, int position) {
		Cursor cursor = adapter.getCursor();
		cursor.moveToPosition(position);
		String index = cursor.getString(cursor.getColumnIndex(FavoriteDb.newsId));
		dbHelper.delete(index);
		// adapter.remove(position);
		// TODO 这里没有刷新界面
		// 在数据库数据少的时候，我们仍然可以安全地使用requery
		// cursor.requery();
		// // adapter.swapCursor(null);
		// adapter.notifyDataSetChanged();
		// 上述方法都不管用的时候使用重新查询然后交换cursor
		cursor = dbHelper.query2();
		adapter.swapCursor(cursor);
		adapter.notifyDataSetChanged();
		// listView.setAdapter(adapter);

		ToastUtil.toast(this, "取消收藏");
		switch (direction) {
		case RIGHT:
			// Toast.makeText(this, "向右删除  " + position, Toast.LENGTH_SHORT)
			// .show();
			break;
		case LEFT:
			// Toast.makeText(this, "向左删除  " + position, Toast.LENGTH_SHORT)
			// .show();
			break;
		}

	}
}