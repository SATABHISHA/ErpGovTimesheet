package org.arb.gst.adapter;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;
import org.arb.gst.Model.UserSingletonModel;
import org.arb.gst.Model.WeekDays;
import org.arb.gst.R;
import org.arb.gst.Timesheet.TimesheetWorkUpdateHrs;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

public class CustomSelectDayAdapter extends RecyclerView.Adapter<CustomSelectDayAdapter.MyViewHolder> {
    UserSingletonModel userSingletonModel = UserSingletonModel.getInstance();
    public LayoutInflater inflater;
    public static ArrayList<WeekDays> weekDaysArrayList;
    private Context context;

    public CustomSelectDayAdapter(Context ctx, ArrayList<WeekDays> weekDaysArrayList){

        inflater = LayoutInflater.from(ctx);
        this.weekDaysArrayList = weekDaysArrayList;
    }
    @Override
    public CustomSelectDayAdapter.MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = inflater.inflate(R.layout.recycler_select_day_row, parent, false);
        CustomSelectDayAdapter.MyViewHolder holder = new CustomSelectDayAdapter.MyViewHolder(view);
        context = parent.getContext();
        return holder;
    }

    @Override
    public void onBindViewHolder(CustomSelectDayAdapter.MyViewHolder holder, int position) {
//        holder.tv_dayname.setText(weekDaysArrayList.get(position).getDayName());  //---temporary commenting

        //---------following is the code to get the first word from the string, code starts--------
        String input = weekDaysArrayList.get(position).getDayName();
        int i = input.indexOf(' ');
        String word = input.substring(0, i);
        String rest = input.substring(i);
        holder.tv_dayname.setText(word);
        //---------code to get the first word from the string ends-----------

        //----following code newly added on 8th dec to make the text color faded if hours value is 0-------
        if(weekDaysArrayList.get(position).getHours().contentEquals("0.0")){
            holder.tv_hr.setText(weekDaysArrayList.get(position).getHours());
            holder.tv_hr.setAlpha(0.6f);
        }else {
            holder.tv_hr.setText(weekDaysArrayList.get(position).getHours());
        }
        //----following code newly added on 8th dec to make the text color faded if hours value is 0-------

        holder.tv_date.setText(weekDaysArrayList.get(position).getDayDate());
        holder.relativeLayout.setBackgroundColor(Color.parseColor(weekDaysArrayList.get(position).getColorCode()));
        userSingletonModel.setColorcode(weekDaysArrayList.get(position).getColorCode());

        //-----------have modified the following code on 3rd dec------------------
        if ((userSingletonModel.getStatusDescription().contentEquals("APPROVED") || userSingletonModel.getStatusDescription().contentEquals("SUBMITTED") || userSingletonModel.getStatusDescription().contentEquals("POSTED") || userSingletonModel.getStatusDescription().contentEquals("PARTIAL_APPROVE")) && weekDaysArrayList.get(position).getHours().contentEquals("0.0")) {
            holder.imgbtn_add.setVisibility(View.INVISIBLE);
            holder.rl_add.setVisibility(View.INVISIBLE);//---added on 30th nov for large area selection of recycler item/button

            holder.imgbtn_view.setVisibility(View.VISIBLE);
            holder.rl_view.setVisibility(View.VISIBLE);//---added on 30th nov for large area selection of recycler item/button
        } else if ((userSingletonModel.getStatusDescription().contentEquals("APPROVED") || userSingletonModel.getStatusDescription().contentEquals("SUBMITTED") || userSingletonModel.getStatusDescription().contentEquals("POSTED") || userSingletonModel.getStatusDescription().contentEquals("PARTIAL_APPROVE")) && !weekDaysArrayList.get(position).getHours().contentEquals("0.0")){
            holder.imgbtn_add.setVisibility(View.INVISIBLE);
            holder.rl_add.setVisibility(View.INVISIBLE);//---added on 30th nov for large area selection of recycler item/button

            holder.imgbtn_view.setVisibility(View.VISIBLE);
            holder.rl_view.setVisibility(View.VISIBLE);//---added on 30th nov for large area selection of recycler item/button
        } else if ((!userSingletonModel.getStatusDescription().contentEquals("APPROVED") || !userSingletonModel.getStatusDescription().contentEquals("SUBMITTED") || !userSingletonModel.getStatusDescription().contentEquals("POSTED") || !userSingletonModel.getStatusDescription().contentEquals("PARTIAL_APPROVE")) && !weekDaysArrayList.get(position).getHours().contentEquals("0.0")){
            holder.imgbtn_add.setVisibility(View.INVISIBLE);
            holder.rl_add.setVisibility(View.INVISIBLE);//---added on 30th nov for large area selection of recycler item/button

            holder.imgbtn_view.setVisibility(View.VISIBLE);
            holder.rl_view.setVisibility(View.VISIBLE);//---added on 30th nov for large area selection of recycler item/button
        }else if ((!userSingletonModel.getStatusDescription().contentEquals("APPROVED") || !userSingletonModel.getStatusDescription().contentEquals("SUBMITTED") || !userSingletonModel.getStatusDescription().contentEquals("POSTED") || !userSingletonModel.getStatusDescription().contentEquals("PARTIAL_APPROVE")) && weekDaysArrayList.get(position).getHours().contentEquals("0.0")){
            holder.imgbtn_add.setVisibility(View.VISIBLE);
            holder.rl_add.setVisibility(View.VISIBLE);//---added on 30th nov for large area selection of recycler item/button

            holder.imgbtn_view.setVisibility(View.INVISIBLE);
            holder.rl_view.setVisibility(View.INVISIBLE);//---added on 30th nov for large area selection of recycler item/button
        }
        //------------have modified the above code on 3rd dec--------------------
    }

    @Override
    public int getItemCount() {
        return weekDaysArrayList.size();
    }
    public  class MyViewHolder extends RecyclerView.ViewHolder {
        public TextView tv_dayname, tv_hr, tv_date;
        ImageButton imgbtn_add,imgbtn_view;
        RelativeLayout relativeLayout, rl_add, rl_view;
        public MyViewHolder(View itemView) {
            super(itemView);
            tv_dayname = (TextView)itemView.findViewById(R.id.tv_dayname);
            tv_hr = (TextView)itemView.findViewById(R.id.tv_hr);
            tv_date = (TextView)itemView.findViewById(R.id.tv_date);
            imgbtn_add = (ImageButton)itemView.findViewById(R.id.imgbtn_add);
            rl_add = (RelativeLayout)itemView.findViewById(R.id.rl_add);//---added on 30th nov for large area selection of recycler item/button
            imgbtn_view = (ImageButton)itemView.findViewById(R.id.imgbtn_view);
            rl_view = (RelativeLayout)itemView.findViewById(R.id.rl_view); //---added on 30th nov for large area selection of recycler item/button
            relativeLayout = (RelativeLayout)itemView.findViewById(R.id.relative_layout);

            imgbtn_add.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    int position = getAdapterPosition();
//                    Toast.makeText(context.getApplicationContext(),weekDaysArrayList.get(position).getDayDate(),Toast.LENGTH_LONG).show();
                    userSingletonModel.setDayDate(weekDaysArrayList.get(position).getDayDate());
                    context.startActivity(new Intent(context,TimesheetWorkUpdateHrs.class));
                }
            });
            //---added on 30th nov for large area selection of recycler item/button, code starts--------
            rl_add.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    int position = getAdapterPosition();
