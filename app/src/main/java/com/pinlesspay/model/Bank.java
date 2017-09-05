package com.pinlesspay.model;

/*
 * Created by arun.sharma on 8/2/2017.
 */

public class Bank {

    private String ServiceName, EntityName, Id, DataBaseAction, AccountId, BankAccountType, MaskBankRoutingNum,
            MaskBankAccountNum, NickName, IsDefault, FirstName, LastName, RepeatBankAccountNum;

    public String getRepeatBankAccountNum() {
        return RepeatBankAccountNum;
    }

    public void setRepeatBankAccountNum(String repeatBankAccountNum) {
        RepeatBankAccountNum = repeatBankAccountNum;
    }

    public String getFirstName() {
        return FirstName;
    }

    public void setFirstName(String firstName) {
        FirstName = firstName;
    }

    public String getLastName() {
        return LastName;
    }

    public void setLastName(String lastName) {
        LastName = lastName;
    }

    public String getServiceName() {
        return ServiceName;
    }

    public void setServiceName(String serviceName) {
        ServiceName = serviceName;
    }

    public String getEntityName() {
        return EntityName;
    }

    public void setEntityName(String entityName) {
        EntityName = entityName;
    }

    public String getId() {
        return Id;
    }

    public void setId(String id) {
        Id = id;
    }

    public String getDataBaseAction() {
        return DataBaseAction;
    }

    public void setDataBaseAction(String dataBaseAction) {
        DataBaseAction = dataBaseAction;
    }

    public String getAccountId() {
        return AccountId;
    }

    public void setAccountId(String accountId) {
        AccountId = accountId;
    }

    public String getBankAccountType() {
        return BankAccountType;
    }

    public void setBankAccountType(String bankAccountType) {
        BankAccountType = bankAccountType;
    }

    public String getMaskBankRoutingNum() {
        return MaskBankRoutingNum;
    }

    public void setMaskBankRoutingNum(String maskBankRoutingNum) {
        MaskBankRoutingNum = maskBankRoutingNum;
    }

    public String getMaskBankAccountNum() {
        return MaskBankAccountNum;
    }

    public void setMaskBankAccountNum(String maskBankAccountNum) {
        MaskBankAccountNum = maskBankAccountNum;
    }

    public String getNickName() {
        return NickName;
    }

    public void setNickName(String nickName) {
        NickName = nickName;
    }

    public String getIsDefault() {
        return IsDefault;
    }

    public void setIsDefault(String isDefault) {
        IsDefault = isDefault;
    }
}
