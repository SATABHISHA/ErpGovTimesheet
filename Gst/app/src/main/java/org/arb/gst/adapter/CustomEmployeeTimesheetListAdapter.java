package org.arb.gst.adapter;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.support.design.widget.Snackbar;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.RecyclerView.ViewHolder;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.util.SparseBooleanArray;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import org.arb.gst.Home.HomeActivity;
import org.arb.gst.Model.EmployeeTimesheetListModel;
import org.arb.gst.Model.UserSingletonModel;
import org.arb.gst.Model.UserUpdateHoursModel;
import org.arb.gst.R;
import org.arb.gst.Timesheet.TimesheetSelectDay;
import org.arb.gst.Timesheet.TimesheetWorkUpdateHrs;

import java.util.ArrayList;

import static org.arb.gst.Timesheet.TimesheetWorkUpdateHrs.sum_initial_value;
import static org.arb.gst.Timesheet.TimesheetWorkUpdateHrs.tv_ts_wrkhrs_totalhrs;

public class CustomEmployeeTimesheetListAdapter extends RecyclerView.Adapter<CustomEmployeeTimesheetListAdapter.MyViewHolder> {

    public LayoutInflater inflater;
    public static ArrayList<EmployeeTimesheetListModel> employeeTimesheetListModelArrayList;
    UserSingletonModel userSingletonModel = UserSingletonModel.getInstance();
    public static String sumValue = "0";
    private Context context;

