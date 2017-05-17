package com.join.utils;

import android.content.Context;
import android.graphics.drawable.BitmapDrawable;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.PopupWindow;
import android.widget.TextView;
import android.widget.Toast;

import com.join.R;
import com.join.adapter.TwoAdapter;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by join on 2017/5/8.
 */

public class PopupWindowUtil {
    private Context context;
    private List<String> list;
    private View view;
    private ListView lv_group;
    private PopupWindow popupWindow;
    private TextView editText;
    private ImageView icon;

    public PopupWindowUtil(Context context, TextView editText, ImageView icon) {
        this.context = context;
        this.editText = editText;
        this.icon = icon;
    }

    public void showWindow(View parent) {

        if (popupWindow == null) {
            LayoutInflater layoutInflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);

            view = layoutInflater.inflate(R.layout.two_list_window, null);
            LinearLayout linear = (LinearLayout) view.findViewById(R.id.listview_background);
            linear.getBackground().setAlpha(255);

            lv_group = (ListView) view.findViewById(R.id.lvGroup);
            // 加载数据
            list = new ArrayList<String>();
            list.add("001");
            list.add("002");
            list.add("003");
            list.add("004");
            list.add("005");


            TwoAdapter groupAdapter = new TwoAdapter(context, list);
            lv_group.setAdapter(groupAdapter);
            // 创建一个PopuWidow对象 设置宽,高
            popupWindow = new PopupWindow(view, 270, 360);
        }
        popupWindow.setFocusable(false); // 获取焦点
        popupWindow.setTouchable(true); // 设置popupwindow可点击
        popupWindow.setOutsideTouchable(false); // 设置popupwindow外部可点击


        // 这个是为了点击“返回Back”也能使其消失，并且并不会影响你的背景
        popupWindow.setBackgroundDrawable(new BitmapDrawable());
        WindowManager windowManager = (WindowManager) context.getSystemService(Context.WINDOW_SERVICE);

        int xPos = windowManager.getDefaultDisplay().getWidth();
        Log.i("coder", "xPos:" + xPos);
        //以parent 为开始的x,y轴
        popupWindow.showAsDropDown(parent, -25, 0);

        lv_group.setOnItemClickListener(new AdapterView.OnItemClickListener() {

            @Override
            public void onItemClick(AdapterView<?> adapterView, View view,
                                    int position, long id) {

                Toast.makeText(context, list.get(position), Toast.LENGTH_LONG).show();
                editText.setText(list.get(position));
                icon.setBackgroundResource(R.drawable.a_011);

                if (popupWindow != null) {
                    popupWindow.dismiss();
                }
            }
        });
    }
}
