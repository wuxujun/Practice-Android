package com.xujun.app.practice;

import android.content.ComponentName;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;

import com.lidroid.xutils.ViewUtils;

/**
 * Created by xujunwu on 15/7/31.
 */
public class StartActivity extends BaseActivity{

    @Override
    protected void onCreate(Bundle bundle){
        super.onCreate(bundle);
        setContentView(R.layout.activity_start);

        createShortcut();
        ViewUtils.inject(this);
    }

    @Override
    protected void onResume(){
        super.onResume();

        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                final Intent intent = new Intent(StartActivity.this, TabActivity.class);
                startActivity(intent);
                finish();
            }
        }, 1);
    }

    /**
     * 创建桌面快捷方式
     */
    private void createShortcut() {
        SharedPreferences setting = getSharedPreferences("silent.preferences", 0);
        // 判断是否第一次启动应用程序（默认为true）
        boolean firstStart = setting.getBoolean("FIRST_START", true);
        // 第一次启动时创建桌面快捷方式
        if (firstStart) {
            Intent shortcut = new Intent("com.android.launcher.action.INSTALL_SHORTCUT");
            // 快捷方式的名称
            shortcut.putExtra(Intent.EXTRA_SHORTCUT_NAME, getString(R.string.app_name));
            // 不允许重复创建
            shortcut.putExtra("duplicate", false);
            // 指定快捷方式的启动对象
            ComponentName comp = new ComponentName(this.getPackageName(), "." + this.getLocalClassName());
            shortcut.putExtra(Intent.EXTRA_SHORTCUT_INTENT, new Intent(Intent.ACTION_MAIN).setComponent(comp));
            // 快捷方式的图标
            Intent.ShortcutIconResource iconRes = Intent.ShortcutIconResource.fromContext(this, R.drawable.ic_launcher);
            shortcut.putExtra(Intent.EXTRA_SHORTCUT_ICON_RESOURCE, iconRes);
            // 发出广播
            sendBroadcast(shortcut);
            // 将第一次启动的标识设置为false
            SharedPreferences.Editor editor = setting.edit();
            editor.putBoolean("FIRST_START", false);
            // 提交设置
            editor.commit();
        }
    }
}
