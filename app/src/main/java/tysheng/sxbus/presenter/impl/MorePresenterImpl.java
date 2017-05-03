package tysheng.sxbus.presenter.impl;

import android.Manifest;
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

import tysheng.sxbus.BuildConfig;
import tysheng.sxbus.Constant;
import tysheng.sxbus.R;
import tysheng.sxbus.presenter.base.AbstractPresenter;
import tysheng.sxbus.ui.inter.MoreView;
import tysheng.sxbus.utils.AlipayZeroSdk;
import tysheng.sxbus.utils.PermissionUtil;
import tysheng.sxbus.utils.SPHelper;
import tysheng.sxbus.utils.SystemUtil;
import tysheng.sxbus.view.ChooseCityFragment;

/**
 * Created by tysheng
 * Date: 2017/2/28 17:00.
 * Email: tyshengsx@gmail.com
 */

public class MorePresenterImpl extends AbstractPresenter<MoreView> {

    public MorePresenterImpl(MoreView view) {
        super(view);
    }

    /**
     * 根据应用包名，跳转到应用市场
     *
     * @param context     承载跳转的Activity
     * @param packageName 所需下载（评论）的应用包名
     */
    public static void shareAppShop(Context context, String packageName) {
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

    }

    public void showAlipayFail(String s) {
        mView.snackBarShow(s);
        ClipboardManager c = (ClipboardManager) getContext().getSystemService(Context.CLIPBOARD_SERVICE);
        c.setPrimaryClip(ClipData.newPlainText("email", getContext().getString(R.string.my_email)));//设置Clipboard 的内容
    }

    public void checkVersion() {
        shareAppShop(getContext(), BuildConfig.APPLICATION_ID);
    }

    public void setStationMode() {
        new AlertDialog.Builder(getContext())
                .setItems(new String[]{"根据站点", "根据距离"}, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        SPHelper.put(Constant.STATION_MODE, which);
                    }
                })
                .show();
    }

    public void chooseCity() {
        ChooseCityFragment f = new ChooseCityFragment();
        f.show(getChildFragmentManager(), "");
    }

    public void donate() {
        if (AlipayZeroSdk.hasInstalledAlipayClient(getContext())) {
            if (!AlipayZeroSdk.startAlipayClient(getActivity())) {
                showAlipayFail(getContext().getString(R.string.msg_alipay_copied));
            }
        } else {
            showAlipayFail(getContext().getString(R.string.msg_alipay_copied));
        }
    }

    public void askPermission() {
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

    public void feedback() {
        SystemUtil.sendEmail(getContext());
    }

    public void bikeInfo() {

    }
}
