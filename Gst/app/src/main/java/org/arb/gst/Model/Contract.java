package org.arb.gst.Model;

public class Contract {
    String ContractID,ContractCode,ContractDescription,ContractFlag;

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

    //================Setter methods ends=================
}
