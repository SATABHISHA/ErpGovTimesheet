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
import org.arb.gst.Timesheet.PayrollPayableClerk;
import org.arb.gst.Timesheet.Subordinate;

public class DrawerSupervisorFragment extends Fragment implements View.OnClickListener {
    @Nullable
    View rootView;
    CheckBox checkbox_not_started,checkbox_saved,checkbox_returned,checkbox_posted,checkbox_submitted, checkbox_partiallyreturn,checkbox_approved, checkbox_partially_approved;
    Button btn_apply;
    UserSingletonModel userSingletonModel = UserSingletonModel.getInstance();
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
//        return super.onCreateView(inflater, container, savedInstanceState);
        rootView =  inflater.inflate(R.layout.fragment_supervisor_drawer, null);
        checkbox_not_started = rootView.findViewById(R.id.checkbox_not_started);
        checkbox_saved = rootView.findViewById(R.id.checkbox_saved);
        checkbox_returned = rootView.findViewById(R.id.checkbox_returned);
        checkbox_posted = rootView.findViewById(R.id.checkbox_posted);
        checkbox_submitted = rootView.findViewById(R.id.checkbox_submitted);
        checkbox_partiallyreturn = rootView.findViewById(R.id.checkbox_partiallyreturn);
        checkbox_approved = rootView.findViewById(R.id.checkbox_approved);
        checkbox_partially_approved = rootView.findViewById(R.id.checkbox_partially_approved);
        btn_apply = rootView.findViewById(R.id.btn_apply_supervisor);
        btn_apply.setOnClickListener(this);
//        onCheckboxClicked(rootView);
        checkbox_select();
        return rootView;
    }

    @Override
    public void onClick(View view) {
        if (!checkbox_not_started.isChecked()){
            userSingletonModel.setSupervisor_notstarted_yn("0");
        }else if(checkbox_not_started.isChecked()){
            userSingletonModel.setSupervisor_notstarted_yn("1");
        }
        if (!checkbox_saved.isChecked()){
            userSingletonModel.setSupervisor_saved_yn("0");
        }else if(checkbox_not_started.isChecked()){
            userSingletonModel.setSupervisor_saved_yn("1");
        }
        if (!checkbox_submitted.isChecked()){
            userSingletonModel.setSupervisor_submitted_yn("0");
        }else if(checkbox_not_started.isChecked()){
            userSingletonModel.setSupervisor_submitted_yn("1");
        }
        if (!checkbox_returned.isChecked()){
            userSingletonModel.setSupervisor_returned_yn("0");
        }else if(checkbox_not_started.isChecked()){
            userSingletonModel.setSupervisor_returned_yn("1");
        }
        if (!checkbox_approved.isChecked()){
            userSingletonModel.setSupervisor_approved_yn("0");
        }else if(checkbox_not_started.isChecked()){
            userSingletonModel.setSupervisor_approved_yn("1");
        }
        if (!checkbox_posted.isChecked()){
            userSingletonModel.setSupervisor_posted_yn("0");
        }else if(checkbox_not_started.isChecked()){
            userSingletonModel.setSupervisor_posted_yn("1");
        }if (!checkbox_partiallyreturn.isChecked()){
            userSingletonModel.setSupervisor_partially_returned_yn("0");
        }else if(checkbox_not_started.isChecked()){
            userSingletonModel.setSupervisor_partially_returned_yn("1");
        }if (!checkbox_partially_approved.isChecked()){
            userSingletonModel.setSupervisor_partially_approved_yn("0");
        }else if(checkbox_not_started.isChecked()){
            userSingletonModel.setSupervisor_partially_approved_yn("1");
        }
        Intent intent = new Intent(getActivity(), Subordinate.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        startActivity(intent);
        getActivity().finish();
    }

    //----------code to select checkbox starts------
    public void checkbox_select(){
        if(userSingletonModel.getSupervisor_notstarted_yn().contentEquals("1")){
            checkbox_not_started.setChecked(true);
        }else if(userSingletonModel.getSupervisor_notstarted_yn().contentEquals("0")){
            checkbox_not_started.setChecked(false);
        }
        if(userSingletonModel.getSupervisor_saved_yn().contentEquals("1")){
            checkbox_saved.setChecked(true);
        }else if(userSingletonModel.getSupervisor_saved_yn().contentEquals("0")){
            checkbox_saved.setChecked(false);
        }
        if(userSingletonModel.getSupervisor_submitted_yn().contentEquals("1")){
            checkbox_submitted.setChecked(true);
        }else if(userSingletonModel.getSupervisor_submitted_yn().contentEquals("0")){
            checkbox_submitted.setChecked(false);
        }
        if(userSingletonModel.getSupervisor_returned_yn().contentEquals("1")){
            checkbox_returned.setChecked(true);
        }else if(userSingletonModel.getSupervisor_returned_yn().contentEquals("0")){
            checkbox_returned.setChecked(false);
        }
        if(userSingletonModel.getSupervisor_approved_yn().contentEquals("1")){
            checkbox_approved.setChecked(true);
        }else if(userSingletonModel.getSupervisor_approved_yn().contentEquals("0")){
            checkbox_approved.setChecked(false);
        }
        if(userSingletonModel.getSupervisor_posted_yn().contentEquals("1")){
            checkbox_posted.setChecked(true);
        }else if(userSingletonModel.getSupervisor_posted_yn().contentEquals("0")){
            checkbox_posted.setChecked(false);
        }
        if(userSingletonModel.getSupervisor_partially_returned_yn().contentEquals("1")){
            checkbox_partiallyreturn.setChecked(true);
        }else if(userSingletonModel.getSupervisor_partially_returned_yn().contentEquals("0")){
            checkbox_partiallyreturn.setChecked(false);
        }
        if(userSingletonModel.getSupervisor_partially_approved_yn().contentEquals("1")){
            checkbox_partially_approved.setChecked(true);
        }else if(userSingletonModel.getSupervisor_partially_approved_yn().contentEquals("0")){
            checkbox_partially_approved.setChecked(false);
        }
    }
    //----------code to select checkbox ends-----


}
