package com.runkun.lbsq.busi.test;

import android.os.Bundle;
import android.text.util.Linkify;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.baidu.lbsapi.BMapManager;
import com.baidu.lbsapi.panoramaview.PanoramaView;
import com.baidu.lbsapi.panoramaview.PanoramaViewListener;
import com.baidu.mapapi.map.BaiduMap;
import com.baidu.mapapi.map.MapView;
import com.baidu.mapapi.model.LatLng;
import com.baidu.mapapi.overlayutil.DrivingRouteOverlay;
import com.baidu.mapapi.search.core.SearchResult;
import com.baidu.mapapi.search.route.DrivingRoutePlanOption;
import com.baidu.mapapi.search.route.DrivingRouteResult;
import com.baidu.mapapi.search.route.OnGetRoutePlanResultListener;
import com.baidu.mapapi.search.route.PlanNode;
import com.baidu.mapapi.search.route.RoutePlanSearch;
import com.baidu.mapapi.search.route.TransitRouteResult;
import com.baidu.mapapi.search.route.WalkingRouteResult;
import com.baidu.pplatform.comapi.basestruct.GeoPoint;
import com.hitoosoft.hrssapp.R;

public class DetailActivity extends BaseActivity implements
		PanoramaViewListener, OnGetRoutePlanResultListener {
	Enerty enerty;
	TextView tv_name, tv_addresss, tv_phone;
	private PanoramaView mPanoView;
	GeoPoint geoPoint;
	double lat, lon;
	double mlat, mlon;

	private MapView mMapView = null;
	private BaiduMap mBaiduMap = null;
	private RoutePlanSearch mSearch = null;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		MyApplication app = (MyApplication) this.getApplication();
		if (app.mBMapManager == null) {
			app.mBMapManager = new BMapManager(app);

			app.mBMapManager.init(new MyApplication.MyGeneralListener());
		}
		setContentView(R.layout.activity_detail);

		mPanoView = (PanoramaView) findViewById(R.id.panorama);

		tv_name = (TextView) findViewById(R.id.tv_name);
		tv_addresss = (TextView) findViewById(R.id.tv_address);
		tv_phone = (TextView) findViewById(R.id.tv_phone);
		enerty = getIntent().getParcelableExtra("p");
		tv_name.setText(enerty.getName());
		((TextView) findViewById(R.id.titlebar)).setText(enerty.getName());
		findViewById(R.id.actionbar_right).setVisibility(View.INVISIBLE);
		tv_addresss.setText(enerty.getAddress());
		String person = enerty.getPerson() == null ? "" : "联系人："
				+ enerty.getPerson().replace(" ", "");
		String phone = enerty.getPhone() == null ? "" : "联系电话："
				+ enerty.getPhone();

		if (person.equals("")) {
			tv_phone.setText(phone);
		} else {
			tv_phone.setText(person + "\t\t\t" + phone);
		}
		if (!phone.equals("")) {
			Linkify.addLinks(tv_phone, Linkify.PHONE_NUMBERS);
		}

		lat = enerty.getLat();
		lon = enerty.getLon();
		mlat = getIntent().getDoubleExtra("lat", 0);
		mlon = getIntent().getDoubleExtra("lon", 0);
		mPanoView.setPanoramaImageLevel(5);
		mPanoView.setPanoramaViewListener(this);

		mPanoView.setShowTopoLink(true);

		mPanoView.setZoomGestureEnabled(true);
		mPanoView.setRotateGestureEnabled(true);
		mPanoView.setPanorama(lon, lat);

		// Log.e("", "" + lon + "###" + lat);
		// Log.e("", "" + (int) (lon * 1000000) + "###" + (int) (lat *
		// 1000000));
		// geoPoint = new GeoPoint((int) (lat * 1000000), (int) (lon *
		// 10000000));
		//
		// ImageMarker marker = new ImageMarker(geoPoint);
		// marker.setMarker(getResources().getDrawable(R.drawable.icon_marka));
		// marker.setOnTabMarkListener(new OnTabMarkListener() {
		//
		// @Override
		// public void onTab(int index) {
		// Toast.makeText(DetailActivity.this, "标注已被点击",
		// Toast.LENGTH_SHORT).show();
		//
		// }
		// });
		// mPanoView.addMarker(marker);
		//
		// // 全景地图的marker没加进来
		// TextMarker textMark1 = new TextMarker(geoPoint);
		// textMark1.setFontColor(0xFFFF0000);
		// textMark1.setText("你好marker");
		// textMark1.setFontSize(36);
		// textMark1.setBgColor(0x7F0000FF);
		// textMark1.setPadding(20, 20, 20, 20);
		// mPanoView.addMarker(textMark1);

		mMapView = (MapView) findViewById(R.id.bmapView);
		mMapView.showScaleControl(false);
		mMapView.showZoomControls(false);
		mBaiduMap = mMapView.getMap();

		mSearch = RoutePlanSearch.newInstance();
		mSearch.setOnGetRoutePlanResultListener(this);
		LatLng locLatLng = new LatLng(mlat, mlon);
		LatLng desLatLng = new LatLng(lat, lon);
		PlanNode st = PlanNode.withLocation(locLatLng);
		PlanNode en = PlanNode.withLocation(desLatLng);
		mSearch.drivingSearch(new DrivingRoutePlanOption().from(st).to(en));
	}

	@Override
	protected void onPause() {
		super.onPause();
		mPanoView.onPause();
	}

	@Override
	protected void onResume() {
		super.onResume();
		mPanoView.onResume();

	}

	@Override
	protected void onDestroy() {
		mPanoView.destroy();
		super.onDestroy();
	}

	@Override
	public void onLoadPanoramBegin() {

	}

	@Override
	public void onLoadPanoramaEnd() {

	}

	@Override
	public void onLoadPanoramaError() {
		Toast.makeText(this, "该地还没有全景图", Toast.LENGTH_SHORT).show();

	}

	@Override
	public void onLoadPanoramaDesc() {

	}

	@Override
	public void onGetWalkingRouteResult(WalkingRouteResult walkingRouteResult) {

	}

	@Override
	public void onGetTransitRouteResult(TransitRouteResult transitRouteResult) {

	}

	@Override
	public void onGetDrivingRouteResult(DrivingRouteResult result) {
		if (result == null || result.error != SearchResult.ERRORNO.NO_ERROR) {
			Toast.makeText(DetailActivity.this, "抱歉，未找到结果", Toast.LENGTH_SHORT)
					.show();
		}

		if (result.error == SearchResult.ERRORNO.AMBIGUOUS_ROURE_ADDR) {

			return;
		}

		if (result.error == SearchResult.ERRORNO.NO_ERROR) {
			// route = result.getRouteLines().get(0);
			DrivingRouteOverlay overlay = new DrivingRouteOverlay(mBaiduMap);
			overlay.setData(result.getRouteLines().get(0));
			overlay.addToMap();
			overlay.zoomToSpan();
		}
	}
}
