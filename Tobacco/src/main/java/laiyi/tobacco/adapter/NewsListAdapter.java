package laiyi.tobacco.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.duowan.mobile.netroid.image.NetworkImageView;

import java.util.ArrayList;
import java.util.List;

import laiyi.tobacco.R;
import laiyi.tobacco.bean.News;

// TODO: imageloader
public class NewsListAdapter extends BaseAdapter
{
    private List<News> newsList = new ArrayList<> ();
    private final Context context;
    private final int FIRST_TYPE = 0;
    private final int OTHERS_TYPE = 1;

    public NewsListAdapter (Context context, List<News> list)
    {
        this.context = context;
        this.newsList = list;

    }

    @Override
    public int getCount ()
    {
        return newsList.size ();
    }

    @Override
    public Object getItem (int position)
    {
        return newsList.get (position);
    }

    @Override
    public long getItemId (int position)
    {
        return position;
    }

    public int getViewTypeCount ()
    {
        return 2;
    }

    @Override
    public int getItemViewType (int position)
    {
        if ( position == 0 )
        {
            return FIRST_TYPE;
        } else
        {
            return OTHERS_TYPE;
        }
    }

    @Override
    public View getView (int position, View convertView, ViewGroup parent)
    {
        ViewHolderFirst holderFirst = null;
        ViewHolderNormal holderNormal = null;

        int type = getItemViewType (position);

        if ( null == convertView )
        {
            switch (type)
            {
                case FIRST_TYPE:
                    convertView = LayoutInflater.from (context).inflate (
                            R.layout.list_item_first, null);
                    holderFirst = new ViewHolderFirst ();
                    holderFirst.titleFirst = (TextView) convertView
                            .findViewById (R.id.first_text);
//                    holderFirst.imageFirst = (NetworkImageView) convertView
//                            .findViewById (R.id.first_image);
                    convertView.setTag (holderFirst);
                    break;
                case OTHERS_TYPE:
                    convertView = LayoutInflater.from (context).inflate (
                            R.layout.list_item, null);
                    holderNormal = new ViewHolderNormal ();
                    holderNormal.title = (TextView) convertView
                            .findViewById (R.id.tv_title);
                    holderNormal.fav = (TextView) convertView
                            .findViewById (R.id.tv_fav);
                    holderNormal.time = (TextView) convertView
                            .findViewById (R.id.tv_time);
//                    holderNormal.imageView = (NetworkImageView) convertView
//                            .findViewById (R.id.iv_pic);
                    holderNormal.arrow = (ImageView) convertView
                            .findViewById (R.id.iv_arrow);
                    convertView.setTag (holderNormal);
                    break;
            }
        } else
        {
            switch (type)
            {
                case FIRST_TYPE:
                    holderFirst = (ViewHolderFirst) convertView.getTag ();
                    break;
                case OTHERS_TYPE:
                    holderNormal = (ViewHolderNormal) convertView.getTag ();
                    break;
            }

        }
        String newsTitle = newsList.get (position).getNewsTitle ();
        switch (type)
        {
            case FIRST_TYPE:
                holderFirst.titleFirst.setText(newsTitle);
//                holderFirst.imageFirst.setImageResource(R.drawable.asd);
//                holderFirst.imageFirst.setImageUrl (newsList.get (position).getPicURL (), MyApplication.mImageLoader);
                break;
            case OTHERS_TYPE:
                holderNormal.title.setText (newsTitle);
                holderNormal.time.setText(newsList.get(position).getPublishDate());
//                holderNormal.imageView.setImageDrawable(context.getResources().getDrawable(R.drawable.qwe));
//                holderNormal.imageView.setImageUrl (newsList.get (position).getPicURL (), MyApplication.mImageLoader);
                break;
        }
        return convertView;
    }

    class ViewHolderFirst
    {
        TextView titleFirst;
        NetworkImageView imageFirst;
    }

    class ViewHolderNormal
    {
        TextView title;
        TextView time;
        TextView fav;
        NetworkImageView imageView;
        ImageView arrow;
    }

    public List<News> getNewsList ()
    {
        return newsList;
    }

    public void setNewsList (List<News> newsList)
    {
        this.newsList = newsList;
    }

    /**
     * 动态增加适配器里面的数据集合
     */
    public void addNewsList (List<News> newsList)
    {
        if ( null == this.newsList )
        {
            this.newsList = newsList;
        } else
        {
            this.newsList.addAll (newsList);
        }
    }

}