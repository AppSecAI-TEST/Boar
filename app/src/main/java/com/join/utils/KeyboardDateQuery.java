package com.join.utils;

import android.content.Context;
import android.content.Intent;
import android.graphics.drawable.BitmapDrawable;
import android.os.Bundle;
import android.text.InputType;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.PopupWindow;

import com.join.R;
import com.zhy.android.percent.support.PercentLinearLayout;

import java.lang.reflect.Method;


public class KeyboardDateQuery implements View.OnClickListener, View.OnTouchListener {
    private Button bu_0, bu_1, bu_2, bu_3, bu_4, bu_5, bu_6, bu_7, bu_8, bu_9, bu_w, delete, out;
    private EditText input_1, input_2;
    private Context context;
    private View view;
    private PopupWindow popupWindow;
    private Intent intent;
    private String TAG = "jjjKeyboardDateQuery";

    public KeyboardDateQuery(Context context, Intent intent) {
        this.context = context;
        this.intent = intent;

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
        input_1 = (EditText) view.findViewById(R.id.input_1);
        input_2 = (EditText) view.findViewById(R.id.input_2);
        closeKeyboard(input_1);
        closeKeyboard(input_2);
        input_1.setOnTouchListener(this);
        input_2.setOnTouchListener(this);

    }

    public void showWindow(View parent) {

        if (popupWindow == null) {
            LayoutInflater layoutInflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);

            view = layoutInflater.inflate(R.layout.keyboard_date, null);
            PercentLinearLayout pl = (PercentLinearLayout) view.findViewById(R.id.percent);
            pl.getBackground().setAlpha(180);
            init();

            // 创建一个PopuWidow对象 设置宽,高,
            popupWindow = new PopupWindow(view, 1280, 752);
        }
        popupWindow.setFocusable(false); // 获取焦点
        popupWindow.setTouchable(true); // 设置popupwindow可点击
        popupWindow.setOutsideTouchable(false); // 设置popupwindow外部不可点击


        // 这个是为了点击“返回Back”也能使其消失，并且并不会影响你的背景
        popupWindow.setBackgroundDrawable(new BitmapDrawable());
        WindowManager windowManager = (WindowManager) context.getSystemService(Context.WINDOW_SERVICE);

