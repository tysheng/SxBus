package tysheng.sxbus.ui;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.CoordinatorLayout;
import android.support.design.widget.Snackbar;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.widget.TextView;

import com.trello.rxlifecycle.android.ActivityEvent;

import java.util.List;

import butterknife.BindString;
import butterknife.BindView;
import rx.Observable;
import rx.functions.Action0;
import rx.functions.Func2;
import tysheng.sxbus.R;
import tysheng.sxbus.adapter.RunningAdapter;
import tysheng.sxbus.base.BaseActivity;
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
import tysheng.sxbus.utils.StySubscriber;

/**
 * 查看 车在哪
 * Created by Sty
 * Date: 16/9/10 20:47.
 */
public class RunningActivity extends BaseActivity {
    @BindView(R.id.recyclerView)
    RecyclerView mRecyclerView;
    @BindView(R.id.coordinatorLayout)
    CoordinatorLayout mCoordinatorLayout;
    @BindView(R.id.swipeRefreshLayout)
    SwipeRefreshLayout mSwipeRefreshLayout;
    @BindString(R.string.running_error)
    String runningError;
    @BindView(R.id.title)
    TextView mTitle;

    private String id, title;
    private RunningAdapter mRunningAdapter;

    public static Intent newIntent(Context context, String id, String title) {
        Intent intent = new Intent(context, RunningActivity.class);
        intent.putExtra("id", id);
        intent.putExtra("title", title);
        return intent;
    }

    @Override
    public int getLayoutId() {
        return R.layout.activity_running;
    }

    @Override
    public void initData(@Nullable Bundle savedInstanceState) {
        id = getIntent().getStringExtra("id");
        title = getIntent().getStringExtra("title");
        mTitle.setText(title);
        mRunningAdapter = new RunningAdapter(null);
        mRecyclerView.setAdapter(mRunningAdapter);
        mRecyclerView.setLayoutManager(new LinearLayoutManager(this));
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
    }


    double countDistance(double[] i1, double[] i2) {
        return (i1[0] - i2[0]) * (i1[0] - i2[0])
                + (i1[1] - i2[1]) * (i1[1] - i2[1]);
    }

    private void refresh() {
        Observable.zip(BusRetrofit.get().getBusLines(id),
                BusRetrofit.get().getRunningBus(id),
                new Func2<CallBack, CallBack, List<Stations>>() {
                    @Override
                    public List<Stations> call(CallBack busLines, CallBack busLine) {
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
                .doAfterTerminate(new Action0() {
                    @Override
                    public void call() {
                        mSwipeRefreshLayout.post(new Runnable() {
                            @Override
                            public void run() {
                                if (mSwipeRefreshLayout.isRefreshing())
                                    mSwipeRefreshLayout.setRefreshing(false);
                            }
                        });
                    }
                })
                .compose(RunningActivity.this.<List<Stations>>bindUntilEvent(ActivityEvent.DESTROY))
                .compose(RxHelper.<List<Stations>>ioToMain())
                .subscribe(new StySubscriber<List<Stations>>() {
                    @Override
                    public void next(List<Stations> stationsList) {
                        mRunningAdapter.setNewData(stationsList);
                    }

                    @Override
                    public void onError(Throwable e) {
                        SnackBarUtil.show(mCoordinatorLayout, runningError, Snackbar.LENGTH_LONG);
                    }
                });
    }
}
