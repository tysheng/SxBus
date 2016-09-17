package tysheng.sxbus.ui;

import android.support.design.widget.CoordinatorLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.SearchView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.listener.OnItemChildClickListener;
import com.trello.rxlifecycle.android.FragmentEvent;

import butterknife.BindString;
import butterknife.BindView;
import rx.Subscriber;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;
import tysheng.sxbus.Constant;
import tysheng.sxbus.R;
import tysheng.sxbus.adapter.SearchAdapter;
import tysheng.sxbus.base.BaseFragment;
import tysheng.sxbus.bean.BusLinesSimple;
import tysheng.sxbus.bean.Stars;
import tysheng.sxbus.net.BusRetrofit;
import tysheng.sxbus.presenter.StarUtil;
import tysheng.sxbus.utils.KeyboardUtil;
import tysheng.sxbus.utils.LogUtil;
import tysheng.sxbus.utils.SnackBarUtil;

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
    private Stars mStars;
    private SearchAdapter mAdapter;

    @Override
    protected int getLayoutId() {
        return R.layout.fragment_search;
    }

    @Override
    protected void initData() {
        mStars = StarUtil.initStars(Constant.RECENT);
        if (mStars.result.size() > 6) {
            mStars.result = mStars.result.subList(0, 6);
        }
        mAdapter = new SearchAdapter(mStars.result);
        if (mStars.result != null && mStars.result.size() != 0) {
            View view = LayoutInflater.from(mActivity).inflate(R.layout.footer_clear, (ViewGroup) getView(), false);
            mAdapter.addFooterView(view);
            (view.findViewById(R.id.textView)).setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    mAdapter.removeAllFooterView();
                    mStars.result.clear();
                    StarUtil.saveStars(Constant.RECENT, mStars);
                    mAdapter.notifyDataSetChanged();
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
                        mStars.result.add(0, mAdapter.getItem(i));
                        StarUtil.saveStars(Constant.RECENT, mStars);
                        break;
                    case R.id.star:
                        mStars.result.add(mAdapter.getItem(i));
                        ImageView imageView = (ImageView) view;
                        imageView.setImageResource(R.drawable.star_yes);
                        StarUtil.saveStars(Constant.STAR, mStars);
                        break;
                    default:
                        break;
                }
            }
        });
        mSearchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                LogUtil.d(query);
                search(query);
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

    private void getBusSimple(int number) {
        if (mAdapter.getFooterLayoutCount() != 0)
            mAdapter.removeAllFooterView();
        BusRetrofit.get().numberToSearch(number)
                .compose(this.<BusLinesSimple>bindUntilEvent(FragmentEvent.DESTROY))
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeOn(Schedulers.io())
                .subscribe(new Subscriber<BusLinesSimple>() {
                    @Override
                    public void onCompleted() {

                    }

                    @Override
                    public void onError(Throwable e) {
                        LogUtil.d("search   " + e.getMessage());
                        SnackBarUtil.show(mCoordinatorLayout, searchError);

                    }

                    @Override
                    public void onNext(BusLinesSimple busLinesSimple) {
                        LogUtil.d(busLinesSimple.status.code);
                        mAdapter.setNewData(busLinesSimple.result.result);
                    }
                });
    }

    private void search(String s) {
        KeyboardUtil.hide(mActivity);
        int number;
        number = Integer.valueOf(s);
        getBusSimple(number);
    }

}
