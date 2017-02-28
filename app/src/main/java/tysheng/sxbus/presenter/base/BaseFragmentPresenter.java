package tysheng.sxbus.presenter.base;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.FragmentManager;

import com.trello.rxlifecycle2.LifecycleTransformer;

/**
 * Created by tysheng
 * Date: 2017/2/28 15:48.
 * Email: tyshengsx@gmail.com
 */

public interface BaseFragmentPresenter {
    <T> LifecycleTransformer<T> bindUntilDestroyView();

    void startActivity(Intent intent);

    void startActivityForResult(Intent intent, int requestCode);

    Context getContext();

    FragmentManager getChildFragmentManager();

    void setArgs(Bundle bundle);

    void initData();

    void onDestroy();

    Activity getActivity();
}
