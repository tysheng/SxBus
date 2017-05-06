package tysheng.sxbus.ui.activities;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.util.Log;
import android.view.MenuItem;

import com.baidu.mapapi.map.offline.MKOLUpdateElement;
import com.baidu.mapapi.map.offline.MKOfflineMap;
import com.baidu.mapapi.map.offline.MKOfflineMapListener;

import java.util.ArrayList;
import java.util.Locale;

import tysheng.sxbus.Constant;
import tysheng.sxbus.R;
import tysheng.sxbus.bean.MapInfo;
import tysheng.sxbus.bean.Stations;
import tysheng.sxbus.presenter.base.AbstractPresenter;
import tysheng.sxbus.ui.fragments.MapFragment;
import tysheng.sxbus.utils.SPHelper;

import static tysheng.sxbus.Constant.BIKE;
import static tysheng.sxbus.Constant.BUS;

/**
 * Created by tysheng
 * Date: 2017/5/5 14:47.
 * Email: tyshengsx@gmail.com
 */

public class MapActivity extends ToolbarActivity<AbstractPresenter> implements MKOfflineMapListener {
    private MapFragment mMapFragment;
    private MKOfflineMap mOffline;
    private ProgressDialog progressBar;

    public static void startMap(Context context, int type, ArrayList<? extends MapInfo> runningList, ArrayList<? extends MapInfo> totalStations, Stations latLng) {
        if (totalStations != null) {
            Intent intent = new Intent(context, MapActivity.class);
            intent.putExtra("0", runningList);
            intent.putExtra("1", totalStations);
            intent.putExtra("2", latLng);
            intent.putExtra("-1", type);
            context.startActivity(intent);
        }
    }

    private void showFragment() {
        Intent intent = getIntent();
        int type = intent.getIntExtra("-1", BUS);
        mMapFragment = MapFragment.newInstance(type, intent.getParcelableArrayListExtra("0"),
                intent.getParcelableArrayListExtra("1"),
                intent.getParcelableExtra("2"));
        if (type == BUS) {
            setTitle("查看定位");
        } else if (type == BIKE) {
            setTitle("公共自行车");
        }
        setSubtitle(getString(R.string.locating));
        replaceFragment(mMapFragment);
    }

    @Override
    public void initData(@Nullable Bundle savedInstanceState) {
        super.initData(savedInstanceState);
        inflateMenu(R.menu.menu_toolbar);
        mOffline = new MKOfflineMap();
        mOffline.init(this);
        MKOLUpdateElement element = mOffline.getUpdateInfo(293);
        if (element == null) {
            // 开始下载离线地图，传入参数为cityID, cityID表示城市的数字标识。
            mOffline.start(293);
            progressBar = new ProgressDialog(this);
            progressBar.setTitle("正在下载离线地图");
            progressBar.show();
        } else {
            showFragment();
            setMapHasOffline();
        }
    }


    @Override
    public void onGetOfflineMapState(int type, int state) {
        switch (type) {
            case MKOfflineMap.TYPE_DOWNLOAD_UPDATE: {
                MKOLUpdateElement update = mOffline.getUpdateInfo(state);
                // 处理下载进度更新提示
                if (update != null) {
                    if (progressBar != null) {
                        progressBar.setMessage(String.format(Locale.ROOT, "%s : %d%%", update.cityName,
                                update.ratio));
                    }
                    if (update.ratio >= 100 && progressBar != null) {
                        progressBar.dismiss();
                        setMapHasOffline();
                        showFragment();
                    }
                }
            }
            break;
            case MKOfflineMap.TYPE_NEW_OFFLINE:
                // 有新离线地图安装
                Log.d("OfflineDemo", String.format("add offlinemap num:%d", state));
                break;
            case MKOfflineMap.TYPE_VER_UPDATE:
                // 版本更新提示
                // MKOLUpdateElement e = mOffline.getUpdateInfo(state);

                break;
            default:
                break;
        }
    }

    private void setMapHasOffline() {
        SPHelper.put(Constant.OFFLINE_MAP, 1);
    }

    @Override
    protected void onDestroy() {
        /**
         * 退出时，销毁离线地图模块
         */
        mOffline.destroy();
        super.onDestroy();
    }

    @Override
    public boolean onMenuItemClick(MenuItem item) {
        if (mMapFragment != null) {
            mMapFragment.onMenuItemClick(item.getItemId());
        }

        return false;
    }

    @Override
    protected AbstractPresenter initPresenter() {
        return null;
    }
}
