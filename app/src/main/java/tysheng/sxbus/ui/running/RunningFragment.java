package tysheng.sxbus.ui.running;

import android.os.Bundle;
import android.support.design.widget.CoordinatorLayout;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.widget.TextView;

import com.trello.rxlifecycle2.LifecycleTransformer;

import java.util.List;

import butterknife.BindString;
import butterknife.BindView;
import butterknife.OnClick;
import tysheng.sxbus.R;
import tysheng.sxbus.adapter.RunningAdapter;
import tysheng.sxbus.base.BaseFragment;
import tysheng.sxbus.bean.Stations;
import tysheng.sxbus.utils.SnackBarUtil;

/**
 * Created by tysheng
 * Date: 2016/9/25 14:49.
 * Email: tyshengsx@gmail.com
 */

public class RunningFragment extends BaseFragment implements RunningView<List<Stations>>, SwipeRefreshLayout.OnRefreshListener {
    @BindView(R.id.title)
    TextView mTitle;
    @BindView(R.id.recyclerView)
    RecyclerView mRecyclerView;
    @BindView(R.id.swipeRefreshLayout)
    SwipeRefreshLayout mSwipeRefreshLayout;
    @BindView(R.id.coordinatorLayout)
    CoordinatorLayout mCoordinatorLayout;
    @BindView(R.id.fab)
    FloatingActionButton mFloatingActionButton;
    @BindString(R.string.running_error)
    String runningError;
    private String id;
    private RunningAdapter mRunningAdapter;
    private RunningPresenter mPresenter;

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
        mPresenter = new RunningPresenter(this);
        Bundle bundle = getArguments();
        id = bundle.getString("0");
        mTitle.setText(bundle.getString("1"));
        mRunningAdapter = new RunningAdapter();
        mRecyclerView.setAdapter(mRunningAdapter);
        mRecyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        mSwipeRefreshLayout.setOnRefreshListener(this);
        mSwipeRefreshLayout.setColorSchemeResources(R.color.colorPrimary);
        mSwipeRefreshLayout.post(new Runnable() {
            @Override
            public void run() {
                mSwipeRefreshLayout.setRefreshing(true);
                mPresenter.refresh(id);
            }
        });
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        mPresenter.onDestroy();
    }

    @Override
    public void onSuccess(List<Stations> list) {
        mRunningAdapter.setNewData(list);
    }

    @Override
    public void onError(Throwable e) {
        SnackBarUtil.show(mCoordinatorLayout, runningError, Snackbar.LENGTH_LONG);
    }

    @Override
    public void onTerminate() {
        if (mSwipeRefreshLayout.isRefreshing())
            mSwipeRefreshLayout.setRefreshing(false);
        mSwipeRefreshLayout.setEnabled(false);
    }

    @OnClick(R.id.fab)
    public void onClick() {
        mSwipeRefreshLayout.setEnabled(true);
        mSwipeRefreshLayout.setRefreshing(true);
        mSwipeRefreshLayout.postDelayed(new Runnable() {
            @Override
            public void run() {
                mPresenter.refresh(id);
            }
        }, 100);
    }

    @Override
    public void onRefresh() {
        mPresenter.refresh(id);
    }

    @Override
    public <T> LifecycleTransformer<T> bind2Lifecycle() {
        return bindToLifecycle();
    }
}
