package tysheng.sxbus.db;

import org.greenrobot.greendao.query.QueryBuilder;

import tysheng.sxbus.App;
import tysheng.sxbus.dao.DaoMaster;
import tysheng.sxbus.dao.DaoSession;

/**
 * Created by tysheng
 * Date: 2016/12/30 09:35.
 * Email: tyshengsx@gmail.com
 */

public class DaoCore {
    private static final String DEFAULT_DB_NAME = "green-dao3.db";
    private static String DB_NAME;
    private static DaoCore mInstance;
    private DaoMaster daoMaster;
    private DaoSession daoSession;

    public static void init() {
        init(DEFAULT_DB_NAME);
    }

    public static void init(String dbName) {
        DB_NAME = dbName;
    }

    public static void enableQueryBuilderLog() {
        QueryBuilder.LOG_SQL = true;
        QueryBuilder.LOG_VALUES = true;
    }

    static DaoCore getInstance() {
        if (mInstance == null) {
            mInstance = new DaoCore();
        }
        return mInstance;
    }

    private DaoMaster getDaoMaster() {
        if (daoMaster == null) {
            //此处不可用 DaoMaster.DevOpenHelper, 那是开发辅助类，我们要自定义一个，方便升级
            DaoMaster.OpenHelper helper = new MyOpenHelper(App.get(), DB_NAME);
            daoMaster = new DaoMaster(helper.getWritableDatabase());
        }
        return daoMaster;
    }

    DaoSession getDaoSession() {
        if (daoSession == null) {
            if (daoMaster == null) {
                daoMaster = getDaoMaster();
            }
            daoSession = daoMaster.newSession();
        }
        return daoSession;
    }


}
