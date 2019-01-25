package org.arb.gst.Model;

public class TimesheetSelectDayModel {
    String EmpName,PeriodRange,statusCode,WeekDate,TotalHours,EmpNote,SupNote,DayStatus,StatusDescription,ColorCode;

    //=====================Setter method starts===================

    public void setEmpName(String empName) {
        EmpName = empName;
    }

    public void setPeriodRange(String periodRange) {
        PeriodRange = periodRange;
    }

    public void setStatusCode(String statusCode) {
        this.statusCode = statusCode;
    }

    public void setWeekDate(String weekDate) {
        WeekDate = weekDate;
    }

    public void setTotalHours(String totalHours) {
        TotalHours = totalHours;
    }

    public void setEmpNote(String empNote) {
        EmpNote = empNote;
    }

    public void setSupNote(String supNote) {
        SupNote = supNote;
    }

    public void setDayStatus(String dayStatus) {
        DayStatus = dayStatus;
    }

    public void setStatusDescription(String statusDescription) {
        StatusDescription = statusDescription;
    }

    public void setColorCode(String colorCode) {
        ColorCode = colorCode;
    }
    //=====================Setter method ends======================


    //=====================Getter method starts================

    public String getEmpName() {
        return EmpName;
    }

    public String getPeriodRange() {
        return PeriodRange;
    }

    public String getStatusCode() {
        return statusCode;
    }

    public String getWeekDate() {
        return WeekDate;
    }

    public String getTotalHours() {
        return TotalHours;
    }

    public String getEmpNote() {
        return EmpNote;
    }

    public String getSupNote() {
        return SupNote;
    }

    public String getDayStatus() {
        return DayStatus;
    }

    public String getStatusDescription() {
        return StatusDescription;
    }

    public String getColorCode() {
        return ColorCode;
    }
    //=====================Getter method ends===================
}
