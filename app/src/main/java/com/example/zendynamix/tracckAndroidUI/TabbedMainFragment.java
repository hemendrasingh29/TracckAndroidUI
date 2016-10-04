package com.example.zendynamix.tracckAndroidUI;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by zendynamix on 7/29/2016.
 */
public class TabbedMainFragment extends Fragment {
    private static final String LOG_TAG = "TABBED_FRAGMENT";
    private static final String LIVE_LIST = "liveList";
    private static final String ARCHIVE_LIST = "archiveList";
    private List<ItemData> mainLst, archiveList;

    public TabbedMainFragment() {
    }

    private TabLayout tabLayout;
    private ViewPager viewPager;


    static TabbedMainFragment newInstance(List<ItemData> lst, List<ItemData> arcchive) {

        TabbedMainFragment tabbedMainFragment = new TabbedMainFragment();
        Bundle args = new Bundle();
        tabbedMainFragment.setArguments(args);
        args.putSerializable(LIVE_LIST, (Serializable) lst);
        args.putSerializable(ARCHIVE_LIST, (Serializable) arcchive);
        return tabbedMainFragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Bundle bundle = this.getArguments();
        mainLst = (List<ItemData>) bundle.getSerializable(LIVE_LIST);
        archiveList = (List<ItemData>) bundle.getSerializable(ARCHIVE_LIST);
    }
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.tabbed_main_activity, container, false);
        viewPager = (ViewPager) view.findViewById(R.id.viewpager);
        setupViewPager(viewPager);
        tabLayout = (TabLayout) view.findViewById(R.id.tabs);
        tabLayout.setupWithViewPager(viewPager);
        return view;
    }

    private void setupViewPager(ViewPager viewPager) {
        ViewPagerAdapter adapter = new ViewPagerAdapter(getActivity().getSupportFragmentManager());
        adapter.addFragment(LiveListItem.newInstance(mainLst), "LIVE");
        adapter.addFragment(ArchiveListItem.newInstance(archiveList), "ARCHIVE");
        viewPager.setAdapter(adapter);
    }

    class ViewPagerAdapter extends FragmentPagerAdapter {
        private final List<Fragment> mFragmentList = new ArrayList<>();
        private final List<String> mFragmentTitleList = new ArrayList<>();

        public ViewPagerAdapter(FragmentManager manager) {
            super(manager);
        }

        @Override
        public Fragment getItem(int position) {
            return mFragmentList.get(position);
        }

        @Override
        public int getCount() {
            return mFragmentList.size();
        }

        public void addFragment(Fragment fragment, String title) {
            mFragmentList.add(fragment);
            mFragmentTitleList.add(title);
        }

        @Override
        public CharSequence getPageTitle(int position) {
            return mFragmentTitleList.get(position);
        }
    }
}



