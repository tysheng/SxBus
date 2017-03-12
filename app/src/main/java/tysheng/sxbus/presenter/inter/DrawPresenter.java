package tysheng.sxbus.presenter.inter;

import com.baidu.mapapi.map.BaiduMap;

import tysheng.sxbus.presenter.base.BaseFragmentPresenter;

/**
 * Created by tysheng
 * Date: 2017/3/12 14:01.
 * Email: tyshengsx@gmail.com
 */

public interface DrawPresenter extends BaseFragmentPresenter {
    BaiduMap getBaiduMap();
}
