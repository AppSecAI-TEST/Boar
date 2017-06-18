package com.join.activity;

import android.app.Activity;
import android.content.ComponentName;
import android.content.Intent;
import android.content.ServiceConnection;
import android.os.Bundle;
import android.os.IBinder;
import android.support.annotation.Nullable;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import com.join.R;
import com.join.adapter.IDQueryAdapter;
import com.join.greenDaoUtils.OperationDao;
import com.join.greenDaoUtils.Storage;
import com.join.service.Humidity;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by join on 2017/6/13.
 */

public class DateQuery extends Activity implements View.OnClickListener,ServiceConnection {
    private String TAG = "jjjDateQuery";
    private ListView listView;
    private Intent intent;
    private IDQueryAdapter idQueryAdapter;
    private ImageView icon_1;
    private Button bu_return;
    private TextView humidity;
    private Humidity.HumidityBinder humidityBinder;
    private List<com.join.entity.IDQuery> list;
    private int numberTag, numberTag1;

    private TextView get_data_1, get_data_2;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.date_query);
        String numberTag = getIntent().getStringExtra("KeyboardDateQueryData1");
        String numberTag1 = getIntent().getStringExtra("KeyboardDateQueryData2");
        this.numberTag = Integer.valueOf(numberTag);
        this.numberTag1 = Integer.valueOf(numberTag1);
        init();

    }

    private void init() {
        intent = new Intent();
        get_data_1 = (TextView) findViewById(R.id.get_data_1);
        get_data_2 = (TextView) findViewById(R.id.get_data_2);
        humidity = (TextView) findViewById(R.id.humidity);
        icon_1 = (ImageView) findViewById(R.id.icon_1);
        icon_1.setOnClickListener(this);
        bu_return = (Button) findViewById(R.id.bu_return);
        bu_return.setOnClickListener(this);
        listView = (ListView) findViewById(R.id.lv_content);
        get_data_1.setText(numberTag+"");
        get_data_2.setText(numberTag1+"");
        list = new ArrayList<>();
        List<Storage> storages = OperationDao.queryLoveDate(numberTag, numberTag1);
        int size = storages.size();
        Log.e(TAG, "start:" + size);
        for (int i = 0; i < size; i++) {
            Storage storage = storages.get(i);
            int checkoutDateInt = storage.getCheckoutDate();

            String originalData = String.valueOf(checkoutDateInt);
            String checkoutDate = originalData.substring(0, 4) + "-" + originalData.substring(4, 6) + "-" + originalData.substring(6, 8);

            String checkoutTime = storage.getCheckoutTime();
            String operator = storage.getOperator();
            Long id = storage.getId();
            String type = storage.getType();
            String density = storage.getDensity();
            String vitality = storage.getVitality();
            String motilityRate = storage.getMotilityRate();
            String result = storage.getResult();

            com.join.entity.IDQuery idQuery = new com.join.entity.IDQuery(id, checkoutDate, checkoutTime, type, density, vitality, motilityRate, operator, result, "查看");
            list.add(idQuery);
        }
        idQueryAdapter = new IDQueryAdapter(DateQuery.this, list);
        listView.setAdapter(idQueryAdapter);

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                com.join.entity.IDQuery idQuery = list.get(position);

                long idTag = idQuery.getId();
                Log.e(TAG, "onItemClick: " + idTag);
                String type = idQuery.getType();
                if (type.equals("精液原液")) {

                    Bundle bundle = new Bundle();
                    bundle.putLong("IDQUeryData", idTag);
                    intent.putExtras(bundle);
                    intent.setAction("com.join.stostedetection2");
                    intent.addFlags(2);
                    startActivity(intent);
                } else if (type.equals("稀释精液")) {
                    Bundle bundle = new Bundle();
                    bundle.putLong("IDQUeryData", idTag);
                    intent.putExtras(bundle);
                    intent.setAction("com.join.stostedetection22");
                    intent.addFlags(2);
                    startActivity(intent);
                }
            }
        });
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {

            case R.id.bu_return:
                finish();
                break;
            case R.id.icon_1:
                intent = new Intent();
                intent.setAction("com.join.function");
                startActivity(intent);
                break;
        }
    }
    @Override
    protected void onResume() {
        super.onResume();
        Intent intentHumidity = new Intent(this, Humidity.class);
        bindService(intentHumidity, this, BIND_AUTO_CREATE);
    }
    @Override
    protected void onPause() {
        super.onPause();
        unbindService(this);
    }
    @Override
    public void onServiceConnected(ComponentName name, IBinder service) {
        humidityBinder = (Humidity.HumidityBinder) service;
        Humidity humidityClass = humidityBinder.getHumidity();
        humidityClass.setHumidityCallback(new Humidity.HumidityCallback() {
            @Override
            public void onHumidityChange(final String data) {
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        humidity.setText(data);

                    }
                });
            }
        });
    }

    @Override
    public void onServiceDisconnected(ComponentName name) {

    }
}
