package tysheng.sxbus.di.module;

import android.app.Application;

import javax.inject.Singleton;

import dagger.Module;
import dagger.Provides;

/**
 * Created by tysheng
 * Date: 2017/5/4 16:40.
 * Email: tyshengsx@gmail.com
 */
@Module
public class ApplicationModule {
    private Application mApp;

    public ApplicationModule(Application app) {
        mApp = app;
    }

    @Singleton
    @Provides
    public Application provideApp() {
        return mApp;
    }
}
