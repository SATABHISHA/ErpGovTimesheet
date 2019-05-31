package org.arb.gst.Timesheet;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.AppBarLayout;
import android.support.design.widget.Snackbar;
import android.support.v4.widget.NestedScrollView;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.BaseExpandableListAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ExpandableListView;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
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
import org.arb.gst.Model.TimesheetSelectDayModel;
import org.arb.gst.Model.UserSingletonModel;
import org.arb.gst.Model.WeekDays;
import org.arb.gst.R;
import org.arb.gst.adapter.ExpandableListAdapter;
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

public class TimesheetSelectDay extends AppCompatActivity implements View.OnClickListener, ConnectivityReceiver.ConnectivityReceiverListener{
    UserSingletonModel userSingletonModel = UserSingletonModel.getInstance();
    ArrayList<TimesheetSelectDayModel> arrayListTimesheetSelectDayModelsWeekDay = new ArrayList<>();
    List<TimesheetSelectDayModel> listTimesheetSelectDayModelsWeekDay = new ArrayList<>();
    HashMap<TimesheetSelectDayModel, ArrayList<WeekDays>> listDataChild = new HashMap<TimesheetSelectDayModel, ArrayList<WeekDays>>();
    List<WeekDays> weekDaysList = new ArrayList<>();
    ArrayList<WeekDays> weekDaysArrayList = new ArrayList<>();
    public static ArrayList<String> datePeriod = new ArrayList<>();
    RecyclerView mRecyclerView;
    public static String colorcode,selectedDate, description_status_temp;
    EditText edtxtEmployeeNote;
    TextView tv_empname, tv_period_date, tv_selected_date, tv_totalhrs, tv_period_totalhrs;
    LinearLayout tv_addOrView_employee_note, tv_addOrView_supervisor_note;
    NestedScrollView nestedScrollView;
    ImageView img_empnote_view, img_empnote_add, img_sup_add_view, img_sup_note_view;
    Button btn_calender, btn_submit, btn_return, btn_approve;
    Context context = this;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
//        setContentView(R.layout.activity_select_day); //==commented on 6th may
        setContentView(R.layout.activity_select_day_new);  //==added on 6th may
        final Toolbar mToolbar = (Toolbar) findViewById(R.id.toolbar);
        mToolbar.setTitle("Select Date");
        setSupportActionBar(mToolbar);

        checkConnection();  //----function calling to check the internet connection

        if (getSupportActionBar() != null) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        }
        mToolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                userSingletonModel.setTimesheetSelectDay_empNote("");
                userSingletonModel.setTimesheetSelectDay_supNote("");
                onBackPressed(); //---added on 6th dec


                //----following code is commented on 6th dec to get the calender save state data-------
               /* Intent intent = new Intent(TimesheetSelectDay.this,TimesheetHome.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                startActivity(intent);
                TimesheetSelectDay.this.finish();*/
                //----following code is commented on 6th dec to get the calender save state data-------
            }
        });
        AppBarLayout mAppBarLayout = (AppBarLayout) findViewById(R.id.app_bar_layout);

        //==========Recycler code initializing and setting layoutManager starts======
            //----------following code commented on 6th may---
       /* mRecyclerView = (RecyclerView) findViewById(R.id.recycler_view_activity_select_day);
        mRecyclerView.setHasFixedSize(true);
        mRecyclerView.setLayoutManager(new LinearLayoutManager(this));*/
        //==========Recycler code initializing and setting layoutManager ends======
//        edtxtEmployeeNote = (EditText)findViewById(R.id.edtxt_employeenote);
        tv_empname = (TextView)findViewById(R.id.tv_empname);
        tv_empname.setText(userSingletonModel.getEmpName());
        tv_period_date = (TextView)findViewById(R.id.tv_period_date);
        tv_selected_date = (TextView)findViewById(R.id.tv_selected_date);
        img_empnote_view = (ImageView)findViewById(R.id.img_empnote_view);
        img_empnote_add = (ImageView)findViewById(R.id.img_empnote_add);
        img_sup_add_view = (ImageView)findViewById(R.id.img_sup_add_view);
        img_sup_note_view = (ImageView)findViewById(R.id.img_sup_note_view);
        btn_calender = (Button)findViewById(R.id.btn_calender);
        btn_submit = (Button)findViewById(R.id.btn_submit);
        btn_return = (Button)findViewById(R.id.btn_return);
        btn_approve = (Button)findViewById(R.id.btn_approve);

        tv_totalhrs = (TextView)findViewById(R.id.tv_totalhrs);
        tv_period_totalhrs = (TextView)findViewById(R.id.tv_period_totalhrs);
        tv_addOrView_employee_note = (LinearLayout) findViewById(R.id.tv_addOrView_employee_note);
        tv_addOrView_supervisor_note = (LinearLayout) findViewById(R.id.tv_addOrView_supervisor_note);
//        tv_selected_date.setText(TimesheetHome.dateOnSelectedCalender);
//        tv_period_date.setText("Date: "+TimesheetHome.dateOnSelectedCalender);
        tv_addOrView_employee_note.setOnClickListener(this);
        tv_addOrView_supervisor_note.setOnClickListener(this);
        btn_calender.setOnClickListener(this);
        btn_submit.setOnClickListener(this);
        btn_approve.setOnClickListener(this);
        //------added on 3rd dec for making submit button default disable code starts--------
        btn_submit.setAlpha(0.5f);
        btn_submit.setEnabled(false);
        btn_submit.setClickable(false);
        //------added on 3rd dec for making submit button default disable code ends--------


        //------added on  3rd dec, making btn approve & return by default disable as it's work is on progress code starts----

        /*btn_approve.setAlpha(0.5f);
        btn_approve.setEnabled(false);
        btn_approve.setClickable(false);*/ //---commented on 29th may

       /* btn_return.setAlpha(0.5f);
        btn_return.setEnabled(false);
        btn_return.setClickable(false);*/  //---commented on 29th may
        //------added on  3rd dec, making btn approve & return by default disable as it's work is on progress code ends----


        img_empnote_view.setVisibility(View.GONE);
        img_empnote_add.setVisibility(View.GONE);
