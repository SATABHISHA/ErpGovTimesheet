package org.arb.gst.Model;

public class UserSingletonModel {
    String UserID, UserName, CompID, CorpID, CompanyName, SupervisorId, UserRole, AdminYN, PayableClerkYN, SupervisorYN, PurchaseYN,
            PayrollClerkYN, EmpName, UserType, EmailId, PwdSetterId, FinYearID, Msg, DayDate, periodStartDate, periodEndDate, timesheetSelectDay_empNote = "", timesheetSelectDay_supNote = "", colorcode = "", statusDescription = "",
            imagePath="",timesheetSelectDate_WeekDate, employeeYN, supervisor_id_person, supervisor_employee_name, supervisor_department, payroll_payable_strTimesheetStatusList, payroll_payable_notstarted, payroll_payable_saved,
            payroll_payable_submitted, payroll_payable_returned, payroll_payable_approve, payroll_payable_posted, payroll_payable_partialreturn, payroll_payable_partialapprove, payroll_payable_type, payroll_payable_strActiveFlag, timesheet_personId_yn,
            payable_payroll_supervisor_person_id, not_started_color, saved_color, submitted_color, returned_color, approved_color, posted_color, partially_returned_color, partially_approved_color, supervisor_notstarted_yn, supervisor_saved_yn,
            supervisor_submitted_yn,supervisor_returned_yn, supervisor_approved_yn, supervisor_posted_yn, supervisor_partially_returned_yn, supervisor_partially_approved_yn, all_employee_type, sub_updated_employee_name;


    /*
     * making the class singleton will create only one object throughout the application
     ** to make the class singleton, we have to make the constructor of the class private and static
     */
    public static void setInstance(UserSingletonModel instance) {
        UserSingletonModel.instance = instance;
    }

    private static UserSingletonModel instance=null;
    protected UserSingletonModel(){
        // Exists only to defeat instantiation.
    }
    public static UserSingletonModel getInstance(){
        if(instance == null) {
            instance = new UserSingletonModel();
        }
        return instance;

    }


    //===============================Getter method starts=====================

    public String getUserID() {
        return UserID;
    }

    public String getUserName() {
        return UserName;
    }

    public String getCompID() {
        return CompID;
    }

    public String getCorpID() {
        return CorpID;
    }

    public String getCompanyName() {
        return CompanyName;
    }

    public String getSupervisorId() {
        return SupervisorId;
    }

    public String getUserRole() {
        return UserRole;
    }

    public String getAdminYN() {
        return AdminYN;
    }

    public String getPayableClerkYN() {
        return PayableClerkYN;
    }

    public String getSupervisorYN() {
        return SupervisorYN;
    }

    public String getPurchaseYN() {
        return PurchaseYN;
    }

    public String getPayrollClerkYN() {
        return PayrollClerkYN;
    }

    public String getEmpName() {
        return EmpName;
    }

    public String getUserType() {
        return UserType;
    }

    public String getEmailId() {
        return EmailId;
    }

    public String getPwdSetterId() {
        return PwdSetterId;
    }

    public String getFinYearID() {
        return FinYearID;
    }

    public String getMsg() {
        return Msg;
    }

    public String getDayDate() {
        return DayDate;
    }

    public String getPeriodStartDate() {
        return periodStartDate;
    }

    public String getPeriodEndDate() {
        return periodEndDate;
    }

    public String getTimesheetSelectDay_empNote() {
        return timesheetSelectDay_empNote;
    }

    public String getTimesheetSelectDay_supNote() {
        return timesheetSelectDay_supNote;
    }

    public String getColorcode() {
        return colorcode;
    }

    public String getStatusDescription() {
        return statusDescription;
    }

    public String getImagePath() {
        return imagePath;
    }

    public String getTimesheetSelectDate_WeekDate() {
        return timesheetSelectDate_WeekDate;
    }

    public String getEmployeeYN() {
        return employeeYN;
    }

    public String getSupervisor_id_person() {
        return supervisor_id_person;
    }

    public String getSupervisor_employee_name() {
        return supervisor_employee_name;
    }

    public String getSupervisor_department() {
        return supervisor_department;
    }

    public String getPayroll_payable_strTimesheetStatusList() {
        return payroll_payable_strTimesheetStatusList;
    }

    public String getPayroll_payable_notstarted() {
        return payroll_payable_notstarted;
    }

    public String getPayroll_payable_saved() {
        return payroll_payable_saved;
    }

    public String getPayroll_payable_submitted() {
        return payroll_payable_submitted;
    }

    public String getPayroll_payable_returned() {
        return payroll_payable_returned;
    }

    public String getPayroll_payable_approve() {
        return payroll_payable_approve;
    }

    public String getPayroll_payable_posted() {
        return payroll_payable_posted;
    }

