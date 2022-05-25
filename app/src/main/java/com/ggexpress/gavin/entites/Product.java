package com.ggexpress.gavin.entites;

import org.json.JSONException;
import org.json.JSONObject;

/**
 * Created by Gavin on 01/03/20.
 */

public class Product {
    public String pk, name, type;
    public JSONObject object;

    public Product(JSONObject object) throws JSONException {
        this.object = object;

        this.pk = object.getString("pk");
        this.name = object.getString("name");
        this.type = object.getString("typ");

    }

    public String getPk() {
        return pk;
    }

    public void setPk(String pk) {
        this.pk = pk;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public JSONObject getObject() {
        return object;
    }

    public void setObject(JSONObject object) {
        this.object = object;
    }
}
