package tysheng.sxbus.net;

import rx.Observable;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

/**
 * Created by Sty
 * Date: 16/9/5 21:23.
 */
public class HttpUtil {
    private static Observable.Transformer sTransformer = new Observable.Transformer() {
        @Override
        public Object call(Object o) {
            return ((Observable) o).observeOn(AndroidSchedulers.mainThread())
                    .subscribeOn(Schedulers.io())
                    .unsubscribeOn(Schedulers.io());
        }
    };

    private static <T> Observable.Transformer<T, T> applySchedulers() {
        return (Observable.Transformer<T, T>) sTransformer;
    }

    public static <T> Observable<T> convert(Observable<T> observable) {
        return observable.observeOn(AndroidSchedulers.mainThread())
                .subscribeOn(Schedulers.io());
    }
}
