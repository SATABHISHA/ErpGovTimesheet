package org.arb.gst.fragment;

import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.os.StrictMode;
import android.support.annotation.Nullable;
import android.support.design.widget.CoordinatorLayout;
import android.support.v4.app.Fragment;
import android.support.v7.app.AlertDialog;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RelativeLayout;
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

import org.arb.gst.Model.SupervisorListModel;
import org.arb.gst.Model.UserSingletonModel;
import org.arb.gst.R;
import org.arb.gst.Timesheet.PayrollPayableClerk;
import org.arb.gst.Timesheet.TimesheetHome;
import org.arb.gst.Timesheet.TimesheetSelectDay;
import org.arb.gst.config.Config;
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

public class SubordinateListFragment extends Fragment {
    UserSingletonModel userSingletonModel = UserSingletonModel.getInstance();
    View rootView;
    ListView lv_subordinatelist;
    ArrayList<SupervisorListModel> arrayList = new ArrayList<>();
    CoordinatorLayout coordinator_layout_subordinate;

    //-------------variables for email, starts----------
    final String username = "gsttest123@gmail.com";
    final String password = "ARB@1234";
    // -------------variables for email, ends----------

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {
        rootView =  inflater.inflate(R.layout.fragment_subordinate_list, null);
        lv_subordinatelist = (ListView)rootView.findViewById(R.id.lv_subordinatelist);
        coordinator_layout_subordinate = (CoordinatorLayout)rootView.findViewById(R.id.coordinator_layout_subordinate);

        loadData();
        return rootView;
    }

    @Override
    public void onResume() {
        loadData();
        super.onResume();
    }

