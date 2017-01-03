package tysheng.sxbus.ui.star;

import org.greenrobot.greendao.query.QueryBuilder;

import java.util.List;

import tysheng.sxbus.Constant;
import tysheng.sxbus.base.BaseModule;
import tysheng.sxbus.bean.Star;
import tysheng.sxbus.dao.StarDao;
import tysheng.sxbus.db.DbUtil;
import tysheng.sxbus.db.StarHelper;

/**
 * Created by tysheng
 * Date: 2017/1/3 14:59.
 * Email: tyshengsx@gmail.com
 */

class DbModule implements BaseModule {
    private StarPresenter mPresenter;
    private StarHelper mHelper;
    private QueryBuilder<Star> mQueryBuilder;

    DbModule(StarPresenter presenter) {
        mPresenter = presenter;
        mHelper = DbUtil.getDriverHelper();

    }

    List<Star> getStarList() {
        if (mQueryBuilder == null)
            mQueryBuilder = mHelper.queryBuilder()
                    .where(StarDao.Properties.TableName.eq(Constant.STAR), StarDao.Properties.IsStar.eq("TRUE"))
                    .orderAsc(StarDao.Properties.SortId);
        return mQueryBuilder
                .list();
    }

    @Override
    public void onDestroy() {
        mPresenter = null;
    }

    void deleteByKey(Long mainId) {
        mHelper.deleteByKey(mainId);
    }

    void dragEnd(List<Star> starList) {
        mHelper.delete(getStarList());
        for (int i = 0; i < starList.size(); i++) {
            Star star = starList.get(i);
            star.setSortId((long) i);
            mHelper.save(star);
        }
    }
}
