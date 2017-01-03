package tysheng.sxbus.base;

/**
 * Created by tysheng
 * Date: 2017/1/3 14:30.
 * Email: tyshengsx@gmail.com
 */

public interface BaseView<T> {
    void onSuccess(T t);

    void onError(Throwable e);

    void onTerminate();
}
