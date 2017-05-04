package tysheng.sxbus.presenter.impl;

import android.graphics.Color;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import com.baidu.location.Address;
import com.baidu.location.BDLocation;
import com.baidu.mapapi.map.BaiduMap;
import com.baidu.mapapi.map.InfoWindow;
import com.baidu.mapapi.map.MapPoi;
import com.baidu.mapapi.map.MapStatus;
import com.baidu.mapapi.map.MapStatusUpdate;
import com.baidu.mapapi.map.MapStatusUpdateFactory;
import com.baidu.mapapi.map.Marker;
import com.baidu.mapapi.map.MyLocationData;
import com.baidu.mapapi.model.LatLng;
import com.baidu.mapapi.model.LatLngBounds;

import java.util.ArrayList;

import tysheng.sxbus.bean.MapInfo;
import tysheng.sxbus.bean.Stations;
import tysheng.sxbus.model.impl.DrawModelImpl;
import tysheng.sxbus.presenter.base.AbstractPresenter;
import tysheng.sxbus.presenter.inter.MapPresenter;
import tysheng.sxbus.ui.inter.MapView;
import tysheng.sxbus.utils.LogUtil;
import tysheng.sxbus.utils.MapUtil;
import tysheng.sxbus.utils.SnackBarUtil;
import tysheng.sxbus.utils.TyLocationListener;
import tysheng.sxbus.utils.UiUtil;

/**
 * Created by tysheng
 * Date: 2017/3/11 15:22.
 * Email: tyshengsx@gmail.com
 */

public class MapPresenterImpl extends AbstractPresenter<MapView> implements MapPresenter {

    private BaiduMap mBaiduMap;
    private ArrayList<? extends MapInfo> mResultList;
    private ArrayList<? extends MapInfo> mStationsList;
    private InfoWindow mInfoWindow;
    private DrawModelImpl mDrawModel;
    private Stations mClickStations;
    private int type;

    public MapPresenterImpl(MapView view) {
        super(view);
        mDrawModel = new DrawModelImpl();
    }

    @Override
    public void setArgs(Bundle bundle) {
        mClickStations = bundle.getParcelable("2");
        mResultList = bundle.getParcelableArrayList("0");
        mStationsList = bundle.getParcelableArrayList("1");
        type = bundle.getInt("-1");
        if (mClickStations != null) {
            mView.setSubtitle(mClickStations.stationName);
        }
    }

    @Override
    public void initData() {
        mBaiduMap = mView.getMap();
        initMap();
        mDrawModel.drawStations(type, getBaiduMap(), mStationsList);
        mDrawModel.drawBuses(getBaiduMap(), mResultList);
    }


    private void initMap() {
        // mBaiduMap.setMapType(BaiduMap.MAP_TYPE_NONE);
        // 开启定位图层
        mBaiduMap.setMyLocationEnabled(true);
        mBaiduMap.setOnMapClickListener(new BaiduMap.OnMapClickListener() {
            @Override
            public void onMapClick(LatLng latLng) {
                mBaiduMap.hideInfoWindow();
            }

            @Override
            public boolean onMapPoiClick(MapPoi mapPoi) {
                return false;
            }
        });
        //绍兴市大概区域
        LatLng northeast = new LatLng(29.7, 121.0);
        LatLng southwest = new LatLng(30.33, 120.26);
        mBaiduMap.setMapStatusLimits(new LatLngBounds.Builder().include(northeast).include(southwest).build());
        mBaiduMap.setOnMarkerClickListener(new BaiduMap.OnMarkerClickListener() {
            @Override
            public boolean onMarkerClick(Marker marker) {
                int position = marker.getZIndex();
                if (position < 1000) {
                    MapInfo item = mStationsList.get(position);
                    TextView tv = new TextView(getContext());
                    int padding = UiUtil.dp2px(5f);
                    tv.setPadding(padding, padding, padding, padding);
                    tv.setBackgroundColor(Color.BLACK);
                    tv.setTextColor(Color.WHITE);
                    tv.setText(item.getName());
                    LatLng ll = marker.getPosition();
                    mInfoWindow = new InfoWindow(tv, ll, -(UiUtil.dp2px(25)));
                    mBaiduMap.showInfoWindow(mInfoWindow);
                    tv.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            mBaiduMap.hideInfoWindow();
                        }
                    });
                } else if (position >= 1000 && position < 2000) {
//                    final int realPosition = position - 1000;
//                    SxBusResult result = mResultList.get(realPosition);
//                    LogUtil.d(result.stationSeqNum);
                }
                return true;
            }
        });
        refreshLocation();
    }

    private LatLng findClickLocation() {
        if (mClickStations != null) {
            return new LatLng(mClickStations.lat, mClickStations.lng);
        }
        return null;
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
                .zoom(15.5f).build();
        // 定义MapStatusUpdate对象，以便描述地图状态将要发生的变化
        MapStatusUpdate mMapStatusUpdate = MapStatusUpdateFactory
                .newMapStatus(mMapStatus);
        LogUtil.d(mMapStatus.toString());
        // 改变地图状态
        mBaiduMap.setMapStatus(mMapStatusUpdate);
    }

    private void setLocation(final BDLocation location) {
        LatLng latLng = new LatLng(location.getLatitude(), location.getLongitude());
        MyLocationData locData = new MyLocationData.Builder()
                // 此处设置开发者获取到的方向信息，顺时针0-360
                .direction(100).latitude(latLng.latitude)
                .longitude(latLng.longitude).build();
        mBaiduMap.setMyLocationData(locData);

        /**
         * 是否是点击进来的，如果是就跳转过去
         */
        LatLng finalLatLng = findClickLocation();
        if (finalLatLng == null) {
            finalLatLng = latLng;
            Address address = location.getAddress();
            String sub = address.street + address.streetNumber;
            LogUtil.d("getAddrStr " + sub);
            mView.setSubtitle(sub.trim());
        } else {
            mDrawModel.drawSinglePlace(getBaiduMap(), finalLatLng);
        }
        setCenterPoint(finalLatLng.latitude, finalLatLng.longitude);
    }

    @Override
    public void refreshLocation() {
        MapUtil.getInstance().getLocation(new TyLocationListener() {
            @Override
            public void onReceiveLocation(BDLocation location) {
                super.onReceiveLocation(location);
                LogUtil.d(getDetail(location));
                int code = location.getLocType();
                if (!(code == 161 || code == 66 || code == 61)) {
                    SnackBarUtil.show(mView.getRootView(), "定位失败%>_<%");
                }
                setLocation(location);
            }
        });
    }


    private BaiduMap getBaiduMap() {
        return mBaiduMap;
    }

    @Override
    public void drawStations() {
        mDrawModel.drawStationsClick(type, getBaiduMap());
    }
}
