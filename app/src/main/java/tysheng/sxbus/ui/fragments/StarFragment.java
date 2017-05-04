package tysheng.sxbus.ui.fragments;

import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.listener.OnItemChildClickListener;

import tysheng.sxbus.R;
import tysheng.sxbus.base.BaseFragmentV2;
import tysheng.sxbus.databinding.FragmentStarBinding;
import tysheng.sxbus.presenter.impl.StarPresenterImpl;
import tysheng.sxbus.ui.inter.StarView;

/**
 * 收藏
 * Created by Sty
 * Date: 16/8/11 21:41.
 */

public class StarFragment extends BaseFragmentV2<StarPresenterImpl, FragmentStarBinding> implements StarView {

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
        return binding.recyclerView;
    }

    @Override
    protected StarPresenterImpl initPresenter() {
        return new StarPresenterImpl(this);
    }

    @Override
    protected void initData() {
        binding.recyclerView.setAdapter(mPresenter.getAdapter());
        binding.recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        mPresenter.setEmptyView();
        binding.recyclerView.addOnItemTouchListener(new OnItemChildClickListener() {
            @Override
            public void onSimpleItemChildClick(BaseQuickAdapter baseQuickAdapter, View view, int i) {
                mPresenter.onSimpleItemChildClick(view, i);
            }
        });
        mPresenter.initData();
    }
}
