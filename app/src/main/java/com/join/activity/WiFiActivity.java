package com.join.activity;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.wifi.ScanResult;
import android.net.wifi.WifiManager;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.join.R;
import com.join.adapter.WiFiAdapter;
import com.join.dialog.ManageDialog;
import com.join.utils.WifiAdmin;
import com.zhy.android.percent.support.PercentLinearLayout;

import java.util.List;

/**
 *
 */

public class WiFiActivity extends Activity implements View.OnClickListener {
    private String TAG = "jjjWiFiActivity";
    private ImageView open_wifi, tile_icon;
    private TextView title_name;
    private ListView mlistView;
    protected WifiAdmin mWifiAdmin;
    private List<ScanResult> mWifiList;
    private String ssid; //WiFi的名称
    private int flagClose;
    private PercentLinearLayout open_wifi_ll;
    private boolean closeThread = false;
    private int wifiClose;

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.wifi_setting);
        initViews();

        mlistView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view,
                                    int position, long id) {
               AlertDialog.Builder alert = new AlertDialog.Builder(WiFiActivity.this);
                ssid = mWifiList.get(position).SSID;
                alert.setTitle(ssid);
                alert.setMessage("输入密码");
                final EditText et_password = new EditText(WiFiActivity.this);
                final SharedPreferences preferences = getSharedPreferences("wifi_password", Context.MODE_PRIVATE);
               et_password.setText(preferences.getString(ssid, ""));
               alert.setView(et_password);

              alert.setPositiveButton("连接", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        String pw = et_password.getText().toString();
                        if (null == pw || pw.length() < 8) {
                            Toast.makeText(WiFiActivity.this, "密码至少8位", Toast.LENGTH_SHORT).show();
                            return;
                        }
                        SharedPreferences.Editor editor = preferences.edit();
                        editor.putString(ssid, pw);
                        editor.commit();
                        Log.e(TAG, "onClick: "+"jjjjjjjjjjjj" + et_password.getText().toString());
                        mWifiAdmin.addNetwork(mWifiAdmin.CreateWifiInfo(ssid, et_password.getText().toString(), 3));
                    }
                });
                alert.setNegativeButton("取消", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        //
                        mWifiAdmin.removeWifi(mWifiAdmin.getNetworkId());
                    }
                });
                alert.create();
                alert.show();
                //showAlertDialog();
            }
        });
    }

    /**
     * 控件初始化
     */
    private void initViews() {
        mWifiAdmin = new WifiAdmin(WiFiActivity.this);
        //注册监听WiFi连接的状态
        IntentFilter filter = new IntentFilter(WifiManager.NETWORK_STATE_CHANGED_ACTION);
        registerReceiver(mReceiver, filter);
        open_wifi = (ImageView) findViewById(R.id.open_wifi);
        mlistView = (ListView) findViewById(R.id.wifi_list);
        title_name = (TextView) findViewById(R.id.title_name);
        open_wifi_ll = (PercentLinearLayout) findViewById(R.id.open_wifi_ll);
        open_wifi_ll.setOnClickListener(this);
        tile_icon = (ImageView) findViewById(R.id.title_icon);
        title_name.setText(mWifiAdmin.getConnectWifiSsid());
        wifiClose = mWifiAdmin.checkState(WiFiActivity.this);
        if (wifiClose == 3) {
            tile_icon.setVisibility(View.VISIBLE);
            open_wifi.setImageResource(R.mipmap.wifi_setting_1);
            flagClose = 0;
            mWifiAdmin.startScan(this);
            mWifiList = mWifiAdmin.getWifiList();
            if (mWifiList != null) {
                mlistView.setAdapter(new WiFiAdapter(WiFiActivity.this, mWifiList));
                new Utility().setListViewHeightBasedOnChildren(mlistView);
            }
        } else {
            title_name.setText("查看可用网络,请打开WiFi");
            title_name.setTextColor(Color.CYAN);
            open_wifi.setImageResource(R.mipmap.wifi_setting_3);
            flagClose = 1;
        }
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.open_wifi_ll:
                flagClose++;
                if (flagClose % 2 == 0) {
                    open_wifi.setImageResource(R.mipmap.wifi_setting_1);
                    mWifiAdmin.openWifi(WiFiActivity.this);
                    closeThread = true;
                    try {
                        Thread.sleep(2000);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                    wifiClose = mWifiAdmin.checkState(WiFiActivity.this);

                    if (wifiClose == 2) {
                        WifiThread wifiThread = new WifiThread();
                        wifiThread.start();
                    }

                } else {
                    open_wifi.setImageResource(R.mipmap.wifi_setting_3);
                    title_name.setText("查看可用网络,请打开WiFi");
                    tile_icon.setVisibility(View.GONE);
                    mWifiAdmin.closeWifi(WiFiActivity.this);
                    mlistView.setAdapter(null);
                    closeThread = false;
                }
                break;
            default:
                break;
        }
    }


    /**
     * 设置listview的高度
     */
    public class Utility {
        public void setListViewHeightBasedOnChildren(ListView listView) {
            ListAdapter listAdapter = listView.getAdapter();
            if (listAdapter == null) {
                return;
            }
            int totalHeight = 0;
            for (int i = 0; i < listAdapter.getCount(); i++) {
                View listItem = listAdapter.getView(i, null, listView);
                listItem.measure(0, 0);
                totalHeight += listItem.getMeasuredHeight();
            }
            ViewGroup.LayoutParams params = listView.getLayoutParams();
            params.height = totalHeight + (listView.getDividerHeight() * (listAdapter.getCount() - 1));
            listView.setLayoutParams(params);
        }
    }

    /**
     * 监听wifi状态变化
     */
    private BroadcastReceiver mReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            ConnectivityManager manager = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
            //判断是否连接成功
            NetworkInfo wifiInfo = manager.getNetworkInfo(ConnectivityManager.TYPE_WIFI);
            if (wifiInfo.isConnected()) {
                WifiManager wifiManager = (WifiManager) context.getSystemService(Context.WIFI_SERVICE);
                String wifiSSID = wifiManager.getConnectionInfo().getSSID();
                Toast.makeText(context, wifiSSID + "连接成功", Toast.LENGTH_LONG).show();
                title_name.setText(mWifiAdmin.getConnectWifiSsid());
                tile_icon.setVisibility(View.VISIBLE);

            }
        }

    };

    @Override
    protected void onResume() {
        super.onResume();

    }

    @Override
    protected void onPause() {
        super.onPause();
        closeThread = false;
        unregisterReceiver(mReceiver);
    }

    class WifiThread extends Thread {
        @Override
        public void run() {
            super.run();
            while (closeThread) {
                mWifiAdmin.startScan(WiFiActivity.this);
                mWifiList = mWifiAdmin.getWifiList();
                if (mWifiList != null) {
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            mlistView.setAdapter(new WiFiAdapter(WiFiActivity.this, mWifiList));
                            new Utility().setListViewHeightBasedOnChildren(mlistView);
                        }
                    });
                }
                try {
                    Thread.sleep(10000);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        }
    }
    /**
     * 最初版的手机号输入当做密码
     */
    public void showAlertDialog() {

        ManageDialog.Builder builder = new ManageDialog.Builder(this,mWifiAdmin,ssid);
/*        builder.setMessage("这个就是自定义的提示框");
        builder.setTitle("提示");*/
        builder.setPositiveButton("确定", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int which) {
                // 隐藏dialog
                dialog.dismiss();
            }
        });

        builder.setNegativeButton("取消",
                new android.content.DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        // 隐藏dialog
                        dialog.dismiss();
                        System.out.println("------------------点击取消----------------");
                    }
                });
        builder.create().show();
    }
}
