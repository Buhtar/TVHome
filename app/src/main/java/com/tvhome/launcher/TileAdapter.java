package com.tvhome.launcher;

import android.view.KeyEvent;
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
        void onFocused(View tile, AppInfo info, int pos, boolean isDock);
        void onUpFromGridFirstRow();
        void onDownFromGridLastRow();
        void onUpFromDock();
        void onDownFromDock();
    }

    private final MainActivity ctx;
    private final List<AppInfo> items;
    private final boolean isDock;
    private final Listener listener;
    private final int spanCount = 5;

    public TileAdapter(MainActivity ctx, List<AppInfo> items, boolean isDock, Listener l) {
        this.ctx = ctx;
        this.items = items;
        this.isDock = isDock;
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
        
        if (info.label == null || info.label.trim().isEmpty()) {
            h.label.setVisibility(View.GONE);
        } else {
            h.label.setVisibility(View.VISIBLE);
            h.label.setText(info.label);
        }

        h.itemView.setOnClickListener(v -> listener.onClick(info));
        
        h.itemView.setOnFocusChangeListener((v, hasFocus) -> {
            h.label.setAlpha(hasFocus ? 1f : 0f);
            v.animate().scaleX(hasFocus ? 1.08f : 1f).scaleY(hasFocus ? 1.08f : 1f)
                    .setDuration(180).start();
            
            // Рамка теперь применяется к контейнеру иконки
            if (hasFocus) {
                h.iconContainer.setBackgroundResource(R.drawable.highlight_bg);
            } else {
                h.iconContainer.setBackground(null);
            }

            if (hasFocus) listener.onFocused(v, info, position, isDock);
        });
        
        h.itemView.setOnKeyListener((v, keyCode, event) -> {
            if (event.getAction() != KeyEvent.ACTION_DOWN) return false;

            if (isDock) {
                if (keyCode == KeyEvent.KEYCODE_DPAD_UP)    { listener.onUpFromDock();   return true; }
                if (keyCode == KeyEvent.KEYCODE_DPAD_DOWN)  { listener.onDownFromDock(); return true; }
            } else {
                int lastRowStart = ((items.size() - 1) / spanCount) * spanCount;
                if (keyCode == KeyEvent.KEYCODE_DPAD_UP && position < spanCount) {
                    listener.onUpFromGridFirstRow(); return true;
                }
                if (keyCode == KeyEvent.KEYCODE_DPAD_DOWN && position >= lastRowStart) {
                    listener.onDownFromGridLastRow(); return true;
                }
            }
            return false;
        });
    }

    @Override
    public int getItemCount() { return items.size(); }

    static class VH extends RecyclerView.ViewHolder {
        View iconContainer;
        ImageView icon; TextView label;
        VH(View v) { 
            super(v); 
            iconContainer = v.findViewById(R.id.icon_container);
            icon = v.findViewById(R.id.tile_icon); 
            label = v.findViewById(R.id.tile_label); 
        }
    }
}