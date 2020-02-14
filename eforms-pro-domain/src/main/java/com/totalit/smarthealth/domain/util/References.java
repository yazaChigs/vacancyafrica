package com.totalit.smarthealth.domain.util;

import org.json.JSONObject;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

@Document
public class References {

//    @Id
//    private String id;
//    private JSONObject details;
//
//    public String getId() {
//        return id;
//    }
//
//    public void setId(String id) {
//        this.id = id;
//    }
//
//    public JSONObject getDetails() {
//        return details;
//    }
//
//    public void setDetails(JSONObject details) {
//        this.details = details;
//    }

    private String refName;
    private String refPhone;
    private String refEmail;
    private String refAddress;

    public String getRefName() {
        return refName;
    }

    public void setRefName(String refName) {
        this.refName = refName;
    }

    public String getRefPhone() {
        return refPhone;
    }

    public void setRefPhone(String refPhone) {
        this.refPhone = refPhone;
    }

    public String getRefEmail() {
        return refEmail;
    }

    public void setRefEmail(String refEmail) {
        this.refEmail = refEmail;
    }

    public String getRefAddress() {
        return refAddress;
    }

    public void setRefAddress(String refAddress) {
        this.refAddress = refAddress;
    }
}
