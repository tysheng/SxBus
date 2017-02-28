package tysheng.sxbus.ui.fragments;

import android.support.design.widget.CoordinatorLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.SearchView;
import android.text.TextUtils;
import android.view.View;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.listener.OnItemChildClickListener;

import butterknife.BindView;
import tysheng.sxbus.R;
import tysheng.sxbus.base.BaseFragment;
import tysheng.sxbus.presenter.impl.SearchPresenterImpl;

/**
 * Created by Sty
 * Date: 16/8/10 22:52.
 */
public class SearchFragment extends BaseFragment<SearchPresenterImpl> implements tysheng.sxbus.ui.inter.SearchView {
    @BindView(R.id.recyclerView)
    RecyclerView mRecyclerView;
    @BindView(R.id.coordinatorLayout)
    CoordinatorLayout mCoordinatorLayout;
    @BindView(R.id.searchView)
    SearchView mSearchView;

    @Override
    protected int getLayoutId() {
        return R.layout.fragment_search;
    }

    /**
     * 切换时刷新页面
     *
     * @param hidden
     */
    @Override
    public void onHiddenChanged(boolean hidden) {
        super.onHiddenChanged(hidden);
        if (!hidden) {
            if (TextUtils.isEmpty(mSearchView.getQuery())) {
                mPresenter.setNewDataFromRecent();
            }
            mSearchView.post(new Runnable() {
                @Override
                public void run() {
                    mSearchView.clearFocus();
                }
            });
        }
    }

    @Override
    public CoordinatorLayout getCoordinatorLayout() {
        return mCoordinatorLayout;
    }

    @Override
    protected void initData() {
        mPresenter = new SearchPresenterImpl(this);
        mPresenter.initData();
        mRecyclerView.setAdapter(mPresenter.getAdapter());
        mRecyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        mRecyclerView.addOnItemTouchListener(new OnItemChildClickListener() {
            @Override
            public void onSimpleItemChildClick(BaseQuickAdapter baseQuickAdapter, View view, int i) {
                mPresenter.onSimpleItemChildClick(view, i);
            }
        });
        mSearchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                getBusSimple(query);
                return true;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                return false;
            }
        });
        mSearchView.onActionViewExpanded();
        mSearchView.clearFocus();
    }

    @Override
    protected SearchPresenterImpl initPresenter() {
        return new SearchPresenterImpl(this);
    }

    @Override
    public void onResume() {
        super.onResume();
        if (mSearchView != null) {
            mSearchView.clearFocus();
        }
    }

    private void getBusSimple(String number) {
        mSearchView.clearFocus();
        mPresenter.getBusSimple(number);
    }

}
