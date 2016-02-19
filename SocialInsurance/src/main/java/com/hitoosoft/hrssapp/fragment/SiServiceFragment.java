package com.hitoosoft.hrssapp.fragment;

import java.util.Map;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.hitoosoft.hrssapp.R;
import com.hitoosoft.hrssapp.acitivity.BindActivity;
import com.hitoosoft.hrssapp.acitivity.MapActivity;
import com.hitoosoft.hrssapp.acitivity.WebActivity;
import com.hitoosoft.hrssapp.util.AnimUtil;
import com.hitoosoft.hrssapp.util.HrssConstants;
import com.hitoosoft.hrssapp.util.SpFactory;
import com.hitoosoft.hrssapp.util.ToastUtil;
import com.hitoosoft.hrssapp.view.SiServiceGridView;

/**
 * 人社服务
 */
public class SiServiceFragment extends Fragment implements OnItemClickListener {

	private static final int BIND_REQUEST_CODE = 1; // BindActivity返回的requestCode

	private TextView bindText;

	// Integer[] siQueryImageArray = new Integer[] { R.drawable.jfjl,
	// R.drawable.zhcx, R.drawable.dycx, R.drawable.jfsb, R.drawable.grxx };
	Integer[] siQueryImageArray = new Integer[] { R.drawable.jfjl,
			R.drawable.zhcx, R.drawable.dycx };
	// Integer[] siCardImageArray = new Integer[] { R.drawable.sbkszmx,
	// R.drawable.sbkye, R.drawable.sbkgs };
	Integer[] siOtherArray = new Integer[] { R.drawable.cjcx, R.drawable.fjwd,
			R.drawable.ypml };
	// String[] s1 = new String[] { "缴费记录", "账户查询", "待遇查询", "缴费申报", "个人信息" };
	String[] s1 = new String[] { "缴费记录", "账户查询", "待遇查询" };
	// String[] s2 = new String[] { "收支明细", "余额", "挂失" };
	String[] s3 = new String[] { "成绩查询", "附近网点", "药品目录" };
	// SiServiceGridView siCardGridView;
	SiServiceGridView siOtherGridView;

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		View siServiceView = inflater.inflate(R.layout.tab_siservice, null);
		// 初始化 社保查询 gridview
		SiServiceGridView siQueryGridView = (SiServiceGridView) siServiceView
				.findViewById(R.id.siquerymodule);
		siQueryGridView.setOnItemClickListener(this);
		siQueryGridView.setAdapter(new SiModulesAdapter(getActivity(),
				siQueryImageArray, s1));
		// 初始化 社保卡 gridview
		// siCardGridView = (SiServiceGridView) siServiceView
		// .findViewById(R.id.sicardmodule);
		// siCardGridView.setOnItemClickListener(this);
		// siCardGridView.setAdapter(new SiModulesAdapter(getActivity(),
		// siCardImageArray, s2));
		// 初始化 其他 gridview
		siOtherGridView = (SiServiceGridView) siServiceView
				.findViewById(R.id.siothermodule);
		siOtherGridView.setOnItemClickListener(this);
		siOtherGridView.setAdapter(new SiModulesAdapter(getActivity(),
				siOtherArray, s3));

