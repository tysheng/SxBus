package tysheng.sxbus.presenter.impl;

import android.Manifest;
import android.app.ProgressDialog;
import android.content.ClipData;
import android.content.ClipboardManager;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.text.SpannableString;
import android.text.Spanned;
import android.text.style.RelativeSizeSpan;

import com.baidu.mapapi.utils.CoordinateConverter;

import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;

import io.reactivex.functions.Function;
import tysheng.sxbus.App;
import tysheng.sxbus.BuildConfig;
import tysheng.sxbus.Constant;
import tysheng.sxbus.R;
import tysheng.sxbus.bean.AllBike;
import tysheng.sxbus.bean.BikeStation;
import tysheng.sxbus.bean.More;
import tysheng.sxbus.di.component.DaggerUniverseComponent;
import tysheng.sxbus.net.BusService;
import tysheng.sxbus.presenter.base.AbstractPresenter;
import tysheng.sxbus.ui.activities.MapActivity;
import tysheng.sxbus.ui.fragments.ListDialogFragment;
import tysheng.sxbus.ui.fragments.MapFragment;
import tysheng.sxbus.ui.inter.MoreView;
import tysheng.sxbus.utils.AlipayZeroSdk;
import tysheng.sxbus.utils.JsonUtil;
import tysheng.sxbus.utils.MapUtil;
import tysheng.sxbus.utils.PermissionUtil;
import tysheng.sxbus.utils.RxHelper;
import tysheng.sxbus.utils.SPHelper;
import tysheng.sxbus.utils.SystemUtil;
import tysheng.sxbus.utils.TyObserver;

/**
 * Created by tysheng
 * Date: 2017/2/28 17:00.
 * Email: tyshengsx@gmail.com
 */

public class MorePresenterImpl extends AbstractPresenter<MoreView> {

    @Inject
    BusService mBusService;

    public MorePresenterImpl(MoreView view) {
        super(view);
        DaggerUniverseComponent.builder()
                .applicationComponent(((App) getActivity().getApplication()).getApplicationComponent())
                .build()
                .inject(this);
    }

    /**
     * 根据应用包名，跳转到应用市场
     *
     * @param context     承载跳转的Activity
     * @param packageName 所需下载（评论）的应用包名
     */
    private void shareAppShop(Context context, String packageName) {
        try {
//            Uri uri = Uri.parse("market://details?id=" + packageName);
//            Intent intent = new Intent(Intent.ACTION_VIEW, uri);
//            intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
//            //跳转酷市场
//            intent.setClassName("com.coolapk.market", "com.coolapk.market.activity.AppViewActivity");
//            context.startActivity(intent);
            //从其他浏览器打开
            Intent intent = new Intent();
            intent.setAction(Intent.ACTION_VIEW);
            Uri content_url = Uri.parse("http://www.coolapk.com/apk/" + packageName);
            intent.setData(content_url);
            context.startActivity(Intent.createChooser(intent, "请选择浏览器"));
        } catch (Exception e) {

        }
    }

    public SpannableString getTitle() {
        SpannableString string = new SpannableString("版本 " + SystemUtil.getVersionName());
        RelativeSizeSpan sizeSpan = new RelativeSizeSpan(0.75f);
        string.setSpan(sizeSpan, 3, 6, Spanned.SPAN_INCLUSIVE_EXCLUSIVE);
        return string;
    }

    @Override
    public void setArgs(Bundle bundle) {

    }

    @Override
    public void initData() {
        mView.setTitle(getTitle());
        List<More> list = new ArrayList<>();
        list.add(createMore(More.HAS_SUB, true, "绍兴", "当前城市", 0));
        list.add(createMore(More.SIMPLE, true, "公共自行车", null, 1));
        list.add(createMore(More.SIMPLE, true, "反馈", null, 2));
        list.add(createMore(More.SIMPLE, true, "赞赏作者", null, 3));
        list.add(createMore(More.SIMPLE, true, "检查更新", null, 4));
        list.add(createMore(More.SIMPLE, false, "开源库", null, 7));
        list.add(createMore(More.HAS_SUB, true, "切换两种到站模式,仅限市公交", "到站模式", 5));
        list.add(createMore(More.HAS_SUB, true, "帮助开发者进行错误日志收集", "日志收集", 6));
        mView.setMoreList(list);
    }

