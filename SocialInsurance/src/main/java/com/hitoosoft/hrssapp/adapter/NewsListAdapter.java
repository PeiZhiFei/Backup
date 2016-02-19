package com.hitoosoft.hrssapp.adapter;

import android.content.Context;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.hitoosoft.hrssapp.R;
import com.hitoosoft.hrssapp.database.FavoriteDbHelper;
import com.hitoosoft.hrssapp.database.ReadedDbHelper;
import com.hitoosoft.hrssapp.entity.News;
import com.hitoosoft.hrssapp.util.HrssConstants;
import com.hitoosoft.hrssapp.view.SmartImageView;

import java.util.ArrayList;
import java.util.List;

public class NewsListAdapter extends BaseAdapter {
    private List<News> newsList = new ArrayList<News>();
    private final Context context;
    private final int FIRST_TYPE = 0;
    private final int OTHERS_TYPE = 1;
    FavoriteDbHelper dbHelper;
    ReadedDbHelper dbHelper2;

    public NewsListAdapter(Context context) {
        this.context = context;
        dbHelper = new FavoriteDbHelper(context);
        dbHelper2 = new ReadedDbHelper(context);
    }

    public NewsListAdapter(Context context, List<News> list) {
        this.context = context;
        this.newsList = list;
    }

    @Override
    public int getCount() {
        return newsList.size();
    }

    @Override
    public Object getItem(int position) {
        return newsList.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    public int getViewTypeCount() {
        return 2;
    }

    @Override
    public int getItemViewType(int position) {
        if (position == 0) {
            return FIRST_TYPE;
        } else {
            return OTHERS_TYPE;
        }
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolderFirst holderFirst = null;
        ViewHolderNormal holderNormal = null;

        int type = getItemViewType(position);

        if (null == convertView) {
            switch (type) {
                case FIRST_TYPE:
                    convertView = LayoutInflater.from(context).inflate(
                            R.layout.list_item_first, null);
                    holderFirst = new ViewHolderFirst();
                    holderFirst.titleFirst = (TextView) convertView
                            .findViewById(R.id.first_text);
                    holderFirst.imageFirst = (SmartImageView) convertView
                            .findViewById(R.id.first_image);
                    convertView.setTag(holderFirst);
                    break;
                case OTHERS_TYPE:
                    convertView = LayoutInflater.from(context).inflate(
                            R.layout.list_item, null);
                    holderNormal = new ViewHolderNormal();
                    holderNormal.title = (TextView) convertView
                            .findViewById(R.id.tv_title);
                    holderNormal.fav = (TextView) convertView
                            .findViewById(R.id.tv_fav);
                    holderNormal.time = (TextView) convertView
                            .findViewById(R.id.tv_time);
                    holderNormal.imageView = (SmartImageView) convertView
                            .findViewById(R.id.iv_pic);
                    holderNormal.arrow = (ImageView) convertView
                            .findViewById(R.id.iv_arrow);
                    convertView.setTag(holderNormal);
                    break;
            }
        } else {
            switch (type) {
                case FIRST_TYPE:
                    holderFirst = (ViewHolderFirst) convertView.getTag();
                    break;
                case OTHERS_TYPE:
                    holderNormal = (ViewHolderNormal) convertView.getTag();
                    break;
            }

        }
        String newsTitle = newsList.get(position).getNewsTitle();
        switch (type) {
            case FIRST_TYPE:
                // 是否阅读过
                boolean result4 = dbHelper2.isExit(newsList.get(position)
                        .getNewsID());
                if (result4) {
                    holderFirst.titleFirst
                            .setTextColor(Color.parseColor("#4A708B"));
                } else {
                    holderFirst.titleFirst.setTextColor(Color.BLACK);
                }
                holderFirst.titleFirst.setText(newsTitle);
//                holderFirst.imageFirst.setImageUrl(HrssConstants.SERVER_URL
//                                + newsList.get(position).getPicURL(),
//                        R.drawable.image_failure, R.drawable.image_loading);
                holderFirst.imageFirst.setImageUrl(HrssConstants.SERVER_URL
                        + newsList.get(position).getPicURL());
                break;
            case OTHERS_TYPE:
                // 是否阅读过
                boolean result3 = dbHelper2.isExit(newsList.get(position)
                        .getNewsID());
                if (result3) {
                    holderNormal.title.setTextColor(Color.parseColor("#778899"));
                } else {
                    holderNormal.title.setTextColor(Color.BLACK);
                }

                boolean result2 = dbHelper.isExit(newsList.get(position)
                        .getNewsID());
                // adapter里的东西必须都设置，否则错乱，是否收藏过
                if (result2) {
                    holderNormal.fav.setText("已收藏");
                } else {
                    holderNormal.fav.setText("");
                }
                holderNormal.title.setText(newsTitle);
                holderNormal.time.setText(newsList.get(position).getPublishDate());
//			holderNormal.imageView.setImageUrl(HrssConstants.SERVER_URL
//					+ newsList.get(position).getPicURL(),
//					R.drawable.image_failure, R.drawable.image_loading);
                holderNormal.imageView.setImageUrl(HrssConstants.SERVER_URL
                        + newsList.get(position).getPicURL());
                break;
        }
        return convertView;
    }

    class ViewHolderFirst {
        TextView titleFirst;
        SmartImageView imageFirst;
    }

    class ViewHolderNormal {
        TextView title;
        TextView time;
        TextView fav;
        SmartImageView imageView;
        ImageView arrow;
    }

    public List<News> getNewsList() {
        return newsList;
    }

    public void setNewsList(List<News> newsList) {
        this.newsList = newsList;
    }

    /**
     * 动态增加适配器里面的数据集合
     */
    public void addNewsList(List<News> newsList) {
        if (null == this.newsList) {
            this.newsList = newsList;
        } else {
            this.newsList.addAll(newsList);
        }
    }

}