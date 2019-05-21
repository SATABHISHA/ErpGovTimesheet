package org.arb.gst.Timesheet;

import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.TextView;

import org.arb.gst.R;
import org.arb.gst.fragment.SubcontractorEmployeeListFragment;
import org.arb.gst.fragment.SubordinateListFragment;

public class Subordinate extends AppCompatActivity {
    ViewPager viewPager;
    TextView tv_subordinate_period_date;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_subordinate);

        //--------Toolbar code starts--------
        final Toolbar mToolbar = (Toolbar) findViewById(R.id.toolbar);
        mToolbar.setTitle("Select Employee");
        setSupportActionBar(mToolbar);
        tv_subordinate_period_date = (TextView)findViewById(R.id.tv_subordinate_period_date);
        tv_subordinate_period_date.setText("( "+TimesheetHome.period_start_date+" - "+TimesheetHome.period_end_date+" )");


        if (getSupportActionBar() != null) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        }
        mToolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onBackPressed();
            }
        });

        //--------Toolbar code ends--------

        //-------------ViewPager and tablayout code starts here inside onCreate()--------------
        TabLayout tabLayout = (TabLayout) findViewById(R.id.tabs);
        tabLayout.addTab(tabLayout.newTab().setText("Subordinate List"));
        tabLayout.addTab(tabLayout.newTab().setText("Sub-Contractor Employees"));
        tabLayout.setTabGravity(TabLayout.GRAVITY_FILL);

        viewPager = (ViewPager) findViewById(R.id.viewpager);
        final ViewPagerAdapter adapter = new ViewPagerAdapter
                (getSupportFragmentManager(), tabLayout.getTabCount());
        viewPager.setAdapter(adapter);


        viewPager.addOnPageChangeListener(new TabLayout.TabLayoutOnPageChangeListener(tabLayout));
        tabLayout.setSelectedTabIndicatorColor(Color.parseColor("#FFFFFF"));
        tabLayout.setOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                viewPager.setCurrentItem(tab.getPosition());
            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab) {

            }

            @Override
            public void onTabReselected(TabLayout.Tab tab) {
            }
        });
        //----ViePager and Tablayout code ends---------
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
    }

    //----------Viepager Adapter code starts----------
    public class ViewPagerAdapter extends FragmentStatePagerAdapter {
        int mNumOfTabs;

        public ViewPagerAdapter(FragmentManager fm, int NumOfTabs) {
            super(fm);
            this.mNumOfTabs = NumOfTabs;
        }
        @Override
        public Fragment getItem(int position) {

            switch (position) {
                case 0:
                    SubordinateListFragment subordinateListFragment = new SubordinateListFragment();
                    return subordinateListFragment;
                case 1:
                    SubcontractorEmployeeListFragment subcontractorEmployeeListFragment = new SubcontractorEmployeeListFragment();
                    return subcontractorEmployeeListFragment;
                default:
                    return null;
            }
        }

        @Override
        public int getCount() {
            return mNumOfTabs;
        }
    }
    //----------Viepager Adapter code ends----------
}