    private More createMore(int type, boolean divider, String main, String sub, int pos) {
        More more = new More();
        more.hasDivider = divider;
        more.main = main;
        more.topSub = sub;
        more.type = type;
        more.internalPosition = pos;
        return more;
    }

    private void showAlipayFail(String s) {
        mView.snackBarShow(s);
        ClipboardManager c = (ClipboardManager) getContext().getSystemService(Context.CLIPBOARD_SERVICE);
        c.setPrimaryClip(ClipData.newPlainText("email", getContext().getString(R.string.my_email)));//设置Clipboard 的内容
    }

    private void checkVersion() {
        shareAppShop(getContext(), BuildConfig.APPLICATION_ID);
    }

    private void setStationMode() {
        new AlertDialog.Builder(getContext())
                .setItems(new String[]{"根据站点", "根据距离"}, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        SPHelper.put(Constant.STATION_MODE, which);
                    }
                })
                .show();
    }

    private void chooseCity(int i) {
        ListDialogFragment f = ListDialogFragment.newInstance(i);
        f.show(getChildFragmentManager(), "");
    }

    private void donate() {
        if (AlipayZeroSdk.hasInstalledAlipayClient(getContext())) {
            if (!AlipayZeroSdk.startAlipayClient(getActivity())) {
                showAlipayFail(getContext().getString(R.string.msg_alipay_copied));
            }
        } else {
            showAlipayFail(getContext().getString(R.string.msg_alipay_copied));
        }
    }

    private void askPermission() {
        PermissionUtil.request(getActivity(), new PermissionUtil.Callback() {
                    @Override
                    public void call(boolean b) {
                        if (!b) {
                            mView.snackBarShow("很遗憾，没有赋予这些权限:(");
                        } else {
                            mView.snackBarShow("谢谢你的帮助:)");
                        }
                    }
                }, Manifest.permission.READ_PHONE_STATE,
                Manifest.permission.WRITE_EXTERNAL_STORAGE);
    }

    private void feedback() {
        SystemUtil.sendEmail(getContext());
    }

    private void bikeInfo() {
        mBusService
                .url(MapFragment.BIKE_URL)
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
                .compose(this.<ArrayList<BikeStation>>bindUntilDestroyView())
                .subscribe(new TyObserver<ArrayList<BikeStation>>() {
                    ProgressDialog dialog;

                    @Override
                    protected void onStart() {
                        super.onStart();
                        dialog = new ProgressDialog(getContext());
                        dialog.setMessage("正在加载...");
                        dialog.show();
                    }

                    @Override
                    public void onError(Throwable t) {
                        super.onError(t);
                        dialog.dismiss();
                    }

                    @Override
                    public void next(ArrayList<BikeStation> s) {
                        super.next(s);
                        dialog.dismiss();
                        MapActivity.startMap(getContext(), Constant.BIKE, null, s, null);
                    }
                });
    }


    public void onItemClick(int internalPosition) {
        switch (internalPosition) {
            case 0:
                chooseCity(ListDialogFragment.CHOOSE_CITY);
                break;
            case 1:
                bikeInfo();
                break;
            case 2:
                feedback();
                break;
            case 3:
                donate();
                break;
            case 4:
                checkVersion();
                break;
            case 5:
                setStationMode();
                break;
            case 6:
                askPermission();
                break;
            case 7:
                chooseCity(ListDialogFragment.OPEN_SOURCE);
                break;
            default:
                break;
        }
    }
}
