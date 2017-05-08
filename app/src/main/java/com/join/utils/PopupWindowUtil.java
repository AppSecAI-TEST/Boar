package com.join.utils;

import android.content.Context;
import android.graphics.drawable.BitmapDrawable;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.PopupWindow;
import android.widget.Toast;

import com.join.R;
import com.join.adapter.TwoAdapter;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by join on 2017/5/8.
 */

public class PopupWindowUtil {
    Context context;
    List<String> list;
    private View view;
    private ListView lv_group;
    private PopupWindow popupWindow;

    public PopupWindowUtil(Context context) {
        this.context = context;

    }

    public void showWindow(View parent) {

        if (popupWindow == null) {
            LayoutInflater layoutInflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);

            view = layoutInflater.inflate(R.layout.two_list_window, null);

            lv_group = (ListView) view.findViewById(R.id.lvGroup);
            // 加载数据
            list = new ArrayList<String>();
            list.add("全部");
            list.add("我的微博");
            list.add("好友");
            list.add("亲人");
            list.add("同学");
            list.add("朋友");
            list.add("陌生人");

            TwoAdapter groupAdapter = new TwoAdapter(context, list);
            lv_group.setAdapter(groupAdapter);
            // 创建一个PopuWidow对象 设置高,宽
            popupWindow = new PopupWindow(view, 100, 100);
        }

        // 使其聚集
        popupWindow.setFocusable(true);
        // 设置允许在外点击消失
        popupWindow.setOutsideTouchable(true);

        // 这个是为了点击“返回Back”也能使其消失，并且并不会影响你的背景
        popupWindow.setBackgroundDrawable(new BitmapDrawable());
        WindowManager windowManager = (WindowManager) context.getSystemService(Context.WINDOW_SERVICE);

        int xPos = windowManager.getDefaultDisplay().getWidth();
        Log.i("coder", "xPos:" + xPos);
        //以parent 为开始的x,y轴
        popupWindow.showAsDropDown(parent, -50, 0);

        lv_group.setOnItemClickListener(new AdapterView.OnItemClickListener() {

            @Override
            public void onItemClick(AdapterView<?> adapterView, View view,
                                    int position, long id) {

                Toast.makeText(context, list.get(position), Toast.LENGTH_LONG).show();

                if (popupWindow != null) {
                    popupWindow.dismiss();
                }
            }
        });
    }
}
