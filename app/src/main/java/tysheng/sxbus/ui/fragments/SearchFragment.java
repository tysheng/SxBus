package tysheng.sxbus.ui.fragments;

import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.SearchView;
import android.text.TextUtils;
import android.view.View;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.listener.OnItemChildClickListener;

import tysheng.sxbus.R;
import tysheng.sxbus.base.BaseFragmentV2;
import tysheng.sxbus.databinding.FragmentSearchBinding;
import tysheng.sxbus.presenter.impl.SearchPresenterImpl;

/**
 * Created by Sty
 * Date: 16/8/10 22:52.
 */
public class SearchFragment extends BaseFragmentV2<SearchPresenterImpl, FragmentSearchBinding> implements tysheng.sxbus.ui.inter.SearchView {

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
            if (TextUtils.isEmpty(binding.searchView.getQuery())) {
                mPresenter.setNewDataFromRecent();
            }
            binding.searchView.post(new Runnable() {
                @Override
                public void run() {
                    binding.searchView.clearFocus();
                }
            });
        }
    }

    @Override
    public View getCoordinatorLayout() {
        return binding.getRoot();
    }

    @Override
    protected void initData() {
        mPresenter = new SearchPresenterImpl(this);
        mPresenter.initData();
        binding.recyclerView.setAdapter(mPresenter.getAdapter());
        binding.recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        binding.recyclerView.addOnItemTouchListener(new OnItemChildClickListener() {
            @Override
            public void onSimpleItemChildClick(BaseQuickAdapter baseQuickAdapter, View view, int i) {
                mPresenter.onSimpleItemChildClick(view, i);
            }
        });
        binding.searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
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
        binding.searchView.onActionViewExpanded();
        binding.searchView.clearFocus();
    }

    @Override
    protected SearchPresenterImpl initPresenter() {
        return new SearchPresenterImpl(this);
    }

    @Override
    public void onResume() {
        super.onResume();
        if (binding.searchView != null) {
            binding.searchView.clearFocus();
        }
    }

    private void getBusSimple(String number) {
        binding.searchView.clearFocus();
        mPresenter.getBusSimple(number);
    }

}
