package tysheng.sxbus.ui.search;

import java.util.List;

import tysheng.sxbus.Constant;
import tysheng.sxbus.base.BaseModule;
import tysheng.sxbus.bean.Star;
import tysheng.sxbus.dao.StarDao;
import tysheng.sxbus.db.DbUtil;
import tysheng.sxbus.db.StarHelper;

/**
 * Created by tysheng
 * Date: 2017/1/3 14:42.
 * Email: tyshengsx@gmail.com
 */

class DbModule implements BaseModule {

    private StarHelper mHelper;

    DbModule() {
        mHelper = DbUtil.getDriverHelper();
    }

    List<Star> getRecentList() {
        return mHelper.queryBuilder()
                .where(StarDao.Properties.TableName.eq(Constant.RECENT))
                .limit(20)
                .orderDesc(StarDao.Properties.MainId)
                .list();
    }

    @Override
    public void onDestroy() {

    }

    void onItemClick(Star star) {
        star.setTableName(Constant.RECENT);
        mHelper.saveOrUpdate(star);
    }

    void onItemClickCollect(Star item) {
        item.isStar = true;

        Star uni = mHelper.queryBuilder()
                .where(StarDao.Properties.Id.eq(item.getId()), StarDao.Properties.TableName.eq(Constant.STAR))
                .unique();
        if (uni == null) {
            try {
                Star s = (Star) item.clone();
                s.setTableName(Constant.STAR);
                mHelper.saveOrUpdate(s);
            } catch (CloneNotSupportedException e) {
                e.printStackTrace();
            }
        }
    }

    void delete() {
        mHelper.delete(getRecentList());
    }
}
