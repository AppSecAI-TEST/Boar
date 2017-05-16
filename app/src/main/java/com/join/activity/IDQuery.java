package com.join.activity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.join.R;
import com.join.adapter.IDQueryAdapter;
import com.join.utils.Keyboard1;
import com.zhy.android.percent.support.PercentLinearLayout;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by join on 2017/5/15.
 */

public class IDQuery extends Activity implements View.OnClickListener {
    private TextView id_Gong_1;
    private ListView listView;
    private IDQueryAdapter idQueryAdapter;
    private ImageView icon_1;
    private Button input, bu_return;
    private PercentLinearLayout ll_Gong;
    private Keyboard1 keyboard1;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.id_query);
        init();
        listView.setAdapter(idQueryAdapter);
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Intent intent = new Intent();
                intent.setAction("com.join.stostedetection2");
                startActivity(intent);
            }
        });
    }

    private void init() {
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
        com.join.entity.IDQuery idQuery = new com.join.entity.IDQuery("2017/02/12", "12:30", "精液原液", "300", "0.8", "0.9", "001", "合格", "查看");
        List<com.join.entity.IDQuery> list = new ArrayList<>();
        list.add(idQuery);
        list.add(idQuery);
        list.add(idQuery);
        list.add(idQuery);
        list.add(idQuery);
        list.add(idQuery);
        idQueryAdapter = new IDQueryAdapter(this, list);
        keyboard1 = new Keyboard1(IDQuery.this, id_Gong_1);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.input:
                Toast.makeText(this, "正在完善中.........", Toast.LENGTH_LONG).show();
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
}
