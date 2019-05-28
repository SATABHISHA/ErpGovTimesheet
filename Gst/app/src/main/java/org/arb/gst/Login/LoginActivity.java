package org.arb.gst.Login;

import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
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
import org.arb.gst.Model.UserSingletonModel;
import org.arb.gst.R;
import org.arb.gst.config.ConnectivityReceiver;
import org.arb.gst.config.MyApplication;
import org.json.JSONArray;
import org.json.JSONObject;
import org.json.XML;

import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

/**
 * Created by niverses on 18-10-2018.
 */

public class LoginActivity extends AppCompatActivity implements View.OnClickListener, ConnectivityReceiver.ConnectivityReceiverListener {
    Button btnLogin;
    TextView btnForgotPassword;
    EditText edtCorpId,edtUsername,edtPassword;
    String corpId,username,password;
    private ProgressDialog progressBar;
    CheckBox chkSignedIn;
    Dialog forgotPassword;
    String corporateID,userName,emailID;
    SharedPreferences sharedPreferences;
    UserSingletonModel userSingletonModel = UserSingletonModel.getInstance();
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        checkConnection();  //----function calling to check the internet connection

        sharedPreferences = getApplication().getSharedPreferences("LoginDetails", Context.MODE_PRIVATE);
        btnLogin=(Button)findViewById(R.id.activity_login_btn_login);
        btnForgotPassword=(TextView)findViewById(R.id.activity_login_btn_forgot_password);
        edtCorpId=(EditText)findViewById(R.id.activity_login_edt_corp_ID);
        edtUsername=(EditText)findViewById(R.id.activity_login_edt_username);
        edtPassword=(EditText)findViewById(R.id.activity_login_edt_password);
        chkSignedIn=(CheckBox)findViewById(R.id.activity_login_chk);

        //===========button onClickListner() code starts=======
        btnLogin.setOnClickListener(this);
        btnForgotPassword.setOnClickListener(this);
        edtCorpId.setOnClickListener(this);
        edtUsername.setOnClickListener(this);
        edtPassword.setOnClickListener(this);
        chkSignedIn.setOnClickListener(this);
        //===========button onClickListner() code ends=======

        //======defining inputLayout id for form validation code starts===========

        //======defining inputLayout id for form validation code ends===========

