package org.arb.gst.adapter;

import android.content.Context;
import android.graphics.Typeface;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseExpandableListAdapter;
import android.widget.ImageButton;
import android.widget.RelativeLayout;
import android.widget.TextView;

import org.arb.gst.Model.WeekDays;
import org.arb.gst.R;
import org.arb.gst.Timesheet.TimesheetSelectDay;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class ExpandableListAdapter extends BaseExpandableListAdapter {
    Context context;
    List<String> listTimesheetSelectDayModelsWeekDay;
//    private HashMap<String, List<String>> weekDaysList;
    private HashMap<String, ArrayList<WeekDays>> weekDaysArrayList;

    public ExpandableListAdapter(TimesheetSelectDay context, List<String> listTimesheetSelectDayModelsWeekDay,
                                 HashMap<String, ArrayList<WeekDays>> weekDaysArrayList) {
        this.context = (Context) context;
        this.listTimesheetSelectDayModelsWeekDay = listTimesheetSelectDayModelsWeekDay;
        this.weekDaysArrayList = weekDaysArrayList;
    }


    public int getGroupCount() {
        return listTimesheetSelectDayModelsWeekDay.size();
    }

    @Override
    public int getChildrenCount(int i) {
        return this.weekDaysArrayList.get(this.listTimesheetSelectDayModelsWeekDay.get(i)).size();
    }

    @Override
    public Object getGroup(int i) {
        return this.listTimesheetSelectDayModelsWeekDay.get(i);
    }

    @Override
    public Object getChild(int i, int i1) {
        return this.weekDaysArrayList.get(this.listTimesheetSelectDayModelsWeekDay.get(i)).get(i1);
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
        if (view == null) {
            LayoutInflater infalInflater = (LayoutInflater) this.context
                    .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            view = infalInflater.inflate(R.layout.listview_select_day_row_group, null);
        }

        TextView lblListHeader = (TextView)view.findViewById(R.id.lblListHeader);
        lblListHeader.setTypeface(null, Typeface.BOLD);
        lblListHeader.setText(listTimesheetSelectDayModelsWeekDay.get(i));
        return view;
    }

    @Override
    public View getChildView(int i, int i1, boolean b, View view, ViewGroup viewGroup) {
//        final String childText = (String) getChild(i, i1);
        WeekDays weekDays = (WeekDays)getChild(i,i1);

        if (view == null) {
            LayoutInflater infalInflater = (LayoutInflater) this.context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            view = infalInflater.inflate(R.layout.listview_select_day_row, null);
        }

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
//        String input = weekDaysArrayList.get(i).get(i1).getDayDate();
       /* int z = input.indexOf(' ');
        String word = input.substring(0, z);
        String rest = input.substring(i);*/
        tv_dayname.setText(weekDays.getDayDate());
        //---------code to get the first word from the string ends-----------
        return view;
    }

    @Override
    public boolean isChildSelectable(int i, int i1) {
        return true;
    }
}
