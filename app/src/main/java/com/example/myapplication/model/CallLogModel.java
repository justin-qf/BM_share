package com.example.myapplication.model;

public class CallLogModel {
    public void setPhNumber(String phNumber) {
        this.phNumber = phNumber;
    }

    public void setContactName(String contactName) {
        this.contactName = contactName;
    }

    public void setCallType(String callType) {
        this.callType = callType;
    }

    public void setCallDate(String callDate) {
        this.callDate = callDate;
    }

    public void setCallTime(String callTime) {
        this.callTime = callTime;
    }

    public void setCallDuration(String callDuration) {
        this.callDuration = callDuration;
    }

    String phNumber, contactName, callType, callDate, callTime, callDuration;

    public CallLogModel() {
        this.phNumber = phNumber;
        this.contactName = contactName;
        this.callType = callType;
        this.callDate = callDate;
        this.callTime = callTime;
        this.callDuration = callDuration;

    }

    public String getPhNumber() {
        return phNumber;
    }

    public String getContactName() {
        return contactName;
    }

    public String getCallType() {
        return callType;
    }

    public String getCallDate() {
        return callDate;
    }

    public String getCallTime() {
        return callTime;
    }

    public String getCallDuration() {
        return callDuration;
    }
}
