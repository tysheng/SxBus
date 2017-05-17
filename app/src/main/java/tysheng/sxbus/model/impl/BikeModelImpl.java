package tysheng.sxbus.model.impl;

import android.app.ProgressDialog;
import android.content.Context;

import com.baidu.mapapi.utils.CoordinateConverter;

import java.util.ArrayList;

import javax.inject.Inject;

import io.reactivex.functions.Function;
import tysheng.sxbus.Constant;
import tysheng.sxbus.R;
import tysheng.sxbus.bean.AllBike;
import tysheng.sxbus.bean.BikeStation;
import tysheng.sxbus.model.base.BaseModelImpl;
import tysheng.sxbus.presenter.inter.BikePresenter;
import tysheng.sxbus.ui.activities.MapActivity;
import tysheng.sxbus.utils.JsonUtil;
import tysheng.sxbus.utils.MapUtil;
import tysheng.sxbus.utils.RxHelper;
import tysheng.sxbus.utils.TyObserver;

/**
 * Created by tysheng
 * Date: 2017/5/17 12:50.
 * Email: tyshengsx@gmail.com
 */

public class BikeModelImpl extends BaseModelImpl {

    String msg;
    Context mContext;
    private BikePresenter mPresenter;

    @Inject
    public BikeModelImpl(BikePresenter presenter) {
        super();
        mPresenter = presenter;
        mContext = presenter.getContext();
        msg = mContext.getString(R.string.loading);
    }

    private Context getContext() {
        return mContext;
    }

    public void bikeInfo() {
        mBusService
                .url(Constant.BIKE_URL)
                .map(new Function<String, ArrayList<BikeStation>>() {
                    @Override
                    public ArrayList<BikeStation> apply(String callBack) throws Exception {
                        int start = "var ibike =".length();
                        String string = callBack.substring(start).trim();
                        AllBike bike = JsonUtil.parse(string, AllBike.class);
                        for (BikeStation station : bike.station) {
                            double[] latLng =
                                    MapUtil.gpsToBdLatLng(CoordinateConverter.CoordType.COMMON, new double[]{station.getLat(), station.getLng()});
                            station.lat = latLng[0];
                            station.lng = latLng[1];
                        }
                        return bike.station;
                    }
                })
                .compose(RxHelper.<ArrayList<BikeStation>>flowableIoToMain())
                .compose(mPresenter.<ArrayList<BikeStation>>bindUntilDestroyView())
                .subscribe(new TyObserver<ArrayList<BikeStation>>() {
                    ProgressDialog dialog;

                    @Override
                    protected void onStart() {
                        super.onStart();
                        dialog = new ProgressDialog(getContext());
                        dialog.setMessage(msg);
                        dialog.show();
                    }

                    @Override
                    public void onError(Throwable t) {
                        super.onError(t);
                        if (dialog != null) {
                            dialog.dismiss();
                        }
                    }

                    @Override
                    public void next(ArrayList<BikeStation> s) {
                        super.next(s);
                        if (dialog != null) {
                            dialog.dismiss();
                        }
                        MapActivity.startMap(getContext(), Constant.BIKE, null, s, null);
                    }
                });
    }
}
