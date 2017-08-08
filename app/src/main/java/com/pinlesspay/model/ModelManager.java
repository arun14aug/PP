package com.pinlesspay.model;


/*
 * Created by arun on 16/12/15.
 */
public class ModelManager {

    public static ModelManager modelMgr = null;

    private AuthManager authMgr;
    private ScheduleManager scheduleManager;
    private PaymentManager paymentManager;
    private RestOfAllManager restOfAllManager;

    private ModelManager() {

        authMgr = new AuthManager();
        scheduleManager = new ScheduleManager();
        paymentManager = new PaymentManager();
        restOfAllManager = new RestOfAllManager();
    }

    public void clearManagerInstance() {

        this.authMgr = null;
        this.scheduleManager = null;
        this.paymentManager = null;
        this.restOfAllManager = null;
    }

    public static ModelManager getInstance() {
        if (modelMgr == null) {
            modelMgr = new ModelManager();
        }
        return modelMgr;
    }

    public static void setInstance() {
        modelMgr = new ModelManager();
    }

    public static boolean getInstanceModelManager() {
        return modelMgr != null;
    }

    public AuthManager getAuthManager() {

        return this.authMgr;
    }

    public ScheduleManager getScheduleManager() {
        return scheduleManager;
    }

    public PaymentManager getPaymentManager() {
        return paymentManager;
    }

    public RestOfAllManager getRestOfAllManager() {
        return restOfAllManager;
    }
}
