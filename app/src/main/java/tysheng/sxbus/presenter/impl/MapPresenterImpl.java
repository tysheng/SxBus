package tysheng.sxbus.presenter.impl;

import android.os.Bundle;
import android.support.v4.content.ContextCompat;

import com.baidu.location.BDLocation;
import com.baidu.mapapi.map.BaiduMap;
import com.baidu.mapapi.map.BitmapDescriptor;
import com.baidu.mapapi.map.BitmapDescriptorFactory;
import com.baidu.mapapi.map.MapStatus;
import com.baidu.mapapi.map.MapStatusUpdate;
import com.baidu.mapapi.map.MapStatusUpdateFactory;
import com.baidu.mapapi.map.MarkerOptions;
import com.baidu.mapapi.map.MyLocationData;
import com.baidu.mapapi.map.OverlayOptions;
import com.baidu.mapapi.map.PolylineOptions;
import com.baidu.mapapi.model.LatLng;
import com.baidu.mapapi.model.LatLngBounds;
import com.baidu.mapapi.utils.CoordinateConverter;

import java.util.ArrayList;
import java.util.List;

import tysheng.sxbus.R;
import tysheng.sxbus.bean.Stations;
import tysheng.sxbus.bean.YueChenBusResult;
import tysheng.sxbus.presenter.base.AbstractPresenter;
import tysheng.sxbus.presenter.inter.MapPresenter;
import tysheng.sxbus.ui.inter.MapView;
import tysheng.sxbus.utils.LogUtil;
import tysheng.sxbus.utils.MapUtil;
import tysheng.sxbus.utils.TyLocationListener;

/**
 * Created by tysheng
 * Date: 2017/3/11 15:22.
 * Email: tyshengsx@gmail.com
 */

public class MapPresenterImpl extends AbstractPresenter<MapView> implements MapPresenter {
    private BitmapDescriptor bdIcon;
    private BaiduMap mBaiduMap;
    private ArrayList<YueChenBusResult> mResultList;
    private ArrayList<Stations> mStationsList;
    private boolean isFirstInit = true;

    public MapPresenterImpl(MapView view) {
        super(view);
    }

    private BitmapDescriptor getBdIcon() {
        if (bdIcon == null) {
            bdIcon = BitmapDescriptorFactory.fromResource(R.drawable.ic_location_bus);
        }
        return bdIcon;
    }

    @Override
    public void setArgs(Bundle bundle) {
        mResultList = bundle.getParcelableArrayList("0");
        mStationsList = bundle.getParcelableArrayList("1");
    }

    @Override
    public void initData() {
        mBaiduMap = mView.getMap();
        initMap();
        List<LatLng> list = new ArrayList<>();
        for (int i = 0; i < mStationsList.size(); i++) {
            Stations sta = mStationsList.get(i);
            LatLng ll = new LatLng(sta.lat, sta.lng);
            list.add(ll);
        }
        OverlayOptions option = new PolylineOptions().points(list).color(ContextCompat.getColor(getContext(), R.color.red_2));
        mBaiduMap.addOverlay(option);

        for (int i = 0; i < mResultList.size(); i++) {
            YueChenBusResult point = mResultList.get(i);
            LatLng ll = new LatLng(point.lat, point.lng);
            // 将GPS设备采集的原始GPS坐标转换成百度坐标
            CoordinateConverter converter = new CoordinateConverter();
            converter.from(CoordinateConverter.CoordType.GPS);
            // sourceLatLng待转换坐标
            converter.coord(ll);
            LatLng desLatLng = converter.convert();
            OverlayOptions oo = new MarkerOptions().position(desLatLng).icon(getBdIcon()).zIndex(i);
            mBaiduMap.addOverlay(oo);
        }

    }

    private void initMap() {
//        mBaiduMap.setMapType(BaiduMap.MAP_TYPE_NONE);
        // 开启定位图层
        mBaiduMap.setMyLocationEnabled(true);
        LatLng northeast = new LatLng(29.7, 121.0);
        LatLng southwest = new LatLng(30.33, 120.26);
        mBaiduMap.setMapStatusLimits(new LatLngBounds.Builder().include(northeast).include(southwest).build());
        refreshLocation();
    }

    @Override
    public void onDestroy() {
        mBaiduMap.clear();
        mBaiduMap.setMyLocationEnabled(false);
        super.onDestroy();
    }

    private void setCenterPoint(double lat, double lng) {
        // 设定中心点坐标
        LatLng cenpt = new LatLng(lat, lng);
        // 定义地图状态
        MapStatus mMapStatus = new MapStatus.Builder().target(cenpt)
                .zoom(17f).build();
        // 定义MapStatusUpdate对象，以便描述地图状态将要发生的变化
        MapStatusUpdate mMapStatusUpdate = MapStatusUpdateFactory
                .newMapStatus(mMapStatus);
        // 改变地图状态
        mBaiduMap.setMapStatus(mMapStatusUpdate);
    }

    private void setLocation(final LatLng latLng) {

        if (isFirstInit) {
            isFirstInit = false;
            setCenterPoint(latLng.latitude, latLng.longitude);
        } else {
            MyLocationData locData = new MyLocationData.Builder()
                    // 此处设置开发者获取到的方向信息，顺时针0-360
                    .direction(100).latitude(latLng.latitude)
                    .longitude(latLng.longitude).build();
            mBaiduMap.setMyLocationData(locData);
        }

//        mBaiduMap.setOnMarkerClickListener(new BaiduMap.OnMarkerClickListener() {
//
//            @Override
//            public boolean onMarkerClick(Marker marker) {
//                int position = marker.getZIndex();
//                YueChenBusResult item = mResultList.get(position);
//                LogUtil.d("onMarkerClick");
////                TextView tv = new TextView(getContext());
////                tv.setPadding(UiUtil.dp2px(3.0f), UiUtil.dp2px(3.0f),
////                        UiUtil.dp2px(3.0f), UiUtil.dp2px(3.0f));
////                tv.setBackgroundColor(0x99ff9A00);
////                tv.setTextColor(0xffffffff);
////                StringBuffer sb = new StringBuffer();
////                sb.append(item.name + "\n")
////                        .append("可借:" + item.availBike + " , 可还:"
////                                + item.availVacancy + "\n")
////                        .append("地址:" + item.address);
////                tv.setText(sb.toString());
////                LatLng ll = marker.getPosition();
////                mInfoWindow = new InfoWindow(tv, ll, -(UiUtil.dp2px(25)));
////                mBaiduMap.showInfoWindow(mInfoWindow);
////                tv.setOnClickListener(new View.OnClickListener() {
////
////                    @Override
////                    public void onClick(View v) {
////                        mBaiduMap.hideInfoWindow();
////                    }
////
////                });
//                return true;
//            }
//        });
    }

    @Override
    public void refreshLocation() {
        MapUtil.getLocation(new TyLocationListener() {
            @Override
            public void onReceiveLocation(BDLocation location) {
                super.onReceiveLocation(location);
                LogUtil.d(getDetail(location));
                LatLng latLng = new LatLng(location.getLatitude(), location.getLongitude());
                setLocation(latLng);
            }
        });
    }
}
