package tysheng.sxbus.model.impl;

import android.animation.Animator;
import android.support.design.widget.FloatingActionButton;
import android.text.TextUtils;
import android.view.View;
import android.view.animation.OvershootInterpolator;

import com.baidu.mapapi.model.LatLng;
import com.baidu.mapapi.utils.CoordinateConverter;
import com.baidu.mapapi.utils.DistanceUtil;

import java.util.ArrayList;
import java.util.List;

import io.reactivex.Flowable;
import io.reactivex.functions.BiFunction;
import io.reactivex.functions.Consumer;
import tysheng.sxbus.Constant;
import tysheng.sxbus.bean.BusLinesResult;
import tysheng.sxbus.bean.CallBack;
import tysheng.sxbus.bean.Stations;
import tysheng.sxbus.bean.Status;
import tysheng.sxbus.bean.SxBusResult;
import tysheng.sxbus.net.BusRetrofit;
import tysheng.sxbus.presenter.inter.RunningPresenter;
import tysheng.sxbus.utils.JsonUtil;
import tysheng.sxbus.utils.ListUtil;
import tysheng.sxbus.utils.MapUtil;
import tysheng.sxbus.utils.RxHelper;
import tysheng.sxbus.utils.SPHelper;
import tysheng.sxbus.utils.SystemUtil;
import tysheng.sxbus.utils.TyObserver;

/**
 * Created by tysheng
 * Date: 2017/1/17 11:16.
 * Email: tyshengsx@gmail.com
 */

public class RunningModelImpl {
    private RunningPresenter mPresenter;
    private List<SxBusResult> mResults;
    private List<Stations> stations;

    public RunningModelImpl(RunningPresenter anPresenter) {
        mPresenter = anPresenter;
    }

    public void refresh(String id) {
        Flowable.zip(BusRetrofit.get().getBusLines(id)
                        .compose(RxHelper.stringIoToCallback()),
                BusRetrofit.get().getRunningBus(id)
                        .compose(RxHelper.stringIoToCallback()),
                new BiFunction<CallBack, CallBack, List<Stations>>() {
                    @Override
                    public List<Stations> apply(CallBack busLines, CallBack busLine) throws Exception {
                        return zip(busLines, busLine);
                    }
                })
                .compose(mPresenter.<List<Stations>>bindUntilDestroyView())
                .compose(RxHelper.<List<Stations>>flowableIoToMain())
                .doOnNext(new Consumer<List<Stations>>() {
                    @Override
                    public void accept(List<Stations> stationses) throws Exception {
                        mPresenter.onDataSuccess(stationses);
                    }
                })
                .doOnError(new Consumer<Throwable>() {
                    @Override
                    public void accept(Throwable throwable) throws Exception {
                        mPresenter.onNetworkError(throwable);
                    }
                })
                .subscribe(new TyObserver<List<Stations>>() {
                    @Override
                    public void onTerminate() {
                        super.onTerminate();
                        if (mPresenter != null) {
                            mPresenter.onNetworkTerminate();
                        }
                    }
                });
    }

    private boolean byStation() {
        return SPHelper.get(Constant.STATION_MODE, Constant.BY_STATION) == Constant.BY_STATION;
    }

    private double countDistance(double[] i1, double[] i2) {
        return DistanceUtil.getDistance(new LatLng(i1[0], i1[1]), new LatLng(i2[0], i2[1]));
//        return (i1[0] - i2[0]) * (i1[0] - i2[0])
//                + (i1[1] - i2[1]) * (i1[1] - i2[1]);
    }

    private List<Stations> zip(CallBack busLines, CallBack busLine) {
        BusLinesResult finalResult = JsonUtil.parse(busLines.result, BusLinesResult.class);
        stations = JsonUtil.parseArray(finalResult.stations, Stations.class);
        //running
        Status status = JsonUtil.parse(busLine.status, Status.class);
        if (status.code == 0 && !ListUtil.isEmpty(stations)) {
            mResults = JsonUtil.parseArray(busLine.result, SxBusResult.class);
            if (TextUtils.equals("市公交集团公司", finalResult.owner) && byStation()) {
                for (SxBusResult result : mResults) {
                    int station = result.stationSeqNum - 1;
                    if (station < stations.size()) {
                        stations.get(station).arriveState = Stations.ArriveState.Arriving;
                    }
                }
            } else {//县汽运巴士
                for (SxBusResult result : mResults) {
                    double[] i1 = new double[]{result.lng, result.lat};
                    double distance = 2;
                    int station = 0;
                    for (int i = 0; i < stations.size(); i++) {
                        double[] i2 = new double[]{stations.get(i).lng, stations.get(i).lat};
                        double temp = countDistance(MapUtil.gpsToBdLatLng(CoordinateConverter.CoordType.GPS, i1), i2);
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

    public ArrayList<Stations> getStations() {
        return (ArrayList<Stations>) stations;
    }

    public ArrayList<SxBusResult> getResults() {
        return (ArrayList<SxBusResult>) mResults;
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
