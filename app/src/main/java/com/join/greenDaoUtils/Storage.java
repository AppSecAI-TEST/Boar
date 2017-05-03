package com.join.greenDaoUtils;

import org.greenrobot.greendao.annotation.Entity;
import org.greenrobot.greendao.annotation.Id;
import org.greenrobot.greendao.annotation.Unique;
import org.greenrobot.greendao.annotation.Generated;

/**
 * Created by join on 2017/5/3.
 */
@Entity
public class Storage {
    //不能用int
    @Id(autoincrement = true)
    private Long id;
    @Unique
    private int number; //公猪编号
    private String date;
    private String time;
    private String type;  //精液类型
    private String color;
    private String smell;
    private String density; //密度
    private String vitality; //活力
    private String motilityRate;
    private String line;
    private String operator;
    private String result;
    @Generated(hash = 1642758981)
    public Storage(Long id, int number, String date, String time, String type,
            String color, String smell, String density, String vitality,
            String motilityRate, String line, String operator, String result) {
        this.id = id;
        this.number = number;
        this.date = date;
        this.time = time;
        this.type = type;
        this.color = color;
        this.smell = smell;
        this.density = density;
        this.vitality = vitality;
        this.motilityRate = motilityRate;
        this.line = line;
        this.operator = operator;
        this.result = result;
    }
    @Generated(hash = 2114225574)
    public Storage() {
    }
    public Long getId() {
        return this.id;
    }
    public void setId(Long id) {
        this.id = id;
    }
    public int getNumber() {
        return this.number;
    }
    public void setNumber(int number) {
        this.number = number;
    }
    public String getDate() {
        return this.date;
    }
    public void setDate(String date) {
        this.date = date;
    }
    public String getTime() {
        return this.time;
    }
    public void setTime(String time) {
        this.time = time;
    }
    public String getType() {
        return this.type;
    }
    public void setType(String type) {
        this.type = type;
    }
    public String getColor() {
        return this.color;
    }
    public void setColor(String color) {
        this.color = color;
    }
    public String getSmell() {
        return this.smell;
    }
    public void setSmell(String smell) {
        this.smell = smell;
    }
    public String getDensity() {
        return this.density;
    }
    public void setDensity(String density) {
        this.density = density;
    }
    public String getVitality() {
        return this.vitality;
    }
    public void setVitality(String vitality) {
        this.vitality = vitality;
    }
    public String getMotilityRate() {
        return this.motilityRate;
    }
    public void setMotilityRate(String motilityRate) {
        this.motilityRate = motilityRate;
    }
    public String getLine() {
        return this.line;
    }
    public void setLine(String line) {
        this.line = line;
    }
    public String getOperator() {
        return this.operator;
    }
    public void setOperator(String operator) {
        this.operator = operator;
    }
    public String getResult() {
        return this.result;
    }
    public void setResult(String result) {
        this.result = result;
    }
}
