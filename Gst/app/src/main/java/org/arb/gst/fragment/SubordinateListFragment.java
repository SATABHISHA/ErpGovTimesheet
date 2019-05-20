package org.arb.gst.fragment;

import android.app.ProgressDialog;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.ListView;
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

import org.arb.gst.Model.SubordinateListModel;
import org.arb.gst.Model.UserSingletonModel;
import org.arb.gst.Model.WeekDay;
import org.arb.gst.R;
import org.arb.gst.Timesheet.TimesheetHome;
import org.arb.gst.config.Config;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.json.XML;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

public class SubordinateListFragment extends Fragment {
    UserSingletonModel userSingletonModel = UserSingletonModel.getInstance();
    View rootView;
    ListView lv_subordinatelist;
    static String period_end_date;
    ArrayList<SubordinateListModel> arrayList = new ArrayList<>();

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {
        rootView =  inflater.inflate(R.layout.fragment_subordinate_list, null);
        lv_subordinatelist = (ListView)rootView.findViewById(R.id.lv_subordinatelist);
        loadData();
        return rootView;
    }

    public void loadData(){
        getPeriodDate(); //---to get the period date
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
                                        SubordinateListModel subordinateListModel = new SubordinateListModel();
                                        subordinateListModel.setId_person(jsonObject2.getString("id_person"));
                                        subordinateListModel.setEmployee_name(jsonObject2.getString("employee_name"));
//                                        subordinateListModel.setDepartment(jsonObj.getString("department"));
                                        arrayList.add(subordinateListModel);
                                    }
                                    lv_subordinatelist.setAdapter(new displaySubordinateList());
                                    lv_subordinatelist.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                                        @Override
                                        public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                                            userSingletonModel.setId_person(arrayList.get(i).getId_person());
                                            userSingletonModel.setSupervisor_employee_name(arrayList.get(i).getEmployee_name());
//                                            Toast.makeText(getContext(),arrayList.get(i).getId_person()+"/"+arrayList.get(i).getEmployee_name(),Toast.LENGTH_SHORT).show();
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
                params.put("WeekEndDate",period_end_date);
                params.put("DeviceType","1");
                return params;
            }
        };

        stringRequest.setRetryPolicy(new DefaultRetryPolicy(10000,
                DefaultRetryPolicy.DEFAULT_MAX_RETRIES, DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
        RequestQueue requestQueue = Volley.newRequestQueue(getActivity());
        requestQueue.add(stringRequest);

    }
    public void getPeriodDate(){
        String url = Config.BaseUrl+"TimeSheetPeriodDetail";
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
                                    period_end_date = jsonObject.getString("period_end_date");
                                    loading.dismiss();
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
                params.put("UserType",userSingletonModel.getUserType());
                params.put("PeriodDate",TimesheetHome.dateOnSelectedCalender);
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
            TextView tv_subordinate_name;
            LayoutInflater layoutInflater = LayoutInflater.from(getContext());
            view = layoutInflater.inflate(R.layout.custom_row_subordinatelist, viewGroup, false);
            tv_subordinate_name=(TextView)view.findViewById(R.id.tv_subordinate_name);
            tv_subordinate_name.setText(arrayList.get(i).getEmployee_name());
           /* ViewHolder viewHolder;
            if (view == null) {
                viewHolder = new ViewHolder();
                LayoutInflater layoutInflater = LayoutInflater.from(getContext());
                view = layoutInflater.inflate(R.layout.custom_row_subordinatelist, viewGroup, false);
                viewHolder.tv_subordinate_name=(TextView)view.findViewById(R.id.tv_subordinate_name);
                view.setTag(viewHolder);
            }else{
                viewHolder = (ViewHolder) view.getTag();
            }
            viewHolder.tv_subordinate_name.setText(arrayList.get(i).getSupervisor_employee_name());*/
            return view;
        }
    }

    static class ViewHolder {
        TextView tv_subordinate_name;
    }
}
