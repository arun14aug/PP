package com.pinlesspay.utility;

/*
 * Created by arun on 5/3/15.
 */
public class ServiceApi {

    private static final String baseurl = "http://pinlesspay.edu360.guru/api/DonorAuth/"; //live..

    public static final String ORGANISATION_KEY = "17CB139A-341C-4D29-BC01-32CE06D445F4"; //live..
    public static final String PAGE_SIZE = "50";

    // Users Family
//    public static final String LOGIN = baseurl + "auth/securelogin";
    public static final String LOGIN = baseurl + "createorganizationdonor";
    public static final String REGISTER = baseurl + "createorganizationdonor";
    public static final String VERIFY_USER = baseurl + "VerifyOrganizationDonor";
    public static final String FORGOT_PASSWORD = baseurl + "recoverpassword";
    public static final String SCHEDULES = baseurl + "Execute";
    public static final String PROFILE = baseurl + "Process";

    public static final String PROCESS_CREDIT_CARD = baseurl + "ProcessCreditCard";
    public static final String PROCESS_BANK_ACCOUNT = baseurl + "ProcessBankAccount";
    public static final String GET_ALL_DATA = baseurl + "GetAllData";
}