    public CustomEmployeeTimesheetListAdapter(Context ctx, ArrayList<EmployeeTimesheetListModel> employeeTimesheetListModelArrayList){

        inflater = LayoutInflater.from(ctx);
        this.employeeTimesheetListModelArrayList = employeeTimesheetListModelArrayList;
    }
    @Override
    public CustomEmployeeTimesheetListAdapter.MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = inflater.inflate(R.layout.recycler_timesheet_workhrs_update, parent, false);
        CustomEmployeeTimesheetListAdapter.MyViewHolder holder = new CustomEmployeeTimesheetListAdapter.MyViewHolder(view);
        context = parent.getContext();
        return holder;
    }

    @Override
    public void onBindViewHolder(CustomEmployeeTimesheetListAdapter.MyViewHolder holder, int position) {
        holder.itemView.setTag(employeeTimesheetListModelArrayList.get(position));
        holder.tv_rcl_ts_wkhsupdte_glcode.setText(employeeTimesheetListModelArrayList.get(position).getAccountCode());
        holder.tv_rcl_ts_wkhsupdte_taskdescription.setText(employeeTimesheetListModelArrayList.get(position).getTask());
        holder.tv_rcl_ts_wkhsupdte_labourtype.setText(employeeTimesheetListModelArrayList.get(position).getLaborCategory());
        holder.relative_task.setBackgroundColor(Color.parseColor(userSingletonModel.getColorcode()));
        Log.d("ColorCode=>",userSingletonModel.getColorcode());

        holder.editText.setText(employeeTimesheetListModelArrayList.get(position).getEditTextValue()); //---this lie is important to hold the editext value position

        holder.imgbtn_rcl_ts_wkhsupdte_viewnote.setVisibility(View.GONE);
        holder.imgbtn_rcl_ts_wkhsupdte_addnote.setVisibility(View.GONE);

        //----newly added 30th nov for making editText eidtable/non editable according to description status code starts-----
        if(HomeActivity.supervisor_yn_temp.contentEquals("0") && HomeActivity.payrollclerk_yn_temp.contentEquals("0") && HomeActivity.payableclerk_yn_temp.contentEquals("0")) {
            if (userSingletonModel.getStatusDescription().contentEquals("APPROVED") || userSingletonModel.getStatusDescription().contentEquals("SUBMITTED") || userSingletonModel.getStatusDescription().contentEquals("POSTED") || userSingletonModel.getStatusDescription().contentEquals("PARTIAL_APPROVE")) {
                holder.editText.setEnabled(false);
                holder.editText.setFocusable(false);
                holder.editText.setCursorVisible(false);
                holder.editText.setKeyListener(null);
                holder.editText.setBackgroundColor(Color.TRANSPARENT);
                holder.editText.setTextColor(Color.parseColor("#626262"));
                if (!employeeTimesheetListModelArrayList.get(position).getNote().contentEquals("")) {
//                Toast.makeText(context.getApplicationContext(),"Test2==>"+employeeTimesheetListModelArrayList.get(position).getNote(),Toast.LENGTH_LONG).show();
                    holder.imgbtn_rcl_ts_wkhsupdte_viewnote.setVisibility(View.VISIBLE);
                    if (!employeeTimesheetListModelArrayList.get(position).getNote().contentEquals("")) {
                        employeeTimesheetListModelArrayList.get(position).setEditTextAddNote(employeeTimesheetListModelArrayList.get(position).getNote());
                    }
                }
            } else {
                holder.editText.setEnabled(true);
                //------newly added 30th nov to make the add/view button visible/invisible, code starts-------
                if (!employeeTimesheetListModelArrayList.get(position).getNote().contentEquals("") || !employeeTimesheetListModelArrayList.get(position).getEditTextAddNote().contentEquals("")) {
//                Toast.makeText(context.getApplicationContext(),"Test1:->"+employeeTimesheetListModelArrayList.get(position).getEditTextAddNote().toString(),Toast.LENGTH_LONG).show();
                    holder.imgbtn_rcl_ts_wkhsupdte_viewnote.setVisibility(View.VISIBLE);
                    if (!employeeTimesheetListModelArrayList.get(position).getNote().contentEquals("")) {
                        employeeTimesheetListModelArrayList.get(position).setEditTextAddNote(employeeTimesheetListModelArrayList.get(position).getNote());
                    }
                } else if (employeeTimesheetListModelArrayList.get(position).getNote().contentEquals("") || employeeTimesheetListModelArrayList.get(position).getEditTextAddNote().contentEquals("")) {
                    holder.imgbtn_rcl_ts_wkhsupdte_addnote.setVisibility(View.VISIBLE);
                }
                Log.d("noteTest:", employeeTimesheetListModelArrayList.get(position).getNote().toString());
                //------newly added 30th nov to make the add/view button visible/invisible, code ends-------
            }
        }else if(HomeActivity.supervisor_yn_temp.contentEquals("1") || HomeActivity.payrollclerk_yn_temp.contentEquals("1") || HomeActivity.payableclerk_yn_temp.contentEquals("1")){
            holder.editText.setEnabled(false);
            holder.editText.setFocusable(false);
            holder.editText.setCursorVisible(false);
            holder.editText.setKeyListener(null);
            holder.editText.setBackgroundColor(Color.TRANSPARENT);
            holder.editText.setTextColor(Color.parseColor("#626262"));
            if (!employeeTimesheetListModelArrayList.get(position).getNote().contentEquals("")) {
//                Toast.makeText(context.getApplicationContext(),"Test2==>"+employeeTimesheetListModelArrayList.get(position).getNote(),Toast.LENGTH_LONG).show();
                holder.imgbtn_rcl_ts_wkhsupdte_viewnote.setVisibility(View.VISIBLE);
                if (!employeeTimesheetListModelArrayList.get(position).getNote().contentEquals("")) {
                    employeeTimesheetListModelArrayList.get(position).setEditTextAddNote(employeeTimesheetListModelArrayList.get(position).getNote());
                }
            }
        }
        Log.d("statusdesc",userSingletonModel.getStatusDescription());
        //----newly added 30th nov for making editText eidtable/non editable according to description status code ends-----






        Log.d("print","yes");
    }

    @Override
    public int getItemCount() {
        return employeeTimesheetListModelArrayList.size();
    }


    public class MyViewHolder extends ViewHolder{
        public TextView tv_rcl_ts_wkhsupdte_glcode,tv_rcl_ts_wkhsupdte_taskdescription,tv_rcl_ts_wkhsupdte_labourtype,
                edtxt_rcl_ts_wkhsupdate_addhrs;
        RelativeLayout relative_task;
        public EditText editText;
        ImageButton imgbtn_rcl_ts_wkhsupdte_addnote, imgbtn_rcl_ts_wkhsupdte_viewnote;
        public MyViewHolder(final View itemView) {
            super(itemView);
            tv_rcl_ts_wkhsupdte_glcode = (TextView)itemView.findViewById(R.id.tv_rcl_ts_wkhsupdte_glcode);
            tv_rcl_ts_wkhsupdte_taskdescription = (TextView)itemView.findViewById(R.id.tv_rcl_ts_wkhsupdte_taskdescription);
            tv_rcl_ts_wkhsupdte_labourtype = (TextView)itemView.findViewById(R.id.tv_rcl_ts_wkhsupdte_labourtype);
            imgbtn_rcl_ts_wkhsupdte_addnote = (ImageButton)itemView.findViewById(R.id.imgbtn_rcl_ts_wkhsupdte_addnote);
            imgbtn_rcl_ts_wkhsupdte_viewnote = (ImageButton)itemView.findViewById(R.id.imgbtn_rcl_ts_wkhsupdte_viewnote);
            editText = (EditText) itemView.findViewById(R.id.edtxt_rcl_ts_wkhsupdate_addhrs);
            relative_task = (RelativeLayout)itemView.findViewById(R.id.relative_task);


            editText.addTextChangedListener(new TextWatcher() {
                @Override
                public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

                }

                @Override
                public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

                    employeeTimesheetListModelArrayList.get(getAdapterPosition()).setEditTextValue(editText.getText().toString());
                    try {
                        employeeTimesheetListModelArrayList.get(getAdapterPosition()).setEditTextValueDouble(Double.valueOf(editText.getText().toString()));
                    }catch (NumberFormatException e){
                        e.printStackTrace();
                    }
                }

                @Override
                public void afterTextChanged(Editable editable) {

                    //===============following code will help to iterate through editextfields in recyclerview and calculate the edittext field value=========
                   employeeTimesheetListModelArrayList.get(getAdapterPosition()).setEditTextValue(editText.getText().toString());
                    Double sum = 0.0;
                    Double value = 0.0;
                    for (int i = 0; i < employeeTimesheetListModelArrayList.size(); i++){
                        try{
                            value = Double.parseDouble(employeeTimesheetListModelArrayList.get(i).getEditTextValue().toString());
                            sum = sum+value;
                        }catch (Exception e){
                        }

                        if(sum<=24){
                            sumValue = sum.toString();
                            tv_ts_wrkhrs_totalhrs.setText(sumValue);
                        }else if(sum>24 && sum_initial_value<=24){
                            //---------Alert dialog code starts--------
                            final AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(context);
                            alertDialogBuilder.setMessage("Task hours cannot be greater than 24 in a single day!");
                            alertDialogBuilder.setPositiveButton("OK",
                                    new DialogInterface.OnClickListener() {
                                        @Override
                                        public void onClick(DialogInterface arg0, int arg1) {
                                            alertDialogBuilder.setCancelable(true);
                                            editText.setText("");
                                        }
                                    });
                            final AlertDialog alertDialog = alertDialogBuilder.create();
                            alertDialog.show();

                            //--------Alert dialog code ends--------
                            break;
                        }else if(sum>24 && sum_initial_value>24){
                            sumValue = sum.toString();
                            tv_ts_wrkhrs_totalhrs.setText(sumValue);
                        }

                    }
                    //==========code to iterate through recycler view and get the edittext field value code ends==========
                }
            });

            //-------------addnote with custom dialog code starts-----------
            imgbtn_rcl_ts_wkhsupdte_addnote.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    final int position = getAdapterPosition();
