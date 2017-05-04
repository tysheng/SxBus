package tysheng.sxbus.model.impl;

import android.support.v4.content.ContextCompat;

import com.baidu.mapapi.map.BaiduMap;
import com.baidu.mapapi.map.BitmapDescriptor;
import com.baidu.mapapi.map.BitmapDescriptorFactory;
import com.baidu.mapapi.map.MarkerOptions;
import com.baidu.mapapi.map.OverlayOptions;
import com.baidu.mapapi.map.PolylineOptions;
import com.baidu.mapapi.model.LatLng;

import java.util.ArrayList;
import java.util.List;

import tysheng.sxbus.Constant;
import tysheng.sxbus.R;
import tysheng.sxbus.bean.MapInfo;
import tysheng.sxbus.utils.ListUtil;
import tysheng.sxbus.utils.MapUtil;
import tysheng.sxbus.utils.SPHelper;

import static com.baidu.mapapi.BMapManager.getContext;

/**
 * Created by tysheng
 * Date: 2017/3/12 14:00.
 * Email: tyshengsx@gmail.com
 */

public class DrawModelImpl {

    private BitmapDescriptor iconBus, iconStation, iconStart, iconEnd, iconClickHere, iconLongArrow;
    private List<LatLng> mLatLngs = new ArrayList<>();
    private ArrayList<? extends MapInfo> stationsList;
    private ArrayList<? extends MapInfo> resultList;

    public DrawModelImpl() {

    }

    private BitmapDescriptor getBusIcon() {
        if (iconBus == null) {
            iconBus = BitmapDescriptorFactory.fromResource(R.drawable.ic_bus);
        }
        return iconBus;
    }

    private BitmapDescriptor getLongIcon() {
        if (iconLongArrow == null) {
            iconLongArrow = BitmapDescriptorFactory.fromResource(R.drawable.ic_long_arrow_up);
        }
        return iconLongArrow;
    }

    private BitmapDescriptor getStationIcon() {
        if (iconStation == null) {
            iconStation = BitmapDescriptorFactory.fromResource(R.drawable.icon_station);
        }
        return iconStation;
    }

    private BitmapDescriptor getClickIcon() {
        if (iconClickHere == null) {
            iconClickHere = BitmapDescriptorFactory.fromResource(R.drawable.ic_click_here);
        }
        return iconClickHere;
    }

    private BitmapDescriptor getStartIcon() {
        if (iconStart == null) {
            iconStart = BitmapDescriptorFactory.fromResource(R.drawable.ic_start);
        }
        return iconStart;
    }

    private BitmapDescriptor getEndIcon() {
        if (iconEnd == null) {
            iconEnd = BitmapDescriptorFactory.fromResource(R.drawable.ic_end);
        }
        return iconEnd;
    }

    public void drawStations(int type, BaiduMap baiduMap, ArrayList<? extends MapInfo> stationsList) {
        boolean drawStation = SPHelper.get(Constant.DRAW_STATION, true);
        this.stationsList = stationsList;
        drawStationInternal(type, baiduMap, stationsList, drawStation, true);
    }

    private void drawStationInternal(int type, BaiduMap baiduMap, ArrayList<? extends MapInfo> stationsList, boolean drawStation, boolean startEnd) {
        mLatLngs.clear();
        for (int i = 0; i < stationsList.size(); i++) {
            MapInfo sta = stationsList.get(i);
            LatLng ll = new LatLng(sta.getLat(), sta.getLng());
            mLatLngs.add(ll);
            if (i == 0 && startEnd && type != Constant.BIKE) {
                //起点
                OverlayOptions oo = new MarkerOptions().position(ll).icon(getStartIcon()).title(sta.getName()).zIndex(i).perspective(true);
                baiduMap.addOverlay(oo);
            } else if (i == stationsList.size() - 1 && startEnd && type != Constant.BIKE) {
                //终点
                OverlayOptions oo = new MarkerOptions().position(ll).icon(getEndIcon()).title(sta.getName()).zIndex(i).perspective(true);
                baiduMap.addOverlay(oo);
            } else if (drawStation) {
                //站点
                OverlayOptions oo = new MarkerOptions().position(ll).icon(getStationIcon()).title(sta.getName()).zIndex(i);
                baiduMap.addOverlay(oo);
            }
        }
        if (type == Constant.BUS) {
            //连接线
            OverlayOptions option = new PolylineOptions().points(mLatLngs)
                    .color(ContextCompat.getColor(getContext(), R.color.baidu_blue));
            baiduMap.addOverlay(option);
        }

    }

    public void drawSinglePlace(BaiduMap baiduMap, LatLng latLng) {
        OverlayOptions oo = new MarkerOptions().position(latLng).icon(getClickIcon()).zIndex(10000)
                .anchor(0.5f, 2f).animateType(MarkerOptions.MarkerAnimateType.grow).draggable(true);

        baiduMap.addOverlay(oo);
    }

    public void drawBuses(BaiduMap map, ArrayList<? extends MapInfo> resultList) {
        this.resultList = resultList;
        if (ListUtil.isEmpty(resultList)) {
            return;
        }
        //车辆坐标
        for (int i = 0; i < resultList.size(); i++) {
            MapInfo point = resultList.get(i);
            LatLng ll = new LatLng(point.getLat(), point.getLng());
            LatLng desLatLng = MapUtil.gpsToBdLatLng(ll);
            OverlayOptions oo = new MarkerOptions().position(desLatLng).icon(getBusIcon()).zIndex(1000 + i);
            map.addOverlay(oo);
        }
    }

    public void drawStationsClick(int type, BaiduMap map) {
        final boolean draw = !SPHelper.get(Constant.DRAW_STATION, true);
        SPHelper.put(Constant.DRAW_STATION, draw);
        map.clear();

        drawStationInternal(type, map, stationsList, draw, true);
        drawBuses(map, resultList);
    }
}
