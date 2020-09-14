package com.alramlawi.schoolapp.elements;

public class Approval {

    private String type, target, title, textt, url,state,req_state, date;
    private int id;

    public Approval(String type, String target, String title, String textt, String url, String state, String req_state, String date, int id) {
        this.type = type;
        this.target = target;
        this.title = title;
        this.textt = textt;
        this.url = url;
        this.state = state;
        this.req_state = req_state;
        this.date = date;
        this.id = id;
    }

    public Approval(int id, String title, String type, String target, String textt, String state, String url, String date) {
        this.id = id;
        this.title = title;
        this.target = target;
        this.type = type;
        this.textt = textt;
        this.state = state;
        this.url = url;
        this.date = date;

    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getState() {
        return state;
    }

    public void setState(String state) {
        this.state = state;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getTarget() {
        return target;
    }

    public void setTarget(String target) {
        this.target = target;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getTxt() {
        return textt;
    }

    public void setTxt(String txt) {
        this.textt = textt;
    }

    public String getReq_state() {
        return req_state;
    }

    public void setReq_state(String req_state) {
        this.req_state = req_state;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

}