        int xPos = windowManager.getDefaultDisplay().getWidth();
        int height = windowManager.getDefaultDisplay().getHeight();
        Log.i("coder", "xPos:" + xPos + "\n" + height);
        //以parent 为开始的x,y轴
        popupWindow.showAsDropDown(parent, -xPos, -height);

    }

    //开始日期输入框的buffer
    private StringBuffer buffer = new StringBuffer();
    //结束日期输入框的buffer
    private StringBuffer buffer2 = new StringBuffer();
    //点击区分那个输入框
    private int selectTag = 0;

    @Override
    public void onClick(View v) {
        int lengthFather_1 = buffer.length();
        int lengthFather_2 = buffer2.length();

        switch (v.getId()) {
          /*  case R.id.input_1:
                //buffer.delete(0, lengthFather_1);
                selectTag = 1;
                bu_0.setEnabled(true);
                bu_1.setEnabled(true);
                bu_2.setEnabled(true);
                bu_3.setEnabled(true);
                bu_4.setEnabled(true);
                bu_5.setEnabled(true);
                bu_6.setEnabled(true);
                bu_7.setEnabled(true);
                bu_8.setEnabled(true);
                bu_9.setEnabled(true);
                delete.setEnabled(true);
                bu_w.setEnabled(true);

                break;
            case R.id.input_2:
                //  buffer.delete(0, lengthFather_1);
                selectTag = 2;
                bu_0.setEnabled(true);
                bu_1.setEnabled(true);
                bu_2.setEnabled(true);
                bu_3.setEnabled(true);
                bu_4.setEnabled(true);
                bu_5.setEnabled(true);
                bu_6.setEnabled(true);
                bu_7.setEnabled(true);
                bu_8.setEnabled(true);
                bu_9.setEnabled(true);
                delete.setEnabled(true);
                bu_w.setEnabled(true);
                break;*/
            case R.id.bu_0:


                Log.e(TAG, "onClick: " + selectTag);

                if (selectTag == 1) {
                    if (lengthFather_1 != 8) {
                        buffer.append("0");
                        input_1.setText(buffer.toString());
                    }
                }
                if (selectTag == 2) {
                    if (lengthFather_2 != 8) {
                        buffer2.append("0");
                        input_2.setText(buffer2.toString());
                    }
                }
                break;
            case R.id.bu_1:
                Log.e(TAG, "onClick: " + selectTag);
                if (selectTag == 1) {
                    if (lengthFather_1 != 8) {
                        buffer.append("1");
                        input_1.setText(buffer.toString());
                    }
                }
                if (selectTag == 2) {
                    if (lengthFather_2 != 8) {
                        buffer2.append("1");
                        input_2.setText(buffer2.toString());
                    }
                }

                break;
            case R.id.bu_2:
                Log.e(TAG, "onClick: " + selectTag);
                if (selectTag == 1) {
                    if (lengthFather_1 != 8) {
                        buffer.append("2");
                        input_1.setText(buffer.toString());
                    }
                }

                if (selectTag == 2) {
                    if (lengthFather_2 != 8) {
                        buffer2.append("2");
                        input_2.setText(buffer2.toString());
                    }
                }

                break;
            case R.id.bu_3:
                Log.e(TAG, "onClick: " + selectTag);
                if (selectTag == 1) {
                    if (lengthFather_1 != 8) {
                        buffer.append("3");
                        input_1.setText(buffer.toString());
                    }
                }

                if (selectTag == 2) {
                    if (lengthFather_2 != 8) {
                        buffer2.append("3");
                        input_2.setText(buffer2.toString());
                    }
                }

                break;
            case R.id.bu_4:
                Log.e(TAG, "onClick: " + selectTag);
                if (selectTag == 1) {
                    if (lengthFather_1 != 8) {
                        buffer.append("4");
                        input_1.setText(buffer.toString());
                    }
                }

                if (selectTag == 2) {
                    if (lengthFather_2 != 8) {
                        buffer2.append("4");
                        input_2.setText(buffer2.toString());
                    }
                }
                break;
            case R.id.bu_5:
                Log.e(TAG, "onClick: " + selectTag);
                if (selectTag == 1) {
                    if (lengthFather_1 != 8) {
                        buffer.append("5");
                        input_1.setText(buffer.toString());
                    }
                }

                if (selectTag == 2) {
                    if (lengthFather_2 != 8) {
                        buffer2.append("5");
                        input_2.setText(buffer2.toString());
                    }
                }
                break;
            case R.id.bu_6:
                Log.e(TAG, "onClick: " + selectTag);
                if (selectTag == 1) {
                    if (lengthFather_1 != 8) {
                        buffer.append("6");
                        input_1.setText(buffer.toString());
                    }
                }
                if (selectTag == 2) {
                    if (lengthFather_2 != 8) {
                        buffer2.append("6");
                        input_2.setText(buffer2.toString());
                    }
                }

                break;
            case R.id.bu_7:
                Log.e(TAG, "onClick: " + selectTag);
                if (selectTag == 1) {
                    if (lengthFather_1 != 8) {
                        buffer.append("7");
                        input_1.setText(buffer.toString());
                    }
                }

                if (selectTag == 2) {
                    if (lengthFather_2 != 8) {
                        buffer2.append("7");
                        input_2.setText(buffer2.toString());
                    }
                }
                break;
            case R.id.bu_8:
                Log.e(TAG, "onClick: " + selectTag);
                if (selectTag == 1) {
                    if (lengthFather_1 != 8) {
                        buffer.append("8");
                        input_1.setText(buffer.toString());
                    }
                }

                if (selectTag == 2) {
                    if (lengthFather_2 != 8) {
                        buffer2.append("8");
                        input_2.setText(buffer2.toString());
                    }
                }
                break;
            case R.id.bu_9:
                Log.e(TAG, "onClick: " + selectTag);
                if (selectTag == 1) {
                    if (lengthFather_1 != 8) {
                        buffer.append("9");
                        input_1.setText(buffer.toString());
                    }
                }

                if (selectTag == 2) {
                    if (lengthFather_2 != 8) {
                        buffer2.append("9");
                        input_2.setText(buffer2.toString());
                    }
                }
                break;
            case R.id.delete:
                if (selectTag == 1) {
                    if (lengthFather_1 > 0) {
                        Log.e(TAG, "onClick: " + selectTag);
                        int length = buffer.length();
                        buffer.delete(length - 1, length);
                        input_1.setText(buffer.toString());
                    }
                } else if (selectTag == 2) {
                    if (lengthFather_2 > 0) {
                        Log.e(TAG, "onClick: " + selectTag);
                        int length = buffer2.length();
                        buffer2.delete(length - 1, length);
                        input_2.setText(buffer2.toString());
                    }
                }
                break;
            case R.id.out:

                if (selectTag == 1) {
                    if (lengthFather_1 >= 0) {
                        Log.e(TAG, "onClick: " + selectTag);
                        buffer.delete(0, lengthFather_1);
                        input_1.setText(null);

                    }
                } else if (selectTag == 2) {
                    if (lengthFather_2 >= 0) {
                        Log.e(TAG, "onClick: " + selectTag);
                        buffer2.delete(0, lengthFather_2);
                        input_2.setText(null);

                    }
                }
                if (popupWindow != null) {
                    popupWindow.dismiss();
                }
                break;
            case R.id.bu_w:
                if (lengthFather_1 > 0 && lengthFather_2 > 0) {
                    Log.e(TAG, "onClick: " + selectTag);
                    if (popupWindow != null) {
                        popupWindow.dismiss();
                        Bundle bundle = new Bundle();
                        bundle.putString("KeyboardDateQueryData1", input_1.getText().toString());
                        bundle.putString("KeyboardDateQueryData2", input_2.getText().toString());
                        buffer.delete(0, lengthFather_1);
                        buffer2.delete(0, lengthFather_2);
                        input_1.setText(null);
                        input_2.setText(null);
                        intent.putExtras(bundle);
                        intent.setAction("com.join.DateQuery");
                        context.startActivity(intent);

                    }
                }
                break;
        }
    }

    @Override
    public boolean onTouch(View v, MotionEvent event) {
        switch (v.getId()) {
            case R.id.input_1:
                selectTag = 1;
                bu_0.setEnabled(true);
                bu_1.setEnabled(true);
                bu_2.setEnabled(true);
                bu_3.setEnabled(true);
                bu_4.setEnabled(true);
                bu_5.setEnabled(true);
                bu_6.setEnabled(true);
                bu_7.setEnabled(true);
                bu_8.setEnabled(true);
                bu_9.setEnabled(true);
                delete.setEnabled(true);
                bu_w.setEnabled(true);

                break;
            case R.id.input_2:
                //  buffer.delete(0, lengthFather_1);
                selectTag = 2;
                bu_0.setEnabled(true);
                bu_1.setEnabled(true);
                bu_2.setEnabled(true);
                bu_3.setEnabled(true);
                bu_4.setEnabled(true);
                bu_5.setEnabled(true);
                bu_6.setEnabled(true);
                bu_7.setEnabled(true);
                bu_8.setEnabled(true);
                bu_9.setEnabled(true);
                delete.setEnabled(true);
                bu_w.setEnabled(true);
                break;
        }


        return false;
    }

    /**
     * 关闭EditText的软件盘
     *
     * @param et
     */
    private void closeKeyboard(EditText et) {
        if (android.os.Build.VERSION.SDK_INT <= 10) {
            et.setInputType(InputType.TYPE_NULL);
        } else {
            Class<EditText> cls = EditText.class;
            Method method;
            try {
                method = cls.getMethod("setShowSoftInputOnFocus", boolean.class);
                method.setAccessible(true);
                method.invoke(et, false);
            } catch (Exception e) {
            }

            try {
                method = cls.getMethod("setSoftInputShownOnFocus", boolean.class);
                method.setAccessible(true);
                method.invoke(et, false);
            } catch (Exception e) {
            }
        }
    }

}
