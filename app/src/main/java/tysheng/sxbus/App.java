package tysheng.sxbus;

import android.app.Application;

import com.baidu.mapapi.SDKInitializer;

import tysheng.sxbus.db.DaoCore;

public class App extends Application {

    private static App instance;

    public static App get() {
        return instance;
    }

    @Override
    public void onCreate() {
        super.onCreate();
        instance = this;
        DaoCore.init();
        if (BuildConfig.DEBUG) {
            DaoCore.enableQueryBuilderLog();
        }
        // 初始化百度地图
        SDKInitializer.initialize(getApplicationContext());
    }


}
