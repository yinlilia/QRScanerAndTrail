package com.samonxu.qrcode.demo.view;

import android.app.ActionBar;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.Toolbar;
import com.samonxu.qrcode.demo.Adapter.SettingsItemAdapter;
import com.samonxu.qrcode.demo.R;
import com.samonxu.qrcode.demo.util.SettingsItem;
import java.util.ArrayList;
import java.util.List;
import butterknife.BindView;
import butterknife.ButterKnife;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
/**
 * Created by yinlili on 2017/4/12.
 */

public class SettingActivity extends Activity{
    private static final int ID_HISTORY=0;
    @BindView(R.id.toolbar) Toolbar toolbar;
    @BindView(R.id.setting_recycler_view) RecyclerView recyclerView;
    private List<SettingsItem> settingsItemList = new ArrayList<>();
    private SettingsItemAdapter adapter;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_setting);
        ButterKnife.bind(this);
        initToolbar();
        initView();
        initData();
        initEvent();
    }

    private void initView() {
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setItemAnimator(new DefaultItemAnimator());

    }

    private void initData() {
        settingsItemList.add(new SettingsItem(ID_HISTORY, getString(R.string.history), "", "",
                false, false, SettingsItem.TYPE_SWITCH, false));
        adapter = new SettingsItemAdapter(settingsItemList);
        recyclerView.setAdapter(adapter);
    }

    private void initToolbar() {
        setActionBar(toolbar);
        ActionBar actionBar = getActionBar();
        if (actionBar != null) {
            actionBar.setTitle(getString(R.string.history));
            actionBar.setDisplayHomeAsUpEnabled(true);
        }
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onBackPressed();
            }
        });
    }
    private void initEvent(){

    }


    public static void toSettingActivity(Context context) {
        Intent intent = new Intent();
        intent.setClass(context, SettingActivity.class);
        context.startActivity(intent);
    }

}
