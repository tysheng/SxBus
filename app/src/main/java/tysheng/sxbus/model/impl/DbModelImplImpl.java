package tysheng.sxbus.model.impl;

import org.greenrobot.greendao.query.QueryBuilder;

import java.util.List;

import tysheng.sxbus.Constant;
import tysheng.sxbus.bean.Star;
import tysheng.sxbus.dao.StarDao;
import tysheng.sxbus.model.base.BaseModelImpl;

/**
 * Created by tysheng
 * Date: 2017/1/3 14:59.
 * Email: tyshengsx@gmail.com
 */

public class DbModelImplImpl extends BaseModelImpl {
    private QueryBuilder<Star> mQueryBuilder;

    public List<Star> getStarList() {
        if (mQueryBuilder == null)
            mQueryBuilder = mHelper.queryBuilder()
                    .where(StarDao.Properties.TableName.eq(Constant.STAR), StarDao.Properties.IsStar.eq("TRUE"))
                    .orderAsc(StarDao.Properties.SortId);
        return mQueryBuilder
                .list();
    }

    public void deleteByKey(Long mainId) {
        mHelper.deleteByKey(mainId);
    }

    public void dragEnd(List<Star> data) {
        mHelper.delete(getStarList());
        for (int i = 0; i < data.size(); i++) {
            Star star = data.get(i);
            star.setSortId((long) i);
            mHelper.save(star);
        }
    }
}
