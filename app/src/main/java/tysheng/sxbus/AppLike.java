package tysheng.sxbus;

import android.annotation.SuppressLint;
import android.annotation.TargetApi;
import android.app.Application;
import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.support.multidex.MultiDex;

import com.baidu.mapapi.SDKInitializer;
import com.tencent.bugly.Bugly;
import com.tencent.bugly.beta.Beta;
import com.tencent.bugly.crashreport.CrashReport;
import com.tencent.tinker.loader.app.DefaultApplicationLike;

import org.greenrobot.greendao.query.QueryBuilder;

import tysheng.sxbus.di.component.ApplicationComponent;
import tysheng.sxbus.di.component.DaggerApplicationComponent;
import tysheng.sxbus.di.module.ApplicationModule;
import tysheng.sxbus.di.module.DaoModule;
import tysheng.sxbus.di.module.NetModule;

/**
 * Created by tysheng
 * Date: 2017/5/7 21:53.
 * Email: tyshengsx@gmail.com
 */

public class AppLike extends DefaultApplicationLike {


    @SuppressLint("StaticFieldLeak")
    private static AppLike mContext;
    private ApplicationComponent mApplicationComponent;

    public AppLike(Application application, int tinkerFlags,
                   boolean tinkerLoadVerifyFlag, long applicationStartElapsedTime,
                   long applicationStartMillisTime, Intent tinkerResultIntent) {
        super(application, tinkerFlags, tinkerLoadVerifyFlag, applicationStartElapsedTime,
                applicationStartMillisTime, tinkerResultIntent);
    }

    public static AppLike getAppLike() {
        return mContext;
    }

    public static Application get() {
        return getAppLike().getApplication();
    }

    @Override
    public void onCreate() {
        super.onCreate();
        // 设置是否开启热更新能力，默认为true
        Beta.enableHotfix = true;
        // 设置是否自动下载补丁，默认为true
        Beta.canAutoDownloadPatch = true;
        // 设置是否自动合成补丁，默认为true
        Beta.canAutoPatch = true;
        // 设置是否提示用户重启，默认为false
        Beta.canNotifyUserRestart = false;
        // 补丁回调接口

        CrashReport.UserStrategy strategy = new CrashReport.UserStrategy(getApplication());
        strategy.setAppChannel("coolapk");
        strategy.setAppPackageName(BuildConfig.APPLICATION_ID);
        if (BuildConfig.DEBUG) {
            strategy.setAppVersion(BuildConfig.VERSION_NAME + ".dev");
        } else {
            strategy.setAppVersion(BuildConfig.VERSION_NAME);
        }
        // Bugly sdk
        Bugly.init(getApplication(), "66733354ef", BuildConfig.DEBUG, strategy);
        // 设置开发设备，默认为false，上传补丁如果下发范围指定为“开发设备”，需要调用此接口来标识开发设备
        Bugly.setIsDevelopmentDevice(getApplication(), BuildConfig.DEBUG);
        if (BuildConfig.DEBUG) {
            enableQueryBuilderLog();
        }
        // 初始化百度地图
        SDKInitializer.initialize(getApplication());
    }

    private void enableQueryBuilderLog() {
        QueryBuilder.LOG_SQL = true;
        QueryBuilder.LOG_VALUES = true;
    }

    public ApplicationComponent getApplicationComponent() {
        return mApplicationComponent;
    }

    @TargetApi(Build.VERSION_CODES.ICE_CREAM_SANDWICH)
    @Override
    public void onBaseContextAttached(Context base) {
        super.onBaseContextAttached(base);
        // you must install multiDex whatever tinker is installed!
        MultiDex.install(base);
        mContext = this;
        mApplicationComponent = DaggerApplicationComponent.builder()
                .applicationModule(new ApplicationModule(getApplication()))
                .daoModule(new DaoModule())
                .netModule(new NetModule())
                .build();
//        mApplicationComponent.inject(this);
        // TODO: 安装tinker
        Beta.installTinker(this);
    }
}
