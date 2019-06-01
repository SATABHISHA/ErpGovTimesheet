package org.arb.gst.Timesheet;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.Nullable;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import org.arb.gst.Home.HomeActivity;
import org.arb.gst.Model.EmployeeTimesheetListModel;
import org.arb.gst.Model.UserSingletonModel;
import org.arb.gst.Model.UserUpdateHoursModel;
import org.arb.gst.R;
import org.arb.gst.adapter.CustomEmployeeTimesheetListAdapter;
import org.arb.gst.adapter.CustomEmployeeTimesheetdemoAdapter;
import org.arb.gst.config.Config;
import org.arb.gst.config.ConnectivityReceiver;
import org.arb.gst.config.MyApplication;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.json.XML;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

public class TimesheetWorkUpdateHrs extends AppCompatActivity implements View.OnClickListener, ConnectivityReceiver.ConnectivityReceiverListener {
    RecyclerView mRecyclerView;
    TextView  tv_ts_wrkhrs_date,tv_ts_wrkhrs_dayname, tv_ts_wrkhrs_date_period, tv_ts_wkhrs_update_empname;
    public static TextView tv_ts_wrkhrs_totalhrs;
    ImageButton imgbtn_ts_wrkhrs_prev, imgbtn_ts_wrkhrs_next;
    ListView listView,listz,k;
    Button btn_save, btn_back;
    ArrayList<UserUpdateHoursModel> contractArrayList = new ArrayList<>();
    List<UserUpdateHoursModel> updateHoursModelList = new ArrayList<>();
    ArrayList<EmployeeTimesheetListModel> employeeTimesheetListModelArrayList = new ArrayList<>();
    ArrayList<String> stringContractArrayList = new ArrayList<>();
    UserSingletonModel userSingletonModel = UserSingletonModel.getInstance();
    public Handler handler = new Handler();
    List<String> list = new ArrayList<>();
    public String[] valueList;
    public Double sum = 0.0;
    public static Double sum_initial_value = 0.0; //---mean to say that the initial sum total value directs to the value during page load
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_timesheet_workhrs_update);

        checkConnection();  //----function calling to check the internet connection

        //============toolbar code starts===============
        final Toolbar mToolbar = (Toolbar) findViewById(R.id.toolbar);
        mToolbar.setTitle("Timesheet Hour(s)");
        setSupportActionBar(mToolbar);

        //-------commented the following code on 3rd dec as back arrow is not required right now--------
       /* if (getSupportActionBar() != null) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        }
        mToolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(TimesheetWorkUpdateHrs.this,TimesheetSelectDay.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                startActivity(intent);
                TimesheetWorkUpdateHrs.this.finish();
//                onBackPressed();
            }
        });*/
        //-------commented the above code on 3rd dec as back arrow is not required right now--------

       //============toolbar code ends============

//        listView = (ListView)findViewById(R.id.lv_workhrs);  //======commented temporary 1st nov
        tv_ts_wrkhrs_totalhrs = (TextView)findViewById(R.id.tv_ts_wrkhrs_totalhrs);
        btn_save = (Button)findViewById(R.id.btn_sup_save);
        btn_back = (Button)findViewById(R.id.btn_back);

        tv_ts_wrkhrs_date = (TextView)findViewById(R.id.tv_ts_wrkhrs_date);
        tv_ts_wrkhrs_dayname = findViewById(R.id.tv_ts_wrkhrs_dayname);
        tv_ts_wrkhrs_date_period = (TextView)findViewById(R.id.tv_ts_wrkhrs_date_period);  //----commented on 13/11/18(as it's looking odd)
        tv_ts_wkhrs_update_empname = (TextView)findViewById(R.id.tv_ts_wkhrs_update_empname);
        imgbtn_ts_wrkhrs_prev = (ImageButton)findViewById(R.id.imgbtn_ts_wrkhrs_prev);
        imgbtn_ts_wrkhrs_next = (ImageButton)findViewById(R.id.imgbtn_ts_wrkhrs_next);
        tv_ts_wrkhrs_date.setText(userSingletonModel.getDayDate());
        //-----------added on 31st may for getting day name code starts...----------
        SimpleDateFormat dateFormat = new SimpleDateFormat("MM-dd-yyy");
        Date sourceDate = null;
        try {
            sourceDate = dateFormat.parse(userSingletonModel.getDayDate());
            SimpleDateFormat sdf = new SimpleDateFormat("EEEE");
            tv_ts_wrkhrs_dayname.setText(sdf.format(sourceDate));
        }catch(Exception e){
            e.printStackTrace();
        }
        //-----------added on 31st may for getting day name code ends...----------
        tv_ts_wrkhrs_date_period.setText("(Period: "+userSingletonModel.getPeriodStartDate()+" To "+userSingletonModel.getPeriodEndDate()+")");  //----commented on 13/11/18(as it's looking odd)
        tv_ts_wkhrs_update_empname.setText(userSingletonModel.getEmpName());
        //==========Recycler code initializing and setting layoutManager starts======
        mRecyclerView = (RecyclerView) findViewById(R.id.recycler_view);
        mRecyclerView.setHasFixedSize(true);
        mRecyclerView.setLayoutManager(new LinearLayoutManager(this));
        //==========Recycler code initializing and setting layoutManager ends======
