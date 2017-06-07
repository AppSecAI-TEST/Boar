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
import com.join.UDisk.SaveToExcel;
import com.join.adapter.IDQueryAdapter;
import com.join.greenDaoUtils.OperationDao;
import com.join.greenDaoUtils.Storage;
import com.join.interface_callback.IDQueryKeyboard1;
import com.join.service.Humidity;
import com.join.utils.CustomToast;
import com.join.utils.Keyboard1;
import com.zhy.android.percent.support.PercentLinearLayout;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by join on 2017/5/15.
 */

public class IDQuery extends Activity implements View.OnClickListener, ServiceConnection {
    private String TAG = "jjjIDQuery";
    private TextView id_Gong_1;
    private ListView listView;
    private IDQueryAdapter idQueryAdapter;
    private ImageView icon_1;
    private Button input, bu_return;
    private PercentLinearLayout ll_Gong;
    private Keyboard1 keyboard1;
    private TextView humidity;
    private Humidity.HumidityBinder humidityBinder;
    private List<com.join.entity.IDQuery> list;
    private Intent intent;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.id_query);
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
        humidity = (TextView) findViewById(R.id.humidity);
        id_Gong_1 = (TextView) findViewById(R.id.id_Gong_1);
        ll_Gong = (PercentLinearLayout) findViewById(R.id.ll_Gong);
        ll_Gong.setOnClickListener(this);
        icon_1 = (ImageView) findViewById(R.id.icon_1);
        icon_1.setOnClickListener(this);
        input = (Button) findViewById(R.id.input);
        input.setOnClickListener(this);
        bu_return = (Button) findViewById(R.id.bu_return);
        bu_return.setOnClickListener(this);
        listView = (ListView) findViewById(R.id.lv_content);
        keyboard1 = new Keyboard1(IDQuery.this, id_Gong_1);

        keyboard1.setIdQueryKeyboard1(new IDQueryKeyboard1() {
            @Override
            public void start() {
                list = new ArrayList<>();
                String s = id_Gong_1.getText().toString();
                List<Storage> storages = OperationDao.queryLove(s);
                int size = storages.size();
                Log.e(TAG, "start:" + size);
                for (int i = 0; i < size; i++) {
                    Storage storage = storages.get(i);
                    String date = storage.getDate();
                    String time = storage.getTime();
                    String operator = storage.getOperator();
                    Long id = storage.getId();
                    String type = storage.getType();
                    String density = storage.getDensity();
                    String vitality = storage.getVitality();
                    String motilityRate = storage.getMotilityRate();
                    String result = storage.getResult();
                    String resultImage = "/storage/emulated/0/CreateCare" + File.separator + "demo.xls";
                    SaveToExcel saveToExcel = new SaveToExcel(resultImage);
                    saveToExcel.writeToExcel(new String[]{date, time, operator, "jjj", "jjj", date,
                            time, operator, "jjj", "jjj", date, time, operator, "jjj", "jjj", date});


                    com.join.entity.IDQuery idQuery = new com.join.entity.IDQuery(id, date, time, type, density, vitality, motilityRate, operator, result, "查看");
                    list.add(idQuery);

                }
                idQueryAdapter = new IDQueryAdapter(IDQuery.this, list);
                listView.setAdapter(idQueryAdapter);
            }
        });


    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.input:
                CustomToast.showToast(this, "正开发中......");
                break;
            case R.id.bu_return:
                finish();
                break;
            case R.id.icon_1:
                Intent intent = new Intent();
                intent.setAction("com.join.function");
                startActivity(intent);
                break;
            case R.id.ll_Gong:
                keyboard1.showWindow(ll_Gong);
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
