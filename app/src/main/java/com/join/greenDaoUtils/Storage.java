package com.join.greenDaoUtils;

import org.greenrobot.greendao.annotation.Entity;
import org.greenrobot.greendao.annotation.Id;
import org.greenrobot.greendao.annotation.Generated;

/**
 * id 不能自动增长(只能用long),
 *
 * @Unique 不能重复(唯一)
 */
@Entity
public class Storage {
    @Id(autoincrement = true)
    private Long id;
    private String color;
    private String smell;
    private String date;          //采精日期
    private String time;//采精时间
    private String checkoutDate;//检测日期
    private String checkoutTime;//检测时间
    private String number; //公猪编号/批号
    private String operator;//操作员
    private String type;  //精液类型
    private String density; //密度
    private String vitality; //活力
    private String motilityRate;//活率
    private String copies;    //分装份数(原液)
    private String add;         //需增加多少稀释精液(原液)
    private String result;//结果
    private String capacity; //每剂容量(稀释)
    private String milliliter; //采精量(原液)
    private String motileSperms;//有效精子数(稀释)
    @Generated(hash = 590371873)
    public Storage(Long id, String color, String smell, String date, String time,
            String checkoutDate, String checkoutTime, String number,
            String operator, String type, String density, String vitality,
            String motilityRate, String copies, String add, String result,
            String capacity, String milliliter, String motileSperms) {
        this.id = id;
        this.color = color;
        this.smell = smell;
        this.date = date;
        this.time = time;
        this.checkoutDate = checkoutDate;
        this.checkoutTime = checkoutTime;
        this.number = number;
        this.operator = operator;
        this.type = type;
        this.density = density;
        this.vitality = vitality;
        this.motilityRate = motilityRate;
        this.copies = copies;
        this.add = add;
        this.result = result;
        this.capacity = capacity;
        this.milliliter = milliliter;
        this.motileSperms = motileSperms;
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
    public String getCheckoutDate() {
        return this.checkoutDate;
    }
    public void setCheckoutDate(String checkoutDate) {
        this.checkoutDate = checkoutDate;
    }
    public String getCheckoutTime() {
        return this.checkoutTime;
    }
    public void setCheckoutTime(String checkoutTime) {
        this.checkoutTime = checkoutTime;
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
    public String getCapacity() {
        return this.capacity;
    }
    public void setCapacity(String capacity) {
        this.capacity = capacity;
    }
    public String getMilliliter() {
        return this.milliliter;
    }
    public void setMilliliter(String milliliter) {
        this.milliliter = milliliter;
    }
    public String getMotileSperms() {
        return this.motileSperms;
    }
    public void setMotileSperms(String motileSperms) {
        this.motileSperms = motileSperms;
    }

}
