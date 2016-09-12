package tysheng.sxbus.base;


import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;

import com.baidu.mobstat.StatService;

import butterknife.ButterKnife;
import rx.Subscription;
import rx.subscriptions.CompositeSubscription;

/**
 * Created by shengtianyang on 16/2/22.
 */
public abstract class BaseActivity extends AppCompatActivity {

    private CompositeSubscription mSubscription;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(getLayoutId());
        ButterKnife.bind(this);
        initData(savedInstanceState);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (this.mSubscription != null) {
            this.mSubscription.unsubscribe();
        }
    }

    protected void add(Subscription s) {
        if (this.mSubscription == null) {
            this.mSubscription = new CompositeSubscription();
        }
        this.mSubscription.add(s);
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
