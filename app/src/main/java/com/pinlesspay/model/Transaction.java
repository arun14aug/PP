package com.pinlesspay.model;

/*
 * Created by arun.sharma on 8/1/2017.
 */

public class Transaction {

    private String InvoiceNo, TranDate, DonorID, RegisterMobile, OrganizationID, DonationID, DonationName, TranSource, SourceIP, TranAmount, TransactionType, PaymentType, AgentID, CommissionPercent, Fees, CardTypeCode, CardNumber, CardShort, CCardExpMM, CCardExpYYYY, CCProcessorID, CCTranApproved, NameOnCard, Address1, Address2, City, State, Zip, CountryISO3, EmailAddress, PhoneNumber, ProcessorTransID, ProcessorAuthCode, ProcessorAuthMsg, ProcessorAVSCode, ProcessorCVVCode, ProcessorResponseCode, ProcessorErrorCode, RequestID, RequestTokenID, IsVoided, IsFraud, BankRoutingNum, BankAccountNum, BankAccountLastNum, BankAccountType;

    public String getInvoiceNo() {
        return InvoiceNo;
    }

    public void setInvoiceNo(String invoiceNo) {
        InvoiceNo = invoiceNo;
    }

    public String getTranDate() {
        return TranDate;
    }

    public void setTranDate(String tranDate) {
        TranDate = tranDate;
    }

    public String getDonorID() {
        return DonorID;
    }

    public void setDonorID(String donorID) {
        DonorID = donorID;
    }

    public String getRegisterMobile() {
        return RegisterMobile;
    }

    public void setRegisterMobile(String registerMobile) {
        RegisterMobile = registerMobile;
    }

    public String getOrganizationID() {
        return OrganizationID;
    }

    public void setOrganizationID(String organizationID) {
        OrganizationID = organizationID;
    }

    public String getDonationID() {
        return DonationID;
    }

    public void setDonationID(String donationID) {
        DonationID = donationID;
    }

    public String getDonationName() {
        return DonationName;
    }

    public void setDonationName(String donationName) {
        DonationName = donationName;
    }

    public String getTranSource() {
        return TranSource;
    }

    public void setTranSource(String tranSource) {
        TranSource = tranSource;
    }

    public String getSourceIP() {
        return SourceIP;
    }

    public void setSourceIP(String sourceIP) {
        SourceIP = sourceIP;
    }

    public String getTranAmount() {
        return TranAmount;
    }

    public void setTranAmount(String tranAmount) {
        TranAmount = tranAmount;
    }

    public String getTransactionType() {
        return TransactionType;
    }

    public void setTransactionType(String transactionType) {
        TransactionType = transactionType;
    }

    public String getPaymentType() {
        return PaymentType;
    }

    public void setPaymentType(String paymentType) {
        PaymentType = paymentType;
    }

    public String getAgentID() {
        return AgentID;
    }

    public void setAgentID(String agentID) {
        AgentID = agentID;
    }

    public String getCommissionPercent() {
        return CommissionPercent;
    }

    public void setCommissionPercent(String commissionPercent) {
        CommissionPercent = commissionPercent;
    }

    public String getFees() {
        return Fees;
    }

    public void setFees(String fees) {
        Fees = fees;
    }

    public String getCardTypeCode() {
        return CardTypeCode;
    }

    public void setCardTypeCode(String cardTypeCode) {
        CardTypeCode = cardTypeCode;
    }

    public String getCardNumber() {
        return CardNumber;
    }

    public void setCardNumber(String cardNumber) {
        CardNumber = cardNumber;
    }

    public String getCardShort() {
        return CardShort;
    }