		// 从手机中取出存储的绑定信息显示出来
		final Map<String, String> bindMap = SpFactory
				.getBindInfo(getActivity());
		bindText = (TextView) siServiceView.findViewById(R.id.bind_text);
		if (null != bindMap) {
			bindText.setText(bindMap.get("name") + "\t\t\t\t\t"
					+ bindMap.get("sfzh"));
		}
		View bindLayout = siServiceView.findViewById(R.id.bind_layout);
		bindLayout.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				Intent bindIntent = new Intent(getActivity(),
						BindActivity.class);
				Map<String, String> bindMap = SpFactory
						.getBindInfo(getActivity());
				if (null != bindMap) {
					bindIntent.putExtra("name", bindMap.get("name"));
					bindIntent.putExtra("sfzh", bindMap.get("sfzh"));
					bindIntent.putExtra("phone", bindMap.get("phone"));
				}
				startActivityForResult(bindIntent, BIND_REQUEST_CODE);
				AnimUtil.animToSlide(getActivity());
			}
		});
		return siServiceView;
	}

	@Override
	public void onResume() {
		super.onResume();
		// 这里隐藏控件需要延迟一下，同侧滑组件的平板适配
		new Handler(new Handler.Callback() {
			@Override
			public boolean handleMessage(Message msg) {
				siOtherGridView.getChildAt(2).setVisibility(View.GONE);
				return false;
			}
		}).sendMessageDelayed(Message.obtain(), 200);

	}

	String ryId;

	@Override
	public void onItemClick(AdapterView<?> adapter, View view, int position,
			long id) {
		Integer iv = (Integer) adapter.getItemAtPosition(position);
		switch (iv) {
		case R.drawable.cjcx:
			view.startAnimation(AnimUtil.getScaleGoneAnimation());
			给intent传入参数(HrssConstants.JNSI_CJCX, "成绩查询");
			return;
		case R.drawable.ypml:
			view.startAnimation(AnimUtil.getScaleGoneAnimation());
			正在开发中();
			return;
		case R.drawable.fjwd:
			startActivity(new Intent(getActivity(), MapActivity.class));
			AnimUtil.animToSlide(getActivity());
			return;
		}
		view.startAnimation(AnimUtil.getScaleGoneAnimation());
		Map<String, String> bindMap = SpFactory.getBindInfo(getActivity());
		if (null == bindMap) {
			new Handler(new Handler.Callback() {
				@Override
				public boolean handleMessage(Message msg) {
					Intent bindIntent = new Intent(getActivity(),
							BindActivity.class);
					startActivityForResult(bindIntent, BIND_REQUEST_CODE);
					AnimUtil.animToSlide(getActivity());
					return false;
				}
			}).sendMessageDelayed(Message.obtain(), 200);

		} else {
			ryId = bindMap.get("ryId");
			switch (iv) {
			case R.drawable.jfjl:
				给intent传入参数(HrssConstants.HRSSMSP_URL_JFJL + "?ryid=" + ryId,
						"个人参保缴费情况");
				break;
			case R.drawable.zhcx:
				给intent传入参数(HrssConstants.HRSSMSP_URL_ZHCX + "?ryid=" + ryId,
						"个人账户情况");
				break;
			case R.drawable.dycx:
				给intent传入参数(HrssConstants.HRSSMSP_URL_DYCX + "?ryid=" + ryId,
						"社保待遇情况");
				break;
			case R.drawable.jfsb:
				正在开发中();
				break;
			case R.drawable.grxx:
				正在开发中();
				break;
			case R.drawable.sbkgs:
				正在开发中();
				break;
			case R.drawable.sbkszmx:
				正在开发中();
				break;
			case R.drawable.sbkye:
				正在开发中();
				break;
			}
		}
	}

	void 给intent传入参数(final String param, final String title) {
		new Handler(new Handler.Callback() {
			@Override
			public boolean handleMessage(Message msg) {
				Intent jfjlIntent = new Intent(getActivity(), WebActivity.class);
				jfjlIntent.putExtra("openUrl", param);
				jfjlIntent.putExtra("title", title);
				startActivity(jfjlIntent);
				AnimUtil.animToSlide(getActivity());
				return false;
			}
		}).sendMessageDelayed(Message.obtain(), 200);
	}

	void 正在开发中() {
		ToastUtil.toast(getActivity(), getString(R.string.module_developping));
	}

	@Override
	public void onActivityResult(int requestCode, int resultCode, Intent data) {
		if (resultCode != Activity.RESULT_OK) {
			return;
		}
		super.onActivityResult(requestCode, resultCode, data);
		if (BIND_REQUEST_CODE == requestCode) {
			String name = data.getExtras().getString("name");
			String sfzh = data.getExtras().getString("sfzh");
			bindText.setText(name + "\t\t\t\t\t" + sfzh);
		}
	}

	class SiModulesAdapter extends BaseAdapter {
		private final Context context;
		private final Integer[] integer;
		private final String[] string;

		public SiModulesAdapter(Context context, Integer[] integer,
				String[] string) {
			this.context = context;
			this.integer = integer;
			this.string = string;
		}

		@Override
		public int getCount() {
			return integer.length;
		}

		@Override
		public Object getItem(int position) {
			return integer[position];
		}

		@Override
		public long getItemId(int position) {
			return position;
		}

		@Override
		public View getView(int position, View convertView, ViewGroup parent) {
			ViewHolder holderFirst = null;
			if (null == convertView) {
				convertView = LayoutInflater.from(context).inflate(
						R.layout.item_module, null);
				holderFirst = new ViewHolder();
				holderFirst.image = (ImageView) convertView
						.findViewById(R.id.iv_module);
				holderFirst.text = (TextView) convertView
						.findViewById(R.id.iv_text);
				convertView.setTag(holderFirst);
			} else {
				holderFirst = (ViewHolder) convertView.getTag();
			}
			holderFirst.image.setImageResource((Integer) getItem(position));
			holderFirst.text.setText(string[position]);
			return convertView;
		}
	}

	final static class ViewHolder {
		ImageView image;
		TextView text;
	}

}