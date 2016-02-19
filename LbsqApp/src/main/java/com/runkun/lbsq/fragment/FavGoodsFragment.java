package com.runkun.lbsq.fragment;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;

import com.runkun.lbsq.R;
import com.runkun.lbsq.activity.GoodDetailActivity;
import com.runkun.lbsq.adapter.GoodAdapter;
import com.runkun.lbsq.bean.GoodMore;
import com.runkun.lbsq.bean.Shop;
import com.tendcloud.tenddata.TCAgent;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

import feifei.project.util.ConfigUtil;

public class FavGoodsFragment extends FavBasefragment<GoodMore> {

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		super.onCreateView(inflater, container, savedInstanceState);
		commandload = "favorite_goodslist";
		commanddelete = "delfavorite_goods";
		useData = new ArrayList<GoodMore>();
		adaper = new GoodAdapter(getActivity(), useData, R.layout.item_good,
				false, true, false);
		list.setAdapter(adaper);
		loadFav();
		list.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> parent, View view,
					int position, long id) {
				Intent intent = new Intent(getActivity(),
						GoodDetailActivity.class);
				intent.putExtra("goods_id", useData.get(position).getGoodsId());
				getActivity().startActivity(intent);
			}
		});
		return view;
	}
	@Override
	public void onPause() {
		TCAgent.onPageEnd(getActivity(), "BuyDLFragment");
		super.onPause();
	}

	@Override
	public void onStart() {
		TCAgent.onPageStart(getActivity(), "BuyDLFragment");
		super.onStart();
	}


	@Override
	protected void makeData(JSONObject jsonObject) {
		GoodMore goodMoreEntity = new GoodMore ();
		try {
			goodMoreEntity.setGoodsName(jsonObject.getString("goods_name"));
			goodMoreEntity.setGoodsPic(jsonObject.getString("goods_pic"));
			goodMoreEntity.setGoodsPrice(jsonObject.getString("goods_price"));
			goodMoreEntity.setGoodsId(jsonObject.getString("goods_id"));
			goodMoreEntity.setOther(jsonObject.getString("id"));
		} catch (JSONException e) {
			e.printStackTrace();
		}
		if (getActivity() != null) {
			goodMoreEntity.setStoreId(ConfigUtil.readString (getActivity (),
					Shop.SHOPID, ""));
			goodMoreEntity.setStoreName(ConfigUtil.readString(getActivity(),
					Shop.SHOPNAME, ""));
		}
		useData.add(goodMoreEntity);
	}

	@Override
	public String getDeleteId(int position) {
		return useData.get(position).getOther();
	}

}
