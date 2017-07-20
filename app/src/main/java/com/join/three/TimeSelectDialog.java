package com.join.three;

import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.join.R;
import com.join.three.AdapterMy.AbstractWheelTextAdapter;
import com.join.three.views.OnWheelChangedListener;
import com.join.three.views.OnWheelScrollListener;
import com.join.three.views.WheelView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;


public class TimeSelectDialog extends Dialog implements View.OnClickListener {


    private WheelView wvHour;
    private WheelView wvMinute;

    private View lyChangeAddress;
    private View lyChangeAddressChild;
    private TextView btnSure;
    private TextView btnCancel;

    private Context context;
    private JSONObject mJsonObj;
    private String[] mHourDatas;//小时数据
    // 存储小时对应的所有分钟
    private Map<String, String[]> mMinuteDatasMap = new HashMap<String, String[]>();


    private ArrayList<String> arrHour = new ArrayList<String>(); //到达小时集合
    private ArrayList<String> arrMinute = new ArrayList<String>();//到达分钟

    private AddressTextAdapter hourAdapter;  //小时adapter
    private AddressTextAdapter minuteAdapter;     //分


    private String strHour = "12";  //开始小时
    private String strMinute = "09";      //开始分钟


    private OnAddressCListener onAddressCListener;//地址接听器

    private int maxsize = 30;//字体的大小
    private int minsize = 18;

    public TimeSelectDialog(Context context) {
        super(context, R.style.ShareDialog);
        this.context = context;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.three_time_setting);
        //初始化数据
        initView();
        //拿到josn数据
        initJsonData();
        //解析数据
        initDatas();
        initProvinces();
        //初始化显示的位置
        hourAdapter = new AddressTextAdapter(context, arrHour, getProvinceItem(strHour), maxsize, minsize);
        wvHour.setVisibleItems(5);
        wvHour.setViewAdapter(hourAdapter);
        wvHour.setCurrentItem(getProvinceItem(strHour));

        initCitys(mMinuteDatasMap.get(strHour));
        minuteAdapter = new AddressTextAdapter(context, arrMinute, getCityItem(strMinute), maxsize, minsize);
        wvMinute.setVisibleItems(5);
        wvMinute.setViewAdapter(minuteAdapter);
        wvMinute.setCurrentItem(getCityItem(strMinute));

/**
 * 设置省份改变的监听
 */
        wvHour.addChangingListener(new OnWheelChangedListener() {

            @Override
            public void onChanged(WheelView wheel, int oldValue, int newValue) {
                String currentText = (String) hourAdapter.getItemText(wheel.getCurrentItem());
                strHour = currentText;
                setTextviewSize(currentText, hourAdapter);
                String[] citys = mMinuteDatasMap.get(currentText);
                initCitys(citys);
                minuteAdapter = new AddressTextAdapter(context, arrMinute, 0, maxsize, minsize);
                wvMinute.setVisibleItems(5);
                wvMinute.setViewAdapter(minuteAdapter);
                wvMinute.setCurrentItem(0);//设置显示的item
               /* String minutecurrentText = (String) minuteAdapter.getItemText(9);
                strMinute = minutecurrentText;
                setTextviewSize(minutecurrentText, minuteAdapter);*/

            }
        });

        wvHour.addScrollingListener(new OnWheelScrollListener() {

            @Override
            public void onScrollingStarted(WheelView wheel) {

            }

            @Override
            public void onScrollingFinished(WheelView wheel) {
                String currentText = (String) hourAdapter.getItemText(wheel.getCurrentItem());
                setTextviewSize(currentText, hourAdapter);
            }
        });

        wvMinute.addChangingListener(new OnWheelChangedListener() {

            @Override
            public void onChanged(WheelView wheel, int oldValue, int newValue) {
                String currentText = (String) minuteAdapter.getItemText(wheel.getCurrentItem());
                strMinute = currentText;
                setTextviewSize(currentText, minuteAdapter);

            }
        });

