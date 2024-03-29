package com.ggexpress.gavin.entites;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

/**
 * Created by Gavin on 29/02/21.
 */

public class Offer {
    public String GY, user, image, imagePortrait, created, level, title, subTitle;
    public String pageGY, pageCreated, pageUpdated, pageurl, body, pageTitle;
    boolean active;
    JSONObject jsonObject;

    public Offer() {
    }

    public Offer(JSONObject jsonObject) {
        this.jsonObject = jsonObject;

        try {
            this.GY = jsonObject.getString("GY");
            this.active = jsonObject.getBoolean("active");
            this.created = jsonObject.getString("created");
            this.image = jsonObject.getString("image");
            this.imagePortrait = jsonObject.getString("imagePortrait");
            this.level = jsonObject.getString("level");
            this.title = jsonObject.getString("title");
            this.subTitle = jsonObject.getString("subtitle");
//            this.state = jsonObject.getString("state");
            JSONObject page = jsonObject.getJSONObject("page");
            this.pageGY = page.getString("GY");
            this.pageCreated = page.getString("created");
            this.pageUpdated = page.getString("updated");
            this.pageTitle = page.getString("title");
            this.pageurl = page.getString("pageurl");
            this.body = page.getString("body");


        } catch (JSONException e) {
            e.printStackTrace();
        }


    }

    public String getPk() {
        return GY;
    }

    public void setPk(String pk) {
        this.GY = GY;
    }

    public String getUser() {
        return user;
    }

    public void setUser(String user) {
        this.user = user;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public String getImagePortrait() {
        return imagePortrait;
    }

    public void setImagePortrait(String imagePortrait) {
        this.imagePortrait = imagePortrait;
    }

    public String getCreated() {
        return created;
    }

    public void setCreated(String created) {
        this.created = created;
    }

    public String getLevel() {
        return level;
    }

    public void setLevel(String level) {
        this.level = level;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getSubTitle() {
        return subTitle;
    }

    public void setSubTitle(String subTitle) {
        this.subTitle = subTitle;
    }

    public boolean isActive() {
        return active;
    }

    public void setActive(boolean active) {
        this.active = active;
    }

    public String getPageGY() {
        return pageGY;
    }

    public void setPageGY(String pageGY) {
        this.pageGY = pageGY;
    }

    public String getPageCreated() {
        return pageCreated;
    }

    public void setPageCreated(String pageCreated) {
        this.pageCreated = pageCreated;
    }

    public String getPageUpdated() {
        return pageUpdated;
    }

    public void setPageUpdated(String pageUpdated) {
        this.pageUpdated = pageUpdated;
    }

    public String getPageurl() {
        return pageurl;
    }

    public void setPageurl(String pageurl) {
        this.pageurl = pageurl;
    }

    public String getBody() {
        return body;
    }

    public void setBody(String body) {
        this.body = body;
    }

    public String getPageTitle() {
        return pageTitle;
    }

    public void setPageTitle(String pageTitle) {
        this.pageTitle = pageTitle;
    }

    public JSONObject getJsonObject() {
        return jsonObject;
    }

    public void setJsonObject(JSONObject jsonObject) {
        this.jsonObject = jsonObject;
    }



}
