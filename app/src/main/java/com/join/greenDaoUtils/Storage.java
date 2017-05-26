package com.join.greenDaoUtils;

import org.greenrobot.greendao.annotation.Entity;
import org.greenrobot.greendao.annotation.Generated;

/**
 * id 不能自动增长(只能用long),
 *
 * @Unique 不能重复(唯一)
 */
@Entity
public class Storage {
    private String color;
    private String smell;
    private String date;
    private String time;
    private String number; //公猪编号
    private String operator;//操作员
    private String type;  //精液类型
    private String density; //密度
    private String vitality; //活力
    private String motilityRate;//活率
    private String copies;
    private String add;
    private String result;
    @Generated(hash = 46239288)
    public Storage(String color, String smell, String date, String time,
            String number, String operator, String type, String density,
            String vitality, String motilityRate, String copies, String add,
            String result) {
        this.color = color;
        this.smell = smell;
        this.date = date;
        this.time = time;
        this.number = number;
        this.operator = operator;
        this.type = type;
        this.density = density;
        this.vitality = vitality;
        this.motilityRate = motilityRate;
        this.copies = copies;
        this.add = add;
        this.result = result;
    }
    @Generated(hash = 2114225574)
    public Storage() {
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
    public String getNumber() {
        return this.number;
    }
    public void setNumber(String number) {
        this.number = number;
    }
    public String getOperator() {
        return this.operator;
    }
    public void setOperator(String operator) {
        this.operator = operator;
    }
    public String getType() {
        return this.type;
    }
    public void setType(String type) {
        this.type = type;
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
    public String getCopies() {
        return this.copies;
    }
    public void setCopies(String copies) {
        this.copies = copies;
    }
    public String getAdd() {
        return this.add;
    }
    public void setAdd(String add) {
        this.add = add;
    }
    public String getResult() {
        return this.result;
    }
    public void setResult(String result) {
        this.result = result;
    }

   
}
