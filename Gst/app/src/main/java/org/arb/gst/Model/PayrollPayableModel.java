package org.arb.gst.Model;

public class PayrollPayableModel {
    String email_id, email_notice, employee_code, employee_name,emp_type, id_person, item_type, supervisor_name, timesheet_status_desc, timesheet_status_id, total_hours, payaroll_payableclerk_colorcode, payroll_payable_clerk_status;
    public static String strTimesheetStatusList = "&lt;status&gt;&lt;id&gt;0&lt;/id&gt;&lt;id&gt;1&lt;/id&gt;&lt;id&gt;2&lt;/id&gt;&lt;id&gt;3&lt;/id&gt;&lt;id&gt;4&lt;/id&gt;&lt;id&gt;5&lt;/id&gt;&lt;id&gt;6&lt;/id&gt;&lt;id&gt;7&lt;/id&gt;&lt;/status&gt;";

    //----------------------Getter method starts-----------------

    public String getEmail_id() {
        return email_id;
    }

    public String getEmail_notice() {
        return email_notice;
    }

    public String getEmployee_code() {
        return employee_code;
    }

    public String getEmployee_name() {
        return employee_name;
    }

    public String getEmp_type() {
        return emp_type;
    }

    public String getId_person() {
        return id_person;
    }

    public String getItem_type() {
        return item_type;
    }

    public String getSupervisor_name() {
        return supervisor_name;
    }

    public String getTimesheet_status_desc() {
        return timesheet_status_desc;
    }

    public String getTimesheet_status_id() {
        return timesheet_status_id;
    }

    public String getTotal_hours() {
        return total_hours;
    }

    public String getPayaroll_payableclerk_colorcode() {
        return payaroll_payableclerk_colorcode;
    }

    public String getPayroll_payable_clerk_status() {
        return payroll_payable_clerk_status;
    }
    //----------------------Getter method ends-----------------

    //----------------------Setter method starts-----------------

    public void setEmail_id(String email_id) {
        this.email_id = email_id;
    }

    public void setEmail_notice(String email_notice) {
        this.email_notice = email_notice;
    }

    public void setEmployee_code(String employee_code) {
        this.employee_code = employee_code;
    }

    public void setEmployee_name(String employee_name) {
        this.employee_name = employee_name;
    }

    public void setEmp_type(String emp_type) {
        this.emp_type = emp_type;
    }

    public void setId_person(String id_person) {
        this.id_person = id_person;
    }

    public void setItem_type(String item_type) {
        this.item_type = item_type;
    }

    public void setSupervisor_name(String supervisor_name) {
        this.supervisor_name = supervisor_name;
    }

    public void setTimesheet_status_desc(String timesheet_status_desc) {
        this.timesheet_status_desc = timesheet_status_desc;
    }

    public void setTimesheet_status_id(String timesheet_status_id) {
        this.timesheet_status_id = timesheet_status_id;
    }

    public void setTotal_hours(String total_hours) {
        this.total_hours = total_hours;
    }

    public void setPayaroll_payableclerk_colorcode(String payaroll_payableclerk_colorcode) {
        this.payaroll_payableclerk_colorcode = payaroll_payableclerk_colorcode;
    }

    public void setPayroll_payable_clerk_status(String payroll_payable_clerk_status) {
        this.payroll_payable_clerk_status = payroll_payable_clerk_status;
    }
    //----------------------Setter method ends-----------------
}
