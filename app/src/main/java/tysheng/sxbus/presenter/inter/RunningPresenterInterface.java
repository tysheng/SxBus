package tysheng.sxbus.presenter.inter;

import java.util.List;

import tysheng.sxbus.bean.Stations;
import tysheng.sxbus.presenter.base.RetrofitErrorInterface;

/**
 * Created by tysheng
 * Date: 2017/2/28 16:42.
 * Email: tyshengsx@gmail.com
 */

public interface RunningPresenterInterface extends RetrofitErrorInterface {
    void onDataSuccess(List<Stations> stationses);

    void refresh();
}
