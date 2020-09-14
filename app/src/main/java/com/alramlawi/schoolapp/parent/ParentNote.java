package com.alramlawi.schoolapp.parent;

public class ParentNote {

    private String type, title,target, textt;
    private int id;

    public ParentNote(int id, String title, String type , String target, String textt) {
        this.id = id;
        this.title = title;
        this.type = type;
        this.target = target;
        this.textt = textt;

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
}
