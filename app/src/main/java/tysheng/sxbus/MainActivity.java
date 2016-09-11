package tysheng.sxbus;

import android.os.Bundle;
import android.support.annotation.IdRes;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;

import com.roughike.bottombar.BottomBar;
import com.roughike.bottombar.OnTabSelectListener;

import butterknife.BindView;
import tysheng.sxbus.base.BaseActivity;
import tysheng.sxbus.ui.MoreFragment;
import tysheng.sxbus.ui.SearchFragment;
import tysheng.sxbus.ui.StarFragment;


public class MainActivity extends BaseActivity implements OnTabSelectListener {
    @BindView(R.id.bottomBar)
    BottomBar mBottomBar;
    private FragmentManager mManager;


    @Override
    public int getLayoutId() {
        return R.layout.activity_main;
    }

    @Override
    public void initData(@Nullable Bundle savedInstanceState) {
        mManager = getSupportFragmentManager();
        mBottomBar.setOnTabSelectListener(this);
    }

    @Override
    public void onTabSelected(@IdRes int tabId) {
        switch (tabId) {
            case R.id.menu_search:
                mManager.beginTransaction()
                        .replace(R.id.frameLayout, SearchFragment.newInstance())
                        .commit();
                break;
            case R.id.menu_star:
                mManager.beginTransaction()
                        .replace(R.id.frameLayout, StarFragment.newInstance())
                        .commit();
                break;
            case R.id.menu_more:
                mManager.beginTransaction()
                        .replace(R.id.frameLayout, Fragment.instantiate(MainActivity.this, MoreFragment.class.getName()))
                        .commit();
            default:
                break;
        }
    }

    @Override
    public void onBackPressed() {
        if (mBottomBar.getCurrentTabPosition() == 0) {
            super.onBackPressed();
        } else {
            mBottomBar.selectTabAtPosition(0);
        }

    }


}
