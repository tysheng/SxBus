package tysheng.sxbus.ui.search;

import android.app.ProgressDialog;
import android.support.design.widget.CoordinatorLayout;
import android.support.design.widget.Snackbar;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.SearchView;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.listener.OnItemChildClickListener;

import java.util.List;

import butterknife.BindString;
import butterknife.BindView;
import tysheng.sxbus.Constant;
import tysheng.sxbus.R;
import tysheng.sxbus.adapter.StarAdapter;
import tysheng.sxbus.base.BaseFragment;
import tysheng.sxbus.bean.CallBack;
import tysheng.sxbus.bean.FragCallback;
import tysheng.sxbus.bean.Star;
import tysheng.sxbus.bean.Stars;
import tysheng.sxbus.bean.Status;
import tysheng.sxbus.utils.JsonUtil;
import tysheng.sxbus.utils.ListUtil;
import tysheng.sxbus.utils.LogUtil;
import tysheng.sxbus.utils.SnackBarUtil;

/**
 * Created by Sty
 * Date: 16/8/10 22:52.
 */
public class SearchFragment extends BaseFragment implements tysheng.sxbus.ui.search.SearchView<CallBack> {
    @BindString(R.string.search_error)
    String searchError;
    @BindView(R.id.recyclerView)
    RecyclerView mRecyclerView;
    @BindView(R.id.coordinatorLayout)
    CoordinatorLayout mCoordinatorLayout;
    @BindView(R.id.searchView)
    SearchView mSearchView;
    private List<Star> mRecentList;
    private StarAdapter mAdapter;
    private ProgressDialog mDialog;
    private SearchPresenter mPresenter;

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
                mAdapter.setNewData(mRecentList = mPresenter.getRecentList());
                initFooter();
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
    protected void initData() {
        mPresenter = new SearchPresenter(this);
        init();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        mPresenter.onDestroy();
    }

    private void init() {
        initAdapter();
        mRecyclerView.setAdapter(mAdapter);
        mRecyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        mRecyclerView.addOnItemTouchListener(new OnItemChildClickListener() {
            @Override
            public void onSimpleItemChildClick(BaseQuickAdapter baseQuickAdapter, View view, int i) {
                switch (view.getId()) {
                    case R.id.number:
                    case R.id.textView:
                        ((FragmentCallback) getActivity()).handleCallbackNew(new FragCallback(Constant.WHAT_SEARCH, mAdapter.getItem(i).id,
                                mAdapter.getItem(i).lineName + " 前往 " + mAdapter.getItem(i).endStationName));
                        mPresenter.onItemClick(mAdapter.getItem(i));
                        break;
                    case R.id.star:
                        mPresenter.onItemClickCollect(mAdapter.getItem(i));
                        mAdapter.notifyItemChanged(i);
                        break;
                    default:
                        break;
                }
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

    private void initAdapter() {
        mAdapter = new StarAdapter(mRecentList = mPresenter.getRecentList());
        initFooter();
    }

    private void initFooter() {
        if (!ListUtil.isEmpty(mRecentList) && mAdapter.getFooterLayoutCount() == 0) {
            View view = LayoutInflater.from(getContext()).inflate(R.layout.footer_clear, (ViewGroup) getView(), false);
            mAdapter.addFooterView(view);
            view.findViewById(R.id.textView).setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    mAdapter.removeAllFooterView();
                    mRecentList.clear();
                    mAdapter.notifyDataSetChanged();
                    mPresenter.delete();
                }
            });
        }
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
        if (mAdapter.getFooterLayoutCount() != 0)
            mAdapter.removeAllFooterView();
        if (mDialog == null) {
            mDialog = new ProgressDialog(getContext());
            mDialog.setMessage("正在搜索...");
        }
        mDialog.show();
        mPresenter.getBusSimple(this, number);
    }

    @Override
    public void onSuccess(CallBack callBack) {
        LogUtil.d(callBack.toString());
        Status status = JsonUtil.parse(callBack.status, Status.class);
        if (status.code == 20306) {
            SnackBarUtil.show(mCoordinatorLayout, "查询的公交线路不存在", Snackbar.LENGTH_SHORT);
        } else if (status.code == 0) {
            mAdapter.setNewData(JsonUtil.parse(callBack.result, Stars.class).result);
        }
    }

    @Override
    public void onError(Throwable e) {
        SnackBarUtil.show(mCoordinatorLayout, searchError, Snackbar.LENGTH_SHORT);
    }

    @Override
    public void onTerminate() {
        mDialog.dismiss();
    }
}
