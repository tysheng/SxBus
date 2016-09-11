package tysheng.sxbus;

import android.app.Application;

import tysheng.sxbus.utils.fastcache.FastCache;

public class App extends Application {

    private static App instance;


    @Override
    public void onCreate() {
        super.onCreate();
        instance = this;
        try {
            FastCache.init(this, 1024 * 50); //in bytes
        } catch (Exception e) {
            //failure
        }
    }

    public static App get() {
        return instance;
    }



}