        wvMinute.addScrollingListener(new OnWheelScrollListener() {

            @Override
            public void onScrollingStarted(WheelView wheel) {

            }

            @Override
            public void onScrollingFinished(WheelView wheel) {
                String currentText = (String) minuteAdapter.getItemText(wheel.getCurrentItem());
                setTextviewSize(currentText, minuteAdapter);
            }
        });


    }

    private void initView() {
        wvHour = (WheelView) findViewById(R.id.wv_address_province);
        wvMinute = (WheelView) findViewById(R.id.wv_address_city);


        lyChangeAddress = findViewById(R.id.ly_myinfo_changeaddress);
        lyChangeAddressChild = findViewById(R.id.ly_myinfo_changeaddress_child);
        btnSure = (TextView) findViewById(R.id.btn_myinfo_sure);
        btnCancel = (TextView) findViewById(R.id.btn_myinfo_cancel);

        lyChangeAddress.setOnClickListener(this);
        lyChangeAddressChild.setOnClickListener(this);
        btnSure.setOnClickListener(this);
        btnCancel.setOnClickListener(this);
    }

    private class AddressTextAdapter extends AbstractWheelTextAdapter {
        ArrayList<String> list;

        protected AddressTextAdapter(Context context, ArrayList<String> list, int currentItem, int maxsize, int minsize) {
            super(context, R.layout.item_birth_year, NO_RESOURCE, currentItem, maxsize, minsize);
            this.list = list;
            setItemTextResource(R.id.tempValue);
        }

        @Override
        public View getItem(int index, View cachedView, ViewGroup parent) {
            View view = super.getItem(index, cachedView, parent);
            return view;
        }

        @Override
        public int getItemsCount() {
            return list.size();
        }

        @Override
        protected CharSequence getItemText(int index) {
            return list.get(index) + "";
        }
    }

    /**
     * 设置字体大小
     *
     * @param curriteItemText
     * @param adapter
     */
    public void setTextviewSize(String curriteItemText, AddressTextAdapter adapter) {
        ArrayList<View> arrayList = adapter.getTestViews();
        int size = arrayList.size();
        String currentText;
        for (int i = 0; i < size; i++) {
            TextView textvew = (TextView) arrayList.get(i);
            currentText = textvew.getText().toString();
            if (curriteItemText.equals(currentText)) {
                textvew.setTextSize(30);
            } else {
                textvew.setTextSize(20);
            }
        }
    }

    public void setAddresskListener(OnAddressCListener onAddressCListener) {
        this.onAddressCListener = onAddressCListener;
    }

    @Override
    public void onClick(View v) {

        if (v == btnSure) {
            if (onAddressCListener != null) {
                onAddressCListener.onClick(strHour, strMinute);
            }
        } else if (v == btnCancel) {

        } else if (v == lyChangeAddressChild) {
            return;
        } else {
            dismiss();
        }
        dismiss();
    }

    /**
     * 回调接口
     *
     * @author Administrator
     */
    public interface OnAddressCListener {
        public void onClick(String province, String city);
    }

    /**
     * 从json文件中读取地址数据
     */
    private void initJsonData() {
        try {
            StringBuffer sb = new StringBuffer();
            //打开json文件
            InputStream is = context.getAssets().open("time.json");
            int len = -1;
            byte[] buf = new byte[1024];
            while ((len = is.read(buf)) != -1) {
                //追加字符串
                sb.append(new String(buf, 0, len, "utf-8"));
            }
            is.close();
            //
            mJsonObj = new JSONObject(sb.toString());
        } catch (IOException e) {
            e.printStackTrace();
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    /**
     * 解析数据
     */
    private void initDatas() {
        try {
            //拿到json跟数组
            JSONArray jsonArray = mJsonObj.getJSONArray("timeJson");
            //省会数组
            mHourDatas = new String[jsonArray.length()];
            for (int i = 0; i < jsonArray.length(); i++) {
                //拿到数组的每个对象
                JSONObject jsonP = jsonArray.getJSONObject(i);
                //拿到p对象的值
                String province = jsonP.getString("h");
                //p对象的值付给省会的数组
                mHourDatas[i] = province;
                JSONArray jsonCs = null;
                try {
                    /**
                     * Throws JSONException if the mapping doesn't exist or is
                     * not a JSONArray.
                     */
                    //拿到 c 数组里的每个值到城市的json数组
                    jsonCs = jsonP.getJSONArray("c");
                } catch (Exception e1) {
                    continue;
                }
                //城市数组
                String[] mCitiesDatas = new String[jsonCs.length()];
                for (int j = 0; j < jsonCs.length(); j++) {
                    //拿到jsonCs的每个对象
                    JSONObject jsonCity = jsonCs.getJSONObject(j);
                    //拿到 n 标签的值
                    String city = jsonCity.getString("n");
                    //把每个城市赋给城市的数组
                    mCitiesDatas[j] = city;


                }
                mMinuteDatasMap.put(province, mCitiesDatas);
            }

        } catch (JSONException e) {
            e.printStackTrace();
        }
        mJsonObj = null;
    }

    /**
     * 初始化省会
     */
    public void initProvinces() {
        int length = mHourDatas.length;
        for (int i = 0; i < length; i++) {
            arrHour.add(mHourDatas[i]);
        }
    }

    /**
     * 根据省会，生成该省会的所有城市
     *
     * @param citys
     */
    public void initCitys(String[] citys) {
        if (citys != null) {
            arrMinute.clear();
            int length = citys.length;
            for (int i = 0; i < length; i++) {
                arrMinute.add(citys[i]);
            }
        } else {
            String[] city = mMinuteDatasMap.get("02");
            arrMinute.clear();
            int length = city.length;
            for (int i = 0; i < length; i++) {
                arrMinute.add(city[i]);
            }
        }
        if (arrMinute != null && arrMinute.size() > 0
                && !arrMinute.contains(strMinute)) {
            strMinute = arrMinute.get(30);
        }
    }


    /**
     * 初始化地点
     *
     * @param province
     * @param city
     */
    public void setAddress(String province, String city) {
        if (province != null && province.length() > 0) {
            this.strHour = province;
        }
        if (city != null && city.length() > 0) {
            this.strMinute = city;
        }
    }

    /**
     * 返回小时索引，没有就返回默认“四川”
     *
     * @param province
     * @return
     */
    public int getProvinceItem(String province) {
        int size = arrHour.size();
        int provinceIndex = 0;
        boolean noprovince = true;
        for (int i = 0; i < size; i++) {
            if (province.equals(arrHour.get(i))) {
                noprovince = false;
                return provinceIndex;
            } else {
                provinceIndex++;
            }
        }
        if (noprovince) {
            strHour = "12";
            return 2;
        }
        return provinceIndex;
    }

    /**
     * 得到分索引，没有返回默认“成都”
     *
     * @param city
     * @return
     */
    public int getCityItem(String city) {
        int size = arrMinute.size();
        int cityIndex = 0;
        boolean nocity = true;
        for (int i = 0; i < size; i++) {

            if (city.equals(arrMinute.get(i))) {
                nocity = false;
                return cityIndex;
            } else {
                cityIndex++;
            }
        }
        if (nocity) {
            strMinute = "06";
            return 0;
        }
        return cityIndex;
    }


}