//                    UserUpdateHoursModel userUpdateHoursModel = (UserUpdateHoursModel)view.getTag();
                    if (employeeTimesheetListModelArrayList.get(position).getEditTextValue().toString().contentEquals("0.00") || employeeTimesheetListModelArrayList.get(position).getEditTextValue().contentEquals("0.0") || employeeTimesheetListModelArrayList.get(position).getEditTextValue().contentEquals("0") || employeeTimesheetListModelArrayList.get(position).getEditTextValue().contentEquals(".0") || employeeTimesheetListModelArrayList.get(position).getEditTextValue().contentEquals(".00") || employeeTimesheetListModelArrayList.get(position).getEditTextValue().contentEquals("") || employeeTimesheetListModelArrayList.get(position).getEditTextValue().contentEquals(".")) {
//                        Toast.makeText(context.getApplicationContext(), "Note cannot be saved for 0 hour", Toast.LENGTH_LONG).show();
                        //---------Alert dialog code starts(added on 1st dec)--------
                        final AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(context);
                        alertDialogBuilder.setMessage("Note cannot be entered for 0 hour");
                        alertDialogBuilder.setPositiveButton("OK",
                                new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface arg0, int arg1) {
                                      alertDialogBuilder.setCancelable(true);
                                    }
                                });
                        final AlertDialog alertDialog = alertDialogBuilder.create();
                        alertDialog.show();

                        //--------Alert dialog code ends--------
                    } else {
                        LayoutInflater li = LayoutInflater.from(context);
                        View dialog = li.inflate(R.layout.dialog_addnotes, null);
//                    dialog.setBackgroundResource(android.R.color.transparent);
                        TextView text = (TextView) dialog.findViewById(R.id.tv_accountcode);
                        final EditText editText = (EditText) dialog.findViewById(R.id.ed_note);
                        Button btn_cancel = (Button) dialog.findViewById(R.id.btn_cancel);
                        Button btn_save = (Button) dialog.findViewById(R.id.btn_save);
                        ImageButton imgbtn_close = (ImageButton) dialog.findViewById(R.id.imgbtn_close);
//                    text.setText("Notes for "+employeeTimesheetListModelArrayList.get(position).getTask()+"("+employeeTimesheetListModelArrayList.get(position).getAccountCode()+")");
//                    editText.setText(employeeTimesheetListModelArrayList.get(position).getEditTextAddNote()); //---if edittext value exists then it will load data to edittext
                        text.setText("Notes for " + employeeTimesheetListModelArrayList.get(position).getTask());
                        AlertDialog.Builder alert = new AlertDialog.Builder(context);
                        alert.setView(dialog);
                        alert.setCancelable(false);
                        //Creating an alert dialog
                        final AlertDialog alertDialog = alert.create();
                        alertDialog.show();

                        //--------newly added 12th nov code starts------
                        editText.addTextChangedListener(new TextWatcher() {
                            @Override
                            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                            }

                            @Override
                            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                                employeeTimesheetListModelArrayList.get(position).setEditTextAddNote(editText.getText().toString());

                                //------newly added 14th nov(if addnote field is not empty then viewnote button will become visible----
                                if (employeeTimesheetListModelArrayList.get(position).getEditTextAddNote().contentEquals("")) {
                                    imgbtn_rcl_ts_wkhsupdte_viewnote.setVisibility(View.GONE);
                                    imgbtn_rcl_ts_wkhsupdte_addnote.setVisibility(View.VISIBLE);
                                } else if (!employeeTimesheetListModelArrayList.get(position).getEditTextAddNote().contentEquals("")) {
                                    imgbtn_rcl_ts_wkhsupdte_addnote.setVisibility(View.GONE);
                                    imgbtn_rcl_ts_wkhsupdte_viewnote.setVisibility(View.VISIBLE);
                                }  //---commented as it is not working properly
                                //-------newly added 14th nov code ends--------
                            }

                            @Override
                            public void afterTextChanged(Editable editable) {
                            }
                        });
                        //--------newly added 12th nov code ends----------
                        btn_save.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View view) {

                                employeeTimesheetListModelArrayList.get(position).setEditTextAddNote(editText.getText().toString());

                                alertDialog.dismiss();  //----newly added 12th nov
                            }
                        });
                        btn_cancel.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View view) {
                                alertDialog.dismiss();
                            }
                        });
                        imgbtn_close.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View view) {
                                alertDialog.dismiss();
                            }
                        });
