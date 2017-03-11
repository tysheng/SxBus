package tysheng.sxbus.utils;


import com.baidu.location.BDLocationListener;
import com.baidu.location.LocationClient;
import com.baidu.location.LocationClientOption;
import com.baidu.mapapi.map.offline.MKOLSearchRecord;
import com.baidu.mapapi.map.offline.MKOfflineMap;
import com.baidu.mapapi.map.offline.MKOfflineMapListener;
import com.baidu.mapapi.model.LatLng;
import com.baidu.mapapi.utils.CoordinateConverter;

import java.util.ArrayList;

import tysheng.sxbus.App;

/**
 * Created by tysheng
 * Date: 2017/3/11 15:27.
 * Email: tyshengsx@gmail.com
 */

public class MapUtil implements MKOfflineMapListener {
    private static LocationClient client;
    private MKOfflineMap mOffline;

    public static LocationClient getClient() {
        return client;
    }

    public static double[] gpsToBdLatLng(double[] latLng) {
        LatLng ll = new LatLng(latLng[0], latLng[1]);
        // 将GPS设备采集的原始GPS坐标转换成百度坐标
        CoordinateConverter converter = new CoordinateConverter();
        converter.from(CoordinateConverter.CoordType.GPS);
        // sourceLatLng待转换坐标
        converter.coord(ll);
        LatLng desLatLng = converter.convert();
        return new double[]{desLatLng.latitude, desLatLng.longitude};
    }

    /**
     * @param listener //结果回调
     */
    public static void getLocation(BDLocationListener listener) {
        if (client == null) {
            // 定位初始化
            client = new LocationClient(App.get());
            client.registerLocationListener(listener);
            LocationClientOption option = new LocationClientOption();
            option.setOpenGps(true);// 打开gps
            option.setCoorType("bd09ll"); // 设置坐标类型
//            option.setScanSpan(5000);
            option.setIsNeedAddress(true);
            option.setLocationMode(LocationClientOption.LocationMode.Hight_Accuracy);
            option.setIgnoreKillProcess(false);
            option.SetIgnoreCacheException(false);
            client.setLocOption(option);
        }
        client.start();
    }

    public void offlineDownload() {
        mOffline = new MKOfflineMap();
// 传入接口事件，离线地图更新会触发该回调
        mOffline.init(this);

// 获取城市可更新列表
        ArrayList<MKOLSearchRecord> records = mOffline.searchCity("绍兴");
        for (MKOLSearchRecord re : records
                ) {
            LogUtil.d(re.cityID);
        }
// 开始下载离线地图，传入参数为cityID, cityID表示城市的数字标识。
//        mOffline.start(cityid);
//// 暂停下载
//        mOffline.pause(cityid);
//// 删除下载
//        mOffline.remove(cityid);
    }

    @Override
    public void onGetOfflineMapState(int i, int i1) {

    }
}
