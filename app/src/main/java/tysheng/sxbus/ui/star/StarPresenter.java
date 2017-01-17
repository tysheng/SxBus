package tysheng.sxbus.ui.star;

import java.util.List;

import tysheng.sxbus.base.BasePresenter;
import tysheng.sxbus.bean.Star;

/**
 * Created by tysheng
 * Date: 2017/1/3 15:00.
 * Email: tyshengsx@gmail.com
 */

class StarPresenter implements BasePresenter {
    private StarView mFragment;
    private DbModule mDbModule;

    StarPresenter(StarView fragment) {
        mFragment = fragment;
        mDbModule = new DbModule(this);
    }

    List<Star> getStarList() {
        return mDbModule.getStarList();
    }

    @Override
    public void onDestroy() {
        mFragment = null;
    }

    void deleteByKey(Long mainId) {
        mDbModule.deleteByKey(mainId);
    }

    void dragEnd() {
        mDbModule.dragEnd();
    }
}
