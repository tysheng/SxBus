package tysheng.sxbus.ui;

import android.content.ClipData;
import android.content.ClipboardManager;
import android.content.Context;
import android.view.View;
import android.widget.LinearLayout;

import butterknife.BindView;
import butterknife.OnClick;
import tysheng.sxbus.App;
import tysheng.sxbus.R;
import tysheng.sxbus.base.BaseFragment;
import tysheng.sxbus.utils.AlipayZeroSdk;
import tysheng.sxbus.utils.SnackBarUtil;

/**
 * Created by Sty
 * Date: 16/9/11 20:23.
 */
public class MoreFragment extends BaseFragment {
    @BindView(R.id.feedback)
    LinearLayout mFeedback;
    @BindView(R.id.donate)
    LinearLayout mDonate;

    @Override
    protected int getLayoutId() {
        return R.layout.fragment_more;
    }

    @Override
    protected void initData() {

    }


    public void showAlipayFail(String s) {
        SnackBarUtil.show(getView(), s);
        ClipboardManager c = (ClipboardManager) getActivity().getSystemService(Context.CLIPBOARD_SERVICE);
        c.setPrimaryClip(ClipData.newPlainText("email", "353491983@qq.com"));//设置Clipboard 的内容
    }



    @OnClick({R.id.feedback, R.id.donate})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.feedback:
                showAlipayFail("邮箱已复制到剪贴板");
                break;
            case R.id.donate:
                if (AlipayZeroSdk.hasInstalledAlipayClient(App.get())) {
                    if (!AlipayZeroSdk.startAlipayClient(getActivity())){
                        showAlipayFail("支付宝账号已复制到剪贴板");
                    }
                } else
                    showAlipayFail("支付宝账号已复制到剪贴板");
                break;
        }
    }
}
