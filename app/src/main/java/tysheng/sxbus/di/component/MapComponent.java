package tysheng.sxbus.di.component;

import dagger.Component;
import tysheng.sxbus.di.PerPresenter;
import tysheng.sxbus.di.module.MapModule;
import tysheng.sxbus.ui.fragments.MapFragment;

/**
 * Created by tysheng
 * Date: 2017/5/17 1:38.
 * Email: tyshengsx@gmail.com
 */
@PerPresenter
@Component(dependencies = UniverseComponent.class, modules = MapModule.class)
public interface MapComponent {

    void inject(MapFragment mapPresenter);
}
