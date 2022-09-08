package com.ggexpress.gavin.entites;

import org.json.JSONException;
import org.json.JSONObject;

/**
 * Created by Gavin on 01/03/21.
 */

public class Product {
    public String GY, name, type;
    public JSONObject object;

    public Product(JSONObject object) throws JSONException {
        this.object = object;

        this.GY = object.getString("GY");
        this.name = object.getString("name");
        this.type = object.getString("typ");

    }

    public String getGY() {
        return GY;
    }

    public void setGY(String GY) {
        this.GY = GY;
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
