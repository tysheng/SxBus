package tysheng.sxbus.model.impl;

import android.animation.Animator;
import android.support.design.widget.FloatingActionButton;
import android.text.TextUtils;
import android.view.View;
import android.view.animation.OvershootInterpolator;

import java.util.List;

import io.reactivex.Observable;
import io.reactivex.functions.BiFunction;
import tysheng.sxbus.App;
import tysheng.sxbus.Constant;
import tysheng.sxbus.bean.BusLinesResult;
import tysheng.sxbus.bean.CallBack;
import tysheng.sxbus.bean.KeQiaoBusResult;
import tysheng.sxbus.bean.Stations;
import tysheng.sxbus.bean.Status;
import tysheng.sxbus.bean.YueChenBusResult;
import tysheng.sxbus.net.BusRetrofit;
import tysheng.sxbus.presenter.inter.RunningPresenterInterface;
import tysheng.sxbus.utils.JsonUtil;
import tysheng.sxbus.utils.ListUtil;
import tysheng.sxbus.utils.RxHelper;
import tysheng.sxbus.utils.SPHelper;
import tysheng.sxbus.utils.StyObserver;
import tysheng.sxbus.utils.SystemUtil;

/**
 * Created by tysheng
 * Date: 2017/1/17 11:16.
 * Email: tyshengsx@gmail.com
 */

public class RunningModel {
    private SPHelper mSPHelper;
    private RunningPresenterInterface mInterface;

    public RunningModel(RunningPresenterInterface anInterface) {
        mInterface = anInterface;
    }

    public void refresh(String id) {
        Observable.zip(BusRetrofit.get().getBusLines(id),
                BusRetrofit.get().getRunningBus(id),
                new BiFunction<CallBack, CallBack, List<Stations>>() {
                    @Override
                    public List<Stations> apply(CallBack busLines, CallBack busLine) throws Exception {
                        return zip(busLines, busLine);
                    }
                })
                .compose(mInterface.<List<Stations>>bindUntilDestroyView())
                .compose(RxHelper.<List<Stations>>ioToMain())
                .subscribe(new StyObserver<List<Stations>>() {
                    @Override
                    public void next(List<Stations> stationses) {
                        mInterface.onDataSuccess(stationses);
                    }

                    @Override
                    public void onTerminate() {
                        super.onTerminate();
                        mInterface.onNetworkTerminate();
                    }

                    @Override
                    public void onError(Throwable t) {
                        mInterface.onNetworkError(t);
                        super.onError(t);
                    }
                });
    }

    private SPHelper getSPHelper() {
        if (mSPHelper == null) {
            mSPHelper = new SPHelper(App.get());
        }
        return mSPHelper;
    }

    private boolean byStation() {
        return getSPHelper().get(Constant.STATION_MODE, Constant.BY_STATION) == Constant.BY_STATION;
    }

    private double countDistance(double[] i1, double[] i2) {
        return (i1[0] - i2[0]) * (i1[0] - i2[0])
                + (i1[1] - i2[1]) * (i1[1] - i2[1]);
    }

    private List<Stations> zip(CallBack busLines, CallBack busLine) {
        BusLinesResult finalResult = JsonUtil.parse(busLines.result, BusLinesResult.class);
        List<Stations> stations = JsonUtil.parseArray(finalResult.stations, Stations.class);
        //running
        Status status = JsonUtil.parse(busLine.status, Status.class);
        if (status.code == 0 && !ListUtil.isEmpty(stations)) {
            if (TextUtils.equals("市公交集团公司", finalResult.owner) && byStation()) {
                List<YueChenBusResult> list = JsonUtil.parseArray(busLine.result, YueChenBusResult.class);
                for (YueChenBusResult result : list) {
                    int station = result.stationSeqNum - 1;
                    if (station < stations.size()) {
                        stations.get(station).arriveState = Stations.ArriveState.Arriving;
                    }
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
                    stations.get(station).arriveState = Stations.ArriveState.Arriving;
                }
            }
        }
        return stations;
    }

    public void popupFab(final FloatingActionButton floatingActionButton) {
        floatingActionButton.setTranslationY(2 * SystemUtil.dp2px(56));
        floatingActionButton.animate()
                .translationY(0)
                .setInterpolator(new OvershootInterpolator(1.f))
                .setStartDelay(500L)
                .setDuration(500L)
                .setListener(new Animator.AnimatorListener() {
                    @Override
                    public void onAnimationStart(Animator animation) {
                        floatingActionButton.setVisibility(View.VISIBLE);
                    }

                    @Override
                    public void onAnimationEnd(Animator animation) {

                    }

                    @Override
                    public void onAnimationCancel(Animator animation) {

                    }

                    @Override
                    public void onAnimationRepeat(Animator animation) {

                    }
                })
                .start();
    }
}
