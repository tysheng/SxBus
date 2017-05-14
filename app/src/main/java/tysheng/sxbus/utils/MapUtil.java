package tysheng.sxbus.utils;


import android.content.Context;

import com.baidu.location.BDLocationListener;
import com.baidu.location.LocationClient;
import com.baidu.location.LocationClientOption;
import com.baidu.mapapi.model.LatLng;
import com.baidu.mapapi.utils.CoordinateConverter;

/**
 * Created by tysheng
 * Date: 2017/3/11 15:27.
 * Email: tyshengsx@gmail.com
 */

public class MapUtil {
    private static MapUtil sUtil;
    private LocationClient client;

    private MapUtil() {
    }

    public static MapUtil getInstance() {
        if (sUtil == null) {
            sUtil = new MapUtil();
        }
        return sUtil;
    }

    public static double[] gpsToBdLatLng(CoordinateConverter.CoordType type, double[] latLng) {
        LatLng ll = new LatLng(latLng[0], latLng[1]);
        // 将GPS设备采集的原始GPS坐标转换成百度坐标
        CoordinateConverter converter = new CoordinateConverter();
        converter.from(type);
        // sourceLatLng待转换坐标
        converter.coord(ll);
        LatLng desLatLng = converter.convert();
        return new double[]{desLatLng.latitude, desLatLng.longitude};
    }

    public static LatLng gpsToBdLatLng(LatLng latLng) {
        // 将GPS设备采集的原始GPS坐标转换成百度坐标
        CoordinateConverter converter = new CoordinateConverter();
        converter.from(CoordinateConverter.CoordType.GPS);
        // sourceLatLng待转换坐标
        converter.coord(latLng);
        return converter.convert();
    }

    LocationClient getClient() {
        return client;
    }

    /**
     * @param listener //结果回调
     */
    public void getLocation(Context context, BDLocationListener listener) {
//        if (client == null) {
//
//        }
        // 定位初始化
        client = new LocationClient(context);
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
        client.start();
    }

//    public void offlineDownload() {
//        MKOfflineMap offline = new MKOfflineMap();
//// 传入接口事件，离线地图更新会触发该回调
//        offline.init(this);
//
//// 获取城市可更新列表
//        ArrayList<MKOLSearchRecord> records = offline.searchCity("绍兴");
//        for (MKOLSearchRecord re : records) {
//            LogUtil.d(re.cityID);
//        }
//// 开始下载离线地图，传入参数为cityID, cityID表示城市的数字标识。
////        mOffline.start(cityid);
////// 暂停下载
////        mOffline.pause(cityid);
////// 删除下载
////        mOffline.remove(cityid);
//    }
//
//    @Override
//    public void onGetOfflineMapState(int i, int i1) {
//
//    }
}
