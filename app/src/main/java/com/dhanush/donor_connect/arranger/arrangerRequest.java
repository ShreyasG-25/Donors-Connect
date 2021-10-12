package com.dhanush.donor_connect.arranger;

public class arrangerRequest {
    public String name_a,email_a,contact_a,contact2_a,address_a,city_a,pin_a,organization_a;

    public arrangerRequest() {
    }

    public arrangerRequest(String name_a, String email_a, String contact_a, String contact2_a, String address_a, String city_a, String pin_a, String organization_a) {
        this.name_a = name_a;
        this.email_a = email_a;
        this.contact_a = contact_a;
        this.contact2_a = contact2_a;
        this.address_a = address_a;
        this.city_a = city_a;
        this.pin_a = pin_a;
        this.organization_a = organization_a;
    }

    public String getName_a() {
        return name_a;
    }

    public void setName_a(String name_a) {
        this.name_a = name_a;
    }

    public String getEmail_a() {
        return email_a;
    }

    public void setEmail_a(String email_a) {
        this.email_a = email_a;
    }

    public String getContact_a() {
        return contact_a;
    }

    public void setContact_a(String contact_a) {
        this.contact_a = contact_a;
    }

    public String getContact2_a() {
        return contact2_a;
    }

    public void setContact2_a(String contact2_a) {
        this.contact2_a = contact2_a;
    }

    public String getAddress_a() {
        return address_a;
    }

    public void setAddress_a(String address_a) {
        this.address_a = address_a;
    }

    public String getCity_a() {
        return city_a;
    }

    public void setCity_a(String city_a) {
        this.city_a = city_a;
    }

    public String getPin_a() {
        return pin_a;
    }

    public void setPin_a(String pin_a) {
        this.pin_a = pin_a;
    }

    public String getOrganization_a() {
        return organization_a;
    }

    public void setOrganization_a(String organization_a) {
        this.organization_a = organization_a;
    }
}
