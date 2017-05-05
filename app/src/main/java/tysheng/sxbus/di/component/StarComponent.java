package tysheng.sxbus.di.component;

import dagger.Component;
import tysheng.sxbus.di.PerPresenter;
import tysheng.sxbus.di.module.StarModule;
import tysheng.sxbus.presenter.impl.StarPresenterImpl;

/**
 * Created by tysheng
 * Date: 2017/5/5 10:28.
 * Email: tyshengsx@gmail.com
 */
@PerPresenter
@Component(dependencies = UniverseComponent.class, modules = StarModule.class)
public interface StarComponent {
    void inject(StarPresenterImpl i);
}
