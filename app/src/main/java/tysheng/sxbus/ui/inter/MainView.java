package tysheng.sxbus.ui.inter;

import android.support.v4.app.Fragment;

import tysheng.sxbus.ui.base.BaseView;

/**
 * Created by tysheng
 * Date: 2017/1/3 20:39.
 * Email: tyshengsx@gmail.com
 */

public interface MainView extends BaseView {
    void jumpFragment(Fragment from, Fragment to, String tag);

    void setCurrentPosition(int pos);
}
