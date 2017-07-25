package delusion.achievers.pinlesspay.model;


/*
 * Created by arun on 16/12/15.
 */
public class ModelManager {

    public static ModelManager modelMgr = null;

    private AuthManager authMgr;
    private ScheduleManager scheduleManager;

    private ModelManager() {

        authMgr = new AuthManager();
        scheduleManager = new ScheduleManager();
    }

    public void clearManagerInstance() {

        this.authMgr = null;
        this.scheduleManager = null;
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
}
