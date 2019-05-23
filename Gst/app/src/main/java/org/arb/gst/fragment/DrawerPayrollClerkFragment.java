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
        btn_apply = rootView.findViewById(R.id.btn_apply);
        btn_apply.setOnClickListener(this);

//        onCheckboxClicked(rootView);
        return rootView;
    }

    @Override
    public void onClick(View view) {
       /* int totalamount=0;
        StringBuilder result=new StringBuilder();
        result.append("Selected Items:");
        if(pizza.isChecked()){
            result.append("\nPizza 100Rs");
            totalamount+=100;
        }*/
       String status1="&lt;status&gt;";
       String status2="&lt;/status&gt;";
       StringBuilder result = new StringBuilder();
       if (checkbox_not_started.isChecked()){
          result.append("&lt;id&gt;0&lt;/id&gt;");
       }if (checkbox_saved.isChecked()){
           result.append("&lt;id&gt;1&lt;/id&gt;");
        }if (checkbox_submitted.isChecked()){
           result.append("&lt;id&gt;2&lt;/id&gt;");
        }if (checkbox_returned.isChecked()){
           result.append("&lt;id&gt;3&lt;/id&gt;");
        }if (checkbox_approved.isChecked()){
           result.append("&lt;id&gt;4&lt;/id&gt;");
        }if (checkbox_posted.isChecked()){
           result.append("&lt;id&gt;5&lt;/id&gt;");
        }if (checkbox_partiallyreturn.isChecked()){
           result.append("&lt;id&gt;6&lt;/id&gt;");
        }if (checkbox_partially_approved.isChecked()){
           result.append("&lt;id&gt;7&lt;/id&gt;");
        }
       String timesheet_status = status1+result.toString()+status2;
       userSingletonModel.setPayroll_strTimesheetStatusList(timesheet_status);
        Intent intent = new Intent(getActivity(),PayrollClerk.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        startActivity(intent);
        getActivity().finish();
    }
}
