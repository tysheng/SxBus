package tysheng.sxbus.model.impl;

import java.util.concurrent.TimeUnit;

import io.reactivex.functions.Consumer;
import tysheng.sxbus.bean.CallBack;
import tysheng.sxbus.bean.Stars;
import tysheng.sxbus.bean.Status;
import tysheng.sxbus.net.BusRetrofit;
import tysheng.sxbus.presenter.inter.SearchPresenter;
import tysheng.sxbus.utils.JsonUtil;
import tysheng.sxbus.utils.LogUtil;
import tysheng.sxbus.utils.RxHelper;
import tysheng.sxbus.utils.TyObserver;

/**
 * Created by tysheng
 * Date: 2017/2/28 16:10.
 * Email: tyshengsx@gmail.com
 */

public class SearchModelImpl {
    private SearchPresenter mPresenter;

    public SearchModelImpl(SearchPresenter anPresenter) {
        mPresenter = anPresenter;
    }

    public void getBusSimple(String number) {
        BusRetrofit.get()
                .numberToSearch(number)
                .delay(200, TimeUnit.MICROSECONDS)
                .compose(mPresenter.<String>bindUntilDestroyView())
                .compose(RxHelper.<String>flowableIoToMain())
                .compose(RxHelper.stringIoToCallback())
                .doOnNext(new Consumer<CallBack>() {
                    @Override
                    public void accept(CallBack callBack) throws Exception {
                        LogUtil.d(callBack.toString());
                        Status status = JsonUtil.parse(callBack.status, Status.class);
                        if (status.code == 20306) {
                            mPresenter.onCodeError();
                        } else if (status.code == 0) {
                            mPresenter.setNewData(JsonUtil.parse(callBack.result, Stars.class).result);
                        }
                    }
                })
                .doOnError(new Consumer<Throwable>() {
                    @Override
                    public void accept(Throwable throwable) throws Exception {
                        mPresenter.onNetworkError(throwable);
                    }
                })
                .subscribe(new TyObserver<CallBack>() {
                    @Override
                    public void onTerminate() {
                        super.onTerminate();
                        if (mPresenter != null) {
                            mPresenter.onNetworkTerminate();
                        }
                    }
                });

    }
}
