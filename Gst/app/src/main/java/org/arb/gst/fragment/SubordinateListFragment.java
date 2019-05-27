package org.arb.gst.fragment;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
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

import org.arb.gst.Model.SupervisorListModel;
import org.arb.gst.Model.UserSingletonModel;
import org.arb.gst.R;
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

public class SubordinateListFragment extends Fragment {
    UserSingletonModel userSingletonModel = UserSingletonModel.getInstance();
    View rootView;
    ListView lv_subordinatelist;
    ArrayList<SupervisorListModel> arrayList = new ArrayList<>();

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {
        rootView =  inflater.inflate(R.layout.fragment_subordinate_list, null);
        lv_subordinatelist = (ListView)rootView.findViewById(R.id.lv_subordinatelist);
        loadData();
        return rootView;
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
                                        supervisorListModel.setId_person(jsonObject2.getString("id_person"));
                                        supervisorListModel.setEmployee_name(jsonObject2.getString("employee_name"));
//                                        supervisorListModel.setSupervisor_department(jsonObj.getString("department"));
                                        arrayList.add(supervisorListModel);
                                    }
                                    lv_subordinatelist.setAdapter(new displaySubordinateList());
                                    lv_subordinatelist.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                                        @Override
                                        public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                                            userSingletonModel.setSupervisor_id_person(arrayList.get(i).getId_person()); //--not in use
                                            userSingletonModel.setSupervisor_employee_name(arrayList.get(i).getEmployee_name());

                                            userSingletonModel.setTimesheet_personId_yn("1");
                                            userSingletonModel.setPayable_payroll_supervisor_person_id(arrayList.get(i).getId_person());
                                            startActivity(new Intent(getActivity(), TimesheetSelectDay.class));
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
            TextView tv_subordinate_name;
            LayoutInflater layoutInflater = LayoutInflater.from(getContext());
            view = layoutInflater.inflate(R.layout.custom_row_subordinatelist, viewGroup, false);
            tv_subordinate_name=(TextView)view.findViewById(R.id.tv_subordinate_name);
            tv_subordinate_name.setText(arrayList.get(i).getEmployee_name());
            return view;
        }
    }

}
