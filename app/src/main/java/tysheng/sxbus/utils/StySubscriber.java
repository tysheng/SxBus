package tysheng.sxbus.utils;

import org.reactivestreams.Subscriber;
import org.reactivestreams.Subscription;

/**
 * Created by Sty
 * Date: 16/9/16 09:09.
 */
public abstract class StySubscriber<T> implements Subscriber<T> {

    @Override
    public void onNext(T t) {
        LogUtil.d(t.toString());
        next(t);
        onTerminate();
    }

    @Override
    public void onComplete() {
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
    public void onSubscribe(Subscription s) {

    }

    public abstract void next(T t);
}
