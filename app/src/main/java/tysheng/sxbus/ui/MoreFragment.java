package tysheng.sxbus.ui;

import android.app.ProgressDialog;
import android.content.ClipData;
import android.content.ClipboardManager;
import android.content.Context;
import android.content.DialogInterface;
import android.support.design.widget.CoordinatorLayout;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.Toolbar;
import android.text.SpannableString;
import android.text.Spanned;
import android.text.style.RelativeSizeSpan;
import android.view.View;

import com.baidu.autoupdatesdk.AppUpdateInfo;
import com.baidu.autoupdatesdk.AppUpdateInfoForInstall;
import com.baidu.autoupdatesdk.BDAutoUpdateSDK;
import com.baidu.autoupdatesdk.CPCheckUpdateCallback;

import butterknife.BindView;
import butterknife.OnClick;
import tysheng.sxbus.Constant;
import tysheng.sxbus.R;
import tysheng.sxbus.base.BaseFragment;
import tysheng.sxbus.utils.AlipayZeroSdk;
import tysheng.sxbus.utils.SPHelper;
import tysheng.sxbus.utils.SnackBarUtil;
import tysheng.sxbus.utils.SystemUtil;
import tysheng.sxbus.view.ChooseCityFragment;

/**
 * Created by Sty
 * Date: 16/9/11 20:23.
 */
public class MoreFragment extends BaseFragment {
    @BindView(R.id.toolbar)
    Toolbar mToolbar;
    @BindView(R.id.coordinatorLayout)
    CoordinatorLayout mCoordinatorLayout;
    private SPHelper mSPHelper;

    @Override
    protected int getLayoutId() {
        return R.layout.fragment_more;
    }

    @Override
    protected void initData() {
        SpannableString string = new SpannableString("版本 " + SystemUtil.getVersionName());
        RelativeSizeSpan sizeSpan = new RelativeSizeSpan(0.75f);
        string.setSpan(sizeSpan, 3, 6, Spanned.SPAN_INCLUSIVE_EXCLUSIVE);
        mToolbar.setTitle(string);
    }

    private void showAlipayFail(String s) {
        SnackBarUtil.show(mCoordinatorLayout, s, Snackbar.LENGTH_SHORT);
        ClipboardManager c = (ClipboardManager) getContext().getSystemService(Context.CLIPBOARD_SERVICE);
        c.setPrimaryClip(ClipData.newPlainText("email", getString(R.string.my_email)));//设置Clipboard 的内容
    }

    public void checkVersionByBaidu() {
        final ProgressDialog dialog = new ProgressDialog(getContext());
        dialog.setMessage(getString(R.string.msg_checking_update));
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
                    SnackBarUtil.show(mCoordinatorLayout, getString(R.string.msg_no_need_update), Snackbar.LENGTH_SHORT);
                }
            }
        });
    }

    private void chooseCity() {
        ChooseCityFragment f = new ChooseCityFragment();
        f.show(getChildFragmentManager(), "");
    }

    private SPHelper getSPHelper() {
        if (mSPHelper == null) {
            mSPHelper = new SPHelper(getContext());
        }
        return mSPHelper;
    }

    @OnClick({R.id.feedback, R.id.donate, R.id.check_update, R.id.chooseCity, R.id.stationMode})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.feedback:
                SystemUtil.sendEmail(getContext());
                break;
            case R.id.donate:
                if (AlipayZeroSdk.hasInstalledAlipayClient(getContext())) {
                    if (!AlipayZeroSdk.startAlipayClient(getActivity())) {
                        showAlipayFail(getString(R.string.msg_alipay_copied));
                    }
                } else
                    showAlipayFail(getString(R.string.msg_alipay_copied));
                break;
            case R.id.check_update:
                checkVersionByBaidu();
                break;
            case R.id.chooseCity:
                chooseCity();
                break;
            case R.id.stationMode:
                new AlertDialog.Builder(getContext())
                        .setItems(new String[]{"根据站点", "根据距离"}, new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                getSPHelper().put(Constant.STATION_MODE, which);
                            }
                        })
                        .show();
                break;
        }
    }


}
