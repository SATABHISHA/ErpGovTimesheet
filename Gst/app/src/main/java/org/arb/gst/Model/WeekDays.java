package org.arb.gst.Model;

public class WeekDays {
    String dayName,hours,activeYN,dayDate,ColorCode;

    //=============Getter method starts============

    public String getDayName() {
        return dayName;
    }

    public String getHours() {
        return hours;
    }

    public String getActiveYN() {
        return activeYN;
    }

    public String getDayDate() {
        return dayDate;
    }

    public String getColorCode() {
        return ColorCode;
    }
    //=============Getter method ends============

    //=============Setter method starts=========

    public void setDayName(String dayName) {
        this.dayName = dayName;
    }

    public void setHours(String hours) {
        this.hours = hours;
    }

    public void setActiveYN(String activeYN) {
        this.activeYN = activeYN;
    }

    public void setDayDate(String dayDate) {
        this.dayDate = dayDate;
    }

    public void setColorCode(String colorCode) {
        ColorCode = colorCode;
    }
    //=============Setter method ends===========
}
