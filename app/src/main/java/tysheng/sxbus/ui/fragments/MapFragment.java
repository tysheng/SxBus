package tysheng.sxbus.ui.fragments;

import android.os.Bundle;
import android.os.Parcelable;
import android.support.annotation.Nullable;

import com.baidu.mapapi.map.BaiduMap;

import java.util.ArrayList;

import javax.inject.Inject;

import tysheng.sxbus.R;
import tysheng.sxbus.base.BaseFragmentV2;
import tysheng.sxbus.databinding.FragmentMapBinding;
import tysheng.sxbus.di.component.DaggerMapComponent;
import tysheng.sxbus.di.module.MapModule;
import tysheng.sxbus.presenter.impl.MapPresenterImpl;
import tysheng.sxbus.ui.activities.ToolbarActivity;
import tysheng.sxbus.ui.inter.MapView;

/**
 * Created by tysheng
 * Date: 2017/3/11 15:21.
 * Email: tyshengsx@gmail.com
 */

public class MapFragment extends BaseFragmentV2<FragmentMapBinding> implements MapView {

    @Inject
    MapPresenterImpl mPresenter;

    /**
     * @param type       bus or bike
     * @param list       running bus list
     * @param stations   total stations
     * @param parcelable click station
     * @return MapFragment
     */
    public static MapFragment newInstance(int type, ArrayList<Parcelable> list, ArrayList<Parcelable> stations, Parcelable parcelable) {
        Bundle args = new Bundle();
        args.putParcelableArrayList("0", list);
        args.putParcelableArrayList("1", stations);
        args.putParcelable("2", parcelable);
        args.putInt("-1", type);
        MapFragment fragment = new MapFragment();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void setSubtitle(String subtitle) {
        ((ToolbarActivity) getActivity()).setSubtitle(subtitle);
    }

    @Override
    public void onResume() {
        super.onResume();
        binding.mapView.onResume();
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mPresenter.setArgs(getArguments());
    }

    @Override
    protected void initDagger() {
        DaggerMapComponent.builder()
                .universeComponent(getUniverseComponent())
                .mapModule(new MapModule(this))
                .build()
                .inject(this);
    }

    @Override
    public void onPause() {
        super.onPause();
        binding.mapView.onPause();
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding.mapView.onDestroy();
    }

    @Override
    protected int getLayoutId() {
        return R.layout.fragment_map;
    }

    @Override
    protected void initData() {
        mPresenter.initData();
        binding.setPresenter(mPresenter);
    }

    @Override
    public BaiduMap getMap() {
        return binding.mapView.getMap();
    }

    public void onMenuItemClick(int itemId) {
        mPresenter.onMenuItemClick(itemId);
    }
}
