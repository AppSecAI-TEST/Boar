package com.join.entity;

import android.databinding.BaseObservable;
import android.databinding.Bindable;

import com.join.BR;

/**
 *
 */

public class StosteDetectionDiluentE extends BaseObservable {
    private String id_Gong_1;
    private String id_ml;
    private String date;
    private String time;
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
    public String getId_Gong_1() {
        return id_Gong_1;
    }

    public void setId_Gong_1(String id_Gong_1) {
        this.id_Gong_1 = id_Gong_1;
        notifyPropertyChanged(BR.id_Gong_1);
    }

    @Bindable
    public String getId_ml() {
        return id_ml;
    }

    public void setId_ml(String id_ml) {
        this.id_ml = id_ml;
        notifyPropertyChanged(BR.id_ml);
    }
}
