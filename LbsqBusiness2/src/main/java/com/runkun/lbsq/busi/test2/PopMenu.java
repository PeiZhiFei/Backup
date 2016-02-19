package com.runkun.lbsq.busi.test2;//package feifei.library.demo;
//
//import android.content.Context;
//import android.graphics.drawable.BitmapDrawable;
//import android.view.LayoutInflater;
//import android.view.View;
//import android.view.ViewGroup;
//import android.view.ViewGroup.LayoutParams;
//import android.widget.AdapterView.OnItemClickListener;
//import android.widget.BaseAdapter;
//import android.widget.ImageButton;
//import android.widget.ListView;
//import android.widget.PopupWindow;
//import android.widget.TextView;
//
//import java.util.ArrayList;
//
//public class PopMenu
//{
//    private ArrayList<String> itemList;
//    private ArrayList<Integer> imgList;
//    private Context context;
//    private PopupWindow popupWindow;
//    private ListView listView;
//
//    // private OnItemClickListener listener;
//
//    public PopMenu (Context context)
//    {
//        this.context = context;
//
//        itemList = new ArrayList<String> ();
//        imgList = new ArrayList<Integer> ();
//        View view = LayoutInflater.from (context).inflate (R.layout.toast_layout,
//                null);
//
//        listView = (ListView) view.findViewById (R.id.list_item);
//        listView.setAdapter (new PopAdapter ());
//        listView.setFocusableInTouchMode (true);
//        listView.setFocusable (true);
//
//        popupWindow = new PopupWindow (view,
//                (int) (Tools.getWidth (context) / 2.5f),
//                LayoutParams.WRAP_CONTENT);
//        //		popupWindow.setAnimationStyle(R.style.pop_animation);
//        popupWindow.setBackgroundDrawable (new BitmapDrawable ());
//    }
//
//    public void setOnItemClickListener (OnItemClickListener listener)
//    {
//        // this.listener = listener;
//        listView.setOnItemClickListener (listener);
//    }
//
//    public void addItems (String[] items, Integer[] imglable)
//    {
//        for (int x = 0, y = items.length; x < y; x++)
//        {
//            itemList.add (items[x]);
//            imgList.add (imglable[x]);
//        }
//    }
//
//    public void addItems (String[] items)
//    {
//        for (String item : items)
//        {
//            itemList.add (item);
//        }
//    }
//
//    public void addItem (String item)
//    {
//        itemList.add (item);
//    }
//
//    public void showAsDropDown (View parent)
//    {
//        popupWindow.showAsDropDown (parent, 30, context.getResources ()
//                .getDimensionPixelSize (R.dimen.dialog_fixed_width_major));
//        popupWindow.setFocusable (true);
//        popupWindow.setOutsideTouchable (true);
//        popupWindow.update ();
//    }
//
//    public void dismiss ()
//    {
//        popupWindow.dismiss ();
//    }
//
//    private final class PopAdapter extends BaseAdapter
//    {
//
//        @Override
//        public int getCount ()
//        {
//            return itemList.size ();
//        }
//
//        @Override
//        public Object getItem (int position)
//        {
//            return itemList.get (position);
//        }
//
//        @Override
//        public long getItemId (int position)
//        {
//            return position;
//        }
//
//        @Override
//        public View getView (int position, View convertView, ViewGroup parent)
//        {
//            ViewHolder holder;
//            if ( convertView == null )
//            {
//                convertView = LayoutInflater.from (context).inflate (
//                        R.layout.md_stub_progress, null);
//                holder = new ViewHolder ();
//                holder.groupItem = (TextView) convertView
//                        .findViewById (R.id.action_menu_divider);
//                holder.groupImag = (ImageButton) convertView
//                        .findViewById (R.id.action_menu_divider);
//                holder.view = convertView.findViewById (R.id.layout);
//                convertView.setTag (holder);
//
//            } else
//            {
//                holder = (ViewHolder) convertView.getTag ();
//            }
//            if ( position == 0 )
//            {
//                if ( position == (getCount () - 1) )
//                {
//                    holder.view
//                            .setBackgroundResource (R.drawable.list_round_single);
//                } else
//                {
//                    holder.view
//                            .setBackgroundResource (R.drawable.list_round_top);
//                }
//            } else if ( position == (getCount () - 1) )
//            {
//                holder.view.setBackgroundResource (R.drawable.list_round_bottom);
//            } else
//            {
//                holder.view.setBackgroundResource (R.drawable.list_round_middle);
//            }
//
//            holder.groupItem.setText (itemList.get (position));
//            if ( imgList.size () > 0 )
//            {
//                holder.groupImag.setBackgroundResource (imgList.get (position));
//            }
//            return convertView;
//        }
//
//        private final class ViewHolder
//        {
//            TextView groupItem;
//            ImageButton groupImag;
//            View view;
//        }
//    }
//}