        //=====================code for one time login starts============
        sharedPreferences = getApplication().getSharedPreferences("LoginDetails", Context.MODE_PRIVATE);
        String id = sharedPreferences.getString("UserID","");
        if (id != ""){
            userSingletonModel.setUserID(sharedPreferences.getString("UserID",""));
            userSingletonModel.setUserName(sharedPreferences.getString("UserName",""));
            userSingletonModel.setCompID(sharedPreferences.getString("CompID",""));
            // user.setCompanyID(jobj.getString("CompanyID"));
            userSingletonModel.setCorpID(sharedPreferences.getString("CorpID",""));
            userSingletonModel.setCompanyName(sharedPreferences.getString("CompanyName",""));
            userSingletonModel.setSupervisorId(sharedPreferences.getString("SupervisorId",""));
            userSingletonModel.setUserRole(sharedPreferences.getString("UserRole",""));
            userSingletonModel.setAdminYN(sharedPreferences.getString("AdminYN",""));
            userSingletonModel.setPayableClerkYN(sharedPreferences.getString("PayableClerkYN",""));//---req for home page
            userSingletonModel.setSupervisorYN(sharedPreferences.getString("SupervisorYN","")); //---req for home page
            userSingletonModel.setPurchaseYN(sharedPreferences.getString("PurchaseYN",""));
            userSingletonModel.setPayrollClerkYN(sharedPreferences.getString("PayrollClerkYN","")); //---req for home page
            userSingletonModel.setEmpName(sharedPreferences.getString("EmpName",""));
            userSingletonModel.setUserType(sharedPreferences.getString("UserType",""));
            userSingletonModel.setEmailId(sharedPreferences.getString("EmailId",""));
            userSingletonModel.setPwdSetterId(sharedPreferences.getString("PwdSetterId",""));
            userSingletonModel.setFinYearID(sharedPreferences.getString("FinYearID",""));
            userSingletonModel.setMsg(sharedPreferences.getString("Msg",""));
            Intent intent = new Intent(LoginActivity.this, HomeActivity.class);
            startActivity(intent);
        }
      //===================code for one time login ends=====================
    }


    //=============swicth case for button onClickListner code starts=========
    @Override
    public void onClick(View view) {
        int id = view.getId();
        switch (id){
            case R.id.activity_login_btn_login:

                if(edtCorpId.getText().toString().isEmpty() || edtUsername.getText().toString().isEmpty() || edtPassword.getText().toString().isEmpty()) {
                    String message = "Filed cannot be left empty";
                    int color = Color.parseColor("#FF4242");
                    Snackbar snackbar = Snackbar
                            .make(findViewById(R.id.relativeLayout), message, Snackbar.LENGTH_LONG);

                    View sbView = snackbar.getView();
                    TextView textView = (TextView) sbView.findViewById(android.support.design.R.id.snackbar_text);
                    textView.setTextColor(color);
                    snackbar.show();
                }
                else{
                    login();
                }
                break;
            case R.id.activity_login_btn_forgot_password:
                break;
            case R.id.activity_login_edt_corp_ID:
                break;
            case R.id.activity_login_edt_username:
                break;
            case R.id.activity_login_edt_password:
                break;
            case R.id.activity_login_chk:
                break;
        }
    }
    //=============swicth case for button onClickListner code ends=========


    //---------------volley code for login starts-----------
    public void login(){
        String loginURL ="http://220.225.40.151:9012/TimesheetService.asmx/ValidateTSheetLogin";


        final ProgressDialog loading = ProgressDialog.show(LoginActivity.this, "Authenticating", "Please wait while logging", false, false);
        StringRequest stringRequest = new StringRequest(Request.Method.POST, loginURL,
                new Response.Listener<String>() {
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
                                String key = (String)keys.next();
                                if ( resobj.get(key) instanceof JSONObject ) {
                                    JSONObject xx = new JSONObject(resobj.get(key).toString());
                                    val = xx.getString("content");
                                    Log.d("res1",xx.getString("content"));
                                    JSONObject jsonObject = new JSONObject(val);
                                    String status = jsonObject.getString("status");
                                    if(status.equalsIgnoreCase("true")){
                                        JSONArray jsonArray = jsonObject.getJSONArray("UserLogin");
                                        for(int i=0; i<=jsonArray.length(); i++){
                                            JSONObject jsonObject1 = jsonArray.getJSONObject(i);
                                            String companyName = jsonObject1.getString("CompanyName");
                                            //--------setting the value in sigleton class variables code starts----------
                                            userSingletonModel.setUserID(jsonObject1.getString("UserID"));
                                            userSingletonModel.setUserName(jsonObject1.getString("UserName"));
                                            userSingletonModel.setCompID(jsonObject1.getString("CompID"));
                                            // user.setCompanyID(jobj.getString("CompanyID"));
                                            userSingletonModel.setCorpID(jsonObject1.getString("CorpID"));
                                            userSingletonModel.setCompanyName(jsonObject1.getString("CompanyName"));
                                            userSingletonModel.setSupervisorId(jsonObject1.getString("SupervisorId"));
                                            userSingletonModel.setUserRole(jsonObject1.getString("UserRole"));
                                            userSingletonModel.setAdminYN(jsonObject1.getString("AdminYN"));
                                            userSingletonModel.setPayableClerkYN(jsonObject1.getString("PayableClerkYN"));
                                            userSingletonModel.setSupervisorYN(jsonObject1.getString("SupervisorYN"));
                                            userSingletonModel.setPurchaseYN(jsonObject1.getString("PurchaseYN"));
                                            userSingletonModel.setPayrollClerkYN(jsonObject1.getString("PayrollClerkYN"));
                                            userSingletonModel.setEmpName(jsonObject1.getString("EmpName"));
                                            userSingletonModel.setUserType(jsonObject1.getString("UserType"));
                                            userSingletonModel.setEmailId(jsonObject1.getString("EmailId"));
                                            userSingletonModel.setPwdSetterId(jsonObject1.getString("PwdSetterId"));
                                            userSingletonModel.setFinYearID(jsonObject1.getString("FinYearID"));
                                            userSingletonModel.setMsg(jsonObject1.getString("Msg"));
                                            //--------setting the value in sigleton class variables code ends----------

                                            //======================storing the value to shared preference for onetime login code starts=============
                                            if (chkSignedIn.isChecked()){
                                                sharedPreferences = getApplication().getSharedPreferences("LoginDetails", Context.MODE_PRIVATE);
                                                SharedPreferences.Editor editor = sharedPreferences.edit();
                                                editor.putString("UserID", userSingletonModel.getUserID());
                                                editor.putString("UserName", userSingletonModel.getUserName());
                                                editor.putString("CompID", userSingletonModel.getCompID());
                                                editor.putString("CorpID", userSingletonModel.getCorpID());
                                                editor.putString("CompanyName", userSingletonModel.getCompanyName());
                                                editor.putString("SupervisorId", userSingletonModel.getSupervisorId());
                                                editor.putString("UserRole", userSingletonModel.getUserRole());
                                                editor.putString("AdminYN", userSingletonModel.getAdminYN());
                                                editor.putString("PayableClerkYN", userSingletonModel.getPayableClerkYN());
                                                editor.putString("SupervisorYN", userSingletonModel.getSupervisorYN());
                                                editor.putString("PurchaseYN", userSingletonModel.getPurchaseYN());
                                                editor.putString("PayrollClerkYN", userSingletonModel.getPayrollClerkYN());
                                                editor.putString("EmpName", userSingletonModel.getEmpName());
                                                editor.putString("UserType", userSingletonModel.getUserType());
                                                editor.putString("EmailId", userSingletonModel.getEmailId());
                                                editor.putString("PwdSetterId", userSingletonModel.getPwdSetterId());
                                                editor.putString("FinYearID", userSingletonModel.getFinYearID());
                                                editor.putString("Msg", userSingletonModel.getMsg());
                                                editor.commit();
                                                //======================storing the value to shared preference for onetime login code ends=============
                                            }
                                            Toast.makeText(LoginActivity.this, "Login Successful", Toast.LENGTH_SHORT).show();
                                            Intent intent = new Intent(LoginActivity.this, HomeActivity.class);
                                            startActivity(intent);
                                            loading.dismiss();
                                            finish();

                                        }

                                    }else{
                                        loading.dismiss();
                                        Toast.makeText(getApplicationContext(),"Invalid Login",Toast.LENGTH_LONG).show();
                                    }
                                    Log.d("statusTest",status);
//                                    Toast.makeText(getApplicationContext(),xx.getString("content"),Toast.LENGTH_LONG).show();
                                }
                            }
                            Log.d("logintest",responseData);
                        }catch (Exception e){
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
                params.put("CorpID", edtCorpId.getText().toString());
                params.put("UserName", edtUsername.getText().toString());
                params.put("Password",edtPassword.getText().toString());
                return params;
            }
        };

        RequestQueue requestQueue = Volley.newRequestQueue(LoginActivity.this);
        requestQueue.add(stringRequest);
    }
    //---------------volley code for login ends-------------

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
                    .make(findViewById(R.id.relativeLayout), message, Snackbar.LENGTH_LONG);

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
