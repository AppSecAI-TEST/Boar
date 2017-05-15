package com.join.activity;

import android.app.Activity;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.widget.ListView;

import com.join.R;
import com.join.adapter.IDQueryAdapter;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by join on 2017/5/15.
 */

public class IDQuery extends Activity {
    private ListView listView;
    private IDQueryAdapter idQueryAdapter;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.id_query);
        init();
        listView.setAdapter(idQueryAdapter);
    }

    private void init() {
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
    }
}
