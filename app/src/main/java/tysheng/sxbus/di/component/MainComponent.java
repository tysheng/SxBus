package tysheng.sxbus.di.component;

import dagger.Component;
import tysheng.sxbus.di.PerActivity;
import tysheng.sxbus.di.module.MainModule;
import tysheng.sxbus.ui.activities.MainActivity;

/**
 * Created by tysheng
 * Date: 2017/5/17 09:43.
 * Email: tyshengsx@gmail.com
 */
@PerActivity
@Component(dependencies = UniverseComponent.class, modules = MainModule.class)
public interface MainComponent {
    void inject(MainActivity activity);
}
