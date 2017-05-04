package tysheng.sxbus.utils;

import io.reactivex.subscribers.ResourceSubscriber;

/**
 * Created by Sty
 * Date: 16/9/16 09:09.
 */
public class TyObserver<T> extends ResourceSubscriber<T> {


    @Override
    public void onNext(T t) {
        next(t);
        onTerminate();
    }

    public void next(T t) {

    }

    public void onTerminate() {

    }

    @Override
    public void onError(Throwable t) {
        if (t != null)
            LogUtil.d(t.getMessage());
        onTerminate();
    }

    @Override
    public void onComplete() {

    }
}
