package tysheng.sxbus.presenter.base;

/**
 * Created by tysheng
 * Date: 2017/2/28 16:46.
 * Email: tyshengsx@gmail.com
 */

public interface RetrofitErrorInterface extends BaseFragmentPresenter {
    void onNetworkError(Throwable t);

    void onCodeError();

    void onNetworkTerminate();
}
