package tysheng.sxbus.presenter.impl;

import android.app.ProgressDialog;
import android.os.Bundle;
import android.support.design.widget.Snackbar;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.util.List;

import tysheng.sxbus.Constant;
import tysheng.sxbus.R;
import tysheng.sxbus.adapter.StarAdapter;
import tysheng.sxbus.base.BaseFragment;
import tysheng.sxbus.bean.FragCallback;
import tysheng.sxbus.bean.Star;
import tysheng.sxbus.model.impl.SearchDbModelImpl;
import tysheng.sxbus.model.impl.SearchModelImpl;
import tysheng.sxbus.presenter.base.AbstractPresenter;
import tysheng.sxbus.presenter.inter.SearchPresenter;
import tysheng.sxbus.ui.inter.SearchView;
import tysheng.sxbus.utils.ListUtil;
import tysheng.sxbus.utils.SnackBarUtil;

/**
 * Created by tysheng
 * Date: 2017/1/3 14:41.
 * Email: tyshengsx@gmail.com
 */

public class SearchPresenterImpl extends AbstractPresenter<SearchView> implements SearchPresenter {
    private SearchDbModelImpl mDbModule;
    private SearchModelImpl mSearchModel;
    private List<Star> mRecentList;
    private StarAdapter mAdapter;
    private ProgressDialog mDialog;

    public SearchPresenterImpl(SearchView view) {
        super(view);
        mDbModule = new SearchDbModelImpl();
        mSearchModel = new SearchModelImpl(this);
    }

    @Override
    public void setArgs(Bundle bundle) {

    }

    @Override
    public void onSimpleItemChildClick(View view, int i) {
        switch (view.getId()) {
            case R.id.number:
            case R.id.textView:
                ((BaseFragment.FragmentCallback) getActivity()).handleCallbackNew(new FragCallback(Constant.WHAT_SEARCH, mAdapter.getItem(i).id,
                        mAdapter.getItem(i).lineName + " 前往 " + mAdapter.getItem(i).endStationName));
                onItemClick(mAdapter.getItem(i));
                break;
            case R.id.star:
                onItemClickCollect(mAdapter.getItem(i));
                mAdapter.notifyItemChanged(i);
                break;
            default:
                break;
        }
    }

    @Override
    public void initData() {
        mAdapter = new StarAdapter(mRecentList = getRecentList());
        initFooter();
    }

    @Override
    public void onDestroy() {
        mDbModule.onDestroy();
        mView = null;
    }

    private List<Star> getRecentList() {
        return mDbModule.getRecentList();
    }

    @Override
    public void initFooter() {
        if (!ListUtil.isEmpty(mRecentList) && mAdapter.getFooterLayoutCount() == 0) {
            View view = LayoutInflater.from(getContext()).inflate(R.layout.footer_clear, (ViewGroup) mView.getView(), false);
            mAdapter.addFooterView(view);
            view.findViewById(R.id.textView).setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    mAdapter.removeAllFooterView();
                    mRecentList.clear();
                    mAdapter.notifyDataSetChanged();
                    delete();
                }
            });
        }
    }

    void onItemClick(Star item) {
        mDbModule.onItemClick(item);
    }

    void onItemClickCollect(Star item) {
        mDbModule.onItemClickCollect(item);
    }

    void delete() {
        mDbModule.delete();
    }

    public void getBusSimple(String number) {
        if (mAdapter.getFooterLayoutCount() != 0)
            mAdapter.removeAllFooterView();
        if (mDialog == null) {
            mDialog = new ProgressDialog(getContext());
            mDialog.setMessage("正在搜索...");
        }
        mDialog.show();
        mSearchModel.getBusSimple(number);
    }

    @Override
    public StarAdapter getAdapter() {
        return mAdapter;
    }

    @Override
    public void setNewData(List<Star> stars) {
        mAdapter.setNewData(stars);
    }

    @Override
    public void setNewDataFromRecent() {
        mAdapter.setNewData(mRecentList = getRecentList());
        initFooter();
    }

    @Override
    public void onNetworkError(Throwable e) {
        SnackBarUtil.show(mView.getCoordinatorLayout(), getContext().getString(R.string.search_error), Snackbar.LENGTH_SHORT);
    }

    @Override
    public void onCodeError() {
        SnackBarUtil.show(mView.getCoordinatorLayout(), getContext().getString(R.string.busline_not_exist), Snackbar.LENGTH_SHORT);
    }

    @Override
    public void onNetworkTerminate() {
        mDialog.dismiss();
    }
}
