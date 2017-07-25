package delusion.achievers.pinlesspay.utility;

/*
 * Created by arun on 5/3/15.
 */
public class ServiceApi {

    public static final String baseurl = "http://mysteelhub.com/"; //staging..


    // Users Family
//    public static final String LOGIN = baseurl + "auth/securelogin";
    public static final String LOGIN = baseurl + "VerifyOrganizationDonor";
    public static final String REGISTER = baseurl + "createorganizationdonor";
    public static final String VERIFY_USER = baseurl + "VerifyOrganizationDonor";
    public static final String FORGOT_PASSWORD = baseurl + "recoverpassword";
    public static final String CHANGE_PASSWORD = baseurl + "auth/changepassword";
    public static final String SCHEDULES = "Execute";
    public static final String PROFILE = baseurl + "Process";
    public static final String LOGOUT = baseurl + "auth/logout";

    public static final String PROCESS_CREDIT_CARD = baseurl + "ProcessCreditCard";
    public static final String PROCESS_BANK_ACCOUNT = baseurl + "ProcessBankAccount";
    public static final String GET_ALL_DATA = baseurl + "GetAllData";
}
