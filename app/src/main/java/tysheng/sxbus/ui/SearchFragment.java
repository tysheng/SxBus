package tysheng.sxbus.ui;

import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.KeyEvent;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.listener.OnItemChildClickListener;

import java.util.ArrayList;

import butterknife.BindString;
import butterknife.BindView;
import butterknife.OnClick;
import rx.Subscriber;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;
import tysheng.sxbus.Constant;
import tysheng.sxbus.R;
import tysheng.sxbus.adapter.SearchAdapter;
import tysheng.sxbus.base.BaseFragment;
import tysheng.sxbus.bean.BusLinesSimple;
import tysheng.sxbus.bean.Star;
import tysheng.sxbus.bean.Stars;
import tysheng.sxbus.net.BusService;
import tysheng.sxbus.net.RetrofitHelper;
import tysheng.sxbus.utils.KeyboardUtil;
import tysheng.sxbus.utils.LogUtil;
import tysheng.sxbus.utils.fastcache.FastCache;

/**
 * Created by Sty
 * Date: 16/8/10 22:52.
 */
public class SearchFragment extends BaseFragment {
    @BindView(R.id.editText)
    EditText mEditText;
    @BindString(R.string.recent)
    String mRecent;
    @BindString(R.string.result)
    String mResult;
    @BindView(R.id.recyclerView)
    RecyclerView mRecyclerView;
    private Stars mStars;

    private SearchAdapter mAdapter;

    public static SearchFragment newInstance() {
        return new SearchFragment();
    }


    @Override
    protected int getLayoutId() {
        return R.layout.fragment_bus_simple;
    }

    @Override
    protected void initData() {
        try {
            mStars = FastCache.get(Constant.STAR, Stars.class);
        } catch (Exception e) {
            e.printStackTrace();
        }
        if (mStars == null) {
            mStars = new Stars();
            mStars.result = new ArrayList<>();
        }


        mAdapter = new SearchAdapter(null);
        mRecyclerView.setAdapter(mAdapter);
        mRecyclerView.setLayoutManager(new LinearLayoutManager(mActivity));

        mRecyclerView.addOnItemTouchListener(new OnItemChildClickListener() {
            @Override
            public void SimpleOnItemChildClick(BaseQuickAdapter baseQuickAdapter, View view, int i) {
                switch (view.getId()) {
                    case R.id.textView:
                        startActivity(RunningActivity.newIntent(getContext(), mAdapter.getItem(i).id));
                        break;
                    case R.id.star:
                        addToStar(mAdapter.getItem(i));
                        ImageView imageView = (ImageView) view;
                        imageView.setImageResource(R.drawable.collect_yes);
                        break;
                    default:
                        break;
                }
            }
        });
        mEditText.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                if (actionId == EditorInfo.IME_ACTION_SEARCH) {
                    search();
                    return true;
                }
                return false;
            }
        });

    }

    private void addToStar(Star star) {
        mStars.result.add(star);
    }

    @Override
    public void onStop() {
        super.onStop();
        if (mStars != null && mStars.result.size() != 0)
            FastCache.putAsync(Constant.STAR, mStars)
                    .subscribe(new Subscriber<Boolean>() {
                        @Override
                        public void onCompleted() {

                        }

                        @Override
                        public void onError(Throwable e) {

                        }

                        @Override
                        public void onNext(Boolean aBoolean) {

                        }
                    });
    }

    private void getBusSimple(int number) {
        RetrofitHelper.get()
                .create(BusService.class)
                .getBusLinesSimple(number)
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeOn(Schedulers.io())
                .subscribe(new Subscriber<BusLinesSimple>() {
                    @Override
                    public void onCompleted() {

                    }

                    @Override
                    public void onError(Throwable e) {
                        LogUtil.d("search   " + e.getMessage());

                    }

                    @Override
                    public void onNext(BusLinesSimple busLinesSimple) {
                        mAdapter.setNewData(busLinesSimple.result.result);
                    }
                });
    }


    @OnClick(R.id.search)
    public void onClick() {
        search();
    }

    private void search() {
        KeyboardUtil.hide(mActivity);
        int number = Integer.valueOf(mEditText.getText().toString());
        getBusSimple(number);
    }

}
