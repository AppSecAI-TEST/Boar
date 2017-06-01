package com.join.entity;

/**
 * Created by join on 2017/5/31.
 */

public class Result1 {
    private String density; //密度
    private String vitality; //活力
    private String motilityRate;//活率
    private String date;
    private String time;
    private String motileSperms;
    private String capacity;
    private String operator;//操作员
    private String result;

    public String getResult() {
        return result;
    }

    public void setResult(String result) {
        this.result = result;
    }

    public String getDensity() {
        return density;
    }

    public void setDensity(String density) {
        this.density = density;
    }

    public String getVitality() {
        return vitality;
    }

    public void setVitality(String vitality) {
        this.vitality = vitality;
    }

    public String getMotilityRate() {
        return motilityRate;
    }

    public void setMotilityRate(String motilityRate) {
        this.motilityRate = motilityRate;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }

    public String getMotileSperms() {
        return motileSperms;
    }

    public void setMotileSperms(String motileSperms) {
        this.motileSperms = motileSperms;
    }

    public String getCapacity() {
        return capacity;
    }

    public void setCapacity(String capacity) {
        this.capacity = capacity;
    }

    public String getOperator() {
        return operator;
    }

    public void setOperator(String operator) {
        this.operator = operator;
    }
}
