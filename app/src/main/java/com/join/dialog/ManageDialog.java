package com.join.dialog;

import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;

import com.join.R;

/**
 * Created by join on 2017/5/9.
 */


public class ManageDialog extends Dialog {

    public ManageDialog(Context context, int theme) {
        super(context, theme);
    }

    // 主要是重写Bulder内部类
    public static class Builder {

        private Context context;
        private String title;
        private String message;
        private String positiveButtonText;
        private String negativeButtonText;
        private View contentView;
        private DialogInterface.OnClickListener positiveButtonClickListener;
        private DialogInterface.OnClickListener negativeButtonClickListener;

        public Builder(Context context) {
            this.context = context;
        }

        public Builder setMessage(String message) {
            this.message = message;
            return this;
        }

        /**
         * 设置的内容
         *
         * @param message
         * @return
         */
        public Builder setMessage(int message) {
            this.message = (String) context.getText(message);
            return this;
        }

        /**
         * 设置标题
         *
         * @param title
         * @return
         */

        public Builder setTitle(int title) {
            this.title = (String) context.getText(title);
            return this;
        }

        public Builder setTitle(String title) {
            this.title = title;
            return this;
        }

        public Builder setContentView(View v) {
            this.contentView = v;
            return this;
        }

        /**
         * 设置确定按钮的监听
         *
         * @param positiveButtonText
         * @param listener
         * @return
         */


        public Builder setPositiveButton(int positiveButtonText,
                                         DialogInterface.OnClickListener listener) {
            this.positiveButtonText = (String) context
                    .getText(positiveButtonText);
            this.positiveButtonClickListener = listener;
            return this;
        }

        public Builder setPositiveButton(String positiveButtonText,
                                         DialogInterface.OnClickListener listener) {
            this.positiveButtonText = positiveButtonText;
            this.positiveButtonClickListener = listener;
            return this;
        }

        /**
         * 设置取消按钮的监听
         *
         * @param negativeButtonText
         * @param listener
         * @return
         */
        public Builder setNegativeButton(int negativeButtonText,
                                         DialogInterface.OnClickListener listener) {
            this.negativeButtonText = (String) context
                    .getText(negativeButtonText);
            this.negativeButtonClickListener = listener;
            return this;
        }

        public Builder setNegativeButton(String negativeButtonText,
                                         DialogInterface.OnClickListener listener) {
            this.negativeButtonText = negativeButtonText;
            this.negativeButtonClickListener = listener;
            return this;
        }

        // 这是自定义弹出框的关键
        public ManageDialog create() {
            LayoutInflater inflater = (LayoutInflater) context
                    .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            // instantiate the dialog with the custom Theme
            final ManageDialog dialog = new ManageDialog(context,
                    R.style.Dialog);
            final View layout = inflater.inflate(R.layout.manage_dialog, null);
            //View v = layout.findViewById(R.id.title);
            //设置背景透明度
            //v.getBackground().setAlpha(150);
            //关联布局
            dialog.addContentView(layout, new ViewGroup.LayoutParams(
                    ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT));
            // 设置标题

           // ((TextView) layout.findViewById(R.id.title)).setText(title);
            // 设置确认按钮
            if (positiveButtonText != null) {
                //设置按钮字体
                ((Button) layout.findViewById(R.id.confirm))
                        .setText(positiveButtonText);
                if (positiveButtonClickListener != null) {
                    //设置按钮
                    ((Button) layout.findViewById(R.id.confirm))
                            .setOnClickListener(new View.OnClickListener() {
                                public void onClick(View v) {
                                    positiveButtonClickListener.onClick(dialog,
                                            DialogInterface.BUTTON_POSITIVE);
                                    //得到输入的内容
                                    EditText viewById = (EditText) layout.findViewById(R.id.input);
                                    String s = viewById.getText().toString();
                                    Log.e("jjj", s);
                                }
                            });
                }
            } /*else {
                //如果没有确认按钮则隐藏
             *//*   layout.findViewById(R.id.positiveButton).setVisibility(
                        View.GONE);*//*
            }
            // 设置取消按钮
            if (negativeButtonText != null) {
                ((Button) layout.findViewById(R.id.negativeButton))
                        .setText(negativeButtonText);
                if (negativeButtonClickListener != null) {
                    ((Button) layout.findViewById(R.id.negativeButton))
                            .setOnClickListener(new View.OnClickListener() {
                                public void onClick(View v) {
                                    negativeButtonClickListener.onClick(dialog,
                                            DialogInterface.BUTTON_NEGATIVE);
                                }
                            });
                }
            } else {
                //如果没有取消按钮则隐藏
                layout.findViewById(R.id.negativeButton).setVisibility(
                        View.GONE);
            }
            // 设置消息
            if (message != null) {
                ((TextView) layout.findViewById(R.id.message)).setText(message);
            } else if (contentView != null) {
                ((LinearLayout) layout.findViewById(R.id.content))
                        .removeAllViews();
                ((LinearLayout) layout.findViewById(R.id.content)).addView(
                        contentView, new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT,
                                ViewGroup.LayoutParams.MATCH_PARENT));
            }*/
            // 把设置好的View加载到弹出框
            dialog.setContentView(layout);
            return dialog;
        }
    }
}
