package com.dhanush.donor_connect.arranger.uiArranger.home;

public class BloodRequest {
    private String id_r;
    private String blood_r;
    private String unit_r;
    private String hospital_r;
    private String name_r;
    private String last_r;
    private String contact_r;
    private String city_r;
    private String arrangement_r;

    public BloodRequest() {
    }

    public BloodRequest(String id_r,String blood_r, String unit_r, String hospital_r, String name_r, String last_r,String contact_r,String city_r,String arrangement_r) {
        this.id_r=id_r;
        this.blood_r = blood_r;
        this.unit_r = unit_r;
        this.hospital_r = hospital_r;
        this.name_r = name_r;
        this.last_r = last_r;
        this.contact_r=contact_r;
        this.city_r=city_r;
        this.arrangement_r=arrangement_r;
    }

    public String getId_r() { return id_r; }

    public void setId_r(String id_r) { this.id_r = id_r; }

    public String getBlood_r() {
        return blood_r;
    }

    public void setBlood_r(String blood_r) {
        this.blood_r = blood_r;
    }

    public String getUnit_r() {
        return unit_r;
    }

    public void setUnit_r(String unit_r) {
        this.unit_r = unit_r;
    }

    public String getHospital_r() {
        return hospital_r;
    }

    public void setHospital_r(String hospital_r) {
        this.hospital_r = hospital_r;
    }

    public String getName_r() {
        return name_r;
    }

    public void setName_r(String name_r) {
        this.name_r = name_r;
    }

    public String getLast_r() {
        return last_r;
    }

    public void setLast_r(String last_r) {
        this.last_r = last_r;
    }

    public String getContact_r() {
        return contact_r;
    }

    public void setContact_r(String contact_r) {
        this.contact_r = contact_r;
    }

    public String getCity_r() { return city_r; }

    public void setCity_r(String city_r) { this.city_r = city_r; }

    public String getArrangement_r() {
        return arrangement_r;
    }

    public void setArrangement_r(String arrangement_r) {
        this.arrangement_r = arrangement_r;
    }
}
