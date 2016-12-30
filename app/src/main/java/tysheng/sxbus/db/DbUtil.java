/*
******************************* Copyright (c)*********************************\
**
**                 (c) Copyright 2015, 蒋朋, china, qd. sd
**                          All Rights Reserved
**
**                           By()
**                         
**-----------------------------------版本信息------------------------------------
** 版    本: V0.1
**
**------------------------------------------------------------------------------
********************************End of Head************************************\
*/

package tysheng.sxbus.db;


import tysheng.sxbus.dao.StarDao;

/**
 * Created by liuwen on 2016/12/26.
 */
public class DbUtil {
    private static StarHelper sStarHelper;

    private static StarDao getDao() {
        return DaoCore.getInstance().getDaoSession().getStarDao();
    }

    public static StarHelper getDriverHelper() {
        if (sStarHelper == null) {
            sStarHelper = new StarHelper(getDao());
        }
        return sStarHelper;
    }

}
