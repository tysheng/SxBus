package tysheng.sxbus.di.module;

import dagger.Module;
import dagger.Provides;
import tysheng.sxbus.adapter.StarAdapter;
import tysheng.sxbus.di.PerPresenter;
import tysheng.sxbus.model.impl.DbModelImplImpl;

/**
 * Created by tysheng
 * Date: 2017/5/5 10:29.
 * Email: tyshengsx@gmail.com
 */
@Module
public class StarModule {
    @PerPresenter
    @Provides
    public StarAdapter provideStarAdapter() {
        return new StarAdapter();
    }

    @PerPresenter
    @Provides
    public DbModelImplImpl provideDbModelImplImpl() {
        return new DbModelImplImpl();
    }

}