    public void loadData(){
        String url = Config.BaseUrl+"SubordinateEmployeeList";
        final ProgressDialog loading = ProgressDialog.show(getActivity(), "Loading", "Please wait...", true, false);
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
//                                    Toast.makeText(getApplicationContext(),xx.getString("content"),Toast.LENGTH_LONG).show();
                                    Log.d("getEmpData1", xx.getString("content"));
                                    JSONObject jsonObject = new JSONObject(val);
                                    loading.dismiss();
                                    JSONObject jsonObject1 = new JSONObject(jsonObject.getString("subordinates"));
                                    Log.d("getEmpData2", jsonObject1.toString());
                                    JSONArray jsonArray = jsonObject1.getJSONArray("employees_main");
                                    for(int i=0;i<jsonArray.length();i++){
                                        JSONObject jsonObject2 = jsonArray.getJSONObject(i);
                                        SupervisorListModel supervisorListModel = new SupervisorListModel();
                                        if(jsonObject2.getInt("ts_status_id")==0 && userSingletonModel.getSupervisor_notstarted_yn().contentEquals("1")) {
                                            setJsonData(supervisorListModel, jsonObject2);
                                        }else if(jsonObject2.getInt("ts_status_id")==1 && userSingletonModel.getSupervisor_saved_yn().contentEquals("1")) {
                                            setJsonData(supervisorListModel, jsonObject2);
                                        }else if(jsonObject2.getInt("ts_status_id")==2 && userSingletonModel.getSupervisor_submitted_yn().contentEquals("1")) {
                                            setJsonData(supervisorListModel, jsonObject2);
                                        }else if(jsonObject2.getInt("ts_status_id")==3 && userSingletonModel.getSupervisor_returned_yn().contentEquals("1")) {
                                            setJsonData(supervisorListModel, jsonObject2);
                                        }else if(jsonObject2.getInt("ts_status_id")==4 && userSingletonModel.getSupervisor_approved_yn().contentEquals("1")) {
                                            setJsonData(supervisorListModel, jsonObject2);
                                        }else if(jsonObject2.getInt("ts_status_id")==5 && userSingletonModel.getSupervisor_posted_yn().contentEquals("1")) {
                                            setJsonData(supervisorListModel, jsonObject2);
                                        }else if(jsonObject2.getInt("ts_status_id")==6 && userSingletonModel.getSupervisor_partially_returned_yn().contentEquals("1")) {
                                            setJsonData(supervisorListModel, jsonObject2);
                                        }else if(jsonObject2.getInt("ts_status_id")==7 && userSingletonModel.getSupervisor_partially_approved_yn().contentEquals("1")) {
                                            setJsonData(supervisorListModel, jsonObject2);
                                        }
//                                        supervisorListModel.setSupervisor_department(jsonObj.getString("department"));
//                                        arrayList.add(supervisorListModel);
                                    }
                                    lv_subordinatelist.setAdapter(new displaySubordinateList());
                                    lv_subordinatelist.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                                        @Override
                                        public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                                            userSingletonModel.setSupervisor_id_person(arrayList.get(i).getId_person()); //--not in use
                                            userSingletonModel.setSupervisor_employee_name(arrayList.get(i).getEmployee_name());

                                            userSingletonModel.setTimesheet_personId_yn("1");
                                            userSingletonModel.setPayable_payroll_supervisor_person_id(arrayList.get(i).getId_person());
                                            if(arrayList.get(i).getSupervisor_status().contentEquals("Not Started")){
                                                //-------------added email notification for "Not Started" public, starts-------------
                                                final String recipientName = arrayList.get(i).getEmployee_name();
                                                final String recipientEmailid = arrayList.get(i).getSupervisor_email_id();
//                                                final String recipientEmailid = "satabhishar@arbsoft.com";  //for testing
                                                final String recipientPeriodDate = TimesheetHome.period_date;
                                                final String orgName = userSingletonModel.getCompanyName();
                                                AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
                                                builder.setMessage("Want to send Email Notification?")
                                                        .setCancelable(false)
                                                        .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                                                            public void onClick(DialogInterface dialog, int id) {
                                                                int SDK_INT = android.os.Build.VERSION.SDK_INT;
                                                                if (SDK_INT > 8)
                                                                {
                                                                    StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder()
                                                                            .permitAll().build();
                                                                    StrictMode.setThreadPolicy(policy);
                                                                    //your codes here

                                                                    if(recipientEmailid.trim().contentEquals("")){
                                                                        Toast.makeText(getActivity(),"Email id not registered",Toast.LENGTH_LONG).show();
                                                                    }else {
                                                                        dialog.cancel();
                                                                        sendEmail(recipientName,recipientEmailid,recipientPeriodDate,orgName);
                                                                    }

                                                                }
                                                            }
                                                        })
                                                        .setNegativeButton("No", new DialogInterface.OnClickListener() {
                                                            public void onClick(DialogInterface dialog, int id) {
                                                                dialog.cancel();
                                                            }
                                                        });
                                                AlertDialog alert = builder.create();
                                                alert.show();
                                                //-------------added email notification for "Not Started" public, ends-------------
                                            }else {
                                                userSingletonModel.setAll_employee_type("MAIN");
                                                startActivity(new Intent(getActivity(), TimesheetSelectDay.class));
//                                            Toast.makeText(getContext(),arrayList.get(i).getId_person()+"/"+arrayList.get(i).getEmployee_name(),Toast.LENGTH_SHORT).show();
                                            }
                                        }
                                    });
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
                loading.dismiss();
            }
        }) {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> params = new HashMap<>();
                params.put("CorpId", userSingletonModel.getCorpID());
                params.put("UserId", userSingletonModel.getUserID());
                params.put("WeekDate",TimesheetHome.period_date);
                params.put("WeekStartDate",TimesheetHome.period_start_date);
                params.put("WeekEndDate",TimesheetHome.period_end_date);
                params.put("DeviceType","1");
                return params;
            }
        };

        stringRequest.setRetryPolicy(new DefaultRetryPolicy(10000,
                DefaultRetryPolicy.DEFAULT_MAX_RETRIES, DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
        RequestQueue requestQueue = Volley.newRequestQueue(getActivity());
        requestQueue.add(stringRequest);

    }

    public class displaySubordinateList extends BaseAdapter{

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
            TextView tv_subordinate_name, tv_subordinate_hrs, tv_subordinate_status;
            RelativeLayout relative_layout;
            LinearLayout linear_layout;
            LayoutInflater layoutInflater = LayoutInflater.from(getContext());
            view = layoutInflater.inflate(R.layout.custom_row_subordinatelist, viewGroup, false);
            tv_subordinate_name=(TextView)view.findViewById(R.id.tv_subordinate_name);
            tv_subordinate_hrs=(TextView)view.findViewById(R.id.tv_subordinate_hrs);
            tv_subordinate_status=(TextView)view.findViewById(R.id.tv_subordinate_status);
            //----------following code is to split----------
            int firstSpace = arrayList.get(i).getEmployee_name().indexOf(" "); // detect the first space character
            if(firstSpace>0) {
                String name_part1 = arrayList.get(i).getEmployee_name().substring(0, firstSpace);  // get everything upto the first space character
                String name_part2 = arrayList.get(i).getEmployee_name().substring(firstSpace).trim(); // get everything after the first space, trimming the spaces off
                tv_subordinate_name.setText(name_part1 + "\n" + name_part2);
            }else{
                tv_subordinate_name.setText(arrayList.get(i).getEmployee_name());
            }
            //-------------------code to split ends----------------------------------
            Double value = 0.0;
            value = Double.parseDouble(arrayList.get(i).getTotal_hours()); //----code to make the hours in 0.0 format
            tv_subordinate_hrs.setText(value.toString());

            tv_subordinate_status.setText(arrayList.get(i).getSupervisor_status());
            relative_layout = view.findViewById(R.id.relative_layout);
            linear_layout = view.findViewById(R.id.linear_layout);
            relative_layout.setBackgroundColor(Color.parseColor(arrayList.get(i).getSupervisor_color_code()));
            linear_layout.setBackgroundColor(Color.parseColor(arrayList.get(i).getSupervisor_color_code()));
            return view;
        }
    }

    //----------created function and have called the the function inside for loop at json parsing for filteration of data, code starts------------
    public void setJsonData(SupervisorListModel supervisorListModel,JSONObject jsonObject2){
        try {
            supervisorListModel.setId_person(jsonObject2.getString("id_person"));
            supervisorListModel.setEmployee_name(jsonObject2.getString("employee_name"));
            supervisorListModel.setTotal_hours(jsonObject2.getString("total_hours"));
            supervisorListModel.setSupervisor_email_id(jsonObject2.getString("email_id"));
            if(jsonObject2.getInt("ts_status_id")==0){
                supervisorListModel.setSupervisor_status("Not Started");
                supervisorListModel.setSupervisor_color_code(userSingletonModel.getNot_started_color());
            }else if(jsonObject2.getInt("ts_status_id")==1){
                supervisorListModel.setSupervisor_status("Saved");
                supervisorListModel.setSupervisor_color_code(userSingletonModel.getSaved_color());
            }else if(jsonObject2.getInt("ts_status_id")==2){
                supervisorListModel.setSupervisor_status("Submitted");
                supervisorListModel.setSupervisor_color_code(userSingletonModel.getSubmitted_color());
            }else if(jsonObject2.getInt("ts_status_id")==3){
                supervisorListModel.setSupervisor_status("Returned");
                supervisorListModel.setSupervisor_color_code(userSingletonModel.getReturned_color());
            }else if(jsonObject2.getInt("ts_status_id")==4){
                supervisorListModel.setSupervisor_status("Approved");
                supervisorListModel.setSupervisor_color_code(userSingletonModel.getApproved_color());
            }else if(jsonObject2.getInt("ts_status_id")==5){
                supervisorListModel.setSupervisor_status("Posted");
                supervisorListModel.setSupervisor_color_code(userSingletonModel.getPosted_color());
            }else if(jsonObject2.getInt("ts_status_id")== 6 ){
                supervisorListModel.setSupervisor_status("Partially Returned");
                supervisorListModel.setSupervisor_color_code(userSingletonModel.getPartially_returned_color());
            }else if(jsonObject2.getInt("ts_status_id")==7){
                supervisorListModel.setSupervisor_status("Partially Approved");
                supervisorListModel.setSupervisor_color_code(userSingletonModel.getPartially_approved_color());
            }
            arrayList.add(supervisorListModel);
        } catch (JSONException e) {
            e.printStackTrace();
        }

    }
    //----------created function and have called the the function inside for loop at json parsing for filteration of data, code ends------------

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
             Toast.makeText(getActivity(),"Notification has been sent successfully",Toast.LENGTH_LONG).show();

         } catch (MessagingException e) {
             throw new RuntimeException(e);
         }
     }
    //-------------email code ends----------------

}