    public String getPayroll_payable_partialreturn() {
        return payroll_payable_partialreturn;
    }

    public String getPayroll_payable_partialapprove() {
        return payroll_payable_partialapprove;
    }

    public String getPayroll_payable_type() {
        return payroll_payable_type;
    }

    public String getPayroll_payable_strActiveFlag() {
        return payroll_payable_strActiveFlag;
    }

    public String getTimesheet_personId_yn() {
        return timesheet_personId_yn;
    }

    public String getPayable_payroll_supervisor_person_id() {
        return payable_payroll_supervisor_person_id;
    }

    public String getNot_started_color() {
        return not_started_color;
    }

    public String getSaved_color() {
        return saved_color;
    }

    public String getSubmitted_color() {
        return submitted_color;
    }

    public String getReturned_color() {
        return returned_color;
    }

    public String getApproved_color() {
        return approved_color;
    }

    public String getPosted_color() {
        return posted_color;
    }

    public String getPartially_returned_color() {
        return partially_returned_color;
    }

    public String getPartially_approved_color() {
        return partially_approved_color;
    }

    public String getSupervisor_notstarted_yn() {
        return supervisor_notstarted_yn;
    }

    public String getSupervisor_saved_yn() {
        return supervisor_saved_yn;
    }

    public String getSupervisor_submitted_yn() {
        return supervisor_submitted_yn;
    }

    public String getSupervisor_returned_yn() {
        return supervisor_returned_yn;
    }

    public String getSupervisor_approved_yn() {
        return supervisor_approved_yn;
    }

    public String getSupervisor_posted_yn() {
        return supervisor_posted_yn;
    }

    public String getSupervisor_partially_returned_yn() {
        return supervisor_partially_returned_yn;
    }

    public String getSupervisor_partially_approved_yn() {
        return supervisor_partially_approved_yn;
    }

    public String getAll_employee_type() {
        return all_employee_type;
    }

    public String getSub_updated_employee_name() {
        return sub_updated_employee_name;
    }
    //===============================Getter method ends=====================



    //===============================Setter method starts====================

    public void setUserID(String userID) {
        UserID = userID;
    }

    public void setUserName(String userName) {
        UserName = userName;
    }

    public void setCompID(String compID) {
        CompID = compID;
    }

    public void setCorpID(String corpID) {
        CorpID = corpID;
    }

    public void setCompanyName(String companyName) {
        CompanyName = companyName;
    }

    public void setSupervisorId(String supervisorId) {
        SupervisorId = supervisorId;
    }

    public void setUserRole(String userRole) {
        UserRole = userRole;
    }

    public void setAdminYN(String adminYN) {
        AdminYN = adminYN;
    }

    public void setPayableClerkYN(String payableClerkYN) {
        PayableClerkYN = payableClerkYN;
    }

    public void setSupervisorYN(String supervisorYN) {
        SupervisorYN = supervisorYN;
    }

    public void setPurchaseYN(String purchaseYN) {
        PurchaseYN = purchaseYN;
    }

    public void setPayrollClerkYN(String payrollClerkYN) {
        PayrollClerkYN = payrollClerkYN;
    }

    public void setEmpName(String empName) {
        EmpName = empName;
    }

    public void setUserType(String userType) {
        UserType = userType;
    }

    public void setEmailId(String emailId) {
        EmailId = emailId;
    }

    public void setPwdSetterId(String pwdSetterId) {
        PwdSetterId = pwdSetterId;
    }

    public void setFinYearID(String finYearID) {
        FinYearID = finYearID;
    }

    public void setMsg(String msg) {
        Msg = msg;
    }

    public void setDayDate(String dayDate) {
        DayDate = dayDate;
    }

    public void setPeriodStartDate(String periodStartDate) {
        this.periodStartDate = periodStartDate;
    }

    public void setPeriodEndDate(String periodEndDate) {
        this.periodEndDate = periodEndDate;
    }

    public void setTimesheetSelectDay_empNote(String timesheetSelectDay_empNote) {
        this.timesheetSelectDay_empNote = timesheetSelectDay_empNote;
    }

    public void setTimesheetSelectDay_supNote(String timesheetSelectDay_supNote) {
        this.timesheetSelectDay_supNote = timesheetSelectDay_supNote;
    }

    public void setColorcode(String colorcode) {
        this.colorcode = colorcode;
    }

    public void setStatusDescription(String statusDescription) {
        this.statusDescription = statusDescription;
    }

    public void setImagePath(String imagePath) {
        this.imagePath = imagePath;
    }

    public void setTimesheetSelectDate_WeekDate(String timesheetSelectDate_WeekDate) {
        this.timesheetSelectDate_WeekDate = timesheetSelectDate_WeekDate;
    }

    public void setEmployeeYN(String employeeYN) {
        this.employeeYN = employeeYN;
    }

