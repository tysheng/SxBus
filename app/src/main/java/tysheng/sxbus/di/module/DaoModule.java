package tysheng.sxbus.di.module;

import javax.inject.Singleton;

import dagger.Module;
import dagger.Provides;
import tysheng.sxbus.App;
import tysheng.sxbus.dao.DaoMaster;
import tysheng.sxbus.dao.DaoSession;
import tysheng.sxbus.dao.StarDao;
import tysheng.sxbus.db.MyOpenHelper;
import tysheng.sxbus.db.StarHelper;

/**
 * Created by tysheng
 * Date: 2017/5/4 16:16.
 * Email: tyshengsx@gmail.com
 */
@Module
public class DaoModule {
    private static final String DEFAULT_DB_NAME = "green-dao3.db";

    @Singleton
    @Provides
    DaoMaster provideDaoMaster(App app) {
        //此处不可用 DaoMaster.DevOpenHelper, 那是开发辅助类，我们要自定义一个，方便升级
        DaoMaster.OpenHelper helper = new MyOpenHelper(app, DEFAULT_DB_NAME);
        return new DaoMaster(helper.getWritableDatabase());

    }

    @Singleton
    @Provides
    DaoSession provideDaoSession(DaoMaster master) {
        return master.newSession();
    }

    @Singleton
    @Provides
    public StarDao provideStarDao(DaoSession daoSession) {
        return daoSession.getStarDao();
    }

    @Singleton
    @Provides
    public StarHelper provideStarHelper(StarDao dao) {
        return new StarHelper(dao);
    }

}