    public void setCardShort(String cardShort) {
        CardShort = cardShort;
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

    public String getCCProcessorID() {
        return CCProcessorID;
    }

    public void setCCProcessorID(String CCProcessorID) {
        this.CCProcessorID = CCProcessorID;
    }

    public String getCCTranApproved() {
        return CCTranApproved;
    }

    public void setCCTranApproved(String CCTranApproved) {
        this.CCTranApproved = CCTranApproved;
    }

    public String getNameOnCard() {
        return NameOnCard;
    }

    public void setNameOnCard(String nameOnCard) {
        NameOnCard = nameOnCard;
    }

    public String getAddress1() {
        return Address1;
    }

    public void setAddress1(String address1) {
        Address1 = address1;
    }

    public String getAddress2() {
        return Address2;
    }

    public void setAddress2(String address2) {
        Address2 = address2;
    }

    public String getCity() {
        return City;
    }

    public void setCity(String city) {
        City = city;
    }

    public String getState() {
        return State;
    }

    public void setState(String state) {
        State = state;
    }

    public String getZip() {
        return Zip;
    }

    public void setZip(String zip) {
        Zip = zip;
    }

    public String getCountryISO3() {
        return CountryISO3;
    }

    public void setCountryISO3(String countryISO3) {
        CountryISO3 = countryISO3;
    }

    public String getEmailAddress() {
        return EmailAddress;
    }

    public void setEmailAddress(String emailAddress) {
        EmailAddress = emailAddress;
    }

    public String getPhoneNumber() {
        return PhoneNumber;
    }

    public void setPhoneNumber(String phoneNumber) {
        PhoneNumber = phoneNumber;
    }

    public String getProcessorTransID() {
        return ProcessorTransID;
    }

    public void setProcessorTransID(String processorTransID) {
        ProcessorTransID = processorTransID;
    }

    public String getProcessorAuthCode() {
        return ProcessorAuthCode;
    }

    public void setProcessorAuthCode(String processorAuthCode) {
        ProcessorAuthCode = processorAuthCode;
    }

    public String getProcessorAuthMsg() {
        return ProcessorAuthMsg;
    }

    public void setProcessorAuthMsg(String processorAuthMsg) {
        ProcessorAuthMsg = processorAuthMsg;
    }

    public String getProcessorAVSCode() {
        return ProcessorAVSCode;
    }

    public void setProcessorAVSCode(String processorAVSCode) {
        ProcessorAVSCode = processorAVSCode;
    }

    public String getProcessorCVVCode() {
        return ProcessorCVVCode;
    }

    public void setProcessorCVVCode(String processorCVVCode) {
        ProcessorCVVCode = processorCVVCode;
    }

    public String getProcessorResponseCode() {
        return ProcessorResponseCode;
    }

    public void setProcessorResponseCode(String processorResponseCode) {
        ProcessorResponseCode = processorResponseCode;
    }

    public String getProcessorErrorCode() {
        return ProcessorErrorCode;
    }

    public void setProcessorErrorCode(String processorErrorCode) {
        ProcessorErrorCode = processorErrorCode;
    }

    public String getRequestID() {
        return RequestID;
    }

    public void setRequestID(String requestID) {
        RequestID = requestID;
    }

    public String getRequestTokenID() {
        return RequestTokenID;
    }

    public void setRequestTokenID(String requestTokenID) {
        RequestTokenID = requestTokenID;
    }

    public String getIsVoided() {
        return IsVoided;
    }

    public void setIsVoided(String isVoided) {
        IsVoided = isVoided;
    }

    public String getIsFraud() {
        return IsFraud;
    }

    public void setIsFraud(String isFraud) {
        IsFraud = isFraud;
    }

    public String getBankRoutingNum() {
        return BankRoutingNum;
    }

    public void setBankRoutingNum(String bankRoutingNum) {
        BankRoutingNum = bankRoutingNum;
    }

    public String getBankAccountNum() {
        return BankAccountNum;
    }

    public void setBankAccountNum(String bankAccountNum) {
        BankAccountNum = bankAccountNum;
    }

    public String getBankAccountLastNum() {
        return BankAccountLastNum;
    }

    public void setBankAccountLastNum(String bankAccountLastNum) {
        BankAccountLastNum = bankAccountLastNum;
    }

    public String getBankAccountType() {
        return BankAccountType;
    }

    public void setBankAccountType(String bankAccountType) {
        BankAccountType = bankAccountType;
    }
}
