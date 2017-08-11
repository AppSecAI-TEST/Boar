package com.join.dialog;

import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;

import com.join.R;
import com.zhy.android.percent.support.PercentLinearLayout;

/**
 * Created by join on 2017/5/9.
 */


public class PasswordManageDialog extends Dialog {

    public PasswordManageDialog(Context context, int theme) {
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
        private OnClickListener positiveButtonClickListener;
        private OnClickListener negativeButtonClickListener;
        private ObtainPassword obtainPassword;

        public void setObtainPassword(ObtainPassword obtainPassword) {
            this.obtainPassword = obtainPassword;
        }

        public interface ObtainPassword {
            void getPassword(String pa);
        }

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
                                         OnClickListener listener) {
            this.positiveButtonText = (String) context
                    .getText(positiveButtonText);
            this.positiveButtonClickListener = listener;
            return this;
        }

        public Builder setPositiveButton(String positiveButtonText,
                                         OnClickListener listener) {
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
                                         OnClickListener listener) {
            this.negativeButtonText = (String) context
                    .getText(negativeButtonText);
            this.negativeButtonClickListener = listener;
            return this;
        }

        public Builder setNegativeButton(String negativeButtonText,
                                         OnClickListener listener) {
            this.negativeButtonText = negativeButtonText;
            this.negativeButtonClickListener = listener;
            return this;
        }

        // 这是自定义弹出框的关键
        public PasswordManageDialog create() {
            LayoutInflater inflater = (LayoutInflater) context
                    .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            // instantiate the dialog with the custom Theme
            final PasswordManageDialog dialog = new PasswordManageDialog(context,
                    R.style.ShareDialog);
            final View layout = inflater.inflate(R.layout.password_manager_dialog, null);
            PercentLinearLayout pl = (PercentLinearLayout) layout.findViewById(R.id.percent);
            pl.getBackground().setAlpha(150);
            //View v = layout.findViewById(R.id.title);
            //设置背景透明度
            //  v.getBackground().setAlpha(150);
            //关联布局
            dialog.addContentView(layout, new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT));
            // 设置标题

            // ((TextView) layout.findViewById(R.id.title)).setText(title);

            // 设置确认按钮
            if (positiveButtonText != null) {
                //设置按钮字体
                ((Button) layout.findViewById(R.id.confirm)).setText(positiveButtonText);
                if (positiveButtonClickListener != null) {
                    //设置按钮
                    ((Button) layout.findViewById(R.id.confirm))
                            .setOnClickListener(new View.OnClickListener() {
                                public void onClick(View v) {
                                    EditText viewById = (EditText) layout.findViewById(R.id.input);
                                    String text = viewById.getText().toString();
                                    if (obtainPassword!=null)
                                    obtainPassword.getPassword(text);
                                    positiveButtonClickListener.onClick(dialog, DialogInterface.BUTTON_POSITIVE);


                                }
                            });
                }
            } else {
                //如果没有确认按钮则隐藏
        /* layout.findViewById(R.id.positiveButton).setVisibility(
                        View.GONE);*/
            }
            // 设置取消按钮
            if (negativeButtonText != null) {
                ((Button) layout.findViewById(R.id.negativeButton)).setText(negativeButtonText);
                if (negativeButtonClickListener != null) {
                    ((Button) layout.findViewById(R.id.negativeButton))
                            .setOnClickListener(new View.OnClickListener() {
                                public void onClick(View v) {
                                    negativeButtonClickListener.onClick(dialog, DialogInterface.BUTTON_NEGATIVE);
                                }
                            });
                }
            } //else {
            //如果没有取消按钮则隐藏
            /*    layout.findViewById(R.id.negativeButton).setVisibility(
                        View.GONE);*/
            // }
            // 设置消息
    /*        if (message != null) {
                ((TextView) layout.findViewById(R.id.message)).setText(message);
            } else if (contentView != null) {
                ((LinearLayout) layout.findViewById(R.id.content))
                        .removeAllViews();
                ((LinearLayout) layout.findViewById(R.id.content)).addView(
                        contentView, new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT,
                                ViewGroup.LayoutParams.MATCH_PARENT));
            }*/

    /*        Window window = dialog.getWindow();
            WindowManager.LayoutParams lp = window.getAttributes();
            lp.alpha = 0.9f;
            window.setAttributes(lp);*/
            // 把设置好的View加载到弹出框
            dialog.setContentView(layout);
            return dialog;
        }
    }
}
