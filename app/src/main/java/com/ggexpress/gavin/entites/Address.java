package com.ggexpress.gavin.entites;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

/**
 * Created by Gavin on 25/02/20.
 */

public class Address {
    public String pk, user, name, city, street, region, buyerName, mobile, country, title, landMark, lat, lon;
    boolean primary;
    ArrayList<String> addressList = new ArrayList<>();
    JSONObject object;

    public Address() {
    }

    public Address(JSONObject object) throws JSONException {
        this.object = object;

        this.pk = object.getString("pk");
        this.user = object.getString("user");
        this.city = object.getString("city");
        this.street = object.getString("street");
        this.region = object.getString("region");
        this.primary = object.getBoolean("primary");
//        this.buyerName = object.getString("buyerName");
        String mobile = object.getString("mobileNo");
        if (mobile.equals("null") || mobile==null){
            this.mobile = "";
        } else this.mobile = mobile;
        this.country = object.getString("country");
        this.title = object.getString("title");
        this.landMark = object.getString("landMark");
        this.lat = object.getString("lat");
        this.lon = object.getString("lon");

    }

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public String getStreet() {
        return street;
    }

    public void setStreet(String street) {
        this.street = street;
    }


    public String getRegion() {
        return region;
    }

    public void setRegione(String state) {
        this.region = region;
    }

    public String getBuyerName() {
        return buyerName;
    }

    public void setBuyerName(String buyerName) {
        this.buyerName = buyerName;
    }

    public String getMobile() {
        return mobile;
    }

    public void setMobile(String mobile) {
        this.mobile = mobile;
    }

    public String getPk() {
        return pk;
    }

    public void setPk(String pk) {
        this.pk = pk;
    }

    public String getUser() {
        return user;
    }

    public void setUser(String user) {
        this.user = user;
    }

    public String getCountry() {
        return country;
    }

    public void setCountry(String country) {
        this.country = country;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getLandMark() {
        return landMark;
    }

    public void setLandMark(String landMark) {
        this.landMark = landMark;
    }

    public String getLat() {
        return lat;
    }

    public void setLat(String lat) {
        this.lat = lat;
    }

    public String getLon() {
        return lon;
    }

    public void setLon(String lon) {
        this.lon = lon;
    }

    public boolean isPrimary() {
        return primary;
    }

    public void setPrimary(boolean primary) {
        this.primary = primary;
    }

    public void addAddressList(String addresslist) {
        this.addressList.add(addresslist);
    }

    public ArrayList<String> getAddressList(){ return this.addressList; }

}
