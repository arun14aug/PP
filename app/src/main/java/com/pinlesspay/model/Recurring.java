package com.pinlesspay.model;

/*
 * Created by HP on 22-07-2017.
 */
public class Recurring {
    private String DonorScheduleId, DonationName, AccountType, PaymentFrom, CardType, MaskCardNumber, ScheduleStartDate, DonationScheduleId, NextScheduleRunDate, LastScheduleRunDate, LastRunStatus, IsActive;

    public String getDonorScheduleId() {
        return DonorScheduleId;
    }

    public void setDonorScheduleId(String donorScheduleId) {
        DonorScheduleId = donorScheduleId;
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

    public String getCardType() {
        return CardType;
    }

    public void setCardType(String cardType) {
        CardType = cardType;
    }

    public String getMaskCardNumber() {
        return MaskCardNumber;
    }

    public void setMaskCardNumber(String maskCardNumber) {
        MaskCardNumber = maskCardNumber;
    }

    public String getScheduleStartDate() {
        return ScheduleStartDate;
    }

    public void setScheduleStartDate(String scheduleStartDate) {
        ScheduleStartDate = scheduleStartDate;
    }

    public String getDonationScheduleId() {
        return DonationScheduleId;
    }

    public void setDonationScheduleId(String donationScheduleId) {
        DonationScheduleId = donationScheduleId;
    }

    public String getNextScheduleRunDate() {
        return NextScheduleRunDate;
    }

    public void setNextScheduleRunDate(String nextScheduleRunDate) {
        NextScheduleRunDate = nextScheduleRunDate;
    }

    public String getLastScheduleRunDate() {
        return LastScheduleRunDate;
    }

    public void setLastScheduleRunDate(String lastScheduleRunDate) {
        LastScheduleRunDate = lastScheduleRunDate;
    }

    public String getLastRunStatus() {
        return LastRunStatus;
    }

    public void setLastRunStatus(String lastRunStatus) {
        LastRunStatus = lastRunStatus;
    }

    public String getIsActive() {
        return IsActive;
    }

    public void setIsActive(String isActive) {
        IsActive = isActive;
    }
}