//        getContractListData();

        checkDateWithPrevNextbutton(); //======calling function to check the first/last date for prev/next visible/invisible feature
        getEployeeTimesheetDetails(); //=====temporary commenting as server work is going on

        imgbtn_ts_wrkhrs_prev.setOnClickListener(this);
        imgbtn_ts_wrkhrs_next.setOnClickListener(this);

        btn_back.setOnClickListener(this);
        //----newly added 30th nov for making editText eidtable/non editable according to description status code starts-----
        if(userSingletonModel.getStatusDescription().contentEquals("APPROVED") || userSingletonModel.getStatusDescription().contentEquals("SUBMITTED") || userSingletonModel.getStatusDescription().contentEquals("POSTED")){
            btn_save.setClickable(false);
            btn_save.setAlpha(0.6f);
        }else{
            btn_save.setClickable(true);
            btn_save.setOnClickListener(this);
        }
        //----newly added 30th nov for making editText eidtable/non editable according to description status code ends-----
    }

    //-----newly added on 30th nov for prev/next button when selecting on the first/last date from the previous page and to make it invisible prev/next button code starts=========
    public void checkDateWithPrevNextbutton(){
        for(int i=0; i<TimesheetSelectDay.datePeriod.size(); i++){
            String value = userSingletonModel.getDayDate();
            if(value == TimesheetSelectDay.datePeriod.get(i)){
                if(i == 0){
                    imgbtn_ts_wrkhrs_prev.setVisibility(View.INVISIBLE);
                    imgbtn_ts_wrkhrs_next.setVisibility(View.VISIBLE);
                }else if(i != 0 && i == TimesheetSelectDay.datePeriod.size()-1){
                    imgbtn_ts_wrkhrs_next.setVisibility(View.GONE);
                    imgbtn_ts_wrkhrs_prev.setVisibility(View.VISIBLE);
                }else if(i !=0 && i != TimesheetSelectDay.datePeriod.size()-1){
                    imgbtn_ts_wrkhrs_next.setVisibility(View.VISIBLE);
                    imgbtn_ts_wrkhrs_prev.setVisibility(View.VISIBLE);
                }
            }
            Log.d("TimesheetDate=>",TimesheetSelectDay.datePeriod.toString());
        }
    }
    //-----newly added on 30th nov for prev/next button when selecting on the first/last date from the previous page and to make it invisible prev/next button code ends=========

    //============swicth case for button onClick() code starts============
    @Override
    public void onClick(View view) {
        int item = view.getId();
        switch (item){
            case R.id.imgbtn_ts_wrkhrs_prev:
                imgbtn_ts_wrkhrs_next.setVisibility(View.VISIBLE);
                final int Start = 0;
                final int secondStart = 1;
                if(HomeActivity.supervisor_yn_temp.contentEquals("0") && HomeActivity.payrollclerk_yn_temp.contentEquals("0") && HomeActivity.payableclerk_yn_temp.contentEquals("0")) {
                    if (userSingletonModel.getStatusDescription().contentEquals("APPROVED") || userSingletonModel.getStatusDescription().contentEquals("SUBMITTED") || userSingletonModel.getStatusDescription().contentEquals("POSTED") || userSingletonModel.getStatusDescription().contentEquals("PARTIAL_APPROVE")) {
                        for (int j = 0; j < TimesheetSelectDay.datePeriod.size(); j++) {
                            String value = tv_ts_wrkhrs_date.getText().toString();
                            if (value == TimesheetSelectDay.datePeriod.get(j)) {
                                if (j != Start) {
                                    if (j == secondStart) {
                                        imgbtn_ts_wrkhrs_prev.setVisibility(View.INVISIBLE);
                                    } else {
                                        imgbtn_ts_wrkhrs_prev.setVisibility(View.VISIBLE);
                                    }
                                    tv_ts_wrkhrs_date.setText(TimesheetSelectDay.datePeriod.get(j - 1));
                                    //-----------added on 31st may for getting day name code starts...----------
                                    SimpleDateFormat dateFormat = new SimpleDateFormat("MM-dd-yyy");
                                    Date sourceDate = null;
                                    try {
                                        sourceDate = dateFormat.parse(tv_ts_wrkhrs_date.getText().toString());
                                        SimpleDateFormat sdf = new SimpleDateFormat("EEEE");
                                        tv_ts_wrkhrs_dayname.setText(sdf.format(sourceDate));
                                    }catch(Exception e){
                                        e.printStackTrace();
                                    }
                                    //-----------added on 31st may for getting day name code ends...----------
                                    userSingletonModel.setDayDate(tv_ts_wrkhrs_date.getText().toString()); //--newly added on 28th nov
                                    getEployeeTimesheetDetails(); //--newly added on 28th nov
                                    break;
                                } else if (j == Start) {
                                    imgbtn_ts_wrkhrs_prev.setVisibility(View.INVISIBLE);
                                    break;
                                }
                            }
                        }
                    } else {
                        //---------Alert dialog code starts(added on 1st dec)--------
                        final AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(this);
                        alertDialogBuilder.setMessage("Entered data will be lost.\nWould you still want to continue!");
                        alertDialogBuilder.setNegativeButton("Yes", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {
                                alertDialogBuilder.setCancelable(false);
                                for (int j = 0; j < TimesheetSelectDay.datePeriod.size(); j++) {
                                    String value = tv_ts_wrkhrs_date.getText().toString();
                                    if (value == TimesheetSelectDay.datePeriod.get(j)) {
                                        if (j != Start) {
                                            if (j == secondStart) {
                                                imgbtn_ts_wrkhrs_prev.setVisibility(View.GONE);
                                            } else {
                                                imgbtn_ts_wrkhrs_prev.setVisibility(View.VISIBLE);
                                            }
                                            tv_ts_wrkhrs_date.setText(TimesheetSelectDay.datePeriod.get(j - 1));
                                            //-----------added on 31st may for getting day name code starts...----------
                                            SimpleDateFormat dateFormat = new SimpleDateFormat("MM-dd-yyy");
                                            Date sourceDate = null;
                                            try {
                                                sourceDate = dateFormat.parse(tv_ts_wrkhrs_date.getText().toString());
                                                SimpleDateFormat sdf = new SimpleDateFormat("EEEE");
                                                tv_ts_wrkhrs_dayname.setText(sdf.format(sourceDate));
                                            }catch(Exception e){
                                                e.printStackTrace();
                                            }
                                            //-----------added on 31st may for getting day name code ends...----------
                                            userSingletonModel.setDayDate(tv_ts_wrkhrs_date.getText().toString()); //--newly added on 28th nov
                                            getEployeeTimesheetDetails(); //--newly added on 28th nov
                                            break;
                                        } else if (j == Start) {
                                            imgbtn_ts_wrkhrs_prev.setVisibility(View.GONE);
                                            break;
                                        }
                                    }
                                }

                            }
                        });
                        alertDialogBuilder.setPositiveButton("No",
                                new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface arg0, int arg1) {
                                        alertDialogBuilder.setCancelable(true);
                                    }
                                });

                        final AlertDialog alertDialog = alertDialogBuilder.create();
                        alertDialog.show();

                        //--------Alert dialog code ends--------
                    }
                }else if(HomeActivity.supervisor_yn_temp.contentEquals("1") || HomeActivity.payrollclerk_yn_temp.contentEquals("1") || HomeActivity.payableclerk_yn_temp.contentEquals("1")){
                    for (int j = 0; j < TimesheetSelectDay.datePeriod.size(); j++) {
                        String value = tv_ts_wrkhrs_date.getText().toString();
                        if (value == TimesheetSelectDay.datePeriod.get(j)) {
                            if (j != Start) {
                                if (j == secondStart) {
                                    imgbtn_ts_wrkhrs_prev.setVisibility(View.INVISIBLE);
                                } else {
                                    imgbtn_ts_wrkhrs_prev.setVisibility(View.VISIBLE);
                                }
                                tv_ts_wrkhrs_date.setText(TimesheetSelectDay.datePeriod.get(j - 1));
                                //-----------added on 31st may for getting day name code starts...----------
                                SimpleDateFormat dateFormat = new SimpleDateFormat("MM-dd-yyy");
                                Date sourceDate = null;
                                try {
                                    sourceDate = dateFormat.parse(tv_ts_wrkhrs_date.getText().toString());
                                    SimpleDateFormat sdf = new SimpleDateFormat("EEEE");
                                    tv_ts_wrkhrs_dayname.setText(sdf.format(sourceDate));
                                }catch(Exception e){
                                    e.printStackTrace();
                                }
                                //-----------added on 31st may for getting day name code ends...----------
                                userSingletonModel.setDayDate(tv_ts_wrkhrs_date.getText().toString()); //--newly added on 28th nov
                                getEployeeTimesheetDetails(); //--newly added on 28th nov
                                break;
                            } else if (j == Start) {
                                imgbtn_ts_wrkhrs_prev.setVisibility(View.INVISIBLE);
                                break;
                            }
                        }
                    }
                }
                break;
            case R.id.imgbtn_ts_wrkhrs_next:
                imgbtn_ts_wrkhrs_prev.setVisibility(View.VISIBLE);
                final int End = TimesheetSelectDay.datePeriod.size()-1;
                final int secondEnd = TimesheetSelectDay.datePeriod.size()-2;
                if(HomeActivity.supervisor_yn_temp.contentEquals("0") && HomeActivity.payrollclerk_yn_temp.contentEquals("0") && HomeActivity.payableclerk_yn_temp.contentEquals("0")) {
                    if (userSingletonModel.getStatusDescription().contentEquals("APPROVED") || userSingletonModel.getStatusDescription().contentEquals("SUBMITTED") || userSingletonModel.getStatusDescription().contentEquals("POSTED") || userSingletonModel.getStatusDescription().contentEquals("PARTIAL_APPROVE")) {
                        for (int j = 0; j < TimesheetSelectDay.datePeriod.size(); j++) {
                            String value = tv_ts_wrkhrs_date.getText().toString();
                            if (value == TimesheetSelectDay.datePeriod.get(j)) {
                                if (j != End) {
                                    if (j == secondEnd) {
                                        imgbtn_ts_wrkhrs_next.setVisibility(View.INVISIBLE);
                                    } else {
                                        imgbtn_ts_wrkhrs_next.setVisibility(View.VISIBLE);
                                    }
                                    tv_ts_wrkhrs_date.setText(TimesheetSelectDay.datePeriod.get(j + 1));
                                    //-----------added on 31st may for getting day name code starts...----------
                                    SimpleDateFormat dateFormat = new SimpleDateFormat("MM-dd-yyy");
                                    Date sourceDate = null;
                                    try {
                                        sourceDate = dateFormat.parse(tv_ts_wrkhrs_date.getText().toString());
                                        SimpleDateFormat sdf = new SimpleDateFormat("EEEE");
                                        tv_ts_wrkhrs_dayname.setText(sdf.format(sourceDate));
                                    }catch(Exception e){
                                        e.printStackTrace();
                                    }
                                    //-----------added on 31st may for getting day name code ends...----------
                                    userSingletonModel.setDayDate(tv_ts_wrkhrs_date.getText().toString()); //--newly added on 28th nov
                                    getEployeeTimesheetDetails(); //--newly added on 28th nov
                                    break;
                                } else if (j == End) {
                                    imgbtn_ts_wrkhrs_next.setVisibility(View.INVISIBLE);
                                    break;
                                }
                            }
//                            Toast.makeText(getApplicationContext(),TimesheetSelectDay.datePeriod.get(j),Toast.LENGTH_SHORT).show();
                        }
                    } else {

                        //---------Alert dialog code starts(added on 1st dec)--------
                        final AlertDialog.Builder alertDialogBuilder3 = new AlertDialog.Builder(this);
                        alertDialogBuilder3.setMessage("Entered data will be lost.\nWould you still want to continue!");
                        alertDialogBuilder3.setNegativeButton("Yes", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {
                                alertDialogBuilder3.setCancelable(true);
                                for (int j = 0; j < TimesheetSelectDay.datePeriod.size(); j++) {
                                    String value = tv_ts_wrkhrs_date.getText().toString();
                                    if (value == TimesheetSelectDay.datePeriod.get(j)) {
                                        if (j != End) {
                                            if (j == secondEnd) {
                                                imgbtn_ts_wrkhrs_next.setVisibility(View.INVISIBLE);
                                            } else {
                                                imgbtn_ts_wrkhrs_next.setVisibility(View.VISIBLE);
                                            }
                                            tv_ts_wrkhrs_date.setText(TimesheetSelectDay.datePeriod.get(j + 1));
                                            //-----------added on 31st may for getting day name code starts...----------
                                            SimpleDateFormat dateFormat = new SimpleDateFormat("MM-dd-yyy");
                                            Date sourceDate = null;
                                            try {
                                                sourceDate = dateFormat.parse(tv_ts_wrkhrs_date.getText().toString());
                                                SimpleDateFormat sdf = new SimpleDateFormat("EEEE");
                                                tv_ts_wrkhrs_dayname.setText(sdf.format(sourceDate));
                                            }catch(Exception e){
                                                e.printStackTrace();
                                            }
                                            //-----------added on 31st may for getting day name code ends...----------
                                            userSingletonModel.setDayDate(tv_ts_wrkhrs_date.getText().toString()); //--newly added on 28th nov
                                            getEployeeTimesheetDetails(); //--newly added on 28th nov
                                            break;
                                        } else if (j == End) {
                                            imgbtn_ts_wrkhrs_next.setVisibility(View.INVISIBLE);
                                            break;
                                        }
                                    }
//                            Toast.makeText(getApplicationContext(),TimesheetSelectDay.datePeriod.get(j),Toast.LENGTH_SHORT).show();
                                }

                            }
                        });
                        alertDialogBuilder3.setPositiveButton("No",
                                new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface arg0, int arg1) {
                                        alertDialogBuilder3.setCancelable(true);
                                    }
                                });

                        final AlertDialog alertDialog3 = alertDialogBuilder3.create();
                        alertDialog3.show();

                        //--------Alert dialog code ends--------
                    }
                }else if(HomeActivity.supervisor_yn_temp.contentEquals("1") || HomeActivity.payrollclerk_yn_temp.contentEquals("1") || HomeActivity.payableclerk_yn_temp.contentEquals("1")){
                    for (int j = 0; j < TimesheetSelectDay.datePeriod.size(); j++) {
                        String value = tv_ts_wrkhrs_date.getText().toString();
                        if (value == TimesheetSelectDay.datePeriod.get(j)) {
                            if (j != End) {
                                if (j == secondEnd) {
                                    imgbtn_ts_wrkhrs_next.setVisibility(View.INVISIBLE);
                                } else {
                                    imgbtn_ts_wrkhrs_next.setVisibility(View.VISIBLE);
                                }
                                tv_ts_wrkhrs_date.setText(TimesheetSelectDay.datePeriod.get(j + 1));
                                //-----------added on 31st may for getting day name code starts...----------
                                SimpleDateFormat dateFormat = new SimpleDateFormat("MM-dd-yyy");
                                Date sourceDate = null;
                                try {
                                    sourceDate = dateFormat.parse(tv_ts_wrkhrs_date.getText().toString());
                                    SimpleDateFormat sdf = new SimpleDateFormat("EEEE");
                                    tv_ts_wrkhrs_dayname.setText(sdf.format(sourceDate));
                                }catch(Exception e){
                                    e.printStackTrace();
                                }
                                //-----------added on 31st may for getting day name code ends...----------
                                userSingletonModel.setDayDate(tv_ts_wrkhrs_date.getText().toString()); //--newly added on 28th nov
                                getEployeeTimesheetDetails(); //--newly added on 28th nov
                                break;
                            } else if (j == End) {
                                imgbtn_ts_wrkhrs_next.setVisibility(View.INVISIBLE);
                                break;
                            }
                        }
//                            Toast.makeText(getApplicationContext(),TimesheetSelectDay.datePeriod.get(j),Toast.LENGTH_SHORT).show();
                    }
                }
                break;
            case R.id.btn_sup_save:
                if(tv_ts_wrkhrs_totalhrs.getText().toString().contentEquals("0.0")){
                    //---------Alert dialog code starts(added on 1st dec)--------
                    final AlertDialog.Builder alertDialogBuilder1 = new AlertDialog.Builder(this);
                    alertDialogBuilder1.setMessage("0 hour cannot be saved");
                    alertDialogBuilder1.setPositiveButton("OK",
                            new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface arg0, int arg1) {
                                    alertDialogBuilder1.setCancelable(true);
                                }
                            });
                    final AlertDialog alertDialog1 = alertDialogBuilder1.create();
                    alertDialog1.show();

                    //--------Alert dialog code ends--------
                }else{
                    saveData();
                }
                break;
            case R.id.btn_back:
                //---------Redirect to previous Activity with condition check and Alert dialog code starts(added on 1st dec)--------
                if(HomeActivity.supervisor_yn_temp.contentEquals("0") && HomeActivity.payrollclerk_yn_temp.contentEquals("0") && HomeActivity.payableclerk_yn_temp.contentEquals("0")) {
                    if (userSingletonModel.getStatusDescription().contentEquals("APPROVED") || userSingletonModel.getStatusDescription().contentEquals("SUBMITTED") || userSingletonModel.getStatusDescription().contentEquals("POSTED") || userSingletonModel.getStatusDescription().contentEquals("PARTIAL_APPROVE")) {
//                    super.onBackPressed(); //----added on 6th dec
                        //-----following code is commented on 6th dec to get the calender saved state data------
                        Intent intent = new Intent(TimesheetWorkUpdateHrs.this, TimesheetSelectDay.class);
//                    intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                        startActivity(intent);
                        TimesheetWorkUpdateHrs.this.finish();
                        //-----above code is commented on 6th dec to get the calender saved state data------
                    } else {
                        final AlertDialog.Builder alertDialogBuilder2 = new AlertDialog.Builder(this);
                        alertDialogBuilder2.setMessage("Entered data will be lost.\nWould you still want to continue!");
                        alertDialogBuilder2.setNegativeButton("Yes", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {
//                            TimesheetWorkUpdateHrs.super.onBackPressed();  //---added on 6th dec
                                //-----following code is commented on 6th dec to get the calender saved state data------
                                Intent intent = new Intent(TimesheetWorkUpdateHrs.this, TimesheetSelectDay.class);
//                            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                                startActivity(intent);
                                TimesheetWorkUpdateHrs.this.finish();
                                //-----above code is commented on 6th dec to get the calender saved state data------
                            }
                        });
                        alertDialogBuilder2.setPositiveButton("No",
                                new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface arg0, int arg1) {
                                        alertDialogBuilder2.setCancelable(true);
                                    }
                                });

                        final AlertDialog alertDialog2 = alertDialogBuilder2.create();
                        alertDialog2.show();
                    }
                }else if(HomeActivity.supervisor_yn_temp.contentEquals("1") || HomeActivity.payrollclerk_yn_temp.contentEquals("1") || HomeActivity.payableclerk_yn_temp.contentEquals("1")){
                    //-----following code is commented on 6th dec to get the calender saved state data------
                    Intent intent = new Intent(TimesheetWorkUpdateHrs.this, TimesheetSelectDay.class);
//                    intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                    startActivity(intent);
                    TimesheetWorkUpdateHrs.this.finish();
                    //-----above code is commented on 6th dec to get the calender saved state data------
                }
                //--------Redirect to previous Activity with condition check and Alert dialog code ends--------
                break;
        }
    }

    //===========switch case for button onClick() code starts=============


    @Override
    public void onBackPressed() {
//        super.onBackPressed();
        //---------Redirect to previous Activity with condition check and Alert dialog code starts(added on 1st dec)--------
        if(HomeActivity.supervisor_yn_temp.contentEquals("0") && HomeActivity.payrollclerk_yn_temp.contentEquals("0") && HomeActivity.payableclerk_yn_temp.contentEquals("0")) {
            if (userSingletonModel.getStatusDescription().contentEquals("APPROVED") || userSingletonModel.getStatusDescription().contentEquals("SUBMITTED") || userSingletonModel.getStatusDescription().contentEquals("POSTED") || userSingletonModel.getStatusDescription().contentEquals("PARTIAL_APPROVE")) {
                //-----following code is commented on 6th dec to get the calender saved state data------
                Intent intent = new Intent(TimesheetWorkUpdateHrs.this, TimesheetSelectDay.class);
//            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                startActivity(intent);
                TimesheetWorkUpdateHrs.this.finish();
                //-----above code is commented on  6th dec to get the calender saved state data-------

//            super.onBackPressed(); //---line added on 6th dec
            } else {
                final AlertDialog.Builder alertDialogBuilder2 = new AlertDialog.Builder(this);
                alertDialogBuilder2.setMessage("Entered data will be lost.\nWould you still want to continue!");
                alertDialogBuilder2.setNegativeButton("Yes", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {

                        //-----following code is commented on 6th dec to get the calender saved state data------
                        Intent intent = new Intent(TimesheetWorkUpdateHrs.this, TimesheetSelectDay.class);
//                    intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                        startActivity(intent);
                        TimesheetWorkUpdateHrs.this.finish();
                        //-----above code is commented on  6th dec to get the calender saved state data-------
//                    TimesheetWorkUpdateHrs.super.onBackPressed();  //---line added on 6th dec

                    }
                });
                alertDialogBuilder2.setPositiveButton("No",
                        new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface arg0, int arg1) {
                                alertDialogBuilder2.setCancelable(true);
                            }
                        });

                final AlertDialog alertDialog2 = alertDialogBuilder2.create();
                alertDialog2.show();
            }
        }else if(HomeActivity.supervisor_yn_temp.contentEquals("1") || HomeActivity.payrollclerk_yn_temp.contentEquals("1") || HomeActivity.payableclerk_yn_temp.contentEquals("1")){
            //-----following code is commented on 6th dec to get the calender saved state data------
            Intent intent = new Intent(TimesheetWorkUpdateHrs.this, TimesheetSelectDay.class);
//            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
            startActivity(intent);
            TimesheetWorkUpdateHrs.this.finish();
            //-----above code is commented on  6th dec to get the calender saved state data-------

//            super.onBackPressed(); //---line added on 6th dec
        }
        //--------Redirect to previous Activity with condition check and Alert dialog code ends--------
    }

    //================function to save the data onClicking the button code starts============
    public void saveData(){
        String  note = "" ;
        String ContractID = null, TaskID = null, LaborcatID = null, CostTypeID = null;

        //---following piece of code is for demo testing(maybe this code will be final as per 26th nov review)----
        String hoursString = "";
        //---above piece of code is for demo testing only---

        String ZeroHourYN = "";
        final JSONObject DocumentElementobj = new JSONObject();
        JSONArray req = new JSONArray();
        JSONObject reqObjdt = new JSONObject();
        try {
            for (int i = 0; i < employeeTimesheetListModelArrayList.size(); i++) {
                JSONObject reqObj = new JSONObject();
                if(employeeTimesheetListModelArrayList.get(i).getEditTextValueDouble()!=null) {
                    try {
                        ContractID = employeeTimesheetListModelArrayList.get(i).getContractID();
                        TaskID = employeeTimesheetListModelArrayList.get(i).getTaskID();
                        LaborcatID = employeeTimesheetListModelArrayList.get(i).getLaborCategoryID();
                        CostTypeID = employeeTimesheetListModelArrayList.get(i).getCostTypeID();
                        note = employeeTimesheetListModelArrayList.get(i).getEditTextAddNote();

                        //-------newly added on 26th dec code starts-------
                        if(employeeTimesheetListModelArrayList.get(i).getEditTextValue().contentEquals("")){
                            hoursString = "0";
                        }else if (!employeeTimesheetListModelArrayList.get(i).getEditTextValue().contentEquals("") && employeeTimesheetListModelArrayList.get(i).getEditTextValue().contentEquals("0")){
                            hoursString = "0";
                        }else if (!employeeTimesheetListModelArrayList.get(i).getEditTextValue().contentEquals("") && !employeeTimesheetListModelArrayList.get(i).getEditTextValue().contentEquals("0")){
                            hoursString = employeeTimesheetListModelArrayList.get(i).getEditTextValue();
                        }

                        //-----newly added on 3rd dec code to set the value of Zerohour starts-----
                        if(Double.parseDouble(hoursString) > 0){
                            ZeroHourYN = "No";
                        }else if(Double.parseDouble(hoursString) == 0){
                            ZeroHourYN = "Yes";
                        }
                        //-----newly added on 3rd dec code to set the value of Zerohour ends-----
                        //-------newly added on 26th dec code ends-------
                    } catch (NullPointerException e) {
                        e.printStackTrace();
                    }
                }else{
                    try {
                        if (!employeeTimesheetListModelArrayList.get(i).getEditTextAddNote().contentEquals("")) {
                            note = employeeTimesheetListModelArrayList.get(i).getEditTextAddNote();
                        }else if(employeeTimesheetListModelArrayList.get(i).getEditTextAddNote().contentEquals("")){
                            note = "";
                        }

                        //-------newly added on 26th dec code starts-------
                        if(employeeTimesheetListModelArrayList.get(i).getEditTextValue().contentEquals("")){
                            hoursString = "0";
                        }else if (!employeeTimesheetListModelArrayList.get(i).getEditTextValue().contentEquals("") && employeeTimesheetListModelArrayList.get(i).getEditTextValue().contentEquals("0")){
                            hoursString = "0";
                        }else if (!employeeTimesheetListModelArrayList.get(i).getEditTextValue().contentEquals("") && !employeeTimesheetListModelArrayList.get(i).getEditTextValue().contentEquals("0")){
                            hoursString = employeeTimesheetListModelArrayList.get(i).getEditTextValue();
                        }
                        //-----newly added on 3rd dec code to set the value of Zerohour starts-----
                        if(Double.parseDouble(hoursString) > 0){
                            ZeroHourYN = "No";
                        }else if(Double.parseDouble(hoursString) == 0){
                            ZeroHourYN = "Yes";
                        }
                        //-----newly added on 3rd dec code to set the value of Zerohour ends-----
                        //-------newly added on 26th dec code ends-------

                        ContractID = employeeTimesheetListModelArrayList.get(i).getContractID();
                        TaskID = employeeTimesheetListModelArrayList.get(i).getTaskID();
                        LaborcatID = employeeTimesheetListModelArrayList.get(i).getLaborCategoryID();
                        CostTypeID = employeeTimesheetListModelArrayList.get(i).getCostTypeID();
                    } catch (NullPointerException e) {
                        e.printStackTrace();
                    }
                }

                reqObj.put("ContractID", ContractID);
                reqObj.put("TaskID", TaskID);
                reqObj.put("LaborcatID", LaborcatID);
                reqObj.put("CostTypeID",CostTypeID);
                reqObj.put("Hour", hoursString);
                reqObj.put("ZeroHourYN", ZeroHourYN);
                reqObj.put("Note",note);
                reqObj.put("NoteType","emp");
                req.put(reqObj);
            }
            DocumentElementobj.put("CorpID",userSingletonModel.getCorpID());
            DocumentElementobj.put("UserID",Double.parseDouble(userSingletonModel.getUserID()));
//            DocumentElementobj.put("WeekDate",TimesheetHome.dateOnSelectedCalender);
            DocumentElementobj.put("WeekDate",userSingletonModel.getTimesheetSelectDate_WeekDate());
            DocumentElementobj.put("WeekStartDate", userSingletonModel.getPeriodStartDate());
            DocumentElementobj.put("TimeSheetDate",userSingletonModel.getDayDate());
            DocumentElementobj.put( "Detail", req );
            reqObjdt.put("dt", DocumentElementobj);
            Log.d("jsonTest",DocumentElementobj.toString());  //---must follow this line---(don't see toast, as it may confuse for this part)----
//            Toast.makeText(getApplicationContext(),reqObjdt.toString(),Toast.LENGTH_LONG).show(); //---commenting toast, as it should be deleted before app live
        }catch (Exception e){
            e.printStackTrace();
        }


        //==========follwing is the code for volley i.e to send the data to the server... code starts=======
        String url = Config.BaseUrl+"EmployeeTimeSheetSave";

        final ProgressDialog loading = ProgressDialog.show(this, "Loading", "Please wait...", true, false);
        StringRequest stringRequest = new StringRequest(Request.Method.POST, url, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                JSONObject jsonObj = null;
                try{
                    jsonObj = XML.toJSONObject(response);
                    String responseData = jsonObj.toString();
                    String val = "";
                    JSONObject resobj = new JSONObject(responseData);
                    Iterator<?> keys = resobj.keys();
                    loading.dismiss();
                    while(keys.hasNext() ) {
                        String key = (String) keys.next();
                        if (resobj.get(key) instanceof JSONObject) {
                            JSONObject xx = new JSONObject(resobj.get(key).toString());
                            val = xx.getString("content");
                            JSONObject jsonObject = new JSONObject(val);
                            Log.d("saveList: ",val);
                            String status = jsonObject.getString("status");
                            if (status.equalsIgnoreCase("1")) {
                                //---------Alert dialog code starts(added on 21st nov)--------
                                AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(TimesheetWorkUpdateHrs.this);
                                alertDialogBuilder.setMessage(jsonObject.getString("message"));
                                alertDialogBuilder.setPositiveButton("OK",
                                        new DialogInterface.OnClickListener() {
                                            @Override
                                            public void onClick(DialogInterface arg0, int arg1) {
//                                                TimesheetWorkUpdateHrs.super.onBackPressed(); //---added on 6th dec
//                                                moveTaskToBack(true);
                                                //-----following code is commented on 6th dec to get the calender saved state data------
                                                Intent intent = new Intent(TimesheetWorkUpdateHrs.this,TimesheetSelectDay.class);
//                                                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                                                startActivity(intent);
                                                TimesheetWorkUpdateHrs.this.finish();
                                                //-----above code is commented on 6th dec to get the calender saved state data------
                                            }
                                        });
                                AlertDialog alertDialog = alertDialogBuilder.create();
                                alertDialog.show();

                                //--------Alert dialog code ends--------
                            }
                            else if (status.equalsIgnoreCase("2")){
                                Toast.makeText(getApplicationContext(),"message: "+jsonObject.getString("message"),Toast.LENGTH_LONG).show();
                            }
                        }
                    }
                }catch (JSONException e){
                    loading.dismiss();
                    e.printStackTrace();
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                error.printStackTrace();
                Toast.makeText(getApplicationContext(),"Error! Please try again",Toast.LENGTH_LONG).show();
                loading.dismiss();
            }
        }) {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> params = new HashMap<>();
                params.put("data", DocumentElementobj.toString());
                return params;
            }
        };

        RequestQueue requestQueue = Volley.newRequestQueue(TimesheetWorkUpdateHrs.this);
        requestQueue.add(stringRequest);
        //==========follwing is the code for volley i.e to send the data to the server... code ends=======
    }
    //================function to save the data onClicking the button code ends==============

    //===============to get the contractList data from server using volley code starts=============
    public void getContractListData(){
        String url ="http://220.225.40.151:9012/TimesheetService.asmx/ContractList";
        final ProgressDialog loading = ProgressDialog.show(this, "Loading", "Please wait...", true, false);
        StringRequest stringRequest = new StringRequest(Request.Method.POST, url, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                JSONObject jsonObj = null;
                try{
                    jsonObj = XML.toJSONObject(response);
                    String responseData = jsonObj.toString();
                    String val = "";
                    JSONObject resobj = new JSONObject(responseData);
                    Iterator<?> keys = resobj.keys();
                    while(keys.hasNext() ) {
                        String key = (String) keys.next();
                        if (resobj.get(key) instanceof JSONObject) {
                            JSONObject xx = new JSONObject(resobj.get(key).toString());
                            val = xx.getString("content");
                            JSONObject jsonObject = new JSONObject(val);
                            String status = jsonObject.getString("status");
                            if(status.equalsIgnoreCase("true")){
                                updateHoursModelList.clear();
                                JSONArray jsonArray = jsonObject.getJSONArray("Contracts");
                                for (int i = 0; i < jsonArray.length(); i++) {
                                    JSONObject jobject = jsonArray.getJSONObject(i);
                                    UserUpdateHoursModel userUpdateHoursModel = new UserUpdateHoursModel();
                                    userUpdateHoursModel.setContractID(jobject.getString("ContractID"));
                                    userUpdateHoursModel.setContractCode(jobject.getString("ContractCode"));
                                    userUpdateHoursModel.setContractDescription(jobject.getString("ContractDescription"));
                                    userUpdateHoursModel.setContractFlag(jobject.getString("ContractFlag"));
                                    contractArrayList.add(userUpdateHoursModel);
                                    updateHoursModelList.add(userUpdateHoursModel);
//                                    stringContractArrayList.add(jobject.getString("ContractDescription"));
                                }
                                mRecyclerView.setAdapter(new CustomEmployeeTimesheetdemoAdapter(TimesheetWorkUpdateHrs.this,contractArrayList));

//                                listView.setAdapter(new customListViewTimesheetWorkUpdateHrs());
                                //------following code to get edittext values
                               /* View v;
                                ArrayList<String> mannschaftsnamen = new ArrayList<String>();
                                EditText et;
                                for (int i = 0; i < listView.getCount(); i++) {
                                    v = listView.getAdapter().getView(i, null, null);
                                    et = (EditText) v.findViewById(i);
                                    mannschaftsnamen.add(et.getText().toString());
                                    Toast.makeText(getApplicationContext(),"Hey".toString(),Toast.LENGTH_LONG).show();
                                }*/
                                //------code to get edittext values ends
                                /*listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                                    @Override
                                    public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
//                                        final EditText ed_addhrs = (EditText)view.findViewById(R.id.edtxt_rcl_ts_wkhsupdate_addhrs);

                                    }
                                });*/
                                Log.d("jsonData",jsonArray.toString());
                                loading.dismiss();

                            }else{
                                loading.dismiss();
                                Toast.makeText(getApplicationContext(),jsonObject.getString("message"),Toast.LENGTH_LONG).show();
                            }
                        }
                    }
                }catch (JSONException e){
                    loading.dismiss();
                    e.printStackTrace();
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {

            }
        }) {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> params = new HashMap<>();
                params.put("UserId", userSingletonModel.getUserID());
                params.put("DayDate","10-07-2018");
//                params.put("DayDate",userSingletonModel.getDayDate());
                params.put("CorpId", userSingletonModel.getCorpID());
                params.put("deviceType","1");

            /*    params.put("UserId", "1");
//                params.put("DayDate","10-07-2018");
                params.put("DayDate","10-03-2018");
                params.put("CorpId", "gst-inc-101");
                params.put("deviceType","1");*/
                return params;
            }
        };

        RequestQueue requestQueue = Volley.newRequestQueue(TimesheetWorkUpdateHrs.this);
        requestQueue.add(stringRequest);


    }
    //===============to get the contractList data from server using volley code ends=============

    //=============to get the Employee timesheet details from server using volley code starts===============
    public void getEployeeTimesheetDetails(){
//        String url ="http://220.225.40.151:9012/TimesheetService.asmx/EmployeeTimeSheetList";
        String url = Config.BaseUrl+"EmployeeTimeSheetList";

        //-----newly added on 28th nov starts------
        if(employeeTimesheetListModelArrayList!=null) {
            employeeTimesheetListModelArrayList.clear();
        }
        //-----newly added on 28th nov ends------

        final ProgressDialog loading = ProgressDialog.show(this, "Loading", "Loading data of "+userSingletonModel.getDayDate(), true, false);
        StringRequest stringRequest = new StringRequest(Request.Method.POST, url, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                JSONObject jsonObj = null;
                try{
                    jsonObj = XML.toJSONObject(response);
                    String responseData = jsonObj.toString();
                    String val = "";
                    JSONObject resobj = new JSONObject(responseData);
                    Iterator<?> keys = resobj.keys();
                    while(keys.hasNext() ) {
                        String key = (String) keys.next();
                        if (resobj.get(key) instanceof JSONObject) {
                            JSONObject xx = new JSONObject(resobj.get(key).toString());
                            val = xx.getString("content");
                            JSONObject jsonObject = new JSONObject(val);
                            String status = jsonObject.getString("status");
                            if(status.equalsIgnoreCase("true")){
                                updateHoursModelList.clear();
                                JSONArray jsonArray = jsonObject.getJSONArray("EmployeeTimeSheetDetails");
                                for (int i = 0; i < jsonArray.length(); i++) {
                                    JSONObject jobject = jsonArray.getJSONObject(i);
                                    UserUpdateHoursModel userUpdateHoursModel = new UserUpdateHoursModel();
                                    EmployeeTimesheetListModel employeeTimesheetListModel = new EmployeeTimesheetListModel();
                                    employeeTimesheetListModel.setAccountCode(jobject.getString("AccountCode"));
                                    employeeTimesheetListModel.setContract(jobject.getString("Contract"));
                                    employeeTimesheetListModel.setContractID(jobject.getString("ContractID"));
                                    employeeTimesheetListModel.setTask(jobject.getString("Task"));
                                    employeeTimesheetListModel.setTaskID(jobject.getString("TaskID"));
                                    employeeTimesheetListModel.setLaborCategory(jobject.getString("LaborCategory"));
                                    employeeTimesheetListModel.setLaborCategoryID(jobject.getString("LaborCategoryID"));
                                    employeeTimesheetListModel.setCostType(jobject.getString("CostType"));
                                    employeeTimesheetListModel.setCostTypeID(jobject.getString("CostTypeID"));
                                    employeeTimesheetListModel.setACSuffix(jobject.getString("ACSuffix"));
                                    employeeTimesheetListModel.setHour(jobject.getString("Hour"));
                                    employeeTimesheetListModel.setNote(jobject.getString("Note"));
                                    //---added on 4th dec----
                                    if(Double.parseDouble(jobject.getString("Hour")) == 0){
                                        employeeTimesheetListModel.setEditTextValue(null);
                                    }else {
                                        employeeTimesheetListModel.setEditTextValue(jobject.getString("Hour")); //---newly added for hour calculation on 26th nov
                                    }
                                    //----above code modified on 4th dec-----

                                    employeeTimesheetListModelArrayList.add(employeeTimesheetListModel);
                                    contractArrayList.add(userUpdateHoursModel);
                                    updateHoursModelList.add(userUpdateHoursModel);
//                                    stringContractArrayList.add(jobject.getString("ContractDescription"));
                                }
                                mRecyclerView.setAdapter(new CustomEmployeeTimesheetListAdapter(TimesheetWorkUpdateHrs.this,employeeTimesheetListModelArrayList));
                                Log.d("jsonData",jsonArray.toString());
                                Double sum = 0.0;
                                Double value = 0.0;
                                for(int i=0;i<employeeTimesheetListModelArrayList.size();i++){
                                    Log.d("EmployeeDetails",employeeTimesheetListModelArrayList.get(i).getContract());
                                    value = Double.parseDouble(employeeTimesheetListModelArrayList.get(i).getHour());
                                    sum = sum+value;
                                }
                                Log.d("sum->",sum.toString());
                                sum_initial_value = sum;
                                if(sum_initial_value>24){
                                    String message = "Daily limit of 24 hours has been exceeded for this day!";
                                    int color = Color.parseColor("#FFFF00");
                                    Snackbar snackbar = Snackbar.make(findViewById(R.id.cordinatorLayout), message, Snackbar.LENGTH_LONG);

                                    View sbView = snackbar.getView();
                                    TextView textView = (TextView) sbView.findViewById(android.support.design.R.id.snackbar_text);
                                    textView.setTextColor(color);
                                    snackbar.show();
                                }
                                loading.dismiss();

                            }else{
                                loading.dismiss();
                                Toast.makeText(getApplicationContext(),jsonObject.getString("message"),Toast.LENGTH_LONG).show();
                            }
                        }
                    }
                }catch (JSONException e){
                    loading.dismiss();
                    e.printStackTrace();
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {

            }
        }) {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> params = new HashMap<>();

           /*     params.put("CorpId", "gst-inc-101");
                params.put("UserID","1");
                params.put("WeekDate", "10-31-2018");
                params.put("Selecteddate","10-29-2018");
                params.put("deviceType","1");*/

                params.put("CorpId", userSingletonModel.getCorpID());
                params.put("UserID",userSingletonModel.getUserID());
//                params.put("WeekDate", TimesheetHome.dateOnSelectedCalender);
                params.put("WeekDate", userSingletonModel.getTimesheetSelectDate_WeekDate());
                params.put("Selecteddate",userSingletonModel.getDayDate());
                params.put("deviceType","1");
                return params;
            }
        };

        RequestQueue requestQueue = Volley.newRequestQueue(TimesheetWorkUpdateHrs.this);
        requestQueue.add(stringRequest);
    }
    //=============to get the Employee timesheet details from server using volley code ends===========


    //=============Internet checking code starts(added 22nd Nov)=============
    // Method to manually check connection status
    private void checkConnection() {
        boolean isConnected = ConnectivityReceiver.isConnected();
        showSnack(isConnected);
    }

    // Showing the status in Snackbar
    private void showSnack(boolean isConnected) {
        String message;
        int color;
        if (isConnected) {
//            message = "Connected to Internet";
//            color = Color.WHITE;
        } else {
            message = "Sorry! Not connected to internet";
            color = Color.parseColor("#FF4242");
            Snackbar snackbar = Snackbar
                    .make(findViewById(R.id.cordinatorLayout), message, Snackbar.LENGTH_LONG);

            View sbView = snackbar.getView();
            TextView textView = (TextView) sbView.findViewById(android.support.design.R.id.snackbar_text);
            textView.setTextColor(color);
            snackbar.show();
        }
    }

    @Override
    protected void onResume() {
        super.onResume();

        // register connection status listener
        MyApplication.getInstance().setConnectivityListener(this);
    }

    /**
     * Callback will be triggered when there is change in
     * network connection
     */
    @Override
    public void onNetworkConnectionChanged(boolean isConnected) {
        showSnack(isConnected);
    }
    //=============Internet checking code ends(added 22nd Nov)=============


}
