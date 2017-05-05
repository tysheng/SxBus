package tysheng.sxbus.presenter.inter;

import android.view.View;

import tysheng.sxbus.presenter.base.BaseFragmentPresenter;

/**
 * Created by tysheng
 * Date: 2017/2/28 20:04.
 * Email: tyshengsx@gmail.com
 */

public interface StarPresenter extends BaseFragmentPresenter {


    void setNewDataFromRecent();

    void onSimpleItemChildClick(View view, int i);
}
