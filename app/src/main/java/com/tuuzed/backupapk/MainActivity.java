package com.tuuzed.backupapk;

import android.annotation.SuppressLint;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Environment;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.Toast;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.tuuzed.backupapk.entity.ApkEntity;
import com.tuuzed.backupapk.util.ApkUtils;
import com.tuuzed.backupapk.widget.DividerItemDecoration;
import com.tuuzed.common.recyclerview.RecyclerViewAdapter;

import java.io.File;
import java.util.ArrayList;
import java.util.List;


public class MainActivity extends AppCompatActivity {
    private ProgressBar mProgressBar;
    private RecyclerView mRecyclerView;
    private RecyclerViewAdapter mAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        initViews();
        initData();
    }

    @SuppressLint("StaticFieldLeak")
    private void initData() {
        new AsyncTask<Void, Integer, List<ApkEntity>>() {
            @Override
            protected void onPostExecute(List<ApkEntity> list) {
                super.onPostExecute(list);
                mProgressBar.setVisibility(View.GONE);
                mAdapter.getItems().clear();
                mAdapter.getItems().addAll(list);
                mAdapter.notifyDataSetChanged();
            }

            @Override
            protected List<ApkEntity> doInBackground(Void... params) {
                return ApkUtils.getAllApps(MainActivity.this);
            }
        }.execute();
    }

    private void initViews() {
        mProgressBar = (ProgressBar) findViewById(R.id.progressBar);
        mRecyclerView = (RecyclerView) findViewById(R.id.recyclerView);
        mAdapter = new RecyclerViewAdapter();
        mAdapter.bind(ApkEntity.class, new ApkEntityItemViewBinder(mAdapter));
        mRecyclerView.setLayoutManager(new LinearLayoutManager(this));
        mRecyclerView.addItemDecoration(new DividerItemDecoration(this, DividerItemDecoration.VERTICAL_LIST));
        mRecyclerView.setAdapter(mAdapter);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        switch (id) {
            case R.id.action_backup:
                backup(Environment.getExternalStorageDirectory().getPath() + File.separator + "backup_apk");
                break;
            default:
                break;
        }
        return super.onOptionsItemSelected(item);
    }

    private void backup(final String path) {
        //创建文件夹
        File file = new File(path);
        if (!file.exists()) file.mkdirs();
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        View view = LayoutInflater.from(this).inflate(R.layout.dialog_layout, null);
        final AlertDialog dialog = builder.setView(view).create();
        dialog.show();
        List<ApkEntity> list = new ArrayList<>();
        for (Object obj : mAdapter.getItems()) {
            if (obj instanceof ApkEntity) {
                list.add((ApkEntity) obj);
            }
        }
        final int count = list.size();
        @SuppressLint("StaticFieldLeak")
        AsyncTask<List<ApkEntity>, Integer, Void> task = new AsyncTask<List<ApkEntity>, Integer, Void>() {
            @Override
            protected void onProgressUpdate(Integer... values) {
                super.onProgressUpdate(values);
                if (values[0] == count) {
                    dialog.hide();
                    Toast.makeText(MainActivity.this, "备份完成！", Toast.LENGTH_LONG).show();
                }
            }

            @SafeVarargs
            @Override
            protected final Void doInBackground(List<ApkEntity>... params) {
                List<ApkEntity> list = params[0];
                for (int i = 0; i < list.size(); i++) {
                    ApkEntity entity = list.get(i);
                    if (entity.isChecked()) {
                        ApkUtils.backupApp(entity.getPackageName(), path, entity.getApkName() + ".apk");
                    }
                    publishProgress(i + 1);
                }
                return null;
            }
        };
        task.execute(list);
    }
}
