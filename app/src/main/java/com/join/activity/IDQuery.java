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
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.id_query);
        ListView listView = (ListView) findViewById(R.id.lv_content);
        com.join.entity.IDQuery idQuery = new com.join.entity.IDQuery("1", "1", "1", "1", "1", "1", "1", "1", "1", "1");
        List<com.join.entity.IDQuery> list = new ArrayList<>();
        list.add(idQuery);
        IDQueryAdapter idQueryAdapter = new IDQueryAdapter(this, list);
        listView.setAdapter(idQueryAdapter);
    }
}
