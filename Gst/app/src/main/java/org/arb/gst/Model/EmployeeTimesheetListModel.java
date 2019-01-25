package org.arb.gst.Model;

public class EmployeeTimesheetListModel {
    String Contract,ContractID,Task,TaskID,LaborCategory,LaborCategoryID,CostType,CostTypeID,ACSuffix,Hour, AccountCode, Note, editTextValue, editTextAddNote = "";
    Double editTextValueDouble;

    //====================Getter method starts==============

    public String getContract() {
        return Contract;
    }

    public String getContractID() {
        return ContractID;
    }

    public String getTask() {
        return Task;
    }

    public String getTaskID() {
        return TaskID;
    }

    public String getLaborCategory() {
        return LaborCategory;
    }

    public String getLaborCategoryID() {
        return LaborCategoryID;
    }

    public String getCostType() {
        return CostType;
    }

    public String getCostTypeID() {
        return CostTypeID;
    }

    public String getACSuffix() {
        return ACSuffix;
    }

    public String getHour() {
        return Hour;
    }

    public String getAccountCode() {
        return AccountCode;
    }

    public String getNote() {
        return Note;
    }

    public String getEditTextValue() {
        return editTextValue;
    }

    public Double getEditTextValueDouble() {
        return editTextValueDouble;
    }

    public String getEditTextAddNote() {
        return editTextAddNote;
    }
    //====================Getter method ends============

    //===================Setter method starts============

    public void setContract(String contract) {
        Contract = contract;
    }

    public void setContractID(String contractID) {
        ContractID = contractID;
    }

    public void setTask(String task) {
        Task = task;
    }

    public void setTaskID(String taskID) {
        TaskID = taskID;
    }

    public void setLaborCategory(String laborCategory) {
        LaborCategory = laborCategory;
    }

    public void setLaborCategoryID(String laborCategoryID) {
        LaborCategoryID = laborCategoryID;
    }

    public void setCostType(String costType) {
        CostType = costType;
    }

    public void setCostTypeID(String costTypeID) {
        CostTypeID = costTypeID;
    }

    public void setACSuffix(String ACSuffix) {
        this.ACSuffix = ACSuffix;
    }

    public void setHour(String hour) {
        Hour = hour;
    }

    public void setAccountCode(String accountCode) {
        AccountCode = accountCode;
    }

    public void setNote(String note) {
        Note = note;
    }

    public void setEditTextValue(String editTextValue) {
        this.editTextValue = editTextValue;
    }

    public void setEditTextValueDouble(Double editTextValueDouble) {
        this.editTextValueDouble = editTextValueDouble;
    }

    public void setEditTextAddNote(String editTextAddNote) {
        this.editTextAddNote = editTextAddNote;
    }
    //===================Setter method ends=============
}
