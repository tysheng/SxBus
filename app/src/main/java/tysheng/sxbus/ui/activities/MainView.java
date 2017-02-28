package tysheng.sxbus.ui.activities;

import android.support.v4.app.Fragment;

/**
 * Created by tysheng
 * Date: 2017/1/3 20:39.
 * Email: tyshengsx@gmail.com
 */

public interface MainView {
    void jumpFragment(Fragment from, Fragment to, String tag);

    void setCurrentPosition(int pos);
}
