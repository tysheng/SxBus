package tysheng.sxbus.presenter.inter;

import android.view.View;

import java.util.List;

import tysheng.sxbus.adapter.StarAdapter;
import tysheng.sxbus.bean.Star;
import tysheng.sxbus.presenter.base.RetrofitErrorInterface;

/**
 * Created by tysheng
 * Date: 2017/2/28 16:14.
 * Email: tyshengsx@gmail.com
 */

public interface SearchPresenterInterface extends RetrofitErrorInterface {

    void setNewData(List<Star> stars);

    void setNewDataFromRecent();

    void initFooter();

    StarAdapter getAdapter();

    void onSimpleItemChildClick(View view, int i);
}
