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
import android.widget.TextView;

import com.trello.rxlifecycle.android.ActivityEvent;

import butterknife.BindString;
import butterknife.BindView;
import rx.Observable;
import rx.Subscriber;
import rx.functions.Action0;
import rx.functions.Func1;
import tysheng.sxbus.R;
import tysheng.sxbus.adapter.RunningAdapter;
import tysheng.sxbus.base.BaseActivity;
import tysheng.sxbus.bean.BusLine;
import tysheng.sxbus.bean.BusLineResult;
import tysheng.sxbus.bean.BusLines;
import tysheng.sxbus.net.BusRetrofit;
import tysheng.sxbus.utils.LogUtil;
import tysheng.sxbus.utils.RxHelper;
import tysheng.sxbus.utils.SnackBarUtil;

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
    private BusLines mBusLines;

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

    private void refresh() {
        BusRetrofit.get().getBusLines(id)
                .compose(RunningActivity.this.<BusLines>bindUntilEvent(ActivityEvent.DESTROY))
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
                .map(new Func1<BusLines, Boolean>() {
                    @Override
                    public Boolean call(BusLines busLines) {
                        mBusLines = busLines;
                        return true;
                    }
                })
                .concatMap(new Func1<Boolean, Observable<BusLine>>() {
                    @Override
                    public Observable<BusLine> call(Boolean aBoolean) {
                        return BusRetrofit.get()
                                .getRunningBus(id);
                    }
                })
                .compose(RxHelper.<BusLine>ioToMain())
                .subscribe(new Subscriber<BusLine>() {
                    @Override
                    public void onCompleted() {

                    }

                    @Override
                    public void onError(Throwable e) {
                        LogUtil.d("running" + e.getMessage());
                        SnackBarUtil.show(mCoordinatorLayout, runningError, Snackbar.LENGTH_LONG);
                    }

                    @Override
                    public void onNext(BusLine busLine) {
                        for (BusLineResult result : busLine.result) {
                            int station = result.stationSeqNum - 1;
                            if (station < mBusLines.result.stations.size())
                                mBusLines.result.stations.get(station).updateTime = "a";
                        }
                        mRunningAdapter.setNewData(mBusLines.result.stations);
                    }
                });
    }
}