//                        Toast.makeText(context.getApplicationContext(), employeeTimesheetListModelArrayList.get(position).getAccountCode(), Toast.LENGTH_LONG).show();
                    }
                }
            });
            //-------------addnote with custom dialog code ends---------------

            //-------------viewnote/editnote with custom dialog code starts---------
            imgbtn_rcl_ts_wkhsupdte_viewnote.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    final int position = getAdapterPosition();
//                    UserUpdateHoursModel userUpdateHoursModel = (UserUpdateHoursModel)view.getTag();
                    LayoutInflater li = LayoutInflater.from(context);
                    View dialog = li.inflate(R.layout.dialog_viewnotes, null);
//                    dialog.setBackgroundResource(android.R.color.transparent);
                    TextView text = (TextView) dialog.findViewById(R.id.tv_accountcode);
                    final EditText editText = (EditText) dialog.findViewById(R.id.ed_note);
                    final Button btn_cancel = (Button) dialog.findViewById(R.id.btn_cancel);
                    final Button btn_save = (Button) dialog.findViewById(R.id.btn_save);
                    ImageButton imgbtn_close = (ImageButton) dialog.findViewById(R.id.imgbtn_close);
                    final ImageButton imgbtn_edit = (ImageButton) dialog.findViewById(R.id.imgbtn_edit);

                    final LinearLayout linearlayout_btns = (LinearLayout) dialog.findViewById(R.id.linearlayout_btns);
