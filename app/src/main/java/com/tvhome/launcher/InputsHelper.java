package com.tvhome.launcher;

import android.app.AlertDialog;
import android.content.Context;
import android.content.Intent;
import android.media.tv.TvInputInfo;
import android.media.tv.TvInputManager;

import java.util.ArrayList;
import java.util.List;

public class InputsHelper {

    public static void show(MainActivity ctx) {
        try {
            TvInputManager mgr = (TvInputManager) ctx.getSystemService(Context.TV_INPUT_SERVICE);
            if (mgr == null) { alert(ctx, "TV Input Manager недоступен"); return; }

            List<TvInputInfo> all = mgr.getTvInputList();
            List<String> labels = new ArrayList<>();
            List<TvInputInfo> valid = new ArrayList<>();
            for (TvInputInfo info : all) {
                if (info.isHidden(ctx)) continue;
                CharSequence lbl = info.loadLabel(ctx);
                if (lbl == null) continue;
                labels.add(lbl.toString());
                valid.add(info);
            }
            if (labels.isEmpty()) { alert(ctx, "Входы не найдены"); return; }

            new AlertDialog.Builder(ctx)
                    .setTitle("Входы")
                    .setItems(labels.toArray(new String[0]), (d, which) -> switchTo(ctx, valid.get(which)))
                    .setNegativeButton("Отмена", null)
                    .show();
        } catch (Exception e) {
            alert(ctx, "Ошибка: " + e.getMessage());
        }
    }

    private static void switchTo(MainActivity ctx, TvInputInfo info) {
        try {
            String pkg = info.getServiceInfo().packageName;
            Intent launch = ctx.getPackageManager().getLeanbackLaunchIntentForPackage(pkg);
            if (launch == null) launch = ctx.getPackageManager().getLaunchIntentForPackage(pkg);
            if (launch != null) { ctx.startActivity(launch); return; }
        } catch (Exception ignored) { }
        alert(ctx, "Не удалось переключить вход. Откройте меню входов штатного ТВ-приложения.");
    }

    private static void alert(Context c, String msg) {
        new AlertDialog.Builder(c).setMessage(msg).setPositiveButton("OK", null).show();
    }
}
