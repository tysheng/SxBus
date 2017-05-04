package tysheng.sxbus.ui.fragments;

import android.os.Bundle;
import android.support.design.widget.Snackbar;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.view.View;

import tysheng.sxbus.R;
import tysheng.sxbus.base.BaseFragmentV2;
import tysheng.sxbus.databinding.FragmentRunningBinding;
import tysheng.sxbus.presenter.impl.RunningPresenterImpl;
import tysheng.sxbus.ui.inter.RunningView;
import tysheng.sxbus.utils.SnackBarUtil;

/**
 * Created by tysheng
 * Date: 2016/9/25 14:49.
 * Email: tyshengsx@gmail.com
 */

public class RunningFragment extends BaseFragmentV2<RunningPresenterImpl, FragmentRunningBinding> implements RunningView, SwipeRefreshLayout.OnRefreshListener {

    private String runningError;

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
        binding.setHost(this);
        mPresenter.setArgs(getArguments());
        runningError = getString(R.string.running_error);
        binding.title.setText(mPresenter.geTitle());
        mPresenter.initData();
        binding.recyclerView.setAdapter(mPresenter.getAdapter());
        binding.recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        binding.swipeRefreshLayout.setOnRefreshListener(this);
        binding.swipeRefreshLayout.setColorSchemeResources(R.color.colorPrimary);
        binding.swipeRefreshLayout.post(new Runnable() {
            @Override
            public void run() {
                binding.swipeRefreshLayout.setRefreshing(true);
                mPresenter.refresh();
            }
        });
        mPresenter.popupFab(binding.fab);
    }

    @Override
    public void onNetworkError(Throwable t) {
        SnackBarUtil.show(binding.getRoot(), runningError, Snackbar.LENGTH_LONG);
    }

    @Override
    public void onNetworkTerminate() {
        if (binding.swipeRefreshLayout.isRefreshing())
            binding.swipeRefreshLayout.setRefreshing(false);
        binding.swipeRefreshLayout.setEnabled(false);
    }

    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.fab:
                binding.swipeRefreshLayout.setEnabled(true);
                binding.swipeRefreshLayout.setRefreshing(true);
                binding.swipeRefreshLayout.postDelayed(new Runnable() {
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
