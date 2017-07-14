package com.join.adapter;

import android.content.Context;
import android.net.wifi.ScanResult;
import android.net.wifi.WifiManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.join.R;

import java.util.List;

import static com.join.R.id.wifi_level;

/**
 * Created by Administrator on 2017/6/27 0027.
 */

public class WiFiAdapter extends BaseAdapter {

    private LayoutInflater inflater;
    private List<ScanResult> list;
    private int level;  //得到信号

    public WiFiAdapter(Context context, List<ScanResult> list) {
        this.inflater = LayoutInflater.from(context);
        this.list = list;
    }

    @Override
    public int getCount() {
        return list.size();
    }

    @Override
    public Object getItem(int position) {
        return position;
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder holder;
        if (convertView == null) {
            holder = new ViewHolder();
            convertView = inflater.inflate(R.layout.wifi_item, null);
            convertView.setTag(holder);
            holder.icon = (ImageView) convertView.findViewById(wifi_level);
            holder.textView = (TextView) convertView.findViewById(R.id.ssid);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }
        ScanResult scanResult = list.get(position);
        holder.textView.setText(scanResult.SSID);
        level = WifiManager.calculateSignalLevel(scanResult.level, 5);//把信号量分成五个等级
        if (scanResult.capabilities.contains("WEP") || scanResult.capabilities.contains("PSK") ||
                scanResult.capabilities.contains("EAP")) {
            holder.icon.setImageResource(R.drawable.wifi_signal_lock);
        } else {
            holder.icon.setImageResource(R.drawable.wifi_signal_open);
        }
        holder.icon.setImageLevel(level);
        //判断信号强度，显示对应的指示图标  
        return convertView;

    }

    static class ViewHolder {
        TextView textView;
        ImageView icon;
    }

}
