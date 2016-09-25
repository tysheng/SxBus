package tysheng.sxbus.base;


import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;

import com.baidu.mobstat.StatService;
import com.trello.rxlifecycle.components.support.RxAppCompatActivity;

import java.util.List;

import butterknife.ButterKnife;
import tysheng.sxbus.R;
import tysheng.sxbus.utils.ListUtil;

/**
 * Created by shengtianyang on 16/2/22.
 */
public abstract class BaseActivity extends RxAppCompatActivity {


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(getLayoutId());
        ButterKnife.bind(this);
        initData(savedInstanceState);
    }

    @Override
    protected void onResume() {
        super.onResume();
        StatService.onResume(this);
    }

    @Override
    protected void onPause() {
        super.onPause();
        StatService.onPause(this);
    }

    @Override
    public void finish() {
        super.finish();
        overridePendingTransition(0, 0);
    }

    @Override
    public void startActivityForResult(Intent intent, int requestCode, Bundle options) {
        super.startActivityForResult(intent, requestCode, options);
        overridePendingTransition(0, 0);
    }

    /**
     * 初始化数据
     */
    public abstract void initData(@Nullable Bundle savedInstanceState);

    /**
     * setContentView
     */
    public abstract int getLayoutId();

    /**
     * activity之间的跳转
     *
     * @param clazz    目标activity
     * @param isfinish 是否关闭
     */
    protected void jumpActivity(Class clazz, boolean isfinish) {
        Intent intent = new Intent(this, clazz);
        startActivity(intent);
        if (isfinish) {
            this.finish();
        }
    }

    /**
     * 知乎 fragment 跳转
     *
     * @param from from list
     * @param to   to list
     * @param tag  tab tag
     */
    protected void jumpFragment(List<String> from, List<String> to, String tag) {
        FragmentManager manager = getSupportFragmentManager();
        if (to == null) {
            return;
        }
        FragmentTransaction transaction = manager.beginTransaction();
        if (ListUtil.isEmpty(from)) {
            add(manager, transaction, to, tag);
        } else {
            hide(manager, transaction, from);
            add(manager, transaction, to, tag);
        }
        transaction
                .setCustomAnimations(0, 0, android.R.anim.fade_in, android.R.anim.fade_out)
                .commitNow();
    }

    private void add(FragmentManager manager, FragmentTransaction trans, List<String> tags, String tag) {
        Fragment frag = manager.findFragmentByTag(tags.get(0));
        if (!frag.isAdded())
            trans.add(R.id.frameLayout, frag, tag);
        else
            trans.show(frag);
        if (tags.size() > 1) {
            for (int i = 1; i < tags.size(); i++) {
                Fragment f = manager.findFragmentByTag(tags.get(i));
                trans.hide(f);
            }
        }
    }

    private void hide(FragmentManager manager, FragmentTransaction trans, List<String> tags) {
        for (String tag : tags) {
            Fragment frag = manager.findFragmentByTag(tag);
            trans.hide(frag);
        }
    }

    protected void jumpFragment(Fragment from, Fragment to, String tag) {
        jumpFragment(from, to, R.id.frameLayout, tag);
    }

    /**
     * Fragment之间的切换
     *
     * @param from 当前
     * @param to   目标
     * @param id
     * @param tag
     */
    protected void jumpFragment(Fragment from, Fragment to, int id, String tag) {
        FragmentManager manager = getSupportFragmentManager();
        if (to == null) {
            return;
        }
        FragmentTransaction transaction = manager.beginTransaction();
        if (from == null) {
            transaction.add(id, to, tag);
        } else {
            transaction.hide(from);
            if (to.isAdded()) {
                transaction.show(to);
            } else {
                transaction.add(id, to, tag);
            }
        }
        transaction
                .setCustomAnimations(0, 0, android.R.anim.fade_in, android.R.anim.fade_out)
                .commit();

    }


}