    public void setSupervisor_id_person(String supervisor_id_person) {
        this.supervisor_id_person = supervisor_id_person;
    }

    public void setSupervisor_employee_name(String supervisor_employee_name) {
        this.supervisor_employee_name = supervisor_employee_name;
    }

    public void setSupervisor_department(String supervisor_department) {
        this.supervisor_department = supervisor_department;
    }

    public void setPayroll_payable_strTimesheetStatusList(String payroll_payable_strTimesheetStatusList) {
        this.payroll_payable_strTimesheetStatusList = payroll_payable_strTimesheetStatusList;
    }

    public void setPayroll_payable_notstarted(String payroll_payable_notstarted) {
        this.payroll_payable_notstarted = payroll_payable_notstarted;
    }

    public void setPayroll_payable_saved(String payroll_payable_saved) {
        this.payroll_payable_saved = payroll_payable_saved;
    }

    public void setPayroll_payable_submitted(String payroll_payable_submitted) {
        this.payroll_payable_submitted = payroll_payable_submitted;
    }

    public void setPayroll_payable_returned(String payroll_payable_returned) {
        this.payroll_payable_returned = payroll_payable_returned;
    }

    public void setPayroll_payable_approve(String payroll_payable_approve) {
        this.payroll_payable_approve = payroll_payable_approve;
    }

    public void setPayroll_payable_posted(String payroll_payable_posted) {
        this.payroll_payable_posted = payroll_payable_posted;
    }

    public void setPayroll_payable_partialreturn(String payroll_payable_partialreturn) {
        this.payroll_payable_partialreturn = payroll_payable_partialreturn;
    }

    public void setPayroll_payable_partialapprove(String payroll_payable_partialapprove) {
        this.payroll_payable_partialapprove = payroll_payable_partialapprove;
    }

    public void setPayroll_payable_type(String payroll_payable_type) {
        this.payroll_payable_type = payroll_payable_type;
    }

    public void setPayroll_payable_strActiveFlag(String payroll_payable_strActiveFlag) {
        this.payroll_payable_strActiveFlag = payroll_payable_strActiveFlag;
    }

    public void setTimesheet_personId_yn(String timesheet_personId_yn) {
        this.timesheet_personId_yn = timesheet_personId_yn;
    }

    public void setPayable_payroll_supervisor_person_id(String payable_payroll_supervisor_person_id) {
        this.payable_payroll_supervisor_person_id = payable_payroll_supervisor_person_id;
    }

    public void setNot_started_color(String not_started_color) {
        this.not_started_color = not_started_color;
    }

    public void setSaved_color(String saved_color) {
        this.saved_color = saved_color;
    }

    public void setSubmitted_color(String submitted_color) {
        this.submitted_color = submitted_color;
    }

    public void setReturned_color(String returned_color) {
        this.returned_color = returned_color;
    }

    public void setApproved_color(String approved_color) {
        this.approved_color = approved_color;
    }

    public void setPosted_color(String posted_color) {
        this.posted_color = posted_color;
    }

    public void setPartially_returned_color(String partially_returned_color) {
        this.partially_returned_color = partially_returned_color;
    }

    public void setPartially_approved_color(String partially_approved_color) {
        this.partially_approved_color = partially_approved_color;
    }

    public void setSupervisor_notstarted_yn(String supervisor_notstarted_yn) {
        this.supervisor_notstarted_yn = supervisor_notstarted_yn;
    }

    public void setSupervisor_saved_yn(String supervisor_saved_yn) {
        this.supervisor_saved_yn = supervisor_saved_yn;
    }

    public void setSupervisor_submitted_yn(String supervisor_submitted_yn) {
        this.supervisor_submitted_yn = supervisor_submitted_yn;
    }

    public void setSupervisor_returned_yn(String supervisor_returned_yn) {
        this.supervisor_returned_yn = supervisor_returned_yn;
    }

    public void setSupervisor_approved_yn(String supervisor_approved_yn) {
        this.supervisor_approved_yn = supervisor_approved_yn;
    }

    public void setSupervisor_posted_yn(String supervisor_posted_yn) {
        this.supervisor_posted_yn = supervisor_posted_yn;
    }

    public void setSupervisor_partially_returned_yn(String supervisor_partially_returned_yn) {
        this.supervisor_partially_returned_yn = supervisor_partially_returned_yn;
    }

    public void setSupervisor_partially_approved_yn(String supervisor_partially_approved_yn) {
        this.supervisor_partially_approved_yn = supervisor_partially_approved_yn;
    }

    public void setAll_employee_type(String all_employee_type) {
        this.all_employee_type = all_employee_type;
    }

    public void setSub_updated_employee_name(String sub_updated_employee_name) {
        this.sub_updated_employee_name = sub_updated_employee_name;
    }
    //===============================Setter method ends====================


}
