package tysheng.sxbus.presenter.impl;

import android.Manifest;
import android.content.ClipData;
import android.content.ClipboardManager;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.support.v7.app.AlertDialog;
import android.text.SpannableString;
import android.text.Spanned;
import android.text.style.RelativeSizeSpan;

import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;

import tysheng.sxbus.BuildConfig;
import tysheng.sxbus.Constant;
import tysheng.sxbus.R;
import tysheng.sxbus.bean.More;
import tysheng.sxbus.di.component.DaggerBikeComponent;
import tysheng.sxbus.di.module.BikeModule;
import tysheng.sxbus.model.impl.BikeModelImpl;
import tysheng.sxbus.presenter.base.AbstractPresenter;
import tysheng.sxbus.presenter.inter.BikePresenter;
import tysheng.sxbus.ui.fragments.ListDialogFragment;
import tysheng.sxbus.ui.inter.MoreView;
import tysheng.sxbus.utils.AlipayZeroSdk;
import tysheng.sxbus.utils.PermissionUtil;
import tysheng.sxbus.utils.SPHelper;
import tysheng.sxbus.utils.SystemUtil;

/**
 * Created by tysheng
 * Date: 2017/2/28 17:00.
 * Email: tyshengsx@gmail.com
 */

public class MorePresenterImpl extends AbstractPresenter<MoreView> implements BikePresenter {

    @Inject
    BikeModelImpl mBikeModel;

    @Inject
    public MorePresenterImpl(MoreView view) {
        super(view);
        DaggerBikeComponent.builder()
                .universeComponent(getUniverseComponent())
                .bikeModule(new BikeModule(this))
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
            context.startActivity(Intent.createChooser(intent, context.getString(R.string.choose_browser)));
        } catch (Exception e) {

        }
    }

    public SpannableString getTitle() {
        SpannableString string = new SpannableString(getString(R.string.version) + SystemUtil.getVersionName());
        RelativeSizeSpan sizeSpan = new RelativeSizeSpan(0.75f);
        string.setSpan(sizeSpan, 3, 6, Spanned.SPAN_INCLUSIVE_EXCLUSIVE);
        return string;
    }

    @Override
    public void initData() {
        mView.setTitle(getTitle());
        List<More> list = new ArrayList<>();
        list.add(createMore(More.HAS_SUB, true, getString(R.string.shaoxing), getString(R.string.current_city), 0));
        list.add(createMore(More.SIMPLE, true, getString(R.string.public_bike), null, 1));
        list.add(createMore(More.SIMPLE, true, getString(R.string.feedback), null, 2));
        list.add(createMore(More.SIMPLE, true, getString(R.string.appreciate_author), null, 3));
        list.add(createMore(More.SIMPLE, true, getString(R.string.launch_tab), null, 8));
        list.add(createMore(More.SIMPLE, true, getString(R.string.check_update), null, 4));
        list.add(createMore(More.SIMPLE, false, getString(R.string.open_source_lib), null, 7));
        list.add(createMore(More.HAS_SUB, true, getString(R.string.station_mode_intro), getString(R.string.station_mode), 5));
        list.add(createMore(More.HAS_SUB, true, getString(R.string.colloct_log_intro), getString(R.string.colloct_log), 6));
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
        c.setPrimaryClip(ClipData.newPlainText("email", getString(R.string.my_email)));//设置Clipboard 的内容
    }

    private void checkVersion() {
        shareAppShop(getContext(), BuildConfig.APPLICATION_ID);
    }

    private void setStationMode() {
        new AlertDialog.Builder(getContext())
                .setItems(new String[]{getString(R.string.station_mode_by_station), getString(R.string.station_mode_by_distance)}, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        SPHelper.put(Constant.STATION_MODE, which);
                    }
                })
                .show();
    }

    private void chooseCity(int i) {
        ListDialogFragment f = ListDialogFragment.newInstance(i);
        f.show(getChildFragmentManager(), ListDialogFragment.class.getSimpleName());
    }

    private void donate() {
        if (AlipayZeroSdk.hasInstalledAlipayClient(getContext())) {
            if (!AlipayZeroSdk.startAlipayClient(getActivity())) {
                showAlipayFail(getString(R.string.msg_alipay_copied));
            }
        } else {
            showAlipayFail(getString(R.string.msg_alipay_copied));
        }
    }

    private void askPermission() {
        PermissionUtil.request(getActivity(), new PermissionUtil.Callback() {
                    @Override
                    public void call(boolean b) {
                        if (!b) {
                            mView.snackBarShow(getString(R.string.permission_fail));
                        } else {
                            mView.snackBarShow(getString(R.string.permission_success));
                        }
                    }
                }, Manifest.permission.READ_PHONE_STATE,
                Manifest.permission.WRITE_EXTERNAL_STORAGE);
    }

    private void feedback() {
        SystemUtil.sendEmail(getContext());
    }


    public void onItemClick(int internalPosition) {
        switch (internalPosition) {
            case 0:
                chooseCity(ListDialogFragment.CHOOSE_CITY);
                break;
            case 1:
                mBikeModel.bikeInfo();
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
            case 8:
                setLaunchTab();
                break;
            default:
                break;
        }
    }

    private void setLaunchTab() {
        new AlertDialog.Builder(getContext())
                .setItems(new String[]{getString(R.string.menu_collect), getString(R.string.menu_search)},
                        new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                SPHelper.put(Constant.LAUNCH_TAB, which);
                            }
                        })
                .show();
    }
}
