package com.runkun.lbsq.activity;

import android.annotation.SuppressLint;
import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.RatingBar;
import android.widget.TextView;

import com.lidroid.xutils.ViewUtils;
import com.lidroid.xutils.exception.HttpException;
import com.lidroid.xutils.http.RequestParams;
import com.lidroid.xutils.http.ResponseInfo;
import com.lidroid.xutils.http.callback.RequestCallBack;
import com.lidroid.xutils.view.annotation.ViewInject;
import com.lidroid.xutils.view.annotation.event.OnClick;
import com.runkun.lbsq.R;
import com.runkun.lbsq.bean.Comment;
import com.runkun.lbsq.utils.HttpHelper;
import com.runkun.lbsq.utils.Tools;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.LinkedList;
import java.util.List;

import feifei.project.view.smartimage.SmartImageView;

@SuppressLint("InflateParams")
public class JudgeListActivity extends BaseAcitivity
{

	@ViewInject(R.id.judge_list)
	private ListView judgeList;

	@ViewInject(R.id.footer_tv)
	private TextView footerTV;

	@ViewInject(R.id.footer_pb)
	private ProgressBar footerPB;

	private LayoutInflater inflater;
	private String goodId;
	private JudgeAdapter adapter;
	private List<Comment> comments;
	private int pageSize = 20;
	private int pageNum = 1;

	@Override
	protected void onCreate(Bundle paramBundle) {
		super.onCreate(paramBundle);
		setContentView(R.layout.activity_good_judgelist);
		inflater = (LayoutInflater) getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		View view = inflater.inflate(R.layout.activity_my_judge, null);
		comments = new LinkedList<Comment>();
		setContentView(view);
		ViewUtils.inject(this, view);
		tint();
		initView();
	}


	@SuppressLint("InflateParams")
	private void initView() {
		goodId = getIntent().getStringExtra("goodId");
		initActionbar();
		setTitles(Tools.getStr(activity, R.string.TJUDGELIST));

		View fv = inflater.inflate(R.layout.listview_footer, null);
		ViewUtils.inject(this, fv);

		judgeList.addFooterView(fv);

		adapter = new JudgeAdapter();
		judgeList.setAdapter(adapter);
		loadComments();
	}

	private void loadComments() {
		RequestParams rp = new RequestParams();

		rp.addQueryStringParameter("goods_id", goodId);
		rp.addQueryStringParameter("pagenumber", String.valueOf(pageNum++));
		rp.addQueryStringParameter("pagesize", String.valueOf(pageSize));

		HttpHelper.postByCommand("commentlist", rp,
				new RequestCallBack<String>() {
					@Override
					public void onStart() {
						updateFooter(true, null);
					}

					@Override
					public void onFailure(HttpException arg0, String arg1) {
						pageNum--;
						updateFooter(false,
								Tools.getStr(activity, R.string.NETERRORCLICK));
					}

					@Override
					public void onSuccess(ResponseInfo<String> resp) {
						String result = resp.result;
						if ("".equals(result)) {
							updateFooter(false,
									Tools.getStr(activity, R.string.NOJUDGE));
						}
						try {
							JSONObject jsonResult = new JSONObject(result);
							if (HttpHelper.isSuccess(jsonResult)) {
								String haveMore = jsonResult
										.getString("haveMore");
								judgeList.setTag(haveMore);
								JSONArray ja = jsonResult.getJSONArray("datas");
								for (int i = 0; i < ja.length(); i++) {
									comments.add(Comment.from (ja
											.getJSONObject (i)));
								}
								adapter.notifyDataSetChanged();
								updateFooter(false,
										Tools.getStr(activity, R.string.NOMORE));
								if ("true".equals(haveMore)) {
									updateFooter(false, Tools.getStr(activity,
											R.string.CLICKLOADINGMORE));
								} else {
									updateFooter(false, Tools.getStr(activity,
											R.string.ALLLOADED));
								}

								return;
							}
							updateFooter(false,
									Tools.getStr(activity, R.string.JSONERROR));
						} catch (JSONException e) {
							e.printStackTrace();
						}
					}

				});
	}

	private void updateFooter(boolean loading, String info) {
		if (loading) {
			footerTV.setVisibility(View.GONE);
			footerPB.setVisibility(View.VISIBLE);
		} else {
			footerPB.setVisibility(View.GONE);
			footerTV.setVisibility(View.VISIBLE);
			footerTV.setText(info);
		}
	}

	@OnClick({ R.id.footer_tv })
	public void onClick(View view) {
		if (view.getId() == R.id.footer_tv) {
			String haveMore = (String) judgeList.getTag();
			if ("true".equals(haveMore)) {
				loadComments();
			}
		}
	}

	private class JudgeAdapter extends BaseAdapter {

		@Override
		public int getCount() {
			return comments.size();
		}

		@Override
		public Object getItem(int position) {
			return position;
		}

		@Override
		public long getItemId(int position) {
			return position;
		}

		@Override
		public View getView(int position, View convertView, ViewGroup parent) {
			if (convertView == null) {
				convertView = inflater.inflate(R.layout.item_judge, null);
				ViewHolder vh = new ViewHolder();
				vh.photo = (SmartImageView) convertView
						.findViewById(R.id.judge_photo_img);
				vh.rb = (RatingBar) convertView.findViewById(R.id.rationbar);
				vh.nick = (TextView) convertView
						.findViewById(R.id.judge_user_nick);
				vh.content = (TextView) convertView
						.findViewById(R.id.judge_content);
				vh.date = (TextView) convertView.findViewById(R.id.judge_date);
				convertView.setTag(vh);
			}

			ViewHolder vh = (ViewHolder) convertView.getTag();
			Comment entity = comments.get(position);
			vh.photo.setImageResource(R.drawable.avatar_default);
			vh.content.setText(entity.getComment());
			vh.rb.setRating(entity.getFlowerNum());
			vh.nick.setText(entity.getMemberName());
			vh.date.setText(entity.getAddTime());

			return convertView;
		}

	}

	private class ViewHolder {
		SmartImageView photo;
		RatingBar rb;
		TextView nick;
		TextView content;
		TextView date;
	}
}
