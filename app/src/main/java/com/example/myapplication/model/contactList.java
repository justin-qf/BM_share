package com.example.myapplication.model;

import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;

public class contactList {


    public ArrayList<ContactData> getData() {
        return data;
    }

    public void setData(ArrayList<ContactData> data) {
        this.data = data;
    }

    private ArrayList<ContactData> data;


    public static class ContactData {

        public String getContactName() {
            return contactName;
        }

        public void setContactName(String contactName) {
            this.contactName = contactName;
        }

        public String getContactNumber() {
            return contactNumber;
        }

        public void setContactNumber(String contactNumber) {
            this.contactNumber = contactNumber;
        }

        String contactName;
        String contactNumber;

    }
}
