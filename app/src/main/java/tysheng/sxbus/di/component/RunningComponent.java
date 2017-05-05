package tysheng.sxbus.di.component;

import dagger.Component;
import tysheng.sxbus.di.PerPresenter;
import tysheng.sxbus.di.module.RunningModule;
import tysheng.sxbus.presenter.impl.RunningPresenterImpl;
import tysheng.sxbus.presenter.inter.RunningPresenter;

/**
 * Created by tysheng
 * Date: 2017/5/5 09:26.
 * Email: tyshengsx@gmail.com
 */
@PerPresenter
@Component(dependencies = UniverseComponent.class, modules = RunningModule.class)
public interface RunningComponent {
    RunningPresenter getPresenter();

    void inject(RunningPresenterImpl presenter);

}
