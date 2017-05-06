package tysheng.sxbus.ui.activities;

import android.content.Intent;
import android.content.pm.ShortcutInfo;
import android.content.pm.ShortcutManager;
import android.graphics.drawable.Icon;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.annotation.RequiresApi;
import android.support.v4.app.Fragment;

import java.util.Arrays;

import tysheng.sxbus.R;
import tysheng.sxbus.base.BaseActivityV2;
import tysheng.sxbus.base.BaseFragmentV2;
import tysheng.sxbus.bean.FragCallback;
import tysheng.sxbus.databinding.ActivityMainBinding;
import tysheng.sxbus.presenter.impl.MainPresenterImpl;
import tysheng.sxbus.ui.inter.MainView;
import tysheng.sxbus.view.PositionBottomNavigationView;

public class MainActivity extends BaseActivityV2<MainPresenterImpl, ActivityMainBinding> implements MainView, PositionBottomNavigationView.onPositionSelectedListener, BaseFragmentV2.FragmentCallback {
    private int shortcutId;

    @Override
    public int getLayoutId() {
        return R.layout.activity_main;
    }

    @Override
    protected MainPresenterImpl initPresenter() {
        return new MainPresenterImpl(this, getSupportFragmentManager());
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        setTheme(R.style.AppTheme_Main);
        super.onCreate(savedInstanceState);
        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.N_MR1) {
            ShortcutManager shortcutManager;
            shortcutManager = getSystemService(ShortcutManager.class);
            try {
                ShortcutInfo shortcutInfo = getShortcutInfo(1, "shortcut_query", R.mipmap.shortcut_search, getString(R.string.menu_search));
                ShortcutInfo shortcutInfo2 = getShortcutInfo(2, "shortcut_more", R.mipmap.shortcut_menu, getString(R.string.menu_more));
                shortcutManager.removeAllDynamicShortcuts();
                shortcutManager.setDynamicShortcuts(Arrays.asList(shortcutInfo, shortcutInfo2));
            } catch (IllegalStateException ignored) {

            }
        }
    }

    @RequiresApi(api = Build.VERSION_CODES.N_MR1)
    @NonNull
    private ShortcutInfo getShortcutInfo(int pos, String shortcutId, int iconRes, String label) {
        Intent intent = new Intent(Intent.ACTION_MAIN, Uri.EMPTY, this, MainActivity.class).setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
        intent.putExtra("shortcut", pos);
        Icon icon = Icon.createWithResource(this, iconRes);
        return new ShortcutInfo.Builder(this, shortcutId)
                .setShortLabel(label)
                .setLongLabel(label)
                .setIcon(icon)
                .setIntent(intent)
                .build();
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        mPresenter.onSaveInstanceState(outState);
    }

    @Override
    public void initData(@Nullable Bundle savedInstanceState) {
        super.initData(savedInstanceState);
        mPresenter.restorePosition(savedInstanceState);
        binding.bottom.registerIds(R.id.menu_star, R.id.menu_search, R.id.menu_more);
        binding.bottom.setOnPositionSelectedListener(this);

        if (savedInstanceState == null) {
            int pos = getIntent().getIntExtra("shortcut", 0);
            if (pos == 1) {
                shortcutId = R.id.menu_search;
            } else if (pos == 2) {
                shortcutId = R.id.menu_more;
            }
        }

    }

    @Override
    protected void onResume() {
        super.onResume();
        if (shortcutId != 0) {
            binding.bottom.setSelectedItemId(shortcutId);
            shortcutId = 0;
        }
    }

    @Override
    public void onBackPressed() {
        if (mPresenter.onBackPressed()) {
            super.onBackPressed();
        }
    }

    @Override
    public void setCurrentPosition(int pos) {
        binding.bottom.setCurrentPosition(pos);
    }

    @Override
    public void onPositionSelected(int position) {
        mPresenter.onPositionSelected(position);
    }

    @Override
    public void jumpFragment(Fragment from, Fragment to, String tag) {
        super.jumpFragment(from, to, tag);
    }

    @Override
    public void onPositionReselected(int position) {

    }

    @Override
    protected void onDestroy() {
        mPresenter.onDestroy();
        super.onDestroy();
    }

    @Override
    public void handleCallbackNew(FragCallback callback) {
        mPresenter.preToCur(callback.what, callback);
    }
}
