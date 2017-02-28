package tysheng.sxbus.ui.inter;

import tysheng.sxbus.ui.base.BaseView;

/**
 * Created by tysheng
 * Date: 2017/1/3 20:51.
 * Email: tyshengsx@gmail.com
 */

public interface RunningView extends BaseView {
    void onNetworkTerminate();

    void onNetworkError(Throwable t);
}
