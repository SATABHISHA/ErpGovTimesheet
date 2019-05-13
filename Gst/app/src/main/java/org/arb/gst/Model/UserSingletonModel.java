package org.arb.gst.Model;

public class UserSingletonModel {
    String UserID, UserName, CompID, CorpID, CompanyName, SupervisorId, UserRole, AdminYN, PayableClerkYN, SupervisorYN, PurchaseYN,
            PayrollClerkYN, EmpName, UserType, EmailId, PwdSetterId, FinYearID, Msg, DayDate, periodStartDate, periodEndDate, timesheetSelectDay_empNote = "", timesheetSelectDay_supNote = "", colorcode = "", statusDescription = "",
            imagePath="",timesheetSelectDate_WeekDate;


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

    //===============================Setter method ends====================


}
