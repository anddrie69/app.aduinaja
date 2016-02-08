package com.aduinaja.aduinaja;

import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.view.ViewPager;
import android.view.View;
import android.widget.TabHost;
import android.widget.Toolbar;

import java.util.HashMap;
import java.util.List;
import java.util.Vector;

/**
 * Created by Triyandi on 06/02/2016.
 */
public class TabsViewPagerFragmentActivity extends FragmentActivity implements TabHost.OnTabChangeListener, ViewPager.OnPageChangeListener{

    private TabHost mTabHost;
    private ViewPager mViewPager;
    private HashMap<String, TabInfo> mapTabInfo = new HashMap<String, TabInfo>();
    private PagerAdapter mPagerAdapter;
    /**
     *
     * @author mwho
     * Maintains extrinsic info of a tab's construct
     */
    private class TabInfo {
        private String tag;
        private Class<?> clss;
        private Bundle args;
        private Fragment fragment;
        TabInfo(String tag, Class<?> clazz, Bundle args) {
            this.tag = tag;
            this.clss = clazz;
            this.args = args;
        }

    }
    /**
     * A simple factory that returns dummy views to the Tabhost
     * @author mwho
     */
    class TabFactory implements TabHost.TabContentFactory {

        private final Context mContext;

        /**
         * @param context
         */
        public TabFactory(Context context) {
            mContext = context;
        }

        /** (non-Javadoc)
         * @see android.widget.TabHost.TabContentFactory#createTabContent(java.lang.String)
         */
        public View createTabContent(String tag) {
            View v = new View(mContext);
            v.setMinimumWidth(0);
            v.setMinimumHeight(0);
            return v;
        }

    }
    /** (non-Javadoc)
     * @see android.support.v4.app.FragmentActivity#onCreate(android.os.Bundle)
     */
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // Inflate the layout
        setContentView(R.layout.view_pager);
        // Initialise the TabHost

