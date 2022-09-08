package com.ggexpress.gavin.entites;

import org.json.JSONException;
import org.json.JSONObject;

/**
 * Created by Gavin on 26/02/21.
 */

public class Frequently {
    public String GY, created, user, questions, answer;
    public JSONObject object;

    public Frequently(JSONObject object) throws JSONException {
        this.object = object;

        this.GY = object.getString("GY");
        this.created = object.getString("created");
        this.user = object.getString("user");
        this.questions = object.getString("ques");
        this.answer = object.getString("ans");
    }

    public String getPk() {
        return GY;
    }

    public void setPk(String pk) {
        this.GY = GY;
    }

    public String getCreated() {
        return created;
    }

    public void setCreated(String created) {
        this.created = created;
    }

    public String getUser() {
        return user;
    }

    public void setUser(String user) {
        this.user = user;
    }

    public String getQuestions() {
        return questions;
    }

    public void setQuestions(String questions) {
        this.questions = questions;
    }

    public String getAnswer() {
        return answer;
    }

    public void setAnswer(String answer) {
        this.answer = answer;
    }

    public JSONObject getObject() {
        return object;
    }

    public void setObject(JSONObject object) {
        this.object = object;
    }
}
