package tysheng.sxbus.model.impl;

import org.greenrobot.greendao.query.QueryBuilder;

import java.util.List;

import tysheng.sxbus.Constant;
import tysheng.sxbus.bean.Star;
import tysheng.sxbus.dao.StarDao;
import tysheng.sxbus.model.base.BaseModel;
import tysheng.sxbus.model.base.BaseModelImpl;

/**
 * Created by tysheng
 * Date: 2017/1/3 14:59.
 * Email: tyshengsx@gmail.com
 */

public class DbModelImplImpl extends BaseModelImpl implements BaseModel {
    private QueryBuilder<Star> mQueryBuilder;
    private List<Star> mStarList;

    public List<Star> getStarList() {
        if (mQueryBuilder == null)
            mQueryBuilder = mHelper.queryBuilder()
                    .where(StarDao.Properties.TableName.eq(Constant.STAR), StarDao.Properties.IsStar.eq("TRUE"))
                    .orderAsc(StarDao.Properties.SortId);
        return mStarList = mQueryBuilder
                .list();
    }

    @Override
    public void onDestroy() {
    }

    public void deleteByKey(Long mainId) {
        mHelper.deleteByKey(mainId);
    }

    public void dragEnd() {
        mHelper.delete(getStarList());
        for (int i = 0; i < mStarList.size(); i++) {
            Star star = mStarList.get(i);
            star.setSortId((long) i);
            mHelper.save(star);
        }
    }
}
