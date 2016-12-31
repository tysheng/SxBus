package tysheng.sxbus.ui;

import android.app.ProgressDialog;
import android.content.ClipData;
import android.content.ClipboardManager;
import android.content.Context;
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
import tysheng.sxbus.App;
import tysheng.sxbus.R;
import tysheng.sxbus.base.BaseFragment;
import tysheng.sxbus.utils.AlipayZeroSdk;
import tysheng.sxbus.utils.SystemUtil;
import tysheng.sxbus.view.ChooseCityFragment;

/**
 * Created by Sty
 * Date: 16/9/11 20:23.
 */
public class MoreFragment extends BaseFragment {
    @BindView(R.id.toolbar)
    Toolbar mToolbar;

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


    public void showAlipayFail(String s) {
        ((MainActivity) getActivity()).showSnackBar(s, false);
        ClipboardManager c = (ClipboardManager) getActivity().getSystemService(Context.CLIPBOARD_SERVICE);
        c.setPrimaryClip(ClipData.newPlainText("email", "353491983@qq.com"));//设置Clipboard 的内容
    }

    public void showMsg(String s) {
        ((MainActivity) getActivity()).showSnackBar(s, false);
        ClipboardManager c = (ClipboardManager) getActivity().getSystemService(Context.CLIPBOARD_SERVICE);
        c.setPrimaryClip(ClipData.newPlainText("wechat", "353491983"));//设置Clipboard 的内容
    }

    public void checkVersionByBaidu() {
        final ProgressDialog dialog = new ProgressDialog(mActivity);
        dialog.setMessage("正在检查新版本...");
        dialog.show();
        BDAutoUpdateSDK.cpUpdateCheck(mActivity, new CPCheckUpdateCallback() {
            @Override
            public void onCheckUpdateCallback(AppUpdateInfo appUpdateInfo, AppUpdateInfoForInstall appUpdateInfoForInstall) {
                if (appUpdateInfo != null && appUpdateInfo.getAppVersionCode() > SystemUtil.getVersionCode()) {
                    dialog.dismiss();
                    BDAutoUpdateSDK.uiUpdateAction(mActivity,
                            new com.baidu.autoupdatesdk.UICheckUpdateCallback() {
                                @Override
                                public void onCheckComplete() {

                                }
                            });
                } else {
                    dialog.dismiss();
                    ((MainActivity) getActivity()).showSnackBar("当前版本已是最新版", false);
                }
            }
        });
    }

    private void chooseCity() {
        ChooseCityFragment f = new ChooseCityFragment();
        f.show(getFragmentManager(), "");
    }


    @OnClick({R.id.feedback, R.id.donate, R.id.check_update, R.id.chooseCity})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.feedback:
                SystemUtil.sendEmail(getActivity());
                break;
            case R.id.donate:
                if (AlipayZeroSdk.hasInstalledAlipayClient(App.get())) {
                    if (!AlipayZeroSdk.startAlipayClient(getActivity())) {
                        showAlipayFail("支付宝账号已复制到剪贴板");
                    }
                } else
                    showAlipayFail("支付宝账号已复制到剪贴板");
                break;
            case R.id.check_update:
                checkVersionByBaidu();
                break;
            case R.id.chooseCity:
                chooseCity();
                break;
        }
    }


}
