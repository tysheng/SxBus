package tysheng.sxbus;

import android.app.Application;

import tysheng.sxbus.utils.fastcache.FastCache;

public class App extends Application {

    private static App instance;

    public static App get() {
        return instance;
    }

    @Override
    public void onCreate() {
        super.onCreate();
        instance = this;

        FastCache.init(this, 1024 * 50); //in bytes
    }


}
