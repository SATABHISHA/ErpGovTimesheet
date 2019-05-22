package org.arb.gst.fragment;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;

import org.arb.gst.R;

public class DrawerPayrollClerkFragment extends Fragment {
    View rootView;
    CheckBox checkbox_not_started,checkbox_saved,checkbox_correctionrequired,checkbox_posted,checkbox_submitted, checkbox_partiallyreturn,checkbox_approved, checkbox_partially_approved;
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
//        return super.onCreateView(inflater, container, savedInstanceState);
        rootView =  inflater.inflate(R.layout.fragment_payrollclerk_drawer, null);
        checkbox_not_started = rootView.findViewById(R.id.checkbox_not_started);
        checkbox_saved = rootView.findViewById(R.id.checkbox_saved);
        checkbox_correctionrequired = rootView.findViewById(R.id.checkbox_correctionrequired);
        checkbox_posted = rootView.findViewById(R.id.checkbox_posted);
        checkbox_submitted = rootView.findViewById(R.id.checkbox_submitted);
        checkbox_partiallyreturn = rootView.findViewById(R.id.checkbox_partiallyreturn);
        checkbox_approved = rootView.findViewById(R.id.checkbox_approved);
        checkbox_partially_approved = rootView.findViewById(R.id.checkbox_partially_approved);

//        onCheckboxClicked(rootView);
        return rootView;
    }
}
