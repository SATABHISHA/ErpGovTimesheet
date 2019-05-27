package org.arb.gst.Timesheet;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.FragmentManager;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import org.arb.gst.Model.PayrollPayableModel;
import org.arb.gst.Model.UserSingletonModel;
import org.arb.gst.R;
import org.arb.gst.config.Config;
import org.arb.gst.fragment.DrawerPayrollPayableClerkFragment;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.json.XML;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

public class PayrollPayableClerk extends AppCompatActivity {
    TextView tv_payrollclerk_period_date;
    ImageButton imgbtn_filter;
    DrawerLayout drawer_layout;
    FragmentManager fragmentManager = getSupportFragmentManager();
    DrawerPayrollPayableClerkFragment drawerPayrollPayableClerkFragment;
    ArrayList<PayrollPayableModel> arrayList = new ArrayList<>();
    UserSingletonModel userSingletonModel = UserSingletonModel.getInstance();
    ListView lv_payrollclerk;
    RelativeLayout relative_layout;


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_payroll_payable_clerk);
        imgbtn_filter = (ImageButton)findViewById(R.id.imgbtn_filter);
        drawer_layout = (DrawerLayout)findViewById(R.id.drawer_layout);
        lv_payrollclerk = (ListView)findViewById(R.id.lv_payrollclerk);
        drawerPayrollPayableClerkFragment = (DrawerPayrollPayableClerkFragment)fragmentManager.findFragmentById(R.id.fragmentitem);

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

        loadData();
    }


    //----------load data using volley code starts----------
    public void loadData(){
        String url = Config.BaseUrl+"PayrollPayableOperatorEmployeeList";
//        final ProgressDialog loading = ProgressDialog.show(getApplicationContext(), "Loading", "Please wait...", true, false);
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
                            Log.d("getEmpData",responseData.toString());

                            Iterator<?> keys = resobj.keys();
                            while(keys.hasNext() ) {
                                String key = (String) keys.next();
                                if (resobj.get(key) instanceof JSONObject) {
                                    JSONObject xx = new JSONObject(resobj.get(key).toString());
                                    val = xx.getString("content");
                                    JSONArray jsonArray = new JSONArray(val);
                                    for(int i=0;i<jsonArray.length();i++){
                                        JSONObject jsonObject = jsonArray.getJSONObject(i);
                                        PayrollPayableModel payrollPayableModel = new PayrollPayableModel();
                                        payrollPayableModel.setId_person(jsonObject.getString("id_person"));
                                        payrollPayableModel.setEmployee_code(jsonObject.getString("employee_code"));
                                        payrollPayableModel.setEmployee_name(jsonObject.getString("employee_name"));
                                        payrollPayableModel.setEmp_type(jsonObject.getString("emp_type"));
                                        payrollPayableModel.setSupervisor_name(jsonObject.getString("supervisor_name"));
                                        payrollPayableModel.setTotal_hours(jsonObject.getString("total_hours"));
//                                        payrollPayableModel.setTimesheet_status_id(jsonObject.getString("timesheet_status_id")); //---since it is in integer format
                                        if(jsonObject.getInt("timesheet_status_id")==0){
                                            payrollPayableModel.setPayaroll_payableclerk_colorcode(userSingletonModel.getNot_started_color());
                                        }else if(jsonObject.getInt("timesheet_status_id")==1){
                                            payrollPayableModel.setPayaroll_payableclerk_colorcode(userSingletonModel.getSaved_color());
                                        }else if(jsonObject.getInt("timesheet_status_id")==2){
                                            payrollPayableModel.setPayaroll_payableclerk_colorcode(userSingletonModel.getSubmitted_color());
                                        }else if(jsonObject.getInt("timesheet_status_id")==3){
                                            payrollPayableModel.setPayaroll_payableclerk_colorcode(userSingletonModel.getReturned_color());
                                        }else if(jsonObject.getInt("timesheet_status_id")==4){
                                            payrollPayableModel.setPayaroll_payableclerk_colorcode(userSingletonModel.getApproved_color());
                                        }else if(jsonObject.getInt("timesheet_status_id")==5){
                                            payrollPayableModel.setPayaroll_payableclerk_colorcode(userSingletonModel.getPosted_color());
                                        }else if(jsonObject.getInt("timesheet_status_id")== 6 ){
                                            payrollPayableModel.setPayaroll_payableclerk_colorcode(userSingletonModel.getPartially_returned_color());
                                        }else if(jsonObject.getInt("timesheet_status_id")==7){
                                            payrollPayableModel.setPayaroll_payableclerk_colorcode(userSingletonModel.getPartially_approved_color());
                                        }

                                        payrollPayableModel.setTimesheet_status_desc(jsonObject.getString("timesheet_status_desc"));
                                        payrollPayableModel.setEmail_id(jsonObject.getString("email_id"));
                                        payrollPayableModel.setEmail_notice(jsonObject.getString("email_notice"));
                                        payrollPayableModel.setItem_type(jsonObject.getString("item_type"));
                                        arrayList.add(payrollPayableModel);
                                    }
//
                                    lv_payrollclerk.setAdapter(new displayPayrollClerk());
                                    lv_payrollclerk.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                                        @Override
                                        public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                                            userSingletonModel.setTimesheet_personId_yn("1");
                                            userSingletonModel.setPayable_payroll_supervisor_person_id(arrayList.get(i).getId_person());
                                            startActivity(new Intent(PayrollPayableClerk.this,TimesheetSelectDay.class));
                                            Toast.makeText(getApplicationContext(),arrayList.get(i).getId_person(),Toast.LENGTH_SHORT).show();
                                        }
                                    });

                                }
                            }
                        }catch (JSONException e){
//                            loading.dismiss();
                            e.printStackTrace();
                        }
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
//                loading.dismiss();
            }
        }) {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> params = new HashMap<>();
                params.put("CorpID", userSingletonModel.getCorpID());
//                params.put("strClarkType", "PAYROLL");
                params.put("ClarkType", userSingletonModel.getPayroll_payable_type());
//                params.put("strActiveFlag", "Active");
                params.put("ActiveFlag", userSingletonModel.getPayroll_payable_strActiveFlag());
//                params.put("strTimesheetStatusList", PayrollPayableModel.strTimesheetStatusList);
                params.put("TimesheetStatusList", userSingletonModel.getPayroll_payable_strTimesheetStatusList());
                params.put("WeekDate", TimesheetHome.period_date);
                params.put("WeekStartDate", TimesheetHome.period_start_date);
                params.put("WeekEndDate", TimesheetHome.period_end_date);
                params.put("DeviceType","1");
                return params;
            }
        };

        RequestQueue requestQueue = Volley.newRequestQueue(this);
        requestQueue.add(stringRequest);

    }
    //----------load data using volley code ends----------

    //==============BaseAdapter code for listview starts===========

    public class displayPayrollClerk extends BaseAdapter{

        @Override
        public int getCount() {
            return arrayList.size();
        }

        @Override
        public Object getItem(int i) {
            return true;
        }

        @Override
        public long getItemId(int i) {
            return 0;
        }

        @Override
        public View getView(int i, View view, ViewGroup viewGroup) {
            TextView tv_payrollclerk;
            LayoutInflater layoutInflater = getLayoutInflater();
            view = layoutInflater.inflate(R.layout.listview_payrollclerk_row,viewGroup,false);
            tv_payrollclerk = (TextView) view.findViewById(R.id.tv_payrollclerk);
            relative_layout = view.findViewById(R.id.relative_layout);
            tv_payrollclerk.setText(arrayList.get(i).getEmployee_name());
            relative_layout.setBackgroundColor(Color.parseColor(arrayList.get(i).getPayaroll_payableclerk_colorcode()));
//            Log.d("colorcode",arrayList.get(i).getPayaroll_payableclerk_colorcode().toString());
            return view;
        }
    }
    //==============BaseAdapter code for listview ends===========

    public void onBackPressed() {
        super.onBackPressed();
    }
}
