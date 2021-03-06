package com.join.utils;

import android.content.Context;
import android.net.wifi.ScanResult;
import android.net.wifi.WifiConfiguration;
import android.net.wifi.WifiInfo;
import android.net.wifi.WifiManager;
import android.net.wifi.WifiManager.WifiLock;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

import static android.content.Context.WIFI_SERVICE;

public class WifiAdmin {
    private String TAG = "jjjWifiAdmin";
    // 管理WiFi的超类
    private WifiManager mWifiManager;
    // 可以从这个对象里取到WiFi的名称(SSID) MAC地址等;
    private WifiInfo mWifiInfo;
    // 扫描出的网络连接列表     
    private List<ScanResult> mWifiList;
    // 网络连接列表     
    private List<WifiConfiguration> mWifiConfiguration;
    // 作用一直保持WiFi的连接,
    WifiLock mWifiLock;


    /**
     * @return WiFi的名称(SSID) MAC地址等;
     */
    public String getConnectWifiSsid() {

        WifiInfo mWifiInfo = mWifiManager.getConnectionInfo();
        String ssid = mWifiInfo.getSSID();
        int length = ssid.length();
        String substring = ssid.substring(1, length - 1);
        return substring;
    }

    public WifiAdmin(Context context) {
        // 取得WifiManager对象     
        mWifiManager = (WifiManager) context.getSystemService(WIFI_SERVICE);
        // 取得WifiInfo对象     
        mWifiInfo = mWifiManager.getConnectionInfo();
    }

    /**
     * 打开WIFI
     */

    public void openWifi(Context context) {
        if (!mWifiManager.isWifiEnabled()) {
            mWifiManager.setWifiEnabled(true);
        } else if (mWifiManager.getWifiState() == 2) {
            Toast.makeText(context, "亲，Wifi正在开启，不用再开了", Toast.LENGTH_SHORT).show();
        } else {
            Toast.makeText(context, "亲，Wifi已经开启,不用再开了", Toast.LENGTH_SHORT).show();
        }
    }

    /**
     * 关闭wifi
     */

    public void closeWifi(Context context) {
        if (mWifiManager.isWifiEnabled()) {
            mWifiManager.setWifiEnabled(false);
        } else if (mWifiManager.getWifiState() == 1) {
            Toast.makeText(context, "亲，Wifi已经关闭，不用再关了", Toast.LENGTH_SHORT).show();
        } else if (mWifiManager.getWifiState() == 0) {
            Toast.makeText(context, "亲，Wifi正在关闭，不用再关了", Toast.LENGTH_SHORT).show();
        } else {
            Toast.makeText(context, "请重新关闭", Toast.LENGTH_SHORT).show();
        }
    }

    /**
     * 检查当前WIFI状态(是否开启)
     */

    public int checkState(Context context) {
        if (mWifiManager.getWifiState() == 0) {
            Toast.makeText(context, "Wifi正在关闭", Toast.LENGTH_SHORT).show();
            return 0;
        } else if (mWifiManager.getWifiState() == 1) {
            Toast.makeText(context, "Wifi已经关闭", Toast.LENGTH_SHORT).show();
            return 1;
        } else if (mWifiManager.getWifiState() == 2) {
            Toast.makeText(context, "Wifi正在开启", Toast.LENGTH_SHORT).show();
            return 2;
        } else if (mWifiManager.getWifiState() == 3) {
            Toast.makeText(context, "Wifi已经开启", Toast.LENGTH_SHORT).show();
            return 3;
        } else {
            Toast.makeText(context, "没有获取到WiFi状态", Toast.LENGTH_SHORT).show();
        }
        return -1;
    }

    /**
     * 锁定WifiLock
     */

    public void acquireWifiLock() {
        mWifiLock.acquire();
    }

    /**
     * 解锁WifiLock
     */

    public void releaseWifiLock() {
        // 判断时候锁定     
        if (mWifiLock.isHeld()) {
            mWifiLock.acquire();
        }
    }

    /**
     * 创建一个WifiLock
     */
    public void creatWifiLock() {
        mWifiLock = mWifiManager.createWifiLock("Test");
    }

    /**
     * 得到配置好的网络
     */

    public List<WifiConfiguration> getConfiguration() {

        return mWifiConfiguration;
    }

    /**
     * 指定配置好的网络进行连接
     */

    public void connectConfiguration(int index) {
        // 索引大于配置好的网络索引返回     
        if (index > mWifiConfiguration.size()) {
            return;
        }
        // 连接配置好的指定ID的网络     
        mWifiManager.enableNetwork(mWifiConfiguration.get(index).networkId,
                true);
    }

    /**
     * 开始扫描
     *
     * @param context
     */
    public void startScan(Context context) {
        mWifiManager.startScan();
        //得到扫描结果   
        List<ScanResult> results = mWifiManager.getScanResults();
        // 得到配置好的网络连接
        mWifiConfiguration = mWifiManager.getConfiguredNetworks();
        if (results == null) {
            if (mWifiManager.getWifiState() == 3) {
                Toast.makeText(context, "当前区域没有无线网络", Toast.LENGTH_SHORT).show();
            } else if (mWifiManager.getWifiState() == 2) {
                Toast.makeText(context, "wifi正在开启，请稍后扫描", Toast.LENGTH_SHORT).show();
            } else {
                Toast.makeText(context, "WiFi没有开启", Toast.LENGTH_SHORT).show();
            }
        } else {
            mWifiList = new ArrayList();

            for (ScanResult result : results) {
                //如果WiFi的名称等于空就跳过本次循环,  continue用来结束本次循环   break用来结束整个循环体
                if (result.SSID == null || result.SSID.length() == 0
                        || result.capabilities.contains("[IBSS]")) {
                    continue;
                }
                boolean found = false;
                for (ScanResult item : mWifiList) {
                    if (item.SSID.equals(result.SSID) && item.capabilities.equals(result.capabilities)) {
                        found = true;
                        break;
                    }
                }
                if (!found) {
                    String temp = getConnectWifiSsid();
                    String ssid = result.SSID;
                    //如果跟连接的WiFi名字一样就不加入mWifiList
                    if (ssid.equals(temp)) {
                        continue;
                    }
                    mWifiList.add(result);
                }
            }
        }
    }

