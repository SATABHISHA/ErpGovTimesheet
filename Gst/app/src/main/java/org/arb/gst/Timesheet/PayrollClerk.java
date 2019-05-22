package org.arb.gst.Timesheet;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.FragmentManager;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Gravity;
import android.view.View;
import android.widget.ImageButton;
import android.widget.TextView;

import org.arb.gst.R;
import org.arb.gst.fragment.DrawerSupervisorFragment;

public class PayrollClerk extends AppCompatActivity {
    TextView tv_payrollclerk_period_date;
    ImageButton imgbtn_filter;
    DrawerLayout drawer_layout;
    FragmentManager fragmentManager = getSupportFragmentManager();
    DrawerSupervisorFragment drawerSupervisorFragment;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_payrollclerk);
        imgbtn_filter = (ImageButton)findViewById(R.id.imgbtn_filter);
        drawer_layout = (DrawerLayout)findViewById(R.id.drawer_layout);
        drawerSupervisorFragment = (DrawerSupervisorFragment)fragmentManager.findFragmentById(R.id.fragmentitem);

        //----------onClick to open the drawer code starts---------
        imgbtn_filter.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                drawer_layout.openDrawer(Gravity.RIGHT);
            }
        });
        //----------onClick to open the drawer code ends---------

        //--------Toolbar code starts--------
        final Toolbar mToolbar = (Toolbar) findViewById(R.id.toolbar);
        mToolbar.setTitle("Select Employee");
        setSupportActionBar(mToolbar);
        tv_payrollclerk_period_date = (TextView)findViewById(R.id.tv_payrollclerk_period_date);
        tv_payrollclerk_period_date.setText("( "+TimesheetHome.period_start_date+" - "+TimesheetHome.period_end_date+" )");


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
    }

    public void onBackPressed() {
        super.onBackPressed();
    }
}
