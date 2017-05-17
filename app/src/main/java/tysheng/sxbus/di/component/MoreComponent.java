package tysheng.sxbus.di.component;

import dagger.Component;
import tysheng.sxbus.di.PerPresenter;
import tysheng.sxbus.di.module.MoreModule;
import tysheng.sxbus.ui.fragments.MoreFragment;

/**
 * Created by tysheng
 * Date: 2017/5/17 13:10.
 * Email: tyshengsx@gmail.com
 */
@PerPresenter
@Component(dependencies = UniverseComponent.class, modules = MoreModule.class)
public interface MoreComponent {

    void inject(MoreFragment fragment);
}
