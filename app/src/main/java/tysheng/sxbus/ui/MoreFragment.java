package tysheng.sxbus.ui;

import android.content.ClipData;
import android.content.ClipboardManager;
import android.content.Context;
import android.view.View;
import android.widget.TextView;

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
import tysheng.sxbus.utils.SnackBarUtil;
import tysheng.sxbus.utils.SystemUtil;

/**
 * Created by Sty
 * Date: 16/9/11 20:23.
 */
public class MoreFragment extends BaseFragment {
    @BindView(R.id.more)
    TextView mMore;

    @Override
    protected int getLayoutId() {
        return R.layout.fragment_more;
    }

    @Override
    protected void initData() {
        mMore.setText("版本: " + SystemUtil.getVersionName());
    }


    public void showAlipayFail(String s) {
        SnackBarUtil.show(getView(), s);
        ClipboardManager c = (ClipboardManager) getActivity().getSystemService(Context.CLIPBOARD_SERVICE);
        c.setPrimaryClip(ClipData.newPlainText("email", "353491983@qq.com"));//设置Clipboard 的内容
    }

    public void showMsg(String s) {
        SnackBarUtil.show(getView(), s);
        ClipboardManager c = (ClipboardManager) getActivity().getSystemService(Context.CLIPBOARD_SERVICE);
        c.setPrimaryClip(ClipData.newPlainText("wechat", "353491983"));//设置Clipboard 的内容
    }

    public void checkVersionByBaidu() {
        BDAutoUpdateSDK.cpUpdateCheck(mActivity, new CPCheckUpdateCallback() {
            @Override
            public void onCheckUpdateCallback(AppUpdateInfo appUpdateInfo, AppUpdateInfoForInstall appUpdateInfoForInstall) {
                if (appUpdateInfo != null && appUpdateInfo.getAppVersionCode() > SystemUtil.getVersionCode()) {
                    BDAutoUpdateSDK.uiUpdateAction(mActivity,
                            new com.baidu.autoupdatesdk.UICheckUpdateCallback() {
                                @Override
                                public void onCheckComplete() {
                                }
                            });
                } else {
                    SnackBarUtil.show(getView(), "当前版本已是最新版");
                }
            }
        });
    }

    @OnClick({R.id.feedback, R.id.donate, R.id.add_wechat, R.id.check_update})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.feedback:
                showAlipayFail("邮箱已复制到剪贴板");
                break;
            case R.id.donate:
                if (AlipayZeroSdk.hasInstalledAlipayClient(App.get())) {
                    if (!AlipayZeroSdk.startAlipayClient(getActivity())) {
                        showAlipayFail("支付宝账号已复制到剪贴板");
                    }
                } else
                    showAlipayFail("支付宝账号已复制到剪贴板");
                break;
            case R.id.add_wechat:
                showMsg("微信号已复制到剪贴板");
                break;
            case R.id.check_update:
                checkVersionByBaidu();
                break;
        }
    }

}