//        loadDataOfDayWiseTimeSheet();  //------function to load the timesheet day details  //---commented on 6th may
        loadDataOfDayWiseTimeSheetNew();

    }


   //========swicth case for onclickListner code starts========
    @Override
    public void onClick(View view) {
        int id = view.getId();
        switch (id){
            case R.id.tv_addOrView_employee_note:


                //------------8th dec newly added code starts---------
                if(HomeActivity.supervisor_yn_temp.contentEquals("0") && HomeActivity.payrollclerk_yn_temp.contentEquals("0") && HomeActivity.payableclerk_yn_temp.contentEquals("0")) {
                    if ((userSingletonModel.getStatusDescription().contentEquals("APPROVED") || userSingletonModel.getStatusDescription().contentEquals("SUBMITTED") || userSingletonModel.getStatusDescription().contentEquals("POSTED") || userSingletonModel.getStatusDescription().contentEquals("PARTIAL_APPROVE")) && userSingletonModel.getTimesheetSelectDay_empNote().contentEquals("")) {
                        Toast.makeText(getApplicationContext(), "No note is available", Toast.LENGTH_LONG).show();
                    } else if ((userSingletonModel.getStatusDescription().contentEquals("APPROVED") || userSingletonModel.getStatusDescription().contentEquals("SUBMITTED") || userSingletonModel.getStatusDescription().contentEquals("POSTED") || userSingletonModel.getStatusDescription().contentEquals("PARTIAL_APPROVE")) && !userSingletonModel.getTimesheetSelectDay_empNote().contentEquals("")) {
                        loadPopupForAddOrViewNote();

                    } else if (!userSingletonModel.getStatusDescription().contentEquals("APPROVED") || !userSingletonModel.getStatusDescription().contentEquals("SUBMITTED") || !userSingletonModel.getStatusDescription().contentEquals("POSTED") || !userSingletonModel.getStatusDescription().contentEquals("PARTIAL_APPROVE")) {
                        loadPopupForAddOrViewNote();
                    }
                }else if (HomeActivity.supervisor_yn_temp.contentEquals("1") || HomeActivity.payrollclerk_yn_temp.contentEquals("1") || HomeActivity.payableclerk_yn_temp.contentEquals("1")) {
                   if(!userSingletonModel.getStatusDescription().contentEquals("SAVED")){
                       loadPopupForAddOrViewNote();
                   }
                }
                //------------8th dec newly added code ends---------

                break;
            case R.id.tv_addOrView_supervisor_note:
                LayoutInflater li1 = LayoutInflater.from(this);
                View dialog1 = li1.inflate(R.layout.dialog_addsupervisor_note, null);
                final EditText editText1 = (EditText) dialog1.findViewById(R.id.ed_addemp_note);
                Button btn_cancel1 = (Button) dialog1.findViewById(R.id.btn_cancel);
                Button btn_save1 = (Button) dialog1.findViewById(R.id.btn_save);
                ImageButton imgbtn_close1 = (ImageButton) dialog1.findViewById(R.id.imgbtn_close);

                if (!userSingletonModel.getTimesheetSelectDay_supNote().contentEquals("")){
                    editText1.setText(userSingletonModel.getTimesheetSelectDay_supNote());
                }else{
                    editText1.setText("");
                }

                //----added/modified on 4th/31st dec/may----
                if(description_status_temp.contentEquals("1")){
                    btn_save1.setVisibility(View.VISIBLE);
                    btn_cancel1.setVisibility(View.VISIBLE);
                    editText1.setEnabled(true);
                }else {
                    btn_save1.setVisibility(View.GONE);
                    btn_cancel1.setVisibility(View.GONE);
                    editText1.setEnabled(false);
                    editText1.setBackgroundColor(Color.parseColor("#EBEBEB"));
                }
                //----above codse added/modified on 4th/31st dec/may------

                AlertDialog.Builder alert1 = new AlertDialog.Builder(context);
                alert1.setView(dialog1);
                alert1.setCancelable(false);
                //Creating an alert dialog
                final AlertDialog alertDialog1 = alert1.create();
                alertDialog1.show();

                btn_cancel1.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        alertDialog1.dismiss();
                    }
                });
                imgbtn_close1.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        alertDialog1.dismiss();
                    }
                });
                btn_save1.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        userSingletonModel.setTimesheetSelectDay_supNote(editText1.getText().toString());
                        if (userSingletonModel.getTimesheetSelectDay_supNote().contentEquals("")){
                            img_sup_add_view.setVisibility(View.VISIBLE);
                            img_sup_note_view.setVisibility(View.GONE);
                        }else if (!userSingletonModel.getTimesheetSelectDay_supNote().contentEquals("")){
                            img_sup_add_view.setVisibility(View.GONE);
                            img_sup_note_view.setVisibility(View.VISIBLE);
                        }
                        alertDialog1.dismiss();
                    }
                });
                break;
            case R.id.btn_calender:
                startActivity(new Intent(TimesheetSelectDay.this,TimesheetHome.class));
                break;
            case R.id.btn_submit:
                validatePasswordAndSubmit(); //----this function will validate password and submit data to the server by calling submitData()
                break;
            case R.id.btn_approve:
                approveEmployee();
                break;

        }
    }
   //========swicth case for onclickListner code ends========

    //==================approve function code starts, added on 31st may==========
    public void approveEmployee(){
        String WeekDate="", EmployeeNote= "", SupervisorNote="";
        final JSONObject DocumentElementobj = new JSONObject();
        JSONArray req = new JSONArray();
        JSONObject reqObjdt = new JSONObject();
        try {
            SimpleDateFormat dateFormat = new SimpleDateFormat("MM/dd/yyy");
            Date sourceDate = null;
            for (int i = 0; i < arrayListTimesheetSelectDayModelsWeekDay.size(); i++) {
                JSONObject reqObj = new JSONObject();
                try {
                    sourceDate = dateFormat.parse(arrayListTimesheetSelectDayModelsWeekDay.get(i).getWeekDate());
                    SimpleDateFormat targetFormat = new SimpleDateFormat("MM-dd-yyyy");
                    WeekDate = targetFormat.format(sourceDate);
                    EmployeeNote = userSingletonModel.getTimesheetSelectDay_empNote();
                    SupervisorNote = userSingletonModel.getTimesheetSelectDay_supNote();
                }catch (NullPointerException e){
                    e.printStackTrace();
                }
                reqObj.put("WeekDate", WeekDate);
                reqObj.put("EmployeeNote", EmployeeNote);
                reqObj.put("SupervisorNote", SupervisorNote);

                req.put(reqObj);

            }
            DocumentElementobj.put("UserID",userSingletonModel.getUserID());
            DocumentElementobj.put("UserCode", userSingletonModel.getUserName());
            if(HomeActivity.supervisor_yn_temp.contentEquals("1")) {
                DocumentElementobj.put("EmployeeID", userSingletonModel.getSupervisor_id_person());
            }else if(HomeActivity.payrollclerk_yn_temp.contentEquals("1") || HomeActivity.payableclerk_yn_temp.contentEquals("1")){
                DocumentElementobj.put("EmployeeID", userSingletonModel.getPayable_payroll_supervisor_person_id());
            }
            DocumentElementobj.put("UserType",userSingletonModel.getAll_employee_type());
            DocumentElementobj.put( "SubmitValue", req );
            reqObjdt.put("dt", DocumentElementobj);
            Log.d("jsonTest",DocumentElementobj.toString());
        }catch (Exception e){
            e.printStackTrace();
        }

        //==========follwing is the code for volley i.e to send the data to the server... code starts=======
        String url = Config.BaseUrl+"TimeSheetApprove";
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
                            String message = jsonObject.getString("message");
                            Toast.makeText(getApplicationContext(),message,Toast.LENGTH_LONG).show();
                            loadDataOfDayWiseTimeSheetNew();
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
                params.put("TSApprovalDataJSON", DocumentElementobj.toString());
                params.put("CorpId", userSingletonModel.getCorpID());
                return params;
            }
        };

        RequestQueue requestQueue = Volley.newRequestQueue(TimesheetSelectDay.this);
        requestQueue.add(stringRequest);
        //==========follwing is the code for volley i.e to send the data to the server... code ends=======

    }
    //==================approve function code ends==========

    //===============added on 8th dec, function to load alert dialog for add or view note button code starts=============
    public void loadPopupForAddOrViewNote(){
        LayoutInflater li = LayoutInflater.from(this);
        View dialog = li.inflate(R.layout.dialog_addemployee_note, null);
        final EditText editText = (EditText) dialog.findViewById(R.id.ed_addemp_note);
        Button btn_cancel = (Button) dialog.findViewById(R.id.btn_cancel);
        Button btn_save = (Button) dialog.findViewById(R.id.btn_save);
        ImageButton imgbtn_close = (ImageButton) dialog.findViewById(R.id.imgbtn_close);
//        editText.setText(userSingletonModel.getTimesheetSelectDay_empNote());
        AlertDialog.Builder alert = new AlertDialog.Builder(context);
        alert.setView(dialog);
        alert.setCancelable(false);
        //Creating an alert dialog
        final AlertDialog alertDialog = alert.create();
        if(HomeActivity.supervisor_yn_temp.contentEquals("0") && HomeActivity.payrollclerk_yn_temp.contentEquals("0") && HomeActivity.payableclerk_yn_temp.contentEquals("0")) {
            alertDialog.show();
        }else if ((HomeActivity.supervisor_yn_temp.contentEquals("1") || HomeActivity.payrollclerk_yn_temp.contentEquals("1") || HomeActivity.payableclerk_yn_temp.contentEquals("1")) && !userSingletonModel.getTimesheetSelectDay_empNote().contentEquals("")) {
            alertDialog.show();
        }
        //---newly added on 8th dec to add the condition check for editable/non editable mode edittext code starts----
        if ((userSingletonModel.getStatusDescription().contentEquals("APPROVED") || userSingletonModel.getStatusDescription().contentEquals("SUBMITTED") || userSingletonModel.getStatusDescription().contentEquals("POSTED")  || userSingletonModel.getStatusDescription().contentEquals("PARTIAL_APPROVE")) && !userSingletonModel.getTimesheetSelectDay_empNote().contentEquals("")){
            btn_save.setVisibility(View.GONE);
            btn_cancel.setVisibility(View.GONE);
            editText.setText(userSingletonModel.getTimesheetSelectDay_empNote());
            editText.setClickable(false);
            editText.setFocusableInTouchMode(false);
            editText.setFocusable(false);
        }else{
            btn_save.setVisibility(View.VISIBLE);
            btn_cancel.setVisibility(View.VISIBLE);
            editText.setText(userSingletonModel.getTimesheetSelectDay_empNote());
            editText.setClickable(true);
            editText.setFocusableInTouchMode(true);
            editText.setFocusable(true);
        }
        //---newly added on 8th dec to add the condition check for editable/non editable mode edittext code ends----
        btn_cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                alertDialog.dismiss();
            }
        });
        imgbtn_close.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                alertDialog.dismiss();
            }
        });
        btn_save.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                userSingletonModel.setTimesheetSelectDay_empNote(editText.getText().toString());
                if (userSingletonModel.getTimesheetSelectDay_empNote().contentEquals("")){
                    img_empnote_add.setVisibility(View.VISIBLE);
                    img_empnote_view.setVisibility(View.GONE);
                }else if (!userSingletonModel.getTimesheetSelectDay_empNote().contentEquals("")){
                    img_empnote_add.setVisibility(View.GONE);
                    img_empnote_view.setVisibility(View.VISIBLE);
                }
                alertDialog.dismiss();
            }
        });
    }
    //===============function to load alert dialog for add or view note button code ends=============

    @Override
    public void onBackPressed() {
        super.onBackPressed();

        //---following code is commented on 6th dec to get the calender saved state data
       /* Intent intent = new Intent(TimesheetSelectDay.this,TimesheetHome.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        startActivity(intent);
        TimesheetSelectDay.this.finish();*/
       //---above code is commented on 6th dec to get the calender saved state data
    }

  /*  @Override
    protected void onPostResume() {
        super.onPostResume();
        loadDataOfDayWiseTimeSheet();
    }*/

    //=============function to validate password and submit data by calling submitData() code starts===========
    public void validatePasswordAndSubmit(){
        LayoutInflater li2 = LayoutInflater.from(this);
        View dialog2 = li2.inflate(R.layout.dialog_validate_pwd, null);
        final EditText ed_pwd = (EditText) dialog2.findViewById(R.id.ed_pwd);
        final TextView tv_incorrectpwd = (TextView)dialog2.findViewById(R.id.tv_incorrectpwd);
        Button btn_cancel2 = (Button) dialog2.findViewById(R.id.btn_cancel);
        Button btn_save2 = (Button) dialog2.findViewById(R.id.btn_submit);
        AlertDialog.Builder alert2 = new AlertDialog.Builder(context);
        alert2.setView(dialog2);
        alert2.setCancelable(false);
        //Creating an alert dialog
        final AlertDialog alertDialog2 = alert2.create();
        alertDialog2.show();

        btn_cancel2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                alertDialog2.dismiss();
            }
        });

        btn_save2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //==========follwing is the code to validate password... code starts(server side working going on)=======
                String url = Config.BaseUrl+"ValidatePassword";

                final ProgressDialog loading = ProgressDialog.show(context, "Loading", "Please wait...", true, false);
                StringRequest stringRequest = new StringRequest(Request.Method.POST, url, new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        JSONObject jsonObj = null;
                        try{
                            jsonObj = XML.toJSONObject(response);
                            String responseData = jsonObj.toString();
                            String val = "";
                            JSONObject resobj = new JSONObject(responseData);
                            Log.d("validateData=>",responseData.toString());
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
                                    if (status.contentEquals("true")) {
                                        submitData();  //----calling this function to submit the data to the server
                                        alertDialog2.dismiss();
                                    }
                                    else if (status.contentEquals("false")){
                                        tv_incorrectpwd.setVisibility(View.VISIBLE);
                                        tv_incorrectpwd.setText(jsonObject.getString("message"));
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
                        params.put("UserType", "MAIN");
                        params.put("UserId", userSingletonModel.getUserID());
                        params.put("Password", ed_pwd.getText().toString());
                        params.put("CompanyId", "1");
                        params.put("CorpId", userSingletonModel.getCorpID());
                        return params;
                    }
                };

                RequestQueue requestQueue = Volley.newRequestQueue(TimesheetSelectDay.this);
                requestQueue.add(stringRequest);
                //==========above is the code to validate password... code ends(server side working going on)=======
            }
        });
    }
    //=============function to validate password and submit data by calling submitData() code ends===========

    //=============function to submit the data to the server using volley code starts============
    public void submitData(){
        //==========follwing is the code for volley i.e to send/save the data to the server... code starts(server side working going on)=======
//        Log.d("EmloyeeNote=>",userSingletonModel.getTimesheetSelectDay_empNote());

        String url = Config.BaseUrl+"EmployeeTimesheetSubmit";

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
                    Log.d("saveData=>",responseData.toString());
                    Iterator<?> keys = resobj.keys();
                    loading.dismiss();
//                    loadDataOfDayWiseTimeSheet(); //----commented on 6th may//---to load the fresh data after submitting the data
                    loadDataOfDayWiseTimeSheetNew();
                            while(keys.hasNext() ) {
                                String key = (String) keys.next();
                                if (resobj.get(key) instanceof JSONObject) {
                                    JSONObject xx = new JSONObject(resobj.get(key).toString());
                                    val = xx.getString("content");
                                    JSONObject jsonObject = new JSONObject(val);
                                    Log.d("saveList: ",val);
                                    String status = jsonObject.getString("status");
                                    if (status.contentEquals("true")) {
                                        //---------Alert dialog code starts(added on 3rd dec)--------
                                        final AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(TimesheetSelectDay.this);
                                        alertDialogBuilder.setMessage(jsonObject.getString("message"));
                                        alertDialogBuilder.setPositiveButton("OK",
                                                new DialogInterface.OnClickListener() {
                                                    @Override
                                                    public void onClick(DialogInterface arg0, int arg1) {
                                                     alertDialogBuilder.setCancelable(true);
                                                    }
                                                });
                                        AlertDialog alertDialog = alertDialogBuilder.create();
                                        alertDialog.show();

                                        //--------Alert dialog code ends--------
                                    }
                                    else if (!status.contentEquals("true")){
                                        //---------Alert dialog code starts(added on 3rd dec)--------
                                        final AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(TimesheetSelectDay.this);
                                        alertDialogBuilder.setMessage(jsonObject.getString("message"));
                                        alertDialogBuilder.setPositiveButton("OK",
                                                new DialogInterface.OnClickListener() {
                                                    @Override
                                                    public void onClick(DialogInterface arg0, int arg1) {
                                                        alertDialogBuilder.setCancelable(true);
                                                    }
                                                });
                                        AlertDialog alertDialog = alertDialogBuilder.create();
                                        alertDialog.show();

                                        //--------Alert dialog code ends--------
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
                params.put("CorpID", userSingletonModel.getCorpID());
                params.put("UserId", userSingletonModel.getUserID());
                params.put("Weekdate", TimesheetHome.dateOnSelectedCalender);
                params.put("WeekStartDate", userSingletonModel.getPeriodStartDate());
                params.put("WeekEndDate", userSingletonModel.getPeriodEndDate());
                params.put("EmployeeNote", userSingletonModel.getTimesheetSelectDay_empNote());
                params.put("SupervisorNote", "");
                params.put("UserType", "EMPLOYEE");
                return params;
            }
        };

        RequestQueue requestQueue = Volley.newRequestQueue(TimesheetSelectDay.this);
        requestQueue.add(stringRequest);
        //==========above is the code for volley i.e to send/save the data to the server... code ends(server side working going on)=======
    }
    //=============function to submit the data to the server using volley code ends============


    //==========volley code to get DayWiseTimeSheet using volley code starts(coded by Satabhisha)===========
   /* public void loadDataOfDayWiseTimeSheet(){
//        String url = "http://220.225.40.151:9012/TimesheetService.asmx/GetDayWiseTimeSheet";
        String url = Config.BaseUrl+"GetDayWiseTimeSheet";
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
                            Log.d("getDayData",responseData.toString());

                            //------code to clear the arraylist so that after submitting the data fresh data will show in the list instead of redundant data code starts-----
                           if(!weekDaysList.isEmpty()){
                               weekDaysList.clear();
                           }
                            if(!weekDaysArrayList.isEmpty()){
                                weekDaysArrayList.clear();
                            }
                            if(!arrayListTimesheetSelectDayModelsWeekDay.isEmpty()){
                                arrayListTimesheetSelectDayModelsWeekDay.clear();
                            }
                            //------code to clear the arraylist so that after submitting the data fresh data will show in the list instead of redundant data code ends-----

                            Iterator<?> keys = resobj.keys();
                            while(keys.hasNext() ) {
                                String key = (String) keys.next();
                                if (resobj.get(key) instanceof JSONObject) {
                                    JSONObject xx = new JSONObject(resobj.get(key).toString());
                                    val = xx.getString("content");
                                    Log.d("getDayData1", xx.getString("content"));
                                    JSONObject jsonObject = new JSONObject(val);
                                    String status = jsonObject.getString("status");
                                    if(status.equalsIgnoreCase("true")){
                                        JSONArray jsonArray = jsonObject.getJSONArray("DayWiseTimeSheet");
                                        for (int i = 0; i < jsonArray.length(); i++) {
                                            JSONObject jobject = jsonArray.getJSONObject(i);

                                   //=============Commented temporary starts==============
                                   *//*         empName = (jobject.getString("EmpName"));
                                            periodRange = (jobject.getString("PeriodRange"));
                                            statusCode = (jobject.getString("statusCode"));
//                                             set text

                                            txtPeriodDate.setText("Period Range: " + periodRange);*//*

                                        *//*    if (from.equalsIgnoreCase("1")) {

                                                txtEmpName.setText("Employee: " + getIntent().getExtras().getString("name"));

                                                String status1 = getIntent().getExtras().getString("status");
                                                if (status1.equalsIgnoreCase("1")
                                                        || status1.equalsIgnoreCase("3")
                                                        || status1.equalsIgnoreCase("4")
                                                        || status1.equalsIgnoreCase("5")
                                                        || status1.equalsIgnoreCase("6")
                                                        || status1.equalsIgnoreCase("7")
                                                        || status1.equalsIgnoreCase("8")
                                                        || status1.equalsIgnoreCase("0")
                                                        ) {
                                                    btnSubmitted.setEnabled(false);
                                                    btnApproved.setEnabled(false);
                                                    btnReturned.setEnabled(false);
                                                } else {
                                                    btnSubmitted.setEnabled(false);
                                                    btnApproved.setEnabled(true);
                                                    btnReturned.setEnabled(true);
                                                }
                                            } else {

                                                txtEmpName.setText("Employee: " + empName);

                                                if (jobject.getString("statusCode").equalsIgnoreCase("2")
                                                        || (jobject.getString("statusCode").equalsIgnoreCase("5"))
                                                        || (jobject.getString("statusCode").equalsIgnoreCase("6"))
                                                        || (jobject.getString("statusCode").equalsIgnoreCase("7"))
                                                        || (jobject.getString("statusCode").equalsIgnoreCase("0"))
                                                        || (jobject.getString("statusCode").equalsIgnoreCase("8"))
                                                        || (jobject.getString("statusCode").equalsIgnoreCase("4"))) {
                                                    btnSubmitted.setEnabled(false);
                                                    btnApproved.setEnabled(false);
                                                    btnReturned.setEnabled(false);
                                                } else {
                                                    btnSubmitted.setEnabled(true);
                                                    btnApproved.setEnabled(false);
                                                    btnReturned.setEnabled(false);
                                                }
                                            }*//*
                                   //=================Commented temporary ends==================

                                            JSONArray weekDays = jobject.getJSONArray("WeekDays");
                                            for (int j = 0; j < weekDays.length(); j++) {
                                                JSONObject days = weekDays.getJSONObject(j);
                                                TimesheetSelectDayModel timesheetSelectDayModel = new TimesheetSelectDayModel();
//                                                Week week = new Week();
                                                timesheetSelectDayModel.setWeekDate(days.getString("WeekDate"));
                                                timesheetSelectDayModel.setTotalHours(days.getString("TotalHours"));
                                                timesheetSelectDayModel.setEmpNote(days.getString("EmpNote"));
                                                userSingletonModel.setTimesheetSelectDay_empNote(days.getString("EmpNote"));  //----added on 4th dec
                                                timesheetSelectDayModel.setSupNote(days.getString("SupNote"));
                                                userSingletonModel.setTimesheetSelectDay_supNote(days.getString("SupNote"));  //----added on 4th dec
                                                timesheetSelectDayModel.setDayStatus(days.getString("DayStatus"));
                                                timesheetSelectDayModel.setColorCode(days.getString("ColorCode"));
                                                colorcode = days.getString("ColorCode");
                                                timesheetSelectDayModel.setStatusDescription(days.getString("StatusDescription"));
                                                tv_totalhrs.setText(timesheetSelectDayModel.getTotalHours());
                                                arrayListTimesheetSelectDayModelsWeekDay.add(timesheetSelectDayModel);

                                                //--------newly added 4th dec and modified on 8th dec--------
                                                //=============Following is the code to check whether empNote is empty or not(the empnote will be present in the dialog box and stored via SingletonModel class)==========
                                                if ((days.getString("StatusDescription").contentEquals("APPROVED") || days.getString("StatusDescription").contentEquals("SUBMITTED") || days.getString("StatusDescription").contentEquals("POSTED")  || days.getString("StatusDescription").contentEquals("PARTIAL_APPROVE")) && userSingletonModel.getTimesheetSelectDay_empNote().contentEquals("")) {
                                                    img_empnote_add.setVisibility(View.GONE);
                                                    img_empnote_view.setVisibility(View.GONE);
                                                }else if((days.getString("StatusDescription").contentEquals("APPROVED") || days.getString("StatusDescription").contentEquals("SUBMITTED") || days.getString("StatusDescription").contentEquals("POSTED")  || days.getString("StatusDescription").contentEquals("PARTIAL_APPROVE")) && !userSingletonModel.getTimesheetSelectDay_empNote().contentEquals("")){
                                                    img_empnote_add.setVisibility(View.GONE);
                                                    img_empnote_view.setVisibility(View.VISIBLE);
                                                }else if(!days.getString("StatusDescription").contentEquals("APPROVED") || !days.getString("StatusDescription").contentEquals("SUBMITTED") || !days.getString("StatusDescription").contentEquals("POSTED")  || !days.getString("StatusDescription").contentEquals("PARTIAL_APPROVE")){
                                                    img_empnote_add.setVisibility(View.VISIBLE);
                                                    img_empnote_view.setVisibility(View.GONE);
                                                }
                                                //==============code to check empNote is empty or not ends===========

                                                //***supervisor note is not modified
                                                //=============Following is the code to check whether supNote is empty or not(the supnote will be present in the dialog box and stored via SingletonModel class)==========
                                                if (userSingletonModel.getTimesheetSelectDay_supNote().contentEquals("")) {
                                                    img_sup_note_view.setVisibility(View.GONE);
                                                    tv_addOrView_supervisor_note.setClickable(false);
                                                  //  img_sup_add_view.setVisibility(View.VISIBLE);  //----commented on 4th dec
                                                } else if (!userSingletonModel.getTimesheetSelectDay_supNote().contentEquals("")) {
                                                 //   img_sup_add_view.setVisibility(View.GONE);  //---commented on 4th dec
                                                    img_sup_note_view.setVisibility(View.VISIBLE);
                                                    tv_addOrView_supervisor_note.setClickable(true);
                                                }
                                                //==============code to check supNote is empty or not ends===========
                                                //-------above code newly added on 4th dec---------

                                                //=========newly added on 1st dec, to check conditions for button submit visibility/invisibility code starts ===========
                                                 //        btn_submit
//                                                if(userSingletonModel.getStatusDescription().contentEquals("APPROVED") || userSingletonModel.getStatusDescription().contentEquals("SUBMITTED") || userSingletonModel.getStatusDescription().contentEquals("POSTED") || userSingletonModel.getStatusDescription().contentEquals("NOT STARTED") || userSingletonModel.getStatusDescription().contentEquals("PARTIAL_APPROVE")){
                                                if(days.getString("StatusDescription").contentEquals("APPROVED") || days.getString("StatusDescription").contentEquals("SUBMITTED") || days.getString("StatusDescription").contentEquals("POSTED") || days.getString("StatusDescription").contentEquals("NOT STARTED") || days.getString("StatusDescription").contentEquals("PARTIAL_APPROVE")){
                                                    btn_submit.setAlpha(0.5f);
                                                    btn_submit.setEnabled(false);
                                                    btn_submit.setClickable(false);
                                                }else{
                                                    btn_submit.setAlpha(1.0f);
                                                    btn_submit.setEnabled(true);
                                                    btn_submit.setClickable(true);
                                                }
                                                Log.d("Employee Status=>",userSingletonModel.getStatusDescription());
                                                //=========newly added on 1st dec, to check conditions for button submit visibility/invisibility code ends===========


                                                JSONArray day = days.getJSONArray("DayHrs");
                                                for (int d = j; d < day.length(); d++) {
                                                    JSONObject dayHrs = day.getJSONObject(d);
                                                    WeekDays weekDays1 = new WeekDays();
                                                    //--------following condition is newly added on 6th May 2019, starts...--------------
                                                    if(d==0){
                                                        userSingletonModel.setPeriodStartDate(dayHrs.getString("DayDate"));
                                                    }
                                                    //--------above condition is newly added on 6th May 2019, ends...--------------

                                                    if(dayHrs.getString("ActiveYN").contentEquals("true")) { //---condition added on 1st may 2019
                                                        weekDays1.setDayName(dayHrs.getString("DayName"));
                                                        weekDays1.setHours(dayHrs.getString("Hours"));
                                                        weekDays1.setActiveYN(dayHrs.getString("ActiveYN"));
                                                        weekDays1.setDayDate(dayHrs.getString("DayDate"));
                                                        weekDays1.setColorCode(colorcode);
                                                        weekDaysList.add(weekDays1);
                                                        weekDaysArrayList.add(weekDays1);

                                                    }


                                                }
                                                datePeriod.clear();
                                                for(int z=0;z<weekDaysList.size();z++){
//                                                    userSingletonModel.setPeriodStartDate(weekDaysList.get(0).getDayDate()); //--commented on 6th may 2019
                                                    userSingletonModel.setPeriodEndDate(weekDaysList.get(weekDaysList.size()-1).getDayDate());
                                                    datePeriod.add(weekDaysList.get(z).getDayDate());
                                                }

                                                tv_period_date.setText("("+userSingletonModel.getPeriodStartDate()+" - "+userSingletonModel.getPeriodEndDate()+")");
                                                mRecyclerView.setAdapter(new CustomSelectDayAdapter(TimesheetSelectDay.this,weekDaysArrayList));
                                                ListView listView = (ListView)findViewById(R.id.lv_color);
                                                listView.setAdapter(new displayStatusAdapter());
                                                listView.setDivider(null);
                                            }

                                        }

                                        loading.dismiss();
                                    }else{
                                        Toast.makeText(getApplicationContext(),jsonObject.getString("message"),Toast.LENGTH_LONG).show();
                                    }
                                }
                            }
                        }catch (JSONException e){
                            e.printStackTrace();
                            loading.dismiss();
                            Toast.makeText(getApplicationContext(),"Please try after sometime.Internal error",Toast.LENGTH_LONG).show();
                        }

                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                loading.dismiss();
                Toast.makeText(getApplicationContext(),"Please try after sometime.Internal error",Toast.LENGTH_LONG).show();
            }
        }) {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> params = new HashMap<>();
                params.put("CorpId", userSingletonModel.getCorpID());
                params.put("UserId", userSingletonModel.getUserID());
                params.put("UserType",userSingletonModel.getUserType());
                params.put("StartDate",TimesheetHome.dateOnSelectedCalender);
                Log.d("StartDate",TimesheetHome.dateOnSelectedCalender);

              *//*  params.put("CorpId", "gst-inc-101");
                params.put("UserId","1");
                params.put("UserType","MAIN");
                params.put("StartDate","10-07-2018");*//*
                return params;
            }
        };

        RequestQueue requestQueue = Volley.newRequestQueue(TimesheetSelectDay.this);
        requestQueue.add(stringRequest);



    }*/
    //===========volley code to get DayWiseTimeSheet using volley code ends(above line temporary commented)============

    //==========added on 6th may, volley code to get DayWiseTimeSheet using volley code starts(coded by Satabhisha)===========
    public void loadDataOfDayWiseTimeSheetNew(){
//        String url = "http://220.225.40.151:9012/TimesheetService.asmx/GetDayWiseTimeSheet";
        String url = Config.BaseUrl+"GetDayWiseTimeSheet";
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
                            Log.d("getDayData",responseData.toString());

                            //------code to clear the arraylist so that after submitting the data fresh data will show in the list instead of redundant data code starts-----
                            if(!weekDaysList.isEmpty()){
                                weekDaysList.clear();
                            }
                            if(!weekDaysArrayList.isEmpty()){
                                weekDaysArrayList.clear();
                            }
                            if(!arrayListTimesheetSelectDayModelsWeekDay.isEmpty()){
                                arrayListTimesheetSelectDayModelsWeekDay.clear();
                            }
                            //----added on 6th may=======
                            if(!listTimesheetSelectDayModelsWeekDay.isEmpty()){
                                listTimesheetSelectDayModelsWeekDay.clear();
                            }
                            if(!listDataChild.isEmpty()){
                                listDataChild.clear();
                            }
                            //----above code added on 6th may------

                            //------code to clear the arraylist so that after submitting the data fresh data will show in the list instead of redundant data code ends-----

                            Iterator<?> keys = resobj.keys();
                            while(keys.hasNext() ) {
                                String key = (String) keys.next();
                                if (resobj.get(key) instanceof JSONObject) {
                                    JSONObject xx = new JSONObject(resobj.get(key).toString());
                                    val = xx.getString("content");
                                    Log.d("getDayData1", xx.getString("content"));
                                    JSONObject jsonObject = new JSONObject(val);
                                    String status = jsonObject.getString("status");
                                    if(status.equalsIgnoreCase("true")){
                                        JSONArray jsonArray = jsonObject.getJSONArray("DayWiseTimeSheet");
                                        for (int i = 0; i < jsonArray.length(); i++) {
                                            JSONObject jobject = jsonArray.getJSONObject(i);
                                            //-----following snippet added on 14th may-------------
                                            userSingletonModel.setPeriodStartDate(jobject.getString("PeriodStartDate"));
                                            userSingletonModel.setPeriodEndDate(jobject.getString("PeriodEndDate"));
                                            //------above snippet added on 14th may-----------
                                            JSONArray weekDays = jobject.getJSONArray("WeekDays");
                                            for (int j = 0; j < weekDays.length(); j++) {
                                                JSONObject days = weekDays.getJSONObject(j);
                                                TimesheetSelectDayModel timesheetSelectDayModel = new TimesheetSelectDayModel();
//                                                Week week = new Week();
                                                timesheetSelectDayModel.setWeekDate(days.getString("WeekDate"));
                                                timesheetSelectDayModel.setTotalHours(days.getString("TotalHours"));
                                                timesheetSelectDayModel.setEmpNote(days.getString("EmpNote"));
                                                userSingletonModel.setTimesheetSelectDay_empNote(days.getString("EmpNote"));  //----added on 4th dec
                                                timesheetSelectDayModel.setSupNote(days.getString("SupNote"));
                                                userSingletonModel.setTimesheetSelectDay_supNote(days.getString("SupNote"));  //----added on 4th dec
                                                timesheetSelectDayModel.setDayStatus(days.getString("DayStatus"));
                                                timesheetSelectDayModel.setColorCode(days.getString("ColorCode"));
                                                colorcode = days.getString("ColorCode");
                                                timesheetSelectDayModel.setStatusDescription(days.getString("StatusDescription"));
//                                                tv_totalhrs.setText(timesheetSelectDayModel.getTotalHours());
                                                arrayListTimesheetSelectDayModelsWeekDay.add(timesheetSelectDayModel);
                                                listTimesheetSelectDayModelsWeekDay.add(timesheetSelectDayModel);

                                                //--------newly added 4th dec and modified on 8th dec--------
                                                //=============Following is the code to check whether empNote is empty or not(the empnote will be present in the dialog box and stored via SingletonModel class)==========
                                                if(HomeActivity.supervisor_yn_temp.contentEquals("0") && HomeActivity.payrollclerk_yn_temp.contentEquals("0") && HomeActivity.payableclerk_yn_temp.contentEquals("0")) {
                                                    if ((days.getString("StatusDescription").contentEquals("APPROVED") || days.getString("StatusDescription").contentEquals("SUBMITTED") || days.getString("StatusDescription").contentEquals("POSTED") || days.getString("StatusDescription").contentEquals("PARTIAL_APPROVE")) && userSingletonModel.getTimesheetSelectDay_empNote().contentEquals("")) {
                                                        img_empnote_add.setVisibility(View.GONE);
                                                        img_empnote_view.setVisibility(View.GONE);
                                                    } else if ((days.getString("StatusDescription").contentEquals("APPROVED") || days.getString("StatusDescription").contentEquals("SUBMITTED") || days.getString("StatusDescription").contentEquals("POSTED") || days.getString("StatusDescription").contentEquals("PARTIAL_APPROVE")) && !userSingletonModel.getTimesheetSelectDay_empNote().contentEquals("")) {
                                                        img_empnote_add.setVisibility(View.GONE);
                                                        img_empnote_view.setVisibility(View.VISIBLE);
                                                    } else if (!days.getString("StatusDescription").contentEquals("APPROVED") || !days.getString("StatusDescription").contentEquals("SUBMITTED") || !days.getString("StatusDescription").contentEquals("POSTED") || !days.getString("StatusDescription").contentEquals("PARTIAL_APPROVE")) {
                                                        img_empnote_add.setVisibility(View.VISIBLE);
                                                        img_empnote_view.setVisibility(View.GONE);
                                                    }
                                                }else if (HomeActivity.supervisor_yn_temp.contentEquals("1") || HomeActivity.payrollclerk_yn_temp.contentEquals("1") || HomeActivity.payableclerk_yn_temp.contentEquals("1")){
                                                    img_empnote_add.setVisibility(View.GONE);
                                                    if(userSingletonModel.getTimesheetSelectDay_empNote().contentEquals("")) {
                                                        img_empnote_view.setVisibility(View.GONE);
                                                    } else if (!userSingletonModel.getTimesheetSelectDay_empNote().contentEquals("")) {
                                                        if(days.getString("StatusDescription").contentEquals("SAVED")){
                                                            img_empnote_view.setVisibility(View.GONE);
                                                        }else {
                                                            img_empnote_view.setVisibility(View.VISIBLE);
                                                        }
                                                    }
                                                }
                                                //==============code to check empNote is empty or not ends===========

                                                //***supervisor note is not modified
                                                //=============Following is the code to check whether supNote is empty or not(the supnote will be present in the dialog box and stored via SingletonModel class)==========
                                                if(HomeActivity.supervisor_yn_temp.contentEquals("0") && HomeActivity.payrollclerk_yn_temp.contentEquals("0") && HomeActivity.payableclerk_yn_temp.contentEquals("0")) {
                                                    if (userSingletonModel.getTimesheetSelectDay_supNote().contentEquals("")) {
                                                        img_sup_note_view.setVisibility(View.GONE);
                                                        tv_addOrView_supervisor_note.setClickable(false);
                                                        //  img_sup_add_view.setVisibility(View.VISIBLE);  //----commented on 4th dec
                                                    } else if (!userSingletonModel.getTimesheetSelectDay_supNote().contentEquals("")) {
                                                        //   img_sup_add_view.setVisibility(View.GONE);  //---commented on 4th dec
                                                        img_sup_note_view.setVisibility(View.VISIBLE);
                                                        tv_addOrView_supervisor_note.setClickable(true);
                                                    }
                                                } else if(HomeActivity.supervisor_yn_temp.contentEquals("1") || HomeActivity.payrollclerk_yn_temp.contentEquals("1") || HomeActivity.payableclerk_yn_temp.contentEquals("1")){
                                                    if((days.getString("StatusDescription").contentEquals("SUBMITTED") || days.getString("StatusDescription").contentEquals("PARTIAL_APPROVE")) && userSingletonModel.getTimesheetSelectDay_supNote().contentEquals("")){
                                                        img_sup_note_view.setVisibility(View.GONE);
                                                        tv_addOrView_supervisor_note.setClickable(true);
                                                        img_sup_add_view.setVisibility(View.VISIBLE);
                                                    }else if((days.getString("StatusDescription").contentEquals("SUBMITTED") || days.getString("StatusDescription").contentEquals("PARTIAL_APPROVE")) && !userSingletonModel.getTimesheetSelectDay_supNote().contentEquals("")){
                                                        img_sup_note_view.setVisibility(View.VISIBLE);
                                                        tv_addOrView_supervisor_note.setClickable(true);
                                                        img_sup_add_view.setVisibility(View.GONE);
                                                    }else if((!days.getString("StatusDescription").contentEquals("SUBMITTED") || !days.getString("StatusDescription").contentEquals("PARTIAL_APPROVE")) && userSingletonModel.getTimesheetSelectDay_supNote().contentEquals("")){
                                                        img_sup_note_view.setVisibility(View.GONE);
                                                        tv_addOrView_supervisor_note.setClickable(false);
                                                        img_sup_add_view.setVisibility(View.GONE);
                                                    }else if((!days.getString("StatusDescription").contentEquals("SUBMITTED") || !days.getString("StatusDescription").contentEquals("PARTIAL_APPROVE")) && !userSingletonModel.getTimesheetSelectDay_supNote().contentEquals("")){
                                                        img_sup_note_view.setVisibility(View.VISIBLE);
                                                        tv_addOrView_supervisor_note.setClickable(true);
                                                        img_sup_add_view.setVisibility(View.GONE);
                                                    }
                                                }
                                                //==============code to check supNote is empty or not ends===========
                                                //-------above code newly added on 4th dec---------

                                                //=========newly added on 1st dec, to check conditions for button submit visibility/invisibility code starts ===========
                                                //        btn_submit
//                                                if(userSingletonModel.getStatusDescription().contentEquals("APPROVED") || userSingletonModel.getStatusDescription().contentEquals("SUBMITTED") || userSingletonModel.getStatusDescription().contentEquals("POSTED") || userSingletonModel.getStatusDescription().contentEquals("NOT STARTED") || userSingletonModel.getStatusDescription().contentEquals("PARTIAL_APPROVE")){
                                                if(HomeActivity.supervisor_yn_temp.contentEquals("0") && HomeActivity.payrollclerk_yn_temp.contentEquals("0") && HomeActivity.payableclerk_yn_temp.contentEquals("0")) {
                                                    if (days.getString("StatusDescription").contentEquals("APPROVED") || days.getString("StatusDescription").contentEquals("SUBMITTED") || days.getString("StatusDescription").contentEquals("POSTED") || days.getString("StatusDescription").contentEquals("NOT STARTED") || days.getString("StatusDescription").contentEquals("PARTIAL_APPROVE")) {
                                                        btn_submit.setAlpha(0.5f);
                                                        btn_submit.setEnabled(false);
                                                        btn_submit.setClickable(false);
                                                    } else {
                                                        btn_submit.setAlpha(1.0f);
                                                        btn_submit.setEnabled(true);
                                                        btn_submit.setClickable(true);
                                                    }
                                                }else if(HomeActivity.supervisor_yn_temp.contentEquals("1") || HomeActivity.payrollclerk_yn_temp.contentEquals("1") || HomeActivity.payableclerk_yn_temp.contentEquals("1")){
                                                    btn_submit.setAlpha(0.5f);
                                                    btn_submit.setEnabled(false);
                                                    btn_submit.setClickable(false);
                                                    if(days.getString("StatusDescription").contentEquals("SUBMITTED") || (days.getString("StatusDescription").contentEquals("PARTIAL_APPROVE")))
                                                    {
                                                        description_status_temp = "1";
                                                        btn_approve.setEnabled(true);
                                                        btn_approve.setClickable(true);

                                                        btn_return.setEnabled(true);
                                                        btn_return.setClickable(true);
                                                    }else{
                                                        description_status_temp = "0";
                                                        btn_approve.setAlpha(0.5f);
                                                        btn_approve.setEnabled(false);
                                                        btn_approve.setClickable(false);

                                                        btn_return.setAlpha(0.5f);
                                                        btn_return.setEnabled(false);
                                                        btn_return.setClickable(false);
                                                    }

                                                }
                                                Log.d("Employee Status=>",userSingletonModel.getStatusDescription());
                                                //=========newly added on 1st dec, to check conditions for button submit visibility/invisibility code ends===========


                                                JSONArray day = days.getJSONArray("DayHrs");
                                                weekDaysArrayList = new ArrayList<>();
                                                for (int d = 0; d < day.length(); d++) {

                                                    JSONObject dayHrs = day.getJSONObject(d);
                                                    WeekDays weekDays1 = new WeekDays();
                                                    //--------following condition is newly added on 6th May 2019, starts...--------------
                                                    /*if(d==0){
                                                        userSingletonModel.setPeriodStartDate(dayHrs.getString("DayDate"));
                                                    }*/
                                                    //--------above condition is newly added on 6th May 2019, ends...--------------

                                                    if(dayHrs.getString("ActiveYN").contentEquals("true")) { //---condition added on 1st may 2019

                                                        weekDays1.setDayName(dayHrs.getString("DayName"));
                                                        weekDays1.setHours(dayHrs.getString("Hours"));
                                                        weekDays1.setActiveYN(dayHrs.getString("ActiveYN"));
                                                        weekDays1.setDayDate(dayHrs.getString("DayDate"));
                                                        weekDays1.setColorCode(colorcode);
                                                        weekDaysList.add(weekDays1);
                                                        weekDaysArrayList.add(weekDays1);
                                                        listDataChild.put(arrayListTimesheetSelectDayModelsWeekDay.get(j),weekDaysArrayList);
                                                    }

                                                }
                                                datePeriod.clear();
                                                for(int z=0;z<weekDaysList.size();z++){
//                                                    userSingletonModel.setPeriodStartDate(weekDaysList.get(0).getDayDate()); //--commented on 6th may 2019
//                                                    userSingletonModel.setPeriodEndDate(weekDaysList.get(weekDaysList.size()-1).getDayDate()); //---commented on 14th may
                                                    datePeriod.add(weekDaysList.get(z).getDayDate());
                                                }

                                                tv_period_date.setText("("+userSingletonModel.getPeriodStartDate()+" - "+userSingletonModel.getPeriodEndDate()+")");

                                                //-------code to add all the total hours and display at the top of the page, starts...added on 31st may--------
                                                Double sum = 0.0;
                                                for(int k=0;k<arrayListTimesheetSelectDayModelsWeekDay.size();k++){
                                                    sum = sum + Double.parseDouble(arrayListTimesheetSelectDayModelsWeekDay.get(k).getTotalHours());
//                                                    Log.d("totalhrs",arrayListTimesheetSelectDayModelsWeekDay.get())
                                                }
                                                tv_period_totalhrs.setText(sum.toString());
                                                //-------code to add all the total hours and display at the top of the page, ends...added on 31st may--------
                                                ListView listView = (ListView)findViewById(R.id.lv_color);
                                                listView.setAdapter(new displayStatusAdapter());
                                                listView.setDivider(null);
                                                //---temporary commenting 9th may
                                                ExpandableListView explistviewData = (ExpandableListView)findViewById(R.id.lvExp);
                                                ExpandableListAdapter explistAdapter;
                                                explistAdapter = new ExpandableListAdapter(TimesheetSelectDay.this, arrayListTimesheetSelectDayModelsWeekDay,listDataChild);
                                                explistviewData.setAdapter(explistAdapter);
                                                //-----code to open child list starts----------
                                                for (int k = 0; k < explistviewData.getExpandableListAdapter().getGroupCount(); k++) {
                                                    //Expand group
                                                    explistviewData.expandGroup(k);
                                                }
                                                //-----code to open child list ends----------
                                            }


                                        }
                                        loading.dismiss();
                                    }else{
                                        Toast.makeText(getApplicationContext(),jsonObject.getString("message"),Toast.LENGTH_LONG).show();
                                    }
                                }
                            }
                        }catch (JSONException e){
                            e.printStackTrace();
                            loading.dismiss();
                            Toast.makeText(getApplicationContext(),"Please try after sometime.Internal error",Toast.LENGTH_LONG).show();
                        }

                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                loading.dismiss();
                Toast.makeText(getApplicationContext(),"Please try after sometime.Internal error",Toast.LENGTH_LONG).show();
            }
        }) {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> params = new HashMap<>();
                params.put("CorpId", userSingletonModel.getCorpID());
                if(userSingletonModel.getTimesheet_personId_yn().contentEquals("0")) {
                    params.put("UserId", userSingletonModel.getUserID());
                }else if(userSingletonModel.getTimesheet_personId_yn().contentEquals("1")){
                    params.put("UserId", userSingletonModel.getPayable_payroll_supervisor_person_id());
                }
                params.put("UserType",userSingletonModel.getUserType());
                params.put("StartDate",TimesheetHome.dateOnSelectedCalender);
                Log.d("StartDate",TimesheetHome.dateOnSelectedCalender);

               /* params.put("CorpId", "gst-inc-101");
                params.put("UserId","1");
                params.put("UserType","MAIN");
                params.put("StartDate","10-07-2018");*/
                return params;
            }
        };

        RequestQueue requestQueue = Volley.newRequestQueue(TimesheetSelectDay.this);
        requestQueue.add(stringRequest);



    }
    //=========== added on 6th may, volley code to get DayWiseTimeSheet using volley code ends============

    //===========added on 8th may starts(not in use)==========
    public class ExampleAdapter extends BaseExpandableListAdapter {

        private Context context;

        public ExampleAdapter() {

            this.context = context;

        }
        @Override
        public int getGroupCount() {
//            return arrayListTimesheetSelectDayModelsWeekDay.size();
            return listTimesheetSelectDayModelsWeekDay.size();
        }

        @Override
        public int getChildrenCount(int i) {
//            return weekDaysArrayList.size();
            return weekDaysList.size();
        }

        @Override
        public Object getGroup(int i) {
            return null;
        }

        @Override
        public Object getChild(int i, int i1) {
            return null;
        }

        @Override
        public long getGroupId(int i) {
            return 0;
        }

        @Override
        public long getChildId(int i, int i1) {
            return 0;
        }

        @Override
        public boolean hasStableIds() {
            return false;
        }

        @Override
        public View getGroupView(int i, boolean b, View view, ViewGroup viewGroup) {
            LayoutInflater layoutInflater = getLayoutInflater();
            view = layoutInflater.inflate(R.layout.listview_select_day_row_group,viewGroup,false);
            TextView lblListHeader = view.findViewById(R.id.tv_selected_date);
            lblListHeader.setText(listTimesheetSelectDayModelsWeekDay.get(i).getWeekDate());
            return view;
        }

        @Override
        public View getChildView(int i, int i1, boolean b, View view, ViewGroup viewGroup) {
            LayoutInflater layoutInflater = getLayoutInflater();
            view = layoutInflater.inflate(R.layout.listview_select_day_row,viewGroup,false);
            TextView tv_dayname = (TextView)view.findViewById(R.id.tv_dayname);
            tv_dayname = (TextView)view.findViewById(R.id.tv_dayname);
            tv_dayname.setText(weekDaysList.get(i1).getDayName());
           /* for(i=0;i<arrayListTimesheetSelectDayModelsWeekDay.size();i++){
               tv_dayname = (TextView)view.findViewById(R.id.tv_dayname);
               tv_dayname.setText(weekDaysArrayList.get(i1).getDayDate());
            }*/
            return view;
        }

        @Override
        public boolean isChildSelectable(int i, int i1) {
            return true;
        }
    }
    //===========added on 8th may ends==========

    //=========Custom listview for displaying status color and status description code starts========
    public class displayStatusAdapter extends BaseAdapter{

        @Override
        public int getCount() {
            return arrayListTimesheetSelectDayModelsWeekDay.size();
        }

        @Override
        public Object getItem(int i) {
            return 0;
        }

        @Override
        public long getItemId(int i) {
            return 0;
        }

        @Override
        public View getView(int i, View view, ViewGroup viewGroup) {
            LayoutInflater layoutInflater = getLayoutInflater();
            view = layoutInflater.inflate(R.layout.listview_color_details_row,viewGroup,false);
            ImageView imageView = (ImageView)view.findViewById(R.id.img_color);
            TextView tv_color_desc = (TextView)view.findViewById(R.id.tv_color_desc);
//            imageView.setBackground(getDrawable(R.drawable.imageview_border));
            imageView.setBackgroundColor(Color.parseColor(arrayListTimesheetSelectDayModelsWeekDay.get(i).getColorCode()));
            tv_color_desc.setText(arrayListTimesheetSelectDayModelsWeekDay.get(i).getStatusDescription());
            userSingletonModel.setStatusDescription(arrayListTimesheetSelectDayModelsWeekDay.get(i).getStatusDescription()); //-----newly added 30th nov as it is required for checking editable/non editable mode in the next page

            return view;
        }
    }
    //==========Custom listview for displaying status color and status description code ends========


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
//            loadDataOfDayWiseTimeSheet();  //-----commented on 6th may
              loadDataOfDayWiseTimeSheetNew();
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
//        loadDataOfDayWiseTimeSheet();  //---commented on 6th may
        loadDataOfDayWiseTimeSheetNew();
    }

    /**
     * Callback will be triggered when there is change in
     * network connection
     */
    @Override
    public void onNetworkConnectionChanged(boolean isConnected) {
        showSnack(isConnected);
    }
    //=============Internet checking code ends (added 22nd Nov)=============
}
