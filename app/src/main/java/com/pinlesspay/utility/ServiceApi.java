package com.pinlesspay.utility;

/*
 * Created by arun on 5/3/15.
 */
public class ServiceApi {

//    private static final String baseurl = "https://www.pinlesspay.com/api/DonorAuth/"; //live..
    private static final String baseurl = "http://pinlesspay.edu360.guru/api/DonorAuth/"; //local..

//    public static final String ORGANISATION_KEY = "34817348-479E-4DDA-8B41-7EC85DDB92A9"; //live..
    public static final String ORGANISATION_KEY = "17CB139A-341C-4D29-BC01-32CE06D445F4"; //local..
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
    public static final String DELETE_RECURRING_SCHEDULE = baseurl + "Process"; // Action "DeleteRecuSchedule"
    public static final String RESEND_OTP = baseurl + "Process"; // Action "resenddonorotp"
    public static final String GET_DONOR_DEVICES = baseurl + "GetAllData"; // Action "GetDevices"
    public static final String POST_TICKET = baseurl + "PostTicket"; // Action "PostTicket"
    public static final String POST_TICKET_REPLY = baseurl + "PostTicketReply"; // Action "PostTicketReply"
    public static final String GET_ALL_TICKETS = baseurl + "GetAllData"; // Action "GetAllTickets"
    public static final String GET_TICKET_DETAIL = baseurl + "GetAllData"; // Action "GetTicketDetail"
    public static final String POST_SUGGESTIONS = baseurl + "PostSuggestion"; // Action "PostSuggestion"
    public static final String ADD_RECURRING_SCHEDULE = baseurl + "Process"; // Action "AddRecuSchedule"
    public static final String DELETE_DONOR_ACOUNT = baseurl + "Process"; // Action "DeleteDonorAccount"
    public static final String DELETE_DONOR_DEVICE = baseurl + "Process"; // Action "DeleteDonorDevice"
    public static final String UPDATE_CREDIT_CARD = baseurl + "Process"; // Action "UpdateCreditCard"
    public static final String UPDATE_BANK_ACCOUNT = baseurl + "Process"; // Action "UpdateBankAccount"
    public static final String GET_DONATION_FREQUENCY = baseurl + "GetAllData"; // Action "GetDonationFrequency"
    public static final String GET_DONATION_CAUSE_LIST = baseurl + "GetAllData"; // Action "GetDonationCauseList"
    public static final String GET_DONOR_PAYMENT_ACCOUNTS = baseurl + "GetAllData"; // Action "GetDonorPaymentAccounts"
    public static final String SAVE_DONATION_SCHEDULE = baseurl + "Process"; // Action "SaveDonationSchedule"
    public static final String POST_PUSH_NOTIFY = baseurl + "Process"; // Action "PostPushNtfy"
    public static final String DELETE_PUSH_NOTIFY = baseurl + "Process"; // Action "DeletePushNtfy"

//    public static final String MAKE_DONATION = "https://www.pinlesspay.com/donate?token="; // live...
        public static final String MAKE_DONATION = "http://pinlesspay.edu360.guru/donate?token="; // local....
    public static final String ADD_RECURRING_TAG = "&r=1";

    public static final String DONATION_LOGO_URL = "https://www.pinlesspay.com/Content/UploadedFiles/OrgLogo/34817348-479E-4DDA-8B41-7EC85DDB92A9_a.png";


    // global topic to receive app wide push notifications
    public static final String TOPIC_GLOBAL = "global";

    // broadcast receiver intent filters
    public static final String REGISTRATION_COMPLETE = "registrationComplete";
    public static final String PUSH_NOTIFICATION = "pushNotification";

    // id to handle the notification in the notification tray
    public static final int NOTIFICATION_ID = 100;
    public static final int NOTIFICATION_ID_BIG_IMAGE = 101;
}
