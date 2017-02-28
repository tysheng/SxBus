package tysheng.sxbus.model.impl;

import java.util.concurrent.TimeUnit;

import tysheng.sxbus.bean.CallBack;
import tysheng.sxbus.bean.Stars;
import tysheng.sxbus.bean.Status;
import tysheng.sxbus.net.BusRetrofit;
import tysheng.sxbus.presenter.inter.SearchPresenterInterface;
import tysheng.sxbus.utils.JsonUtil;
import tysheng.sxbus.utils.LogUtil;
import tysheng.sxbus.utils.RxHelper;
import tysheng.sxbus.utils.StyObserver;

/**
 * Created by tysheng
 * Date: 2017/2/28 16:10.
 * Email: tyshengsx@gmail.com
 */

public class SearchModel {
    private SearchPresenterInterface mInterface;

    public SearchModel(SearchPresenterInterface anInterface) {
        mInterface = anInterface;
    }

    public void getBusSimple(String number) {
        BusRetrofit.get()
                .numberToSearch(number)
                .delay(200, TimeUnit.MICROSECONDS)
                .compose(mInterface.<CallBack>bindUntilDestroyView())
                .compose(RxHelper.<CallBack>ioToMain())
                .subscribe(new StyObserver<CallBack>() {
                    @Override
                    public void onTerminate() {
                        super.onTerminate();
                        mInterface.onNetworkTerminate();
                    }

                    @Override
                    public void next(CallBack callBack) {
                        LogUtil.d(callBack.toString());
                        Status status = JsonUtil.parse(callBack.status, Status.class);
                        if (status.code == 20306) {
                            mInterface.onCodeError();
                        } else if (status.code == 0) {
                            mInterface.setNewData(JsonUtil.parse(callBack.result, Stars.class).result);
                        }
                    }

                    @Override
                    public void onError(Throwable e) {
                        super.onError(e);
                        mInterface.onNetworkError(e);
                    }
                });

    }
}
