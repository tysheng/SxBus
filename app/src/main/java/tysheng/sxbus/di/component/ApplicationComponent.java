package tysheng.sxbus.di.component;

import android.app.Application;

import javax.inject.Singleton;

import dagger.Component;
import tysheng.sxbus.AppLike;
import tysheng.sxbus.db.StarHelper;
import tysheng.sxbus.di.module.ApplicationModule;
import tysheng.sxbus.di.module.DaoModule;
import tysheng.sxbus.di.module.NetModule;
import tysheng.sxbus.model.base.BaseModelImpl;
import tysheng.sxbus.net.BusService;

/**
 * Created by tysheng
 * Date: 2017/5/4 16:15.
 * Email: tyshengsx@gmail.com
 */
@Singleton
@Component(modules = {ApplicationModule.class, DaoModule.class, NetModule.class})
public interface ApplicationComponent {
    Application getApp();

    BusService getBusService();

    StarHelper getStarHelper();

    void inject(AppLike app);

    void inject(BaseModelImpl model);
}
