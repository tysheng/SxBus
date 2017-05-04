package tysheng.sxbus.di.component;

import dagger.Component;
import tysheng.sxbus.di.PerFragment;
import tysheng.sxbus.model.base.BaseModelImpl;
import tysheng.sxbus.presenter.impl.MorePresenterImpl;

/**
 * Created by tysheng
 * Date: 2017/5/4 16:50.
 * Email: tyshengsx@gmail.com
 */
@PerFragment
@Component(dependencies = ApplicationComponent.class)
public interface UniverseComponent {
    void inject(BaseModelImpl model);

    void inject(MorePresenterImpl im);
}
