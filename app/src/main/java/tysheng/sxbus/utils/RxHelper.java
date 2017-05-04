package tysheng.sxbus.utils;

import org.reactivestreams.Publisher;

import io.reactivex.Flowable;
import io.reactivex.FlowableTransformer;
import io.reactivex.Observable;
import io.reactivex.ObservableSource;
import io.reactivex.ObservableTransformer;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.functions.Function;
import io.reactivex.schedulers.Schedulers;
import tysheng.sxbus.bean.CallBack;

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
    public static <T> FlowableTransformer<T, T> flowableIoToMain() {
        return new FlowableTransformer<T, T>() {
            @Override
            public Publisher<T> apply(Flowable<T> observable) {
                return observable
                        .subscribeOn(Schedulers.io())
                        .observeOn(AndroidSchedulers.mainThread());
            }
        };
    }

    public static FlowableTransformer<String, CallBack> stringIoToCallback() {
        return new FlowableTransformer<String, CallBack>() {
            @Override
            public Publisher<CallBack> apply(Flowable<String> observable) {
                return observable
                        .map(new Function<String, CallBack>() {
                            @Override
                            public CallBack apply(String string) throws Exception {

                                return JsonUtil.parse(string, CallBack.class);
                            }
                        });
            }
        };
    }


    public static <T> ObservableTransformer<T, T> ioToMain() {
        return new ObservableTransformer<T, T>() {
            @Override
            public ObservableSource<T> apply(Observable<T> observable) {
                return observable
                        .subscribeOn(Schedulers.io())
                        .observeOn(AndroidSchedulers.mainThread());
            }
        };
    }
}
