package tysheng.sxbus.ui.search;

import java.util.List;
import java.util.concurrent.TimeUnit;

import tysheng.sxbus.base.BasePresenter;
import tysheng.sxbus.bean.CallBack;
import tysheng.sxbus.bean.Star;
import tysheng.sxbus.net.BusRetrofit;
import tysheng.sxbus.utils.RxHelper;
import tysheng.sxbus.utils.StyObserver;

/**
 * Created by tysheng
 * Date: 2017/1/3 14:41.
 * Email: tyshengsx@gmail.com
 */

class SearchPresenter implements BasePresenter {
    private SearchView<CallBack> mView;
    private DbModule mDbModule;

    SearchPresenter(SearchView<CallBack> view) {
        mView = view;
        mDbModule = new DbModule();
    }

    @Override
    public void onDestroy() {
        mDbModule.onDestroy();
        mView = null;
    }

    List<Star> getRecentList() {
        return mDbModule.getRecentList();
    }

    void onItemClick(Star item) {
        mDbModule.onItemClick(item);
    }

    void onItemClickCollect(Star item) {
        mDbModule.onItemClickCollect(item);
    }

    void delete() {
        mDbModule.delete();
    }

    void getBusSimple(String number) {
        BusRetrofit.get()
                .numberToSearch(number)
                .delay(200, TimeUnit.MICROSECONDS)
                .compose(mView.<CallBack>bind2Lifecycle())
                .compose(RxHelper.<CallBack>ioToMain())
                .subscribe(new StyObserver<CallBack>() {
                    @Override
                    public void onTerminate() {
                        super.onTerminate();
                        mView.onTerminate();
                    }

                    @Override
                    public void next(CallBack s) {
                        mView.onSuccess(s);
                    }

                    @Override
                    public void onError(Throwable e) {
                        super.onError(e);
                        mView.onError(e);
                    }
                });

    }
}
