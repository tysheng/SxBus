package tysheng.sxbus.ui.fragments;

import android.app.Dialog;
import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.DialogFragment;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
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
import tysheng.sxbus.adapter.OpenSourceAdapter;
import tysheng.sxbus.base.BaseRecyclerViewAdapter;
import tysheng.sxbus.bean.CitySection;
import tysheng.sxbus.bean.OpenSourceInfo;
import tysheng.sxbus.databinding.FragmentStarBinding;

/**
 * Created by Sty
 * Date: 16/9/15 10:15.
 */
public class ListDialogFragment extends DialogFragment {

    public static final int CHOOSE_CITY = 0, OPEN_SOURCE = 1;
    public static String[] names = new String[]{
            "海南省", "三亚", "江苏省", "江阴", "泰州", "扬州", "镇江", "黑龙江省", "七台河", "河北省", "石家庄", "廊坊",
            "四川省", "泸州", "河南省", "许昌", "济源", "山东省", "济南", "章丘", "济宁", "泰安", "淄博", "威海", "聊城",
            "浙江省", "杭州", "温州", "绍兴", "衢州", "诸暨", "湖南省", "湘潭", "祁阳", "辽宁省", "锦州", "葫芦岛"
    };
    public static String[] openSourceName = new String[]{
            "RxJava 2.0", "Retrofit 2.0", "RxPermissions", "RxLifecycle", "Dagger 2", "GreenDAO 3.0",
            "BaseRecyclerViewAdapterHelper", "fastjson"
    };
    public static String[] openSourceIntro = new String[]{
            "Reactive Extensions for the JVM – a library for composing asynchronous and event-based programs using observable sequences for the Java VM",
            "Type-safe HTTP client for Android and Java by Square, Inc.",
            "Android runtime permissions powered by RxJava",
            "Lifecycle handling APIs for Android apps using RxJava",
            "A fast dependency injector for Android and Java.",
            "greenDAO is a light & fast ORM solution for Android that maps objects to SQLite databases.",
            "Powerful and flexible RecyclerAdapter",
            "A fast JSON parser/generator for Java"
    };

    public static ListDialogFragment newInstance(int type) {
        Bundle args = new Bundle();
        args.putInt("0", type);
        ListDialogFragment fragment = new ListDialogFragment();
        fragment.setArguments(args);
        return fragment;
    }

    private void initCity(RecyclerView recyclerView) {
        List<CitySection> list = new ArrayList<>();
        for (String s : names) {
            CitySection section = new CitySection(s);
            if (s.endsWith("省")) {
                section.type = 1;
            } else {
                section.type = 0;
            }
            list.add(section);
        }

        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        ChooseCityAdapter adapter = new ChooseCityAdapter();
        recyclerView.setAdapter(adapter);
        adapter.setData(list);
        recyclerView.addOnItemTouchListener(new OnItemChildClickListener() {
            @Override
            public void onSimpleItemChildClick(BaseQuickAdapter baseQuickAdapter, View view, int i) {
                if (view.getId() == R.id.textView) {
                    Toast.makeText(App.get(), "功能暂未开放", Toast.LENGTH_SHORT).show();
                    dismissAllowingStateLoss();
                }
            }
        });
    }

    @NonNull
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {

        FragmentStarBinding binding = DataBindingUtil.inflate(getActivity().getLayoutInflater(), R.layout.fragment_star, (ViewGroup) getView(), false);

        int type = getArguments().getInt("0");
        if (type == CHOOSE_CITY) {
            initCity(binding.recyclerView);
        } else if (type == OPEN_SOURCE) {
            initOpenSource(binding.recyclerView);
        }
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        builder.setView(binding.getRoot());
        return builder.create();
    }

    private void initOpenSource(RecyclerView recyclerView) {
        List<OpenSourceInfo> list = new ArrayList<>();
        for (int i = 0; i < openSourceName.length; i++) {
            OpenSourceInfo info = new OpenSourceInfo();
            info.name = openSourceName[i];
            info.intro = openSourceIntro[i];
            list.add(info);
        }
        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        OpenSourceAdapter adapter = new OpenSourceAdapter();
        recyclerView.setAdapter(adapter);
        recyclerView.setHasFixedSize(true);
        adapter.setData(list);
        adapter.setOnItemClickListener(new BaseRecyclerViewAdapter.SimpleItemClickListener() {
            @Override
            public void onItemClick(View view, int position) {
                super.onItemClick(view, position);
                dismissAllowingStateLoss();
            }
        });
    }
}
