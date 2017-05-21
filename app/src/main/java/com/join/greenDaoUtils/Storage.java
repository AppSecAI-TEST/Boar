package com.join.greenDaoUtils;

import org.greenrobot.greendao.annotation.Entity;

/**
 *
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
    private int number; //公猪编号
    private String operator;
    private String type;  //精液类型
    private String density; //密度
    private String vitality; //活力
    private String motilityRate;//活率
    private String result;


}