        this.initialiseTabHost(savedInstanceState);
        if (savedInstanceState != null) {
            mTabHost.setCurrentTabByTag(savedInstanceState.getString("tab")); //set the tab as per the saved state
        }
        // Intialise ViewPager
        this.intialiseViewPager();
    }

    /** (non-Javadoc)
     * @see android.support.v4.app.FragmentActivity#onSaveInstanceState(android.os.Bundle)
     */
    protected void onSaveInstanceState(Bundle outState) {
        outState.putString("tab", mTabHost.getCurrentTabTag()); //save the tab selected
        super.onSaveInstanceState(outState);
    }

    /**
     * Initialise ViewPager
     */
    private void intialiseViewPager() {

        List<Fragment> fragments = new Vector<Fragment>();
        fragments.add(Fragment.instantiate(this, LihatAduan.class.getName()));
        fragments.add(Fragment.instantiate(this, LihatAduan.class.getName()));
        fragments.add(Fragment.instantiate(this, LihatAduan.class.getName()));
        fragments.add(Fragment.instantiate(this, LihatAduan.class.getName()));
        fragments.add(Fragment.instantiate(this, LihatAduan.class.getName()));
        fragments.add(Fragment.instantiate(this, LihatAduan.class.getName()));
        fragments.add(Fragment.instantiate(this, LihatAduan.class.getName()));
        fragments.add(Fragment.instantiate(this, LihatAduan.class.getName()));
        this.mPagerAdapter  = new PagerAdapter(super.getSupportFragmentManager(), fragments);

        this.mViewPager = (ViewPager)super.findViewById(R.id.viewpager);
        this.mViewPager.setAdapter(this.mPagerAdapter);
        this.mViewPager.setOnPageChangeListener(this);
    }

    /**
     * Initialise the Tab Host
     */
    private void initialiseTabHost(Bundle args) {
        mTabHost = (TabHost)findViewById(android.R.id.tabhost);
        mTabHost.setup();
        TabInfo tabInfo = null;
        TabsViewPagerFragmentActivity.AddTab(this, this.mTabHost, this.mTabHost.newTabSpec("Transportasi").setIndicator("Transportasi"), (tabInfo = new TabInfo("Transportasi", LihatAduan.class, args)));
        this.mapTabInfo.put(tabInfo.tag, tabInfo);
        TabsViewPagerFragmentActivity.AddTab(this, this.mTabHost, this.mTabHost.newTabSpec("Infrastruktur").setIndicator("Infrastruktur"), (tabInfo = new TabInfo("Infrastruktur", LihatAduan.class, args)));
        this.mapTabInfo.put(tabInfo.tag, tabInfo);
        TabsViewPagerFragmentActivity.AddTab(this, this.mTabHost, this.mTabHost.newTabSpec("Kriminal").setIndicator("Kriminal"), (tabInfo = new TabInfo("Kriminal", LihatAduan.class, args)));
        this.mapTabInfo.put(tabInfo.tag, tabInfo);
        TabsViewPagerFragmentActivity.AddTab(this, this.mTabHost, this.mTabHost.newTabSpec("Lingkungan").setIndicator("Lingkungan"), (tabInfo = new TabInfo("Lingkungan", LihatAduan.class, args)));
        this.mapTabInfo.put(tabInfo.tag, tabInfo);
        TabsViewPagerFragmentActivity.AddTab(this, this.mTabHost, this.mTabHost.newTabSpec("Sosial").setIndicator("Sosial"), (tabInfo = new TabInfo("Sosial", LihatAduan.class, args)));
        this.mapTabInfo.put(tabInfo.tag, tabInfo);
        TabsViewPagerFragmentActivity.AddTab(this, this.mTabHost, this.mTabHost.newTabSpec("Kebudayaan").setIndicator("Kebudayaan"), (tabInfo = new TabInfo("Kebudayaan", LihatAduan.class, args)));
        this.mapTabInfo.put(tabInfo.tag, tabInfo);
        TabsViewPagerFragmentActivity.AddTab(this, this.mTabHost, this.mTabHost.newTabSpec("Pendidikan").setIndicator("Pendidikan"), (tabInfo = new TabInfo("Pendidikan", LihatAduan.class, args)));
        this.mapTabInfo.put(tabInfo.tag, tabInfo);
        TabsViewPagerFragmentActivity.AddTab(this, this.mTabHost, this.mTabHost.newTabSpec("Kesehatan").setIndicator("Kesehatan"), (tabInfo = new TabInfo("Kesehatan", LihatAduan.class, args)));
        this.mapTabInfo.put(tabInfo.tag, tabInfo);

        //Default to first tab
        this.onTabChanged("Transportasi");

        mTabHost.setOnTabChangedListener(this);
    }

    /**
     * Add Tab content to the Tabhost
     * @param activity
     * @param tabHost
     * @param tabSpec
     * @param tabInfo
     */
    private static void AddTab(TabsViewPagerFragmentActivity activity, TabHost tabHost, TabHost.TabSpec tabSpec, TabInfo tabInfo) {
        // Attach a Tab view factory to the spec
        tabSpec.setContent(activity.new TabFactory(activity));
        tabHost.addTab(tabSpec);
    }

    /** (non-Javadoc)
     * @see android.widget.TabHost.OnTabChangeListener#onTabChanged(java.lang.String)
     */
    public void onTabChanged(String tag) {
        //TabInfo newTab = this.mapTabInfo.get(tag);
        int pos = this.mTabHost.getCurrentTab();
        this.mViewPager.setCurrentItem(pos);
    }

    /* (non-Javadoc)
     * @see android.support.v4.view.ViewPager.OnPageChangeListener#onPageScrolled(int, float, int)
     */
    @Override
    public void onPageScrolled(int position, float positionOffset,
                               int positionOffsetPixels) {
        // TODO Auto-generated method stub

    }

    /* (non-Javadoc)
     * @see android.support.v4.view.ViewPager.OnPageChangeListener#onPageSelected(int)
     */
    @Override
    public void onPageSelected(int position) {
        // TODO Auto-generated method stub
        this.mTabHost.setCurrentTab(position);
    }

    /* (non-Javadoc)
     * @see android.support.v4.view.ViewPager.OnPageChangeListener#onPageScrollStateChanged(int)
     */
    @Override
    public void onPageScrollStateChanged(int state) {
        // TODO Auto-generated method stub

    }
}
