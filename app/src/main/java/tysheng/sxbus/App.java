package tysheng.sxbus;

import android.app.Application;

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
        DaoCore.enableQueryBuilderLog();
    }


}
