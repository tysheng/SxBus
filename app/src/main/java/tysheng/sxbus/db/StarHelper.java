package tysheng.sxbus.db;

import org.greenrobot.greendao.AbstractDao;

import tysheng.sxbus.bean.Star;

/**
 * Created by tysheng
 * Date: 2016/12/30 09:41.
 * Email: tyshengsx@gmail.com
 */

public class StarHelper extends BaseDbHelper<Star, Long> {
    StarHelper(AbstractDao<Star, Long> dao) {
        super(dao);
    }
}
