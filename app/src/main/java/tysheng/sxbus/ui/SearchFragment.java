package tysheng.sxbus.ui;

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
import java.util.concurrent.TimeUnit;

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
import tysheng.sxbus.dao.StarDao;
import tysheng.sxbus.db.DbUtil;
import tysheng.sxbus.db.StarHelper;
import tysheng.sxbus.net.BusRetrofit;
import tysheng.sxbus.utils.JsonUtil;
import tysheng.sxbus.utils.LogUtil;
import tysheng.sxbus.utils.RxHelper;
import tysheng.sxbus.utils.SnackBarUtil;
import tysheng.sxbus.utils.StyObserver;

/**
 * Created by Sty
 * Date: 16/8/10 22:52.
 */
public class SearchFragment extends BaseFragment {

    @BindString(R.string.recent)
    String mRecent;
    @BindString(R.string.result)
    String mResult;
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
    private StarHelper mHelper;

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
                mRecentList = getRecentList();
                mAdapter.setNewData(mRecentList);
                if (mRecentList != null && mRecentList.size() != 0 && mAdapter.getFooterLayoutCount() == 0) {
                    View view = LayoutInflater.from(getContext()).inflate(R.layout.footer_clear, (ViewGroup) getView(), false);
                    mAdapter.addFooterView(view);
                    (view.findViewById(R.id.textView)).setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            mAdapter.removeAllFooterView();
                            mRecentList.clear();
                            mAdapter.notifyDataSetChanged();
                            mHelper.delete(getRecentList());
                        }
                    });
                }
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
        mHelper = DbUtil.getDriverHelper();
        mRecentList = getRecentList();
        doNext();
    }


    List<Star> getRecentList() {
        return mHelper.queryBuilder()
                .where(StarDao.Properties.TableName.eq(Constant.RECENT))
                .list();
    }

    private void doNext() {
        mAdapter = new StarAdapter(1, mRecentList);
        if (mRecentList != null && mRecentList.size() != 0) {
            View view = LayoutInflater.from(getContext()).inflate(R.layout.footer_clear, (ViewGroup) getView(), false);
            mAdapter.addFooterView(view);
            (view.findViewById(R.id.textView)).setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    mAdapter.removeAllFooterView();
                    mRecentList.clear();
                    mAdapter.notifyDataSetChanged();
                    mHelper.delete(getRecentList());
                }
            });
        }
        mRecyclerView.setAdapter(mAdapter);
        mRecyclerView.setLayoutManager(new LinearLayoutManager(getContext()));

        mRecyclerView.addOnItemTouchListener(new OnItemChildClickListener() {
            @Override
            public void SimpleOnItemChildClick(BaseQuickAdapter baseQuickAdapter, View view, int i) {
                switch (view.getId()) {
                    case R.id.number:
                    case R.id.textView:
                        ((FragmentCallback) getActivity()).handleCallbackNew(new FragCallback(Constant.WHAT_SEARCH, mAdapter.getItem(i).id,
                                mAdapter.getItem(i).lineName + " 前往 " + mAdapter.getItem(i).endStationName));
                        Star star = mAdapter.getItem(i);
                        star.setTableName(Constant.RECENT);
                        mHelper.saveOrUpdate(star);
                        break;
                    case R.id.star:
                        Star star1 = mAdapter.getItem(i);
                        star1.isStar = true;
                        star1.setTableName(Constant.STAR);
                        Star uni = mHelper.queryBuilder()
                                .where(StarDao.Properties.Id.eq(star1.getLocalLineId()))
                                .unique();
                        if (uni == null)
                            mHelper.saveOrUpdate(star1);
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
        BusRetrofit.get()
                .numberToSearch(number)
                .delay(200, TimeUnit.MICROSECONDS)
                .compose(this.<CallBack>bindToLifecycle())
                .compose(RxHelper.<CallBack>ioToMain())
                .subscribe(new StyObserver<CallBack>() {
                    @Override
                    public void onTerminate() {
                        super.onTerminate();
                        mDialog.dismiss();
                    }

                    @Override
                    public void next(CallBack s) {
                        LogUtil.d(s.toString());
                        Status status = JsonUtil.parse(s.status, Status.class);
                        if (status.code == 20306) {
                            showSnackBar("查询的公交线路不存在", false);
                        } else if (status.code == 0) {
                            Stars stars = JsonUtil.parse(s.result, Stars.class);
                            mAdapter.setNewData(stars.result);
                        }
                    }

                    @Override
                    public void onError(Throwable e) {
                        super.onError(e);
                        showSnackBar(searchError, false);
                    }
                });

    }

    public void showSnackBar(String msg, boolean isLong) {
        SnackBarUtil.show(mCoordinatorLayout, msg,
                isLong ? Snackbar.LENGTH_LONG : Snackbar.LENGTH_SHORT);
    }

}
