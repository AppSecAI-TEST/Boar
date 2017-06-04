package com.join.entity;

import android.databinding.BaseObservable;
import android.databinding.Bindable;

import com.join.BR;

/**
 * Created by join on 2017/5/31.
 */

public class Result1 extends BaseObservable {
    private String density; //密度
    private String vitality; //活力
    private String motilityRate;//活率
    private String date;
    private String time;
    private String motileSperms;//有效精子数
    private String capacity;//容量
    private String operator;//操作员
    private String result;
    private String batch;//批号ID//nu
    private String color;
    private String smell;
    private String humidity;

    @Bindable
    public String getHumidity() {
        return humidity;
    }

    public void setHumidity(String humidity) {
        this.humidity = humidity;
        notifyPropertyChanged(BR.humidity);
    }

    @Bindable
    public String getBatch() {
        return batch;
    }

    public void setBatch(String batch) {
        this.batch = batch;
        notifyPropertyChanged(BR.batch);
    }

    @Bindable
    public String getColor() {
        return color;
    }

    public void setColor(String color) {
        this.color = color;
        notifyPropertyChanged(BR.color);
    }

    @Bindable
    public String getSmell() {
        return smell;
    }

    public void setSmell(String smell) {
        this.smell = smell;
        notifyPropertyChanged(BR.smell);
    }

    @Bindable
    public String getResult() {
        return result;
    }

    public void setResult(String result) {
        this.result = result;
        notifyPropertyChanged(BR.result);
    }

    @Bindable
    public String getDensity() {
        return density;
    }

    public void setDensity(String density) {
        this.density = density;
        notifyPropertyChanged(BR.density);
    }

    @Bindable
    public String getVitality() {
        return vitality;
    }

    public void setVitality(String vitality) {
        this.vitality = vitality;
        notifyPropertyChanged(BR.vitality);
    }

    @Bindable
    public String getMotilityRate() {
        return motilityRate;
    }

    public void setMotilityRate(String motilityRate) {
        this.motilityRate = motilityRate;
        notifyPropertyChanged(BR.motilityRate);
    }

    @Bindable
    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
        notifyPropertyChanged(BR.date);
    }

    @Bindable
    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
        notifyPropertyChanged(BR.time);
    }

    @Bindable
    public String getMotileSperms() {
        return motileSperms;
    }

    public void setMotileSperms(String motileSperms) {
        this.motileSperms = motileSperms;
        notifyPropertyChanged(BR.motileSperms);
    }

    @Bindable
    public String getCapacity() {
        return capacity;
    }

    public void setCapacity(String capacity) {
        this.capacity = capacity;
        notifyPropertyChanged(BR.capacity);
    }

    @Bindable
    public String getOperator() {
        return operator;
    }

    public void setOperator(String operator) {
        this.operator = operator;
        notifyPropertyChanged(BR.operator);
    }
}
