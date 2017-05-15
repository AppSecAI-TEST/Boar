package com.join.entity;

/**
 * Created by join on 2017/5/15.
 */

public class IDQuery {
    private String date;
    private String time;
    private String type;
    private String id;
    private String density;
    private String vitality;
    private String motilityRate;
    private String operator;
    private String result;
    private String check;

    public IDQuery(String date, String time, String type, String id, String density, String vitality,
                   String motilityRate, String operator, String result, String check) {
        this.date = date;
        this.time = time;
        this.type = type;
        this.id = id;
        this.density = density;
        this.vitality = vitality;
        this.motilityRate = motilityRate;
        this.operator = operator;
        this.result = result;
        this.check = check;
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

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
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

    public String getOperator() {
        return operator;
    }

    public void setOperator(String operator) {
        this.operator = operator;
    }

    public String getResult() {
        return result;
    }

    public void setResult(String result) {
        this.result = result;
    }

    public String getCheck() {
        return check;
    }

    public void setCheck(String check) {
        this.check = check;
    }
}
