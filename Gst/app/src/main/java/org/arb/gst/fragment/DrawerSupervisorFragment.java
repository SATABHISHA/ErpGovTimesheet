package org.arb.gst.fragment;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;

import org.arb.gst.R;

public class DrawerSupervisorFragment extends Fragment {
    @Nullable
    View rootView;
    CheckBox checkbox_not_started,checkbox_saved,checkbox_correctionrequired,checkbox_posted,checkbox_submitted, checkbox_partiallyreturn,checkbox_approved, checkbox_partially_approved;
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
//        return super.onCreateView(inflater, container, savedInstanceState);
        rootView =  inflater.inflate(R.layout.fragment_supervisor_drawer, null);
        checkbox_not_started = rootView.findViewById(R.id.checkbox_not_started);
        checkbox_saved = rootView.findViewById(R.id.checkbox_saved);
        checkbox_correctionrequired = rootView.findViewById(R.id.checkbox_returned);
        checkbox_posted = rootView.findViewById(R.id.checkbox_posted);
        checkbox_submitted = rootView.findViewById(R.id.checkbox_submitted);
        checkbox_partiallyreturn = rootView.findViewById(R.id.checkbox_partiallyreturn);
        checkbox_approved = rootView.findViewById(R.id.checkbox_approved);
        checkbox_partially_approved = rootView.findViewById(R.id.checkbox_partially_approved);

//        onCheckboxClicked(rootView);
        return rootView;
    }

    /*private void onCheckboxClicked(View rootView) {
        boolean checked = ((CheckBox) rootView).isChecked();
        switch(rootView.getId()) {
            case R.id.checkbox_not_started:
                if (checked){
                    Toast.makeText(getContext(),"Not Started",Toast.LENGTH_LONG).show();
                }
                // Put some meat on the sandwich
            else
                // Remove the meat
                break;
            case R.id.checkbox_saved:
                if (checked){

                }
                // Cheese me
            else
                // I'm lactose intolerant
                break;
            case R.id.checkbox_returned:
                if (checked){

                }
                // Cheese me
                else
                    // I'm lactose intolerant
                    break;
            case R.id.checkbox_posted:
                if (checked){

                }
                // Cheese me
                else
                    // I'm lactose intolerant
                    break;
            case R.id.checkbox_submitted:
                if (checked){

                }
                // Cheese me
                else
                    // I'm lactose intolerant
                    break;
            case R.id.checkbox_partiallyreturn:
                if (checked){

                }
                // Cheese me
                else
                    // I'm lactose intolerant
                    break;
            case R.id.checkbox_approved:
                if (checked){

                }
                // Cheese me
                else
                    // I'm lactose intolerant
                    break;

            // TODO: Veggie sandwich
        }
    }*/


}
