package tysheng.sxbus;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.IdRes;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;

import com.roughike.bottombar.BottomBar;
import com.roughike.bottombar.OnTabSelectListener;

import butterknife.BindView;
import tysheng.sxbus.base.BaseActivity;
import tysheng.sxbus.ui.SearchFragment;
import tysheng.sxbus.ui.StarFragment;


public class MainActivity extends BaseActivity implements OnTabSelectListener {
    @BindView(R.id.bottomBar)
    BottomBar mBottomBar;
    //    @BindView(R.id.coordinatorLayout)
//    CoordinatorLayout mCoordinatorLayout;
    private FragmentManager mManager;
    private SearchFragment mSearchFragment;
    private Fragment mCurrentFragment;
    private StarFragment mStarFragment;

    @Override
    public int getLayoutId() {
        return R.layout.activity_main;
    }

    @Override
    public void initData(@Nullable Bundle savedInstanceState) {
//        if (mCurrentFragment == null)
//            mCurrentFragment = SearchFragment.newInstance();
        mManager = getSupportFragmentManager();
//        mManager.beginTransaction()
//                .replace(R.id.frameLayout, mCurrentFragment)
//                .commit();
        mBottomBar.setOnTabSelectListener(this);
    }

    @Override
    public void onTabSelected(@IdRes int tabId) {
//        mManager.popBackStack(null, FragmentManager.POP_BACK_STACK_INCLUSIVE);
        switch (tabId) {
            case R.id.menu_search:
//                if (mSearchFragment == null)
//                    mSearchFragment = SearchFragment.newInstance();
//                if (!(mCurrentFragment instanceof SearchFragment)){
//                    jumpFragment(mCurrentFragment, mSearchFragment, R.id.frameLayout, SearchFragment.class.getName());
//                    mCurrentFragment = mSearchFragment;
//                }
                mManager.beginTransaction()
                        .replace(R.id.frameLayout, SearchFragment.newInstance())
                        .commit();
                break;
            case R.id.menu_star:
//                if (mStarFragment == null)
//                    mStarFragment = StarFragment.newInstance();
//                if (!(mCurrentFragment instanceof StarFragment)){
//                    jumpFragment(mCurrentFragment, mStarFragment, R.id.frameLayout, StarFragment.class.getName());
//                    mCurrentFragment = mStarFragment;
//                }
                mManager.beginTransaction()
                        .replace(R.id.frameLayout, StarFragment.newInstance())
                        .commit();
                break;
            default:
                break;
        }
    }

    @Override
    public void startActivityForResult(Intent intent, int requestCode, Bundle options) {
        super.startActivityForResult(intent, requestCode, options);
        overridePendingTransition(0,0);
    }
}