//                    text.setText("Notes for "+employeeTimesheetListModelArrayList.get(position).getTask()+"("+employeeTimesheetListModelArrayList.get(position).getAccountCode()+")");
                    text.setText("Notes for "+employeeTimesheetListModelArrayList.get(position).getTask());
                    if(employeeTimesheetListModelArrayList.get(position).getEditTextAddNote().contentEquals("")){
                        editText.setText("");
                    }else if(!employeeTimesheetListModelArrayList.get(position).getEditTextAddNote().contentEquals("")){
                        editText.setText(employeeTimesheetListModelArrayList.get(position).getEditTextAddNote()); //---newly added 14th nov
                    }
                    AlertDialog.Builder alert = new AlertDialog.Builder(context);
                    alert.setView(dialog);
                    alert.setCancelable(false);
                    //Creating an alert dialog
                    final AlertDialog alertDialog = alert.create();
                    alertDialog.show();
                    imgbtn_close.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            alertDialog.dismiss();
                        }
                    });
//                    Toast.makeText(context.getApplicationContext(),employeeTimesheetListModelArrayList.get(position).getAccountCode(),Toast.LENGTH_LONG).show();
                    editText.setFocusable(false);
                    editText.setFocusableInTouchMode(false);
                    editText.setClickable(false);

                    //--------added on 3rd dec, code to check the condition and make the edit button visible/invisible according to the condition code starts-----
                    if(HomeActivity.supervisor_yn_temp.contentEquals("0") && HomeActivity.payrollclerk_yn_temp.contentEquals("0") && HomeActivity.payableclerk_yn_temp.contentEquals("0")) {
                        if (userSingletonModel.getStatusDescription().contentEquals("APPROVED") || userSingletonModel.getStatusDescription().contentEquals("SUBMITTED") || userSingletonModel.getStatusDescription().contentEquals("POSTED") || userSingletonModel.getStatusDescription().contentEquals("PARTIAL_APPROVE")) {
                            imgbtn_edit.setVisibility(View.GONE);
                        } else {
                            imgbtn_edit.setVisibility(View.VISIBLE);
                        }
                    }else if(HomeActivity.supervisor_yn_temp.contentEquals("1") || HomeActivity.payrollclerk_yn_temp.contentEquals("1") || HomeActivity.payableclerk_yn_temp.contentEquals("1")){
                        imgbtn_edit.setVisibility(View.GONE);
                    }
                    //--------added on 3rd dec, code to check the condition and make the edit button visible/invisible according to the condition code ends-----

                    //-------onClicking the imgbtn_edit, editText will become editable...code starts-----
                    imgbtn_edit.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            imgbtn_edit.setVisibility(View.GONE);
                            linearlayout_btns.setVisibility(View.VISIBLE);
                            editText.setBackgroundColor(Color.parseColor("#ffffff"));
                            editText.setFocusable(true);
                            editText.setFocusableInTouchMode(true);
                            editText.setClickable(true);
                            //--------newly added 14th nov code starts------
                            editText.addTextChangedListener(new TextWatcher() {
                                @Override
                                public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                                }

                                @Override
                                public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                                 /*   int position = getAdapterPosition();
                                    try {
                                        employeeTimesheetListModelArrayList.get(position).setEditTextAddNote(editText.getText().toString());
                                        //------newly added 14th nov(if addnote field is not empty then viewnote button will become visible----
                                        if(employeeTimesheetListModelArrayList.get(position).getEditTextAddNote().contentEquals("")){
                                            imgbtn_rcl_ts_wkhsupdte_viewnote.setVisibility(View.GONE);
                                            imgbtn_rcl_ts_wkhsupdte_addnote.setVisibility(View.VISIBLE);
                                        }else if(!employeeTimesheetListModelArrayList.get(position).getEditTextAddNote().contentEquals("")){
                                            imgbtn_rcl_ts_wkhsupdte_addnote.setVisibility(View.GONE);
                                            imgbtn_rcl_ts_wkhsupdte_viewnote.setVisibility(View.VISIBLE);
                                        }  //---commented as it is not working properly
                                        //-------newly added 14th nov code ends-------
                                    }catch (ArrayIndexOutOfBoundsException e){
                                        e.printStackTrace();
                                    }*/

                                }

                                @Override
                                public void afterTextChanged(Editable editable) {
                                    employeeTimesheetListModelArrayList.get(position).setEditTextAddNote(editText.getText().toString());  //--------added on 1st dec18
                                    employeeTimesheetListModelArrayList.get(position).setNote(employeeTimesheetListModelArrayList.get(position).getEditTextAddNote()); //---added on 1st dec2018
                                }
                            });
                            //--------newly added 14th nov code ends----------
                            btn_cancel.setOnClickListener(new View.OnClickListener() {
                                @Override
                                public void onClick(View view) {
                                    alertDialog.dismiss();
                                }
                            });
                            btn_save.setOnClickListener(new View.OnClickListener() {
                                @Override
                                public void onClick(View view) {
                                 employeeTimesheetListModelArrayList.get(position).setEditTextAddNote(editText.getText().toString());
                                    //------newly added 14th nov(if addnote field is not empty then viewnote button will become visible----
                                    if(employeeTimesheetListModelArrayList.get(position).getEditTextAddNote().contentEquals("")){
                                        imgbtn_rcl_ts_wkhsupdte_viewnote.setVisibility(View.GONE);
                                        imgbtn_rcl_ts_wkhsupdte_addnote.setVisibility(View.VISIBLE);
                                    }else if(!employeeTimesheetListModelArrayList.get(position).getEditTextAddNote().contentEquals("")){
                                        imgbtn_rcl_ts_wkhsupdte_addnote.setVisibility(View.GONE);
                                        imgbtn_rcl_ts_wkhsupdte_viewnote.setVisibility(View.VISIBLE);
                                    }  //---commented as it is not working properly
                                    //-------newly added 14th nov code ends-------
                                    alertDialog.dismiss();  //----newly added 12th nov
                                }
                            });
                        }
                    });
                    //---------onClicking the imgbtn_edit, editText will become eitable...code ends-------
                }
            });
            //-------------viewnote/editnote with custom dialog code ends-----------
        }
    }


}
