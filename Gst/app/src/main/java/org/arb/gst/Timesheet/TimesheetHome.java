package org.arb.gst.Timesheet;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.NavigationView;
import android.support.design.widget.Snackbar;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.roomorama.caldroid.CaldroidFragment;
import com.roomorama.caldroid.CaldroidListener;

import org.arb.gst.Home.HomeActivity;
import org.arb.gst.Login.LoginActivity;
import org.arb.gst.R;

import org.arb.gst.Model.UserSingletonModel;
import org.arb.gst.Model.WeekDay;
import org.arb.gst.config.CameraUtils;
import org.arb.gst.config.Config;
import org.arb.gst.config.ConnectivityReceiver;
import org.arb.gst.config.MyApplication;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.json.XML;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import static org.arb.gst.Home.HomeActivity.BITMAP_SAMPLE_SIZE;

public class TimesheetHome extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener, ConnectivityReceiver.ConnectivityReceiverListener {
    UserSingletonModel userSingletonModel = UserSingletonModel.getInstance();
    ArrayList<WeekDay> weekDayArrayList = new ArrayList<>();
    //Calender
    private boolean undo = false;
    private CaldroidFragment caldroidFragment;
    private CaldroidFragment dialogCaldroidFragment;
    public static String dateOnSelectedCalender;
    Date date1, date2, holidayDay;
    int day;
    String inputString2, inputString1;
    String cl, data, dateString;
    String start = "01-01-2017";
    String end = "12-31-2019";
    private ProgressDialog progressBar;
    SimpleDateFormat myFormat = new SimpleDateFormat("MM-dd-yyyy");
    Date selectDate;
    ArrayList<Date> disableDates = new ArrayList<Date>();
    List<Date> selectedDates = new ArrayList<Date>();
    String earn, sick, comp,date;
    TextView txtSick, txtEarn, txtComp,txtUpto;
    public static Bundle savedInstanceState;
    ImageView imageView;
    public static String period_start_date, period_end_date, period_date;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_timesheethome);

        checkConnection();  //----function calling to check the internet connection

        //============Navigation drawer and toolbar code starts=============
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        View header = navigationView.getHeaderView(0);
        TextView textUserName = (TextView)header.findViewById(R.id.text_username);
        textUserName.setText(userSingletonModel.getEmpName());

        TextView textCompanyName = (TextView)header.findViewById(R.id.text_compname);
        textCompanyName.setText(userSingletonModel.getCompanyName());

        imageView = (ImageView)header.findViewById(R.id.imageView);
        if (!userSingletonModel.getImagePath().contentEquals("")){
            Bitmap bitmap = CameraUtils.optimizeBitmap(BITMAP_SAMPLE_SIZE, userSingletonModel.getImagePath());
            imageView.setImageBitmap(bitmap);
            Log.d("imagePath=>",userSingletonModel.getImagePath());
        }
        navigationView.setNavigationItemSelectedListener(this);
        //==============Navigation drawer and toolbar code ends=============

        // arraylist dates between two dates

        Date startDate = null;
        try {
            startDate = (Date) myFormat.parse(start);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        Date endDate = null;
        try {
            endDate = (Date) myFormat.parse(end);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        long interval = 24 * 1000 * 60 * 60; // 1 hour in millis
        long endTime = endDate.getTime(); // create your endtime here, possibly using Calendar or Date
        long curTime = startDate.getTime();
        while (curTime <= endTime) {
            disableDates.add(new Date(curTime));
            curTime += interval;
        }

        final SimpleDateFormat formatter = new SimpleDateFormat("MM-dd-yyyy");


        //==========Calender code starts===========

        // version, uncomment below line ****
        menuDate();  //-----calling menuDate() to allocate the menu to the calender dates
        caldroidFragment = new CaldroidSampleCustomFragment();
        // Setup arguments

        // If Activity is created after rotation
        this.savedInstanceState = savedInstanceState;
        if (savedInstanceState != null) {
            caldroidFragment.restoreStatesFromKey(savedInstanceState,
                    "CALDROID_SAVED_STATE");
        }
        // If activity is created from fresh
        else {
            Bundle args = new Bundle();
            Calendar cal = Calendar.getInstance();
            args.putInt(CaldroidFragment.MONTH, cal.get(Calendar.MONTH) + 1);
            args.putInt(CaldroidFragment.YEAR, cal.get(Calendar.YEAR));
            args.putBoolean(CaldroidFragment.ENABLE_SWIPE, true);
            args.putBoolean(CaldroidFragment.SIX_WEEKS_IN_CALENDAR, true);
//            args.putBoolean(CaldroidFragment.ENABLE_CLICK_ON_DISABLED_DATES, false);
//            args.putInt(CaldroidFragment.THEME_RESOURCE, com.caldroid.R.style.AppBaseTheme);
//            args.putBoolean(CaldroidFragment.SELECTED_DATES,true);

            // Uncomment this to customize startDayOfWeek
            // args.putInt(CaldroidFragment.START_DAY_OF_WEEK,
            // CaldroidFragment.TUESDAY); // Tuesday

            // Uncomment this line to use Caldroid in compact mode
            // args.putBoolean(CaldroidFragment.SQUARE_TEXT_VIEW_CELL, false);

            // Uncomment this line to use dark theme
//             args.putInt(CaldroidFragment.THEME_RESOURCE, com.caldroid.R.style.CaldroidDefaultDarkCell);

            caldroidFragment.setArguments(args);

        }

        //setCustomResourceForDates();
        // Service Call
        // setCustomResourceForDate();
        // Attach to the activity
        FragmentTransaction t = getSupportFragmentManager().beginTransaction();
        t.replace(R.id.calendar1, caldroidFragment);
        t.commit();


        // Setup listener
        final Date finalEndDate = endDate;
        final CaldroidListener listener = new CaldroidListener() {

            @Override
            public void onSelectDate(Date date, View view) {
             dateOnSelectedCalender = formatter.format(date);
             //===========Code to check, whether selected date is available in the arraylist or not...starts======
                for (int i = 0; i<selectedDates.size(); i++)
                {
                    if(date.toString().contains(selectedDates.get(i).toString()) && userSingletonModel.getEmployeeYN().contentEquals("1")){
                        Log.d("Hey dates",date.toString());
                        startActivity(new Intent(TimesheetHome.this,TimesheetSelectDay.class));
                    }else if(date.toString().contains(selectedDates.get(i).toString()) && userSingletonModel.getEmployeeYN().contentEquals("0")){
                        getPeriodDate(); //---to get period_end_date
                        /*if(!period_end_date.isEmpty()) {
                            startActivity(new Intent(TimesheetHome.this, Subordinate.class));
                        }else{
                            Toast.makeText(getApplicationContext(),"Please try again",Toast.LENGTH_SHORT).show();
                        }*/
                    }
                }
                //===========Code to check, whether selected date is available in the arraylist or not...ends======
            }

            @Override
            public void onChangeMonth(int month, int year) {
                String text = "month: " + month + " year: " + year;
                // Toast.makeText(getApplicationContext(), text, Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onLongClickDate(Date date, View view) {
                // Toast.makeText(getApplicationContext(), "Long click " + formatter.format(date), Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onCaldroidViewCreated() {
                if (caldroidFragment.getLeftArrowButton() != null) {
                    // Toast.makeText(getApplicationContext(), "Caldroid view is created", Toast.LENGTH_SHORT).show();

                }
            }

        };
        // Setup Caldroid
        caldroidFragment.setCaldroidListener(listener);
        //==========Calender code ends===========

    }



   //----commenting the following onPostResume() as this is creating duplicate activity for next page-----
  /*  @Override
    protected void onPostResume() {
        super.onPostResume();
        menuDate();
    }*/
    //----commenting the following onPostResume() as this is creating duplicate activity for next page-----

    //================to get the menu of dates using volley code starts====================
    public void menuDate(){
//        String url ="http://220.225.40.151:9012/TimesheetService.asmx/GenerateWeekDates";
       String url = Config.BaseUrl+"GenerateWeekDates";

        final ProgressDialog loading = ProgressDialog.show(this, "Loading", "Please wait...", true, false);
        StringRequest stringRequest = new StringRequest(Request.Method.POST, url,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        JSONObject jsonObj = null;
                        try{
                            jsonObj = XML.toJSONObject(response);
                            String responseData = jsonObj.toString();
                            String val = "";
                            JSONObject resobj = new JSONObject(responseData);
//                            String status=jsonObject.getString("status");

//                            Toast.makeText(getApplicationContext(),jsonObject.getString("status"),Toast.LENGTH_LONG).show();
                            Log.d("getData",responseData.toString());

                            Iterator<?> keys = resobj.keys();
                            while(keys.hasNext() ) {
                                String key = (String) keys.next();
                                if (resobj.get(key) instanceof JSONObject) {
                                    JSONObject xx = new JSONObject(resobj.get(key).toString());
                                    val = xx.getString("content");
//                                    Toast.makeText(getApplicationContext(),xx.getString("content"),Toast.LENGTH_LONG).show();
//                                    Log.d("getdata1", xx.getString("content"));
                                    JSONObject jsonObject = new JSONObject(val);
                                    String status = jsonObject.getString("status");
                                    if(status.equalsIgnoreCase("true")){
                                        JSONArray jsonArray = jsonObject.getJSONArray("DayStatus");
                                        for (int i = 0; i < jsonArray.length(); i++) {
                                            JSONObject jobject = jsonArray.getJSONObject(i);

                                            WeekDay weekDay = new WeekDay();
                                            weekDay.setColorCode(jobject.getString("ColorCode"));
                                            weekDay.setDescription(jobject.getString("Description"));
                                            weekDay.setStatus(jobject.getString("Status"));
                                            weekDay.setTotalHours(jobject.getString("TotalHours"));
                                            weekDay.setPeriod(jobject.getString("Period"));
                                            weekDayArrayList.add(weekDay);

                                            for (int j = 0; j < weekDayArrayList.size(); j++) {
                                                dateString = weekDayArrayList.get(j).getPeriod();
                                                cl = weekDayArrayList.get(j).getColorCode();

                                            }
                                            Date cDate = new Date();
                                            String fDate = new SimpleDateFormat("MM-dd-yyyy").format(cDate);
                                            Date today = (Date) myFormat.parse(fDate);

                                            selectDate = (Date) myFormat.parse(dateString);
                                            ColorDrawable color = new ColorDrawable(Color.parseColor(cl));
                                            if(userSingletonModel.getEmployeeYN().contentEquals("1")) {
                                                caldroidFragment.setBackgroundDrawableForDate(color, selectDate);
                                            }else{
                                                String c2 = "#c2c2c2";
                                                ColorDrawable color1 = new ColorDrawable(Color.parseColor(c2));
                                                caldroidFragment.setBackgroundDrawableForDate(color1, selectDate);
                                            }
//                                            selectedDates.add(today); //----Commented by Satabhisha on 24th Oct
                                            selectedDates.add(selectDate);
                                            disableDates.removeAll(selectedDates);
                                            caldroidFragment.setTextColorForDate(R.color.caldroid_black, selectDate);

                                            // caldroidFragment.setDisableDates(disableDates);
                                            //caldroidFragment.setSelectedDate(selectDate);
                                        }
                                        caldroidFragment.refreshView();
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
                        } catch (ParseException e) {
                            loading.dismiss();
                            e.printStackTrace();
                        }
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                loading.dismiss();
            }
        }) {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> params = new HashMap<>();
                params.put("CorpId", userSingletonModel.getCorpID());
                params.put("UserId", userSingletonModel.getUserID());
                params.put("UserType",userSingletonModel.getUserType());
                params.put("StartDate",start);
                params.put("EndDate",end);
                params.put("CompanyId",userSingletonModel.getCompID());
                return params;
            }
        };

        stringRequest.setRetryPolicy(new DefaultRetryPolicy(10000,
                DefaultRetryPolicy.DEFAULT_MAX_RETRIES, DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
        RequestQueue requestQueue = Volley.newRequestQueue(TimesheetHome.this);
        requestQueue.add(stringRequest);

    }
    //================to get the menu of dates using volley code ends===========


    //=================function to get period_dates(includes start_date, end_date) starts==========
    public void getPeriodDate(){
        String url = Config.BaseUrl+"TimeSheetPeriodDetail";
        StringRequest stringRequest = new StringRequest(Request.Method.POST, url,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        JSONObject jsonObj = null;
                        try{
                            jsonObj = XML.toJSONObject(response);
                            String responseData = jsonObj.toString();
                            String val = "";
                            JSONObject resobj = new JSONObject(responseData);
                            Log.d("getPeriodDate",responseData.toString());

                            Iterator<?> keys = resobj.keys();
                            while(keys.hasNext() ) {
                                String key = (String) keys.next();
                                if (resobj.get(key) instanceof JSONObject) {
                                    JSONObject xx = new JSONObject(resobj.get(key).toString());
                                    val = xx.getString("content");
//                                    Toast.makeText(getApplicationContext(),xx.getString("content"),Toast.LENGTH_LONG).show();
                                    Log.d("getPeriodDate1", xx.getString("content"));
                                    JSONObject jsonObject = new JSONObject(val);
                                    period_date = jsonObject.getString("period_date");
                                    period_start_date = jsonObject.getString("period_start_date");
                                    period_end_date = jsonObject.getString("period_end_date");
                                    if(HomeActivity.supervisor_yn_temp.contentEquals("1")) {
                                        startActivity(new Intent(TimesheetHome.this, Subordinate.class));
                                    }else if(HomeActivity.payrollclerk_yn_temp.contentEquals("1")){
                                        userSingletonModel.setPayroll_payable_type("PAYROLL");
                                        userSingletonModel.setPayroll_payable_notstarted("1");
                                        userSingletonModel.setPayroll_payable_saved("1");
                                        userSingletonModel.setPayroll_payable_submitted("1");
                                        userSingletonModel.setPayroll_payable_returned("1");
                                        userSingletonModel.setPayroll_payable_approve("1");
                                        userSingletonModel.setPayroll_payable_posted("1");
                                        userSingletonModel.setPayroll_payable_partialreturn("1");
                                        userSingletonModel.setPayroll_payable_partialapprove("1");
                                        userSingletonModel.setPayroll_payable_strActiveFlag("Active");
                                        userSingletonModel.setPayroll_payable_strTimesheetStatusList("&lt;status&gt;&lt;id&gt;0&lt;/id&gt;&lt;id&gt;1&lt;/id&gt;&lt;id&gt;2&lt;/id&gt;&lt;id&gt;3&lt;/id&gt;&lt;id&gt;4&lt;/id&gt;&lt;id&gt;5&lt;/id&gt;&lt;id&gt;6&lt;/id&gt;&lt;id&gt;7&lt;/id&gt;&lt;/status&gt;");
                                        startActivity(new Intent(TimesheetHome.this, PayrollPayableClerk.class));
                                    }else if(HomeActivity.payableclerk_yn_temp.contentEquals("1")){
                                        userSingletonModel.setPayroll_payable_type("PAYABLE");
                                        userSingletonModel.setPayroll_payable_notstarted("1");
                                        userSingletonModel.setPayroll_payable_saved("1");
                                        userSingletonModel.setPayroll_payable_submitted("1");
                                        userSingletonModel.setPayroll_payable_returned("1");
                                        userSingletonModel.setPayroll_payable_approve("1");
                                        userSingletonModel.setPayroll_payable_posted("1");
                                        userSingletonModel.setPayroll_payable_partialreturn("1");
                                        userSingletonModel.setPayroll_payable_partialapprove("1");
                                        userSingletonModel.setPayroll_payable_strActiveFlag("Active");
                                        userSingletonModel.setPayroll_payable_strTimesheetStatusList("&lt;status&gt;&lt;id&gt;0&lt;/id&gt;&lt;id&gt;1&lt;/id&gt;&lt;id&gt;2&lt;/id&gt;&lt;id&gt;3&lt;/id&gt;&lt;id&gt;4&lt;/id&gt;&lt;id&gt;5&lt;/id&gt;&lt;id&gt;6&lt;/id&gt;&lt;id&gt;7&lt;/id&gt;&lt;/status&gt;");
                                        startActivity(new Intent(TimesheetHome.this, PayrollPayableClerk.class));
                                    }

                                }
                            }
                        }catch (JSONException e){

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
                params.put("CorpId", userSingletonModel.getCorpID());
                params.put("UserId", userSingletonModel.getUserID());
                params.put("UserType",userSingletonModel.getUserType());
                params.put("PeriodDate",dateOnSelectedCalender);
                return params;
            }
        };

       /* stringRequest.setRetryPolicy(new DefaultRetryPolicy(10000,
                DefaultRetryPolicy.DEFAULT_MAX_RETRIES, DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));*/
        RequestQueue requestQueue = Volley.newRequestQueue(this);
        requestQueue.add(stringRequest);
    }
    //=================function to get period_dates(includes start_date, end_date) ends==========


    //=========Navigation drawer onBackPressed code starts=====
    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
//            super.onBackPressed();
            Intent intent = new Intent(TimesheetHome.this,HomeActivity.class);
            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
            startActivity(intent);
            TimesheetHome.this.finish();
            this.finish();
        }
    }
    //===========Navigation Drawer onBackPressed code ends=======

    //===============Navigation drawer on Selecting the items, code starts==============
    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
        int id = item.getItemId();

        if (id == R.id.nav_home) {
            // Handle the camera action
            startActivity(new Intent(TimesheetHome.this,HomeActivity.class));
        }else if (id == R.id.nav_upcoming_events){
            AlertDialog.Builder builder = new AlertDialog.Builder(this);
            builder.setMessage("Coming Soon")
                    .setCancelable(false)
                    .setPositiveButton("Ok", new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int id) {
                            dialog.cancel();
                        }
                    });
            AlertDialog alert = builder.create();
            alert.show();

        }else if (id == R.id.nav_announcements){
            AlertDialog.Builder builder = new AlertDialog.Builder(this);
            builder.setMessage("Coming Soon")
                    .setCancelable(false)
                    .setPositiveButton("Ok", new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int id) {
                            dialog.cancel();
                        }
                    });
            AlertDialog alert = builder.create();
            alert.show();

        }else if (id == R.id.nav_pending_item){
            AlertDialog.Builder builder = new AlertDialog.Builder(this);
            builder.setMessage("Coming Soon")
                    .setCancelable(false)
                    .setPositiveButton("Ok", new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int id) {
                            dialog.cancel();
                        }
                    });
            AlertDialog alert = builder.create();
            alert.show();

        }else if (id == R.id.nav_holiday_list){
            AlertDialog.Builder builder = new AlertDialog.Builder(this);
            builder.setMessage("Coming Soon")
                    .setCancelable(false)
                    .setPositiveButton("Ok", new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int id) {
                            dialog.cancel();
                        }
                    });
            AlertDialog alert = builder.create();
            alert.show();

        } else if (id == R.id.nav_calender) {

        }else if (id == R.id.nav_vacation_request){
            AlertDialog.Builder builder = new AlertDialog.Builder(this);
            builder.setMessage("Coming Soon")
                    .setCancelable(false)
                    .setPositiveButton("Ok", new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int id) {
                            dialog.cancel();
                        }
                    });
            AlertDialog alert = builder.create();
            alert.show();

        } else if (id == R.id.nav_logout) {
            AlertDialog.Builder builder = new AlertDialog.Builder(this);
            builder.setMessage("Are you sure you want to exit?")
                    .setCancelable(false)
                    .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int id) {
                            removeSharedPref();
                            Intent intent = new Intent(TimesheetHome.this,LoginActivity.class);
                            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                            startActivity(intent);
                            TimesheetHome.this.finish();
                        }
                    })
                    .setNegativeButton("No", new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int id) {
                            dialog.cancel();
                        }
                    });
            AlertDialog alert = builder.create();
            alert.show();

        }/* else if (id == R.id.nav_share) {

        } else if (id == R.id.nav_send) {

        }*/

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

    //===============Navigation drawer on Selecting the items, code ends==============

    //===============code to clear sharedPref data starts=========
    public void removeSharedPref(){
        SharedPreferences sharedPreferences = getApplication().getSharedPreferences("LoginDetails", Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.remove("UserID");
        editor.remove("UserName");
        editor.remove("CompID");
        editor.remove("CorpID");
        editor.remove("CompanyName");
        editor.remove("SupervisorId");
        editor.remove("UserRole");
        editor.remove("AdminYN");
        editor.remove("PayableClerkYN");
        editor.remove("SupervisorYN");
        editor.remove("PurchaseYN");
        editor.remove("PayrollClerkYN");
        editor.remove("EmpName");
        editor.remove("UserType");
        editor.remove("EmailId");
        editor.remove("PwdSetterId");
        editor.remove("FinYearID");
        editor.remove("Msg");
        editor.commit();
    }
    //===============code to clear sharedPref data ends=========

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


    @Override
    protected void onPostResume() {
        super.onPostResume();
        menuDate();
    }
}
