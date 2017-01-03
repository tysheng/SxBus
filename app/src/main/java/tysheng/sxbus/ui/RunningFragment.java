package tysheng.sxbus.ui;

import android.os.Bundle;
import android.support.design.widget.CoordinatorLayout;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.View;
import android.widget.TextView;

import java.util.List;

import butterknife.BindString;
import butterknife.BindView;
import io.reactivex.Observable;
import io.reactivex.functions.BiFunction;
import tysheng.sxbus.R;
import tysheng.sxbus.adapter.RunningAdapter;
import tysheng.sxbus.base.BaseFragment;
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
import tysheng.sxbus.utils.SnackBarUtil;
import tysheng.sxbus.utils.StyObserver;

/**
 * Created by tysheng
 * Date: 2016/9/25 14:49.
 * Email: tyshengsx@gmail.com
 */

public class RunningFragment extends BaseFragment {
    @BindView(R.id.title)
    TextView mTitle;
    @BindView(R.id.recyclerView)
    RecyclerView mRecyclerView;
    @BindView(R.id.swipeRefreshLayout)
    SwipeRefreshLayout mSwipeRefreshLayout;
    @BindView(R.id.coordinatorLayout)
    CoordinatorLayout mCoordinatorLayout;
    @BindString(R.string.running_error)
    String runningError;
    @BindView(R.id.fab)
    FloatingActionButton mFloatingActionButton;
    private String id, title;
    private RunningAdapter mRunningAdapter;

    public static RunningFragment newFragment(String id, String title) {
        RunningFragment fragment = new RunningFragment();
        Bundle bundle = new Bundle();
        bundle.putString("0", id);
        bundle.putString("1", title);
        fragment.setArguments(bundle);
        return fragment;
    }

    @Override
    protected int getLayoutId() {
        return R.layout.fragment_running;
    }

    @Override
    protected void initData() {
        Bundle bundle = getArguments();
        id = bundle.getString("0");
        title = bundle.getString("1");
        mTitle.setText(title);
        mRunningAdapter = new RunningAdapter(null);
        mRecyclerView.setAdapter(mRunningAdapter);
        mRecyclerView.setLayoutManager(new LinearLayoutManager(mActivity));
        mSwipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                refresh();
            }
        });
        mSwipeRefreshLayout.setColorSchemeResources(R.color.colorPrimary);
        mSwipeRefreshLayout.post(new Runnable() {
            @Override
            public void run() {
                mSwipeRefreshLayout.setRefreshing(true);
                refresh();
            }
        });
        mFloatingActionButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mSwipeRefreshLayout.setEnabled(true);
                mSwipeRefreshLayout.setRefreshing(true);
                mSwipeRefreshLayout.postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        refresh();
                    }
                }, 100);
            }
        });
    }


    double countDistance(double[] i1, double[] i2) {
        return (i1[0] - i2[0]) * (i1[0] - i2[0])
                + (i1[1] - i2[1]) * (i1[1] - i2[1]);
    }

    private void refresh() {
        Observable.zip(BusRetrofit.get().getBusLines(id),
                BusRetrofit.get().getRunningBus(id),
                new BiFunction<CallBack, CallBack, List<Stations>>() {
                    @Override
                    public List<Stations> apply(CallBack busLines, CallBack busLine) throws Exception {
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
                })
                .compose(this.<List<Stations>>bindToLifecycle())
                .compose(RxHelper.<List<Stations>>ioToMain())
                .subscribe(new StyObserver<List<Stations>>() {
                    @Override
                    public void next(List<Stations> stationses) {
                        mRunningAdapter.setNewData(stationses);

                    }

                    @Override
                    public void onTerminate() {
                        super.onTerminate();
                        if (mSwipeRefreshLayout.isRefreshing())
                            mSwipeRefreshLayout.setRefreshing(false);
                        mSwipeRefreshLayout.setEnabled(false);
                    }

                    @Override
                    public void onError(Throwable t) {
                        SnackBarUtil.show(mCoordinatorLayout, runningError, Snackbar.LENGTH_LONG);
                        super.onError(t);
                    }
                });
    }
}
