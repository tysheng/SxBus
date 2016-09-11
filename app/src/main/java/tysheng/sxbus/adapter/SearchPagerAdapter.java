package tysheng.sxbus.adapter;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.support.v4.app.FragmentTransaction;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Sty
 * Date: 16/8/11 21:36.
 */
public class SearchPagerAdapter extends FragmentStatePagerAdapter {
    private List<Fragment> mFragments;
    private List<String> mFragmentTitles;
    private FragmentManager mFragmentManager;

    public SearchPagerAdapter(FragmentManager fm) {
        super(fm);
        mFragments = new ArrayList<>();
        mFragmentTitles = new ArrayList<>();
        mFragmentManager = fm;
    }
    public void addFragment(Fragment fragment, String title) {
        mFragments.add(fragment);
        mFragmentTitles.add(title);
    }
    public void addFragments(List<Fragment> fragments, List<String> titles) {
        mFragments.addAll(fragments);
        mFragmentTitles.addAll(titles);
        notifyDataSetChanged();
    }

    public void clear() {
        FragmentTransaction ft = mFragmentManager.beginTransaction();
        for (Fragment f : this.mFragments) {
            ft.remove(f);
        }
        ft.commit();
        mFragmentManager.executePendingTransactions();

        mFragments.clear();
        mFragmentTitles.clear();
    }

    @Override
    public Fragment getItem(int position) {
        return mFragments.get(position);
    }

    @Override
    public int getCount() {
        return mFragments.size();
    }

    @Override
    public int getItemPosition(Object object) {
        return POSITION_NONE;
    }

    @Override
    public CharSequence getPageTitle(int position) {
        return mFragmentTitles.get(position);
    }
}
