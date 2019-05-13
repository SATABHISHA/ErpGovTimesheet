package org.arb.gst.adapter;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.Typeface;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseExpandableListAdapter;
import android.widget.ExpandableListView;
import android.widget.ImageButton;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import org.arb.gst.Model.TimesheetSelectDayModel;
import org.arb.gst.Model.UserSingletonModel;
import org.arb.gst.Model.WeekDays;
import org.arb.gst.R;
import org.arb.gst.Timesheet.TimesheetSelectDay;
import org.arb.gst.Timesheet.TimesheetWorkUpdateHrs;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class ExpandableListAdapter extends BaseExpandableListAdapter {
    Context context;
    ArrayList<TimesheetSelectDayModel> arraylistTimesheetSelectDayModelsWeekDay;
    private HashMap<TimesheetSelectDayModel, ArrayList<WeekDays>> weekDaysArrayList;
    UserSingletonModel userSingletonModel = UserSingletonModel.getInstance();

    public ExpandableListAdapter(TimesheetSelectDay context, ArrayList<TimesheetSelectDayModel> arraylistTimesheetSelectDayModelsWeekDay,
                                 HashMap<TimesheetSelectDayModel, ArrayList<WeekDays>> weekDaysArrayList) {
        this.context = (Context) context;
        this.arraylistTimesheetSelectDayModelsWeekDay = arraylistTimesheetSelectDayModelsWeekDay;
        this.weekDaysArrayList = weekDaysArrayList;
    }


    public int getGroupCount() {
        return arraylistTimesheetSelectDayModelsWeekDay.size();
    }

    @Override
    public int getChildrenCount(int i) {
        return this.weekDaysArrayList.get(this.arraylistTimesheetSelectDayModelsWeekDay.get(i)).size();
    }

    @Override
    public Object getGroup(int i) {
        return this.arraylistTimesheetSelectDayModelsWeekDay.get(i);
    }

    @Override
    public Object getChild(int i, int i1) {
        return this.weekDaysArrayList.get(this.arraylistTimesheetSelectDayModelsWeekDay.get(i)).get(i1);
    }

    @Override
    public long getGroupId(int i) {
        return i;
    }

    @Override
    public long getChildId(int i, int i1) {
        return i1;
    }

    @Override
    public boolean hasStableIds() {
        return false;
    }

    @Override
    public View getGroupView(int i, boolean b, View view, ViewGroup viewGroup) {
//        String headerTitle = (String) getGroup(i);
        final TimesheetSelectDayModel timesheetSelectDayModel = (TimesheetSelectDayModel)getGroup(i);
       /* if (view == null) {
            LayoutInflater infalInflater = (LayoutInflater) this.context
                    .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            view = infalInflater.inflate(R.layout.listview_select_day_row_group, null);
        }*/

        LayoutInflater infalInflater = (LayoutInflater) this.context
                .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        view = infalInflater.inflate(R.layout.listview_select_day_row_group, null);

        TextView tv_selected_date = (TextView)view.findViewById(R.id.tv_selected_date);
        TextView tv_totalhrs = (TextView)view.findViewById(R.id.tv_totalhrs);
        tv_selected_date.setTypeface(null, Typeface.BOLD);
        tv_selected_date.setText(timesheetSelectDayModel.getWeekDate());
        tv_totalhrs.setText(timesheetSelectDayModel.getTotalHours());
        return view;
    }

    @Override
    public View getChildView(int i, int i1, boolean b, View view, ViewGroup viewGroup) {
//        final String childText = (String) getChild(i, i1);
        final WeekDays weekDays = (WeekDays)getChild(i,i1);
        final TimesheetSelectDayModel timesheetSelectDayModel = (TimesheetSelectDayModel)getGroup(i);

        /*if (view == null) {
            LayoutInflater infalInflater = (LayoutInflater) this.context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            view = infalInflater.inflate(R.layout.listview_select_day_row, null);
        }*/

        LayoutInflater infalInflater = (LayoutInflater) this.context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        view = infalInflater.inflate(R.layout.listview_select_day_row, null);

        TextView tv_dayname, tv_hr, tv_date;
        ImageButton imgbtn_add,imgbtn_view;
        RelativeLayout relativeLayout, rl_add, rl_view;
        tv_dayname = (TextView)view.findViewById(R.id.tv_dayname);
        tv_hr = (TextView)view.findViewById(R.id.tv_hr);
        tv_date = (TextView)view.findViewById(R.id.tv_date);
        imgbtn_add = (ImageButton)view.findViewById(R.id.imgbtn_add);
        rl_add = (RelativeLayout)view.findViewById(R.id.rl_add);
        imgbtn_view = (ImageButton)view.findViewById(R.id.imgbtn_view);
        rl_view = (RelativeLayout)view.findViewById(R.id.rl_view);
        relativeLayout = (RelativeLayout)view.findViewById(R.id.relative_layout);

        //---------following is the code to get the first word from the string, code starts--------
//        String input = weekDaysList.get(i).get(i1);
        String input = weekDays.getDayName();
        int z = input.indexOf(' ');
        String word = input.substring(0, z);
        String rest = input.substring(i);
        tv_dayname.setText(word);
        tv_date.setText(weekDays.getDayDate());
//        tv_hr.setText(weekDays.getHours());
        //---------code to get the first word from the string ends-----------

        //----following code newly added on 8th dec to make the text color faded if hours value is 0, starts...-------
        if(weekDays.getHours().contentEquals("0.0")){
            tv_hr.setText(weekDays.getHours());
            tv_hr.setAlpha(0.6f);
        }else if(!weekDays.getHours().contentEquals("0.0")){
//            tv_hr.setTextColor(Color.parseColor("#000000"));
            tv_hr.setText(weekDays.getHours());
            tv_hr.setTextSize(19);
        }
        //----above code newly added on 8th dec to make the text color faded if hours value is 0, ends...-------
        relativeLayout.setBackgroundColor(Color.parseColor(weekDays.getColorCode()));
        userSingletonModel.setColorcode(weekDays.getColorCode());

        //-----------have modified the following code on 9th May------------------
        if ((userSingletonModel.getStatusDescription().contentEquals("APPROVED") || userSingletonModel.getStatusDescription().contentEquals("SUBMITTED") || userSingletonModel.getStatusDescription().contentEquals("POSTED") || userSingletonModel.getStatusDescription().contentEquals("PARTIAL_APPROVE")) && weekDays.getHours().contentEquals("0.0")) {
            imgbtn_add.setVisibility(View.INVISIBLE);
            rl_add.setVisibility(View.INVISIBLE);//---added on 30th nov for large area selection of recycler item/button

            imgbtn_view.setVisibility(View.VISIBLE);
            rl_view.setVisibility(View.VISIBLE);//---added on 30th nov for large area selection of recycler item/button
        } else if ((userSingletonModel.getStatusDescription().contentEquals("APPROVED") || userSingletonModel.getStatusDescription().contentEquals("SUBMITTED") || userSingletonModel.getStatusDescription().contentEquals("POSTED") || userSingletonModel.getStatusDescription().contentEquals("PARTIAL_APPROVE")) && !weekDays.getHours().contentEquals("0.0")){
            imgbtn_add.setVisibility(View.INVISIBLE);
            rl_add.setVisibility(View.INVISIBLE);//---added on 30th nov for large area selection of recycler item/button

            imgbtn_view.setVisibility(View.VISIBLE);
            rl_view.setVisibility(View.VISIBLE);//---added on 30th nov for large area selection of recycler item/button
        } else if ((!userSingletonModel.getStatusDescription().contentEquals("APPROVED") || !userSingletonModel.getStatusDescription().contentEquals("SUBMITTED") || !userSingletonModel.getStatusDescription().contentEquals("POSTED") || !userSingletonModel.getStatusDescription().contentEquals("PARTIAL_APPROVE")) && !weekDays.getHours().contentEquals("0.0")){
            imgbtn_add.setVisibility(View.INVISIBLE);
            rl_add.setVisibility(View.INVISIBLE);//---added on 30th nov for large area selection of recycler item/button

            imgbtn_view.setVisibility(View.VISIBLE);
            rl_view.setVisibility(View.VISIBLE);//---added on 30th nov for large area selection of recycler item/button
        }else if ((!userSingletonModel.getStatusDescription().contentEquals("APPROVED") || !userSingletonModel.getStatusDescription().contentEquals("SUBMITTED") || !userSingletonModel.getStatusDescription().contentEquals("POSTED") || !userSingletonModel.getStatusDescription().contentEquals("PARTIAL_APPROVE")) && weekDays.getHours().contentEquals("0.0")){
            imgbtn_add.setVisibility(View.VISIBLE);
            rl_add.setVisibility(View.VISIBLE);//---added on 30th nov for large area selection of recycler item/button

            imgbtn_view.setVisibility(View.INVISIBLE);
            rl_view.setVisibility(View.INVISIBLE);//---added on 30th nov for large area selection of recycler item/button
        }

        imgbtn_add.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                userSingletonModel.setDayDate(weekDays.getDayDate());
                userSingletonModel.setTimesheetSelectDate_WeekDate(timesheetSelectDayModel.getWeekDate());
//                Toast.makeText(context,userSingletonModel.getTimesheetSelectDate_WeekDate(),Toast.LENGTH_LONG).show();
                context.startActivity(new Intent(context, TimesheetWorkUpdateHrs.class));
            }
        });
        imgbtn_view.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                userSingletonModel.setDayDate(weekDays.getDayDate());
                userSingletonModel.setTimesheetSelectDate_WeekDate(timesheetSelectDayModel.getWeekDate());
//                Toast.makeText(context,userSingletonModel.getTimesheetSelectDate_WeekDate(),Toast.LENGTH_LONG).show();
                context.startActivity(new Intent(context,TimesheetWorkUpdateHrs.class));
            }
        });
        rl_view.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                userSingletonModel.setDayDate(weekDays.getDayDate());
                userSingletonModel.setTimesheetSelectDate_WeekDate(timesheetSelectDayModel.getWeekDate());
//                Toast.makeText(context,userSingletonModel.getTimesheetSelectDate_WeekDate(),Toast.LENGTH_LONG).show();
                context.startActivity(new Intent(context,TimesheetWorkUpdateHrs.class));
            }
        });
        //------------have modified the above code on 9th May--------------------

        return view;
    }

    @Override
    public boolean isChildSelectable(int i, int i1) {
        return true;
    }
}
