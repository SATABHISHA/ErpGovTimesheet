package org.arb.gst.Model;

public class UserUpdateHoursModel {
    String ContractID,ContractCode,ContractDescription,ContractFlag,TaskID,TaskCode,TaskDescription,LaborCatgryID,LaborCatgryCode,LaborCatgryDescription, editTextValue;

    //================Getter methods starts=================
    public String getContractID() {
        return ContractID;
    }

    public String getContractCode() {
        return ContractCode;
    }

    public String getContractDescription() {
        return ContractDescription;
    }

    public String getContractFlag() {
        return ContractFlag;
    }

    public String getTaskID() {
        return TaskID;
    }

    public String getTaskCode() {
        return TaskCode;
    }

    public String getTaskDescription() {
        return TaskDescription;
    }

    public String getLaborCatgryID() {
        return LaborCatgryID;
    }

    public String getLaborCatgryCode() {
        return LaborCatgryCode;
    }

    public String getLaborCatgryDescription() {
        return LaborCatgryDescription;
    }

    public String getEditTextValue() {
        return editTextValue;
    }
    //================Getter methods ends=================

    //================Setter methods starts=================

    public void setContractID(String contractID) {
        ContractID = contractID;
    }

    public void setContractCode(String contractCode) {
        ContractCode = contractCode;
    }

    public void setContractDescription(String contractDescription) {
        ContractDescription = contractDescription;
    }

    public void setContractFlag(String contractFlag) {
        ContractFlag = contractFlag;
    }

    public void setTaskID(String taskID) {
        TaskID = taskID;
    }

    public void setTaskCode(String taskCode) {
        TaskCode = taskCode;
    }

    public void setTaskDescription(String taskDescription) {
        TaskDescription = taskDescription;
    }

    public void setLaborCatgryID(String laborCatgryID) {
        LaborCatgryID = laborCatgryID;
    }

    public void setLaborCatgryCode(String laborCatgryCode) {
        LaborCatgryCode = laborCatgryCode;
    }

    public void setLaborCatgryDescription(String laborCatgryDescription) {
        LaborCatgryDescription = laborCatgryDescription;
    }

    public void setEditTextValue(String editTextValue) {
        this.editTextValue = editTextValue;
    }
    //================Setter methods ends=================
}
