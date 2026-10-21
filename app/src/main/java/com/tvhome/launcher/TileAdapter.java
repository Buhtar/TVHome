package com.tvhome.launcher;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

public class TileAdapter extends RecyclerView.Adapter<TileAdapter.VH> {

    public interface Listener {
        void onClick(AppInfo info);
        void onFocused(View tile, AppInfo info, int pos);
    }

    private final MainActivity ctx;
    private final List<AppInfo> items;
    private final Listener listener;

    public TileAdapter(MainActivity ctx, List<AppInfo> items, Listener l) {
        this.ctx = ctx;
        this.items = items;
        this.listener = l;
    }

    @NonNull
    @Override
    public VH onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_tile, parent, false);
        return new VH(v);
    }

    @Override
    public void onBindViewHolder(@NonNull VH h, int position) {
        AppInfo info = items.get(position);
        h.icon.setImageDrawable(info.icon);
        h.label.setText(info.label);

        h.itemView.setOnClickListener(v -> listener.onClick(info));
        
        h.itemView.setOnFocusChangeListener((v, hasFocus) -> {
            // Увеличение иконки
            v.animate().scaleX(hasFocus ? 1.3f : 1f).scaleY(hasFocus ? 1.3f : 1f)
                    .setDuration(200).start();
            
            // Показываем/скрываем текст
            h.label.setAlpha(hasFocus ? 1f : 0f);
            
            // Полностью прозрачный фон (ничего не применяем)
            v.setBackground(null);

            if (hasFocus) {
                v.bringToFront(); // Чтобы увеличенная иконка не обрезалась соседями
                listener.onFocused(v, info, position);
            }
        });
    }

    @Override
    public int getItemCount() { return items.size(); }

    static class VH extends RecyclerView.ViewHolder {
        ImageView icon; TextView label;
        VH(View v) { 
            super(v); 
            icon = v.findViewById(R.id.tile_icon); 
            label = v.findViewById(R.id.tile_label); 
        }
    }
}