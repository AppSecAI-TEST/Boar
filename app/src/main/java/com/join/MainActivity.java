package com.join;

import android.Manifest;
import android.app.Activity;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.util.Log;
import android.view.View;
import android.widget.Button;

import com.join.greenDaoUtils.OperationDao;
import com.join.greenDaoUtils.Storage;

import java.util.List;

public class MainActivity extends Activity implements View.OnClickListener {
    private Button add, delete, update, query;
    Storage storage;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(MainActivity.this, new String[]{ Manifest.permission.WRITE_EXTERNAL_STORAGE,
                    Manifest.permission.READ_EXTERNAL_STORAGE,Manifest.permission.ACCESS_CHECKIN_PROPERTIES}, 1);
        }
        init();
    }

    private void init() {
        add = (Button) findViewById(R.id.bu_add);
        delete = (Button) findViewById(R.id.bu_delete);
        update = (Button) findViewById(R.id.bu_update);
        query = (Button) findViewById(R.id.bu_query);
        add.setOnClickListener(this);
        delete.setOnClickListener(this);
        update.setOnClickListener(this);
        query.setOnClickListener(this);
        storage = new Storage(0L, 001, "2017/05/", null, "jj", null, null, null, null, null, null, null, null);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.bu_add:
                OperationDao.addData(storage);
                break;
            case R.id.bu_delete:

                break;
            case R.id.bu_update:

                break;
            case R.id.bu_query:
                List<Storage> jj = OperationDao.queryLove("jj");
                Storage storage = jj.get(0);
                Long id = storage.getId();
                Log.e("jjj", "" + id);
                String type = storage.getType();
                Log.e("jjj",type);
                break;
        }
    }
}
