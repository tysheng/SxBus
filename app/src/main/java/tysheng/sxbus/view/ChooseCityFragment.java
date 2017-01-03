package tysheng.sxbus.view;

import android.app.Dialog;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.DialogFragment;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.listener.OnItemChildClickListener;

import java.util.ArrayList;
import java.util.List;

import tysheng.sxbus.App;
import tysheng.sxbus.R;
import tysheng.sxbus.adapter.ChooseCityAdapter;
import tysheng.sxbus.bean.CitySection;
import tysheng.sxbus.utils.SPHelper;

/**
 * Created by Sty
 * Date: 16/9/15 10:15.
 */
public class ChooseCityFragment extends DialogFragment {

    public static String[] names = new String[]{
            "海南省", "三亚", "江苏省", "江阴", "泰州", "扬州", "镇江", "黑龙江省", "七台河", "河北省", "石家庄", "廊坊",
            "四川省", "泸州", "河南省", "许昌", "济源", "山东省", "济南", "章丘", "济宁", "泰安", "淄博", "威海", "聊城",
            "浙江省", "杭州", "温州", "绍兴", "衢州", "诸暨", "湖南省", "湘潭", "祁阳", "辽宁省", "锦州", "葫芦岛"
    };

    List<CitySection> mList;
    SPHelper helper;

    @NonNull
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        helper = new SPHelper(getContext());
        mList = new ArrayList<>();
        for (String s : names) {
            CitySection section;
            if (s.endsWith("省")) {
                section = new CitySection(true, s);
            } else {
                section = new CitySection(s);
            }
            mList.add(section);
        }
        View view = LayoutInflater.from(getActivity())
                .inflate(R.layout.fragment_star, (ViewGroup) getView());
        RecyclerView recyclerView = (RecyclerView) view.findViewById(R.id.recyclerView);
        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        ChooseCityAdapter adapter = new ChooseCityAdapter(mList);
        recyclerView.setAdapter(adapter);
        recyclerView.addOnItemTouchListener(new OnItemChildClickListener() {
            @Override
            public void onSimpleItemChildClick(BaseQuickAdapter baseQuickAdapter, View view, int i) {
                if (view.getId() == R.id.textView) {
//                    helper.put("city", names[i]);
//                    LogUtil.d("name" + names[i]);
                    Toast.makeText(App.get(), "功能暂未开放", Toast.LENGTH_SHORT).show();
                    dismiss();
                }
            }
        });
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        builder.setView(view);
        return builder.create();
    }
}
