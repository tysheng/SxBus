package tysheng.sxbus.presenter.impl;

import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.widget.RecyclerView;

import java.util.List;

import tysheng.sxbus.adapter.RunningAdapter;
import tysheng.sxbus.bean.Stations;
import tysheng.sxbus.model.impl.RunningModel;
import tysheng.sxbus.presenter.base.AbstractPresenter;
import tysheng.sxbus.presenter.inter.RunningPresenterInterface;
import tysheng.sxbus.ui.inter.RunningView;

/**
 * Created by tysheng
 * Date: 2017/1/3 14:24.
 * Email: tyshengsx@gmail.com
 */

public class RunningPresenterPresenterImpl extends AbstractPresenter<RunningView> implements RunningPresenterInterface {
    private RunningModel mRunningModel;
    private String title;
    private String id;
    private RunningAdapter mRunningAdapter;

    public RunningPresenterPresenterImpl(RunningView view) {
        super(view);
        mRunningModel = new RunningModel(this);
    }

    public void refresh() {
        mRunningModel.refresh(id);
    }

    @Override
    public void setArgs(Bundle bundle) {
        id = bundle.getString("0");
        title = bundle.getString("1");
    }

    @Override
    public void initData() {
        mRunningAdapter = new RunningAdapter();
    }

    @Override
    public void onDestroy() {
        mView = null;
    }

    public void popupFab(FloatingActionButton floatingActionButton) {
        mRunningModel.popupFab(floatingActionButton);
    }

    @Override
    public void onDataSuccess(List<Stations> stationses) {
        mRunningAdapter.setNewData(stationses);
    }

    @Override
    public void onNetworkError(Throwable t) {
        mView.onNetworkError(t);
    }

    @Override
    public void onCodeError() {

    }

    @Override
    public void onNetworkTerminate() {
        mView.onNetworkTerminate();
    }

    public String geTitle() {
        return title;
    }

    public RecyclerView.Adapter getAdapter() {
        return mRunningAdapter;
    }
}
