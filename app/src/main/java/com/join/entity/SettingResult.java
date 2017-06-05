package com.join.entity;

import android.databinding.BaseObservable;
import android.databinding.Bindable;

import com.join.BR;

/**
 * Created by join on 2017/6/5.
 */

public class SettingResult extends BaseObservable {
    private String humidity;

    @Bindable
    public String getHumidity() {
        return humidity;
    }

    public void setHumidity(String humidity) {
        this.humidity = humidity;
        notifyPropertyChanged(BR.humidity);
    }
}
