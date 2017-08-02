package com.pinlesspay.model;

/*
 * Created by HP on 22-07-2017.
 */
public class Recurring {

    //    "ServiceName": "RecurringService",
    //                                                    "EntityName": "DonationSchedule",
//                                                    "Id": 8,
//                                                    "DataBaseAction": 2,
//                                                    "RowNum": 1,
//                                                    "DonationName": "Self-enabling attitude-oriented systemengine",
//                                                    "AccountType": "CARD",
//                                                    "PaymentFrom": "5454XXXXXXXX5454",
//                                                    "CardType": "MasterCard",
//                                                    "NextScheduleRunDate": "09 Aug 2017",
//                                                    "ScheduleName": "Every 2 Week",
//                                                    "DonationAmount": 19.99,
//                                                    "DonorID": 39
    private String ServiceName, EntityName, Id, DataBaseAction, RowNum, DonationName, AccountType, PaymentFrom,
            NextScheduleRunDate, ScheduleName, DonationAmount, DonorID, CardType;

    public String getCardType() {
        return CardType;
    }

    public void setCardType(String cardType) {
        CardType = cardType;
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

    public String getRowNum() {
        return RowNum;
    }

    public void setRowNum(String rowNum) {
        RowNum = rowNum;
    }

    public String getDonationName() {
        return DonationName;
    }

    public void setDonationName(String donationName) {
        DonationName = donationName;
    }

    public String getAccountType() {
        return AccountType;
    }

    public void setAccountType(String accountType) {
        AccountType = accountType;
    }

    public String getPaymentFrom() {
        return PaymentFrom;
    }

    public void setPaymentFrom(String paymentFrom) {
        PaymentFrom = paymentFrom;
    }

    public String getNextScheduleRunDate() {
        return NextScheduleRunDate;
    }

    public void setNextScheduleRunDate(String nextScheduleRunDate) {
        NextScheduleRunDate = nextScheduleRunDate;
    }

    public String getScheduleName() {
        return ScheduleName;
    }

    public void setScheduleName(String scheduleName) {
        ScheduleName = scheduleName;
    }

    public String getDonationAmount() {
        return DonationAmount;
    }

    public void setDonationAmount(String donationAmount) {
        DonationAmount = donationAmount;
    }

    public String getDonorID() {
        return DonorID;
    }

    public void setDonorID(String donorID) {
        DonorID = donorID;
    }
}
