package org.arb.gst.Model;

public class UserSingletonModel {
    String UserID, UserName, CompID, CorpID, CompanyName, SupervisorId, UserRole, AdminYN, PayableClerkYN, SupervisorYN, PurchaseYN,
            PayrollClerkYN, EmpName, UserType, EmailId, PwdSetterId, FinYearID, Msg, DayDate, periodStartDate, periodEndDate, timesheetSelectDay_empNote = "", timesheetSelectDay_supNote = "", colorcode = "", statusDescription = "",
            imagePath="",timesheetSelectDate_WeekDate, employeeYN, supervisor_id_person, supervisor_employee_name, supervisor_department, payroll_payable_strTimesheetStatusList, payroll_payable_notstarted, payroll_payable_saved,
            payroll_payable_submitted, payroll_payable_returned, payroll_payable_approve, payroll_payable_posted, payroll_payable_partialreturn, payroll_payable_partialapprove, payroll_payable_type, payroll_payable_strActiveFlag;


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
    //===============================Setter method ends====================


}
