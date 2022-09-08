package com.ggexpress.gavin.entites;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

/**
 * Created by Gavin on 28/02/21.
 */

public class Generic {
    public String GY, name, minCost, visual;
    public String fieldGY, fieldName, fieldType, unit, helpText, fieldData, fieldDefault;
    public String parentGY, parentName, parentMinCost, parentVisual;

    JSONObject object;


    public Generic() {

    }

    public Generic(JSONObject object) {
        this.object = object;

        try {
            this.GY = object.getString("GY");
            this.name = object.getString("name");
            this.minCost = object.getString("minCost");
            this.visual = object.getString("visual");
            JSONArray fields = object.getJSONArray("fields");
            for (int i=0; i<fields.length(); i++) {
                JSONObject fieldsObject = fields.getJSONObject(i);
                this.fieldGY = fieldsObject.getString("GY");
                this.fieldName = fieldsObject.getString("name");
                this.fieldType = fieldsObject.getString("fieldType");
                this.fieldData = fieldsObject.getString("data");
                this.fieldDefault = fieldsObject.getString("default");
                this.unit = fieldsObject.getString("unit");
            }

            JSONObject parent = object.getJSONObject("parent");
            if (parent!=null) {
                this.parentGY = parent.getString("GY");
                this.parentName = parent.getString("name");
                JSONObject parentObject = parent.getJSONObject("parent");
                new Generic(parentObject);
            }

        } catch (JSONException e) {
            e.printStackTrace();
        }

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

    public String getMinCost() {
        return minCost;
    }

    public void setMinCost(String minCost) {
        this.minCost = minCost;
    }

    public String getVisual() {
        return visual;
    }

    public void setVisual(String visual) {
        this.visual = visual;
    }

    public String getFieldGY() {
        return fieldGY;
    }

    public void setFieldGY(String fieldPk) {
        this.fieldGY = fieldGY;
    }

    public String getFieldName() {
        return fieldName;
    }

    public void setFieldName(String fieldName) {
        this.fieldName = fieldName;
    }

    public String getFieldType() {
        return fieldType;
    }

    public void setFieldType(String fieldType) {
        this.fieldType = fieldType;
    }

    public String getUnit() {
        return unit;
    }

    public void setUnit(String unit) {
        this.unit = unit;
    }

    public String getHelpText() {
        return helpText;
    }

    public void setHelpText(String helpText) {
        this.helpText = helpText;
    }

    public String getFieldData() {
        return fieldData;
    }

    public void setFieldData(String fieldData) {
        this.fieldData = fieldData;
    }

    public String getFieldDefault() {
        return fieldDefault;
    }

    public void setFieldDefault(String fieldDefault) {
        this.fieldDefault = fieldDefault;
    }

    public String getParentPGY() {
        return parentGY;
    }

    public void setParentGY(String parentGY) {
        this.parentGY = parentGY;
    }

    public String getParentName() {
        return parentName;
    }

    public void setParentName(String parentName) {
        this.parentName = parentName;
    }

    public String getParentMinCost() {
        return parentMinCost;
    }

    public void setParentMinCost(String parentMinCost) {
        this.parentMinCost = parentMinCost;
    }

    public String getParentVisual() {
        return parentVisual;
    }

    public void setParentVisual(String parentVisual) {
        this.parentVisual = parentVisual;
    }

    public JSONObject getObject() {
        return object;
    }

    public void setObject(JSONObject object) {
        this.object = object;
    }
}
