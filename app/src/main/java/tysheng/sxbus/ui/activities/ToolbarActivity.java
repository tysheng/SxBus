package tysheng.sxbus.ui.activities;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.view.View;

import tysheng.sxbus.R;
import tysheng.sxbus.base.BaseActivityV2;
import tysheng.sxbus.databinding.ActivityToolbarBinding;
import tysheng.sxbus.presenter.base.AbstractPresenter;

/**
 * Created by tysheng
 * Date: 2017/3/11 15:57.
 * Email: tyshengsx@gmail.com
 */

public abstract class ToolbarActivity<P extends AbstractPresenter> extends BaseActivityV2<P, ActivityToolbarBinding> implements Toolbar.OnMenuItemClickListener {
    @Override
    public void initData(@Nullable Bundle savedInstanceState) {
        super.initData(savedInstanceState);
        binding.toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        binding.toolbar.setOnMenuItemClickListener(this);
    }

    protected void inflateMenu(int res) {
        binding.toolbar.inflateMenu(res);
    }

    protected void replaceFragment(Fragment fragment) {
        getSupportFragmentManager()
                .beginTransaction()
                .replace(R.id.frameLayout, fragment)
                .commit();
    }

    @Override
    public int getLayoutId() {
        return R.layout.activity_toolbar;
    }

    public void setTitle(String title) {
        binding.toolbar.setTitle(title);
    }

    public void setSubtitle(final String subtitle) {
        binding.toolbar.postDelayed(new Runnable() {
            @Override
            public void run() {
                binding.toolbar.setSubtitle(subtitle);
            }
        }, 300);
    }

    @Override
    public boolean onMenuItemClick(MenuItem item) {
        return false;
    }
}
