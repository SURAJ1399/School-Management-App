package com.alramlawi.schoolapp.elements;

public class Student {

    private int id;
    private String name, password,parent,parPass,teacher,lastName,tableName;
    private int count_on;
    private int count_off;
    private boolean check;

    public Student(String name, String password) {
        this.name = name;
        this.password = password;
    }

    public Student(int id, String name, int count_on, int count_off) {
        this.id = id;
        this.name = name;
        this.count_on = count_on;
        this.count_off = count_off;
    }

    public Student(int id, String name, String password, int count_on, int count_off) {
        this.id = id;
        this.name = name;
        this.count_on = count_on;
        this.count_off = count_off;
        this.password = password;
    }

    public Student(int id, String name, String password, boolean check, int count_on, int count_off) {
        this.id = id;
        this.name = name;
        this.count_on = count_on;
        this.count_off = count_off;
        this.password = password;
        this.check = check;
    }

    public Student(int id, String name, String password, String parent, String parPass, String teacher, String lastName, String tableName, int count_on, int count_off) {
        this.id = id;
        this.name = name;
        this.password = password;
        this.parent = parent;
        this.parPass = parPass;
        this.teacher = teacher;
        this.lastName = lastName;
        this.tableName = tableName;
        this.count_on = count_on;
        this.count_off = count_off;
    }

    public String getParent() {
        return parent;
    }

    public void setParent(String parent) {
        this.parent = parent;
    }

    public String getParPass() {
        return parPass;
    }

    public void setParPass(String parPass) {
        this.parPass = parPass;
    }

    public String getTeacher() {
        return teacher;
    }

    public void setTeacher(String teacher) {
        this.teacher = teacher;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public String getTableName() {
        return tableName;
    }

    public void setTableName(String tableName) {
        this.tableName = tableName;
    }

    public String getPassword() {
        return password;
    }
    public void setPassword(String password) {
        this.password = password;
    }

    public int getCount_off() {
        return count_off;
    }
    public void setCount_off(int count_off) {
        this.count_off = count_off;
    }


    public int getId() {
        return id;
    }
    public void setId(int id) {
        this.id = id;
    }


    public String getName() {
        return name;
    }
    public void setName(String name) {
        this.name = name;
    }


    public int getCount_on() {
        return count_on;
    }
    public void setCount_on(int count_on) {
        this.count_on = count_on;
    }

    public boolean isCheck() {
        return check;
    }

    public void setCheck(boolean check) {
        this.check = check;
    }



}