//                    Toast.makeText(context.getApplicationContext(),weekDaysArrayList.get(position).getDayDate(),Toast.LENGTH_LONG).show();
                    userSingletonModel.setDayDate(weekDaysArrayList.get(position).getDayDate());
                    context.startActivity(new Intent(context,TimesheetWorkUpdateHrs.class));
                }
            });
            //---added on 30th nov for large area selection of recycler item/button, code ends--------

            imgbtn_view.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    int position = getAdapterPosition();
//                    Toast.makeText(context.getApplicationContext(),weekDaysArrayList.get(position).getDayDate(),Toast.LENGTH_LONG).show();
                    userSingletonModel.setDayDate(weekDaysArrayList.get(position).getDayDate());
                    context.startActivity(new Intent(context,TimesheetWorkUpdateHrs.class));
                }
            });

            //---added on 30th nov for large area selection of recycler item/button, code starts--------
            rl_view.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    int position = getAdapterPosition();
//                    Toast.makeText(context.getApplicationContext(),weekDaysArrayList.get(position).getDayDate(),Toast.LENGTH_LONG).show();
                    userSingletonModel.setDayDate(weekDaysArrayList.get(position).getDayDate());
                    context.startActivity(new Intent(context,TimesheetWorkUpdateHrs.class));
                }
            });
            //---added on 30th nov for large area selection of recycler item/button, code ends--------

        }
    }
}
