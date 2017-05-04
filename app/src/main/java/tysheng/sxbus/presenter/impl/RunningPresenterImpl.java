package tysheng.sxbus.presenter.impl;

import android.content.DialogInterface;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.RecyclerView;
import android.view.View;

import java.util.List;

import tysheng.sxbus.Constant;
import tysheng.sxbus.adapter.RunningAdapter;
import tysheng.sxbus.base.BaseRecyclerViewAdapter;
import tysheng.sxbus.bean.Stations;
import tysheng.sxbus.model.impl.RunningModelImpl;
import tysheng.sxbus.presenter.base.AbstractPresenter;
import tysheng.sxbus.presenter.inter.RunningPresenter;
import tysheng.sxbus.ui.activities.ToolbarActivity;
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
    private RunningModelImpl mRunningModel;
    private String title;
    private String id;
    private RunningAdapter mRunningAdapter;

    public RunningPresenterImpl(RunningView view) {
        super(view);
        mRunningModel = new RunningModelImpl(this);
    }

    public void refresh() {
        mRunningModel.refresh(id);
    }

    @Override
    public void startMap() {
        if (SPHelper.get(Constant.OFFLINE_MAP, 0) == 0) {
            new AlertDialog.Builder(getContext())
                    .setTitle("提醒")
                    .setMessage("第一次查看会下载离线地图，是否继续？")
                    .setNegativeButton("不了", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            dialog.dismiss();
                        }
                    })
                    .setPositiveButton("继续查看", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            startMapActivity(null);
                        }
                    })
                    .show();
        } else {
            startMapActivity(null);
        }
    }

    private void startMapActivity(final Stations stations) {
        PermissionUtil.requestLocation(getActivity(), new PermissionUtil.Callback() {
            @Override
            public void call(boolean b) {
                if (b) {
                    if (mRunningModel.getResults() != null && mRunningModel.getStations() != null) {
                        ToolbarActivity.startMap(getContext(), mRunningModel.getResults(), mRunningModel.getStations(), stations);
                    } else {
                        SnackBarUtil.show(mView.getRootView(), "数据还未加载完全，请等等");
                    }
                } else {
                    SnackBarUtil.show(mView.getRootView(), "请开启定位权限");
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
        mRunningAdapter = new RunningAdapter();
        mRunningAdapter.setOnItemClickListener(new BaseRecyclerViewAdapter.SimpleItemClickListener() {
            @Override
            public void onItemClick(View view, int position) {
                super.onItemClick(view, position);
                Stations stations = mRunningAdapter.getItem(position);
                startMapActivity(stations);
            }
        });
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
