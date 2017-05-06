package tysheng.sxbus;

import android.app.Application;

import com.baidu.mapapi.SDKInitializer;
import com.tencent.bugly.crashreport.CrashReport;

import org.greenrobot.greendao.query.QueryBuilder;

import tysheng.sxbus.di.component.ApplicationComponent;
import tysheng.sxbus.di.component.DaggerApplicationComponent;
import tysheng.sxbus.di.module.ApplicationModule;
import tysheng.sxbus.di.module.DaoModule;
import tysheng.sxbus.di.module.NetModule;

public class App extends Application {

    private static App instance;
    private ApplicationComponent mApplicationComponent;

    public static App get() {
        return instance;
    }

    public static void enableQueryBuilderLog() {
        QueryBuilder.LOG_SQL = true;
        QueryBuilder.LOG_VALUES = true;
    }

    @Override
    public void onCreate() {
        super.onCreate();
        instance = this;
        CrashReport.UserStrategy strategy = null;
        if (BuildConfig.DEBUG) {
            strategy = new CrashReport.UserStrategy(getApplicationContext());
            strategy.setAppChannel("coolapk");
            strategy.setAppPackageName(BuildConfig.APPLICATION_ID);
            strategy.setAppVersion(BuildConfig.VERSION_NAME + ".dev");
        }
        // Bugly sdk
        CrashReport.initCrashReport(getApplicationContext(), "66733354ef", BuildConfig.DEBUG, strategy);
        CrashReport.setIsDevelopmentDevice(getApplicationContext(), BuildConfig.DEBUG);
        mApplicationComponent = DaggerApplicationComponent.builder()
                .applicationModule(new ApplicationModule(this))
                .daoModule(new DaoModule())
                .netModule(new NetModule())
                .build();
        mApplicationComponent.inject(this);
        if (BuildConfig.DEBUG) {
            enableQueryBuilderLog();
        }
        // 初始化百度地图
        SDKInitializer.initialize(getApplicationContext());
    }

    public ApplicationComponent getApplicationComponent() {
        return mApplicationComponent;
    }

}
