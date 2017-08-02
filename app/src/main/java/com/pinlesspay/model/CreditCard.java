package com.pinlesspay.model;

/*
 * Created by arun.sharma on 8/2/2017.
 */

public class CreditCard {
    private String ServiceName, EntityName, Id, DataBaseAction, AccountId, CardTypeCode, MaskCardNumber,
            CCardExpMM, CCardExpYYYY, NickName, IsDefault;

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

    public String getCardTypeCode() {
        return CardTypeCode;
    }

    public void setCardTypeCode(String cardTypeCode) {
        CardTypeCode = cardTypeCode;
    }

    public String getMaskCardNumber() {
        return MaskCardNumber;
    }

    public void setMaskCardNumber(String maskCardNumber) {
        MaskCardNumber = maskCardNumber;
    }

    public String getCCardExpMM() {
        return CCardExpMM;
    }

    public void setCCardExpMM(String CCardExpMM) {
        this.CCardExpMM = CCardExpMM;
    }

    public String getCCardExpYYYY() {
        return CCardExpYYYY;
    }

    public void setCCardExpYYYY(String CCardExpYYYY) {
        this.CCardExpYYYY = CCardExpYYYY;
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
