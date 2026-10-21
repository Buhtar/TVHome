package com.tvhome.launcher;

import android.app.Activity;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.graphics.Typeface;
import android.net.wifi.WifiInfo;
import android.net.wifi.WifiManager;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.text.TextUtils;
import android.view.View;
import android.view.WindowManager;
import android.widget.FrameLayout;
import android.widget.TextView;

import androidx.core.content.res.ResourcesCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.List;
import java.util.Locale;

public class MainActivity extends Activity implements TileAdapter.Listener {

    private FrameLayout root;
    private RecyclerView gridRecycler;
    private TextView clock, date, greeting;
    private TextView pillInputs, pillWifi, pillSettings;

    private final List<AppInfo> allApps = new ArrayList<>();
    private final List<View> topPills = new ArrayList<>();
    private final Handler handler = new Handler(Looper.getMainLooper());

    private BroadcastReceiver packageReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            loadApps();
            if (gridRecycler.getAdapter() != null) gridRecycler.getAdapter().notifyDataSetChanged();
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_main);
        getWindow().setStatusBarColor(0);
        getWindow().setNavigationBarColor(0);

        root = findViewById(R.id.root);
        clock = findViewById(R.id.clock);
        date = findViewById(R.id.date);
        greeting = findViewById(R.id.greeting);
        gridRecycler = findViewById(R.id.grid);
        pillInputs = findViewById(R.id.pill_inputs);
        pillWifi = findViewById(R.id.pill_wifi);
        pillSettings = findViewById(R.id.pill_settings);

        applyFonts();
        loadApps();
        setupGrid();
        setupPills();
        startClock();

        gridRecycler.post(() -> {
            if (gridRecycler.getChildAt(0) != null) gridRecycler.getChildAt(0).requestFocus();
        });
    }

    @Override
    protected void onResume() {
        super.onResume();
        IntentFilter filter = new IntentFilter();
        filter.addAction(Intent.ACTION_PACKAGE_ADDED);
        filter.addAction(Intent.ACTION_PACKAGE_REMOVED);
        filter.addAction(Intent.ACTION_PACKAGE_REPLACED);
        filter.addDataScheme("package");
        registerReceiver(packageReceiver, filter);

        loadApps();
        if (gridRecycler.getAdapter() != null) gridRecycler.getAdapter().notifyDataSetChanged();
    }

    @Override
    protected void onPause() {
        super.onPause();
        try { unregisterReceiver(packageReceiver); } catch (IllegalArgumentException e) { }
    }

    private void applyFonts() {
        Typeface outfit = loadFont("outfit_light");
        if (outfit == null) outfit = loadFont("outfit_regular");
        if (outfit != null) {
            clock.setTypeface(outfit);
            date.setTypeface(outfit);
            greeting.setTypeface(outfit);
        }
    }

    private Typeface loadFont(String name) {
        int id = getResources().getIdentifier(name, "font", getPackageName());
        if (id == 0) return null;
        try { return ResourcesCompat.getFont(this, id); } catch (Exception e) { return null; }
    }

    private void loadApps() {
        android.content.pm.PackageManager pm = getPackageManager();
        List<android.content.pm.ResolveInfo> all = new ArrayList<>();
        Intent lean = new Intent(Intent.ACTION_MAIN).addCategory(Intent.CATEGORY_LEANBACK_LAUNCHER);
        all.addAll(pm.queryIntentActivities(lean, 0));
        Intent reg = new Intent(Intent.ACTION_MAIN).addCategory(Intent.CATEGORY_LAUNCHER);
        for (android.content.pm.ResolveInfo r : pm.queryIntentActivities(reg, 0)) {
            boolean dup = false;
            for (android.content.pm.ResolveInfo e : all) if (e.activityInfo.packageName.equals(r.activityInfo.packageName)) { dup = true; break; }
            if (!dup) all.add(r);
        }

        allApps.clear();
        for (android.content.pm.ResolveInfo r : all) {
            String pkg = r.activityInfo.packageName.toLowerCase(Locale.ROOT);
            String label = r.loadLabel(pm).toString();
            
            if (pkg.equals(getPackageName())) continue;
            if (pkg.equals("com.example.user.myapplication")) continue;
            if (pkg.contains("settings") || pkg.contains("documentsui") || pkg.contains("shell") || pkg.contains("packageinstaller") || pkg.contains("provider")) continue;
            if (label == null || label.trim().isEmpty() || label.equalsIgnoreCase("Puzzle") || label.equalsIgnoreCase("Files")) continue;
            if (r.activityInfo.getIconResource() == 0) continue;
            
            allApps.add(new AppInfo(r.activityInfo.packageName, label, r.loadIcon(pm)));
        }
        Collections.sort(allApps, (a, b) -> a.label.compareToIgnoreCase(b.label));
    }

    private void setupGrid() {
        // Горизонтальная лента
        gridRecycler.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false));
        gridRecycler.setAdapter(new TileAdapter(this, allApps, this));
    }

    private void setupPills() {
        topPills.clear();
        topPills.add(pillInputs);
        topPills.add(pillWifi);
        topPills.add(pillSettings);

        for (View p : topPills) {
            p.setFocusable(true);
            p.setFocusableInTouchMode(false);
            p.setOnFocusChangeListener((v, has) -> {
                v.setAlpha(has ? 1f : 0.7f);
                v.animate().scaleX(has ? 1.06f : 1f).scaleY(has ? 1.06f : 1f).setDuration(150).start();
            });
            p.setOnKeyListener((v, keyCode, event) -> {
                if (event.getAction() != android.view.KeyEvent.ACTION_DOWN) return false;
                if (keyCode == android.view.KeyEvent.KEYCODE_DPAD_DOWN) {
                    View d = gridRecycler.getChildAt(0);
                    if (d != null) { d.requestFocus(); return true; }
                }
                return false;
            });
        }

        pillInputs.setOnClickListener(v -> InputsHelper.show(this));
        pillWifi.setOnClickListener(v -> startActivity(new Intent(android.provider.Settings.ACTION_WIFI_SETTINGS)));
        pillSettings.setOnClickListener(v -> startActivity(new Intent(android.provider.Settings.ACTION_SETTINGS)));

        refreshWifiName();
    }

    private void refreshWifiName() {
        try {
            WifiManager wm = (WifiManager) getApplicationContext().getSystemService(WIFI_SERVICE);
            WifiInfo info = wm.getConnectionInfo();
            String ssid = info != null ? info.getSSID() : null;
            if (ssid != null && ssid.startsWith("\"") && ssid.endsWith("\"")) ssid = ssid.substring(1, ssid.length() - 1);
            if (!TextUtils.isEmpty(ssid) && !ssid.equals("<unknown ssid>")) pillWifi.setText(ssid);
        } catch (Exception ignored) { }
    }

    private void startClock() {
        Runnable tick = new Runnable() {
            @Override public void run() { updateClock(); handler.postDelayed(this, 30000); }
        };
        handler.post(tick);
    }

    private void updateClock() {
        Calendar c = Calendar.getInstance();
        SimpleDateFormat tf = new SimpleDateFormat("HH:mm", new Locale("ru"));
        SimpleDateFormat df = new SimpleDateFormat("EEEE, d MMMM", new Locale("ru"));
        clock.setText(tf.format(c.getTime()));
        String d = df.format(c.getTime());
        if (!d.isEmpty()) d = Character.toUpperCase(d.charAt(0)) + d.substring(1);
        date.setText(d);
        int h = c.get(Calendar.HOUR_OF_DAY);
        String g = (h >= 5 && h < 12) ? "Доброе утро"
                : (h >= 12 && h < 18) ? "Добрый день"
                : (h >= 18 && h < 23) ? "Добрый вечер"
                : "Доброй ночи";
        greeting.setText(g);
    }

    @Override public void onClick(AppInfo info) {
        try {
            Intent i = getPackageManager().getLeanbackLaunchIntentForPackage(info.packageName);
            if (i == null) i = getPackageManager().getLaunchIntentForPackage(info.packageName);
            if (i != null) startActivity(i);
        } catch (Exception ignored) { }
    }

    @Override public void onFocused(View tile, AppInfo info, int pos) {
        // При фокусе ничего дополнительно делать не нужно
    }

    @Override protected void onDestroy() { super.onDestroy(); handler.removeCallbacksAndMessages(null); }
}