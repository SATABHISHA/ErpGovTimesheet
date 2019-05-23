package org.arb.gst.fragment;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CheckBox;

import org.arb.gst.Model.UserSingletonModel;
import org.arb.gst.R;
import org.arb.gst.Timesheet.PayrollClerk;

public class DrawerPayrollClerkFragment extends Fragment implements View.OnClickListener {
    View rootView;
    CheckBox checkbox_not_started,checkbox_saved, checkbox_returned,checkbox_posted,checkbox_submitted, checkbox_partiallyreturn,checkbox_approved, checkbox_partially_approved;
    Button btn_apply;
    UserSingletonModel userSingletonModel = UserSingletonModel.getInstance();
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
//        return super.onCreateView(inflater, container, savedInstanceState);
        rootView =  inflater.inflate(R.layout.fragment_payrollclerk_drawer, null);
        checkbox_not_started = rootView.findViewById(R.id.checkbox_not_started);
        checkbox_saved = rootView.findViewById(R.id.checkbox_saved);
        checkbox_returned = rootView.findViewById(R.id.checkbox_returned);
        checkbox_posted = rootView.findViewById(R.id.checkbox_posted);
        checkbox_submitted = rootView.findViewById(R.id.checkbox_submitted);
        checkbox_partiallyreturn = rootView.findViewById(R.id.checkbox_partiallyreturn);
        checkbox_approved = rootView.findViewById(R.id.checkbox_approved);
        checkbox_partially_approved = rootView.findViewById(R.id.checkbox_partially_approved);

        checkbox_select(); //---function to select checkbox
        btn_apply = rootView.findViewById(R.id.btn_apply);
        btn_apply.setOnClickListener(this);
        return rootView;
    }

    @Override
    public void onClick(View view) {
        userSingletonModel.setPayroll_notstarted("0");
        userSingletonModel.setPayroll_saved("0");
        userSingletonModel.setPayroll_submitted("0");
        userSingletonModel.setPayroll_returned("0");
        userSingletonModel.setPayroll_approve("0");
        userSingletonModel.setPayroll_posted("0");
        userSingletonModel.setPayroll_partialreturn("0");
        userSingletonModel.setPayroll_partialapprove("0");

       String status1="&lt;status&gt;";
       String status2="&lt;/status&gt;";
       StringBuilder result = new StringBuilder();
       if (checkbox_not_started.isChecked()){
           userSingletonModel.setPayroll_notstarted("1");
          result.append("&lt;id&gt;0&lt;/id&gt;");
       }if (checkbox_saved.isChecked()){
            userSingletonModel.setPayroll_saved("1");
           result.append("&lt;id&gt;1&lt;/id&gt;");
        }if (checkbox_submitted.isChecked()){
            userSingletonModel.setPayroll_submitted("1");
           result.append("&lt;id&gt;2&lt;/id&gt;");
        }if (checkbox_returned.isChecked()){
            userSingletonModel.setPayroll_returned("1");
           result.append("&lt;id&gt;3&lt;/id&gt;");
        }if (checkbox_approved.isChecked()){
            userSingletonModel.setPayroll_approve("1");
           result.append("&lt;id&gt;4&lt;/id&gt;");
        }if (checkbox_posted.isChecked()){
            userSingletonModel.setPayroll_posted("1");
           result.append("&lt;id&gt;5&lt;/id&gt;");
        }if (checkbox_partiallyreturn.isChecked()){
            userSingletonModel.setPayroll_partialreturn("1");
           result.append("&lt;id&gt;6&lt;/id&gt;");
        }if (checkbox_partially_approved.isChecked()){
            userSingletonModel.setPayroll_partialapprove("1");
           result.append("&lt;id&gt;7&lt;/id&gt;");
        }
       String timesheet_status = status1+result.toString()+status2;
       userSingletonModel.setPayroll_strTimesheetStatusList(timesheet_status);
        Intent intent = new Intent(getActivity(),PayrollClerk.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        startActivity(intent);
        getActivity().finish();
    }

//----------code to select checkbox starts------
    public void checkbox_select(){
        if(userSingletonModel.getPayroll_notstarted().contentEquals("1")){
            checkbox_not_started.setChecked(true);
        }else if(userSingletonModel.getPayroll_notstarted().contentEquals("0")){
            checkbox_not_started.setChecked(false);
        }
        if(userSingletonModel.getPayroll_saved().contentEquals("1")){
            checkbox_saved.setChecked(true);
        }else if(userSingletonModel.getPayroll_saved().contentEquals("0")){
            checkbox_saved.setChecked(false);
        }
        if(userSingletonModel.getPayroll_submitted().contentEquals("1")){
            checkbox_submitted.setChecked(true);
        }else if(userSingletonModel.getPayroll_submitted().contentEquals("0")){
            checkbox_submitted.setChecked(false);
        }
        if(userSingletonModel.getPayroll_returned().contentEquals("1")){
            checkbox_returned.setChecked(true);
        }else if(userSingletonModel.getPayroll_returned().contentEquals("0")){
            checkbox_returned.setChecked(false);
        }
        if(userSingletonModel.getPayroll_approve().contentEquals("1")){
            checkbox_approved.setChecked(true);
        }else if(userSingletonModel.getPayroll_approve().contentEquals("0")){
            checkbox_approved.setChecked(false);
        }
        if(userSingletonModel.getPayroll_posted().contentEquals("1")){
            checkbox_posted.setChecked(true);
        }else if(userSingletonModel.getPayroll_posted().contentEquals("0")){
            checkbox_posted.setChecked(false);
        }
        if(userSingletonModel.getPayroll_partialreturn().contentEquals("1")){
            checkbox_partiallyreturn.setChecked(true);
        }else if(userSingletonModel.getPayroll_partialreturn().contentEquals("0")){
            checkbox_partiallyreturn.setChecked(false);
        }
        if(userSingletonModel.getPayroll_partialapprove().contentEquals("1")){
            checkbox_partially_approved.setChecked(true);
        }else if(userSingletonModel.getPayroll_partialapprove().contentEquals("0")){
            checkbox_partially_approved.setChecked(false);
        }
    }
    //----------code to select checkbox ends------
}
