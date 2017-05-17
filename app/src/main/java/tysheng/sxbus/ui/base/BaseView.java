package tysheng.sxbus.ui.base;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.support.v4.app.FragmentManager;
import android.view.View;

import com.trello.rxlifecycle2.LifecycleTransformer;

/**
 * Created by tysheng
 * Date: 2017/2/28 15:51.
 * Email: tyshengsx@gmail.com
 */

public interface BaseView {
    <T> LifecycleTransformer<T> bindUntilDestroyView();

    Activity getActivity();

    Context getContext();

    void startActivityForResult(Intent intent, int requestCode);

    void startActivity(Intent intent);

    FragmentManager getChildFragmentManager();

    View getRootView();

}
