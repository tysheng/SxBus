package tysheng.sxbus.presenter.impl;

import android.app.ProgressDialog;
import android.content.ClipData;
import android.content.ClipboardManager;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.text.SpannableString;
import android.text.Spanned;
import android.text.style.RelativeSizeSpan;

import com.baidu.autoupdatesdk.AppUpdateInfo;
import com.baidu.autoupdatesdk.AppUpdateInfoForInstall;
import com.baidu.autoupdatesdk.BDAutoUpdateSDK;
import com.baidu.autoupdatesdk.CPCheckUpdateCallback;

import tysheng.sxbus.Constant;
import tysheng.sxbus.R;
import tysheng.sxbus.presenter.base.AbstractPresenter;
import tysheng.sxbus.ui.inter.MoreView;
import tysheng.sxbus.utils.AlipayZeroSdk;
import tysheng.sxbus.utils.SPHelper;
import tysheng.sxbus.utils.SystemUtil;
import tysheng.sxbus.view.ChooseCityFragment;

/**
 * Created by tysheng
 * Date: 2017/2/28 17:00.
 * Email: tyshengsx@gmail.com
 */

public class MorePresenterImpl extends AbstractPresenter<MoreView> {
    private SPHelper mSPHelper;

    public MorePresenterImpl(MoreView view) {
        super(view);
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

    public void checkVersionByBaidu() {
        final ProgressDialog dialog = new ProgressDialog(getContext());
        dialog.setMessage(getContext().getString(R.string.msg_checking_update));
        dialog.show();
        BDAutoUpdateSDK.cpUpdateCheck(getContext(), new CPCheckUpdateCallback() {
            @Override
            public void onCheckUpdateCallback(AppUpdateInfo appUpdateInfo, AppUpdateInfoForInstall appUpdateInfoForInstall) {
                dialog.dismiss();
                if (appUpdateInfo != null && appUpdateInfo.getAppVersionCode() > SystemUtil.getVersionCode()) {
                    BDAutoUpdateSDK.uiUpdateAction(getContext(),
                            new com.baidu.autoupdatesdk.UICheckUpdateCallback() {
                                @Override
                                public void onCheckComplete() {

                                }
                            });
                } else {
                    mView.snackBarShow(getContext().getString(R.string.msg_no_need_update));
                }
            }
        });
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

    public void feedback() {
        SystemUtil.sendEmail(getContext());
    }
}
