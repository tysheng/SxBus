package tysheng.sxbus.utils;

import rx.Subscriber;

/**
 * Created by Sty
 * Date: 16/9/16 09:09.
 */
public abstract class StySubscriber<T> extends Subscriber<T> {

    @Override
    public void onCompleted() {

    }

    @Override
    public void onError(Throwable e) {

    }

    @Override
    public void onNext(T t) {
        next(t);
    }

    public abstract void next(T t);
}
