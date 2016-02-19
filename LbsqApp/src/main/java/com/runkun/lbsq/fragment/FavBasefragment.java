package com.runkun.lbsq.fragment;

import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.lidroid.xutils.exception.HttpException;
import com.lidroid.xutils.http.RequestParams;
import com.lidroid.xutils.http.ResponseInfo;
import com.lidroid.xutils.http.callback.RequestCallBack;
import com.runkun.lbsq.R;
import com.runkun.lbsq.activity.LoginActivity;
import com.runkun.lbsq.interfaces.onButtonClick;
import com.runkun.lbsq.utils.HttpHelper;
import com.runkun.lbsq.utils.Tools;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.List;

import feifei.project.view.swipmenu.SwipeMenu;
import feifei.project.view.swipmenu.SwipeMenuCreator;
import feifei.project.view.swipmenu.SwipeMenuItem;
import feifei.project.view.swipmenu.SwipeMenuListView;

public abstract class FavBasefragment<T> extends BaseFragment {
	protected View view;
	protected SwipeMenuListView list;
	protected TextView emptyTextView;
	protected String memberId;
	protected String commandload;
	protected String commanddelete;

	protected BaseAdapter adaper;
	protected List<T> useData;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		memberId = HttpHelper.getPrefParams(getActivity(), "memberId");
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		super.onCreateView(inflater, container, savedInstanceState);
		view = inflater.inflate(R.layout.fragment_fav, null);
		list = (SwipeMenuListView) view.findViewById(R.id.list);
		dialogInit();

		Tools tools = new Tools();
		View view2 = tools.getEmptyView(getActivity(), R.drawable.evaluate_gray);
		((ViewGroup) list.getParent()).addView(view2);
		emptyTextView = tools.getEmptyText();
		emptyTextView.setText("");
		emptyTextView.setTextColor(Color.parseColor("#d5d5d5"));
		emptyTextView.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				loadFav();
			}
		});
		list.setEmptyView(view2);

		SwipeMenuCreator creator = new SwipeMenuCreator() {
			@Override
			public void create(SwipeMenu menu) {
				SwipeMenuItem deleteItem = new SwipeMenuItem(getActivity());
				deleteItem.setBackground(new ColorDrawable(Color.rgb(0xF9,
						0x3F, 0x25)));
				deleteItem.setWidth(Tools.dp2px(getActivity(), 70));
				deleteItem.setIcon(R.drawable.ic_delete);
				menu.addMenuItem(deleteItem);
			}
		};
		list.setMenuCreator(creator);
		list.setOnMenuItemClickListener(new SwipeMenuListView.OnMenuItemClickListener () {
			@Override
			public void onMenuItemClick(int position, SwipeMenu menu, int index) {
				deleteFav(position);
			}

		});
		return view;
	}

	protected boolean hasLogin() {
		return !"".equals(memberId);
	}

	protected void promtLogin() {
		Tools.dialog(getActivity(),
				Tools.getStr(fragment, R.string.REQUESTLOGIN),
				true,new onButtonClick() {
					@Override
					public void buttonClick() {
						startActivity(new Intent(getActivity(),
								LoginActivity.class));
					}
				});
	}

	protected void loadFav() {
		if (!hasLogin()) {
			promtLogin();
			return;
		}
		dialogProgress(Tools.getStr(fragment, R.string.LOADING));
		RequestParams rp = new RequestParams();
		rp.addQueryStringParameter("member_id", memberId);
		HttpHelper.postByCommand(commandload, rp,
				new RequestCallBack<String>() {

					@Override
					public void onFailure(HttpException arg0, String arg1) {
						emptyTextView.setText(Tools.getStr(fragment,
								R.string.NETERRORGOING));
						dialogDismiss();
					}

					@Override
					public void onSuccess(ResponseInfo<String> resp) {
						useData.clear();
						try {
							JSONObject jsonData = new JSONObject(resp.result);
							if (HttpHelper.isSuccess(jsonData)) {
								JSONArray data = jsonData.getJSONArray("data");
								for (int x = 0; x < data.length(); x++) {
									JSONObject jsonObject = data
											.getJSONObject(x);
									makeData(jsonObject);
								}
								adaper.notifyDataSetChanged();
								emptyTextView.setText(Tools.getStr(fragment,
										R.string.NOFAV));
							}
						} catch (JSONException e) {
							e.printStackTrace();
							dialogDismiss();
						}
						dialogDismiss();
					}

				});
	}

	public abstract String getDeleteId(int position);

	public void deleteFav(final int position) {
		if (!hasLogin()) {
			promtLogin();
			return;
		}
		dialogProgress(Tools.getStr(fragment, R.string.DELETING));
		RequestParams rp = new RequestParams();
		rp.addQueryStringParameter("member_id", memberId);
		if (position != -1) {
			rp.addQueryStringParameter("id", getDeleteId(position));

		}
		HttpHelper.postByCommand(commanddelete, rp,
				new RequestCallBack<String>() {
					@Override
					public void onFailure(HttpException arg0, String arg1) {
						emptyTextView.setText(Tools.getStr(fragment,
								R.string.NETERRORGOING));
						dialogDismiss();
					}

					@Override
					public void onSuccess(ResponseInfo<String> resp) {
						try {
							JSONObject jsonData = new JSONObject(resp.result);
							String code = jsonData.getString("code");
							if (code.equals("200")) {
								JSONObject datas = jsonData
										.getJSONObject("data");
								String res = datas.getString("delResult");
								if (res.equals("true")) {
									Tools.toast(getActivity(), Tools.getStr(
											fragment, R.string.DELSUCCESS));
									if (position != -1) {
										useData.remove(position);
									} else {
										useData.clear();
									}
									adaper.notifyDataSetChanged();
								}
							}

						} catch (JSONException e) {
							e.printStackTrace();
							dialogDismiss();
						}
						dialogDismiss();
					}

				});
	}

	protected abstract void makeData(JSONObject jsonObject);

	public int getCount() {
		return useData.size();
	}
}
