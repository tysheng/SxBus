package tysheng.sxbus;

import android.app.Application;

import tysheng.sxbus.db.DaoCore;
import tysheng.sxbus.utils.rxfastcache.RxFastCache;

public class App extends Application {

    private static App instance;

    public static App get() {
        return instance;
    }

    @Override
    public void onCreate() {
        super.onCreate();
        instance = this;
        RxFastCache.init(this, 1024 * 50); //in bytes
        DaoCore.init();
        DaoCore.enableQueryBuilderLog();
    }


}
