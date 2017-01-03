package tysheng.sxbus.ui.running;

import android.text.TextUtils;

import java.util.List;

import io.reactivex.Observable;
import io.reactivex.functions.BiFunction;
import tysheng.sxbus.base.BaseFragment;
import tysheng.sxbus.base.BasePresenter;
import tysheng.sxbus.bean.BusLinesResult;
import tysheng.sxbus.bean.CallBack;
import tysheng.sxbus.bean.KeQiaoBusResult;
import tysheng.sxbus.bean.Stations;
import tysheng.sxbus.bean.Status;
import tysheng.sxbus.bean.YueChenBusResult;
import tysheng.sxbus.net.BusRetrofit;
import tysheng.sxbus.utils.JsonUtil;
import tysheng.sxbus.utils.ListUtil;
import tysheng.sxbus.utils.LogUtil;
import tysheng.sxbus.utils.RxHelper;
import tysheng.sxbus.utils.StyObserver;

/**
 * Created by tysheng
 * Date: 2017/1/3 14:24.
 * Email: tyshengsx@gmail.com
 */

class RunningPresenter implements BasePresenter {
    private RunningView<List<Stations>> mFragment;

    RunningPresenter(RunningView<List<Stations>> fragment) {
        mFragment = fragment;
    }

    void refresh(BaseFragment fragment, String id) {
        Observable.zip(BusRetrofit.get().getBusLines(id),
                BusRetrofit.get().getRunningBus(id),
                new BiFunction<CallBack, CallBack, List<Stations>>() {
                    @Override
                    public List<Stations> apply(CallBack busLines, CallBack busLine) throws Exception {
                        return zip(busLines, busLine);
                    }
                })
                .compose(fragment.<List<Stations>>bindToLifecycle())
                .compose(RxHelper.<List<Stations>>ioToMain())
                .subscribe(new StyObserver<List<Stations>>() {
                    @Override
                    public void next(List<Stations> stationses) {
                        mFragment.onSuccess(stationses);
                    }

                    @Override
                    public void onTerminate() {
                        super.onTerminate();
                        mFragment.onTerminate();
                    }

                    @Override
                    public void onError(Throwable t) {
                        mFragment.onError(t);
                        super.onError(t);
                    }
                });
    }

    private double countDistance(double[] i1, double[] i2) {
        return (i1[0] - i2[0]) * (i1[0] - i2[0])
                + (i1[1] - i2[1]) * (i1[1] - i2[1]);
    }

    private List<Stations> zip(CallBack busLines, CallBack busLine) {
        BusLinesResult finalResult = JsonUtil.parse(busLines.result, BusLinesResult.class);
        LogUtil.d(finalResult.lineName + finalResult.endStationName);
        List<Stations> stations = JsonUtil.parseArray(finalResult.stations, Stations.class);
        //running
        Status status = JsonUtil.parse(busLine.status, Status.class);
        if (status.code == 0 && !ListUtil.isEmpty(stations)) {
            if (TextUtils.equals("市公交集团公司", finalResult.owner)) {
                List<YueChenBusResult> list = JsonUtil.parseArray(busLine.result, YueChenBusResult.class);
                for (YueChenBusResult result : list) {
                    int station = result.stationSeqNum - 1;
                    if (station < stations.size())
                        stations.get(station).updateTime = "a";
                }
            } else {//县汽运巴士
                List<KeQiaoBusResult> runningList = JsonUtil.parseArray(busLine.result, KeQiaoBusResult.class);
                for (KeQiaoBusResult result : runningList) {
                    double[] i1 = new double[]{result.lng, result.lat};
                    double distance = 2;
                    int station = 0;
                    for (int i = 0; i < stations.size(); i++) {
                        double[] i2 = new double[]{stations.get(i).lng, stations.get(i).lat};
                        double temp = countDistance(i1, i2);
                        if (temp < distance) {
                            station = i;
                            distance = temp;
                        }
                    }
                    stations.get(station).updateTime = "a";
                }
            }
        }
        return stations;
    }

    @Override
    public void onDestroy() {
        mFragment = null;
    }
}
