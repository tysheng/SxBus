package tysheng.sxbus.di.module;

import android.support.v4.app.FragmentManager;

import dagger.Module;
import dagger.Provides;
import tysheng.sxbus.di.PerActivity;
import tysheng.sxbus.ui.inter.MainView;

/**
 * Created by tysheng
 * Date: 2017/5/17 09:44.
 * Email: tyshengsx@gmail.com
 */
@Module
public class MainModule {
    private MainView mainView;
    private FragmentManager mFragmentManager;

    public MainModule(MainView mainView, FragmentManager fragmentManager) {
        this.mainView = mainView;
        mFragmentManager = fragmentManager;
    }

    @Provides
    @PerActivity
    MainView provideMainView() {
        return mainView;
    }

    @Provides
    @PerActivity
    FragmentManager provideFragmentManager() {
        return mFragmentManager;
    }
}
