package tysheng.sxbus.ui.activities;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;

import com.baidu.mapapi.map.offline.MKOLUpdateElement;
import com.baidu.mapapi.map.offline.MKOfflineMap;
import com.baidu.mapapi.map.offline.MKOfflineMapListener;
import com.baidu.mapapi.model.LatLng;

import java.util.ArrayList;
import java.util.Locale;

import butterknife.BindView;
import tysheng.sxbus.Constant;
import tysheng.sxbus.R;
import tysheng.sxbus.base.BaseActivity;
import tysheng.sxbus.bean.Stations;
import tysheng.sxbus.bean.YueChenBusResult;
import tysheng.sxbus.ui.fragments.MapFragment;
import tysheng.sxbus.utils.LogUtil;
import tysheng.sxbus.utils.SPHelper;

/**
 * Created by tysheng
 * Date: 2017/3/11 15:57.
 * Email: tyshengsx@gmail.com
 */

public class ToolbarActivity extends BaseActivity implements Toolbar.OnMenuItemClickListener, MKOfflineMapListener {
    @BindView(R.id.toolbar)
    Toolbar mToolbar;
    private MapFragment mMapFragment;
    private MKOfflineMap mOffline;
    private ProgressDialog progressBar;
    private Intent intent;

    public static void startMap(Context context, ArrayList<YueChenBusResult> list, ArrayList<Stations> stations, LatLng latLng) {
        if (list != null && stations != null) {
            Intent intent = new Intent(context, ToolbarActivity.class);
            intent.putExtra("0", list);
            intent.putExtra("1", stations);
            intent.putExtra("2", latLng);
            context.startActivity(intent);
        }
    }

    @Override
    public void initData(@Nullable Bundle savedInstanceState) {
        mToolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        mToolbar.inflateMenu(R.menu.menu_toolbar);
        mToolbar.setOnMenuItemClickListener(this);
        setTitle("查看定位");
        intent = getIntent();
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
            LogUtil.d(element.size);
            showFragment();
            setMapHasOffline();
        }
    }

    private void showFragment() {
        mMapFragment = MapFragment.newInstance(intent.getParcelableArrayListExtra("0"), intent.getParcelableArrayListExtra("1"), intent.getParcelableExtra("2"));
        getSupportFragmentManager()
                .beginTransaction()
                .replace(R.id.frameLayout, mMapFragment)
                .commit();
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
    public int getLayoutId() {
        return R.layout.activity_toolbar;
    }

    private void setTitle(String title) {
        mToolbar.setTitle(title);
    }

    @Override
    public boolean onMenuItemClick(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.action_refresh:
                if (mMapFragment != null) {
                    mMapFragment.refreshLocation();
                }
                break;
            case R.id.action_draw_station:
                if (mMapFragment != null) {
                    mMapFragment.drawStations();
                }
                break;
            default:
                break;
        }
        return false;
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
}
