package com.runkun.lbsq.bean;

import android.widget.CheckBox;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ShopCart
{

	private List<Shop> shops;
	private Map<String, Integer> shopsIdxer;
	private boolean allChecked = false;
	private CheckBox ck;

	public ShopCart(CheckBox ck) {
		shops = new ArrayList<Shop>();
		shopsIdxer = new HashMap<String, Integer>();
		this.ck = ck;
	}

	public Shop getShop(int idx) {
		Shop s = shops.get(idx);
		return s;
	}

	public Shop addShop(Shop shop) {
		int size = shops.size();
		shopsIdxer.put(shop.storeId, size);
		shops.add(shop);
		return shop;
	}

	public int getShopsCount() {
		return shops.size();
	}

	public void removeShop(int idx) {
		shops.remove(idx);
	}

	public boolean isAllChecked() {
		return allChecked;
	}

	public void onShopCheckedChange() {

		if (shops.isEmpty()) {
			ck.setChecked(false);
			return;
		}

		for (Shop s : shops) {
			if (!s.isSelected) {
				allChecked = false;
				break;
			}
			allChecked = true;
		}
		ck.setChecked(allChecked);
	}

	public int add(JSONObject jo) throws JSONException {
		Shop shop = null;
		String storeId = jo.getString("store_id");
		if (shopsIdxer.get(storeId) != null) {
			int shopIdx = shopsIdxer.get(storeId);
			shop = getShop(shopIdx);
			shop.setSelected(false);
		} else {
			shop = addShop(Shop.from(this, jo));
		}

		Good good = Good.from(shop, jo);

		shop.addGood(good);
		return good.getNumber();
	}

	public List<Shop> getSelectedShop() {
		List<Shop> selectedShops = new ArrayList<Shop>();

		for (Shop shop : shops) {
			List<Good> goods = shop.getSelectedGoods();
			if (goods.size() > 0) {
				selectedShops.add(shop);
			}
		}

		return selectedShops;
	}

	public List<Shop> getShops() {
		return shops;
	}

	public void clear() {
		for (Shop shop : shops) {
			shop.clear();
		}
		shopsIdxer.clear();
		shops.clear();
	}

	public static class Shop {
		private String storeId;
		private String storeName;
		private boolean isSelected;
		private List<Good> goods;
		private ShopCart shopcart;

		public Shop(ShopCart shopcart) {
			goods = new ArrayList<Good>();
			isSelected = false;
			this.shopcart = shopcart;
		}

		public String getStoreId() {
			return storeId;
		}

		public void setStoreId(String storeId) {
			this.storeId = storeId;
		}

		public String getStoreName() {
			return storeName;
		}

		public void setStoreName(String storeName) {
			this.storeName = storeName;
		}

		public Good getGood(int idx) {
			return goods.get(idx);
		}

		public void addGood(Good good) {
			goods.add(good);
		}

		public void removeGood(int idx) {
			goods.remove(idx);
		}

		public int getGoodsCount() {
			return goods.size();
		}

		public boolean isSelected() {
			return isSelected;
		}

		public void setSelected(boolean isSelected) {
			this.isSelected = isSelected;
		}

		public void onClick() {
			for (Good g : goods) {
				g.onMyShopCheckedChange(isSelected);
			}
			shopcart.onShopCheckedChange();
		}

		public void onMyGoodsCheckedChange(boolean isChecked) {
			if (isChecked) {
				boolean hasUnchecked = false;
				for (Good g : goods) {
					if (!g.isSelected()) {
						hasUnchecked = true;
						break;
					}
				}
				isSelected = !hasUnchecked;
			} else {
				isSelected = false;
			}
			shopcart.onShopCheckedChange();
		}

		public void clear() {
			goods.clear();
		}

		public List<Good> getSelectedGoods() {
			List<Good> selectedGoods = new ArrayList<Good>();

			for (Good good : goods) {
				if (good.isSelected) {
					selectedGoods.add(good);
				}
			}

			return selectedGoods;
		}

		public List<Good> getGoods() {
			return goods;
		}

		public static Shop from(ShopCart shopcart, JSONObject jo)
				throws JSONException {
			Shop s = new Shop(shopcart);

			s.setStoreId(jo.getString("store_id"));
			s.setStoreName(jo.getString("store_name"));

			return s;
		}
	}

	public static class Good {
		private String shopcarId;
		private String goodId;
		private String goodName;
		private String price;
		private int number;
		private String state;
		private String goodPic;
		private boolean isSelected;
		private String newcount;
		private Shop shop;

		public Good() {
			isSelected = false;
		}

		public Good(Shop shop) {
			isSelected = false;
			this.shop = shop;
		}

		public String getGoodId() {
			return goodId;
		}

		public void setGoodId(String goodId) {
			this.goodId = goodId;
		}

		public String getGoodName() {
			return goodName;
		}

		public void setGoodName(String goodName) {
			this.goodName = goodName;
		}

		public String getPrice() {
			return price;
		}

		public void setPrice(String price) {
			this.price = price;
		}

		public int getNumber() {
			return number;
		}

		public void setNumber(int number) {
			this.number = number;
		}

		public String getState() {
			return state;
		}

		public void setState(String state) {
			this.state = state;
		}

		public String getGoodPic() {
			return goodPic;
		}

		public void setGoodPic(String goodPic) {
			this.goodPic = goodPic;
		}

		public boolean isSelected() {
			return isSelected;
		}

		public void setSelected(boolean isSelected) {
			this.isSelected = isSelected;
		}

		public void onClick(boolean isChecked) {
			this.isSelected = isChecked;
			shop.onMyGoodsCheckedChange(isChecked);
		}

		public String getNewcount() {
			return newcount;
		}

		public void setNewcount(String newcount) {
			this.newcount = newcount;
		}

		public Shop getShop() {
			return shop;
		}

		public void setShop(Shop shop) {
			this.shop = shop;
		}

		public String getShopcarId() {
			return shopcarId;
		}

		public void setShopcarId(String shopcarId) {
			this.shopcarId = shopcarId;
		}

		public void onMyShopCheckedChange(boolean isChecked) {
			isSelected = isChecked;
		}

		public static Good from(Shop shop, JSONObject jo) throws JSONException {
			Good g = new Good(shop);

			g.setGoodId(jo.getString("item_id"));
			g.setGoodName(jo.getString("item_name"));
			g.setPrice(jo.getString("price"));
			g.setNumber(jo.getInt("number"));
			g.setState(jo.getString("state"));
			g.setNewcount(jo.getString("number"));
			g.setShopcarId(jo.getString("shopcar_id"));
			g.setGoodPic(jo.getString("goods_pic"));

			return g;
		}
	}
}
