package com.join.activity;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.wifi.ScanResult;
import android.net.wifi.WifiManager;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.join.R;
import com.join.adapter.WiFiAdapter;
import com.join.utils.WifiAdmin;

import java.util.List;

/**
 *
 */

public class WiFiActivity extends Activity implements View.OnClickListener {
    private String TAG = "jjjWiFiActivity";
    private Button check_wifi, close_wifi, scan_wifi;
    private ImageView open_wifi;
    private TextView title_name;
    private ListView mlistView;
    protected WifiAdmin mWifiAdmin;
    private List<ScanResult> mWifiList;
    protected String ssid; //WiFi的名称

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.wifi_setting);
        mWifiAdmin = new WifiAdmin(WiFiActivity.this);
        initViews();
        IntentFilter filter = new IntentFilter(WifiManager.NETWORK_STATE_CHANGED_ACTION);

        registerReceiver(mReceiver, filter);
        title_name.setText(mWifiAdmin.getConnectWifiSsid());

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

            }
        });

    }

    /**
     * 控件初始化
     */
    private void initViews() {
        // check_wifi = (Button) findViewById(R.id.check_wifi);
        open_wifi = (ImageView) findViewById(R.id.open_wifi);
        //  close_wifi = (Button) findViewById(R.id.close_wifi);
        // scan_wifi = (Button) findViewById(R.id.scan_wifi);
        mlistView = (ListView) findViewById(R.id.wifi_list);
        // check_wifi.setOnClickListener(WiFiActivity.this);
        open_wifi.setOnClickListener(WiFiActivity.this);
        // close_wifi.setOnClickListener(WiFiActivity.this);
        //  scan_wifi.setOnClickListener(WiFiActivity.this);
        title_name = (TextView) findViewById(R.id.title_name);
    }

    @Override
    public void onClick(View v) {
        mWifiAdmin.startScan(WiFiActivity.this);
        mWifiList = mWifiAdmin.getWifiList();
        WiFiAdapter myAdapter = new WiFiAdapter(this, mWifiList);
        switch (v.getId()) {
      /*      case R.id.check_wifi:
                int i = mWifiAdmin.checkState(WiFiActivity.this);

                break;*/
            case R.id.open_wifi:
                mWifiAdmin.openWifi(WiFiActivity.this);
                mWifiAdmin.startScan(WiFiActivity.this);

                mWifiList = mWifiAdmin.getWifiList();
                if (mWifiList != null) {
                    mlistView.setAdapter(new WiFiAdapter(this, mWifiList));
                    new Utility().setListViewHeightBasedOnChildren(mlistView);
                }

                mWifiAdmin.getConnectWifiSsid();
                break;
 /*           case R.id.close_wifi:
                mWifiAdmin.closeWifi(WiFiActivity.this);
                mlistView.setAdapter(null);
                break;*/

 /*           case R.id.scan_wifi:
                mWifiAdmin.startScan(WiFiActivity.this);
                mWifiList = mWifiAdmin.getWifiList();
                if (mWifiList != null) {
                    mlistView.setAdapter(new WiFiAdapter(this, mWifiList));
                    new Utility().setListViewHeightBasedOnChildren(mlistView);
                }
                break;*/
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

            }
        }

    };
}
