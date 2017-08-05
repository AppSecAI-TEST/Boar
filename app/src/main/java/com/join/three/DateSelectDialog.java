package com.join.three;

import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
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


/**
 * 更改封面对话框
 *
 * @author ywl
 */
public class DateSelectDialog extends Dialog implements View.OnClickListener {

    private WheelView wvYear;
    private WheelView wvMonth;
    private WheelView wvDay;

    private View lyChangeAddress; //three_date_setting 的布局
    private View lyChangeAddressChild; //three_date_setting 子布局
    private Button btnSure;       //确认的文本框  可以改成button
    private Button btnCancel;     //取消的文本框  可以改成button

    private Context context;
    private JSONObject mJsonObj;     //得到year.json对象
    private String[] mYearDatas;//年数据
    // 存储省对应的所有市
    private Map<String, String[]> mMonthDatasMap = new HashMap<String, String[]>();
    // 存储市对应的所有区
    private Map<String, String[]> mDayDataMap = new HashMap<String, String[]>();


    private ArrayList<String> arrYear = new ArrayList<String>(); //到达年集合
    private ArrayList<String> arrMonth = new ArrayList<String>();//到达月
    private ArrayList<String> arrDay = new ArrayList<String>();//到达天
    private AddressTextAdapter yearAdapter;  //年adapter
    private AddressTextAdapter monthAdapter;     //月
    private AddressTextAdapter dayAdapter;     //天

    private String strYear = "2017";  //开始年
    private String strMonth = "05";      //开始月
    private String strDay = "15";      //开始天
    private OnAddressCListener onAddressCListener;//地址接听器

    private int maxsize = 24;
    private int minsize = 14;

    public DateSelectDialog(Context context) {
        super(context, R.style.ShareDialog);
        this.context = context;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.three_date_setting);
        //初始化数据
        initView();
        //拿到josn数据
        initJsonData();
        //解析数据
        initDatas();
        initProvinces();
        //初始化显示的位置
        yearAdapter = new AddressTextAdapter(context, arrYear, getProvinceItem(strYear), maxsize, minsize);
        wvYear.setVisibleItems(5);
        wvYear.setViewAdapter(yearAdapter);
        wvYear.setCurrentItem(getProvinceItem(strYear));

        initCitys(mMonthDatasMap.get(strYear));
        monthAdapter = new AddressTextAdapter(context, arrMonth, getCityItem(strMonth), maxsize, minsize);
        wvMonth.setVisibleItems(5);
        wvMonth.setViewAdapter(monthAdapter);
        wvMonth.setCurrentItem(getCityItem(strMonth));

        initAreas(mDayDataMap.get(strMonth));
        dayAdapter = new AddressTextAdapter(context, arrDay, getAreasItem(strDay), maxsize, minsize);
        wvDay.setVisibleItems(5);
        wvDay.setViewAdapter(dayAdapter);
        wvDay.setCurrentItem(getAreasItem(strDay));
/**
 * 设置省份改变的监听
 */
        wvYear.addChangingListener(new OnWheelChangedListener() {

            @Override
            public void onChanged(WheelView wheel, int oldValue, int newValue) {
                String currentText = (String) yearAdapter.getItemText(wheel.getCurrentItem());
                strYear = currentText;
                setTextviewSize(currentText, yearAdapter);
                String[] citys = mMonthDatasMap.get(currentText);
                initCitys(citys);
                monthAdapter = new AddressTextAdapter(context, arrMonth, 0, maxsize, minsize);
                wvMonth.setVisibleItems(5);
                wvMonth.setViewAdapter(monthAdapter);
                wvMonth.setCurrentItem(0);
            }
        });

        wvYear.addScrollingListener(new OnWheelScrollListener() {

            @Override
            public void onScrollingStarted(WheelView wheel) {

            }

            @Override
            public void onScrollingFinished(WheelView wheel) {
                String currentText = (String) yearAdapter.getItemText(wheel.getCurrentItem());
                setTextviewSize(currentText, yearAdapter);
            }
        });

        wvMonth.addChangingListener(new OnWheelChangedListener() {

            @Override
            public void onChanged(WheelView wheel, int oldValue, int newValue) {
                String currentText = (String) monthAdapter.getItemText(wheel.getCurrentItem());
                strMonth = currentText;
                setTextviewSize(currentText, monthAdapter);
                String[] areas = mDayDataMap.get(currentText);
                initAreas(areas);
                dayAdapter = new AddressTextAdapter(context, arrDay, 0, maxsize, minsize);
                wvDay.setVisibleItems(5);
                wvDay.setViewAdapter(dayAdapter);
                wvDay.setCurrentItem(0);
            }
        });

        wvMonth.addScrollingListener(new OnWheelScrollListener() {

            @Override
            public void onScrollingStarted(WheelView wheel) {

            }

            @Override
            public void onScrollingFinished(WheelView wheel) {
                String currentText = (String) monthAdapter.getItemText(wheel.getCurrentItem());
                setTextviewSize(currentText, monthAdapter);
            }
        });

