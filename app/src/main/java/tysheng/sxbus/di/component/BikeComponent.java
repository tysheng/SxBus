package tysheng.sxbus.di.component;

import dagger.Component;
import tysheng.sxbus.di.PerPresenter;
import tysheng.sxbus.di.module.BikeModule;
import tysheng.sxbus.presenter.impl.MorePresenterImpl;

/**
 * Created by tysheng
 * Date: 2017/5/17 13:02.
 * Email: tyshengsx@gmail.com
 */
@PerPresenter
@Component(dependencies = UniverseComponent.class, modules = BikeModule.class)
public interface BikeComponent {
//    BikePresenter getBikePresenter();

    void inject(MorePresenterImpl i);
}
