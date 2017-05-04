package tysheng.sxbus.model.base;

import javax.inject.Inject;

import tysheng.sxbus.App;
import tysheng.sxbus.db.StarHelper;
import tysheng.sxbus.di.component.DaggerUniverseComponent;
import tysheng.sxbus.net.BusService;

/**
 * Created by tysheng
 * Date: 2017/5/4 16:45.
 * Email: tyshengsx@gmail.com
 */

public class BaseModelImpl {
    @Inject
    protected StarHelper mHelper;

    @Inject
    protected BusService mBusService;

    public BaseModelImpl() {
        DaggerUniverseComponent.builder()
                .applicationComponent(App.get().getApplicationComponent())
                .build()
                .inject(this);
    }
}
