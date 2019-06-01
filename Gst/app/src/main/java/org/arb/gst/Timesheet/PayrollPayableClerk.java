package org.arb.gst.Timesheet;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.os.StrictMode;
import android.support.annotation.Nullable;
import android.support.design.widget.Snackbar;
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

import org.arb.gst.Home.HomeActivity;
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
import java.util.Properties;

import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.PasswordAuthentication;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;

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

    //-------------variables for email, starts----------
    final String username = "gsttest123@gmail.com";
    final String password = "ARB@1234";
    // -------------variables for email, ends----------


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
                                    Log.d("getEmpdata",val.toString());
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
                                        payrollPayableModel.setPayroll_payable_email_id(jsonObject.getString("email_id"));
//                                        payrollPayableModel.setTimesheet_status_id(jsonObject.getString("timesheet_status_id")); //---since it is in integer format
                                        if(jsonObject.getInt("timesheet_status_id")==0){
                                            payrollPayableModel.setPayroll_payable_clerk_status("Not Started");
                                            payrollPayableModel.setPayaroll_payableclerk_colorcode(userSingletonModel.getNot_started_color());
                                        }else if(jsonObject.getInt("timesheet_status_id")==1){
                                            payrollPayableModel.setPayroll_payable_clerk_status("Saved");
                                            payrollPayableModel.setPayaroll_payableclerk_colorcode(userSingletonModel.getSaved_color());
                                        }else if(jsonObject.getInt("timesheet_status_id")==2){
                                            payrollPayableModel.setPayroll_payable_clerk_status("Submitted");
                                            payrollPayableModel.setPayaroll_payableclerk_colorcode(userSingletonModel.getSubmitted_color());
                                        }else if(jsonObject.getInt("timesheet_status_id")==3){
                                            payrollPayableModel.setPayroll_payable_clerk_status("Returned");
                                            payrollPayableModel.setPayaroll_payableclerk_colorcode(userSingletonModel.getReturned_color());
                                        }else if(jsonObject.getInt("timesheet_status_id")==4){
                                            payrollPayableModel.setPayroll_payable_clerk_status("Approved");
                                            payrollPayableModel.setPayaroll_payableclerk_colorcode(userSingletonModel.getApproved_color());
                                        }else if(jsonObject.getInt("timesheet_status_id")==5){
                                            payrollPayableModel.setPayroll_payable_clerk_status("Posted");
                                            payrollPayableModel.setPayaroll_payableclerk_colorcode(userSingletonModel.getPosted_color());
                                        }else if(jsonObject.getInt("timesheet_status_id")== 6 ){
                                            payrollPayableModel.setPayroll_payable_clerk_status("Partially Returned");
                                            payrollPayableModel.setPayaroll_payableclerk_colorcode(userSingletonModel.getPartially_returned_color());
                                        }else if(jsonObject.getInt("timesheet_status_id")==7){
                                            payrollPayableModel.setPayroll_payable_clerk_status("Partially Approved");
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

                                            if(arrayList.get(i).getPayroll_payable_clerk_status().contentEquals("Not Started")){
                                               /* String message = "Not Started Timesheet cannot be viewed";
                                                int color = Color.parseColor("#FF4242");
                                                Snackbar snackbar = Snackbar.make(findViewById(R.id.coordinator_layout_payrollpayable), message, 4000);

                                                View sbView = snackbar.getView();
                                                TextView textView = (TextView) sbView.findViewById(android.support.design.R.id.snackbar_text);
                                                textView.setTextColor(color);
                                                snackbar.show();*/

                                                //-----------added email section on 1st june, starts----
                                                String message = "Want to send notification?";
                                                final String recipientName = arrayList.get(i).getEmployee_name();
//                                                final String recipientEmailid = arrayList.get(i).getPayroll_payable_email_id();
                                                final String recipientEmailid = "satabhishar@arbsoft.com"; //for testing
                                                final String recipientPeriodDate = TimesheetHome.period_date;
                                                final String orgName = userSingletonModel.getCompanyName();
                                                final Snackbar snackbar = Snackbar.make(findViewById(R.id.coordinator_layout_payrollpayable), message, Snackbar.LENGTH_LONG);

                                                snackbar.setAction("Yes", new View.OnClickListener() {
                                                    @Override
                                                    public void onClick(View v) {
                                                        int SDK_INT = android.os.Build.VERSION.SDK_INT;
                                                        if (SDK_INT > 8)
                                                        {
                                                            StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder()
                                                                    .permitAll().build();
                                                            StrictMode.setThreadPolicy(policy);
                                                            //your codes here

                                                            if(recipientEmailid.contentEquals("")){
                                                                Toast.makeText(getApplicationContext(),"Email id not registered",Toast.LENGTH_LONG).show();
                                                                snackbar.dismiss();
                                                            }else {
                                                                sendEmail(recipientName,recipientEmailid,recipientPeriodDate,orgName);
                                                                snackbar.dismiss();
                                                            }

                                                        }
                                                    }
                                                });

                                                snackbar.show();

                                                //-----------added email section on 1st june, ends----
                                            }else {
                                                if(HomeActivity.payrollclerk_yn_temp.contentEquals("1")){
                                                    userSingletonModel.setAll_employee_type("MAIN");
                                                }else if(HomeActivity.payableclerk_yn_temp.contentEquals("1")){
                                                    userSingletonModel.setAll_employee_type("SUB");
                                                }
                                                startActivity(new Intent(PayrollPayableClerk.this,TimesheetSelectDay.class));
                                            }


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
            TextView tv_payroll_payable_clerk, tv_payroll_payable_clerk_hrs,tv_payroll_payable_clerk_status;
            LayoutInflater layoutInflater = getLayoutInflater();
            view = layoutInflater.inflate(R.layout.listview_payroll_payableclerk_row,viewGroup,false);
            tv_payroll_payable_clerk = (TextView) view.findViewById(R.id.tv_payroll_payable_clerk);
            tv_payroll_payable_clerk_hrs = (TextView) view.findViewById(R.id.tv_payroll_payable_clerk_hrs);
            tv_payroll_payable_clerk_status = (TextView) view.findViewById(R.id.tv_payroll_payable_clerk_status);
            relative_layout = view.findViewById(R.id.relative_layout);
//            tv_payroll_payable_clerk.setText(arrayList.get(i).getEmployee_name());

            //----------following code is to split----------
            int firstSpace = arrayList.get(i).getEmployee_name().indexOf(" "); // detect the first space character
            if(firstSpace>0) {
                String name_part1 = arrayList.get(i).getEmployee_name().substring(0, firstSpace);  // get everything upto the first space character
                String name_part2 = arrayList.get(i).getEmployee_name().substring(firstSpace).trim(); // get everything after the first space, trimming the spaces off
                tv_payroll_payable_clerk.setText(name_part1 + "\n" + name_part2);
            }else{
                tv_payroll_payable_clerk.setText(arrayList.get(i).getEmployee_name());
            }
            //-------------------code to split ends----------------------------------

            Double value = 0.0;
            value = Double.parseDouble(arrayList.get(i).getTotal_hours()); //----code to make the hours in 0.0 format
            tv_payroll_payable_clerk_hrs.setText(value.toString());

            tv_payroll_payable_clerk_status.setText(arrayList.get(i).getPayroll_payable_clerk_status());

            relative_layout.setBackgroundColor(Color.parseColor(arrayList.get(i).getPayaroll_payableclerk_colorcode()));
//            Log.d("colorcode",arrayList.get(i).getPayaroll_payableclerk_colorcode().toString());
            return view;
        }
    }
    //==============BaseAdapter code for listview ends===========

    //-------------email code starts----------------
    public void sendEmail(String recipientName,String recipientEmailid, String recipientPeriodDate, String orgName){
        Properties props = new Properties();
        props.put("mail.smtp.auth", "true");
        props.put("mail.smtp.starttls.enable", "true");
        props.put("mail.smtp.host", Config.host);
        props.put("mail.smtp.port", Config.port);

        Session session = Session.getInstance(props,
                new javax.mail.Authenticator() {
                    protected PasswordAuthentication getPasswordAuthentication() {
                        return new PasswordAuthentication(Config.email_username, Config.email_password);
                    }
                });
        try {
            Message message = new MimeMessage(session);
            message.setFrom(new InternetAddress("gsttest123@gmail.com"));
            message.setRecipients(Message.RecipientType.TO,
                    InternetAddress.parse(recipientEmailid));
            message.setSubject("Fill-up and Submit Timesheet - "+recipientPeriodDate);
            message.setText(
                    "Hello "+recipientName+"," +
                            "\n\n" +
                            "Please fill up and submit your timesheet for the period of "+ recipientPeriodDate+"." +
                            "\n\n" +
                            "Thanks." +
                            "\n\n" +
                            "Admin" +
                            "\n" +
                            orgName);

            /* MimeBodyPart messageBodyPart = new MimeBodyPart();

             Multipart multipart = new MimeMultipart();

             messageBodyPart = new MimeBodyPart();
             String file = "path of file to be attached";
             String fileName = "attachmentName";
             DataSource source = new FileDataSource(file);
             messageBodyPart.setDataHandler(new DataHandler(source));
             messageBodyPart.setFileName(fileName);
             multipart.addBodyPart(messageBodyPart);

             message.setContent(multipart);*/

            Transport.send(message);

            System.out.println("Done");
            Toast.makeText(getApplicationContext(),"Notification has been sent successfully",Toast.LENGTH_LONG).show();

        } catch (MessagingException e) {
            throw new RuntimeException(e);
        }
    }
    //-------------email code ends----------------

    public void onBackPressed() {
        super.onBackPressed();
    }
}
