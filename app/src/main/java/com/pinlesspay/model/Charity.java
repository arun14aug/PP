package com.pinlesspay.model;

/*
 * Created by HP on 22-07-2017.
 */
public class Charity {
//                                        {
//                                                "ServiceName": "AboutusService",
//                                                "EntityName": "AboutUs",
//                                                "Id": null,
//                                                "DataBaseAction": 2,
//                                                "AboutUsId": 1,
//                                                "Description": "Tell%20her%20to%20wink%20with%20one%20finger%3B%20and%20the%20March%20Hare.",
//                                                "CreatedOn": "2017-07-14T15:56:11.983",
//                                                "CreatedBy": "138e31d6-8959-488c-b04e-5e8bab6afde2",
//                                                "IsActive": "N"
//                                        }

    private String ServiceName, EntityName, Id, DataBaseAction, AboutUsId, Description,
            CreatedOn, CreatedBy, IsActive;

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

    public String getIsActive() {
        return IsActive;
    }

    public void setIsActive(String isActive) {
        IsActive = isActive;
    }

    public String getAboutUsId() {
        return AboutUsId;
    }

    public void setAboutUsId(String aboutUsId) {
        AboutUsId = aboutUsId;
    }

    public String getDescription() {
        return Description;
    }

    public void setDescription(String description) {
        Description = description;
    }
}
