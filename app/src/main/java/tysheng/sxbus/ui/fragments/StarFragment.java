package tysheng.sxbus.ui.fragments;

import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.listener.OnItemChildClickListener;

import butterknife.BindView;
import tysheng.sxbus.R;
import tysheng.sxbus.base.BaseFragment;
import tysheng.sxbus.presenter.impl.StarPresenterImpl;
import tysheng.sxbus.ui.inter.StarView;

/**
 * 收藏
 * Created by Sty
 * Date: 16/8/11 21:41.
 */

public class StarFragment extends BaseFragment<StarPresenterImpl> implements StarView {
    @BindView(R.id.recyclerView)
    RecyclerView mRecyclerView;

    @Override
    protected int getLayoutId() {
        return R.layout.fragment_star;
    }

    @Override
    public void onHiddenChanged(boolean hidden) {
        super.onHiddenChanged(hidden);
        if (!hidden) {
            mPresenter.setNewDataFromRecent();
        }
    }

    @Override
    public RecyclerView getRecyclerView() {
        return mRecyclerView;
    }

    @Override
    protected StarPresenterImpl initPresenter() {
        return new StarPresenterImpl(this);
    }

    @Override
    protected void initData() {
        mRecyclerView.setAdapter(mPresenter.getAdapter());
        mRecyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        mPresenter.setEmptyView();
        mRecyclerView.addOnItemTouchListener(new OnItemChildClickListener() {
            @Override
            public void onSimpleItemChildClick(BaseQuickAdapter baseQuickAdapter, View view, int i) {
                mPresenter.onSimpleItemChildClick(view, i);
            }
        });
        mPresenter.initData();
    }
}
