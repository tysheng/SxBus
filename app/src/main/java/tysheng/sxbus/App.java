package tysheng.sxbus;

import android.app.Application;

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
        mApplicationComponent = DaggerApplicationComponent.builder()
                .applicationModule(new ApplicationModule(this))
                .daoModule(new DaoModule())
                .netModule(new NetModule())
                .build();
        mApplicationComponent.inject(this);
        if (BuildConfig.DEBUG) {
            enableQueryBuilderLog();
        }
    }

    public ApplicationComponent getApplicationComponent() {
        return mApplicationComponent;
    }

}
