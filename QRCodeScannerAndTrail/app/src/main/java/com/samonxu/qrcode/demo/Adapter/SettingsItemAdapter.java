package com.samonxu.qrcode.demo.Adapter;

import android.support.v7.widget.RecyclerView;
import android.view.ViewGroup;

import com.samonxu.qrcode.demo.util.SettingsItem;

import java.util.List;

/**
 * Created by yinlili on 2017/4/12.
 */

public class SettingsItemAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder>{
    private List<SettingsItem> settingItemList;

    public SettingsItemAdapter(List<SettingsItem> settingItemList) {
        this.settingItemList = settingItemList;
    }
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return null;
    }
    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {

    }
    @Override
    public int getItemCount() {
        return settingItemList.size();
    }

}