        wvDay.addChangingListener(new OnWheelChangedListener() {
            @Override
            public void onChanged(WheelView wheel, int oldValue, int newValue) {
                String currentText = (String) dayAdapter.getItemText(wheel.getCurrentItem());
                strDay = currentText;
                setTextviewSize(currentText, dayAdapter);

            }
        });
        wvDay.addScrollingListener(new OnWheelScrollListener() {
            @Override
            public void onScrollingStarted(WheelView wheel) {

            }

            @Override
            public void onScrollingFinished(WheelView wheel) {
                String currentText = (String) dayAdapter.getItemText(wheel.getCurrentItem());
                setTextviewSize(currentText, dayAdapter);
            }
        });
    }

    private void initView() {
        wvYear = (WheelView) findViewById(R.id.wv_address_province);
        wvMonth = (WheelView) findViewById(R.id.wv_address_city);
        wvDay = (WheelView) findViewById(R.id.wv_address_area);

        lyChangeAddress = findViewById(R.id.ly_myinfo_changeaddress);
        lyChangeAddressChild = findViewById(R.id.ly_myinfo_changeaddress_child);
        btnSure = (Button) findViewById(R.id.btn_myinfo_sure);
        btnCancel = (Button) findViewById(R.id.btn_myinfo_cancel);

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
                textvew.setTextSize(24);
            } else {
                textvew.setTextSize(14);
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
                onAddressCListener.onClick(strYear, strMonth, strDay);
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
        public void onClick(String province, String city, String area);
    }

    /**
     * 从json文件中读取地址数据
     */
    private void initJsonData() {
        try {
            StringBuffer sb = new StringBuffer();
            //打开json文件
            InputStream is = context.getAssets().open("year.json");
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
            JSONArray jsonArray = mJsonObj.getJSONArray("yearJson");
            //省会数组
            mYearDatas = new String[jsonArray.length()];
            for (int i = 0; i < jsonArray.length(); i++) {
                //拿到数组的每个对象
                JSONObject jsonP = jsonArray.getJSONObject(i);
                //拿到p对象的值
                String province = jsonP.getString("y");
                //p对象的值付给省会的数组
                mYearDatas[i] = province;
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
                    JSONArray jsonAreas = null;
                    try {
                        /**
                         * Throws JSONException if the mapping doesn't exist or
                         * is not a JSONArray.
                         */
                        jsonAreas = jsonCity.getJSONArray("a");
                    } catch (Exception e) {
                        continue;
                    }
                    //乡村数组
                    String[] mAreasDatas = new String[jsonAreas.length()];
                    for (int k = 0; k < jsonAreas.length(); k++) {
                        String area = jsonAreas.getJSONObject(k).getString("s");
                        mAreasDatas[k] = area;
                    }
                    // 存储市对应的所有第三级区域
                    mDayDataMap.put(city, mAreasDatas);
                }
                mMonthDatasMap.put(province, mCitiesDatas);
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
        int length = mYearDatas.length;
        for (int i = 0; i < length; i++) {
            arrYear.add(mYearDatas[i]);
        }
    }

    /**
     * 根据年，生成该年的所有月
     *
     * @param citys
     */
    public void initCitys(String[] citys) {
        if (citys != null) {
            arrMonth.clear();
            int length = citys.length;
            for (int i = 0; i < length; i++) {
                arrMonth.add(citys[i]);
            }
        } else {
            String[] city = mMonthDatasMap.get("2017");
            arrMonth.clear();
            int length = city.length;
            for (int i = 0; i < length; i++) {
                arrMonth.add(city[i]);
            }
        }
        if (arrMonth != null && arrMonth.size() > 0
                && !arrMonth.contains(strMonth)) {
            strMonth = arrMonth.get(5);
        }
    }

    /**
     * 根据月，生成该月的所有天
     *
     * @param citys
     */
    public void initAreas(String[] citys) {
        if (citys != null) {
            arrDay.clear();
            int length = citys.length;
            for (int i = 0; i < length; i++) {
                arrDay.add(citys[i]);
            }
        } else {
            String[] city = mDayDataMap.get("06");
            arrDay.clear();
            int length = city.length;
            for (int i = 0; i < length; i++) {
                arrDay.add(city[i]);
            }
        }
        if (arrDay != null && arrDay.size() > 0
                && !arrDay.contains(strDay)) {
            strDay = arrDay.get(15);
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
            this.strYear = province;
        }
        if (city != null && city.length() > 0) {
            this.strMonth = city;
        }
    }

    /**
     * 返回省会索引，没有就返回默认“四川”
     *
     * @param province
     * @return
     */
    public int getProvinceItem(String province) {
        int size = arrYear.size();
        int provinceIndex = 0;
        boolean noprovince = true;
        for (int i = 0; i < size; i++) {
            if (province.equals(arrYear.get(i))) {
                noprovince = false;
                return provinceIndex;
            } else {
                provinceIndex++;
            }
        }
        if (noprovince) {
            strYear = "2017";
            return 2;
        }
        return provinceIndex;
    }

    /**
     * 得到城市索引，没有返回默认“成都”
     *
     * @param city
     * @return
     */
    public int getCityItem(String city) {
        int size = arrMonth.size();
        int cityIndex = 0;
        boolean nocity = true;
        for (int i = 0; i < size; i++) {
            System.out.println(arrMonth.get(i));
            if (city.equals(arrMonth.get(i))) {
                nocity = false;
                return cityIndex;
            } else {
                cityIndex++;
            }
        }
        if (nocity) {
            strMonth = "06";
            return 0;
        }
        return cityIndex;
    }

    /**
     * 得到城市索引，没有返回默认“成都”
     *
     * @param city
     * @return
     */
    public int getAreasItem(String city) {
        int size = arrDay.size();
        int cityIndex = 0;
        boolean nocity = true;
        for (int i = 0; i < size; i++) {
            System.out.println(arrDay.get(i));
            if (city.equals(arrDay.get(i))) {
                nocity = false;
                return cityIndex;
            } else {
                cityIndex++;
            }
        }
        if (nocity) {
            strDay = "15";
            return 0;
        }
        return cityIndex;
    }
}