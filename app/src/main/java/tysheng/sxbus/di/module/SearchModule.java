package tysheng.sxbus.di.module;

import android.app.ProgressDialog;

import dagger.Module;
import dagger.Provides;
import tysheng.sxbus.di.PerPresenter;
import tysheng.sxbus.model.impl.SearchDbModelImplImpl;
import tysheng.sxbus.model.impl.SearchModelImplImpl;
import tysheng.sxbus.presenter.inter.SearchPresenter;

/**
 * Created by tysheng
 * Date: 2017/5/5 10:01.
 * Email: tyshengsx@gmail.com
 */

@Module
public class SearchModule {
    private SearchPresenter mSearchPresenter;

    public SearchModule(SearchPresenter searchPresenter) {
        mSearchPresenter = searchPresenter;
    }

    @PerPresenter
    @Provides
    public SearchPresenter provideSearchPresenter() {
        return mSearchPresenter;
    }

    @PerPresenter
    @Provides
    public ProgressDialog provideProgressDialog(SearchPresenter presenter) {
        ProgressDialog dialog = new ProgressDialog(presenter.getContext());
        dialog.setMessage("正在搜索...");
        return dialog;
    }

    @PerPresenter
    @Provides
    public SearchDbModelImplImpl provideSearchDbModelImplImpl() {
        return new SearchDbModelImplImpl();
    }

    @PerPresenter
    @Provides
    public SearchModelImplImpl provideSearchModelImplImpl(SearchPresenter presenter) {
        return new SearchModelImplImpl(presenter);
    }

}
