package com.utflnx.whokonws.model;

import java.util.HashMap;
import java.util.List;

public class APIRequestModel {
    private String about;
    private String database;
    private String param;
    private HashMap<String, Object> table;

    public String getAbout() {
        return about;
    }

    public void setAbout(String about) {
        this.about = about;
    }

    public String getDatabase() {
        return database;
    }

    public void setDatabase(String database) {
        this.database = database;
    }

    public String getParam() {
        return param;
    }

    public void setParam(String param) {
        this.param = param;
    }

    public HashMap<String, Object> getTable() {
        return table;
    }

    public void setTable(HashMap<String, Object> table) {
        this.table = table;
    }
}
