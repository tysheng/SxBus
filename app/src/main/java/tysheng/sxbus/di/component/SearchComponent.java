package tysheng.sxbus.di.component;

import dagger.Component;
import tysheng.sxbus.di.PerPresenter;
import tysheng.sxbus.di.module.SearchModule;
import tysheng.sxbus.presenter.impl.SearchPresenterImpl;
import tysheng.sxbus.presenter.inter.SearchPresenter;

/**
 * Created by tysheng
 * Date: 2017/5/5 10:00.
 * Email: tyshengsx@gmail.com
 */
@PerPresenter
@Component(dependencies = UniverseComponent.class, modules = SearchModule.class)
public interface SearchComponent {
    SearchPresenter getSearchPresenter();

    void inject(SearchPresenterImpl i);

}
