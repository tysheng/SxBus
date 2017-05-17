package tysheng.sxbus.di.module;

import dagger.Module;
import dagger.Provides;
import tysheng.sxbus.di.PerPresenter;
import tysheng.sxbus.model.impl.BikeModelImpl;
import tysheng.sxbus.presenter.inter.BikePresenter;

/**
 * Created by tysheng
 * Date: 2017/5/17 12:49.
 * Email: tyshengsx@gmail.com
 */
@Module
public class BikeModule {

    private BikePresenter mPresenter;

    public BikeModule(BikePresenter presenter) {
        mPresenter = presenter;
    }

    @Provides
    @PerPresenter
    public BikePresenter provideBikePresenter() {
        return mPresenter;
    }

    @Provides
    @PerPresenter
    public BikeModelImpl provideBikeModelImpl(BikePresenter presenter) {
        return new BikeModelImpl(presenter);
    }
}
