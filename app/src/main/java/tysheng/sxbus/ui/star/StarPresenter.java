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
    private StarFragment mFragment;
    private DbModule mDbModule;

    StarPresenter(StarFragment fragment) {
        mFragment = fragment;
        mDbModule = new DbModule(this);
    }

    List<Star> getStarList() {
        return mDbModule.getStarList();
    }

    @Override
    public void onDestroy() {

    }

    void deleteByKey(Long mainId) {
        mDbModule.deleteByKey(mainId);
    }

    void dragEnd(List<Star> starList) {
        mDbModule.dragEnd(starList);
    }
}
