package org.arb.gst.adapter;

import android.content.Context;
import android.content.Intent;
import android.support.v4.content.LocalBroadcastManager;
import android.support.v7.widget.RecyclerView;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import org.arb.gst.Model.UserUpdateHoursModel;
import org.arb.gst.R;
import org.arb.gst.Timesheet.TimesheetSelectDay;

import java.util.ArrayList;

import static org.arb.gst.Timesheet.TimesheetWorkUpdateHrs.tv_ts_wrkhrs_totalhrs;

public class CustomEmployeeTimesheetdemoAdapter extends RecyclerView.Adapter<CustomEmployeeTimesheetdemoAdapter.MyViewHolder>{
    public LayoutInflater inflater;
    public static ArrayList<UserUpdateHoursModel> userUpdateHoursModelArrayList;
    public static String sumValue = "0";
    private Context context;


    public CustomEmployeeTimesheetdemoAdapter(Context ctx, ArrayList<UserUpdateHoursModel> userUpdateHoursModelArrayList){

        inflater = LayoutInflater.from(ctx);
        this.userUpdateHoursModelArrayList = userUpdateHoursModelArrayList;
    }

    @Override
    public CustomEmployeeTimesheetdemoAdapter.MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = inflater.inflate(R.layout.recycler_timesheet_workhrs_update, parent, false);
        MyViewHolder holder = new MyViewHolder(view);
        context = parent.getContext();
        return holder;
    }

    @Override
    public void onBindViewHolder(CustomEmployeeTimesheetdemoAdapter.MyViewHolder holder, int position) {
        holder.itemView.setTag(userUpdateHoursModelArrayList.get(position));
        holder.tv_rcl_ts_wkhsupdte_glcode.setText(userUpdateHoursModelArrayList.get(position).getContractCode());
//        holder.edtxt_rcl_ts_wkhsupdate_addhrs.setText(userUpdateHoursModelArrayList.get(position).getEditTextValue());
        holder.editText.setText(userUpdateHoursModelArrayList.get(position).getEditTextValue());
        Log.d("print","yes");
    }

    @Override
    public int getItemCount() {
        return userUpdateHoursModelArrayList.size();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder{
        public TextView tv_rcl_ts_wkhsupdte_glcode,tv_rcl_ts_wkhsupdte_taskdescription,tv_rcl_ts_wkhsupdte_labourtype,
                edtxt_rcl_ts_wkhsupdate_addhrs;
        public EditText editText;
        ImageButton imgbtn_rcl_ts_wkhsupdte_addnote;
        public MyViewHolder(View itemView) {
            super(itemView);
            tv_rcl_ts_wkhsupdte_glcode = (TextView)itemView.findViewById(R.id.tv_rcl_ts_wkhsupdte_glcode);
            tv_rcl_ts_wkhsupdte_taskdescription = (TextView)itemView.findViewById(R.id.tv_rcl_ts_wkhsupdte_taskdescription);
            tv_rcl_ts_wkhsupdte_labourtype = (TextView)itemView.findViewById(R.id.tv_rcl_ts_wkhsupdte_labourtype);
            imgbtn_rcl_ts_wkhsupdte_addnote = (ImageButton)itemView.findViewById(R.id.imgbtn_rcl_ts_wkhsupdte_addnote);
            editText = (EditText) itemView.findViewById(R.id.edtxt_rcl_ts_wkhsupdate_addhrs);

            editText.addTextChangedListener(new TextWatcher() {
                @Override
                public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

                }

                @Override
                public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

                    userUpdateHoursModelArrayList.get(getAdapterPosition()).setEditTextValue(editText.getText().toString());
                }

                @Override
                public void afterTextChanged(Editable editable) {

                    //===============following code will help to iterate through editextfields in recyclerview and calculate the edittext field value=========
                    Double sum = 0.0;
                    Double value = 0.0;
                    for (int i = 0; i < userUpdateHoursModelArrayList.size(); i++){
                        try{
                            value = Double.parseDouble(userUpdateHoursModelArrayList.get(i).getEditTextValue().toString());
                            sum = sum+value;
                        }catch (Exception e){
                        }
                        sumValue = sum.toString();
                        tv_ts_wrkhrs_totalhrs.setText(sumValue);

                    }
                    //==========code to iterate through recycler view and get the edittext field value code ends==========
                }
            });

            imgbtn_rcl_ts_wkhsupdte_addnote.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    int position = getAdapterPosition();
//                    UserUpdateHoursModel userUpdateHoursModel = (UserUpdateHoursModel)view.getTag();
                    Toast.makeText(context.getApplicationContext(),userUpdateHoursModelArrayList.get(position).getContractCode(),Toast.LENGTH_LONG).show();
                }
            });
        }
    }
}
