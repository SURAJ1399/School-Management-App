package com.alramlawi.schoolapp.elements;

public class Request {

    private String name, request_txt;


    public Request(String name,String request_txt) {

        this.request_txt = request_txt;
        this.name = name;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getTxt() {
        return request_txt;
    }

    public void setTxt(String request_txt) {
        this.request_txt = request_txt;
    }
}
