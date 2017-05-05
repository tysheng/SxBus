package tysheng.sxbus.di.module;

import dagger.Module;
import dagger.Provides;
import tysheng.sxbus.adapter.RunningAdapter;
import tysheng.sxbus.di.PerPresenter;
import tysheng.sxbus.model.impl.RunningModelImpl;
import tysheng.sxbus.presenter.inter.RunningPresenter;

/**
 * Created by tysheng
 * Date: 2017/5/5 09:28.
 * Email: tyshengsx@gmail.com
 */
@Module
public class RunningModule {
    private RunningPresenter mRunningPresenter;

    public RunningModule(RunningPresenter runningPresenter) {
        mRunningPresenter = runningPresenter;
    }

    @PerPresenter
    @Provides
    public RunningPresenter provideRunningPresenter() {
        return mRunningPresenter;
    }

    @PerPresenter
    @Provides
    public RunningAdapter provideAdapter() {
        return new RunningAdapter();
    }

    @PerPresenter
    @Provides
    public RunningModelImpl provideRunningModelImpl(RunningPresenter presenter) {
        return new RunningModelImpl(presenter);
    }
}
