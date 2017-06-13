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
 * 按公猪ID查询
 */

public class IDQuery extends Activity implements View.OnClickListener, ServiceConnection {
    private String TAG = "jjjIDQuery";
    private ListView listView;
    private IDQueryAdapter idQueryAdapter;
    private ImageView icon_1;
    private Button bu_return;
    private TextView humidity;
    private Humidity.HumidityBinder humidityBinder;
    private List<com.join.entity.IDQuery> list;
    private Intent intent;
    private String numberTag;
    private TextView get_data;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.id_query);
        numberTag = getIntent().getStringExtra("KeyboardIDQueryData");
        Log.e(TAG, "onCreate: " + numberTag);
        init();
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


    private void init() {

        intent = new Intent();
        get_data = (TextView) findViewById(R.id.get_data);
        humidity = (TextView) findViewById(R.id.humidity);
        icon_1 = (ImageView) findViewById(R.id.icon_1);
        icon_1.setOnClickListener(this);
        bu_return = (Button) findViewById(R.id.bu_return);
        bu_return.setOnClickListener(this);
        listView = (ListView) findViewById(R.id.lv_content);
        get_data.setText(numberTag);

        list = new ArrayList<>();
        List<Storage> storages = OperationDao.queryLove(numberTag);
        int size = storages.size();
        Log.e(TAG, "start:" + size);
        for (int i = 0; i < size; i++) {
            Storage storage = storages.get(i);
            int checkoutDateInt = storage.getCheckoutDate();
            Log.e(TAG, "init: " + checkoutDateInt + "new");
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
        idQueryAdapter = new IDQueryAdapter(IDQuery.this, list);
        listView.setAdapter(idQueryAdapter);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {

            case R.id.bu_return:
                finish();
                break;
            case R.id.icon_1:
                Intent intent = new Intent();
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
            public void onHumidityChange(final int data) {
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        humidity.setText("" + data);

                    }
                });
            }
        });
    }

    @Override
    public void onServiceDisconnected(ComponentName name) {

    }
}
