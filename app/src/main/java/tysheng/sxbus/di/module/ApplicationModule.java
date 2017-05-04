package tysheng.sxbus.di.module;

import javax.inject.Singleton;

import dagger.Module;
import dagger.Provides;
import tysheng.sxbus.App;

/**
 * Created by tysheng
 * Date: 2017/5/4 16:40.
 * Email: tyshengsx@gmail.com
 */
@Module
public class ApplicationModule {
    private App mApp;

    public ApplicationModule(App app) {
        mApp = app;
    }

    @Singleton
    @Provides
    public App provideApp() {
        return mApp;
    }
}
