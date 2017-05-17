package tysheng.sxbus.di.module;

import dagger.Module;
import dagger.Provides;
import tysheng.sxbus.di.PerPresenter;
import tysheng.sxbus.presenter.impl.MorePresenterImpl;
import tysheng.sxbus.ui.inter.MoreView;

/**
 * Created by tysheng
 * Date: 2017/5/17 13:10.
 * Email: tyshengsx@gmail.com
 */
@Module
public class MoreModule {
    private MoreView mMoreView;

    public MoreModule(MoreView moreView) {
        mMoreView = moreView;
    }

    @Provides
    @PerPresenter
    MoreView provideMoreView() {
        return mMoreView;
    }

    @Provides
    @PerPresenter
    public MorePresenterImpl provideMorePresenterImpl(MoreView moreView) {
        return new MorePresenterImpl(moreView);
    }

}
