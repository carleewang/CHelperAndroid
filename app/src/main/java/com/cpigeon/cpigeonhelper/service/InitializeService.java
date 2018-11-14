package com.cpigeon.cpigeonhelper.service;

import android.app.IntentService;
import android.content.Context;
import android.content.Intent;
import android.support.annotation.Nullable;

import com.facebook.stetho.Stetho;
import com.tencent.bugly.Bugly;

/**
 * 初始化App所需要的服务
 * Created by Administrator on 2017/7/17.
 */

public class InitializeService extends IntentService {

    private static final String ACTION_INIT_WHEN_APP_CREATE = "InitializeService";

    public InitializeService() {
        super("InitializeService");
    }

    @Override
    protected void onHandleIntent(@Nullable Intent intent) {
        if (intent != null) {
            final String action = intent.getAction();
            if (ACTION_INIT_WHEN_APP_CREATE.equals(action)) {
                performInit();
            }
        }
    }

    public static void start(Context context) {
        Intent intent = new Intent(context, InitializeService.class);
        intent.setAction(ACTION_INIT_WHEN_APP_CREATE);
        context.startService(intent);
    }

    private void performInit() {
        //初始化Bugly
        initBugly();
        //初始化Steho调试工具
        initSteho();
    }

    private void initSteho() {
        Stetho.initialize(
                Stetho.newInitializerBuilder(this.getApplicationContext())
                        .enableDumpapp(Stetho.defaultDumperPluginsProvider(this.getApplicationContext()))
                        .enableWebKitInspector(Stetho.defaultInspectorModulesProvider(this.getApplicationContext()))
                        .build());
    }

    private void initBugly() {
        //腾讯bugly 建议在测试阶段建议设置成true，发布时设置为false
//        CrashReport.initCrashReport(this.getApplicationContext(), "7d06f65182", BuildConfig.DEBUG);

        Bugly.init(getApplicationContext(), "7d06f65182", false);
    }
}
