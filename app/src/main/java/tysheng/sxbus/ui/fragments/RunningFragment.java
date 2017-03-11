package tysheng.sxbus.ui.fragments;

import android.os.Bundle;
import android.support.design.widget.CoordinatorLayout;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.TextView;

import butterknife.BindString;
import butterknife.BindView;
import butterknife.OnClick;
import tysheng.sxbus.R;
import tysheng.sxbus.base.BaseFragment;
import tysheng.sxbus.presenter.impl.RunningPresenterImpl;
import tysheng.sxbus.ui.inter.RunningView;
import tysheng.sxbus.utils.SnackBarUtil;

/**
 * Created by tysheng
 * Date: 2016/9/25 14:49.
 * Email: tyshengsx@gmail.com
 */

public class RunningFragment extends BaseFragment<RunningPresenterImpl> implements RunningView, SwipeRefreshLayout.OnRefreshListener {
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

    public static RunningFragment newFragment(String id, String title) {
        RunningFragment fragment = new RunningFragment();
        Bundle bundle = new Bundle();
        bundle.putString("0", id);
        bundle.putString("1", title);
        fragment.setArguments(bundle);
        return fragment;
    }

    @Override
    protected RunningPresenterImpl initPresenter() {
        return new RunningPresenterImpl(this);
    }

    @Override
    protected int getLayoutId() {
        return R.layout.fragment_running;
    }

    @Override
    protected void initData() {
        mPresenter.setArgs(getArguments());
        mTitle.setText(mPresenter.geTitle());
        mPresenter.initData();
        mRecyclerView.setAdapter(mPresenter.getAdapter());
        mRecyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        mSwipeRefreshLayout.setOnRefreshListener(this);
        mSwipeRefreshLayout.setColorSchemeResources(R.color.colorPrimary);
        mSwipeRefreshLayout.post(new Runnable() {
            @Override
            public void run() {
                mSwipeRefreshLayout.setRefreshing(true);
                mPresenter.refresh();
            }
        });
        mPresenter.popupFab(mFloatingActionButton);
    }

    @Override
    public void onNetworkError(Throwable t) {
        SnackBarUtil.show(mCoordinatorLayout, runningError, Snackbar.LENGTH_LONG);
    }

    @Override
    public void onNetworkTerminate() {
        if (mSwipeRefreshLayout.isRefreshing())
            mSwipeRefreshLayout.setRefreshing(false);
        mSwipeRefreshLayout.setEnabled(false);
    }

    @OnClick({R.id.fab, R.id.forMap})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.fab:
                mSwipeRefreshLayout.setEnabled(true);
                mSwipeRefreshLayout.setRefreshing(true);
                mSwipeRefreshLayout.postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        mPresenter.refresh();
                    }
                }, 100);
                break;
            case R.id.forMap:
                mPresenter.startMap();
                break;
            default:
                break;
        }
    }

    @Override
    public void onRefresh() {
        mPresenter.refresh();
    }

}
