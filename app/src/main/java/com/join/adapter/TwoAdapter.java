package com.join.adapter;

import android.content.Context;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.join.R;

import java.util.List;

/**
 * Created by join on 2017/5/8.
 */

public class TwoAdapter extends BaseAdapter {
    private Context context;

    private List<String> list;

    public TwoAdapter(Context context, List<String> list) {

        this.context = context;
        this.list = list;

    }

    @Override
    public int getCount() {
        return list.size();
    }

    @Override
    public Object getItem(int position) {

        return list.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup viewGroup) {


        ViewHolder holder;
        if (convertView == null) {
            convertView = LayoutInflater.from(context).inflate(R.layout.two_adapter_item, null);
            holder = new ViewHolder();

            convertView.setTag(holder);

            holder.groupItem = (TextView) convertView.findViewById(R.id.two_item);

        } else {
            holder = (ViewHolder) convertView.getTag();
        }
        holder.groupItem.setTextColor(Color.WHITE);
        holder.groupItem.setText(list.get(position));

        return convertView;
    }

    static class ViewHolder {
        TextView groupItem;
    }

}
