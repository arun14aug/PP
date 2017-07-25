package delusion.achievers.pinlesspay.model;

import android.app.Activity;
import android.util.Log;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

import de.greenrobot.event.EventBus;
import delusion.achievers.pinlesspay.utility.PPLog;
import delusion.achievers.pinlesspay.utility.ServiceApi;
import delusion.achievers.pinlesspay.utility.Utils;

/**
 * Created by HP on 22-07-2017.
 */
public class ScheduleManager {

    private String TAG = ScheduleManager.class.getSimpleName();
    private ArrayList<Schedule> scheduleArrayList;

    public ArrayList<Schedule> getSchedules(Activity activity, boolean shouldRefresh) {
        if (shouldRefresh)
            getScheduleList(activity);
        return scheduleArrayList;
    }

    private void getScheduleList(Activity activity) {
        JsonObjectRequest jsonObjReq = new JsonObjectRequest(Request.Method.GET, ServiceApi.SCHEDULES, null,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        Log.e(TAG, "onSuccess  --> " + response.toString());

                        try {

                            boolean state = response.getBoolean("success");
                            if (state) {
                                JSONArray jsonArray = response.getJSONArray("data");
                                if (jsonArray == null)
                                    jsonArray = new JSONArray();
                                int count = jsonArray.length();
                                scheduleArrayList = new ArrayList<>();
                                if (count > 0)
                                    for (int i = 0; i < count; i++) {
                                        Schedule schedule = new Schedule();
                                        //    "$type": "VirtualService.Core.VirtualEntity, VirtualService.3psoft.Core",
//            "ServiceName": "TaskService",
//            "EntityName": "OrganizationSchedule",
//            "Id": 9,
//            "DataBaseAction": 2,
//            "RowNum": 1,
//            "TaskId": 9,
//            "TaskTitle": "Inverse encompassing groupware",
//            "TaskDescription": "However, on the slate. 'Herald, read the accusation!' said the Hatter, and here the conversation",
//            "TaskDate": "07-29-2017",
//            "CreatedOn": "2017-07-19T14:45:50.88",
//            "CreatedBy": "138e31d6-8959-488c-b04e-5e8bab6afde2",
//            "OrganizationID": 8,
//            "IsActive": "Y"

                                        schedule.setType(jsonArray.getJSONObject(i).getString("$type"));
                                        schedule.setId(jsonArray.getJSONObject(i).getString("ServiceName"));
                                        schedule.setType(jsonArray.getJSONObject(i).getString("EntityName"));
                                        schedule.setId(jsonArray.getJSONObject(i).getString("id"));
                                        schedule.setType(jsonArray.getJSONObject(i).getString("DataBaseAction"));
                                        schedule.setId(jsonArray.getJSONObject(i).getString("RowNum"));
                                        schedule.setType(jsonArray.getJSONObject(i).getString("TaskId"));
                                        schedule.setId(jsonArray.getJSONObject(i).getString("TaskTitle"));
                                        schedule.setType(jsonArray.getJSONObject(i).getString("TaskDescription"));
                                        schedule.setId(jsonArray.getJSONObject(i).getString("TaskDate"));
                                        schedule.setType(jsonArray.getJSONObject(i).getString("CreatedOn"));
                                        schedule.setId(jsonArray.getJSONObject(i).getString("CreatedBy"));
                                        schedule.setId(jsonArray.getJSONObject(i).getString("OrganizationID"));
                                        schedule.setId(jsonArray.getJSONObject(i).getString("IsActive"));

                                        scheduleArrayList.add(schedule);
                                    }
                                EventBus.getDefault().post("GetBrandList True");
                            }

                        } catch (JSONException e) {
                            e.printStackTrace();
                            EventBus.getDefault().post("GetBrandList False");
                        }
                    }

                }, new Response.ErrorListener() {

            @Override
            public void onErrorResponse(VolleyError error) {
                PPLog.e("Error Response : ", "Error: " + error.getMessage());
                EventBus.getDefault().post("GetBrandList False");
            }
        });
        RequestQueue requestQueue = Utils.getVolleyRequestQueue(activity);
        requestQueue.add(jsonObjReq);
    }

}
