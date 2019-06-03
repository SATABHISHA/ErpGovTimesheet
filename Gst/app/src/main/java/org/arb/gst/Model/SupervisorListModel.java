package org.arb.gst.Model;

public class SupervisorListModel {
    String id_person="", employee_name="", department="", ts_status_id, total_hours, supervisor_color_code, supervisor_status, supervisor_email_id;

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

    public String getTs_status_id() {
        return ts_status_id;
    }

    public String getTotal_hours() {
        return total_hours;
    }

    public String getSupervisor_color_code() {
        return supervisor_color_code;
    }

    public String getSupervisor_status() {
        return supervisor_status;
    }

    public String getSupervisor_email_id() {
        return supervisor_email_id;
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

    public void setTs_status_id(String ts_status_id) {
        this.ts_status_id = ts_status_id;
    }

    public void setTotal_hours(String total_hours) {
        this.total_hours = total_hours;
    }

    public void setSupervisor_color_code(String supervisor_color_code) {
        this.supervisor_color_code = supervisor_color_code;
    }

    public void setSupervisor_status(String supervisor_status) {
        this.supervisor_status = supervisor_status;
    }

    public void setSupervisor_email_id(String supervisor_email_id) {
        this.supervisor_email_id = supervisor_email_id;
    }
    //===================Setter method ends===============
}
