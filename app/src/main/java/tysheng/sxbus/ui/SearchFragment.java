package tysheng.sxbus.ui;

import android.app.ProgressDialog;
import android.support.design.widget.CoordinatorLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.SearchView;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.alibaba.fastjson.JSON;
import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.listener.OnItemChildClickListener;
import com.trello.rxlifecycle.android.FragmentEvent;

import java.util.List;
import java.util.concurrent.TimeUnit;

import butterknife.BindString;
import butterknife.BindView;
import rx.Observable;
import rx.functions.Action0;
import rx.functions.Func2;
import tysheng.sxbus.Constant;
import tysheng.sxbus.R;
import tysheng.sxbus.adapter.SearchAdapter;
import tysheng.sxbus.base.BaseFragment;
import tysheng.sxbus.bean.CallBack;
import tysheng.sxbus.bean.Star;
import tysheng.sxbus.bean.Stars;
import tysheng.sxbus.bean.Status;
import tysheng.sxbus.net.BusRetrofit;
import tysheng.sxbus.presenter.StarUtil;
import tysheng.sxbus.utils.LogUtil;
import tysheng.sxbus.utils.RxHelper;
import tysheng.sxbus.utils.SnackBarUtil;
import tysheng.sxbus.utils.StySubscriber;
import tysheng.sxbus.utils.rxfastcache.RxFastCache;

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
    private List<Star> mRecentList, mStarList;
    private SearchAdapter mAdapter;
    private ProgressDialog mDialog;

    @Override
    protected int getLayoutId() {
        return R.layout.fragment_search;
    }

    @Override
    protected void initData() {
        Observable.zip(RxFastCache.getArray(Constant.RECENT, Star.class),
                RxFastCache.getArray(Constant.STAR, Star.class),
                new Func2<List<Star>, List<Star>, Boolean>() {
                    @Override
                    public Boolean call(List<Star> recent, List<Star> star) {
                        if (recent.size() > 10) {
                            mRecentList = recent.subList(0, 10);
                        } else {
                            mRecentList = recent;
                        }
                        mStarList = star;
                        return true;
                    }
                })
                .compose(this.<Boolean>bindUntilEvent(FragmentEvent.DESTROY))
                .subscribe(new StySubscriber<Boolean>() {
                    @Override
                    public void next(Boolean aBoolean) {
                        doNext();
                    }
                });
    }

    private void doNext() {
        mAdapter = new SearchAdapter(mRecentList);
        if (mRecentList != null && mRecentList.size() != 0) {
            View view = LayoutInflater.from(mActivity).inflate(R.layout.footer_clear, (ViewGroup) getView(), false);
            mAdapter.addFooterView(view);
            (view.findViewById(R.id.textView)).setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    mAdapter.removeAllFooterView();
                    mRecentList.clear();
                    mAdapter.notifyDataSetChanged();
                    StarUtil.saveStarList(Constant.RECENT, mRecentList);
                }
            });
        }
        mRecyclerView.setAdapter(mAdapter);
        mRecyclerView.setLayoutManager(new LinearLayoutManager(mActivity));

        mRecyclerView.addOnItemTouchListener(new OnItemChildClickListener() {
            @Override
            public void SimpleOnItemChildClick(BaseQuickAdapter baseQuickAdapter, View view, int i) {
                switch (view.getId()) {
                    case R.id.textView:
                        startActivity(RunningActivity.newIntent(getContext(), mAdapter.getItem(i).id
                                , mAdapter.getItem(i).lineName + " 前往 " + mAdapter.getItem(i).endStationName)
                        );
                        save(Constant.RECENT, mRecentList, i);
                        break;
                    case R.id.star:
                        ImageView imageView = (ImageView) view;
                        imageView.setImageResource(R.drawable.star_yes);
                        save(Constant.STAR, mStarList, i);
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
        if (mSearchView != null)
            mSearchView.clearFocus();
    }

    private void save(String tag, List<Star> list, int i) {
        boolean flag = true;
        for (Star star : list) {
            if (TextUtils.equals(star.id, mAdapter.getItem(i).id)) {
                flag = false;
            }
        }
        if (flag) {
            list.add(0, mAdapter.getItem(i));
            StarUtil.saveStarList(tag, list);
        }
    }

    private void getBusSimple(String number) {
        BusRetrofit.get()
                .numberToSearch(number)
                .delay(200, TimeUnit.MILLISECONDS)
                .doAfterTerminate(new Action0() {
                    @Override
                    public void call() {
                        mDialog.dismiss();
                    }
                })
                .compose(this.<CallBack>bindUntilEvent(FragmentEvent.DESTROY))
                .compose(RxHelper.<CallBack>ioToMain())
                .subscribe(new StySubscriber<CallBack>() {
                    @Override
                    public void onStart() {
                        mSearchView.clearFocus();
                        if (mAdapter.getFooterLayoutCount() != 0)
                            mAdapter.removeAllFooterView();
                        if (mDialog == null) {
                            mDialog = new ProgressDialog(mActivity);
                            mDialog.setMessage("正在搜索...");
                        }
                        mDialog.show();
                    }

                    @Override
                    public void next(CallBack s) {
                        Status status = JSON.parseObject(s.status, Status.class);

                        if (status.code == 20306) {
                            SnackBarUtil.show(mCoordinatorLayout, "查询的公交线路不存在");
                        } else if (status.code == 0) {
                            Stars stars = JSON.parseObject(s.result, Stars.class);
                            mAdapter.setNewData(stars.result);
                        } else
                            onError(null);
                    }

                    @Override
                    public void onError(Throwable e) {
                        LogUtil.d("search   " + e.getMessage());
                        SnackBarUtil.show(mCoordinatorLayout, searchError);

                    }
                });
    }


}
