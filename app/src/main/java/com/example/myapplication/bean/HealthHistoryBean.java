package com.example.myapplication.bean;

import java.io.Serializable;
import java.security.interfaces.RSAMultiPrimePrivateCrtKey;

/**
 * @Description 记录信息的实体类
 */
public class HealthHistoryBean implements Serializable {
    private int id;
    private String name;
    private String time;
    private String location;
    private String tem;

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

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }

    public String getLocation() {
        return location;
    }

    public void setLocation(String location) {
        this.location = location;
    }

    public String getTem() {
        return tem;
    }

    public void setTem(String tem) {
        this.tem = tem;
    }
}
