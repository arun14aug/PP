package com.pinlesspay.model;

/**
 * Created by HP on 22-07-2017.
 */
public class Schedule {
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

    private String type, ServiceName, EntityName, Id, DataBaseAction, RowNum, TaskId, TaskTitle, TaskDescription, TaskDate,
            CreatedOn, CreatedBy, OrganizationID, IsActive;

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
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

    public String getTaskId() {
        return TaskId;
    }

    public void setTaskId(String taskId) {
        TaskId = taskId;
    }

    public String getTaskTitle() {
        return TaskTitle;
    }

    public void setTaskTitle(String taskTitle) {
        TaskTitle = taskTitle;
    }

    public String getTaskDescription() {
        return TaskDescription;
    }

    public void setTaskDescription(String taskDescription) {
        TaskDescription = taskDescription;
    }

    public String getTaskDate() {
        return TaskDate;
    }

    public void setTaskDate(String taskDate) {
        TaskDate = taskDate;
    }

    public String getCreatedOn() {
        return CreatedOn;
    }

    public void setCreatedOn(String createdOn) {
        CreatedOn = createdOn;
    }

    public String getCreatedBy() {
        return CreatedBy;
    }

    public void setCreatedBy(String createdBy) {
        CreatedBy = createdBy;
    }

    public String getOrganizationID() {
        return OrganizationID;
    }

    public void setOrganizationID(String organizationID) {
        OrganizationID = organizationID;
    }

    public String getIsActive() {
        return IsActive;
    }

    public void setIsActive(String isActive) {
        IsActive = isActive;
    }
}
