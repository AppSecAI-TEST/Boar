package com.join.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.join.R;
import com.join.entity.IDQuery;

import java.util.List;

/**
 * Created by join on 2017/5/8.
 */

public class IDQueryAdapter extends BaseAdapter {
    private Context context;

    private List<IDQuery> list;

    public IDQueryAdapter(Context context, List<IDQuery> list) {

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
            convertView = LayoutInflater.from(context).inflate(R.layout.id_query_item, null);
            holder = new ViewHolder();



            holder.date = (TextView) convertView.findViewById(R.id.date);
            holder.time = (TextView) convertView.findViewById(R.id.time);
            holder.type = (TextView) convertView.findViewById(R.id.type);
            holder.density = (TextView) convertView.findViewById(R.id.density);
            holder.vitality = (TextView) convertView.findViewById(R.id.vitality);
            holder.motilityRate = (TextView) convertView.findViewById(R.id.motilityRate);
            holder.operator = (TextView) convertView.findViewById(R.id.operator);
            holder.result = (TextView) convertView.findViewById(R.id.result);
            holder.check = (TextView) convertView.findViewById(R.id.check);
            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }
        IDQuery idQuery = list.get(position);
        // holder.groupItem.setTextColor(Color.WHITE);
        holder.date.setText(idQuery.getDate());
        holder.time.setText(idQuery.getTime());
        holder.type.setText(idQuery.getType());
        holder.density.setText(idQuery.getDensity());
        holder.vitality.setText(idQuery.getVitality());
        holder.motilityRate.setText(idQuery.getMotilityRate());
        holder.operator.setText(idQuery.getOperator());
        holder.result.setText(idQuery.getResult());
        holder.check.setText(idQuery.getCheck());
        return convertView;
    }

    static class ViewHolder {
        TextView date;
        TextView time;
        TextView type;
        TextView density;
        TextView vitality;
        TextView motilityRate;
        TextView operator;
        TextView result;
        TextView check;

    }

}
