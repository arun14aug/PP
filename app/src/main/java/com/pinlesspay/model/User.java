package com.pinlesspay.model;

/*
 * Created by arun.sharma on 4/25/2017.
 */

public class User {
//                                            "ServiceName": null,
//                                                    "EntityName": null,
//                                                    "Id": null,
//                                                    "DataBaseAction": 2,
//                                                    "DonorId": 39,
//                                                    "RegisterMobile": "918699155321",

    private String id, FirstName, LastName, Email, MobileNo, Address1, Address2,
            State, City, Zip, Country, ServiceName,EntityName, DataBaseAction, DonorId, RegisterMobile;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
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

    public String getEmail() {
        return Email;
    }

    public void setEmail(String email) {
        Email = email;
    }

    public String getMobileNo() {
        return MobileNo;
    }

    public void setMobileNo(String mobileNo) {
        MobileNo = mobileNo;
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

    public String getState() {
        return State;
    }

    public void setState(String state) {
        State = state;
    }

    public String getCity() {
        return City;
    }

    public void setCity(String city) {
        City = city;
    }

    public String getZip() {
        return Zip;
    }

    public void setZip(String zip) {
        Zip = zip;
    }

    public String getCountry() {
        return Country;
    }

    public void setCountry(String country) {
        Country = country;
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

    public String getDataBaseAction() {
        return DataBaseAction;
    }

    public void setDataBaseAction(String dataBaseAction) {
        DataBaseAction = dataBaseAction;
    }

    public String getDonorId() {
        return DonorId;
    }

    public void setDonorId(String donorId) {
        DonorId = donorId;
    }

    public String getRegisterMobile() {
        return RegisterMobile;
    }

    public void setRegisterMobile(String registerMobile) {
        RegisterMobile = registerMobile;
    }
}
