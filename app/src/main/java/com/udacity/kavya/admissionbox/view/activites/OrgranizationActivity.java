package com.udacity.kavya.admissionbox.view.activites;

import android.content.Intent;
import android.graphics.Typeface;
import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.widget.LinearLayout;

import com.udacity.kavya.admissionbox.R;
import com.udacity.kavya.admissionbox.utils.FontManager;
import com.udacity.kavya.admissionbox.view.fragments.ChatsFragment;
import com.udacity.kavya.admissionbox.view.fragments.HomeFragment;
import com.udacity.kavya.admissionbox.view.fragments.MoreFragment;
import com.udacity.kavya.admissionbox.view.fragments.OrgDetailFragment;
import com.udacity.kavya.admissionbox.view.fragments.RootFragment;

import java.util.ArrayList;
import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;


public class OrgranizationActivity extends AppCompatActivity  {

    @Bind(R.id.toolbar)
    Toolbar mToolbar;
    @Bind(R.id.tab_layout)
    TabLayout mTabLayout;
    @Bind(R.id.viewpager)
    ViewPager mViewpager;

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);
        setSupportActionBar(mToolbar);
        setupViewPager(mViewpager);
        mTabLayout.setupWithViewPager(mViewpager);
        setupTabIcons();
        Typeface iconFont = FontManager.getTypeface(getApplicationContext(), FontManager.FONTAWESOME);
        FontManager.markAsIconContainer(findViewById(R.id.main_layout), iconFont);

        // Initialize message ListView and its adapter
        mTabLayout.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {

            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab) {

            }

            @Override
            public void onTabReselected(TabLayout.Tab tab) {

            }
        });
    }

    @Override
    protected void onStart() {
        super.onStart();
    }

    /**
     * Adding custom view to tab
     */
    private void setupTabIcons() {

        LinearLayout tabOne = (LinearLayout) LayoutInflater.from(this).inflate(R.layout.tab_hom, null);
        mTabLayout.getTabAt(0).setCustomView(tabOne);

        LinearLayout tabTwo = (LinearLayout) LayoutInflater.from(this).inflate(R.layout.tab_chats, null);
        mTabLayout.getTabAt(1).setCustomView(tabTwo);

        LinearLayout tabThree = (LinearLayout) LayoutInflater.from(this).inflate(R.layout.tab_more, null);
        mTabLayout.getTabAt(2).setCustomView(tabThree);
    }

    /**
     * Adding fragments to ViewPager
     *
     * @param viewPager
     */
    private void setupViewPager(ViewPager viewPager) {
        ViewPagerAdapter adapter = new ViewPagerAdapter(getSupportFragmentManager());
        adapter.addFrag(new HomeFragment(), getString(R.string.tab_home));
        adapter.addFrag(new ChatsFragment(), getString(R.string.tab_chats));
        adapter.addFrag(new MoreFragment(), getString(R.string.tab_more));
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
            if (position == 0)
                return new RootFragment();
            else
                return mFragmentList.get(position);
        }

        @Override
        public int getCount() {
            return mFragmentList.size();
        }

        public void addFrag(Fragment fragment, String title) {
            mFragmentList.add(fragment);
            mFragmentTitleList.add(title);
        }

        @Override
        public CharSequence getPageTitle(int position) {
            return mFragmentTitleList.get(position);
        }
    }

    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            // Respond to the action bar's Up/Home button
            case android.R.id.home:
                getSupportFragmentManager().popBackStack();
                return true;
        }
        return super.onOptionsItemSelected(item);
    }

    public void showUpButton() {
        if (getSupportActionBar() != null)
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
    }

    public void hideUpButton() {
        if (getSupportActionBar() != null)
            getSupportActionBar().setDisplayHomeAsUpEnabled(false);
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        List<Fragment> frags = getSupportFragmentManager().getFragments();
        if (frags != null) {
            for (Fragment f : frags) {
                if (f != null)
                    handleResult(f, requestCode, resultCode, data);
            }
        }
    }

    private void handleResult(Fragment frag, int requestCode, int resultCode, Intent data) {
        if (frag instanceof RootFragment) { // custom interface with no signitures
            frag.onActivityResult(requestCode, resultCode, data);
        }
    }

}
