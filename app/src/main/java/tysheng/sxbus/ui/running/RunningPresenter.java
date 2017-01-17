package tysheng.sxbus.ui.running;

import java.util.List;

import io.reactivex.Observable;
import io.reactivex.functions.BiFunction;
import tysheng.sxbus.base.BasePresenter;
import tysheng.sxbus.bean.CallBack;
import tysheng.sxbus.bean.Stations;
import tysheng.sxbus.net.BusRetrofit;
import tysheng.sxbus.utils.RxHelper;
import tysheng.sxbus.utils.StyObserver;

/**
 * Created by tysheng
 * Date: 2017/1/3 14:24.
 * Email: tyshengsx@gmail.com
 */

class RunningPresenter implements BasePresenter {
    private RunningView<List<Stations>> mView;
    private RunningModule mRunningModule;

    RunningPresenter(RunningView<List<Stations>> view) {
        mView = view;
        mRunningModule = new RunningModule();
    }

    void refresh(String id) {
        Observable.zip(BusRetrofit.get().getBusLines(id),
                BusRetrofit.get().getRunningBus(id),
                new BiFunction<CallBack, CallBack, List<Stations>>() {
                    @Override
                    public List<Stations> apply(CallBack busLines, CallBack busLine) throws Exception {
                        return mRunningModule.zip(busLines, busLine);
                    }
                })
                .compose(mView.<List<Stations>>bind2Lifecycle())
                .compose(RxHelper.<List<Stations>>ioToMain())
                .subscribe(new StyObserver<List<Stations>>() {
                    @Override
                    public void next(List<Stations> stationses) {
                        mView.onSuccess(stationses);
                    }

                    @Override
                    public void onTerminate() {
                        super.onTerminate();
                        mView.onTerminate();
                    }

                    @Override
                    public void onError(Throwable t) {
                        mView.onError(t);
                        super.onError(t);
                    }
                });
    }


    @Override
    public void onDestroy() {
        mView = null;
    }
}
