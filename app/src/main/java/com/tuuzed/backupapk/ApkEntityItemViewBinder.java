package com.tuuzed.backupapk;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.tuuzed.backupapk.entity.ApkEntity;
import com.tuuzed.common.recyclerview.ItemViewBinder;
import com.tuuzed.common.recyclerview.RecyclerViewAdapter;

/**
 * @author TuuZed
 */
public class ApkEntityItemViewBinder implements ItemViewBinder<ApkEntity, ApkEntityItemViewBinder.ViewHolder> {

    private RecyclerViewAdapter mAdapter;

    public ApkEntityItemViewBinder(RecyclerViewAdapter adapter) {
        mAdapter = adapter;
    }

    @Override
    public void onBindViewHolder(ViewHolder viewHolder, final ApkEntity apkEntity, final int i) {
        viewHolder.checkbox.setChecked(apkEntity.isChecked());
        viewHolder.imageIcon.setImageDrawable(apkEntity.getIcon());
        viewHolder.textName.setText(apkEntity.getApkName());
        viewHolder.textPackage.setText(apkEntity.getPackageName());
        View.OnClickListener listener = new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                apkEntity.setChecked(!apkEntity.isChecked());
                mAdapter.notifyItemChanged(i);
            }
        };
        viewHolder.checkbox.setOnClickListener(listener);
        viewHolder.itemView.setOnClickListener(listener);
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        View view = inflater.inflate(R.layout.item, parent, false);
        return new ViewHolder(view);
    }


    static class ViewHolder extends RecyclerView.ViewHolder {
        CheckBox checkbox;
        ImageView imageIcon;
        TextView textName;
        TextView textPackage;


        ViewHolder(View itemView) {
            super(itemView);
            checkbox = (CheckBox) itemView.findViewById(R.id.checkbox);
            imageIcon = (ImageView) itemView.findViewById(R.id.image_icon);
            textName = (TextView) itemView.findViewById(R.id.text_name);
            textPackage = (TextView) itemView.findViewById(R.id.text_package);
        }
    }
}
