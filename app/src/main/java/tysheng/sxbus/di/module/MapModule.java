package tysheng.sxbus.di.module;

import dagger.Module;
import dagger.Provides;
import tysheng.sxbus.di.PerPresenter;
import tysheng.sxbus.ui.inter.MapView;

/**
 * Created by tysheng
 * Date: 2017/5/17 10:38.
 * Email: tyshengsx@gmail.com
 */
@Module
public class MapModule {
    private MapView mMapView;

    public MapModule(MapView mapView) {
        mMapView = mapView;
    }

    @PerPresenter
    @Provides
    MapView provideMapView() {
        return mMapView;
    }


}
