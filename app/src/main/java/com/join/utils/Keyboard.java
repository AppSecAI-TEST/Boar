package com.join.utils;

import android.content.Context;
import android.graphics.drawable.BitmapDrawable;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.PopupWindow;
import android.widget.TextView;

import com.join.R;


public class Keyboard implements View.OnClickListener {
    private Button bu_0, bu_1, bu_2, bu_3, bu_4, bu_5, bu_6, bu_7, bu_8, bu_9, bu_w, delete, out;
    private TextView input, id_Gong;
    private Context context;
    private View view;
    private PopupWindow popupWindow;

    public Keyboard(Context context, TextView id_Gong) {
        this.context = context;
        this.id_Gong = id_Gong;
    }

    private void init() {
        bu_0 = (Button) view.findViewById(R.id.bu_0);
        bu_0.setOnClickListener(this);
        bu_1 = (Button) view.findViewById(R.id.bu_1);
        bu_1.setOnClickListener(this);
        bu_2 = (Button) view.findViewById(R.id.bu_2);
        bu_2.setOnClickListener(this);
        bu_3 = (Button) view.findViewById(R.id.bu_3);
        bu_3.setOnClickListener(this);
        bu_4 = (Button) view.findViewById(R.id.bu_4);
        bu_4.setOnClickListener(this);
        bu_5 = (Button) view.findViewById(R.id.bu_5);
        bu_5.setOnClickListener(this);
        bu_6 = (Button) view.findViewById(R.id.bu_6);
        bu_6.setOnClickListener(this);
        bu_7 = (Button) view.findViewById(R.id.bu_7);
        bu_7.setOnClickListener(this);
        bu_8 = (Button) view.findViewById(R.id.bu_8);
        bu_8.setOnClickListener(this);
        bu_9 = (Button) view.findViewById(R.id.bu_9);
        bu_9.setOnClickListener(this);
        delete = (Button) view.findViewById(R.id.delete);
        delete.setOnClickListener(this);
        out = (Button) view.findViewById(R.id.out);
        out.setOnClickListener(this);
        bu_w = (Button) view.findViewById(R.id.bu_w);
        bu_w.setOnClickListener(this);
        input = (TextView) view.findViewById(R.id.input);

    }

    public void showWindow(View parent) {

        if (popupWindow == null) {
            LayoutInflater layoutInflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);

            view = layoutInflater.inflate(R.layout.keyboard, null);
            init();
            // 创建一个PopuWidow对象 设置高,宽
            popupWindow = new PopupWindow(view, 900, 760);
        }
        popupWindow.setFocusable(false); // 获取焦点
        popupWindow.setTouchable(true); // 设置popupwindow可点击
        popupWindow.setOutsideTouchable(false); // 设置popupwindow外部不可点击


        // 这个是为了点击“返回Back”也能使其消失，并且并不会影响你的背景
        popupWindow.setBackgroundDrawable(new BitmapDrawable());
        WindowManager windowManager = (WindowManager) context.getSystemService(Context.WINDOW_SERVICE);

        int xPos = windowManager.getDefaultDisplay().getWidth();
        Log.i("coder", "xPos:" + xPos);
        //以parent 为开始的x,y轴
        popupWindow.showAsDropDown(parent, -70, -220);


    }

    StringBuffer buffer = new StringBuffer();


    @Override
    public void onClick(View v) {
        int length1 = buffer.length();
        switch (v.getId()) {
            case R.id.bu_0:
                if (length1 != 5) {
                    buffer.append("0");
                    input.setText(buffer.toString());
                }
                break;
            case R.id.bu_1:
                if (length1 != 5) {
                    buffer.append("1");
                    input.setText(buffer.toString());
                }
                break;
            case R.id.bu_2:
                if (length1 != 5) {
                    buffer.append("2");
                    input.setText(buffer.toString());
                }
                break;
            case R.id.bu_3:
                if (length1 != 5) {
                    buffer.append("3");
                    input.setText(buffer.toString());
                }
                break;
            case R.id.bu_4:
                if (length1 != 5) {
                    buffer.append("4");
                    input.setText(buffer.toString());
                }
                break;
            case R.id.bu_5:
                if (length1 != 5) {
                    buffer.append("5");
                    input.setText(buffer.toString());
                }
                break;
            case R.id.bu_6:
                if (length1 != 5) {
                    buffer.append("6");
                    input.setText(buffer.toString());
                }
                break;
            case R.id.bu_7:
                if (length1 != 5) {
                    buffer.append("7");
                    input.setText(buffer.toString());
                }
                break;
            case R.id.bu_8:
                if (length1 != 5) {
                    buffer.append("8");
                    input.setText(buffer.toString());
                }
                break;
            case R.id.bu_9:
                if (length1 != 5) {
                    buffer.append("9");
                    input.setText(buffer.toString());
                }
                break;
            case R.id.delete:
                if (length1 > 0) {
                    int length = buffer.length();
                    Log.e("jjjjjj", "" + length);
                    buffer.delete(length - 1, length);
                    input.setText(buffer.toString());
                }
                break;
            case R.id.out:
                if (popupWindow != null) {
                    popupWindow.dismiss();
                }
                break;
            case R.id.bu_w:
                if (length1 > 0) {
                    id_Gong.setText(buffer);
                }
                if (popupWindow != null) {
                    popupWindow.dismiss();
                }
                break;
        }
    }
}
