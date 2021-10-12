package com.dhanush.donor_connect.donorPack;

public class donor {

    public String fullname_d,gender_d,dob_d,bloodtype_d,phone_d,email_d,address_d,city_d,pin_d,last_d;

    public donor() {

    }

    public donor(String fullname_d, String gender_d, String dob_d, String bloodtype_d, String phone_d, String email_d, String address_d, String city_d, String pin_d,String last_d) {
        this.fullname_d = fullname_d;
        this.gender_d = gender_d;
        this.dob_d = dob_d;
        this.bloodtype_d = bloodtype_d;
        this.phone_d = phone_d;
        this.email_d = email_d;
        this.address_d = address_d;
        this.city_d = city_d;
        this.pin_d = pin_d;
        this.last_d = last_d;
    }

    public String getFullname_d() {
        return fullname_d;
    }

    public void setFullname_d(String fullname_d) {
        this.fullname_d = fullname_d;
    }

    public String getGender_d() {
        return gender_d;
    }

    public void setGender_d(String gender_d) {
        this.gender_d = gender_d;
    }

    public String getDob_d() {
        return dob_d;
    }

    public void setDob_d(String dob_d) {
        this.dob_d = dob_d;
    }

    public String getBloodtype_d() {
        return bloodtype_d;
    }

    public void setBloodtype_d(String bloodtype_d) {
        this.bloodtype_d = bloodtype_d;
    }

    public String getPhone_d() {
        return phone_d;
    }

    public void setPhone_d(String phone_d) {
        this.phone_d = phone_d;
    }

    public String getEmail_d() {
        return email_d;
    }

    public void setEmail_d(String email_d) {
        this.email_d = email_d;
    }

    public String getAddress_d() {
        return address_d;
    }

    public void setAddress_d(String address_d) {
        this.address_d = address_d;
    }

    public String getCity_d() {
        return city_d;
    }

    public void setCity_d(String city_d) {
        this.city_d = city_d;
    }

    public String getPin_d() {
        return pin_d;
    }

    public void setPin_d(String pin_d) {
        this.pin_d = pin_d;
    }

    public String getLast_d() {
        return last_d;
    }

    public void setLast_d(String last_d) {
        this.last_d = last_d;
    }
}
