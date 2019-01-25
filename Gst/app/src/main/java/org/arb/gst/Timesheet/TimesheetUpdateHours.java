package org.arb.gst.Timesheet;

import android.app.ProgressDialog;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import org.arb.gst.Model.UserSingletonModel;
import org.arb.gst.Model.UserUpdateHoursModel;
import org.arb.gst.R;
import org.arb.gst.config.Config;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.json.XML;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

import fr.ganfra.materialspinner.MaterialSpinner;

public class TimesheetUpdateHours extends AppCompatActivity {
    UserSingletonModel userSingletonModel = UserSingletonModel.getInstance();
    ArrayList<UserUpdateHoursModel> contractArrayList = new ArrayList<>();
    ArrayList<String> stringContractArrayList = new ArrayList<>();
    ArrayList<UserUpdateHoursModel> taskArrayList = new ArrayList<>();
    ArrayList<String> stringtaskArrayList = new ArrayList<>();
    MaterialSpinner materialSpinnerContract,materialSpinnerSelectTask,materialSpinnerSelectLabourType;
    public static String contractID;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_timesheet_updatehours);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        //-----Code to use back button at the toolbar starts-----
        if (getSupportActionBar() != null) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        }
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                onBackPressed();
            }
        });
        //-----Code to use back button at the toolbar ends-----
        Toast.makeText(getApplicationContext(),TimesheetHome.dateOnSelectedCalender,Toast.LENGTH_LONG).show();

        getContractListData();  //----method to get contractlistdata
    }

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
                                JSONArray jsonArray = jsonObject.getJSONArray("Contracts");
                                for (int i = 0; i < jsonArray.length(); i++) {
                                    JSONObject jobject = jsonArray.getJSONObject(i);
                                    UserUpdateHoursModel userUpdateHoursModel = new UserUpdateHoursModel();
                                    userUpdateHoursModel.setContractID(jobject.getString("ContractID"));
                                    userUpdateHoursModel.setContractCode(jobject.getString("ContractCode"));
                                    userUpdateHoursModel.setContractDescription(jobject.getString("ContractDescription"));
                                    userUpdateHoursModel.setContractFlag(jobject.getString("ContractFlag"));
                                    contractArrayList.add(userUpdateHoursModel);
                                    stringContractArrayList.add(jobject.getString("ContractDescription"));
                                }
                                materialSpinnerContract = (MaterialSpinner)findViewById(R.id.spinner);
                                ArrayAdapter<String> arrayAdapter = new ArrayAdapter<String>(TimesheetUpdateHours.this,android.R.layout.simple_spinner_dropdown_item, stringContractArrayList);
                                arrayAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                                materialSpinnerContract.setAdapter(arrayAdapter);
                                materialSpinnerContract.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                                  @Override
                                  public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                                      if(i!= -1) {
                                          Toast.makeText(getApplicationContext(),contractArrayList.get(i).getContractID().toString(),Toast.LENGTH_LONG).show();
                                          contractID = contractArrayList.get(i).getContractID();
                                          getTaskListdata();
                                          getLabourCategoryListData();
                                      }
                                  }
                                  @Override
                                  public void onNothingSelected(AdapterView<?> adapterView) {
                                  }
                              });
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
                params.put("DayDate",TimesheetHome.dateOnSelectedCalender);
                params.put("CorpId", userSingletonModel.getCorpID());
                params.put("deviceType","1");
                return params;
            }
        };

        RequestQueue requestQueue = Volley.newRequestQueue(TimesheetUpdateHours.this);
        requestQueue.add(stringRequest);


    }
    //===============to get the contractList data from server using volley code ends=============

    //==============to get the taskdetails data from server using volley code starts==========
    public void getTaskListdata(){
        String url = Config.BaseUrl+"TaskList";
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
                                JSONArray jsonArray = jsonObject.getJSONArray("TaskList");
                                for (int i = 0; i < jsonArray.length(); i++) {
                                    JSONObject jobject = jsonArray.getJSONObject(i);
                                    UserUpdateHoursModel userUpdateHoursModel = new UserUpdateHoursModel();
                                    userUpdateHoursModel.setTaskID(jobject.getString("TaskID"));
                                    userUpdateHoursModel.setTaskCode(jobject.getString("TaskCode"));
                                    userUpdateHoursModel.setTaskDescription(jobject.getString("TaskDescription"));
                                    taskArrayList.add(userUpdateHoursModel);
                                    stringtaskArrayList.add(jobject.getString("TaskDescription"));
                                }
                                materialSpinnerSelectTask = (MaterialSpinner)findViewById(R.id.spinner_select_task);
                                ArrayAdapter<String> arrayAdapter = new ArrayAdapter<String>(TimesheetUpdateHours.this,android.R.layout.simple_spinner_dropdown_item, stringtaskArrayList);
                                arrayAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                                materialSpinnerSelectTask.setAdapter(arrayAdapter);
                                materialSpinnerSelectTask.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                                    @Override
                                    public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                                        if(i!= -1) {
                                            Toast.makeText(getApplicationContext(),taskArrayList.get(i).getTaskID(),Toast.LENGTH_LONG).show();
                                        }
                                    }
                                    @Override
                                    public void onNothingSelected(AdapterView<?> adapterView) {
                                    }
                                });
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
                params.put("ContractID", contractID);
                params.put("CorpId", userSingletonModel.getCorpID());
                params.put("UserId",userSingletonModel.getUserID());
                params.put("DayDate",TimesheetHome.dateOnSelectedCalender);
                return params;
            }
        };

        RequestQueue requestQueue = Volley.newRequestQueue(TimesheetUpdateHours.this);
        requestQueue.add(stringRequest);

    }
    //==============to get the taskdetails data from server using volley code ends==========

    //==============to get the LabourCategoryList data from server using volley code starts======
    public void getLabourCategoryListData(){
        String url = Config.BaseUrl+"LabourCategoryList";
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
                                JSONArray jsonArray = jsonObject.getJSONArray("LabourCategoryList");
                                for (int i = 0; i < jsonArray.length(); i++) {
                                    JSONObject jobject = jsonArray.getJSONObject(i);
                                    UserUpdateHoursModel userUpdateHoursModel = new UserUpdateHoursModel();
                                    userUpdateHoursModel.setLaborCatgryID(jobject.getString("LaborCatgryID"));
                                    userUpdateHoursModel.setLaborCatgryCode(jobject.getString("LaborCatgryCode"));
                                    userUpdateHoursModel.setLaborCatgryDescription(jobject.getString("LaborCatgryDescription"));
                                    contractArrayList.add(userUpdateHoursModel);
                                    stringContractArrayList.add(jobject.getString("LaborCatgryDescription"));
                                }
                                materialSpinnerSelectTask = (MaterialSpinner)findViewById(R.id.spinner_select_labour_category);
                                ArrayAdapter<String> arrayAdapter = new ArrayAdapter<String>(TimesheetUpdateHours.this,android.R.layout.simple_spinner_dropdown_item, stringContractArrayList);
                                arrayAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                                materialSpinnerSelectTask.setAdapter(arrayAdapter);
                                materialSpinnerSelectTask.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                                    @Override
                                    public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                                        if(i!= -1) {
//                                            Toast.makeText(getApplicationContext(),contractArrayList.get(i).getTaskID(),Toast.LENGTH_LONG).show();
                                        }
                                    }
                                    @Override
                                    public void onNothingSelected(AdapterView<?> adapterView) {
                                    }
                                });
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
                params.put("ContractID", contractID);
                params.put("CorpId", userSingletonModel.getCorpID());
                params.put("UserId",userSingletonModel.getUserID());
                params.put("DayDate",TimesheetHome.dateOnSelectedCalender);
                return params;
            }
        };

        RequestQueue requestQueue = Volley.newRequestQueue(TimesheetUpdateHours.this);
        requestQueue.add(stringRequest);

    }
    //==============to get the LabourCategoryList data from server using volley code ends========
}
