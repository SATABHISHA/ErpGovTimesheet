package org.arb.gst.Model;

public class SubordinateListModel {
    String id_person="", employee_name="", department="";

    //===================Getter method starts===============

    public String getId_person() {
        return id_person;
    }

    public String getEmployee_name() {
        return employee_name;
    }

    public String getDepartment() {
        return department;
    }
    //===================Getter method ends===============



    //===================Setter method starts===============

    public void setId_person(String id_person) {
        this.id_person = id_person;
    }

    public void setEmployee_name(String employee_name) {
        this.employee_name = employee_name;
    }

    public void setDepartment(String department) {
        this.department = department;
    }

    //===================Setter method ends===============
}
