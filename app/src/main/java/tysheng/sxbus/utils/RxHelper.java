package tysheng.sxbus.utils;

import rx.Observable;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

/**
 * Created by Sty
 * Date: 16/9/16 09:04.
 */
public class RxHelper {
    /**
     * io to main
     *
     * @param <T>
     * @return
     */
    public static <T> Observable.Transformer<T, T> ioToMain() {
        return new Observable.Transformer<T, T>() {
            @Override
            public Observable<T> call(Observable<T> observable) {
                return observable
                        .subscribeOn(Schedulers.io())
                        .observeOn(AndroidSchedulers.mainThread());
            }
        };
    }
}
