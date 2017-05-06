package tysheng.sxbus.presenter.impl;

import android.content.DialogInterface;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AlertDialog;
import android.view.View;

import java.util.List;

import javax.inject.Inject;

import tysheng.sxbus.Constant;
import tysheng.sxbus.R;
import tysheng.sxbus.adapter.RunningAdapter;
import tysheng.sxbus.base.BaseRecyclerViewAdapter;
import tysheng.sxbus.bean.Stations;
import tysheng.sxbus.di.component.DaggerRunningComponent;
import tysheng.sxbus.di.module.RunningModule;
import tysheng.sxbus.model.impl.RunningModelImpl;
import tysheng.sxbus.presenter.base.AbstractPresenter;
import tysheng.sxbus.presenter.inter.RunningPresenter;
import tysheng.sxbus.ui.activities.MapActivity;
import tysheng.sxbus.ui.inter.RunningView;
import tysheng.sxbus.utils.PermissionUtil;
import tysheng.sxbus.utils.SPHelper;
import tysheng.sxbus.utils.SnackBarUtil;

/**
 * Created by tysheng
 * Date: 2017/1/3 14:24.
 * Email: tyshengsx@gmail.com
 */

public class RunningPresenterImpl extends AbstractPresenter<RunningView> implements RunningPresenter {
    @Inject
    RunningModelImpl mRunningModel;
    @Inject
    RunningAdapter mRunningAdapter;
    private String title;
    private String id;

    public RunningPresenterImpl(RunningView view) {
        super(view);
        DaggerRunningComponent.builder()
                .universeComponent(getUniverseComponent())
                .runningModule(new RunningModule(this))
                .build()
                .inject(this);
    }

    public void refresh() {
        mRunningModel.refresh(id);
    }

    @Override
    public void startMap(final Stations stations) {
        if (SPHelper.get(Constant.OFFLINE_MAP, 0) == 0) {
            new AlertDialog.Builder(getContext())
                    .setTitle(R.string.notice)
                    .setMessage(R.string.map_notice_msg)
                    .setNegativeButton(R.string.nope, new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            dialog.dismiss();
                        }
                    })
                    .setPositiveButton(R.string.forward_view, new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            startMapActivity(stations);
                        }
                    })
                    .show();
        } else {
            startMapActivity(stations);
        }
    }

    private void startMapActivity(final Stations stations) {
        PermissionUtil.requestLocation(getActivity(), new PermissionUtil.Callback() {
            @Override
            public void call(boolean b) {
                if (b) {
                    if (mRunningModel.getResults() != null && mRunningModel.getStations() != null) {
                        MapActivity.startMap(getContext(), Constant.BUS, mRunningModel.getResults(), mRunningModel.getStations(), stations);
                    } else {
                        SnackBarUtil.show(mView.getRootView(), getString(R.string.wait_for_data));
                    }
                } else {
                    SnackBarUtil.show(mView.getRootView(), getString(R.string.ask_for_location_permission));
                }
            }
        });
    }

    @Override
    public void setArgs(Bundle bundle) {
        id = bundle.getString("0");
        title = bundle.getString("1");
    }

    @Override
    public void initData() {
        mRunningAdapter.setOnItemClickListener(new BaseRecyclerViewAdapter.SimpleItemClickListener() {
            @Override
            public void onItemClick(View view, int position) {
                super.onItemClick(view, position);
                Stations stations = mRunningAdapter.getItem(position);
                startMap(stations);
            }
        });
    }

    public void popupFab(FloatingActionButton floatingActionButton) {
        mRunningModel.popupFab(floatingActionButton);
    }

    @Override
    public void onDataSuccess(List<Stations> stationsList) {
        mRunningAdapter.setNewData(stationsList);
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

    public RunningAdapter getAdapter() {
        return mRunningAdapter;
    }
}
