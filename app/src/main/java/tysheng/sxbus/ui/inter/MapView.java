package tysheng.sxbus.ui.inter;

import com.baidu.mapapi.map.BaiduMap;

import tysheng.sxbus.ui.base.BaseView;

/**
 * Created by tysheng
 * Date: 2017/3/11 15:22.
 * Email: tyshengsx@gmail.com
 */

public interface MapView extends BaseView {
    BaiduMap getMap();

    void refreshLocation();

    void drawStations();
}