    /**
     * 得到网络列表
     */

    public List<ScanResult> getWifiList() {
        return mWifiList;
    }

    /**
     * 查看扫描结果
     */

    public StringBuilder lookUpScan() {
        StringBuilder stringBuilder = new StringBuilder();
        for (int i = 0; i < mWifiList.size(); i++) {
            stringBuilder
                    .append("Index_" + new Integer(i + 1).toString() + ":");
            // 将ScanResult信息转换成一个字符串包     
            // 其中把包括：BSSID、SSID、capabilities、frequency、level    
            stringBuilder.append((mWifiList.get(i)).toString());
            stringBuilder.append("/n");
        }
        return stringBuilder;
    }

    /**
     * 得到MAC地址
     */

    public String getMacAddress() {
        return (mWifiInfo == null) ? "NULL" : mWifiInfo.getMacAddress();
    }

    /**
     * 得到接入点的BSSID
     */

    public String getBSSID() {
        return (mWifiInfo == null) ? "NULL" : mWifiInfo.getBSSID();
    }

    /**
     * 得到连接的IP
     */
    public int getIPAddress() {
        return (mWifiInfo == null) ? 0 : mWifiInfo.getIpAddress();
    }

    /**
     * 得到连接的ID
     */

    public int getNetworkId() {
        return (mWifiInfo == null) ? 0 : mWifiInfo.getNetworkId();
    }

    /**
     * 得到WifiInfo的所有信息包
     *
     * @return
     */
    public String getWifiInfo() {
        return (mWifiInfo == null) ? "NULL" : mWifiInfo.toString();
    }

    /**
     * 添加一个网络并连接
     */

    public void addNetwork(WifiConfiguration wcg) {
        int wcgID = mWifiManager.addNetwork(wcg);
        boolean b = mWifiManager.enableNetwork(wcgID, true);

    }

    /**
     * 断开指定ID的网络
     */

    public void disconnectWifi(int netId) {
        mWifiManager.disableNetwork(netId);
        mWifiManager.disconnect();
    }

    public void removeWifi(int netId) {
        disconnectWifi(netId);
        mWifiManager.removeNetwork(netId);
    }

    /**
     * 创建wifi热点的。
     */
    public WifiConfiguration CreateWifiInfo(String SSID, String Password, int Type) {
        WifiConfiguration config = new WifiConfiguration();
        config.allowedAuthAlgorithms.clear();
        config.allowedGroupCiphers.clear();
        config.allowedKeyManagement.clear();
        config.allowedPairwiseCiphers.clear();
        config.allowedProtocols.clear();
        config.SSID = "\"" + SSID + "\"";

        WifiConfiguration tempConfig = this.IsExsits(SSID);
        if (tempConfig != null) {
            mWifiManager.removeNetwork(tempConfig.networkId);
        }

        if (Type == 1) //WIFICIPHER_NOPASS
        {
            config.wepKeys[0] = "";
            config.allowedKeyManagement.set(WifiConfiguration.KeyMgmt.NONE);
            config.wepTxKeyIndex = 0;
        }
        if (Type == 2) //WIFICIPHER_WEP
        {
            config.hiddenSSID = true;
            config.wepKeys[0] = "\"" + Password + "\"";
            config.allowedAuthAlgorithms.set(WifiConfiguration.AuthAlgorithm.SHARED);
            config.allowedGroupCiphers.set(WifiConfiguration.GroupCipher.CCMP);
            config.allowedGroupCiphers.set(WifiConfiguration.GroupCipher.TKIP);
            config.allowedGroupCiphers.set(WifiConfiguration.GroupCipher.WEP40);
            config.allowedGroupCiphers.set(WifiConfiguration.GroupCipher.WEP104);
            config.allowedKeyManagement.set(WifiConfiguration.KeyMgmt.NONE);
            config.wepTxKeyIndex = 0;
        }
        if (Type == 3) //WIFICIPHER_WPA
        {
            config.preSharedKey = "\"" + Password + "\"";
            config.hiddenSSID = true;
            config.allowedAuthAlgorithms.set(WifiConfiguration.AuthAlgorithm.OPEN);
            config.allowedGroupCiphers.set(WifiConfiguration.GroupCipher.TKIP);
            config.allowedKeyManagement.set(WifiConfiguration.KeyMgmt.WPA_PSK);
            config.allowedPairwiseCiphers.set(WifiConfiguration.PairwiseCipher.TKIP);
            //config.allowedProtocols.set(WifiConfiguration.Protocol.WPA);
            config.allowedGroupCiphers.set(WifiConfiguration.GroupCipher.CCMP);
            config.allowedPairwiseCiphers.set(WifiConfiguration.PairwiseCipher.CCMP);
            config.status = WifiConfiguration.Status.ENABLED;
        }
        return config;
    }

    private WifiConfiguration IsExsits(String SSID) {
        List<WifiConfiguration> existingConfigs = mWifiManager.getConfiguredNetworks();
        for (WifiConfiguration existingConfig : existingConfigs) {
            if (existingConfig.SSID.equals("\"" + SSID + "\"")) {
                return existingConfig;
            }
        }
        return null;
    }
}
